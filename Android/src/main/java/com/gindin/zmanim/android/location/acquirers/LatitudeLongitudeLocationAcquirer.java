/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.LatitudeLongitudeParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.Nullable;

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


  @Override
  public void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage ) {

    Location ourLocation = new Location( "Zmanim Latitude & Longitude Provider" );

    String error = updateLatitudeFromPrefs( locationManagementPrefs, ourLocation );
    if ( error != null ) {
      fireLocationNotAcquired( new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + error ) );
      return;
    }

    error = updateLongitudeFromPrefs( locationManagementPrefs, ourLocation );
    if ( error != null ) {
      fireLocationNotAcquired( new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + error ) );
      return;
    }

    updateElevationFromPrefs( locationManagementPrefs, ourLocation );

    fireLocationAcquired( ourLocation );
  }


  @Nullable
  private String updateLatitudeFromPrefs(
    SharedPreferences locationManagementPrefs,
    Location          ourLocation
  ) {

    String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
    if ( latitudePref.isEmpty() ) {
      latitudePref = "0";
    }

    try {
      ourLocation.setLatitude( LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( latitudePref ) );
      ourLocation.setTime( System.currentTimeMillis() );

      return null;
    }
    catch ( Exception e ) {
      return latitudePref;
    }
  }


  @Nullable
  private String updateLongitudeFromPrefs(
    SharedPreferences locationManagementPrefs,
    Location          ourLocation
  ) {

    String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
    if ( longitudePref.isEmpty() ) {
      longitudePref = "0";
    }

    try {
      ourLocation.setLongitude( LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( longitudePref ) );
      ourLocation.setTime( System.currentTimeMillis() );

      return null;
    }
    catch ( Exception e ) {
      return longitudePref;
    }
  }


  private void updateElevationFromPrefs(
    SharedPreferences locationManagementPrefs,
    Location          ourLocation
  ) {

    String elevationPref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name(), "0" );
    if ( elevationPref.isEmpty() ) {
      ourLocation.setAltitude( 0.0 );
    }
    else {
      ourLocation.setAltitude( Double.parseDouble( elevationPref ) );
    }

  }

}  // End of LatitudeLongitudeLocationProvider class
