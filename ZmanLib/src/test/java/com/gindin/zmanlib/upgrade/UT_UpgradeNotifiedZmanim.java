/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.upgrade;

import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The {@code .notifiedZmanim} files written by the released app are Java-serialized {@link NotifiedZmanim}
 *  objects; the current class has to read them back with the same contents.
 */
public class UT_UpgradeNotifiedZmanim {

  @TempDir
  Path                                                      filesDir;

  private NotifiedZmanim.Manager                            manager;

  private Locale                                            savedLocale;


  @BeforeEach
  public void restoreFilesWrittenByReleasedApp() throws IOException {

    // Calendars are created for the default locale; make sure they are Gregorian.
    savedLocale = Locale.getDefault();
    Locale.setDefault( Locale.US );

    UpgradeFixtures.copyFilesDirectory( filesDir );

    manager = new NotifiedZmanim.Manager();
    manager.init( filesDir.toFile() );
  }


  @AfterEach
  public void restoreLocale() {
    Locale.setDefault( savedLocale );
  }


  @Test
  public void restoresTheZmanimNotifiedOnEachDay() {

    assertNotifiedExactly( day( 2026, Calendar.OCTOBER, 18 ),
      EnumSet.of( Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila, Zmanim.Type.Shkia ) );

    assertNotifiedExactly( day( 2026, Calendar.OCTOBER, 17 ),
      EnumSet.of( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.CandleLighting, Zmanim.Type.ShabbatEnd,
        Zmanim.Type.NightChatzot ) );
  }


  @Test
  public void restoresADayWithNothingNotified() {

    assertNotifiedExactly( day( 2026, Calendar.OCTOBER, 16 ), EnumSet.noneOf( Zmanim.Type.class ) );
  }


  /**
   * A file that cannot be read is replaced by an empty record for today's date, which would look just like a day with
   *  nothing notified. So check which dates the restored records are for.
   */
  @Test
  public void everyFileIsRestoredToTheRecordForItsOwnDay() throws ReflectiveOperationException {

    Field restoredField = NotifiedZmanim.Manager.class.getDeclaredField( "notified" );
    restoredField.setAccessible( true );
    List<?> restored = (List<?>)restoredField.get( manager );

    Set<String> days = new HashSet<>();
    for ( Object record : restored ) {
      days.add( intField( record, "year" ) + "-" + intField( record, "month" ) + "-" + intField( record, "date" ) );
    }

    Assertions.assertEquals( 3, restored.size() );
    Assertions.assertEquals( Set.of( "2026-9-16", "2026-9-17", "2026-9-18" ), days );
  }


  @Test
  public void asksForTheRestoredObjectRatherThanCreatingANewOne() {

    NotifiedZmanim restored = manager.get( day( 2026, Calendar.OCTOBER, 18 ) );

    // A second request for the same day must hand back what was restored, not a new, empty record.
    Assertions.assertSame( restored, manager.get( day( 2026, Calendar.OCTOBER, 18 ) ) );
    Assertions.assertTrue( restored.contains( Zmanim.Type.SofZmanShema ) );
  }


  @Test
  public void aRestoredRecordKeepsAccumulatingAndSavesUnderTheSameName() throws IOException {

    NotifiedZmanim restored = manager.get( day( 2026, Calendar.OCTOBER, 18 ) );
    restored.notified( Zmanim.Type.TzaitHacochavim );
    manager.save( restored );

    NotifiedZmanim.Manager reloaded = new NotifiedZmanim.Manager();
    reloaded.init( filesDir.toFile() );

    NotifiedZmanim afterSave = reloaded.get( day( 2026, Calendar.OCTOBER, 18 ) );
    Assertions.assertTrue( afterSave.contains( Zmanim.Type.TzaitHacochavim ) );
    Assertions.assertTrue( afterSave.contains( Zmanim.Type.Hanetz ) );
    Assertions.assertEquals( 3, filesDir.toFile().list( ( dir, name ) -> name.endsWith( ".notifiedZmanim" ) ).length );
  }


  private void assertNotifiedExactly( Calendar day, Set<Zmanim.Type> expected ) {

    NotifiedZmanim notifiedZmanim = manager.get( day );
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      Assertions.assertEquals( expected.contains( type ), notifiedZmanim.contains( type ), type.name() );
    }
  }


  private static int intField( Object record, String name ) throws ReflectiveOperationException {

    Field field = NotifiedZmanim.class.getDeclaredField( name );
    field.setAccessible( true );
    return field.getInt( record );
  }


  private static Calendar day( int year, int month, int date ) {

    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set( year, month, date );
    return calendar;
  }

}
