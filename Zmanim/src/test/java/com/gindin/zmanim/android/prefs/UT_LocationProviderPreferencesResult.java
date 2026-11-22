/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * The main screen looks the location up again only when the location preferences screen reports a change, so the
 *  screen must report one however it is left.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 34 )
public class UT_LocationProviderPreferencesResult {

  private SharedPreferences                                 prefs;


  @Before
  public void startWithATypedPlace() {

    prefs = ApplicationProvider.getApplicationContext()
      .getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    prefs.edit()
      .putBoolean( LocationProviders.STATIC_POSITION.name(), true )
      .putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Albany" )
      .commit();
  }


  @After
  public void forgetThePreferences() {
    prefs.edit().clear().commit();
  }


  @Test
  public void finishingAfterAChangeReportsIt() {

    Activity screen = Robolectric.setupActivity( LocationProviderPreferences.class );
    prefs.edit().putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Jerusalem" ).commit();

    screen.finish();

    assertEquals( Activity.RESULT_OK, Shadows.shadowOf( screen ).getResultCode() );
  }


  @Test
  public void finishingWithoutAChangeReportsNone() {

    Activity screen = Robolectric.setupActivity( LocationProviderPreferences.class );

    screen.finish();

    assertEquals( Activity.RESULT_CANCELED, Shadows.shadowOf( screen ).getResultCode() );
  }

}
