package com.gindin.zmanim.location;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Handles the translation of a ZmanimLocation to/from JSON.
 */
class ZmanLocationJSON {

	private static final String                               LATITUDE = "lat";
	private static final String                               LONGITUDE = "lon";
	private static final String                               ELEVATION = "el";
	private static final String                               TIME_ZONE = "tz";
	private static final String                               PROVIDER = "pv";
	private static final String                               ACCURACY = "ac";
	private static final String                               NAME = "name";

	private static final String                               NO_NAME = " - - ";


	@SuppressWarnings( "FeatureEnvy" )
	public static String toJSON( ZmanimLocation location ) {

		StringBuilder json = new StringBuilder( "{" );
		append( json, LATITUDE, ZmanimLocation.getLatitudeString( location.getLatitude() ) );
		json.append( ",\n" );

		append( json, LONGITUDE, ZmanimLocation.getLongitudeString( location.getLongitude() ) );
		json.append( ",\n" );

		append( json, ELEVATION, Double.toString( location.getElevation() ) );
		json.append( ",\n" );

		append( json, TIME_ZONE, location.getTimeZone().getDisplayName( false, TimeZone.SHORT, Locale.getDefault() ) );
		json.append( ",\n" );

		append( json, PROVIDER, location.providerName );
		json.append( ",\n" );

		append( json, ACCURACY, Float.toString( location.accuracy ) );
		json.append( ",\n" );

		if ( location.hasName() ) {
			append( json, NAME, location.getLocationName() );
		}
		else {
			append( json, NAME, NO_NAME );
		}

		json.append( "\n}" );

		return json.toString();
	}


	public static ZmanimLocation fromJSON( String json ) throws JSONException {

		JSONTokener tokener = new JSONTokener( json );
		JSONObject root = (JSONObject)tokener.nextValue();

		double latitude = root.getDouble( LATITUDE );
		double longitude = root.getDouble( LONGITUDE );
		double elevation = root.getDouble( ELEVATION );
		float accuracy = (float)root.getDouble( ACCURACY );
		String provider = root.getString( PROVIDER );
		String tz = root.getString( TIME_ZONE );
		TimeZone timeZone = TimeZone.getTimeZone( tz );
		String name = root.getString( NAME );
		if ( name.equals( NO_NAME ) ) {
			name = null;
		}

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( latitude )
			.atLongitude( longitude )
			.atElevation( elevation )
			.withAccuracy( accuracy )
			.fromProvider( provider )
			.inTimeZone( timeZone )
			.locationName( name )
			.build();

		return location;
	}


	private static void append(
		StringBuilder json,
		String        key,
	  String        value
	) {

		json.append( "\"" ).append( key ).append( "\": \"" ).append( value ).append( "\"" );
	}
}
