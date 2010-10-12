/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import com.gindin.zmanim.android.prefs.LocationProviders;

/**
 * Location provider that deals with a static latitude & longitude location.
 */
class LatitudeLongitudeLocationProvider
	extends LocationProvider {


	/** The location represented by this provider. */
	private final Location                                    ourLocation = new Location( "Zmanim Latitude & Longitude Provider" );


	LatitudeLongitudeLocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		super( activity, locationManagementPrefs );

		updateLatitudeFromPrefs();
		updateLongitudeFromPrefs();
	}


	@Override
	public void onLocationProvidersChanged() {
		// Doesn't matter to us.
	}


	@Override
	protected Location getLastKnownLocation() {
		return ourLocation;
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

				if ( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name().equals( prefKey ) ) {
					updateLatitudeFromPrefs();
					fireEvent = true;
				}
				else if ( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name().equals( prefKey ) ) {
					updateLongitudeFromPrefs();
					fireEvent = true;
				}

				if ( fireEvent ) {
					fireNewLocation( ourLocation );
				}
			}
		};
	}
	

	private void updateLatitudeFromPrefs() {

		String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
		try {
			ourLocation.setLatitude( Double.parseDouble( latitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + latitudePref ) );
		}
	}


	private void updateLongitudeFromPrefs() {

		String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
		try {
			ourLocation.setLongitude( Double.parseDouble( longitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + longitudePref ) );
		}
	}




}  // End of LatitudeLongitudeLocationProvider class
