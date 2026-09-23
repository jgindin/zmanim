/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test our ability to properly parse latitude & longitude.
 * <p/>
 * <p>
 * City data comes from: http://www.infoplease.com/ipa/A0001769.html, http://www.infoplease.com/ipa/A0001796.html
 * <p>
 * <p/>
 * A good converter seems to be: http://www.csgnetwork.com/gpscoordconv.html
 * <p/>
 * <p>
 * Each of the five test methods below is a {@code @ParameterizedTest} sourced from
 *  {@link #gatherTestData()}'s 8 cities, with {@code CityInfo} passed as a method parameter
 *  rather than a constructor-injected field -- JUnit 5 creates a fresh test instance per
 *  invocation anyway (the default PER_METHOD lifecycle), so a field would add nothing.
 * <p>
 * {@code assertThat(...)} below (used with the custom {@code BaseMatcher}) is
 *  {@link org.hamcrest.MatcherAssert#assertThat}, not a JUnit assertion -- JUnit 5's
 *  {@code org.junit.jupiter.api.Assertions} has no {@code assertThat} method at all. Hamcrest is
 *  JUnit-version-agnostic, so this only needs the {@code testImplementation(libs.hamcrest)}
 *  dependency in ZmanLib/build.gradle.kts, no JUnit-specific wiring.
 */
@SuppressWarnings( "MagicNumber" )
class UT_LatitudeLongitudeParser {


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


  private static Stream<CityInfo> gatherTestData() {

    return Stream.of(

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


  @ParameterizedTest( name = "{0}" )
  @MethodSource( "gatherTestData" )
  void testFullStringParsing( CityInfo cityInfoToTest ) {
    fullString( cityInfoToTest, false );
  }


  @ParameterizedTest( name = "{0}" )
  @MethodSource( "gatherTestData" )
  void testFullStringParsingWithMarkers( CityInfo cityInfoToTest ) {
    fullString( cityInfoToTest, true );
  }


  @ParameterizedTest( name = "{0}" )
  @MethodSource( "gatherTestData" )
  void testSimpleStringParsing( CityInfo cityInfoToTest ) {
    simpleString( cityInfoToTest, false );
  }


  @ParameterizedTest( name = "{0}" )
  @MethodSource( "gatherTestData" )
  void testSimpleStringParsingWithMarkers( CityInfo cityInfoToTest ) {
    simpleString( cityInfoToTest, true );
  }


  @ParameterizedTest( name = "{0}" )
  @MethodSource( "gatherTestData" )
  void fullDecimalInDegrees( CityInfo cityInfoToTest ) {

    final LatitudeLongitudeParser latitude = cityInfoToTest.latitude;
    LatitudeLongitudeParser holder = LatitudeLongitudeParser.latitude( latitude.direction,
      Double.toString( cityInfoToTest.convertedLatitude ), "", "" );
    Assertions.assertEquals( latitude.degrees,
      holder.degrees, cityInfoToTest.cityName + " latitude.degrees" );

    // This gets a bit complicated because IF the actual is ONE degree larger AND we have seconds, THEN that has
    //  happened because of rounding...
    assertThat( cityInfoToTest.cityName + " latitude.getMinutes()", latitude,
      new DegreesWithRoundingMatcher( holder ) );

    if ( latitude.seconds != 0 ) {
      Assertions.assertEquals( latitude.seconds,
        holder.seconds, 0, cityInfoToTest.cityName + " latitude.seconds" );
    }

    final LatitudeLongitudeParser longitude = cityInfoToTest.longitude;
    holder = LatitudeLongitudeParser.longitude( longitude.direction,
      Double.toString( cityInfoToTest.convertedLongitude ), "", "" );
    Assertions.assertEquals( longitude.degrees,
      holder.degrees, cityInfoToTest.cityName + " longitude.degrees" );
    assertThat( cityInfoToTest.cityName + " longitude.getMinutes()", longitude,
      new DegreesWithRoundingMatcher( holder ) );

    if ( longitude.seconds != 0 ) {
      Assertions.assertEquals( longitude.seconds,
        holder.seconds, 0, cityInfoToTest.cityName + " longitude.seconds" );
    }
  }


  private void fullString( CityInfo cityInfoToTest, boolean withMarkers ) {

    double converted = LatitudeLongitudeParser.latitudeValue(
      cityInfoToTest.latitude.toFullString( withMarkers ) );
    Assertions.assertEquals( cityInfoToTest.convertedLatitude,
      converted, 0.1, cityInfoToTest.cityName + " latitude" );

    converted = LatitudeLongitudeParser.longitudeValue(
      cityInfoToTest.longitude.toFullString( withMarkers ) );
    Assertions.assertEquals( cityInfoToTest.convertedLongitude,
      converted, 0.1, cityInfoToTest.cityName + " longitude" );
  }


  private void simpleString( CityInfo cityInfoToTest, boolean withMarkers ) {

    double converted = LatitudeLongitudeParser.latitudeValue(
      cityInfoToTest.latitude.toSimpleString( withMarkers ) );
    Assertions.assertEquals( cityInfoToTest.convertedLatitude,
      converted, 0.1, cityInfoToTest.cityName + " latitude" );

    converted = LatitudeLongitudeParser.longitudeValue(
      cityInfoToTest.longitude.toSimpleString( withMarkers ) );
    Assertions.assertEquals( cityInfoToTest.convertedLongitude,
      converted, 0.1, cityInfoToTest.cityName + " longitude" );
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
