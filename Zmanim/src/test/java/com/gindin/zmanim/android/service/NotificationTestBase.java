/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowNotificationManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Drives the real notification pipeline in a Robolectric environment: {@link ZmanimService} schedules the alarms
 *  for the upcoming zmanim into the (shadowed) {@link AlarmManager}, and the alarm's intent is delivered to
 *  {@link DisplayNotificationService}, which posts into the (shadowed) {@link NotificationManager}.
 * <p>
 * The preferences are written into the same shared preferences files the app reads.
 */
abstract class NotificationTestBase {

  protected static final TimeZone                           NEW_YORK_ZONE = TimeZone.getTimeZone( "America/New_York" );

  protected static final ZmanimLocation                     ALBANY = new ZmanimLocation.Builder()
    .locationName( "Albany, New York" )
    .atLatitude( 42.6526 )
    .atLongitude( -73.7562 )
    .atElevation( 0 )
    .inTimeZone( NEW_YORK_ZONE )
    .at( 0L )
    .build();

  protected static final long                               MINUTE_MS = 60 * 1000;

  protected Context                                         context;

  protected AlarmManager                                    alarmManager;

  protected NotificationManager                             notificationManager;

  private Locale                                            savedLocale;

  private ServiceController<ZmanimService>                  serviceController;


  @Before
  public final void startWithNoStateFromEarlierTests() throws Exception {

    // Calendars are created for the default locale; make sure they are Gregorian.
    savedLocale = Locale.getDefault();
    Locale.setDefault( Locale.US );

    context = ApplicationProvider.getApplicationContext();
    alarmManager = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );

    // The notified-zmanim bookkeeping is process-wide, and so outlives the Robolectric application of a test.
    clearNotifiedZmanim();
    ZmanimFactory.useElevation( false );

    // The shadow alarm manager refuses exact alarms unless told otherwise, as the system does for an app that has
    //  not been granted the permission.
    ShadowAlarmManager.setCanScheduleExactAlarms( true );
  }


  @After
  public final void stopTheServiceAndRestoreTheLocale() throws Exception {

    if ( serviceController != null ) {
      serviceController.destroy();
      serviceController = null;
    }

    clearNotifiedZmanim();
    Locale.setDefault( savedLocale );
  }


  /**
   * Writes the preferences the notification code consults. {@code alertMinutes} maps a zman type to how many
   *  minutes before the zman the user wants to be alerted; a type that is not in the map keeps the "no alert"
   *  default.
   */
  protected final void givenPreferences(
    boolean                     showNotifications,
    boolean                     notifyOnShabbat,
    boolean                     useAlarmClock,
    Map<Zmanim.Type, Integer>   alertMinutes
  ) {

    SharedPreferences.Editor editor = context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE ).edit();
    editor.putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), showNotifications );
    editor.putBoolean( PersonalPreferences.Prefs.ENABLE_NOTIFICATIONS_ON_SHABBAT.name(), notifyOnShabbat );
    editor.putBoolean( PersonalPreferences.Prefs.USE_ALARM_CLOCK.name(), useAlarmClock );
    for ( Map.Entry<Zmanim.Type, Integer> entry : alertMinutes.entrySet() ) {
      editor.putInt( entry.getKey().name(), entry.getValue() );
    }
    Assert.assertTrue( editor.commit() );
  }


  /** A calendar for the specified wall-clock time in the specified zone. */
  protected static Calendar timeIs(
    TimeZone  zone,
    int       year,
    int       month,
    int       day,
    int       hour,
    int       minute
  ) {

    Calendar calendar = Calendar.getInstance( zone );
    calendar.clear();
    calendar.set( year, month, day, hour, minute, 0 );
    return calendar;
  }


  /** Starts the real {@link ZmanimService}, which creates the notification channel. */
  protected final void startService() {
    serviceController = Robolectric.buildService( ZmanimService.class ).create();
  }


  /**
   * Has the started service's real scheduler work out and schedule the alarms for the zmanim after {@code now}, just
   *  as the service does when it receives a location, except that the time is the one specified. The system clock
   *  cannot be set back to a fixed date in a Robolectric test, and the service itself always takes the time from it.
   */
  protected final void scheduleAlarmsAsOf(
    ZmanimLocation  location,
    Calendar        now
  ) throws Exception {

    Field schedulerField = ZmanimService.class.getDeclaredField( "zmanNotificationScheduler" );
    schedulerField.setAccessible( true );
    ZmanNotificationScheduler scheduler = (ZmanNotificationScheduler)schedulerField.get( serviceController.get() );
    Assert.assertNotNull( "The service creates its scheduler when it is created", scheduler );

    scheduler.run( location, now );
  }


  /** Delivers the location to the started service the way location acquisition does. */
  protected final void acquireLocation( ZmanimLocation location ) {
    ZmanimService.ZMANIM_EVENT_BUS.fire( new LocationAcquiredEvent( location ) );
  }


  protected final List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms() {
    return Shadows.shadowOf( alarmManager ).getScheduledAlarms();
  }


  /** Delivers the alarm's intent to the receiver registered for it, as the alarm going off does. */
  protected final void fire( ShadowAlarmManager.ScheduledAlarm alarm ) {

    Shadows.shadowOf( alarmManager ).fireAlarm( alarm );
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  protected final ShadowNotificationManager shadowNotifications() {
    return Shadows.shadowOf( notificationManager );
  }


  protected static Zman defaultZman(
    ZmanimLocation  location,
    int             year,
    int             month,
    int             day,
    Zmanim.Type     type
  ) {
    return ZmanimFactory.get( location, HebrewCalendar.getForDate( year, month, day ), type ).getDefault();
  }


  /** Forgets the notified-zman records held in memory, as happens when the app's process is started afresh. */
  protected final void forgetNotifiedZmanimInMemory() throws Exception {

    Field notified = NotifiedZmanim.Manager.class.getDeclaredField( "notified" );
    notified.setAccessible( true );
    ( (List<?>)notified.get( NotifiedZmanim.manager ) ).clear();
  }


  /** Forgets every notified-zman record, in memory and on disk. */
  private void clearNotifiedZmanim() throws Exception {

    forgetNotifiedZmanimInMemory();

    File[] files = context.getFilesDir().listFiles();
    if ( files != null ) {
      for ( File file : files ) {
        if ( file.getName().endsWith( ".notifiedZmanim" ) ) {
          Assert.assertTrue( file.delete() );
        }
      }
    }
  }
}
