/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * On Android 12 and later, with location access granted and the notification question answered, a fresh install that
 *  may not set exact alarms explains why its alerts need them, opens the system page that allows them only if the
 *  user agrees, and never asks again on a later launch, whatever the answer.
 *<p/>
 *
 * The system page belongs to another app, so it is driven through UI Automator. The test never turns exact alarms on:
 *  once allowed, taking them away again stops the app's process, which is the process the test runs in. Exact alarms
 *  must therefore be denied when the test starts, which holds after Gradle installs the app on Android 14 or later
 *  (a fresh install is not granted them, and emulators do not exempt the app from battery optimization).
 */
@RunWith( AndroidJUnit4.class )
public class ExactAlarmPromptTest {

  /** Generous, because emulators on CI machines are much slower than local ones; the waits end as soon as it shows. */
  private static final long                                 DIALOG_TIMEOUT_MS = 30_000;

  /** How long a dialog or page that must not appear is given to appear anyway, once the screen has settled. */
  private static final long                                 ABSENCE_TIMEOUT_MS = 4_000;

  /** The buttons of the app's own explanation (a platform AlertDialog). */
  private static final BySelector                           EXPLANATION_ACCEPT = By.res( "android:id/button1" );
  private static final BySelector                           EXPLANATION_DECLINE = By.res( "android:id/button2" );

  /** The system settings app, in the foreground. */
  private static final BySelector                           SYSTEM_SETTINGS = By.pkg( Pattern.compile( ".*settings.*" ) ).depth( 0 );

  @Rule
  public final GrantPermissionRule                          location = AppState.grantLocationPermissions();

  private UiDevice                                          device;

  private BySelector                                        explanation;


  @Before
  public void setUp() {

    Assume.assumeTrue( "Exact alarms are only restricted from Android 12 on",
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.S );
    AppState.assumeSafeToWipe();

    device = UiDevice.getInstance( InstrumentationRegistry.getInstrumentation() );
    explanation = By.text( targetContext().getString( R.string.exact_alarm_permission_title ) );

    assertFalse( "Exact alarms are already allowed, and the test cannot deny them from inside the app's process; run "
        + "`adb shell cmd appops set " + targetContext().getPackageName() + " SCHEDULE_EXACT_ALARM deny` before the "
        + "tests start (or reinstall the app on Android 14 or later) and run again",
      exactAlarmsAllowed() );
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void acceptingOpensTheSystemPageAndIsNotAskedAgain() throws Exception {

    startAsAFreshInstall();
    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      assertNotNull( "The explanation should appear once the location is in",
        device.wait( Until.findObject( explanation ), DIALOG_TIMEOUT_MS ) );
      click( EXPLANATION_ACCEPT, "the explanation's Open Settings button" );

      assertTrue( "Accepting should open the system page for exact alarms",
        device.wait( Until.hasObject( SYSTEM_SETTINGS ), DIALOG_TIMEOUT_MS ) );
      assertNotNull( "The system page should be the one for alarms and reminders",
        device.wait( Until.findObject( By.text( Pattern.compile( "(?i).*alarms.*reminders.*" ) ) ), DIALOG_TIMEOUT_MS ) );
      assertTrue( answerRecorded() );

      device.pressBack();
      assertFalse( exactAlarmsAllowed() );
    }
    assertNotAskedOnRelaunch();
  }


  @Test
  public void decliningOpensNothingAndIsNotAskedAgain() throws Exception {

    startAsAFreshInstall();
    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      assertNotNull( "The explanation should appear once the location is in",
        device.wait( Until.findObject( explanation ), DIALOG_TIMEOUT_MS ) );
      click( EXPLANATION_DECLINE, "the explanation's No thanks button" );

      assertFalse( "Declining must not open the system page",
        device.wait( Until.hasObject( SYSTEM_SETTINGS ), ABSENCE_TIMEOUT_MS ) );
      assertTrue( answerRecorded() );
    }
    assertNotAskedOnRelaunch();
    assertPreferencesLeadToTheSystemPage();
  }


  /** Saved state as a fresh install with a static location has it, with the notification question answered. */
  private static void startAsAFreshInstall() throws Exception {
    AppState.installStaticLocation();
    AppState.markNotificationQuestionAnswered();
  }


  /** Launches again, lets the screen settle on its location, and checks that the explanation does not show up. */
  private void assertNotAskedOnRelaunch() throws Exception {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      ZmanimActivity activity = ViewWaits.activityOf( scenario );
      ViewWaits.until( "the saved location name in the header", () ->
        LaunchSmokeTest.SAVED_LOCATION_NAME.contentEquals( ( (TextView)activity.findViewById( R.id.location ) ).getText() ) );

      assertFalse( "Asked again on a later launch", device.wait( Until.hasObject( explanation ), ABSENCE_TIMEOUT_MS ) );
    }
  }


  /** With notifications wanted but exact alarms denied, the preferences show a row that opens the system page. */
  private void assertPreferencesLeadToTheSystemPage() {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      click( By.text( targetContext().getString( R.string.exact_alarms_denied_pref_title ) ),
        "the preferences' late alerts row" );

      assertTrue( "The row should open the system page",
        device.wait( Until.hasObject( SYSTEM_SETTINGS ), DIALOG_TIMEOUT_MS ) );
      device.pressBack();
    }
  }


  private void click(
    BySelector  selector,
    String      what
  ) {

    UiObject2 button = device.wait( Until.findObject( selector ), DIALOG_TIMEOUT_MS );
    assertNotNull( "Never saw " + what, button );
    button.click();
  }


  private static boolean answerRecorded() {
    return targetContext().getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE )
      .getBoolean( ExactAlarmPermission.ASKED_KEY, false );
  }


  private static boolean exactAlarmsAllowed() {
    return targetContext().getSystemService( AlarmManager.class ).canScheduleExactAlarms();
  }


  private static Context targetContext() {
    return InstrumentationRegistry.getInstrumentation().getTargetContext();
  }

}
