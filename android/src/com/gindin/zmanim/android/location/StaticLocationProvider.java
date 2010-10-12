/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
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
	public boolean addListener( Listener listener ) {

		if ( super.addListener( listener ) ) {
			// Don't want to fireNewLocation because that would notify *everyone*...
			listener.onLocationChanged( ourLocation, lookupLocationName( ourLocation ) );
			return true;
		}

		return false;
	}


	@Override
	protected SharedPreferences.OnSharedPreferenceChangeListener createLocationManagementPrefsListener() {

		return new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences,
				String            prefKey
			) {

				boolean fireEvent = false;

				if ( LocationProviders.StaticProviderOptions.PLACE.name().equals( prefKey ) ) {
					updateLocationFromPlace();
					fireEvent = true;
				}

				if ( fireEvent ) {
					fireNewLocation( ourLocation );
				}
			}
		};
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
				ourLocation.setTime( System.currentTimeMillis() );
				ourLocation.setLongitude( addresses.get( 0 ).getLongitude() );
				ourLocation.setLatitude( addresses.get( 0 ).getLatitude() );
			}
		}
		else {
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid place: " + place ) );
		}
	}

}  // End of StaticLocationProvider class
