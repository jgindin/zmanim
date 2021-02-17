/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.location.Address;

import java.util.List;

/**
 * Base for all resolvers that take a location and determine the name.
 *<p/>
 *
 * After resolving the location, a work item will be enqueued for the ZmanimService to handle the result.
 */
abstract class LocationResolver {


  static String addressToLocationName( Address address ) {

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


  @Nullable
  final ZmanimLocation resolve(
    @NonNull final ZmanimLocation          location,
    @NonNull final List<LocationResolver>  nextResolvers
  ) {

    final ZmanimLocation zmanimLocation;
    if ( location.hasCoordinates() ) {
      zmanimLocation = resolveFromCoordinates( location );
    }
    else {
      zmanimLocation = resolveFromName( location );
    }

    if ( ( zmanimLocation == null ) && !nextResolvers.isEmpty() ) {
      return nextResolvers.get( 0 ).resolve( location, nextResolvers.subList( 1, nextResolvers.size() ) );
    }
    else {
      return zmanimLocation;
    }
  }


  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the latitude and longitude, and return a new location with all possible details filled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromCoordinates( @NonNull ZmanimLocation zmanimLocation );

  /**
   * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
   *  specified location using the name, and return a new location with all possible details fillled out.
   *
   * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
   */
  @Nullable
  protected abstract ZmanimLocation resolveFromName( @NonNull ZmanimLocation zmanimLocation );


} // End of LocationResolver class
