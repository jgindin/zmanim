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
import org.robolectric.shadows.ShadowGeocoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * What {@link LocationToAddressConverter} tells the rest of the app after looking up a location, through its real
 *  resolver chain with a Mockito geocoder in place of the system one. The work goes in the way the app sends it, with
 *  {@link LocationToAddressConverter#enqueueWork}. Tests named {@code ...CurrentBehavior} pin behavior that looks
 *  wrong; it is kept as it is until a later change fixes it on purpose.
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
  public void setup() throws Exception {

    context = ApplicationProvider.getApplicationContext();
    context.deleteFile( "CachedLocations" );
    LocationToAddressConverter.wipeCacheOnStart = false;

    converter = Robolectric.setupService( LocationToAddressConverter.class );

    geocoder = Mockito.mock( Geocoder.class );
    resolvers( converter ).set( 1, new AndroidResolver( geocoder ) );

    ZmanimServiceRequests.drainIntents();
  }


  @After
  public void restoreGeocoder() {
    ShadowGeocoder.setIsPresent( true );
  }


  @SuppressWarnings( "unchecked" )
  private static List<LocationResolver> resolvers( LocationToAddressConverter converter ) throws Exception {

    // The converter keeps its chain in a private field; there is no other way to see it or to put a fake geocoder in.
    Field field = LocationToAddressConverter.class.getDeclaredField( "orderedResolvers" );
    field.setAccessible( true );
    return (List<LocationResolver>)field.get( converter );
  }


  private static List<Class<?>> classesOf( List<LocationResolver> resolvers ) {

    List<Class<?>> classes = new ArrayList<>();
    for ( LocationResolver resolver : resolvers ) {
      classes.add( resolver.getClass() );
    }
    return classes;
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


  private static Address albany() {
    Address address = new Address( Locale.US );
    address.setLocality( "Albany" );
    address.setAdminArea( "New York" );
    address.setLatitude( 42.6526 );
    address.setLongitude( -73.7562 );
    return address;
  }


  private static ZmanimLocation deviceFix() {
    return new ZmanimLocation.Builder()
      .fromProvider( "fused" )
      .atLatitude( 42.65 )
      .atLongitude( -73.76 )
      .inTimeZone( NEW_YORK )
      .build();
  }


  private static ZmanimLocation typed( String name ) {
    return new ZmanimLocation.Builder()
      .locationName( name )
      .userName( name )
      .inTimeZone( NEW_YORK )
      .build();
  }


  @Test
  public void theCacheIsAskedFirstThenTheGeocoderThenNobody() throws Exception {

    LocationToAddressConverter started = Robolectric.setupService( LocationToAddressConverter.class );

    Assert.assertEquals(
      List.of( CachedLocationResolver.class, AndroidResolver.class, PassthroughResolver.class ),
      classesOf( resolvers( started ) ) );
  }


  @Test
  public void withoutAGeocoderTheCacheIsAskedThenNobody() throws Exception {

    ShadowGeocoder.setIsPresent( false );
    LocationToAddressConverter started = Robolectric.setupService( LocationToAddressConverter.class );

    Assert.assertEquals( List.of( CachedLocationResolver.class, PassthroughResolver.class ),
      classesOf( resolvers( started ) ) );
  }


  @Test
  public void aDeviceFixIsNamedByTheGeocoderAndCached() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenReturn( Collections.singletonList( albany() ) );

    ZmanimServiceRequests.Request request = resolve( deviceFix(), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );

    // The next fix nearby is answered from the cache, without the geocoder.
    Mockito.reset( geocoder );
    request = resolve( deviceFix(), LocationToAddressConverter.Actions.RESOLVE_PLACE );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( geocoder );
  }


  /**
   * Going through a bundle names an unnamed fix after its coordinates, so when the geocoder cannot name it the fix
   *  comes back as resolved, under its coordinates.
   */
  @Test
  public void aDeviceFixTheGeocoderCannotNameIsAcquiredUnderItsCoordinatesCurrentBehavior() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolve( deviceFix(), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( ZmanimLocation.toLatLongString( 42.65, -73.76 ), request.location.getLocationName() );
  }


  /** And that name is cached, so the place keeps it after the network comes back. */
  @Test
  public void theCoordinatesNameStaysCachedOnceTheGeocoderWorksAgainCurrentBehavior() throws IOException {

    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenThrow( new IOException( "no network" ) );
    resolve( deviceFix(), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Mockito.reset( geocoder );
    Mockito.when( geocoder.getFromLocation( 42.65, -73.76, 5 ) ).thenReturn( Collections.singletonList( albany() ) );
    ZmanimServiceRequests.Request request = resolve( deviceFix(), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( ZmanimLocation.toLatLongString( 42.65, -73.76 ), request.location.getLocationName() );
    Mockito.verifyNoInteractions( geocoder );
  }


  @Test
  public void aLocationThatIsAlreadyResolvedIsNotLookedUpAgain() {

    ZmanimLocation resolved = new ZmanimLocation.Builder().from( deviceFix() ).locationName( "Albany, New York" ).build();

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

    ZmanimServiceRequests.Request request = resolve( typed( "Albany" ), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( "LOCATION_ACQUIRED", request.action );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Assert.assertEquals( "Albany", request.location.originalUserName );
    Assert.assertEquals( 42.6526, request.location.getLatitude(), 1e-9 );

    Mockito.reset( geocoder );
    request = resolve( typed( "Albany" ), LocationToAddressConverter.Actions.RESOLVE_PLACE );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( geocoder );
  }


  @Test
  public void aTypedPlaceTheGeocoderDoesNotKnowCannotBeDetermined() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Nowhere", 5 ) ).thenReturn( Collections.emptyList() );

    ZmanimServiceRequests.Request request = resolve( typed( "Nowhere" ), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( "LOCATION_NOT_ACQUIRED", request.action );
    Assert.assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, request.error.errorCode );
    Assert.assertEquals( "Nowhere", request.location.getLocationName() );
  }


  /** Offline, a typed place that is not in the cache cannot be found at all. */
  @Test
  public void aTypedPlaceCannotBeDeterminedOffline() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "Albany", 5 ) ).thenThrow( new IOException( "no network" ) );

    ZmanimServiceRequests.Request request = resolve( typed( "Albany" ), LocationToAddressConverter.Actions.RESOLVE_PLACE );

    Assert.assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, request.error.errorCode );
  }

}  // End of UT_LocationToAddressConverter class
