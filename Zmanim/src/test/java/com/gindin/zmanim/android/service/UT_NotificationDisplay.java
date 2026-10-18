/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import android.app.Notification;
import android.content.Intent;
import android.os.Looper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * An alarm scheduled by the real {@link ZmanimService} goes off, and the real {@link DisplayNotificationService}
 *  decides whether the user gets a notification and, if so, posts it to the {@link android.app.NotificationManager}.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_NotificationDisplay extends NotificationTestBase {

  /** An ordinary Tuesday. */
  private static final int                                  YEAR = 2026;
  private static final int                                  MONTH = Calendar.AUGUST;
  private static final int                                  DAY = 4;

  /** The Shabbat after that Tuesday, and the Friday before it. */
  private static final int                                  SHABBAT_DAY = 8;
  private static final int                                  EREV_SHABBAT_DAY = 7;


  @Test
  public void theAlarmForAZmanPostsANotificationNamingTheZmanAndItsTime() throws Exception {

    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, true, false, false );

    List<Notification> posted = shadowNotifications().getAllNotifications();
    Assert.assertEquals( 1, posted.size() );

    Notification notification = posted.get( 0 );
    Assert.assertSame( notification, shadowNotifications().getNotification( Zmanim.Type.Shkia.ordinal() ) );
    Assert.assertEquals( DisplayNotificationService.NOTIFICATION_CHANNEL_ID, notification.getChannelId() );
    Assert.assertEquals( "Zmanim", notification.extras.getString( Notification.EXTRA_TITLE ) );
    Assert.assertEquals( "Shkia at 8:11 PM.", withPlainSpaces( notification.extras.getString( Notification.EXTRA_TEXT ) ) );
    Assert.assertEquals( "Albany, New York", notification.extras.getString( Notification.EXTRA_SUB_TEXT ) );
    Assert.assertEquals( Notification.CATEGORY_REMINDER, notification.category );
    Assert.assertEquals( Notification.VISIBILITY_PUBLIC, notification.visibility );
    Assert.assertTrue( ( notification.flags & Notification.FLAG_AUTO_CANCEL ) != 0 );
  }


  @Test
  public void tappingTheNotificationOpensTheApp() throws Exception {

    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, true, false, false );

    Notification notification = shadowNotifications().getNotification( Zmanim.Type.Shkia.ordinal() );
    Intent opened = Shadows.shadowOf( notification.contentIntent ).getSavedIntent();
    Assert.assertEquals( ZmanimActivity.class.getName(), opened.getComponent().getClassName() );
  }


  @Test
  public void eachZmanGetsItsOwnNotificationSoTheyStack() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.PlagHaMincha, 30 );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    givenPreferences( true, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );
    fireAll();

    Assert.assertEquals( 2, shadowNotifications().getAllNotifications().size() );
    Assert.assertNotNull( shadowNotifications().getNotification( Zmanim.Type.PlagHaMincha.ordinal() ) );
    Assert.assertNotNull( shadowNotifications().getNotification( Zmanim.Type.Shkia.ordinal() ) );
  }


  @Test
  public void nothingIsPostedWhenNotificationsAreTurnedOff() throws Exception {

    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, false, false, false );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void nothingIsPostedOnShabbatUnlessTheUserAsksForIt() throws Exception {

    assertShabbat( SHABBAT_DAY );

    scheduleAndFire( SHABBAT_DAY, Zmanim.Type.Shkia, 20, true, false, false );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void notificationsArePostedOnShabbatWhenTheUserAsksForThem() throws Exception {

    assertShabbat( SHABBAT_DAY );

    scheduleAndFire( SHABBAT_DAY, Zmanim.Type.Shkia, 20, true, true, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
    Assert.assertNotNull( shadowNotifications().getNotification( Zmanim.Type.Shkia.ordinal() ) );
  }


  @Test
  public void nothingIsPostedOnYomTovUnlessTheUserAsksForIt() throws Exception {

    Details firstDayOfPesach = HebrewCalendar.getForDate( 2026, Calendar.APRIL, 2 ).getDetails();
    Assert.assertTrue( firstDayOfPesach.isYomTov() );

    scheduleAndFire( 2026, Calendar.APRIL, 2, Zmanim.Type.Shkia, 20, true, false, false );
    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void notificationsArePostedOnYomTovWhenTheUserAsksForThem() throws Exception {

    scheduleAndFire( 2026, Calendar.APRIL, 2, Zmanim.Type.Shkia, 20, true, true, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void cholHamoedCountsAsAnOrdinaryDay() throws Exception {

    Details cholHamoed = HebrewCalendar.getForDate( 2026, Calendar.APRIL, 5 ).getDetails();
    Assert.assertTrue( cholHamoed.isCholHaMoed() );
    Assert.assertFalse( cholHamoed.isYomTov() );
    Assert.assertFalse( cholHamoed.isShabbat() );

    scheduleAndFire( 2026, Calendar.APRIL, 5, Zmanim.Type.Shkia, 20, true, false, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void anAlertBeforeCandleLightingOnErevShabbatIsPosted() throws Exception {

    scheduleAndFire( EREV_SHABBAT_DAY, Zmanim.Type.PlagHaMincha, 30, true, false, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void anAlertAfterCandleLightingOnErevShabbatIsNotPosted() throws Exception {

    // The zman is sunset, which is later than candle lighting, and the alert comes at the zman.
    scheduleAndFire( EREV_SHABBAT_DAY, Zmanim.Type.Shkia, 0, true, false, false );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void anAlertAfterCandleLightingOnErevYomTovIsNotPosted() throws Exception {

    Assert.assertTrue( HebrewCalendar.getForDate( 2026, Calendar.APRIL, 2 ).getDetails().isYomTov() );
    Assert.assertFalse( HebrewCalendar.getForDate( 2026, Calendar.APRIL, 1 ).getDetails().isYomTov() );
    Assert.assertFalse( HebrewCalendar.getForDate( 2026, Calendar.APRIL, 1 ).getDetails().isShabbat() );

    scheduleAndFire( 2026, Calendar.APRIL, 1, Zmanim.Type.Shkia, 0, true, false, false );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void anAlertBeforeCandleLightingOnErevYomTovIsPosted() throws Exception {

    scheduleAndFire( 2026, Calendar.APRIL, 1, Zmanim.Type.PlagHaMincha, 30, true, false, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void anAlertAfterShabbatEndsIsPosted() throws Exception {

    assertShabbat( SHABBAT_DAY );

    givenPreferences( true, false, false, new EnumMap<>( Zmanim.Type.class ) );
    startService();

    Zman shabbatEnd = defaultZman( ALBANY, YEAR, MONTH, SHABBAT_DAY, Zmanim.Type.ShabbatEnd );
    Calendar fiveMinutesLater = (Calendar)shabbatEnd.getTime().clone();
    fiveMinutesLater.add( Calendar.MINUTE, 5 );

    deliver( DisplayNotificationService.createIntentForService( context, shabbatEnd, ALBANY, fiveMinutesLater ) );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
    Assert.assertNotNull( shadowNotifications().getNotification( Zmanim.Type.ShabbatEnd.ordinal() ) );
  }


  @Test
  public void anAlertJustBeforeShabbatEndsIsNotPosted() throws Exception {

    givenPreferences( true, false, false, new EnumMap<>( Zmanim.Type.class ) );
    startService();

    Zman shabbatEnd = defaultZman( ALBANY, YEAR, MONTH, SHABBAT_DAY, Zmanim.Type.ShabbatEnd );
    Calendar fiveMinutesEarlier = (Calendar)shabbatEnd.getTime().clone();
    fiveMinutesEarlier.add( Calendar.MINUTE, -5 );

    deliver( DisplayNotificationService.createIntentForService( context, shabbatEnd, ALBANY, fiveMinutesEarlier ) );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
  }


  @Test
  public void aZmanIsRecordedAsNotifiedOnceItsNotificationIsShown() throws Exception {

    Calendar now = timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 );
    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, true, false, false );

    Assert.assertEquals( 1, shadowNotifications().getAllNotifications().size() );
    Assert.assertTrue( NotifiedZmanim.manager.get( now ).contains( Zmanim.Type.Shkia ) );
    Assert.assertFalse( NotifiedZmanim.manager.get( now ).contains( Zmanim.Type.PlagHaMincha ) );
  }


  @Test
  public void aZmanIsRecordedAsNotifiedEvenWhenItsNotificationIsSuppressed() throws Exception {

    Calendar now = timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 );
    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, false, false, false );

    Assert.assertEquals( 0, shadowNotifications().getAllNotifications().size() );
    Assert.assertTrue( NotifiedZmanim.manager.get( now ).contains( Zmanim.Type.Shkia ) );
  }


  @Test
  public void aZmanThatWasNotifiedIsNotScheduledAgain() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.PlagHaMincha, 30 );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    givenPreferences( true, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );
    Assert.assertEquals( 2, scheduledAlarms().size() );

    long plagAlert = defaultZman( ALBANY, YEAR, MONTH, DAY, Zmanim.Type.PlagHaMincha ).getTime().getTimeInMillis() - 30 * MINUTE_MS;
    long shkiaAlert = defaultZman( ALBANY, YEAR, MONTH, DAY, Zmanim.Type.Shkia ).getTime().getTimeInMillis() - 20 * MINUTE_MS;
    for ( ShadowAlarmManager.ScheduledAlarm alarm : new ArrayList<>( scheduledAlarms() ) ) {
      if ( alarm.getTriggerAtMs() == shkiaAlert ) {
        fire( alarm );
      }
    }
    Assert.assertEquals( "only the sunset alarm went off", 1, shadowNotifications().getAllNotifications().size() );

    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );

    List<Long> triggers = new ArrayList<>();
    for ( ShadowAlarmManager.ScheduledAlarm alarm : scheduledAlarms() ) {
      triggers.add( alarm.getTriggerAtMs() );
    }
    Assert.assertEquals( List.of( plagAlert ), triggers );
  }


  @Test
  public void theRecordOfANotifiedZmanSurvivesTheAppBeingRestarted() throws Exception {

    Calendar now = timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 );
    scheduleAndFire( DAY, Zmanim.Type.Shkia, 20, true, false, false );

    File record = new File( context.getFilesDir(), YEAR + "_" + MONTH + "_" + DAY + ".notifiedZmanim" );
    waitForTheFile( record );

    forgetNotifiedZmanimInMemory();
    ZmanCallback restarted = new PrefsBasedZmanCallback( context ) {
      @Override
      public void scheduleAlert( Zman zman, ZmanimLocation location, Calendar alertAt ) {
      }
    };

    Assert.assertTrue( restarted.hasBeenNotified( now, Zmanim.Type.Shkia ) );
    Assert.assertFalse( restarted.hasBeenNotified( now, Zmanim.Type.PlagHaMincha ) );
  }


  private void scheduleAndFire(
    int         day,
    Zmanim.Type type,
    int         minutesBefore,
    boolean     showNotifications,
    boolean     notifyOnShabbat,
    boolean     useAlarmClock
  ) throws Exception {
    scheduleAndFire( YEAR, MONTH, day, type, minutesBefore, showNotifications, notifyOnShabbat, useAlarmClock );
  }


  /**
   * Has the service schedule the alarm for the one zman type, as of noon on the day, and then lets every scheduled
   *  alarm go off.
   */
  private void scheduleAndFire(
    int         year,
    int         month,
    int         day,
    Zmanim.Type type,
    int         minutesBefore,
    boolean     showNotifications,
    boolean     notifyOnShabbat,
    boolean     useAlarmClock
  ) throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( type, minutesBefore );
    givenPreferences( showNotifications, notifyOnShabbat, useAlarmClock, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, year, month, day, 12, 0 ) );
    Assert.assertEquals( "one alarm was scheduled", 1, scheduledAlarms().size() );

    fireAll();
  }


  private void fireAll() {

    for ( ShadowAlarmManager.ScheduledAlarm alarm : new ArrayList<>( scheduledAlarms() ) ) {
      fire( alarm );
    }
  }


  private void assertShabbat( int day ) {
    Assert.assertTrue( HebrewCalendar.getForDate( YEAR, MONTH, day ).getDetails().isShabbat() );
  }


  private static String withPlainSpaces( String text ) {
    // Newer JDKs separate the time from AM/PM with a narrow no-break space.
    return text.replace( '\u202f', ' ' );
  }


  private void deliver( Intent intent ) {

    new DisplayNotificationService().onReceive( context, intent );
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private void waitForTheFile( File file ) throws InterruptedException {

    // The record is written by a background task.
    for ( int attempt = 0; attempt < 250 && !file.exists(); attempt++ ) {
      Shadows.shadowOf( Looper.getMainLooper() ).idle();
      Thread.sleep( 20 );
    }
    Assert.assertTrue( "saved " + file, file.exists() );
  }
}
