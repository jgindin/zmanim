/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

/**
 * Location provider that deals with a static latitude & longitude location.
 */
public class LatitudeLongitudeLocationAcquirer
  extends LocationAcquirer {


  public LatitudeLongitudeLocationAcquirer(
    SharedPreferences locationManagementPrefs,
    Context           context
  ) {

    super( locationManagementPrefs, context );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  public void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage ) {

    ZmanimLocation.Builder locationBuilder = new ZmanimLocation.Builder().fromProvider( "Zmanim Latitude & Longitude Provider" );

    String error = updateLatitudeFromPrefs( locationManagementPrefs, locationBuilder );
    if ( error != null ) {
      fireLocationNotAcquired( new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + error ), null );
      return;
    }

    error = updateLongitudeFromPrefs( locationManagementPrefs, locationBuilder );
    if ( error != null ) {
      fireLocationNotAcquired( new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + error ), null );
      return;
    }

    updateElevationFromPrefs( locationManagementPrefs, locationBuilder );
    locationBuilder.at( System.currentTimeMillis() );
    locationBuilder.inTimeZone( getTimeZoneFromPrefs() );

    fireLocationAcquired( locationBuilder.build() );
  }


  @Nullable
  private String updateLatitudeFromPrefs(
    SharedPreferences       locationManagementPrefs,
    ZmanimLocation.Builder  ourLocation
  ) {

    String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
    if ( latitudePref.isEmpty() ) {
      latitudePref = "0";
    }

    try {
      ourLocation.atLatitude( LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( latitudePref ) );

      return null;
    }
    catch ( Exception e ) {
      return latitudePref;
    }
  }


  @Nullable
  private String updateLongitudeFromPrefs(
    SharedPreferences       locationManagementPrefs,
    ZmanimLocation.Builder  ourLocation
  ) {

    String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
    if ( longitudePref.isEmpty() ) {
      longitudePref = "0";
    }

    try {
      ourLocation.atLongitude( LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( longitudePref ) );

      return null;
    }
    catch ( Exception e ) {
      return longitudePref;
    }
  }


  private void updateElevationFromPrefs(
    SharedPreferences       locationManagementPrefs,
    ZmanimLocation.Builder  ourLocation
  ) {

    String elevationPref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name(), "0" );
    if ( elevationPref.isEmpty() ) {
      ourLocation.atElevation( 0.0 );
    }
    else {
      ourLocation.atElevation( Double.parseDouble( elevationPref ) );
    }

  }

}  // End of LatitudeLongitudeLocationProvider class
