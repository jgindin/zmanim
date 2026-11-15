/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanim.android.upgrade.UpgradeFixtures;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.TimeZone;

/**
 * The cached locations file written by the released app answers {@link LocationToAddressConverter}'s lookups once
 *  the converter starts over it. The geocoder knows no place, so each answer comes from the cache or the bundled
 *  place data, and the tests tell the two apart by name or elevation.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_UpgradeCachedLocations {

  private Context                                           context;

  private LocationToAddressConverter                        converter;


  @Before
  public void startConverterOverFilesWrittenByReleasedApp() throws Exception {

    context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );
    PlaceResolution.forgetShared();

    // The flag is process-wide and is left set by any earlier test in this JVM that ran the app's upgrade steps over
    //  a fresh install (launching the main activity does); starting the service with it set would wipe the cache
    //  instead of loading the fixture.
    LocationToAddressConverter.wipeCacheOnStart = false;

    converter = Robolectric.setupService( LocationToAddressConverter.class );
    converter.namePlacesWith( PlaceResolution.create( context, Mockito.mock( Geocoder.class ) ) );
    ZmanimServiceRequests.drainIntents();
  }


  @After
  public void forgetTheSharedCache() {
    PlaceResolution.forgetShared();
  }


  private ZmanimLocation resolved( ZmanimLocation location ) {

    LocationToAddressConverter.enqueueWork( context, location, LocationToAddressConverter.Actions.RESOLVE_PLACE );
    for ( Intent work : ZmanimServiceRequests.startingService( ZmanimServiceRequests.drainIntents(),
      LocationToAddressConverter.class ) ) {
      converter.onHandleWork( work );
    }

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    Assert.assertEquals( "LOCATION_ACQUIRED", requests.get( 0 ).action );
    return requests.get( 0 ).location;
  }


  @Test
  public void cachedEntryWithoutUserNameAnswersACoordinateLookup() {

    // Roughly 100 meters from the cached Albany entry, which has no user name, and at about the same elevation.
    ZmanimLocation nearAlbany = new ZmanimLocation.Builder()
      .atLatitude( 42.6531 )
      .atLongitude( -73.7568 )
      .atElevation( 45.0 )
      .inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
      .build();

    ZmanimLocation resolved = resolved( nearAlbany );

    // The bundled data would name it "Albany, United States".
    Assert.assertEquals( "Albany, New York", resolved.getLocationName() );
    Assert.assertEquals( 47.0, resolved.getElevation(), 1e-9 );
  }


  @Test
  public void cachedEntryAnswersALookupByTheNameTheUserTyped() {

    ZmanimLocation typed = new ZmanimLocation.Builder()
      .locationName( "Saratoga Springs, NY" )
      .userName( "Saratoga Springs, NY" )
      .build();

    ZmanimLocation resolved = resolved( typed );

    Assert.assertEquals( "Saratoga Springs, New York", resolved.getLocationName() );
    Assert.assertEquals( 43.0831, resolved.getLatitude(), 1e-9 );
    Assert.assertEquals( -73.7846, resolved.getLongitude(), 1e-9 );
  }


  @Test
  public void cachedEntryWithEmptyUserNameAnswersACoordinateLookup() {

    ZmanimLocation nearJerusalem = new ZmanimLocation.Builder()
      .atLatitude( 31.7690 )
      .atLongitude( 35.2140 )
      .atElevation( 750.0 )
      .inTimeZone( TimeZone.getTimeZone( "Asia/Jerusalem" ) )
      .build();

    ZmanimLocation resolved = resolved( nearJerusalem );

    // The bundled data would keep the fix's own elevation, 750 m.
    Assert.assertEquals( "Jerusalem, Israel", resolved.getLocationName() );
    Assert.assertEquals( 754.0, resolved.getElevation(), 1e-9 );
  }


  @Test
  public void aLocationFarFromEveryCachedEntryIsNotAnsweredFromTheCache() {

    ZmanimLocation farAway = new ZmanimLocation.Builder()
      .atLatitude( 51.5072 )
      .atLongitude( -0.1276 )
      .inTimeZone( TimeZone.getTimeZone( "Europe/London" ) )
      .build();

    Assert.assertEquals( "London, United Kingdom", resolved( farAway ).getLocationName() );
  }

}
