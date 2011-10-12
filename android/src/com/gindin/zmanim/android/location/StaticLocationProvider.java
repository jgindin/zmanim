/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.prefs.LocationProviders;

import java.io.IOException;
import java.util.List;

/**
 * Location provider which uses a place name like "5217 South Brandon St., Seattle, WA" to determine the Zmanim.
 */
class StaticLocationProvider
	extends LocationProvider {

	/** The location represented by this provider. */
	private final Location                                    ourLocation;


	StaticLocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		super( activity, locationManagementPrefs );
		ourLocation = new Location( "Zmanim Place Provider" );

		updateLocationFromPlace();
	}


	@Override
	public void onLocationProvidersChanged() {
		// Doesn't matter to us.
	}


	@Override
	protected void listenerAdded(
		Listener  listener,
		boolean   isFirst
	) {
		// Don't want to fireNewLocation because that would notify *everyone*...
		listener.onLocationChanged( ourLocation, lookupLocationName( ourLocation ) );
	}


	@Override
	protected void listenerRemoved(
		Listener  listener,
		boolean   wasLast
	) {
		// Nothing to do
	}


	@Override
	protected Location getLastKnownLocation() {
		return ourLocation;
	}


	private void updateLocationFromPlace() {

		String place = locationManagementPrefs.getString( LocationProviders.StaticProviderOptions.PLACE.name(), "" );
		if ( place.length() > 0 ) {

			AsyncTask<String, Void, List<Address>> lookupPlaceTask = new AsyncTask<String, Void, List<Address>>() {
				@Override
				protected List<Address> doInBackground( String... places ) {

					try {
						return geocoder.getFromLocationName( places[ 0 ], 5 );
					}
					catch ( IOException e ) {
						LogUtils.logError( "StaticLocationProvider::updateLocationFromPlace", "IOE getting from location name", e );
						return null;
					}
				}
			};

			lookupPlaceTask.execute( place );
			List<Address> addresses;
			try {
				addresses = lookupPlaceTask.get();
			}
			catch ( Exception e ) {
				addresses = null;
			}

			if ( ( null != addresses ) && !addresses.isEmpty() ) {
				final Address address = addresses.get( 0 );

				ourLocation.setTime( System.currentTimeMillis() );
				ourLocation.setLongitude( address.getLongitude() );
				ourLocation.setLatitude( address.getLatitude() );

				Bundle extras = new Bundle();
				extras.putParcelable( ADDRESS, address );
				ourLocation.setExtras( extras );
			}
		}
		else {
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid place: " + place ) );
		}
	}

}  // End of StaticLocationProvider class
