/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.List;

/**
 * Uses the information in either a RawLocationAcquiredEvent or PlaceAcquiredEvent to fill out the full set of
 *  data about a given location.
 *<p/>
 *
 * After resolving the location, a FullLocationAcquiredEvent will be fired.
 */
abstract class LocationResolver {


  private final Context                                     context;


  protected static String addressToLocationName( Address address ) {

    String locationString = null;

    if ( address == null ) {
      return ZmanimLocation.UNKNOWN_LOCATION;
    }

    final String locality = address.getLocality();
    final String adminArea = address.getAdminArea();
    final String country = address.getCountryName();

    // There are cases where locality is null. No kiddin'...if you use the StaticLocationProvider, and choose
    //  "Brooklyn, New York", the locality comes back null. Go figure...
    //  (Well, that is the case, except that when the StaticLocationProvider goes to look up what the location is
    //    it now stores the returned Address as the Extras in the Location. And, for some reason, *that* Address
    //    comes back properly. Oh well...)
    if ( locality != null ) {
      locationString = locality;

      if ( null != adminArea ) {
        locationString += ", " + adminArea;
      }
      else if ( country != null ) {
        locationString += ", " + country;
      }
    }
    else if ( adminArea != null ) {
      locationString = adminArea;

      if ( country != null ) {
        locationString += ", " + country;
      }
    }
    else {

      final int maxAddressLineIndex = address.getMaxAddressLineIndex();
      if ( maxAddressLineIndex > 0 ) {

        final String postalCode = address.getPostalCode();

        // Find the address line that has the location. In the case mentioned above, address line two is: Brooklyn, NY 11226
        for ( int index = 0; index < maxAddressLineIndex; index++ ) {
          String addrLine = address.getAddressLine( index );
          if ( null == addrLine ) continue;

          if ( ( null != postalCode ) && addrLine.contains( postalCode ) ) {
            locationString = addrLine.substring( 0, addrLine.indexOf( postalCode ) );
            break;
          }
        }
      }
      else if ( null != country ) {
        // Yup. I forced a usage of 55.0, -1.6 and the only info we get back is "United Kingdom".
        locationString = country;
      }

    }

    // Final fallback...if we got nothing then just use the lat & long...
    if ( null == locationString ) {
      locationString = ZmanimLocation.toLatLongString( address.getLatitude(), address.getLongitude() );
    }

    return locationString;
  }


  LocationResolver(
    Context context
  ) {
    this.context = context;
  }


  /**
   * Called when the resolver is being destroyed.
   */
  public abstract void onDestroy();


  final void resolve(
    @NotNull final ZmanimLocation          location,
    @NotNull final List<LocationResolver>  nextResolvers
  ) {

    AsyncTask<ZmanimLocation, Void, ZmanimLocation> task = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
      @Override
      protected ZmanimLocation doInBackground( ZmanimLocation... zmanimLocations ) {

        if ( location.hasCoordinates() ) {
          return resolveFromCoordinatesInBackground( zmanimLocations[0] );
        }
        else {
          return resolveFromNameInBackground( zmanimLocations[0] );
        }
      }


      @Override
      protected void onPostExecute( ZmanimLocation resolvedLocation ) {
        super.onPostExecute( resolvedLocation );
        handlePostExecute( location, resolvedLocation, nextResolvers );
      }
    };

    task.execute( location );
  }


  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the latitude and longitude, and return a new location with all possible details filled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation );

  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the name, and return a new location with all possible details fillled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation );


  private void handlePostExecute(
    @NotNull ZmanimLocation                originalLocation,
    @Nullable ZmanimLocation               resolvedLocation,
    @NotNull final List<LocationResolver>  nextResolvers
  ) {

    if ( ( resolvedLocation == null ) && !nextResolvers.isEmpty() ) {
      nextResolvers.get( 0 ).resolve( originalLocation, nextResolvers.subList( 1, nextResolvers.size() ) );
    }
    else {

      if ( resolvedLocation == null ) {
        //noinspection AssignmentToMethodParameter
        resolvedLocation = new ZmanimLocation.Builder().invalidLocation();
      }

      if ( resolvedLocation.isInvalid || !resolvedLocation.isResolved() ) {
        ZmanimService.enqueueWork( context, new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION ) );
      }
      else {

        ZmanimService.enqueueWork( context, resolvedLocation );

        // Let's keep track of how often we use each resolver.
        LogUtils.logEvent( "LocationResolved",
          Collections.singletonList( new Pair<>( "Resolver", getClass().getSimpleName() ) ) );
      }
    }
  }


} // End of LocationResolver class
