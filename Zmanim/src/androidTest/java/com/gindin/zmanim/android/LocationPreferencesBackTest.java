/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.LocationProviderPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Leaving the location preferences screen with the system's Back must tell the main screen whether the location
 *  changed, or the main screen keeps showing the old place. From Android 16 the system's predictive back finishes the
 *  screen without calling onBackPressed(), so this runs the real Back on each emulator.
 */
@RunWith( AndroidJUnit4.class )
public class LocationPreferencesBackTest {

  @Before
  public void startWithATypedPlace() throws IOException {

    AppState.assumeSafeToWipe();
    AppState.reset();
    AppState.useTypedPlace( "Albany" );
  }


  @After
  public void cleanUp() throws IOException {
    AppState.reset();
  }


  @Test
  public void backAfterChangingThePlaceReportsTheChange() {

    try ( ActivityScenario<LocationProviderPreferences> screen =
            ActivityScenario.launchActivityForResult( LocationProviderPreferences.class ) ) {

      screen.onActivity( activity -> AppState.edit( ZmanimPrefs.LOCATION_MANAGEMENT ).putString( "PLACE", "Jerusalem" ).commit() );
      pressBack();

      assertEquals( Activity.RESULT_OK, screen.getResult().getResultCode() );
    }
  }


  @Test
  public void backWithoutAChangeReportsNone() {

    try ( ActivityScenario<LocationProviderPreferences> screen =
            ActivityScenario.launchActivityForResult( LocationProviderPreferences.class ) ) {

      screen.onActivity( activity -> {} );
      pressBack();

      assertEquals( Activity.RESULT_CANCELED, screen.getResult().getResultCode() );
    }
  }


  private static void pressBack() {
    UiDevice.getInstance( InstrumentationRegistry.getInstrumentation() ).pressBack();
  }

}
