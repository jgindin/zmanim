/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * What {@link LocationToAddressConverter} tells the rest of the app after looking up a location, through the app's
 *  chain (the cache, the geocoder, the bundled place data) with a Mockito geocoder in place of the system one. The
 *  work goes in the way the app sends it, with {@link LocationToAddressConverter#enqueueWork}.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_LocationToAddressConverter {

  private static final TimeZone                             NEW_YORK = TimeZone.getTimeZone( "America/New_York" );

  private Context                                           context;

  private Geocoder                                          geocoder;

  private LocationToAddressConverter                        converter;


  @Before
  public void setup() {

    context = ApplicationProvider.getApplicationContext();
    context.deleteFile( FileGeocodeCache.FILE_NAME );
    context.deleteFile( FileGeocodeCache.LEGACY_FILE_NAME );
    PlaceResolution.forgetShared();
    LocationToAddressConverter.wipeCacheOnStart = false;

    geocoder = Mockito.mock( Geocoder.class );
    converter = started();

    ZmanimServiceRequests.drainIntents();
  }


  @After
  public void forgetTheSharedCache() {
    PlaceResolution.forgetShared();
    LocationToAddressConverter.wipeCacheOnStart = false;
  }


  /** A converter that asks {@link #geocoder} in place of the system one. */
  private LocationToAddressConverter started() {

    LocationToAddressConverter started = Robolectric.setupService( LocationToAddressConverter.class );
    started.namePlacesWith( PlaceResolution.create( context, geocoder ) );
    return started;
  }


  /** Sends the work the way the app does, and lets the converter handle it. */
  private ZmanimServiceRequests.Request resolve( ZmanimLocation location, LocationToAddressConverter.Actions action ) {

    LocationToAddressConverter.enqueueWork( context, location, action );
    List<Intent> work = ZmanimServiceRequests.startingService( ZmanimServiceRequests.drainIntents(),
      LocationToAddressConverter.class );
    Assert.assertEquals( 1, work.size() );

    converter.onHandleWork( work.get( 0 ) );

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    return requests.get( 0 );
  }


  private ZmanimServiceRequests.Request resolvePlace( ZmanimLocation location ) {
    return resolve( location, LocationToAddressConverter.Actions.RESOLVE_PLACE );
  }


  private static Address albany() {
    Address address = new Address( Locale.US );
    address.setLocality( "Albany" );
    address.setAdminArea( "New York" );
    address.setLatitude( 42.6526 );
    address.setLongitude( -73.7562 );
    return address;
  }


  private static ZmanimLocation fix( double latitude, double longitude ) {
    return new ZmanimLocation.Builder()
      .fromProvider( "fused" )
      .atLatitude( latitude )
      .atLongitude( longitude )
      .atElevation( 30 )
      .inTimeZone( NEW_YORK )
      .build();
  }


  private static ZmanimLocation nearAlbany() {
    return fix( 42.65, -73.76 );
  }


  /** In the North Atlantic, far from any place in the bundled data. */
  private static ZmanimLocation atSea() {
    return fix( 35.0, -45.0 );
  }


  private static ZmanimLocation typed( String name ) {
    return new ZmanimLocation.Builder()
      .locationName( name )
      .userName( name )
      .inTimeZone( NEW_YORK )
      .build();
  }


  @Test
  public void aDeviceFixIsNamedByTheGeocoderAndCached() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenReturn( Collections.singletonList( albany() ) );

    ZmanimServiceRequests.Request request = resolvePlace( nearAlbany() );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );

    // The next fix nearby is answered from the cache, without the geocoder.
    Mockito.reset( geocoder );
    request = resolvePlace( nearAlbany() );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( geocoder );
  }


  /** The address the geocoder picks is only near the fix; the fix is where the user is. */
  @Test
  public void aNamedFixKeepsItsCoordinatesElevationAndTimeZone() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenReturn( Collections.singletonList( albany() ) );

    ZmanimLocation named = resolvePlace( nearAlbany() ).location;

    Assert.assertEquals( 42.65, named.getLatitude(), 1e-9 );
    Assert.assertEquals( -73.76, named.getLongitude(), 1e-9 );
    Assert.assertEquals( 30, named.getElevation(), 1e-9 );
    Assert.assertEquals( NEW_YORK, named.getTimeZone() );
  }


  @Test
  public void offlineTheBundledDataNamesAFixNearATown() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( nearAlbany() );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Albany, United States", request.location.getLocationName() );
    Assert.assertEquals( 42.65, request.location.getLatitude(), 1e-9 );
  }


  /** So the zmanim can still be shown: the fix comes back under its coordinates. */
  @Test
  public void aFixNoSourceCanNameIsAcquiredUnderItsCoordinates() throws IOException {

    Mockito.when( geocoder.getFromLocation( 35.0, -45.0, 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( atSea() );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( ZmanimLocation.toLatLongString( 35.0, -45.0 ), request.location.getLocationName() );
    Assert.assertEquals( 35.0, request.location.getLatitude(), 1e-9 );
  }


  /** A fix named after its coordinates is not cached, so once the geocoder works again it names the place. */
  @Test
  public void aFixNamedAfterItsCoordinatesIsNamedOnceTheGeocoderWorksAgain() throws IOException {

    Mockito.when( geocoder.getFromLocation( 35.0, -45.0, 5 ) ).thenThrow( new IOException( "no network" ) );
    resolvePlace( atSea() );

    Address ocean = new Address( Locale.US );
    ocean.setAdminArea( "North Atlantic Ocean" );
    Mockito.reset( geocoder );
    Mockito.when( geocoder.getFromLocation( 35.0, -45.0, 5 ) ).thenReturn( Collections.singletonList( ocean ) );

    Assert.assertEquals( "North Atlantic Ocean", resolvePlace( atSea() ).location.getLocationName() );
  }


  /**
   * In the Negev, 29 km (18 miles) from Dimona, the nearest place in the bundled data: too far to be named after it, near
   * enough to be near it.
   */
  private static final double                               NEGEV_LATITUDE = 30 + 51 / 60.0;
  private static final double                               NEGEV_LONGITUDE = 34 + 52 / 60.0;

  private static ZmanimLocation inTheNegev() {
    return fix( NEGEV_LATITUDE, NEGEV_LONGITUDE );
  }


  @Test
  public void offlineAFixTooFarFromATownToBeNamedAfterItIsNamedNearTheNearestOne() throws IOException {

    Mockito.when( geocoder.getFromLocation( NEGEV_LATITUDE, NEGEV_LONGITUDE, 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( inTheNegev() );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Near Dimona, Israel", request.location.getLocationName() );
    Assert.assertEquals( NEGEV_LATITUDE, request.location.getLatitude(), 1e-9 );
    Assert.assertEquals( NEGEV_LONGITUDE, request.location.getLongitude(), 1e-9 );
    Assert.assertEquals( 30, request.location.getElevation(), 1e-9 );
  }


  /** Farther into the Negev, 57 km (35 miles) from Dimona, the nearest place in the bundled data: too far to be near it. */
  @Test
  public void offlineAFixMoreThanTwentyMilesFromEveryTownIsAcquiredUnderItsCoordinates() throws IOException {

    Mockito.when( geocoder.getFromLocation( 30.6, 34.8, 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( fix( 30.6, 34.8 ) );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( ZmanimLocation.toLatLongString( 30.6, 34.8 ), request.location.getLocationName() );
  }


  /** A "near" name is not cached, so once the geocoder works again it names the place. */
  @Test
  public void aFixNamedNearATownIsNamedOnceTheGeocoderWorksAgain() throws IOException {

    Mockito.when( geocoder.getFromLocation( NEGEV_LATITUDE, NEGEV_LONGITUDE, 5 ) ).thenThrow( new IOException( "no network" ) );
    resolvePlace( inTheNegev() );

    Address sdeBoker = new Address( Locale.US );
    sdeBoker.setLocality( "Sde Boker" );
    sdeBoker.setCountryName( "Israel" );
    Mockito.reset( geocoder );
    Mockito.when( geocoder.getFromLocation( NEGEV_LATITUDE, NEGEV_LONGITUDE, 5 ) ).thenReturn( Collections.singletonList( sdeBoker ) );

    Assert.assertEquals( AddressNames.addressToLocationName( sdeBoker ),
      resolvePlace( inTheNegev() ).location.getLocationName() );
  }


  @Test
  public void aLocationThatIsAlreadyResolvedIsNotLookedUpAgain() {

    ZmanimLocation resolved = new ZmanimLocation.Builder().from( nearAlbany() ).locationName( "Albany, New York" ).build();

    LocationToAddressConverter.enqueueWork( context, resolved, LocationToAddressConverter.Actions.RESOLVE_LOCATION );
    for ( Intent work : ZmanimServiceRequests.startingService( ZmanimServiceRequests.drainIntents(),
      LocationToAddressConverter.class ) ) {
      converter.onHandleWork( work );
    }

    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
    Mockito.verifyNoInteractions( geocoder );
  }


  @Test
  public void aTypedPlaceIsPlacedByTheGeocoderAndCachedUnderWhatWasTyped() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Albany", 5 ) ).thenReturn( Collections.singletonList( albany() ) );

    ZmanimServiceRequests.Request request = resolvePlace( typed( "Albany" ) );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Assert.assertEquals( "Albany", request.location.originalUserName );
    Assert.assertEquals( 42.6526, request.location.getLatitude(), 1e-9 );

    Mockito.reset( geocoder );
    request = resolvePlace( typed( "Albany" ) );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( geocoder );
  }


  @Test
  public void offlineTheBundledDataFindsATypedPlace() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Jerusalem", 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( typed( "Jerusalem" ) );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Jerusalem, Israel", request.location.getLocationName() );
    Assert.assertEquals( NEW_YORK, request.location.getTimeZone() );
  }


  @Test
  public void aTypedPlaceNoSourceKnowsIsNotFound() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Qwxzvv", 5 ) ).thenReturn( Collections.emptyList() );

    ZmanimServiceRequests.Request request = resolvePlace( typed( "Qwxzvv" ) );

    Assert.assertEquals( "LOCATION_NOT_ACQUIRED", request.action );
    Assert.assertEquals( LocationError.Code.PLACE_NOT_FOUND, request.error.errorCode );
    Assert.assertEquals( "Qwxzvv", request.error.message );
    Assert.assertEquals( "Qwxzvv", request.location.getLocationName() );
  }


  @Test
  public void offlineATypedPlaceTheBundledDataDoesNotKnowIsNotFound() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Qwxzvv", 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolvePlace( typed( "Qwxzvv" ) );

    Assert.assertEquals( LocationError.Code.PLACE_NOT_FOUND, request.error.errorCode );
    Assert.assertEquals( "Qwxzvv", request.error.message );
  }


  @Test
  public void anUpgradeThatAsksForItStartsWithAnEmptyCache() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenReturn( Collections.singletonList( albany() ) );
    resolvePlace( nearAlbany() );

    LocationToAddressConverter.wipeCacheOnStart = true;
    converter = started();
    Assert.assertFalse( LocationToAddressConverter.wipeCacheOnStart );

    Mockito.reset( geocoder );
    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenThrow( new IOException( "no network" ) );
    Assert.assertEquals( "Albany, United States", resolvePlace( nearAlbany() ).location.getLocationName() );
  }

}  // End of UT_LocationToAddressConverter class
