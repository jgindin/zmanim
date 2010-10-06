/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import java.util.Map;

/**
 * Used from the main activity to check if any database, preferences, or other data needs to be upgraded.
 */
class AppUpgrader {

	private static final String                               LAST_KNOWN_CODE_VERSION = "lastKnownCodeVersion";

	public static void handle(
		Activity zmanimActivity
	) {

		// Compare our version to the the last known version and see if anything needs to be upgraded.
		int thisCodeVersion = Version.androidCodeVersion;

		SharedPreferences prefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.VERSION.name(), Context.MODE_PRIVATE );
		int lastKnownCodeVersion = prefs.getInt( LAST_KNOWN_CODE_VERSION, -1 );

		if ( thisCodeVersion > lastKnownCodeVersion ) {
			doUpgrade( zmanimActivity, thisCodeVersion, lastKnownCodeVersion );
		}

		final SharedPreferences.Editor editor = prefs.edit();
		editor.putInt( LAST_KNOWN_CODE_VERSION, thisCodeVersion );
		editor.commit();
	}


	private static void doUpgrade(
		Activity  activity,
		int       thisCodeVersion,
		int       lastKnownCodeVersion
	) {

		if ( -1 == lastKnownCodeVersion ) {

			// We used to store the zmanim selection under the shared prefs key "ZmanimSelection", but now we have an enum
			//  and it's the com.gindin.zmanim.android.prefs.ZmanimPrefs.ZMANIM_SELECTION.
			final SharedPreferences oldPrefs = activity.getSharedPreferences( "ZmanimSelection", Context.MODE_PRIVATE );
			final Map<String,?> zmanimSelections = oldPrefs.getAll();
			if ( null != zmanimSelections ) {

				final SharedPreferences newPrefs = activity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
				final SharedPreferences.Editor newPrefsEditor = newPrefs.edit();

				for ( Map.Entry<String, ?> entry : zmanimSelections.entrySet() ) {
					// We know that we only stored strings here.
					newPrefsEditor.putString( entry.getKey(), (String)entry.getValue() );
				}

				newPrefsEditor.commit();

				// Now remove the old prefs.
				final SharedPreferences.Editor oldPrefsEditor = oldPrefs.edit();
				oldPrefsEditor.clear();
				oldPrefsEditor.commit();
			}
		}

	}

}  // End of AppUpgrader class
