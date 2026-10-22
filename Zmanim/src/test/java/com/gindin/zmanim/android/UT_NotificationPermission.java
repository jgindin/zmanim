/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

import java.util.TimeZone;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * When the app asks for permission to post notifications: only on Android 13 and later, only when the user wants
 *  notifications and has not granted the permission, never before the location questions are settled, and only once.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_NotificationPermission {

  /**
   * Launching the main activity over a fresh install runs the app's upgrade steps, which leave this process-wide flag
   *  set. Clear it so that tests running later in the same JVM that start the converter see a loaded cache.
   */
  @After
  public void clearProcessWideUpgradeFlag() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  @Test
  public void decisionNeedsAndroid13AWishForNotificationsNoGrantAndNoEarlierAnswer() {

    assertTrue( NotificationPermission.shouldAsk( Build.VERSION_CODES.TIRAMISU, true, false, false ) );
    assertTrue( NotificationPermission.shouldAsk( Build.VERSION_CODES.BAKLAVA, true, false, false ) );

    assertFalse( "below Android 13", NotificationPermission.shouldAsk( Build.VERSION_CODES.S_V2, true, false, false ) );
    assertFalse( "notifications turned off", NotificationPermission.shouldAsk( Build.VERSION_CODES.TIRAMISU, false, false, false ) );
    assertFalse( "already granted", NotificationPermission.shouldAsk( Build.VERSION_CODES.TIRAMISU, true, true, false ) );
    assertFalse( "already asked", NotificationPermission.shouldAsk( Build.VERSION_CODES.TIRAMISU, true, false, true ) );
  }


  @Test
  public void freshInstallIsAsked() {
    assertTrue( NotificationPermission.shouldAsk( context() ) );
  }


  @Test
  @Config( sdk = 32 )
  public void notAskedBelowAndroid13() {
    assertFalse( NotificationPermission.shouldAsk( context() ) );
  }


  @Test
  public void notAskedWhenGranted() {

    Shadows.shadowOf( (Application)context() ).grantPermissions( Manifest.permission.POST_NOTIFICATIONS );

    assertFalse( NotificationPermission.shouldAsk( context() ) );
  }


  @Test
  public void notAskedWhenNotificationsAreTurnedOff() {

    personalPrefs().edit().putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false ).commit();

    assertFalse( NotificationPermission.shouldAsk( context() ) );
  }


  @Test
  public void notAskedAgainOnceAnswered() {

    NotificationPermission.markAsked( context() );

    assertTrue( personalPrefs().getBoolean( NotificationPermission.ASKED_KEY, false ) );
    assertFalse( NotificationPermission.shouldAsk( context() ) );
  }


  @Test
  public void acceptingTheExplanationRequestsThePermissionOnceAndOnlyOnce() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        AlertDialog explanation = shownNotificationExplanation();
        assertNotNull( "The explanation should appear once a location is in", explanation );
        assertFalse( "Only an answer may close the explanation", Shadows.shadowOf( explanation ).isCancelable() );

        click( explanation, DialogInterface.BUTTON_POSITIVE );

        ShadowActivity.PermissionsRequest request = Shadows.shadowOf( activity ).getLastRequestedPermission();
        assertNotNull( "Accepting must lead to the system request", request );
        assertEquals( ZmanimActivity.NOTIFICATION_PERMISSION_REQUEST_CODE, request.requestCode );
        assertArrayEquals( new String[] { Manifest.permission.POST_NOTIFICATIONS }, request.requestedPermissions );
        assertTrue( personalPrefs().getBoolean( NotificationPermission.ASKED_KEY, false ) );
      } );
    }

    assertNotAskedOnRelaunch();
  }


  @Test
  public void decliningTheExplanationAsksNothingAndIsRemembered() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation();
        AlertDialog explanation = shownNotificationExplanation();
        assertNotNull( explanation );

        click( explanation, DialogInterface.BUTTON_NEGATIVE );

        assertNull( "Declining must not reach the system request",
          Shadows.shadowOf( activity ).getLastRequestedPermission() );
        assertTrue( personalPrefs().getBoolean( NotificationPermission.ASKED_KEY, false ) );
      } );
    }

    assertNotAskedOnRelaunch();
  }


  @Test
  public void waitsWhileTheLocationPermissionIsBeingAskedFor() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        ZmanimService.ZMANIM_EVENT_BUS.fire(
          new LocationAcquiredEvent( new LocationError( LocationError.Code.NO_PERMISSIONS ), null ) );
        idle();
        assertNotNull( "The location explanation should be up", ShadowDialog.getLatestDialog() );

        // A location from another source arriving while the user is still deciding about location access.
        fireLocation();

        assertNull( "The notification question must wait for the location one", shownNotificationExplanation() );
      } );
    }
  }


  @Test
  public void notAskedWhileTheActivityIsFinishing() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        activity.finish();
        assertTrue( activity.isFinishing() );

        fireLocation();

        assertNull( "No dialog may be shown on an activity that is going away", shownNotificationExplanation() );
      } );
    }
  }


  @Test
  public void destroyingTheActivityDismissesTheExplanation() {

    AlertDialog[] explanation = new AlertDialog[ 1 ];

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        explanation[ 0 ] = shownNotificationExplanation();
      } );
    }

    assertNotNull( explanation[ 0 ] );
    assertFalse( "A destroyed activity must not leave its dialog's window behind", explanation[ 0 ].isShowing() );
  }


  @Test
  public void notAskedWhenAlreadyGranted() {

    Shadows.shadowOf( (Application)context() ).grantPermissions( Manifest.permission.POST_NOTIFICATIONS );

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {
        fireLocation();
        assertNull( shownNotificationExplanation() );
      } );
    }
  }


  @Test
  public void preferencesLeadToTheSystemSettingsWhileNotificationsAreBlocked() {

    blockNotifications();

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        Preference blocked = preference( activity, PersonalPreferences.Prefs.NOTIFICATIONS_BLOCKED );
        assertTrue( "The blocked notice should show while notifications are wanted but blocked", blocked.isVisible() );

        Intent intent = blocked.getIntent();
        assertEquals( Settings.ACTION_APP_NOTIFICATION_SETTINGS, intent.getAction() );
        assertEquals( context().getPackageName(), intent.getStringExtra( Settings.EXTRA_APP_PACKAGE ) );
      } );
    }
  }


  @Test
  public void turningNotificationsOnWhileBlockedExplainsAndOffersTheSystemSettings() {

    blockNotifications();
    personalPrefs().edit().putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), false ).commit();

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        Preference blocked = preference( activity, PersonalPreferences.Prefs.NOTIFICATIONS_BLOCKED );
        assertFalse( "No notice while the user does not want notifications", blocked.isVisible() );

        preference( activity, PersonalPreferences.Prefs.SHOW_NOTIFICATIONS ).callChangeListener( true );
        idle();

        assertTrue( blocked.isVisible() );
        AlertDialog dialog = (AlertDialog)ShadowDialog.getLatestDialog();
        assertNotNull( "Turning notifications on while blocked should explain why nothing will appear", dialog );
        assertEquals( context().getString( R.string.notifications_blocked_message ),
          Shadows.shadowOf( dialog ).getMessage().toString() );

        click( dialog, DialogInterface.BUTTON_POSITIVE );
        Intent started = Shadows.shadowOf( activity ).getNextStartedActivity();
        assertNotNull( "Open Settings should open the system settings", started );
        assertEquals( Settings.ACTION_APP_NOTIFICATION_SETTINGS, started.getAction() );
      } );
    }
  }


  @Test
  public void preferencesShowNoNoticeWhileNotificationsAreAllowed() {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity -> {

        assertFalse( preference( activity, PersonalPreferences.Prefs.NOTIFICATIONS_BLOCKED ).isVisible() );

        preference( activity, PersonalPreferences.Prefs.SHOW_NOTIFICATIONS ).callChangeListener( true );
        idle();
        assertNull( "Nothing to explain while notifications are allowed", ShadowDialog.getLatestDialog() );
      } );
    }
  }


  @Test
  @Config( sdk = 32 )
  public void preferencesShowNoNoticeBelowAndroid13() {

    blockNotifications();

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      scenario.onActivity( activity ->
        assertFalse( preference( activity, PersonalPreferences.Prefs.NOTIFICATIONS_BLOCKED ).isVisible() ) );
    }
  }


  private static void blockNotifications() {
    NotificationManager manager = context().getSystemService( NotificationManager.class );
    Shadows.shadowOf( manager ).setNotificationsEnabled( false );
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
        assertNull( "Asked again on a later launch", shownNotificationExplanation() );
        assertNull( Shadows.shadowOf( activity ).getLastRequestedPermission() );
      } );
    }
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


  /** The notification explanation, if one is on screen; recognized by its message. */
  private static AlertDialog shownNotificationExplanation() {

    String explanation = context().getString( R.string.notification_permission_explanation );
    for ( Dialog dialog : ShadowDialog.getShownDialogs() ) {
      if ( ( dialog instanceof AlertDialog ) && dialog.isShowing() ) {
        ShadowAlertDialog shadow = Shadows.shadowOf( (AlertDialog)dialog );
        if ( explanation.contentEquals( shadow.getMessage() ) ) {
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

}  // End of UT_NotificationPermission class
