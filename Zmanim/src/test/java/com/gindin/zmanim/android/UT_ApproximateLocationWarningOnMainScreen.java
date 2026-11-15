/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.messageDisplay.ApproximateLocationWarning;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.JobIntentService;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * When the main screen shows {@link ApproximateLocationWarning}: for the device's location under an approximate-only
 *  grant, once, ahead of the notification question.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
@SuppressWarnings( "MagicNumber" )
public class UT_ApproximateLocationWarningOnMainScreen {

  private final Context                                     context = ApplicationProvider.getApplicationContext();


  @Before
  public void startWithTheDevicesLocationChosen() throws Exception {

    Field enqueuers = JobIntentService.class.getDeclaredField( "sClassWorkEnqueuer" );
    enqueuers.setAccessible( true );
    ( (Map<?, ?>)enqueuers.get( null ) ).clear();

    ExactAlarmPermission.markAsked( context );
    useTheDevicesLocation( true );
    ShadowAlertDialog.reset();
  }


  /** Launching the main activity runs the app's upgrade steps, which leave this process-wide flag set. */
  @After
  public void restoreProcessWideState() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  private void useTheDevicesLocation( boolean device ) {
    context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE ).edit()
      .putBoolean( LocationProviders.DYNAMIC_POSITION.name(), device )
      .putBoolean( LocationProviders.STATIC_POSITION.name(), !device )
      .commit();
  }


  private void grant( String... permissions ) {
    Shadows.shadowOf( (Application)context ).grantPermissions( permissions );
  }


  private static void idle() {
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
  }


  private static void locationArrives() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .locationName( "Yonkers, NY" )
      .atLatitude( 40.9312 )
      .atLongitude( -73.8988 )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .build();
    ZmanimService.ZMANIM_EVENT_BUS.fire( new LocationAcquiredEvent( location ) );
    idle();
  }


  /** Every warning on screen, by its message. */
  private List<View> warningsOn( ZmanimActivity activity ) {

    String message = context.getString( R.string.approximate_location_warning );
    List<View> found = new ArrayList<>();
    ArrayList<View> matches = new ArrayList<>();
    activity.getWindow().getDecorView().findViewsWithText( matches, message, View.FIND_VIEWS_WITH_TEXT );
    for ( View view : matches ) {
      if ( view.isShown() && ( view instanceof TextView ) ) {
        found.add( view );
      }
    }
    return found;
  }


  private void onTheMainScreen( Consumer<ZmanimActivity> check ) {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {
      scenario.onActivity( activity -> {
        locationArrives();
        check.accept( activity );
      } );
    }
  }


  @Test
  public void theDevicesLocationUnderAnApproximateOnlyGrantShowsTheWarningOnce() {

    grant( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS );

    onTheMainScreen( activity -> {
      List<View> shown = warningsOn( activity );
      assertEquals( 1, shown.size() );

      // The warning already up stays, rather than being replaced by a new one.
      locationArrives();
      assertEquals( shown, warningsOn( activity ) );
    } );
  }


  @Test
  public void aPreciseGrantShowsNoWarning() {

    grant( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.POST_NOTIFICATIONS );

    onTheMainScreen( activity -> assertTrue( warningsOn( activity ).isEmpty() ) );
  }


  @Test
  public void aTypedPlaceShowsNoWarning() {

    grant( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS );
    useTheDevicesLocation( false );

    onTheMainScreen( activity -> assertTrue( warningsOn( activity ).isEmpty() ) );
  }


  @Test
  public void aDismissedWarningIsNotShownAgainForTheSameGrant() {

    grant( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS );
    context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE ).edit()
      .putBoolean( ApproximateLocationWarning.DISMISSED_KEY, true )
      .commit();

    onTheMainScreen( activity -> assertTrue( warningsOn( activity ).isEmpty() ) );
  }


  @Test
  public void theNotificationQuestionWaitsWhileTheWarningIsUp() {

    grant( Manifest.permission.ACCESS_COARSE_LOCATION );

    onTheMainScreen( activity -> {
      assertEquals( 1, warningsOn( activity ).size() );
      assertNull( ShadowAlertDialog.getLatestAlertDialog() );

      activity.findViewById( R.id.approximate_location_ok ).performClick();
      idle();
      locationArrives();
      assertNotNull( ShadowAlertDialog.getLatestAlertDialog() );
    } );
  }


  @Test
  public void usePreciseAsksForPreciseLocation() {

    grant( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS );

    onTheMainScreen( activity -> {
      Button usePrecise = activity.findViewById( com.google.android.material.R.id.snackbar_action );
      usePrecise.performClick();
      idle();

      ShadowActivity.PermissionsRequest request = Shadows.shadowOf( activity ).getLastRequestedPermission();
      assertEquals( LocationError.Code.NO_PERMISSIONS.ordinal(), request.requestCode );
      assertArrayEquals(
        new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
        request.requestedPermissions );
      assertTrue( warningsOn( activity ).isEmpty() );
    } );
  }

}  // End of UT_ApproximateLocationWarningOnMainScreen class
