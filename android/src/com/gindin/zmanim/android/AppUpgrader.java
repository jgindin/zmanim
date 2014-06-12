/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;

import android.content.Context;
import android.content.SharedPreferences;


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

}  // End of AppUpgrader class
