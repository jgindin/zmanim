/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.feedback;

import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.TimeZone;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The Feedback menu item opens a dialog whose Send button hands the user's text, the app version and the current
 *  location to the user's own email app, addressed to the developers. Nothing is sent by the app itself.
 */
@RunWith( RobolectricTestRunner.class )
// Below Android 13 the app does not ask about notifications, so no permission dialog competes with the feedback one.
@Config( sdk = 32 )
public class UT_FeedbackFragment {

  private static final String FEEDBACK = "The candle lighting time looks a minute early.";

  /** 2023-11-14T22:13:20Z, which is 17:13:20 in New York. */
  private static final long LOCATION_TIME = 1_700_000_000_000L;

  private static final String EMAIL_APP = "com.example.mail";


  @Before
  public void allowExactAlarms() {
    ShadowAlarmManager.setCanScheduleExactAlarms( true );
  }


  /** Robolectric's package manager starts with no apps, so give it an email app, as most devices have. */
  @Before
  public void installAnEmailApp() {

    ResolveInfo emailApp = new ResolveInfo();
    emailApp.activityInfo = new ActivityInfo();
    emailApp.activityInfo.packageName = EMAIL_APP;
    emailApp.activityInfo.name = EMAIL_APP + ".Compose";
    Shadows.shadowOf( context().getPackageManager() ).addResolveInfoForIntent( emailIntent(), emailApp );
  }


