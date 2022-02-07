/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test our ability to properly parse latitude & longitude.
 * <p/>
 * <p>
 * City data comes from: http://www.infoplease.com/ipa/A0001769.html, http://www.infoplease.com/ipa/A0001796.html
 * <p>
 * <p/>
 * A good converter seems to be: http://www.csgnetwork.com/gpscoordconv.html
 */
@SuppressWarnings( "MagicNumber" )
@RunWith( Parameterized.class )
public class UT_LatitudeLongitudeParser {


  private static class CityInfo {
    final String cityName;

    final LatitudeLongitudeParser latitude;

    final LatitudeLongitudeParser longitude;

    final double convertedLatitude;

    final double convertedLongitude;


    private CityInfo(
      String cityName,
      LatitudeLongitudeParser latitude,
      LatitudeLongitudeParser longitude,
      double convertedLatitude,
      double convertedLongitude
    ) {
      this.cityName = cityName;
      this.latitude = latitude;
      this.longitude = longitude;
      this.convertedLatitude = convertedLatitude;
      this.convertedLongitude = convertedLongitude;
    }


    @Override
    public String toString() {
      return cityName;
    }
  }


  /**
   * Info for the city we're currently testing.
   */
  private final CityInfo cityInfoToTest;


  @Parameterized.Parameters( name = "{0}" )
  public static Collection<CityInfo> gatherTestData() {

    return Arrays.asList(

      new CityInfo(
        "Aberdeen, Scotland",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 57, 9, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 2, 9, 0 ),
        57.15,
        -2.15
      ),

      new CityInfo(
        "Adelaide, Australia",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.SOUTH, 34, 55, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.EAST, 138, 36, 0 ),
        -34.91666666666666666667,
        138.6
      ),

      new CityInfo(
        "Tel Aviv",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 32, 4, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.EAST, 34, 46, 0 ),
        32.06666666666666666667,
        34.76666666666666666667
      ),

      new CityInfo(
        "Seattle, WA",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 47, 37, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 122, 20, 0 ),
        47.61666666666666666667,
        -122.33333333333333333333
      ),

      new CityInfo(
        "New York, NY",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 40, 47, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 73, 58, 0 ),
        40.78333333333333333333,
        -73.96666666666666666667
      ),

      new CityInfo(
        "San Francisco, Calif.",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 37, 47, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 122, 26, 0 ),
        37.78333333333333333333,
        -122.43333333333333333333
      ),

      new CityInfo(
        "Phoenix, Ariz.",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 33, 29, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 112, 4, 0 ),
        33.48333333333333333333,
        -112.06666666666666666667
      ),

      new CityInfo(
        "Ottawa, Ont., Can.",
        LatitudeLongitudeParser.latitude( LatitudeLongitudeParser.Direction.NORTH, 45, 24, 0 ),
        LatitudeLongitudeParser.longitude( LatitudeLongitudeParser.Direction.WEST, 73, 43, 0 ),
        45.4,
        -73.71666666666666666667
      )
    );

  }


  public UT_LatitudeLongitudeParser( CityInfo cityInfoToTest ) {
    this.cityInfoToTest = cityInfoToTest;
  }


  @Test
  public void testFullStringParsing() {
    fullString( false );
  }


  @Test
  public void testFullStringParsingWithMarkers() {
    fullString( true );
  }


  @Test
  public void testSimpleStringParsing() {
    simpleString( false );
  }


  @Test
  public void testSimpleStringParsingWithMarkers() {
    simpleString( true );
  }


  @Test
  public void fullDecimalInDegrees() {

    final LatitudeLongitudeParser latitude = cityInfoToTest.latitude;
    LatitudeLongitudeParser holder = LatitudeLongitudeParser.latitude( latitude.direction,
      Double.toString( cityInfoToTest.convertedLatitude ), "", "" );
    Assert.assertEquals( cityInfoToTest.cityName + " latitude.degrees", latitude.degrees,
      holder.degrees );

    // This gets a bit complicated because IF the actual is ONE degree larger AND we have seconds, THEN that has
    //  happened because of rounding...
    Assert.assertThat( cityInfoToTest.cityName + " latitude.getMinutes()", latitude,
      new DegreesWithRoundingMatcher( holder ) );

    if ( latitude.seconds != 0 ) {
      Assert.assertEquals( cityInfoToTest.cityName + " latitude.seconds", latitude.seconds,
        holder.seconds, 0 );
    }

    final LatitudeLongitudeParser longitude = cityInfoToTest.longitude;
    holder = LatitudeLongitudeParser.longitude( longitude.direction,
      Double.toString( cityInfoToTest.convertedLongitude ), "", "" );
    Assert.assertEquals( cityInfoToTest.cityName + " longitude.degrees", longitude.degrees,
      holder.degrees );
    Assert.assertThat( cityInfoToTest.cityName + " longitude.getMinutes()", longitude,
      new DegreesWithRoundingMatcher( holder ) );

    if ( longitude.seconds != 0 ) {
      Assert.assertEquals( cityInfoToTest.cityName + " longitude.seconds", longitude.seconds,
        holder.seconds, 0 );
    }
  }


  private void fullString( boolean withMarkers ) {

    double converted = LatitudeLongitudeParser.latitudeValue(
      cityInfoToTest.latitude.toFullString( withMarkers ) );
    Assert.assertEquals( cityInfoToTest.cityName + " latitude", cityInfoToTest.convertedLatitude,
      converted, 0.1 );

    converted = LatitudeLongitudeParser.longitudeValue(
      cityInfoToTest.longitude.toFullString( withMarkers ) );
    Assert.assertEquals( cityInfoToTest.cityName + " longitude", cityInfoToTest.convertedLongitude,
      converted, 0.1 );
  }


  private void simpleString( boolean withMarkers ) {

    double converted = LatitudeLongitudeParser.latitudeValue(
      cityInfoToTest.latitude.toSimpleString( withMarkers ) );
    Assert.assertEquals( cityInfoToTest.cityName + " latitude", cityInfoToTest.convertedLatitude,
      converted, 0.1 );

    converted = LatitudeLongitudeParser.longitudeValue(
      cityInfoToTest.longitude.toSimpleString( withMarkers ) );
    Assert.assertEquals( cityInfoToTest.cityName + " longitude", cityInfoToTest.convertedLongitude,
      converted, 0.1 );
  }


  private static class DegreesWithRoundingMatcher
    extends BaseMatcher<LatitudeLongitudeParser> {

    private final LatitudeLongitudeParser actual;

    private String mismatchReason;


    DegreesWithRoundingMatcher( LatitudeLongitudeParser actual ) {
      this.actual = actual;
    }


    @Override
    public boolean matches( Object item ) {

      LatitudeLongitudeParser expected = (LatitudeLongitudeParser)item;

      int diff = expected.minutes - actual.minutes;
      if ( diff == 0 ) {
        return true;
      }

      if ( diff == 1 ) {

        if ( ( expected.seconds == 0 ) && ( actual.seconds > 0 ) ) {
          return true;
        }
      }

      mismatchReason = "diff " + diff + " and expected seconds: " + expected.seconds +
        " but actual seconds: " + actual.seconds;
      return false;
    }


    @Override
    public void describeTo( Description description ) {

    }
  }


} // End of UT_LatitudeLongitudeParser class
