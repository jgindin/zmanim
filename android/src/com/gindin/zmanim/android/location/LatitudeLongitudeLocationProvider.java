/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import com.gindin.util.location.LatitudeLongitudeParser;
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
		// nothing to do
	}


	private void updateLatitudeFromPrefs() {

		String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
		try {
			ourLocation.setLatitude( LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( latitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + latitudePref ) );
		}
	}


	private void updateLongitudeFromPrefs() {

		String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
		try {
			ourLocation.setLongitude( LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( longitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + longitudePref ) );
		}
	}


}  // End of LatitudeLongitudeLocationProvider class
