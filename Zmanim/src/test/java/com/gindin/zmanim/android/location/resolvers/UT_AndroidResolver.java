/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import android.location.Address;
import android.location.Geocoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

/**
 * What {@link AndroidResolver} makes of the system geocoder's answers. The geocoder is a final framework class, so a
 *  Mockito mock stands in for it.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 33 )
@SuppressWarnings( "MagicNumber" )
public class UT_AndroidResolver {

  private static final TimeZone                             CHICAGO = TimeZone.getTimeZone( "America/Chicago" );

  private Geocoder                                          geocoder;

  private AndroidResolver                                   resolver;


  @Before
  public void setup() {
    geocoder = Mockito.mock( Geocoder.class );
    resolver = new AndroidResolver( geocoder );
  }


  private static Address seattle() {
    Address address = new Address( Locale.US );
    address.setLocality( "Seattle" );
    address.setAdminArea( "Washington" );
    address.setLatitude( 47.6062 );
    address.setLongitude( -122.3321 );
    return address;
  }


  private static Address tacoma() {
    Address address = new Address( Locale.US );
    address.setLocality( "Tacoma" );
    address.setAdminArea( "Washington" );
    address.setLatitude( 47.25 );
    address.setLongitude( -122.44 );
    return address;
  }


  private static ZmanimLocation deviceFix() {
    return new ZmanimLocation.Builder()
      .fromProvider( "fused" )
      .at( 1_700_000_000_000L )
      .atLatitude( 47.62 )
      .atLongitude( -122.33 )
      .atElevation( 56 )
      .withAccuracy( 12 )
      .inTimeZone( CHICAGO )
      .build();
  }


  private static ZmanimLocation typed( String name ) {
    return new ZmanimLocation.Builder()
      .locationName( name )
      .userName( name )
      .inTimeZone( CHICAGO )
      .build();
  }


  @Test
  public void coordinatesAreNamedFromTheFirstAddress() throws IOException {

    Mockito.when( geocoder.getFromLocation( 47.62, -122.33, 5 ) ).thenReturn( Arrays.asList( seattle(), tacoma() ) );

    ZmanimLocation named = resolver.resolveFromCoordinates( deviceFix() );

    Assert.assertNotNull( named );
    Assert.assertEquals( "Seattle, Washington", named.getLocationName() );
  }


  /** The fix takes the address's coordinates; everything else about the fix is kept. */
  @Test
  public void namedCoordinatesMoveToTheAddressCurrentBehavior() throws IOException {

    Mockito.when( geocoder.getFromLocation( 47.62, -122.33, 5 ) ).thenReturn( Collections.singletonList( seattle() ) );

    ZmanimLocation named = resolver.resolveFromCoordinates( deviceFix() );

    Assert.assertEquals( 47.6062, named.getLatitude(), 1e-9 );
    Assert.assertEquals( -122.3321, named.getLongitude(), 1e-9 );
    Assert.assertEquals( 56.0, named.getElevation(), 0.0 );
    Assert.assertEquals( "fused", named.providerName );
    Assert.assertEquals( 1_700_000_000_000L, named.locationTime );
    Assert.assertEquals( CHICAGO, named.getTimeZone() );
  }


  @Test
  public void coordinatesWithNoAddressAreNotNamed() throws IOException {

    Mockito.when( geocoder.getFromLocation( 47.62, -122.33, 5 ) ).thenReturn( Collections.emptyList() );

    Assert.assertNull( resolver.resolveFromCoordinates( deviceFix() ) );
  }


  @Test
  public void aGeocoderFailureLeavesCoordinatesUnnamed() throws IOException {

    Mockito.when( geocoder.getFromLocation( 47.62, -122.33, 5 ) ).thenThrow( new IOException( "no network" ) );
    Assert.assertNull( resolver.resolveFromCoordinates( deviceFix() ) );

    Mockito.reset( geocoder );
    Mockito.when( geocoder.getFromLocation( 47.62, -122.33, 5 ) ).thenThrow( new IllegalStateException( "broken" ) );
    Assert.assertNull( resolver.resolveFromCoordinates( deviceFix() ) );
  }


  @Test
  public void aTypedNameIsPlacedAtTheFirstAddressAndRemembersWhatWasTyped() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "seattle", 5 ) ).thenReturn( Arrays.asList( seattle(), tacoma() ) );

    ZmanimLocation placed = resolver.resolveFromName( typed( "seattle" ) );

    Assert.assertNotNull( placed );
    Assert.assertTrue( placed.isResolved() );
    Assert.assertEquals( "Seattle, Washington", placed.getLocationName() );
    Assert.assertEquals( "seattle", placed.originalUserName );
    Assert.assertEquals( 47.6062, placed.getLatitude(), 1e-9 );
    Assert.assertEquals( -122.3321, placed.getLongitude(), 1e-9 );
    Assert.assertEquals( CHICAGO, placed.getTimeZone() );
  }


  /** The geocoder has no elevation, so a typed place is at sea level. */
  @Test
  public void aTypedPlaceIsAtSeaLevel() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "seattle", 5 ) ).thenReturn( Collections.singletonList( seattle() ) );

    Assert.assertEquals( 0.0, resolver.resolveFromName( typed( "seattle" ) ).getElevation(), 0.0 );
  }


  @Test
  public void aTypedNameWithNoAddressIsNotPlaced() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "nowhere at all", 5 ) ).thenReturn( Collections.emptyList() );

    Assert.assertNull( resolver.resolveFromName( typed( "nowhere at all" ) ) );
  }


  @Test
  public void aGeocoderFailureLeavesATypedNameUnplaced() throws IOException {

    Mockito.when( geocoder.getFromLocationName( "seattle", 5 ) ).thenThrow( new IOException( "no network" ) );

    Assert.assertNull( resolver.resolveFromName( typed( "seattle" ) ) );
  }

}  // End of UT_AndroidResolver class
