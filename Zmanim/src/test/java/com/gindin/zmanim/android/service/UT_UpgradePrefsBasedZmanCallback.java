/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.upgrade.UpgradeFixtures;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * The notification offsets, zman selection, other personal preferences and notified-zmanim files written by the
 *  released app are read by {@link PrefsBasedZmanCallback}, which is what the notification code and the zmanim
 *  display consult.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_UpgradePrefsBasedZmanCallback {

  /**
   * How many minutes ahead of each zman the released app's user asked to be alerted. The second night candle
   *  lighting is not on the preferences screen, so nothing is stored for it and it reads the default of -1.
   */
  private static final Map<Zmanim.Type, Integer>            ALERT_MINUTES = new EnumMap<>( Zmanim.Type.class );

  /**
   * The zman the released app's user selected for each type. Candle Lighting and the second night candle lighting
   *  share the display name "Candle Lighting", which is the selection key, so both read what was stored last.
   */
  private static final Map<Zmanim.Type, String>             SELECTED_ZMAN = new EnumMap<>( Zmanim.Type.class );

  static {
    ALERT_MINUTES.put( Zmanim.Type.Alot, 50 );
    ALERT_MINUTES.put( Zmanim.Type.Talit, 30 );
    ALERT_MINUTES.put( Zmanim.Type.Hanetz, 5 );
    ALERT_MINUTES.put( Zmanim.Type.SofZmanShema, 45 );
    ALERT_MINUTES.put( Zmanim.Type.SofZmanTefila, 20 );
    ALERT_MINUTES.put( Zmanim.Type.SofZmanAchilatChametz, 10 );
    ALERT_MINUTES.put( Zmanim.Type.SofZmanBiurChametz, 25 );
    ALERT_MINUTES.put( Zmanim.Type.Chatzot, 60 );
    ALERT_MINUTES.put( Zmanim.Type.MinchaGedola, 3 );
    ALERT_MINUTES.put( Zmanim.Type.MinchaKetana, 35 );
    ALERT_MINUTES.put( Zmanim.Type.PlagHaMincha, 15 );
    ALERT_MINUTES.put( Zmanim.Type.CandleLighting, 40 );
    ALERT_MINUTES.put( Zmanim.Type.Shkia, 90 );
    ALERT_MINUTES.put( Zmanim.Type.BeinHashmashot, 8 );
    ALERT_MINUTES.put( Zmanim.Type.LatestMincha, 55 );
    ALERT_MINUTES.put( Zmanim.Type.FastEnd, 12 );
    ALERT_MINUTES.put( Zmanim.Type.TzaitHacochavim, 7 );
    ALERT_MINUTES.put( Zmanim.Type.ShabbatEnd, 18 );
    ALERT_MINUTES.put( Zmanim.Type.YomTovSheniCandleLighting, -1 );
    ALERT_MINUTES.put( Zmanim.Type.NightChatzot, 120 );

    SELECTED_ZMAN.put( Zmanim.Type.Alot, "GR'A 96 Minutes Zmaniyot" );
    SELECTED_ZMAN.put( Zmanim.Type.Talit, "11.5 deg" );
    SELECTED_ZMAN.put( Zmanim.Type.Hanetz, "Sunrise (with elevation)" );
    SELECTED_ZMAN.put( Zmanim.Type.SofZmanShema, " Rav Moshe Feinstein - GR'A half day" );
    SELECTED_ZMAN.put( Zmanim.Type.SofZmanTefila, " Rav Moshe Feinstein - GR'A half day" );
    SELECTED_ZMAN.put( Zmanim.Type.SofZmanAchilatChametz, "Baal HaTanya" );
    SELECTED_ZMAN.put( Zmanim.Type.SofZmanBiurChametz, "Baal HaTanya" );
    SELECTED_ZMAN.put( Zmanim.Type.Chatzot, "Fixed Local Mid-Day" );
    SELECTED_ZMAN.put( Zmanim.Type.MinchaGedola, "Rabbi Yaakov Moshe Hillel" );
    SELECTED_ZMAN.put( Zmanim.Type.MinchaKetana, " Rav Moshe Feinstein - GR'A half day" );
    SELECTED_ZMAN.put( Zmanim.Type.PlagHaMincha, " Rav Moshe Feinstein - GR'A half day" );
    SELECTED_ZMAN.put( Zmanim.Type.CandleLighting, "Rabbi Moshe Feinstein" );
    SELECTED_ZMAN.put( Zmanim.Type.Shkia, "Sunset (with elevation)" );
    SELECTED_ZMAN.put( Zmanim.Type.LatestMincha, "Hacham Ben Sion" );
    SELECTED_ZMAN.put( Zmanim.Type.BeinHashmashot, "13.5 Minutes" );
    SELECTED_ZMAN.put( Zmanim.Type.FastEnd, "Seattle Sephardic" );
    SELECTED_ZMAN.put( Zmanim.Type.TzaitHacochavim, "Seattle Sephardic" );
    SELECTED_ZMAN.put( Zmanim.Type.ShabbatEnd, "Rabbi Moshe Feinstein" );
    SELECTED_ZMAN.put( Zmanim.Type.YomTovSheniCandleLighting, "Rabbi Moshe Feinstein" );
    SELECTED_ZMAN.put( Zmanim.Type.NightChatzot, "Fixed Local Mid-Night" );
  }


  private Context                                           context;

  private PrefsBasedZmanCallback                            callback;

  private Locale                                            savedLocale;


  @Before
  public void startOverStateWrittenByReleasedApp() throws IOException {

    // Calendars are created for the default locale; make sure they are Gregorian.
    savedLocale = Locale.getDefault();
    Locale.setDefault( Locale.US );

    context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );

    callback = new PrefsBasedZmanCallback( context ) {
      @Override
      public void scheduleAlert( Zman zman, ZmanimLocation location, Calendar alertAt ) {
        // Not exercised: only the reading of saved state is under test.
      }
    };
  }


  @After
  public void restoreLocale() {
    Locale.setDefault( savedLocale );
  }


  @Test
  public void everyTypeIsCoveredByTheExpectations() {

    Assert.assertEquals( Set.of( Zmanim.Type.values() ), ALERT_MINUTES.keySet() );
    Assert.assertEquals( Set.of( Zmanim.Type.values() ), SELECTED_ZMAN.keySet() );
  }


  @Test
  public void alertMinutesAreReadForEveryType() {

    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      Assert.assertEquals( type.name(), (int)ALERT_MINUTES.get( type ), callback.howManyMinutesToAlertBefore( type ) );
    }
  }


  @Test
  public void everyStoredPreferenceKeyIsAZmanimType() {

    Set<String> constantNames = new HashSet<>();
    Set<String> displayNames = new HashSet<>();
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      constantNames.add( type.name() );
      displayNames.add( type.displayName );
    }

    Set<String> booleanKeys = Set.of(
      PersonalPreferences.Prefs.SHOW_SECONDS.name(),
      PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(),
      PersonalPreferences.Prefs.ENABLE_NOTIFICATIONS_ON_SHABBAT.name(),
      PersonalPreferences.Prefs.USE_ALARM_CLOCK.name() );

    Set<String> personalKeys = new HashSet<>( preferences( ZmanimPrefs.PERSONAL ).getAll().keySet() );
    Assert.assertTrue( personalKeys.containsAll( booleanKeys ) );
    personalKeys.removeAll( booleanKeys );
    Assert.assertTrue( "unknown personal preference keys: " + personalKeys, constantNames.containsAll( personalKeys ) );

    // Every type that the released app lets the user pick a zman for has a stored selection.
    Assert.assertEquals( displayNames, preferences( ZmanimPrefs.ZMANIM_SELECTION ).getAll().keySet() );
  }


  @Test
  public void selectedZmanIsReadForEveryType() {

    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      Assert.assertEquals( type.name(), SELECTED_ZMAN.get( type ), callback.getPreferredZman( type ) );
    }
  }


  @Test
  public void selectedZmanNamesStillIdentifyTheZmanimOfEachType() {

    HebrewDate date = HebrewCalendar.getForDate( 2026, Calendar.OCTOBER, 18 );
    ZmanimLocation albany = new ZmanimLocation.Builder()
      .atLatitude( 42.6526 )
      .atLongitude( -73.7562 )
      .atElevation( 47.0 )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .locationName( "Albany, New York" )
      .build();

    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      Zmanim zmanim = ZmanimFactory.get( albany, date, type );
      Zman selected = zmanim.getByName( callback.getPreferredZman( type ) );

      if ( type == Zmanim.Type.CandleLighting ) {
        // The stored name was chosen from the second night candle lighting's list, so it means nothing here and
        //  the default is shown.
        Assert.assertEquals( zmanim.getDefault().name, selected.name );
      }
      else {
        Assert.assertEquals( type.name(), SELECTED_ZMAN.get( type ), selected.name );
        Assert.assertNotEquals( type.name(), zmanim.getDefault().name, selected.name );
      }
    }
  }


  @Test
  public void otherPersonalPreferencesAreRead() {

    Assert.assertTrue( callback.formatTimeWithSeconds() );
    Assert.assertFalse( callback.shouldShowAnyNotifications() );
    Assert.assertTrue( callback.shouldNotifyOnShabbat() );
    Assert.assertTrue( preferences( ZmanimPrefs.PERSONAL ).getBoolean( PersonalPreferences.Prefs.USE_ALARM_CLOCK.name(), false ) );
    Assert.assertNull( callback.getNotificationTone() );
  }


  @Test
  public void notifiedZmanimAreRestoredFromTheirFiles() {

    Calendar sunday = Calendar.getInstance();
    sunday.clear();
    sunday.set( 2026, Calendar.OCTOBER, 18 );

    Assert.assertTrue( callback.hasBeenNotified( sunday, Zmanim.Type.Hanetz ) );
    Assert.assertTrue( callback.hasBeenNotified( sunday, Zmanim.Type.SofZmanShema ) );
    Assert.assertTrue( callback.hasBeenNotified( sunday, Zmanim.Type.SofZmanTefila ) );
    Assert.assertTrue( callback.hasBeenNotified( sunday, Zmanim.Type.Shkia ) );
    Assert.assertFalse( callback.hasBeenNotified( sunday, Zmanim.Type.Talit ) );
    Assert.assertFalse( callback.hasBeenNotified( sunday, Zmanim.Type.TzaitHacochavim ) );

    Calendar saturday = Calendar.getInstance();
    saturday.clear();
    saturday.set( 2026, Calendar.OCTOBER, 17 );

    Assert.assertTrue( callback.hasBeenNotified( saturday, Zmanim.Type.ShabbatEnd ) );
    Assert.assertTrue( callback.hasBeenNotified( saturday, Zmanim.Type.CandleLighting ) );
    Assert.assertFalse( callback.hasBeenNotified( saturday, Zmanim.Type.Shkia ) );
  }


  private SharedPreferences preferences( ZmanimPrefs prefs ) {
    return context.getSharedPreferences( prefs.name(), Context.MODE_PRIVATE );
  }

}
