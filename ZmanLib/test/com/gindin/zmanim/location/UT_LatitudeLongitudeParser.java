/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.location;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test our ability to properly parse latitude & longitude.
 *<p/>
 *
 * City data comes from: http://www.infoplease.com/ipa/A0001769.html, http://www.infoplease.com/ipa/A0001796.html
 *
 *<p/>
 * A good converter seems to be: http://www.csgnetwork.com/gpscoordconv.html
 *
 */
@SuppressWarnings( "MagicNumber" )
@RunWith( Parameterized.class )
public class UT_LatitudeLongitudeParser {


	private static class CityInfo {
		public String                                           cityName;
		public LatitudeLongitudeParser.Holder                   latitude;
		public LatitudeLongitudeParser.Holder                   longitude;
		public double                                           convertedLatitude;
		public double                                           convertedLongitude;


		private CityInfo(
			String                          cityName,
			LatitudeLongitudeParser.Holder  latitude,
			LatitudeLongitudeParser.Holder  longitude,
			double                          convertedLatitude,
			double                          convertedLongitude
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


	/** Info for the city we're currently testing. */
	private final CityInfo                                    cityInfoToTest;

	@Parameterized.Parameters( name = "{0}" )
	public static Collection<CityInfo[]> gatherTestData() {

		CityInfo[][] cityInfos = new CityInfo[][] {

			{
				new CityInfo(
					"Aberdeen, Scotland",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 57, 9 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 2, 9 ),
					57.15,
					-2.15
				)
			},

			{
				new CityInfo(
					"Adelaide, Australia",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.SOUTH, 34, 55 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.EAST, 138, 36 ),
					-34.91666666666666666667,
					138.6
				)
			},

			{
				new CityInfo(
					"Tel Aviv",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 32, 4 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.EAST, 34, 46 ),
					32.06666666666666666667,
					34.76666666666666666667
				)
			},

			{
				new CityInfo(
					"Seattle, WA",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 47, 37 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 122, 20 ),
					47.61666666666666666667,
					-122.33333333333333333333
				)
			},

			{
				new CityInfo(
					"New York, NY",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 40, 47 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 73, 58 ),
					40.78333333333333333333,
					-73.96666666666666666667
				)
			},

			{
				new CityInfo(
					"San Francisco, Calif.",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 37, 47 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 122, 26 ),
					37.78333333333333333333,
					-122.43333333333333333333
				)
			},

			{
				new CityInfo(
					"Phoenix, Ariz.",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 33, 29 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 112, 4 ),
					33.48333333333333333333,
					-112.06666666666666666667
				)
			},

			{
				new CityInfo(
					"Ottawa, Ont., Can.",
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 45, 24 ),
					new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 73, 43 ),
					45.4,
					-73.71666666666666666667
				)
			}
		};

		return Arrays.asList( cityInfos );
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


	private void fullString( boolean withMarkers ) {

		double converted = LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( cityInfoToTest.latitude.toFullString( withMarkers ) );
		Assert.assertEquals( cityInfoToTest.cityName + " latitude", cityInfoToTest.convertedLatitude, converted, 0.1 );

		converted = LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( cityInfoToTest.longitude.toFullString( withMarkers ) );
		Assert.assertEquals( cityInfoToTest.cityName + " longitude", cityInfoToTest.convertedLongitude, converted, 0.1 );
	}


	private void simpleString( boolean withMarkers ) {

		double converted = LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( cityInfoToTest.latitude.toSimpleString( withMarkers ) );
		Assert.assertEquals( cityInfoToTest.cityName + " latitude", cityInfoToTest.convertedLatitude, converted, 0.1 );

		converted = LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( cityInfoToTest.longitude.toSimpleString( withMarkers ) );
		Assert.assertEquals( cityInfoToTest.cityName + " longitude", cityInfoToTest.convertedLongitude, converted, 0.1 );
	}


} // End of UT_LatitudeLongitudeParser class
