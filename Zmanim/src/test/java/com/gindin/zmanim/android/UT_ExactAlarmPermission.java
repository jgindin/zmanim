/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.ZmanimReceiver;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.app.JobIntentService;
import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * When the app asks the user to allow exact alarms: only on Android 12 and later, only when the user wants
 *  notifications and exact alarms are not allowed, never before the location and notification permission questions
 *  are settled, and only once. And when exact alarms become allowed, the alerts are scheduled again.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_ExactAlarmPermission {

  private ExactAlarmPermission.Rescheduler                  realRescheduler;

  private int                                               reschedules;


  @Before
  public void startWithExactAlarmsDenied() throws Exception {

    // A fresh install on Android 14 and later, which is what the shadow alarm manager reports by default too.
    ShadowAlarmManager.setCanScheduleExactAlarms( false );

    realRescheduler = ExactAlarmPermission.rescheduler;
    ExactAlarmPermission.rescheduler = context -> reschedules++;

    forgetTheJobSchedulersOfEarlierTests();
  }


  /**
   * Launching the main activity over a fresh install runs the app's upgrade steps, which leave this process-wide flag
   *  set. Clear it so that tests running later in the same JVM that start the converter see a loaded cache.
   */
  @After
  public void restoreProcessWideState() {
    LocationToAddressConverter.wipeCacheOnStart = false;
    ExactAlarmPermission.rescheduler = realRescheduler;
  }


  @Test
  public void decisionNeedsAndroid12AWishForNotificationsThatAreNotBlockedNoExactAlarmsAndNoEarlierAnswer() {

    assertTrue( ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.S, true, false, false, false ) );
    assertTrue( ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.BAKLAVA, true, false, false, false ) );

    assertFalse( "below Android 12", ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.R, true, false, false, false ) );
    assertFalse( "notifications turned off",
      ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.S, false, false, false, false ) );
    assertFalse( "notifications blocked", ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.S, true, true, false, false ) );
    assertFalse( "exact alarms allowed", ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.S, true, false, true, false ) );
    assertFalse( "already asked", ExactAlarmPermission.shouldAsk( Build.VERSION_CODES.S, true, false, false, true ) );
  }


  @Test
  public void notAskedWhileTheSystemBlocksNotifications() {

    blockNotifications();

    assertFalse( ExactAlarmPermission.shouldAsk( context() ) );
  }


  @Test
  public void askedOnceNotificationsAreAllowedAfterBeingBlocked() {

    settleTheNotificationQuestion();
    blockNotifications();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        assertNull( "Blocked notifications cannot be late", shownExactAlarmExplanation() );
      } );
    }

    // The user allows notifications, from the preferences' row for instance, and comes back.
    allowNotifications();
    ShadowDialog.reset();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        AlertDialog explanation = shownExactAlarmExplanation();
        assertNotNull( "Asked once notifications can appear", explanation );
        click( explanation, DialogInterface.BUTTON_NEGATIVE );
      } );
    }

    assertNotAskedOnRelaunch();
  }


  @Test
  public void freshInstallIsAsked() {
    assertTrue( ExactAlarmPermission.isDenied( context() ) );
    assertTrue( ExactAlarmPermission.shouldAsk( context() ) );
  }


  @Test
  @Config( sdk = 32 )
  public void freshInstallIsAskedOnAndroid12() {
    assertTrue( ExactAlarmPermission.shouldAsk( context() ) );
  }


  /** Granted by the user, or exempt from battery optimization: the alarm manager reports both the same way. */
  @Test
  public void notAskedWhenExactAlarmsAreAllowed() {

    ShadowAlarmManager.setCanScheduleExactAlarms( true );

    assertFalse( ExactAlarmPermission.isDenied( context() ) );
    assertFalse( ExactAlarmPermission.shouldAsk( context() ) );
  }


  @Test
  public void notAskedWhenNotificationsAreTurnedOff() {

    personalPrefs().edit().putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false ).commit();

    assertFalse( ExactAlarmPermission.shouldAsk( context() ) );
  }


  @Test
  public void notAskedAgainOnceAnswered() {

    ExactAlarmPermission.markAsked( context() );

    assertTrue( personalPrefs().getBoolean( ExactAlarmPermission.ASKED_KEY, false ) );
    assertFalse( ExactAlarmPermission.shouldAsk( context() ) );
  }


  @Test
  public void theSystemPageIsTheOneForThisApp() {

    Intent intent = ExactAlarmPermission.exactAlarmSettings( context() );

    assertEquals( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, intent.getAction() );
    assertEquals( Uri.parse( "package:" + context().getPackageName() ), intent.getData() );
  }


  @Test
  public void acceptingTheExplanationOpensTheSystemPageOnceAndOnlyOnce() {

    settleTheNotificationQuestion();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        AlertDialog explanation = shownExactAlarmExplanation();
        assertNotNull( "The explanation should appear once a location is in", explanation );
        assertFalse( "Only an answer may close the explanation", Shadows.shadowOf( explanation ).isCancelable() );

        click( explanation, DialogInterface.BUTTON_POSITIVE );

        Intent started = Shadows.shadowOf( activity ).getNextStartedActivity();
        assertNotNull( "Accepting must open the system page", started );
        assertEquals( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, started.getAction() );
        assertEquals( Uri.parse( "package:" + context().getPackageName() ), started.getData() );
        assertTrue( personalPrefs().getBoolean( ExactAlarmPermission.ASKED_KEY, false ) );
      } );
    }

    assertNotAskedOnRelaunch();
  }


  @Test
  public void decliningTheExplanationOpensNothingAndIsRemembered() {

    settleTheNotificationQuestion();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        AlertDialog explanation = shownExactAlarmExplanation();
        assertNotNull( explanation );

        click( explanation, DialogInterface.BUTTON_NEGATIVE );

        assertNull( "Declining must not open the system page", nextStartedSettingsPage( activity ) );
        assertTrue( personalPrefs().getBoolean( ExactAlarmPermission.ASKED_KEY, false ) );
      } );
    }

    assertNotAskedOnRelaunch();
  }


  @Test
  public void anotherLocationWhileTheExplanationIsUpDoesNotStackASecondOne() {

    settleTheNotificationQuestion();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        assertNotNull( shownExactAlarmExplanation() );

        fireLocation();

        int showing = 0;
        for ( Dialog dialog : ShadowDialog.getShownDialogs() ) {
          if ( dialog.isShowing() ) {
            showing++;
          }
        }
        assertEquals( "One explanation at a time", 1, showing );
      } );
    }
  }


  @Test
  public void notAskedWhenExactAlarmsAreAlreadyAllowed() {

    settleTheNotificationQuestion();
    ShadowAlarmManager.setCanScheduleExactAlarms( true );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        assertNull( shownExactAlarmExplanation() );
      } );
    }
  }


  /** Shown alone even if the system reported notifications as allowed before the permission is granted. */
  @Test
  public void neverShownBesideTheNotificationQuestion() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        assertNotNull( "The notification question comes first",
          shownDialogWithMessage( R.string.notification_permission_explanation ) );
        assertNull( "Never two questions at once", shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void notAskedAfterTheNotificationQuestionIsDeclined() {

    // A fresh install on Android 13 and later: notifications are blocked until the permission is granted.
    blockNotifications();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        click( shownDialogWithMessage( R.string.notification_permission_explanation ), DialogInterface.BUTTON_NEGATIVE );

        assertNull( "No second permission question once notifications are declined", shownExactAlarmExplanation() );

        fireLocation();
        assertNull( shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void waitsForTheAnswerToTheSystemNotificationPrompt() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        blockNotifications();
        fireLocation();
        click( shownDialogWithMessage( R.string.notification_permission_explanation ), DialogInterface.BUTTON_POSITIVE );
        assertNotNull( "The system notification prompt is up", Shadows.shadowOf( activity ).getLastRequestedPermission() );

        // A location arriving while the user is still answering the system prompt.
        fireLocation();
        assertNull( "The exact alarm question must wait for the system prompt", shownExactAlarmExplanation() );

        Shadows.shadowOf( (Application)context() ).grantPermissions( Manifest.permission.POST_NOTIFICATIONS );
        allowNotifications();
        activity.onRequestPermissionsResult( ZmanimActivity.NOTIFICATION_PERMISSION_REQUEST_CODE,
          new String[] { Manifest.permission.POST_NOTIFICATIONS },
          new int[] { PackageManager.PERMISSION_GRANTED }
        );
        idle();

        assertNotNull( "The exact alarm question follows the system prompt", shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void notAskedWhenTheSystemNotificationPromptIsRefused() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        blockNotifications();
        fireLocation();
        click( shownDialogWithMessage( R.string.notification_permission_explanation ), DialogInterface.BUTTON_POSITIVE );

        activity.onRequestPermissionsResult( ZmanimActivity.NOTIFICATION_PERMISSION_REQUEST_CODE,
          new String[] { Manifest.permission.POST_NOTIFICATIONS },
          new int[] { PackageManager.PERMISSION_DENIED }
        );
        idle();

        assertNull( "No second permission question once notifications are refused", shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void waitsWhileTheLocationPermissionIsBeingAskedFor() {

    settleTheNotificationQuestion();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        ZmanimService.ZMANIM_EVENT_BUS.fire(
          new LocationAcquiredEvent( new LocationError( LocationError.Code.NO_PERMISSIONS ), null ) );
        idle();
        assertNotNull( "The location explanation should be up", ShadowDialog.getLatestDialog() );

        // A location from another source arriving while the user is still deciding about location access.
        fireLocation();

        assertNull( "The exact alarm question must wait for the location one", shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void notAskedWhileTheActivityIsFinishing() {

    settleTheNotificationQuestion();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        activity.finish();
        assertTrue( activity.isFinishing() );

        fireLocation();

        assertNull( "No dialog may be shown on an activity that is going away", shownExactAlarmExplanation() );
      } );
    }
  }


  @Test
  public void destroyingTheActivityDismissesTheExplanation() {

    settleTheNotificationQuestion();
    AlertDialog[] explanation = new AlertDialog[ 1 ];

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        explanation[ 0 ] = shownExactAlarmExplanation();
      } );
    }

    assertNotNull( explanation[ 0 ] );
    assertFalse( "A destroyed activity must not leave its dialog's window behind", explanation[ 0 ].isShowing() );
  }


  @Test
  public void comingBackWithExactAlarmsAllowedSchedulesTheAlertsAgain() {

    settleTheNotificationQuestion();
    ExactAlarmPermission.markAsked( context() );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      // Away on the system page, without changing anything.
      scenario.moveToState( Lifecycle.State.CREATED );
      scenario.moveToState( Lifecycle.State.RESUMED );
      assertEquals( "Nothing changed, so nothing to reschedule", 0, reschedules );

      // Away again, and this time exact alarms are allowed.
      scenario.moveToState( Lifecycle.State.CREATED );
      ShadowAlarmManager.setCanScheduleExactAlarms( true );
      scenario.moveToState( Lifecycle.State.RESUMED );
      assertEquals( "The queued inexact alarms must be replaced", 1, reschedules );

      scenario.moveToState( Lifecycle.State.CREATED );
      scenario.moveToState( Lifecycle.State.RESUMED );
      assertEquals( "Already rescheduled", 1, reschedules );
    }
  }


  @Test
  public void theDefaultRescheduleQueuesWorkForTheService() {

    JobScheduler jobs = context().getSystemService( JobScheduler.class );
    jobs.cancelAll();

    realRescheduler.reschedule( context() );

    assertServiceWorkQueued( jobs );
  }


  @Test
  public void theSystemAnnouncingThatExactAlarmsAreAllowedSchedulesTheAlertsAgain() {

    Intent announcement = new Intent( AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED )
      .setPackage( context().getPackageName() );

    List<ResolveInfo> receivers = context().getPackageManager().queryBroadcastReceivers( announcement, 0 );
    assertEquals( "The manifest must register a receiver for the announcement", 1, receivers.size() );
    assertEquals( ZmanimReceiver.class.getName(), receivers.get( 0 ).activityInfo.name );

    JobScheduler jobs = context().getSystemService( JobScheduler.class );
    jobs.cancelAll();

    new ZmanimReceiver().onReceive( context(), announcement );

    assertServiceWorkQueued( jobs );
  }


  @Test
  public void preferencesLeadToTheSystemPageWhileExactAlarmsAreDenied() {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        Preference denied = preference( activity, PersonalPreferences.Prefs.EXACT_ALARMS_DENIED );
        assertTrue( "The notice should show while notifications are wanted but may be late", denied.isVisible() );

        Intent intent = denied.getIntent();
        assertEquals( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, intent.getAction() );
        assertEquals( Uri.parse( "package:" + context().getPackageName() ), intent.getData() );
      } );
    }
  }


  @Test
  public void turningNotificationsOnWhileExactAlarmsAreDeniedExplainsAndOffersTheSystemPage() {

    personalPrefs().edit().putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false ).commit();

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        Preference denied = preference( activity, PersonalPreferences.Prefs.EXACT_ALARMS_DENIED );
        assertFalse( "No notice while the user does not want notifications", denied.isVisible() );

        preference( activity, PersonalPreferences.Prefs.SHOW_NOTIFICATIONS ).callChangeListener( true );
        idle();

        assertTrue( denied.isVisible() );
        AlertDialog dialog = (AlertDialog)ShadowDialog.getLatestDialog();
        assertNotNull( "Turning notifications on while exact alarms are denied should explain the delay", dialog );
        assertEquals( context().getString( R.string.exact_alarms_denied_message ),
          Shadows.shadowOf( dialog ).getMessage().toString() );

        click( dialog, DialogInterface.BUTTON_POSITIVE );
        Intent started = Shadows.shadowOf( activity ).getNextStartedActivity();
        assertNotNull( "Open Settings should open the system page", started );
        assertEquals( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, started.getAction() );
      } );
    }
  }


  @Test
  public void turningNotificationsOnWhileTheyAreBlockedExplainsOnlyTheBlocking() {

    personalPrefs().edit().putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false ).commit();
    blockNotifications();

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        preference( activity, PersonalPreferences.Prefs.SHOW_NOTIFICATIONS ).callChangeListener( true );
        idle();

        assertEquals( "One explanation at a time", 1, ShadowDialog.getShownDialogs().size() );
        assertEquals( context().getString( R.string.notifications_blocked_message ),
          Shadows.shadowOf( (AlertDialog)ShadowDialog.getLatestDialog() ).getMessage().toString() );
        assertTrue( preference( activity, PersonalPreferences.Prefs.NOTIFICATIONS_BLOCKED ).isVisible() );
        assertFalse( "Late alerts only matter once notifications can appear",
          preference( activity, PersonalPreferences.Prefs.EXACT_ALARMS_DENIED ).isVisible() );
      } );
    }
  }


  @Test
  public void preferencesShowNoNoticeWhileExactAlarmsAreAllowed() {

    ShadowAlarmManager.setCanScheduleExactAlarms( true );

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        assertFalse( preference( activity, PersonalPreferences.Prefs.EXACT_ALARMS_DENIED ).isVisible() );

        preference( activity, PersonalPreferences.Prefs.SHOW_NOTIFICATIONS ).callChangeListener( true );
        idle();
        assertNull( "Nothing to explain while exact alarms are allowed", ShadowDialog.getLatestDialog() );
      } );
    }
  }


  /**
   * JobIntentService keeps, for the life of the process, the job scheduler it first used for each service. Each
   *  Robolectric test has an application of its own, so forget them, so that work is queued with this test's
   *  scheduler.
   */
  private static void forgetTheJobSchedulersOfEarlierTests() throws Exception {

    Field enqueuers = JobIntentService.class.getDeclaredField( "sClassWorkEnqueuer" );
    enqueuers.setAccessible( true );
    ( (Map<?, ?>)enqueuers.get( null ) ).clear();
  }


  private static void assertServiceWorkQueued( JobScheduler jobs ) {

    List<JobInfo> pending = jobs.getAllPendingJobs();
    assertEquals( 1, pending.size() );
    assertEquals( ZmanimService.class.getName(), pending.get( 0 ).getService().getClassName() );
  }


  private static void blockNotifications() {
    Shadows.shadowOf( context().getSystemService( NotificationManager.class ) ).setNotificationsEnabled( false );
  }


  private static void allowNotifications() {
    Shadows.shadowOf( context().getSystemService( NotificationManager.class ) ).setNotificationsEnabled( true );
  }


  /** Grants the notification permission, so that the notification question does not come up. */
  private static void settleTheNotificationQuestion() {
    Shadows.shadowOf( (Application)context() ).grantPermissions( Manifest.permission.POST_NOTIFICATIONS );
  }


  private static Preference preference(
    PersonalPreferences       activity,
    PersonalPreferences.Prefs key
  ) {

    activity.getSupportFragmentManager().executePendingTransactions();
    idle();

    PreferenceFragmentCompat fragment =
      (PreferenceFragmentCompat)activity.getSupportFragmentManager().findFragmentById( android.R.id.content );
    Preference preference = fragment.findPreference( key.name() );
    assertNotNull( key.name(), preference );
    return preference;
  }


  private void assertNotAskedOnRelaunch() {

    ShadowDialog.reset();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        assertNull( "Asked again on a later launch", shownExactAlarmExplanation() );
        assertNull( nextStartedSettingsPage( activity ) );
      } );
    }
  }


  /** The next activity started that is the system exact alarm page, skipping anything else the activity started. */
  private static Intent nextStartedSettingsPage( ZmanimActivity activity ) {

    Intent started;
    while ( ( started = Shadows.shadowOf( activity ).getNextStartedActivity() ) != null ) {
      if ( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM.equals( started.getAction() ) ) {
        return started;
      }
    }
    return null;
  }


  private static void fireLocation() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .locationName( "Albany, New York" )
      .atLatitude( 42.6526 )
      .atLongitude( -73.7562 )
      .atElevation( 0 )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .at( 0L )
      .build();

    ZmanimService.ZMANIM_EVENT_BUS.fire( new LocationAcquiredEvent( location ) );
    idle();
  }


  private static AlertDialog shownExactAlarmExplanation() {
    return shownDialogWithMessage( R.string.exact_alarm_permission_explanation );
  }


  /** The dialog with the specified message, if one is on screen. */
  private static AlertDialog shownDialogWithMessage( int message ) {

    String text = context().getString( message );
    for ( Dialog dialog : ShadowDialog.getShownDialogs() ) {
      if ( ( dialog instanceof AlertDialog ) && dialog.isShowing() ) {
        ShadowAlertDialog shadow = Shadows.shadowOf( (AlertDialog)dialog );
        if ( text.contentEquals( shadow.getMessage() ) ) {
          return (AlertDialog)dialog;
        }
      }
    }
    return null;
  }


  private static void click(
    AlertDialog dialog,
    int         which
  ) {
    dialog.getButton( which ).performClick();
    idle();
  }


  private static void idle() {
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private static SharedPreferences personalPrefs() {
    return context().getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
  }


  private static Context context() {
    return ApplicationProvider.getApplicationContext();
  }

}  // End of UT_ExactAlarmPermission class
