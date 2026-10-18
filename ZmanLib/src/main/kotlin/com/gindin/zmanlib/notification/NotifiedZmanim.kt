package com.gindin.zmanlib.notification;

import com.gindin.zmanlib.zman.Zmanim;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * Keeps track of which zmanim we've already notified the user about for a given day.
 */
public final class NotifiedZmanim
  implements Serializable {

  public static class Manager {

    /** Name of the file created to store the notified zmanim. */
    private static final String                             NOTIFIED_ZMANIM_FILE_EXTENSION = "notifiedZmanim";


    private final List<NotifiedZmanim>                      notified = new ArrayList<>();

    private File                                            filesDir;


    public void init( File filesDir ) {

      this.filesDir = filesDir;
      final File[] files = filesDir.listFiles( new FileFilter() {
        @Override
        public boolean accept( File pathname ) {
          return pathname.getName().endsWith( NOTIFIED_ZMANIM_FILE_EXTENSION );
        }
      } );


      if ( ( files == null ) || files.length == 0 ) {
        return;
      }

      // Clean up old files. Or read it in if it's not too old.
      Calendar oldestToKeep = Calendar.getInstance();
      oldestToKeep.add( Calendar.DATE, -3 );
      final long mills = oldestToKeep.getTimeInMillis();

      for ( File file : files ) {
        if ( file.lastModified() < mills ) {
          //noinspection ResultOfMethodCallIgnored
          file.delete();
        }
        else {
          restore( file );
        }
      }
    }


    @NotNull
    public NotifiedZmanim get( Calendar forDate ) {

      NotifiedZmanim newNotifiedZmanim;
      
      synchronized ( notified ) {
        for ( NotifiedZmanim notifiedZmanim : notified ) {
          if ( notifiedZmanim.isForDate( forDate ) ) {
            return notifiedZmanim;
          }
        }

        newNotifiedZmanim = new NotifiedZmanim( forDate );
        notified.add( newNotifiedZmanim );
      }

      return newNotifiedZmanim;
    }


    public void save( NotifiedZmanim notifiedZmanim ) {

      String fileName = notifiedZmanim.year + "_" + notifiedZmanim.month + "_" + notifiedZmanim.date + "." + NOTIFIED_ZMANIM_FILE_EXTENSION;
      File storedData = new File( filesDir, fileName );
      try {
        ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( storedData, false ) );
        oos.writeObject( notifiedZmanim );
        oos.flush();
        oos.close();

      }
      catch ( Exception e ) {
        // Urgh. Can't save... We're going to lose our state...
        //e.printStackTrace();
      }
    }


    private void restore( File storedData ) {

      NotifiedZmanim restored;

      if ( !storedData.canRead() ) {
        restored = new NotifiedZmanim();
      }
      else {
        ObjectInputStream ois = null;
        try {
          ois = new ObjectInputStream( new FileInputStream( storedData ) );
          restored = (NotifiedZmanim)ois.readObject();

          ois.close();
        }
        catch ( Throwable e ) {
          if ( ois != null ) {
            try {
              ois.close();
            }
            catch ( Throwable e1 ) {
              // Oh well, we tried...
            }
          }
          restored = new NotifiedZmanim();
        }
      }

      synchronized ( notified ) {
        notified.add( restored );
      }
    }

  }


  public static final Manager                               manager = new Manager();


  private static final long                                 serialVersionUID = 8982487060417217170L;

  /** The date this applies to. */
  private final int                                         year;
  private final int                                         month;
  private final int                                         date;

  /** Which of the zmanim types have already been notified? */
  private final Set<Zmanim.Type>                            notifiedTypes = new HashSet<>();


  public NotifiedZmanim(
    final Calendar day
  ) {

    this.year = day.get( Calendar.YEAR );
    this.month = day.get( Calendar.MONTH );
    this.date = day.get( Calendar.DATE );
  }


  private NotifiedZmanim() {

    final Calendar day = Calendar.getInstance();
    this.year = day.get( Calendar.YEAR );
    this.month = day.get( Calendar.MONTH );
    this.date = day.get( Calendar.DATE );
  }


  boolean isForDate( final Calendar other ) {

    int otherYear = other.get( Calendar.YEAR );
    if ( year != otherYear ) {
      return false;
    }

    int otherMonth = other.get( Calendar.MONTH );
    if ( month != otherMonth ) {
      return false;
    }

    int otherDate = other.get( Calendar.DATE );
    return date == otherDate;
  }


  /**
   * Once the user has been notified about the specified type, then this method is invoked to note that notification.
   */
  public void notified(
    final Zmanim.Type type
  ) {

    notifiedTypes.add( type );
  }


  public boolean contains( final Zmanim.Type type ) {
    return notifiedTypes.contains( type );
  }

}
