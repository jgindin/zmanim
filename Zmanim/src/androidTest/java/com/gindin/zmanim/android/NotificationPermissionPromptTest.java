/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;

import android.Manifest;
import android.app.UiAutomation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * On Android 13 and later, a fresh install with location access already granted explains why it wants to post
 *  notifications, asks the system only if the user agrees, and never asks again on a later launch, whatever the answer.
 *<p/>
 *
 * The system permission prompt belongs to another app, so it is driven through UI Automator.
 *<p/>
 *
 * A granted runtime permission cannot be taken back from inside the test: revoking it kills the app's process, which
 *  is the process the test runs in. So the answers are walked through in one test, in the order that leaves the
 *  permission revocable until the end: declining the explanation, then refusing the system prompt, then allowing it.
 *  Each step starts over from fresh saved state, and clears the system's record of earlier answers (which does not
 *  kill the process) so that the system prompt may appear again. The permission must not be granted when the test
 *  starts, which holds after Gradle installs the app because no other test grants it (see
 *  {@link AppState#grantLocationPermissions()}).
 */
@RunWith( AndroidJUnit4.class )
public class NotificationPermissionPromptTest {

  private static final long                                 DIALOG_TIMEOUT_MS = 15_000;

  /** How long a dialog that must not appear is given to appear anyway, once the screen has its location. */
  private static final long                                 ABSENCE_TIMEOUT_MS = 4_000;

  /** The Allow and Don't allow buttons of the system permission prompt, whichever permission controller ships. */
  private static final BySelector                           SYSTEM_ALLOW = By.res( Pattern.compile( ".*:id/permission_allow_button" ) );
  private static final BySelector                           SYSTEM_DENY = By.res( Pattern.compile( ".*:id/permission_deny_button" ) );
  private static final BySelector                           ANY_SYSTEM_BUTTON =
    By.res( Pattern.compile( ".*:id/permission_(allow|deny)_button" ) );

  /** The buttons of the app's own explanation (a platform AlertDialog). */
  private static final BySelector                           EXPLANATION_ACCEPT = By.res( "android:id/button1" );
  private static final BySelector                           EXPLANATION_DECLINE = By.res( "android:id/button2" );

  @Rule
  public final GrantPermissionRule                          location = AppState.grantLocationPermissions();

  private UiDevice                                          device;

  private BySelector                                        explanation;


  @Before
  public void setUp() {

    Assume.assumeTrue( "The notification permission only exists from Android 13 on",
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU );
    AppState.assumeSafeToWipe();

    device = UiDevice.getInstance( InstrumentationRegistry.getInstrumentation() );
    explanation = By.text( targetContext().getString( R.string.notification_permission_title ) );

    assertFalse( "The notification permission is already granted, and the test cannot revoke it from inside the app's "
      + "process; reinstall the app (Gradle's connected test tasks do) and run again", isNotificationPermissionGranted() );
  }


  @After
  public void clearState() throws Exception {
    AppState.reset();
  }


  @Test
  public void everyAnswerIsKeptAndTheUserIsNeverAskedTwice() throws Exception {

    // Declining the explanation: the system prompt never appears.
    startAsAFreshInstall();
    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      click( EXPLANATION_DECLINE, "the explanation's No thanks button" );

      assertFalse( "Declining must not lead to the system prompt",
        device.wait( Until.hasObject( ANY_SYSTEM_BUTTON ), ABSENCE_TIMEOUT_MS ) );
      assertFalse( isNotificationPermissionGranted() );
    }
    assertNotAskedOnRelaunch();
    assertPreferencesLeadToTheSystemSettings();

    // Accepting the explanation, then refusing the system prompt: the app says how to turn notifications on later.
    startAsAFreshInstall();
    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      click( EXPLANATION_ACCEPT, "the explanation's Turn on button" );
      click( SYSTEM_DENY, "the system prompt's Don't allow button" );

      assertNotNull( "Refusing should say how to turn notifications on later",
        device.wait( Until.findObject( By.text( targetContext().getString( R.string.notification_permission_denied ) ) ),
          DIALOG_TIMEOUT_MS ) );
      assertFalse( isNotificationPermissionGranted() );
    }
    assertNotAskedOnRelaunch();

    // Accepting the explanation, then allowing: the permission is granted.
    startAsAFreshInstall();
    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      click( EXPLANATION_ACCEPT, "the explanation's Turn on button" );
      click( SYSTEM_ALLOW, "the system prompt's Allow button" );

      assertTrue( "Allowing must grant the permission",
        device.wait( ignored -> isNotificationPermissionGranted(), DIALOG_TIMEOUT_MS ) );
    }
    assertNotAskedOnRelaunch();
  }


  /**
   * Saved state as a fresh install with a static location has it, and no record of an earlier answer to the system
   *  prompt (after two refusals the system would otherwise stop showing it at all).
   */
  private static void startAsAFreshInstall() throws IOException {

    AppState.installStaticLocation();
    shell( "pm clear-permission-flags " + targetContext().getPackageName() + " "
      + Manifest.permission.POST_NOTIFICATIONS + " user-set user-fixed" );
  }


  /** Launches again, lets the screen settle on its location, and checks that neither prompt shows up. */
  private void assertNotAskedOnRelaunch() throws Exception {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      ZmanimActivity activity = ViewWaits.activityOf( scenario );
      ViewWaits.until( "the saved location name in the header", () ->
        LaunchSmokeTest.SAVED_LOCATION_NAME.contentEquals( ( (TextView)activity.findViewById( R.id.location ) ).getText() ) );

      assertFalse( "Asked again on a later launch", device.wait( Until.hasObject( explanation ), ABSENCE_TIMEOUT_MS ) );
      assertFalse( device.hasObject( ANY_SYSTEM_BUTTON ) );
    }
  }


  /** With notifications wanted but not permitted, the preferences show a row that opens the system settings. */
  private void assertPreferencesLeadToTheSystemSettings() {

    try ( ActivityScenario<PersonalPreferences> scenario = ActivityScenario.launch( PersonalPreferences.class ) ) {

      click( By.text( targetContext().getString( R.string.notifications_blocked_pref_title ) ),
        "the preferences' blocked notifications row" );

      assertTrue( "The row should open the system settings",
        device.wait( Until.hasObject( By.pkg( Pattern.compile( ".*settings.*" ) ).depth( 0 ) ), DIALOG_TIMEOUT_MS ) );
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


  private static boolean isNotificationPermissionGranted() {
    return targetContext().checkSelfPermission( Manifest.permission.POST_NOTIFICATIONS ) == PackageManager.PERMISSION_GRANTED;
  }


  private static void shell( String command ) throws IOException {

    UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
    try (
      ParcelFileDescriptor output = automation.executeShellCommand( command );
      InputStream in = new FileInputStream( output.getFileDescriptor() )
    ) {
      byte[] buffer = new byte[ 1024 ];
      StringBuilder text = new StringBuilder();
      int count;
      while ( ( count = in.read( buffer ) ) != -1 ) {
        text.append( new String( buffer, 0, count ) );
      }
      assertEquals( "Unexpected output from: " + command, "", text.toString().trim() );
    }
  }


  private static Context targetContext() {
    return InstrumentationRegistry.getInstrumentation().getTargetContext();
  }

}
