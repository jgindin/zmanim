/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanim.android.upgrade.UpgradeFixtures;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * The cached locations file written by the released app is loaded when {@link LocationToAddressConverter} starts,
 *  and the cached entries then answer lookups.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
public class UT_UpgradeCachedLocations {

  private CachedLocationResolver                            resolver;


  @Before
  public void startConverterOverFilesWrittenByReleasedApp() throws Exception {

    Context context = ApplicationProvider.getApplicationContext();
    UpgradeFixtures.install( context );

    // The flag is process-wide and is left set by any earlier test in this JVM that ran the app's upgrade steps over
    //  a fresh install (launching the main activity does); starting the service with it set would wipe the cache
    //  instead of loading the fixture.
    LocationToAddressConverter.wipeCacheOnStart = false;

    // Starting the service is what loads the cache, through its own file access.
    LocationToAddressConverter converter = Robolectric.setupService( LocationToAddressConverter.class );

    Field field = LocationToAddressConverter.class.getDeclaredField( "cachedLocationResolver" );
    field.setAccessible( true );
    resolver = (CachedLocationResolver)field.get( converter );
  }


  @Test
  public void loadsEveryCachedLocation() {

    Map<String, ZmanimLocation> byName = new HashMap<>();
    for ( ZmanimLocation location : resolver.locationCache ) {
      byName.put( location.getLocationName(), location );
    }

    Assert.assertEquals( 4, resolver.locationCache.size() );
    Assert.assertEquals( 4, byName.size() );
    Assert.assertEquals( "", byName.get( "Albany, New York" ).originalUserName );
    Assert.assertEquals( "Saratoga Springs, NY", byName.get( "Saratoga Springs, New York" ).originalUserName );
    Assert.assertEquals( "", byName.get( "Jerusalem, Israel" ).originalUserName );
    Assert.assertEquals( "", byName.get( "New York, New York" ).originalUserName );
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

    ZmanimLocation resolved = resolver.resolveFromCoordinates( nearAlbany );

    Assert.assertNotNull( resolved );
    Assert.assertEquals( "Albany, New York", resolved.getLocationName() );
    Assert.assertEquals( 47.0, resolved.getElevation(), 1e-9 );
  }


  @Test
  public void cachedEntryAnswersALookupByTheNameTheUserTyped() {

    ZmanimLocation typed = new ZmanimLocation.Builder()
      .locationName( "Saratoga Springs, NY" )
      .userName( "Saratoga Springs, NY" )
      .build();

    ZmanimLocation resolved = resolver.resolveFromName( typed );

    Assert.assertNotNull( resolved );
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

    ZmanimLocation resolved = resolver.resolveFromCoordinates( nearJerusalem );

    Assert.assertNotNull( resolved );
    Assert.assertEquals( "Jerusalem, Israel", resolved.getLocationName() );
    Assert.assertEquals( 754.0, resolved.getElevation(), 1e-9 );
  }


  @Test
  public void aLocationFarFromEveryCachedEntryIsNotAnswered() {

    ZmanimLocation farAway = new ZmanimLocation.Builder()
      .atLatitude( 51.5072 )
      .atLongitude( -0.1276 )
      .inTimeZone( TimeZone.getTimeZone( "Europe/London" ) )
      .build();

    Assert.assertNull( resolver.resolveFromCoordinates( farAway ) );
  }

}
