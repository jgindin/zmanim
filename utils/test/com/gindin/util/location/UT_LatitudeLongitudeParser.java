/*
 * Copyright (c) 2013. Jay R. Gindin
 */

package com.gindin.util.location;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
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
	}


	private static final List<CityInfo>                       cityInfo = new ArrayList<CityInfo>();

	static {

		/**
		 * Data comes from: http://www.infoplease.com/ipa/A0001769.html, http://www.infoplease.com/ipa/A0001796.html
		 */

		cityInfo.add(
			new CityInfo(
				"Aberdeen, Scotland",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 57, 9 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 2, 9 ),
				57.15,
				-2.15
			)
		);

		cityInfo.add(
			new CityInfo(
				"Adelaide, Australia",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.SOUTH, 34, 55 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.EAST, 138, 36 ),
				-34.91666666666666666667,
				138.6
			)
		);

		cityInfo.add(
			new CityInfo(
				"Tel Aviv",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 32, 4 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.EAST, 34, 46 ),
				32.06666666666666666667,
				34.76666666666666666667
			)
		);

		cityInfo.add(
			new CityInfo(
				"Seattle, WA",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 47, 37 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 122, 20 ),
				47.61666666666666666667,
				-122.33333333333333333333
			)
		);

		cityInfo.add(
			new CityInfo(
				"New York, NY",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 40, 47 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 73, 58 ),
				40.78333333333333333333,
				-73.96666666666666666667
			)
		);

		cityInfo.add(
			new CityInfo(
				"San Francisco, Calif.",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 37, 47 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 122, 26),
				37.78333333333333333333,
				-122.43333333333333333333
			)
		);

		cityInfo.add(
			new CityInfo(
				"Phoenix, Ariz.",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 33, 29 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 112, 4 ),
				33.48333333333333333333,
				-112.06666666666666666667
			)
		);

		cityInfo.add(
			new CityInfo(
				"Ottawa, Ont., Can.",
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.NORTH, 45, 24 ),
				new LatitudeLongitudeParser.Holder( LatitudeLongitudeParser.Direction.WEST, 73, 43 ),
				45.4,
				-73.71666666666666666667
			)
		);
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

		for ( CityInfo info : cityInfo ) {
			double converted = LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( info.latitude.toFullString( withMarkers ) );
			Assert.assertEquals( info.cityName + " latitude", info.convertedLatitude, converted, 0.1 );

			converted = LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( info.longitude.toFullString( withMarkers ) );
			Assert.assertEquals( info.cityName + " longitude", info.convertedLongitude, converted, 0.1 );
		}
	}


	private void simpleString( boolean withMarkers ) {

		for ( CityInfo info : cityInfo ) {
			double converted = LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( info.latitude.toSimpleString( withMarkers ) );
			Assert.assertEquals( info.cityName + " latitude", info.convertedLatitude, converted, 0.1 );

			converted = LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( info.longitude.toSimpleString( withMarkers ) );
			Assert.assertEquals( info.cityName + " longitude", info.convertedLongitude, converted, 0.1 );
		}
	}


} // End of UT_LatitudeLongitudeParser class
