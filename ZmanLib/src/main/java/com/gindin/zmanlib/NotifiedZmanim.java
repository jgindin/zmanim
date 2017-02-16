package com.gindin.zmanlib;

import com.gindin.zmanlib.zman.Zmanim;

import java.io.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track of which zmanim we've already notified the user about for a given day.
 */
public final class NotifiedZmanim
  implements Serializable {

  private static final long                                 serialVersionUID = 8982487060417217170L;

  /** Name of the file created to store the notified zmanim. */
  private static final String                               NOTIFIED_ZMANIM_FILE_NAME = "notifiedZmanim";

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


  public boolean isForDate( final Calendar other ) {

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


  /**
   * @return true if no zmanim have been notified yet for the represented date.
   */
  public boolean isEmpty() {
    return notifiedTypes.isEmpty();
  }


  public boolean save( File filesDir ) {

    boolean saved;

    File storedData = new File( filesDir, NOTIFIED_ZMANIM_FILE_NAME );
    try {
      ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( storedData, false ) );
      oos.writeObject( this );
      oos.flush();
      oos.close();

      saved = true;
    }
    catch ( IOException e ) {
      // Urgh. Can't save... We're going to lose our state...
      //e.printStackTrace();
      saved = false;
    }

    return saved;
  }


  public static NotifiedZmanim restore( File filesDir ) {

    NotifiedZmanim restored;

    File storedData = new File( filesDir, NOTIFIED_ZMANIM_FILE_NAME );
    if ( !storedData.canRead() ) {
      restored = new NotifiedZmanim();
    }
    else {
      try {
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream( storedData ) );
        restored = (NotifiedZmanim)ois.readObject();

        ois.close();
      }
      catch ( Exception e ) {
        restored = new NotifiedZmanim();
      }
    }

    return restored;
  }

}
