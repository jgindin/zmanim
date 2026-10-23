/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zmanim;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Reads back the {@code .notifiedZmanim} files the released version 409 app wrote, with the code of the build under
 *  test. Those files are Java-serialized {@link NotifiedZmanim} objects, so the class name, serialVersionUID and field
 *  names of {@link NotifiedZmanim}, and the name of the {@link Zmanim.Type} enum in its set, must be exactly what they
 *  are in the source. In a minified build only the keep rules in proguard-rules.pro make that so; run against the
 *  {@code releaseCheck} build type, this is the test that fails when one of those rules is lost.
 *<p/>
 *
 * The expected sets are the ones the JVM upgrade tests (UT_UpgradeNotifiedZmanim) check for the same files.
 */
@RunWith( AndroidJUnit4.class )
public class NotifiedZmanimRestoreTest {

  @Before
  public void installReleasedState() throws Exception {
    AppState.installUpgradeState();
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  /**
   * The app's restore path turns any unreadable file into an empty record for today, so a broken format would only
   *  show up as "nothing notified". Reading the files directly reports the actual cause, e.g. a
   *  {@code ClassNotFoundException} naming the class whose name R8 changed.
   */
  @Test
  public void everySavedFileDeserializesAsANotifiedZmanim() throws Exception {

    File[] files = filesDir().listFiles( ( dir, name ) -> name.endsWith( ".notifiedZmanim" ) );
    assertEquals( "fixture .notifiedZmanim files installed", 3, files.length );

    for ( File file : files ) {
      try ( ObjectInputStream in = new ObjectInputStream( new FileInputStream( file ) ) ) {
        Object restored = in.readObject();
        assertTrue( file.getName() + " restored as " + restored.getClass(), restored instanceof NotifiedZmanim );
      }
    }
  }


  @Test
  public void theAppRestoresTheZmanimNotifiedOnEachDay() {

    NotifiedZmanim.Manager manager = new NotifiedZmanim.Manager();
    manager.init( filesDir() );

    assertNotifiedExactly( manager, day( 2026, Calendar.OCTOBER, 18 ),
      EnumSet.of( Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila, Zmanim.Type.Shkia ) );

    assertNotifiedExactly( manager, day( 2026, Calendar.OCTOBER, 17 ),
      EnumSet.of( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.CandleLighting, Zmanim.Type.ShabbatEnd,
        Zmanim.Type.NightChatzot ) );

    assertNotifiedExactly( manager, day( 2026, Calendar.OCTOBER, 16 ), EnumSet.noneOf( Zmanim.Type.class ) );
  }


  private static void assertNotifiedExactly(
    NotifiedZmanim.Manager  manager,
    Calendar                day,
    Set<Zmanim.Type>        expected
  ) {

    NotifiedZmanim notifiedZmanim = manager.get( day );
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      assertEquals( type.name() + " on " + day.getTime(), expected.contains( type ), notifiedZmanim.contains( type ) );
    }
  }


  private static Calendar day( int year, int month, int date ) {
    return new GregorianCalendar( year, month, date );
  }


  private static File filesDir() {
    return InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir();
  }

}
