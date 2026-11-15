/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import android.location.Address;

/**
 * The name shown for a place the geocoder returns: the locality with its region or country, or whichever of those the
 *  address has.
 */
final class AddressNames {

  private AddressNames() {
  }


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

}  // End of AddressNames class
