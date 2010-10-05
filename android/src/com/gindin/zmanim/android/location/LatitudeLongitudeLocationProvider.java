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
	private final Location                                    ourLocation = new Location( "zmanim latitude & longitude" );

	/** Listen for the user changing where they want to be. */
	private final SharedPreferences.OnSharedPreferenceChangeListener preferredLocationListener;



	LatitudeLongitudeLocationProvider( Activity activity ) {

		super( activity );

		updateLatitudeFromPrefs();
		updateLongitudeFromPrefs();

		preferredLocationListener = createPreferredLocationListener();
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
	protected void onDestroy() {

		locationManagementPrefs.unregisterOnSharedPreferenceChangeListener( preferredLocationListener );
		super.onDestroy();
	}


	@Override
	public boolean addListener( Listener listener ) {

		if ( super.addListener( listener ) ) {
			listener.onLocationChanged( ourLocation, lookupLocationName( ourLocation ) );
			return true;
		}

		return false;
	}


	private SharedPreferences.OnSharedPreferenceChangeListener createPreferredLocationListener() {

		return new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences,
				String            prefKey
			) {

				boolean fireEvent = false;

				if ( LocationProviders.StaticPositionOptions.LATITUDE.name().equals( prefKey ) ) {
					updateLatitudeFromPrefs();
					fireEvent = true;
				}
				else if ( LocationProviders.StaticPositionOptions.LONGITUDE.name().equals( prefKey ) ) {
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

		String latitudePref = locationManagementPrefs.getString( LocationProviders.StaticPositionOptions.LATITUDE.name(), "0" );
		try {
			ourLocation.setLatitude( Double.parseDouble( latitudePref ) );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Bad latitude: " + latitudePref ) );
		}
	}


	private void updateLongitudeFromPrefs() {

		String longitudePref = locationManagementPrefs.getString( LocationProviders.StaticPositionOptions.LONGITUDE.name(), "0" );
		try {
			ourLocation.setLongitude( Double.parseDouble( longitudePref ) );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Bad longitude: " + longitudePref ) );
		}
	}




}  // End of LatitudeLongitudeLocationProvider class
