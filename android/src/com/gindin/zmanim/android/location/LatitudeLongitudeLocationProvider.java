/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

/**
 * Location provider that deals with a static latitude & longitude location.
 *
 * TODO: Need to listen for pref changes so we can update when the user changes the lat & long...
 */
class LatitudeLongitudeLocationProvider
	extends LocationProvider {

	/** Name of the preference from which we get the user-specified latitude. */
	private static final String                               LATITUDE = "latitude";

	/** Name of the preference from which we get the user-specified latitude. */
	private static final String                               LONGITUDE = "longitude";


	/** The location represented by this provider. */
	private final Location                                    ourLocation = new Location( "zmanim latitude & longitude" );


	LatitudeLongitudeLocationProvider( Activity activity ) {

		super( activity );

		SharedPreferences prefs = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name, Context.MODE_PRIVATE );
		String latitudePref = prefs.getString( LATITUDE, "0" );
		try {
			ourLocation.setLatitude( Double.parseDouble( latitudePref ) );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Bad latitude: " + latitudePref ) );
		}

		
		String longitudePref = prefs.getString( LONGITUDE, "0" );
		try {
			ourLocation.setLongitude( Double.parseDouble( longitudePref ) );
		}
		catch ( NumberFormatException e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Bad longitude: " + longitudePref ) );
		}
	}


	@Override
	public void onLocationProvidersChanged() {
		// Doesn't matter to us.
	}


	@Override
	protected Location getLastKnownLocation() {
		return ourLocation;
	}

}  // End of LatitudeLongitudeLocationProvider class
