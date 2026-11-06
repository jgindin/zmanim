/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import android.location.Address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

/**
 * The name {@link LocationResolver#addressToLocationName} builds from what the geocoder returns. Tests named
 *  {@code ...CurrentBehavior} pin behavior that looks wrong; it is kept as it is until a later change fixes it on
 *  purpose.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_AddressToLocationName {

  private static Address address() {
    Address address = new Address( Locale.US );
    address.setLatitude( 40.65 );
    address.setLongitude( -73.95 );
    return address;
  }


  @Test
  public void localityAndAdminArea() {

    Address address = address();
    address.setLocality( "Seattle" );
    address.setAdminArea( "Washington" );
    address.setCountryName( "United States" );

    Assertions.assertEquals( "Seattle, Washington", LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void localityAndCountryWhenThereIsNoAdminArea() {

    Address address = address();
    address.setLocality( "Monaco" );
    address.setCountryName( "Monaco" );

    Assertions.assertEquals( "Monaco, Monaco", LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void localityAlone() {

    Address address = address();
    address.setLocality( "Monaco" );

    Assertions.assertEquals( "Monaco", LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void adminAreaAndCountryWhenThereIsNoLocality() {

    Address address = address();
    address.setAdminArea( "New York" );
    address.setCountryName( "United States" );

    Assertions.assertEquals( "New York, United States", LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void adminAreaAlone() {

    Address address = address();
    address.setAdminArea( "New York" );

    Assertions.assertEquals( "New York", LocationResolver.addressToLocationName( address ) );
  }


  /** The text before the postal code is used, including the space in front of the code. */
  @Test
  public void theAddressLineWithThePostalCodeCurrentBehavior() {

    Address address = address();
    address.setAddressLine( 0, "123 Main Street" );
    address.setAddressLine( 1, "Brooklyn, NY 11226" );
    address.setAddressLine( 2, "USA" );
    address.setPostalCode( "11226" );
    address.setCountryName( "United States" );

    Assertions.assertEquals( "Brooklyn, NY ", LocationResolver.addressToLocationName( address ) );
  }


  /** The last address line is never searched for the postal code, so the coordinates are used instead. */
  @Test
  public void thePostalCodeOnTheLastAddressLineIsNotFoundCurrentBehavior() {

    Address address = address();
    address.setAddressLine( 0, "123 Main Street" );
    address.setAddressLine( 1, "Brooklyn, NY 11226" );
    address.setPostalCode( "11226" );

    Assertions.assertEquals( ZmanimLocation.toLatLongString( 40.65, -73.95 ),
      LocationResolver.addressToLocationName( address ) );
  }


  /** With more than one address line the country is not used, even when no line has the postal code. */
  @Test
  public void addressLinesWithoutThePostalCodeIgnoreTheCountryCurrentBehavior() {

    Address address = address();
    address.setAddressLine( 0, "123 Main Street" );
    address.setAddressLine( 1, "Somewhere" );
    address.setCountryName( "United States" );

    Assertions.assertEquals( ZmanimLocation.toLatLongString( 40.65, -73.95 ),
      LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void countryAloneWhenThereIsAtMostOneAddressLine() {

    Address address = address();
    address.setAddressLine( 0, "Somewhere" );
    address.setCountryName( "United Kingdom" );

    Assertions.assertEquals( "United Kingdom", LocationResolver.addressToLocationName( address ) );
  }


  @Test
  public void theCoordinatesWhenTheAddressHasNothingElse() {
    Assertions.assertEquals( ZmanimLocation.toLatLongString( 40.65, -73.95 ),
      LocationResolver.addressToLocationName( address() ) );
  }


  @Test
  public void noAddressIsAnUnknownLocation() {
    Assertions.assertEquals( ZmanimLocation.UNKNOWN_LOCATION, LocationResolver.addressToLocationName( null ) );
  }

}  // End of UT_AddressToLocationName class
