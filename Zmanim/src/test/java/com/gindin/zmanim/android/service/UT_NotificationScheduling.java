/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The real {@link ZmanimService} and {@link ZmanNotificationScheduler} work out when to alert the user and hand
 *  the alarms to the {@link android.app.AlarmManager}; the alarm manager, preferences and notified-zmanim records
 *  are the ones the app uses.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_NotificationScheduling extends NotificationTestBase {

  /** An ordinary Tuesday, with no Yom Tov or fast within a few days of it. */
  private static final int                                  YEAR = 2026;
  private static final int                                  MONTH = Calendar.AUGUST;
  private static final int                                  DAY = 4;


  @Test
  public void anAlarmIsScheduledForEachZmanWithAnOffsetAtTheZmanTimeMinusThatOffset() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    alertMinutes.put( Zmanim.Type.PlagHaMincha, 30 );
    // Already over by the time of day used below, so no alarm is wanted for it.
    alertMinutes.put( Zmanim.Type.SofZmanShema, 45 );
    givenPreferences( true, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );

    long sunset = zmanMillis( Zmanim.Type.Shkia );
    long plag = zmanMillis( Zmanim.Type.PlagHaMincha );
    Assert.assertTrue( "plag haMincha comes before sunset", plag < sunset );
    Assert.assertEquals( List.of( plag - 30 * MINUTE_MS, sunset - 20 * MINUTE_MS ), triggerTimes() );
  }


  @Test
  public void aZmanTheUserNeverAskedToBeAlertedAboutGetsNoAlarm() throws Exception {

    givenPreferences( true, false, false, Collections.emptyMap() );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 0, 1 ) );

    Assert.assertEquals( List.of(), scheduledAlarms() );
  }


  @Test
  public void anOffsetLongerThanTheTimeLeftAlertsStraightAway() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.Shkia, 90 );
    givenPreferences( true, false, false, alertMinutes );

    Calendar now = timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 0, 0 );
    now.setTimeInMillis( zmanMillis( Zmanim.Type.Shkia ) - 30 * MINUTE_MS );

    startService();
    scheduleAlarmsAsOf( ALBANY, now );

    Assert.assertEquals( List.of( now.getTimeInMillis() ), triggerTimes() );
  }


  @Test
  public void theZmanVariantTheUserChoseIsTheOneAlertedAbout() throws Exception {

    Zmanim plagHaMincha = ZmanimFactory.get( ALBANY, HebrewCalendar.getForDate( YEAR, MONTH, DAY ), Zmanim.Type.PlagHaMincha );
    Zman chosen = plagHaMincha.getByName( "MG'A 120 Minutes" );
    Assert.assertEquals( "MG'A 120 Minutes", chosen.name );
    Assert.assertNotEquals( "the variant must differ from the default for this test to mean anything",
      plagHaMincha.getDefault().getTime().getTimeInMillis(), chosen.getTime().getTimeInMillis() );

    SharedPreferences selection = context.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
    Assert.assertTrue( selection.edit().putString( Zmanim.Type.PlagHaMincha.displayName, chosen.name ).commit() );

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.PlagHaMincha, 10 );
    givenPreferences( true, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 6, 0 ) );

    Assert.assertEquals( List.of( chosen.getTime().getTimeInMillis() - 10 * MINUTE_MS ), triggerTimes() );
  }


  @Test
  public void aZmanAlreadyNotifiedForTheDayGetsNoAlarm() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    alertMinutes.put( Zmanim.Type.PlagHaMincha, 30 );
    givenPreferences( true, false, false, alertMinutes );

    Calendar now = timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 );
    NotifiedZmanim.manager.init( context.getFilesDir() );
    NotifiedZmanim.manager.get( now ).notified( Zmanim.Type.Shkia );

    startService();
    scheduleAlarmsAsOf( ALBANY, now );

    Assert.assertEquals( List.of( zmanMillis( Zmanim.Type.PlagHaMincha ) - 30 * MINUTE_MS ), triggerTimes() );
  }


  @Test
  public void alarmsAreStillScheduledWhenNotificationsAreTurnedOff() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    givenPreferences( false, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );

    Assert.assertEquals( List.of( zmanMillis( Zmanim.Type.Shkia ) - 20 * MINUTE_MS ), triggerTimes() );
  }


  @Test
  public void midnightAfterTheDaysSunsetIsStillScheduledAsPartOfThatDay() throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.NightChatzot, 30 );
    givenPreferences( true, false, false, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 23, 50 ) );

    long midnight = zmanMillis( Zmanim.Type.NightChatzot );
    Assert.assertEquals( List.of( midnight - 30 * MINUTE_MS ), triggerTimes() );
    Assert.assertTrue( "the alarm is still ahead of the time of day used",
      midnight - 30 * MINUTE_MS > timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 23, 50 ).getTimeInMillis() );
  }


  @Test
  public void aLocationAcquiredByTheServiceSchedulesAlarmsFromTheCurrentTime() {

    // Every zman asks for an alert, so whatever the time of day, some zman still lies ahead.
    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      alertMinutes.put( type, 0 );
    }
    givenPreferences( true, false, false, alertMinutes );

    startService();
    long before = System.currentTimeMillis();
    acquireLocation( ALBANY );

    List<Long> triggers = triggerTimes();
    Assert.assertFalse( triggers.isEmpty() );
    long threeDays = 3L * 24 * 60 * MINUTE_MS;
    for ( long trigger : triggers ) {
      Assert.assertTrue( "alarm at " + trigger + " is not between now and a few days from now",
        trigger >= before - MINUTE_MS && trigger <= before + threeDays );
    }
  }


  @Test
  public void theAlarmClockIsUsedWhenTheUserAsksForIt() throws Exception {

    scheduleOneAlarm( true );

    ShadowAlarmManager.ScheduledAlarm alarm = onlyAlarm();
    Assert.assertNotNull( "set with setAlarmClock", alarm.getAlarmClockInfo() );
    Assert.assertNotNull( "the alarm clock entry opens the app", alarm.showIntent );
    Assert.assertEquals( alarm.getTriggerAtMs(), alarm.getAlarmClockInfo().getTriggerTime() );
  }


  @Test
  public void anExactAlarmThatWorksWhileIdleIsUsedOtherwise() throws Exception {

    scheduleOneAlarm( false );

    ShadowAlarmManager.ScheduledAlarm alarm = onlyAlarm();
    Assert.assertNull( alarm.getAlarmClockInfo() );
    Assert.assertTrue( "setExactAndAllowWhileIdle", alarm.allowWhileIdle );
    Assert.assertEquals( "exact", 0L, alarm.getWindowLengthMs() );
  }


  @Test
  public void anInexactAlarmIsUsedWhenExactAlarmsAreNotAllowed() throws Exception {

    ShadowAlarmManager.setCanScheduleExactAlarms( false );

    scheduleOneAlarm( true );

    ShadowAlarmManager.ScheduledAlarm alarm = onlyAlarm();
    Assert.assertNull( alarm.getAlarmClockInfo() );
    Assert.assertFalse( alarm.allowWhileIdle );
    Assert.assertEquals( "inexact", ShadowAlarmManager.WINDOW_HEURISTIC, alarm.getWindowLengthMs() );
  }


  @Test
  public void theNotificationChannelIsCreatedWhenTheServiceStarts() {

    givenPreferences( true, false, false, Collections.emptyMap() );
    Assert.assertNull( notificationManager.getNotificationChannel( DisplayNotificationService.NOTIFICATION_CHANNEL_ID ) );

    startService();

    Assert.assertEquals( "zmanim-notifications_3", DisplayNotificationService.NOTIFICATION_CHANNEL_ID );
    NotificationChannel channel = notificationManager.getNotificationChannel( DisplayNotificationService.NOTIFICATION_CHANNEL_ID );
    Assert.assertNotNull( channel );
    Assert.assertEquals( NotificationManager.IMPORTANCE_HIGH, channel.getImportance() );
    Assert.assertEquals( "Zmanim Notifications", channel.getName().toString() );
    Assert.assertTrue( channel.shouldVibrate() );
    Assert.assertTrue( channel.shouldShowLights() );
  }


  @Test
  public void theChannelsOfEarlierVersionsAreDeletedWhenTheServiceStarts() {

    givenPreferences( true, false, false, Collections.emptyMap() );
    for ( String oldChannel : List.of( "zmanim-notifications", "zmanim-notifications_2" ) ) {
      notificationManager.createNotificationChannel(
        new NotificationChannel( oldChannel, oldChannel, NotificationManager.IMPORTANCE_DEFAULT ) );
    }

    startService();

    Assert.assertNull( notificationManager.getNotificationChannel( "zmanim-notifications" ) );
    Assert.assertNull( notificationManager.getNotificationChannel( "zmanim-notifications_2" ) );
    Assert.assertNotNull( notificationManager.getNotificationChannel( DisplayNotificationService.NOTIFICATION_CHANNEL_ID ) );
  }


  private void scheduleOneAlarm( boolean useAlarmClock ) throws Exception {

    Map<Zmanim.Type, Integer> alertMinutes = new EnumMap<>( Zmanim.Type.class );
    alertMinutes.put( Zmanim.Type.Shkia, 20 );
    givenPreferences( true, false, useAlarmClock, alertMinutes );

    startService();
    scheduleAlarmsAsOf( ALBANY, timeIs( NEW_YORK_ZONE, YEAR, MONTH, DAY, 12, 0 ) );
  }


  private ShadowAlarmManager.ScheduledAlarm onlyAlarm() {

    List<ShadowAlarmManager.ScheduledAlarm> alarms = scheduledAlarms();
    Assert.assertEquals( 1, alarms.size() );
    return alarms.get( 0 );
  }


  private List<Long> triggerTimes() {

    List<Long> times = new ArrayList<>();
    for ( ShadowAlarmManager.ScheduledAlarm alarm : scheduledAlarms() ) {
      times.add( alarm.getTriggerAtMs() );
    }
    Collections.sort( times );
    return times;
  }


  private static long zmanMillis( Zmanim.Type type ) {
    return defaultZman( ALBANY, YEAR, MONTH, DAY, type ).getTime().getTimeInMillis();
  }
}
