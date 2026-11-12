/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

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

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * What the main screen does with the user's answer to the system's location prompt. From Android 12 the user can
 *  allow approximate location only, which the app accepts.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_LocationPermissionAnswer {

  private static final String[]                             ASKED = {
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
  };


  @Before
  public void startWithTheDevicesLocationChosen() throws Exception {

    Field enqueuers = JobIntentService.class.getDeclaredField( "sClassWorkEnqueuer" );
    enqueuers.setAccessible( true );
    ( (Map<?, ?>)enqueuers.get( null ) ).clear();

    Shadows.shadowOf( (Application)ApplicationProvider.getApplicationContext() )
      .grantPermissions( Manifest.permission.POST_NOTIFICATIONS );
  }


  /** Launching the main activity runs the app's upgrade steps, which leave this process-wide flag set. */
  @After
  public void restoreProcessWideState() {
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  private static SharedPreferences locationPrefs() {
    return ApplicationProvider.<Context>getApplicationContext()
      .getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
  }


  /** Answers the prompt with the specified results for precise and approximate location. */
  private static boolean usesTheDevicesLocationAfterAnswering( int precise, int approximate ) {

    try ( ActivityScenario<ZmanimActivity> scenario = ActivityScenario.launch( ZmanimActivity.class ) ) {
      scenario.onActivity( activity -> {
        locationPrefs().edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
        activity.onRequestPermissionsResult( LocationError.Code.NO_PERMISSIONS.ordinal(), ASKED,
          new int[] { precise, approximate } );
      } );
    }

    return locationPrefs().getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
  }


  @Test
  public void allowingPreciseLocationKeepsTheDevicesLocation() {
    assertTrue( usesTheDevicesLocationAfterAnswering( PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED ) );
  }


  @Test
  public void allowingApproximateLocationOnlyKeepsTheDevicesLocation() {
    assertTrue( usesTheDevicesLocationAfterAnswering( PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED ) );
  }


  @Test
  public void refusingLocationSwitchesToAPlaceTheUserGives() {
    assertFalse( usesTheDevicesLocationAfterAnswering( PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_DENIED ) );
  }

}  // End of UT_LocationPermissionAnswer class
