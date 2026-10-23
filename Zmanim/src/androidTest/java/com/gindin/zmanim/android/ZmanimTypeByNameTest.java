/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.DisplayNotificationService;
import com.gindin.zmanim.android.service.PrefsBasedZmanCallback;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * {@link Zmanim.Type} constants leave the process as strings and come back by name: the personal preferences are keyed
 *  by {@code name()}, the zman choices by {@code displayName}, and the notification alarm carries {@code name()} in an
 *  Intent extra that {@code Enum.valueOf} turns back into the constant. The saved strings were written by the released
 *  app, so they are the source names; this checks that the build under test still maps them to the same constants,
 *  through the app's own readers.
 */
@RunWith( AndroidJUnit4.class )
public class ZmanimTypeByNameTest {

  /** Lead times the released app saved in PERSONAL.xml, keyed by the constant's name. */
  private static final Map<Zmanim.Type, Integer>            SAVED_LEAD_TIMES = new EnumMap<>( Zmanim.Type.class );

  static {
    SAVED_LEAD_TIMES.put( Zmanim.Type.Alot, 50 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.Talit, 30 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.Hanetz, 5 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.SofZmanShema, 45 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.SofZmanTefila, 20 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.SofZmanAchilatChametz, 10 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.SofZmanBiurChametz, 25 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.Chatzot, 60 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.MinchaGedola, 3 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.MinchaKetana, 35 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.PlagHaMincha, 15 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.CandleLighting, 40 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.Shkia, 90 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.LatestMincha, 55 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.BeinHashmashot, 8 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.FastEnd, 12 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.TzaitHacochavim, 7 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.ShabbatEnd, 18 );
    SAVED_LEAD_TIMES.put( Zmanim.Type.NightChatzot, 120 );
  }

  private static final ZmanimLocation                       ALBANY = new ZmanimLocation.Builder()
    .locationName( "Albany, New York" )
    .atLatitude( 42.6526 )
    .atLongitude( -73.7562 )
    .atElevation( 0 )
    .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
    .at( 0L )
    .build();


  @Before
  public void installReleasedState() throws Exception {

    AppState.installUpgradeState();

    // The alarm path below is about the Intent, not the notification it would post.
    AppState.edit( ZmanimPrefs.PERSONAL )
      .putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false )
      .commit();
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void preferencesSavedUnderTheConstantNamesAreRead() {

    PrefsBasedZmanCallback callback = new PrefsBasedZmanCallback( targetContext() ) {
      @Override
      public void scheduleAlert( Zman zman, ZmanimLocation location, Calendar alertAt ) {
      }
    };

    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      int expected = SAVED_LEAD_TIMES.containsKey( type ) ? SAVED_LEAD_TIMES.get( type ) : -1;
      assertEquals( "lead time saved for " + type.name(), expected, callback.howManyMinutesToAlertBefore( type ) );
    }

    assertEquals( "Seattle Sephardic", callback.getPreferredZman( Zmanim.Type.FastEnd ) );
    assertEquals( "Rabbi Moshe Feinstein", callback.getPreferredZman( Zmanim.Type.ShabbatEnd ) );
    assertEquals( "Fixed Local Mid-Night", callback.getPreferredZman( Zmanim.Type.NightChatzot ) );
    assertEquals( "GR'A 96 Minutes Zmaniyot", callback.getPreferredZman( Zmanim.Type.Alot ) );
  }


  /**
   * Delivers the alarm Intent for each type to the receiver that handles it, the way AlarmManager would, and checks
   *  that the receiver recorded the zman it named as notified.
   */
  @Test
  public void theAlarmIntentNamesTheZmanItIsFor() {

    Context context = targetContext();

    // A day far from any real use of the app, so that no earlier record for it exists in this process.
    Calendar alertAt = Calendar.getInstance( TimeZone.getTimeZone( "America/New_York" ) );
    alertAt.clear();
    alertAt.set( 2031, Calendar.MARCH, 12, 12, 0 );

    int delivered = 0;
    for ( Zmanim.Type type : Zmanim.Type.values() ) {

      Zman zman = ZmanimFactory.get( ALBANY, HebrewCalendar.getForDate( 2031, Calendar.MARCH, 12 ), type ).getDefault();
      if ( ( zman == null ) || ( zman.getTime() == null ) ) {
        continue;
      }

      assertFalse( type.name() + " already recorded", NotifiedZmanim.manager.get( alertAt ).contains( type ) );

      Intent intent = DisplayNotificationService.createIntentForService( context, zman, ALBANY, alertAt );
      new DisplayNotificationService().onReceive( context, intent );

      assertTrue( type.name() + " recorded as notified", NotifiedZmanim.manager.get( alertAt ).contains( type ) );
      delivered++;
    }

    assertTrue( "only " + delivered + " zmanim exist on the test day", delivered >= 10 );
  }


  private static Context targetContext() {
    return InstrumentationRegistry.getInstrumentation().getTargetContext();
  }

}
