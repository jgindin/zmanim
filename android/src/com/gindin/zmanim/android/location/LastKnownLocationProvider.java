/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;


import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;

/**
 * This is a pseudo-location provider that is used to keep track of the last known location. It cannot be a real location
 *  provider because that would require various permissions that we just don't need. And, there's some discussion
 *  that such permissions actually can only be granted if its installed in a package signed with the system key.
 *  See http://stackoverflow.com/questions/3394626/how-to-provide-your-own-locationprovider-on-android.
 */
class LastKnownLocationProvider {

	public static final String                                NAME = "LastKnownLocationProvider";

	private static final String                               HAS_ACCURACY = "lastFixHasAccuracy";
	private static final String                               ACCURACY = "lastFixAccuracy";

	private static final String                               HAS_ALTITUDE = "lastfixHasAltitude";
	private static final String                               ALTITUDE = "lastfixAltitude";

	private static final String                               BEARING = "lastfixBearing";
	private static final String                               HAS_BEARING = "lastfixHasBearing";

	private static final String                               HAS_SPEED = "lastfixHasSpeed";
	private static final String                               SPEED = "lastfixSpeed";

	private static final String                               LATITUDE = "lastfixLatitude";
	private static final String                               LONGITUDE = "lastfixLongitude";
	private static final String                               PROVIDER_NAME = "lastFixProviderName";
	private static final String                               TIME = "lastfixTime";


	/** Stores data that we need between invocations. */
	private final SharedPreferences                           preferences;


	private Location                                          lastKnownLocation;


	LastKnownLocationProvider( SharedPreferences preferences ) {
		this.preferences = preferences;

		String lastFixProviderName = preferences.getString( PROVIDER_NAME, null );
		if ( null != lastFixProviderName ) {

			lastKnownLocation = new Location( lastFixProviderName );
			if ( preferences.getBoolean( HAS_ACCURACY, false ) ) {
				lastKnownLocation.setAccuracy( preferences.getFloat( ACCURACY, 0 ) );
			}

			if ( preferences.getBoolean( HAS_ALTITUDE, false ) ) {
				lastKnownLocation.setAltitude( Double.parseDouble( preferences.getString( ALTITUDE, "0" ) ) );
			}

			if ( preferences.getBoolean( HAS_BEARING, false ) ) {
				lastKnownLocation.setBearing( preferences.getFloat( BEARING, 0.0f ) );
			}

			lastKnownLocation.setLatitude( Double.parseDouble( preferences.getString( LATITUDE, "0" ) ) );
			lastKnownLocation.setLongitude( Double.parseDouble( preferences.getString( LONGITUDE, "0" ) ) );

			if ( preferences.getBoolean( HAS_SPEED, false ) ) {
				lastKnownLocation.setSpeed( preferences.getFloat( SPEED, 0.0f ) );
			}

			lastKnownLocation.setTime( preferences.getLong( TIME, 0 ) );
		}

	}


	public int getPowerRequirement() {
		return Criteria.POWER_LOW;
	}


	public boolean supportsAltitude() {
		return preferences.getBoolean( HAS_ALTITUDE, false );
	}


	public boolean supportsBearing() {
		return preferences.getBoolean( HAS_BEARING, false );
	}


	public boolean supportsSpeed() {
		return preferences.getBoolean( HAS_SPEED, false );
	}


	public float getAccuracy() {
		return lastKnownLocation.getAccuracy();
	}


	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}


	public void update( Location newLocation ) {

		if ( null == newLocation ) {
			return;
		}

		final SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean( HAS_ACCURACY, newLocation.hasAccuracy() );
		editor.putFloat( ACCURACY, newLocation.getAccuracy() );

		editor.putBoolean( HAS_ALTITUDE, newLocation.hasAltitude() );
		editor.putString( ALTITUDE, Double.toString( newLocation.getAltitude() ) );

		editor.putBoolean( HAS_BEARING, newLocation.hasBearing() );
		editor.putFloat( BEARING, newLocation.getBearing() );

		editor.putString( LATITUDE, Double.toString( newLocation.getLatitude() ) );
		editor.putString( LONGITUDE, Double.toString( newLocation.getLongitude() ) );

		editor.putString( PROVIDER_NAME, newLocation.getProvider() );

		editor.putBoolean( HAS_SPEED, newLocation.hasSpeed() );
		editor.putFloat( SPEED, newLocation.getSpeed() );

		editor.putLong( TIME, newLocation.getTime() );

		editor.apply();
	}

} // End of LocationProvider class