  /** Launching the main activity over a fresh install leaves this process-wide flag set; see UT_NotificationPermission. */
  @After
  public void clearProcessWideUpgradeFlag() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  @Test
  public void theMainMenuOffersFeedback() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        MenuItem item = feedbackMenuItem( activity );
        assertNotNull( "The main menu should have a Feedback item", item );
        assertTrue( item.isVisible() );
      } );
    }
  }


  @Test
  public void sendOpensTheEmailAppWithTheFeedbackVersionAndLocation() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation( 12.5f );
        Intent email = sendFeedback( activity );

        assertNotNull( "Send should start the email app", email );
        assertEquals( Intent.ACTION_SENDTO, email.getAction() );
        assertEquals( "mailto:", email.getDataString() );
        assertArrayEquals( new String[] { "zmanim.apps@gmail.com" }, email.getStringArrayExtra( Intent.EXTRA_EMAIL ) );
        assertEquals( "Zmanim feedback (" + BuildConfig.VERSION_NAME + ")", email.getStringExtra( Intent.EXTRA_SUBJECT ) );

        String body = email.getStringExtra( Intent.EXTRA_TEXT );
        assertNotNull( body );
        assertTrue( body, body.startsWith( FEEDBACK + "\n" ) );
        assertContainsLine( body, "App: Zmanim " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")" );
        assertContainsLine( body, "Location name: Albany, New York" );
        assertContainsLine( body, "Latitude: 42.6526" );
        assertContainsLine( body, "Longitude: -73.7562" );
        assertContainsLine( body, "Elevation: 30.5 m" );
        assertContainsLine( body, "Time zone: America/New_York" );
        assertContainsLine( body, "Provider: fused" );
        assertContainsLine( body, "Accuracy: 12.5 m" );
        assertContainsLine( body, "Obtained: 2023-11-14T17:13:20-05:00" );
      } );
    }
  }


  @Test
  public void anUnknownAccuracyIsLeftOut() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation( -1f );
        String body = sendFeedback( activity ).getStringExtra( Intent.EXTRA_TEXT );

        assertContainsLine( body, "Latitude: 42.6526" );
        assertFalse( body, body.contains( "Accuracy" ) );
      } );
    }
  }


  @Test
  public void withoutALocationTheBodySaysSo() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        String body = sendFeedback( activity ).getStringExtra( Intent.EXTRA_TEXT );

        assertContainsLine( body, "App: Zmanim " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")" );
        assertContainsLine( body, "Location: not available" );
        assertFalse( body, body.contains( "Latitude" ) );
      } );
    }
  }


  @Test
  public void anInvalidLocationIsTreatedAsNone() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        FeedbackFragment.newInstance( new ZmanimLocation.Builder().invalidLocation() )
          .show( activity.getSupportFragmentManager(), "feedback" );
        String body = typeAndSend( activity ).getStringExtra( Intent.EXTRA_TEXT );

        assertContainsLine( body, "Location: not available" );
        assertFalse( body, body.contains( "Latitude" ) );
      } );
    }
  }


  @Test
  public void withoutAnEmailAppFeedbackSaysWhereToWriteInsteadOfOpeningTheDialog() {

    uninstallTheEmailApp();

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        openFeedback( activity );

        assertNull( "The feedback dialog would be no use", activity.getSupportFragmentManager().findFragmentByTag( "feedback" ) );
        Dialog explanation = ShadowDialog.getLatestDialog();
        assertTrue( explanation instanceof AlertDialog );
        assertTrue( explanation.isShowing() );
        TextView message = explanation.findViewById( android.R.id.message );
        assertNotNull( message );
        assertEquals( context().getString( R.string.feedback_no_email_app ), message.getText().toString() );
      } );
    }
  }


  @Test
  public void anEmailAppRemovedWhileTheDialogIsOpenIsReportedWithAToast() {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {

      scenario.onActivity( activity -> {

        fireLocation( 12.5f );
        openFeedback( activity );

        uninstallTheEmailApp();
        // Makes starting an activity that nothing handles fail, as it does on a device.
        Shadows.shadowOf( (Application)context() ).checkActivities( true );

        assertNull( "Nothing can handle the email", typeAndSend( activity ) );
        assertEquals( context().getString( R.string.feedback_no_email_app ), ShadowToast.getTextOfLatestToast() );
      } );
    }
  }


  /** Opens Feedback from the main menu, types the feedback and presses Send; returns the activity that was started. */
  private static Intent sendFeedback( ZmanimActivity activity ) {

    openFeedback( activity );
    return typeAndSend( activity );
  }


  private static void openFeedback( ZmanimActivity activity ) {

    MenuItem item = feedbackMenuItem( activity );
    assertNotNull( item );
    assertTrue( activity.onOptionsItemSelected( item ) );
    idle();
  }


  /** What an email app answers to: the email intent's action and data, without its extras. */
  private static Intent emailIntent() {
    return new Intent( Intent.ACTION_SENDTO, Uri.parse( "mailto:" ) );
  }


  private static void uninstallTheEmailApp() {
    Shadows.shadowOf( context().getPackageManager() ).removeResolveInfosForIntent( emailIntent(), EMAIL_APP );
  }


  /** Types the feedback into the shown Feedback dialog and presses Send; returns the activity that was started. */
  private static Intent typeAndSend( ZmanimActivity activity ) {

    idle();
    activity.getSupportFragmentManager().executePendingTransactions();

    DialogFragment fragment = (DialogFragment)activity.getSupportFragmentManager().findFragmentByTag( "feedback" );
    assertNotNull( "Feedback should show its dialog", fragment );
    AlertDialog dialog = (AlertDialog)fragment.requireDialog();

    EditText input = dialog.findViewById( R.id.feedback_input );
    assertNotNull( input );
    input.setText( FEEDBACK );

    // Ignore whatever the activity started while launching.
    while ( Shadows.shadowOf( activity ).getNextStartedActivity() != null ) {
      // Drain.
    }

    dialog.getButton( DialogInterface.BUTTON_POSITIVE ).performClick();
    idle();

    return Shadows.shadowOf( activity ).getNextStartedActivity();
  }


  private static MenuItem feedbackMenuItem( ZmanimActivity activity ) {

    PopupMenu popup = new PopupMenu( activity, activity.getWindow().getDecorView() );
    activity.onCreateOptionsMenu( popup.getMenu() );
    return popup.getMenu().findItem( R.id.feedback );
  }


  private static void fireLocation( float accuracy ) {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .fromProvider( "fused" )
      .locationName( "Albany, New York" )
      .atLatitude( 42.6526 )
      .atLongitude( -73.7562 )
      .atElevation( 30.5 )
      .withAccuracy( accuracy )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .at( LOCATION_TIME )
      .build();

    ZmanimService.ZMANIM_EVENT_BUS.fire( new LocationAcquiredEvent( location ) );
    idle();
  }


  private static void assertContainsLine(
    String body,
    String line
  ) {
    assertTrue( "Expected the line \"" + line + "\" in:\n" + body, ( "\n" + body + "\n" ).contains( "\n" + line + "\n" ) );
  }


  private static void idle() {
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private static Context context() {
    return ApplicationProvider.getApplicationContext();
  }

}  // End of UT_FeedbackFragment class
