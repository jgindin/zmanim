/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.upgrade;

import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Set;

/**
 * The {@code .notifiedZmanim} files written by the released app are Java-serialized {@link NotifiedZmanim}
 *  objects; the current class has to read them back with the same contents.
 */
public class UT_UpgradeNotifiedZmanim {

  @TempDir
  Path                                                      filesDir;

  private NotifiedZmanim.Manager                            manager;


  @BeforeEach
  public void restoreFilesWrittenByReleasedApp() throws IOException {

    UpgradeFixtures.copyFilesDirectory( filesDir );

    manager = new NotifiedZmanim.Manager();
    manager.init( filesDir.toFile() );
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


  private static Calendar day( int year, int month, int date ) {

    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set( year, month, date );
    return calendar;
  }

}
