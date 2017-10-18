/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.eventBus.EventBus;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Uses the built-in Android Geocoder to resolve the location.
 */
class AndroidResolver
  extends LocationResolver {

  private final Geocoder                                    geocoder;


  AndroidResolver(
    EventBus          eventBus,
    Geocoder          geocoder,
    SharedPreferences locationManagementPrefs
  ) {
    super( eventBus, locationManagementPrefs );
    this.geocoder = geocoder;
  }


  @Override
  public void onDestroy() {
    // Nothing to destroy here...
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Nullable
  protected ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation location ) {

    if ( !Geocoder.isPresent() ) {
      return null;
    }

    final double latitude = location.getLatitude();
    final double longitude = location.getLongitude();

    try {
      final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 5 );

      if ( ( null != addresses ) && !addresses.isEmpty() ) {
        Address address = addresses.get( 0 );

        return new ZmanimLocation.Builder()
          .from( location )
          .locationName( addressToLocationName( address ) )
          .atLatitude( address.getLatitude() )
          .atLongitude( address.getLongitude() )
          .build();
      }
    }
    catch ( IOException ioe ) {
      // Docs for Geocoder.getFromLocation say this happens when there's no network connectivity, or
      //  there's some other I/O error. In any case, it can happen...
    }
    catch ( Exception e ) {
      LogUtils.logError( "AR::updateLocationWithName", "", e );
    }

    return null;
  }


  @Nullable
  @SuppressWarnings( "FeatureEnvy" )
  protected ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation location ) {

    ZmanimLocation resolved = null;

    if ( Geocoder.isPresent() ) {

      try {
        List<Address> addresses = geocoder.getFromLocationName( location.getLocationName(), 5 );

        if ( ( null != addresses ) && !addresses.isEmpty() ) {
          final Address address = addresses.get( 0 );
          resolved = new ZmanimLocation.Builder()
            .locationName( addressToLocationName( address ) )
            .atLatitude( address.getLatitude() )
            .atLongitude( address.getLongitude() )
            .inTimeZone( location.getTimeZone() )
            .build();
        }
      }
      catch ( IOException ioe ) {
        // Docs for Geocoder.getFromLocationName say this happens when there's no network connectivity, or
        //  there's some other I/O error. In any case, it can happen...
        //LogUtils.logError( "AR::updateLocationFromPlace", "", ioe );
      }
      catch ( Exception e ) {
        LogUtils.logError( "AR::updateLocationFromPlace", "", e );
      }

    }

    return resolved;
  }


} // End of AndroidResolver class
