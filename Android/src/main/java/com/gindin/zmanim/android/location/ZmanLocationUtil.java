/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.TimeZone;

/**
 * Since the ZmanLocation class is in our ZmanLib module, which knows nothing about Android, the methods in this
 *  class are intended to serve as a bit of a bridge.
 */
public class ZmanLocationUtil {


  private static final String                               SAVED_LOCATION_FILENAME = "LastKnownLocationProvider";

  private static final String                               ACCURACY = "accuracy";
  private static final String                               ELEVATION = "elevation";
  private static final String                               LATITUDE = "latitude";
  private static final String                               LOCATION_NAME = "location name";
  private static final String                               LOCATION_TIME = "location time";
  private static final String                               LONGITUDE = "longitude";
  private static final String                               PROVIDER_NAME = "provider";
  private static final String                               TIME_ZONE = "time zone";
  private static final String                               USER_NAME = "user name";


  @SuppressWarnings( "FeatureEnvy" )
  @NonNull
  public static ZmanimLocation fromLocation(
    @Nullable Location  location,
    @Nullable TimeZone  timeZone
  ) {

    if ( location == null ) {
      return new ZmanimLocation.Builder().invalidLocation();
    }

    ZmanimLocation.Builder builder = new ZmanimLocation.Builder()
      .fromProvider( location.getProvider() )
      .at( location.getTime() )
      .atLatitude( location.getLatitude() )
      .atLongitude( location.getLongitude() );

    if ( location.hasAccuracy() ) { builder.withAccuracy( location.getAccuracy() ); }
    if ( location.hasAltitude() ) { builder.atElevation( location.getAltitude() ); }
    if ( timeZone != null ) { builder.inTimeZone( timeZone ); }

    return builder.build();
  }


  @NonNull
  public static Bundle toBundle(
    @Nullable ZmanimLocation  location
  ) {

    Bundle bundle = new Bundle();

    if ( location != null ) {

      bundle.putFloat( ACCURACY, location.accuracy );
      bundle.putDouble( ELEVATION, location.getElevation() );
      bundle.putDouble( LATITUDE, location.getLatitude() );
      bundle.putString( LOCATION_NAME, location.getLocationName() );
      bundle.putLong( LOCATION_TIME, location.locationTime );
      bundle.putDouble( LONGITUDE, location.getLongitude() );
      bundle.putString( PROVIDER_NAME, location.providerName );
      bundle.putSerializable( TIME_ZONE, location.getTimeZone() );
      bundle.putString( USER_NAME, location.originalUserName );
    }

    return bundle;
  }


  @SuppressWarnings( "FeatureEnvy" )
  @NonNull
  public static ZmanimLocation fromBundle(
    @Nullable Bundle bundle
  ) {

    if ( ( bundle == null ) || bundle.keySet().isEmpty() )  {
      return new ZmanimLocation.Builder().invalidLocation();
    }

    return new ZmanimLocation.Builder()
      .withAccuracy( bundle.getFloat( ACCURACY ) )
      .atElevation( bundle.getDouble( ELEVATION ) )
      .atLatitude( bundle.getDouble( LATITUDE ) )
      .locationName( bundle.getString( LOCATION_NAME ) )
      .at( bundle.getLong( LOCATION_TIME ) )
      .atLongitude( bundle.getDouble( LONGITUDE ) )
      .fromProvider( bundle.getString( PROVIDER_NAME ) )
      .inTimeZone( (TimeZone)bundle.getSerializable( TIME_ZONE ) )
      .userName( bundle.getString( USER_NAME ) )
      .build();
  }


  public static void save(
    Context         context,
    ZmanimLocation  location
  ) {

    if ( location == null ) {
      return;
    }

    try ( FileOutputStream output = context.openFileOutput( SAVED_LOCATION_FILENAME, Context.MODE_PRIVATE ) ) {

      location.save( output );
      output.flush();
      output.getFD().sync();
    }
    catch ( Exception e ) {
      // Just means we can't actually save the last location, and next time the user starts we'll have to go
      //  query from scratch.
    }
  }


  public static ZmanimLocation load(
    Context context
  ) {

    ZmanimLocation location;

    try ( FileInputStream input = context.openFileInput( SAVED_LOCATION_FILENAME ) ) {
      location = ZmanimLocation.load( input );
    }
    catch ( Exception e ) {
      // Oh well, we can't read the last location, so...we just will have to deal ...
//      LogUtils.logError( "LKLP::ctor", "Couldn't read last location", e );
      location = new ZmanimLocation.Builder().invalidLocation();
    }

    return location;
  }

} // End of ZmanLocationUtil class
