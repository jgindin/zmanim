/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import net.sourceforge.zmanim.util.GeoLocation;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;

/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 * <p/>
 * TODO: Look at using the Geocoder class to work out what the place is called given the lat & long.
 */
public class AndroidGeoLocation
	extends GeoLocation {

	private static final Location                             SEATTLE;

	private final Context                                     context;

	private boolean                                           locationWasDetermined = false;

	private Formatter                                         rawLatitudeLongitudeFormatter = null;


	static {
		SEATTLE = new Location( "hard-coded" );
		SEATTLE.setLatitude( 47.554163 );
		SEATTLE.setLongitude( -122.257919 );
		SEATTLE.setAltitude( 0.0 );
		SEATTLE.setTime( System.currentTimeMillis() );
	}


	public AndroidGeoLocation(
		Context context
	) {

		this.context = context;

		initFromPhoneLocation();
	}


	private void initFromPhoneLocation() {

		Location location = getLocation();
		setLatitude( location.getLatitude() );
		setLongitude( location.getLongitude() );
		setElevation( location.getAltitude() );
		setTimeZone( TimeZone.getDefault() );

		// We don't yet know the location name...
		setLocationName( null );
	}


	/**
	 * Find a relatively accurate location.
	 * <p/>
	 * Code taken from: http://www.androidsnippets.org/snippets/21/
	 *
	 * @return the phone's location
	 */
	private Location getLocation() {

		LocationManager lm = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
		List<String> providers = lm.getProviders( true );

		// Loop over the array backwards, and if you get an accurate location, then break out the loop
		Location location;

		for ( int i = providers.size() - 1; i >= 0; i-- ) {
			location = lm.getLastKnownLocation( providers.get( i ) );
			if ( location != null ) {
				locationWasDetermined = true;
				return location;
			}
		}

		locationWasDetermined = false;
		return SEATTLE;
	}


	@Override
	public String getLocationName() {

		if ( null != super.getLocationName() ) {
			return super.getLocationName();
		}

		final double latitude = getLatitude();
		final double longitude = getLongitude();

		Geocoder geocoder = new Geocoder( context );

		try {
			final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 1 );
			if ( ( null == addresses ) || ( addresses.isEmpty() ) ) {
				setLocationName( getUnknownLocationName( latitude, longitude ) );
			}
			else {
				final Address address = addresses.get( 0 );
				setLocationName( address.getLocality() + ", " + address.getAdminArea() );
			}
		}
		catch ( IOException e ) {
			setLocationName( getUnknownLocationName( latitude, longitude ) );
		}

		return super.getLocationName();
	}


	public boolean isLocationDetermined() {
		return locationWasDetermined;
	}


	private String getUnknownLocationName(
		double latitude,
		double longitude
	) {

		if ( null == rawLatitudeLongitudeFormatter ) {
			rawLatitudeLongitudeFormatter = new Formatter();
		}

		return rawLatitudeLongitudeFormatter.format( "%.1f lat %.1f long", latitude, longitude ).toString();
	}


}  // End of AndroidGeoLocation class
