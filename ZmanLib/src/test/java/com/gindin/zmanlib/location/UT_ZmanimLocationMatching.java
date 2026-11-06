/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

/**
 * The rules the location cache and the resolvers build on: when two locations count as the same place, and when a
 *  location counts as resolved. Tests named {@code ...CurrentBehavior} pin behavior that looks wrong; it is kept as
 *  it is until a later change fixes it on purpose.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ZmanimLocationMatching {

  private static final TimeZone                             LOS_ANGELES = TimeZone.getTimeZone( "America/Los_Angeles" );


  private static ZmanimLocation.Builder seattle() {
    return new ZmanimLocation.Builder()
      .locationName( "Seattle, Washington" )
      .atLatitude( 47.62 )
      .atLongitude( -122.33 )
      .atElevation( 50 )
      .inTimeZone( LOS_ANGELES );
  }


  @Test
  public void aMoveOfLessThanOneKilometerIsNotSignificant() {

    // 0.008 degrees of latitude is about 890 m.
    ZmanimLocation moved = seattle().atLatitude( 47.628 ).build();

    Assertions.assertFalse( moved.isSignificantChangeFrom( seattle().build() ) );
  }


  @Test
  public void aMoveOfOneKilometerOrMoreIsSignificant() {

    // 0.01 degrees of latitude is about 1,110 m.
    ZmanimLocation moved = seattle().atLatitude( 47.63 ).build();

    Assertions.assertTrue( moved.isSignificantChangeFrom( seattle().build() ) );
  }


  @Test
  public void anElevationChangeOfMoreThanFiveMetersIsSignificant() {

    Assertions.assertFalse( seattle().atElevation( 55 ).build().isSignificantChangeFrom( seattle().build() ) );
    Assertions.assertTrue( seattle().atElevation( 56 ).build().isSignificantChangeFrom( seattle().build() ) );
  }


  @Test
  public void aDifferentTimeZoneIsSignificant() {

    ZmanimLocation otherZone = seattle().inTimeZone( TimeZone.getTimeZone( "America/New_York" ) ).build();

    Assertions.assertTrue( otherZone.isSignificantChangeFrom( seattle().build() ) );
  }


  @Test
  public void everyLocationIsASignificantChangeFromNoLocation() {
    Assertions.assertTrue( seattle().build().isSignificantChangeFrom( null ) );
  }


  @Test
  public void aDifferentNameAloneIsNotSignificant() {

    ZmanimLocation renamed = seattle().locationName( "Somewhere else" ).build();

    Assertions.assertFalse( renamed.isSignificantChangeFrom( seattle().build() ) );
  }


  @Test
  public void aResolvedLocationHasBothANameAndCoordinates() {

    Assertions.assertTrue( seattle().build().isResolved() );
    Assertions.assertFalse( seattle().locationName( null ).build().isResolved() );
    Assertions.assertFalse( new ZmanimLocation.Builder().locationName( "Seattle" ).build().isResolved() );
  }


  @Test
  public void aLocationWithoutANameIsNamedByItsCoordinates() {

    ZmanimLocation unnamed = seattle().locationName( null ).build();

    Assertions.assertFalse( unnamed.hasName() );
    Assertions.assertEquals( ZmanimLocation.toLatLongString( 47.62, -122.33 ), unnamed.getLocationName() );
  }


  /** Copying goes through {@code getLocationName()}, so the coordinates become a real name in the copy. */
  @Test
  public void aCopyOfALocationWithoutANameIsNamedCurrentBehavior() {

    ZmanimLocation copy = new ZmanimLocation.Builder().from( seattle().locationName( null ).build() ).build();

    Assertions.assertTrue( copy.hasName() );
    Assertions.assertTrue( copy.isResolved() );
    Assertions.assertEquals( ZmanimLocation.toLatLongString( 47.62, -122.33 ), copy.getLocationName() );
  }


  /** A place on the equator or the prime meridian is treated as having no coordinates at all. */
  @Test
  public void aLatitudeOrLongitudeOfExactlyZeroMeansNoCoordinatesCurrentBehavior() {

    Assertions.assertFalse( seattle().atLatitude( 0 ).build().hasCoordinates() );
    Assertions.assertFalse( seattle().atLongitude( 0 ).build().hasCoordinates() );
    Assertions.assertFalse( seattle().atLongitude( 0 ).build().isResolved() );
  }


  @Test
  public void anElevationBelowSeaLevelIsStoredAsSeaLevel() {
    Assertions.assertEquals( 0.0, seattle().atElevation( -430 ).build().getElevation() );
  }

}  // End of UT_ZmanimLocationMatching class
