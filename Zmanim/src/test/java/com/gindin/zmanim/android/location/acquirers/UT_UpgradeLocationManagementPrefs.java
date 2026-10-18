/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.upgrade.UpgradeFixtures;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Set;
import java.util.TimeZone;

/**
 * The location management preferences written by the released app select a location provider, hold the coordinates
 *  or place the user entered, and override the time zone.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_UpgradeLocationManagementPrefs {

  private Context                                           context;

  private SharedPreferences                                 prefs;


  @Before
  public void installPrefsWrittenByReleasedApp() throws IOException {

    context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );

    prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
  }


  @Test
  public void theTimeZoneTheUserChoseOverridesTheSystemOne() {

    Assert.assertEquals( TimeZone.getTimeZone( "America/Chicago" ), LocationAcquirer.getTimeZoneFromPrefs( prefs ) );
  }


  @Test
  public void theSelectedProviderIsRecognized() {

    EventBus eventBus = new EventBus();
    new ZmanimLocationManager( context, eventBus, prefs );

    // The manager falls back to the device's location, and records that choice, only when it finds no selected
    //  provider.
    eventBus.fire( new RequestLocationUpdateEvent( RequestLocationUpdateEvent.CachedLocationUsage.NEVER ) );

    Assert.assertFalse( prefs.getBoolean( "DYNAMIC_POSITION", true ) );
    Assert.assertFalse( prefs.getBoolean( "STATIC_POSITION", true ) );
    Assert.assertTrue( prefs.getBoolean( "LATITUDE_LONGITUDE_POSITION", false ) );
  }


  @Test
  public void theCoordinatesTheUserEnteredAreParsed() {

    // The stored text is degrees, minutes and seconds ("N 42 39 9.36..."); only whole seconds are read from it.
    Assert.assertEquals( 42.6525,
      LatitudeLongitudeParser.latitudeValue( prefs.getString( "LATITUDE", null ) ), 1e-9 );
    Assert.assertEquals( -73.75611111111111,
      LatitudeLongitudeParser.longitudeValue( prefs.getString( "LONGITUDE", null ) ), 1e-9 );
    Assert.assertEquals( 47.0, Double.parseDouble( prefs.getString( "LATITUDE_LONGITUDE_ELEVATION", null ) ), 1e-9 );
  }


  @Test
  public void thePlaceTheUserEnteredIsRead() {

    Assert.assertEquals( "Lake Placid, New York", prefs.getString( "PLACE", null ) );
    Assert.assertEquals( "570", prefs.getString( "PLACE_ELEVATION", null ) );
  }


  @Test
  public void theKeysInTheSavedPrefsAreTheNamesOfTheCurrentPreferenceConstants() {

    Set<String> constantNames = Set.of(
      LocationProviders.DYNAMIC_POSITION.name(),
      LocationProviders.STATIC_POSITION.name(),
      LocationProviders.LATITUDE_LONGITUDE_POSITION.name(),
      LocationProviders.StaticProviderOptions.PLACE.name(),
      LocationProviders.StaticProviderOptions.PLACE_ELEVATION.name(),
      LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(),
      LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(),
      LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name(),
      LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name(),
      LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(),
      LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name() );

    Assert.assertEquals( constantNames, prefs.getAll().keySet() );
  }

}
