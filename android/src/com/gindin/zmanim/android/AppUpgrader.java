/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.Version;
import com.gindin.zmanim.android.location.LastKnownLocationProvider;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.location.ZmanimLocation;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Used from the main activity to check if any database, preferences, or other data needs to be upgraded.
 */
class AppUpgrader {

	private static final String                               LAST_KNOWN_CODE_VERSION = "lastKnownCodeVersion";

	public static void handle(
		ZmanimActivity              zmanimActivity,
		PersonalPrefsManager        personalPrefsManager,
		ZmanimSelectionPrefsManager zmanimSelectionPrefsManager
	) {

		// Compare our version to the the last known version and see if anything needs to be upgraded.
		int thisCodeVersion = Version.androidCodeVersion;

		SharedPreferences prefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.VERSION.name(), Context.MODE_PRIVATE );
		int lastKnownCodeVersion = prefs.getInt( LAST_KNOWN_CODE_VERSION, -1 );

		if ( thisCodeVersion > lastKnownCodeVersion ) {
			try {

				personalPrefsManager.onHandleUpgrade( thisCodeVersion, lastKnownCodeVersion );
				zmanimSelectionPrefsManager.onHandleUpgrade( thisCodeVersion, lastKnownCodeVersion );
				upgradeLocationPrefs( zmanimActivity, lastKnownCodeVersion );
			}
			catch ( Throwable e ) {
				//noinspection unchecked
				LogUtils.logError( "doUpgrade", "", e,
					new Pair<String, String>( "ver", Integer.toString( thisCodeVersion ) ),
					new Pair<String, String>( "old", Integer.toString( lastKnownCodeVersion ) )
				);
			}
		}

		final SharedPreferences.Editor editor = prefs.edit();
		editor.putInt( LAST_KNOWN_CODE_VERSION, thisCodeVersion );
		editor.apply();
	}


	private static void upgradeLocationPrefs(
		ZmanimActivity    activity,
		int               lastKnownCodeVersion
	) {

		if ( lastKnownCodeVersion > 230 ) {
			return;
		}

		Location lastKnownLocation;

		SharedPreferences locationPreferences = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		String lastFixProviderName = locationPreferences.getString( "lastFixProviderName", null );
		if ( null != lastFixProviderName ) {

			lastKnownLocation = new Location( lastFixProviderName );
			if ( locationPreferences.getBoolean( "lastFixHasAccuracy", false ) ) {
				lastKnownLocation.setAccuracy( locationPreferences.getFloat( "lastFixAccuracy", 0 ) );
			}

			if ( locationPreferences.getBoolean( "lastfixHasAltitude", false ) ) {
				lastKnownLocation.setAltitude( Double.parseDouble( locationPreferences.getString( "lastfixAltitude", "0" ) ) );
			}

			if ( locationPreferences.getBoolean( "lastfixHasBearing", false ) ) {
				lastKnownLocation.setBearing( locationPreferences.getFloat( "lastfixBearing", 0.0f ) );
			}

			lastKnownLocation.setLatitude( Double.parseDouble( locationPreferences.getString( "lastfixLatitude", "0" ) ) );
			lastKnownLocation.setLongitude( Double.parseDouble( locationPreferences.getString( "lastfixLongitude", "0" ) ) );

			if ( locationPreferences.getBoolean( "lastfixHasSpeed", false ) ) {
				lastKnownLocation.setSpeed( locationPreferences.getFloat( "lastfixSpeed", 0.0f ) );
			}

			lastKnownLocation.setTime( locationPreferences.getLong( "lastfixTime", 0 ) );

			ZmanimLocation location = new ZmanimLocation.Builder()
				.from( lastKnownLocation )
				.build();

			OutputStream output = null;
			try {
				output = activity.openFileOutput( LastKnownLocationProvider.NAME, Context.MODE_PRIVATE );
			  location.save( output );
			}
			catch ( IOException e ) {
				// Oh well, we'll lose the last known location, and we'll have to acquire the current location.
				LogUtils.logError( "AppUpgrader::locationPrefs", "Couldn't upgrade last location", e );
			}
			finally {
				if ( output != null ) {
					try {
						output.close();
					}
					catch ( IOException e ) {
						// too bad...
					}
				}
			}
		}

	}

}  // End of AppUpgrader class
