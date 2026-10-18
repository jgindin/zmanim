/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.TimeZone;

/**
 * Handles the translation of a ZmanimLocation to/from JSON.
 */
public class ZmanLocationJSON {

  private static final String                               LATITUDE = "lat";
  private static final String                               LONGITUDE = "lon";
  private static final String                               ELEVATION = "el";
  private static final String                               TIME_ZONE = "tz";
  private static final String                               PROVIDER = "pv";
  private static final String                               ACCURACY = "ac";
  private static final String                               NAME = "name";
  private static final String                               USER_NAME = "userName";

  private static final String                               NO_NAME = " - - ";


  public static String toJSON( ZmanimLocation location ) {

    JSONObject jsonLocation = new JSONObject();
    jsonLocation.put( LATITUDE, location.getLatitude() );
    jsonLocation.put( LONGITUDE, location.getLongitude() );
    jsonLocation.put( ELEVATION, location.getElevation() );
    jsonLocation.put( TIME_ZONE, location.getTimeZone().getID() );
    jsonLocation.put( PROVIDER, location.providerName );
    jsonLocation.put( ACCURACY, location.accuracy );

    if ( location.hasName() ) {
      jsonLocation.put( NAME, location.getLocationName() );
    }
    else {
      jsonLocation.put( NAME, NO_NAME );
    }

    jsonLocation.put( USER_NAME, location.originalUserName );

    return jsonLocation.toString( 2 );
  }


  @SuppressWarnings( "FeatureEnvy" )
  public static ZmanimLocation fromJSON( String json ) {

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
    String userName = root.optString( USER_NAME );

    return new ZmanimLocation.Builder()
      .atLatitude( latitude )
      .atLongitude( longitude )
      .atElevation( elevation )
      .withAccuracy( accuracy )
      .fromProvider( provider )
      .inTimeZone( timeZone )
      .locationName( name )
      .userName( userName )
      .build();
  }

}
