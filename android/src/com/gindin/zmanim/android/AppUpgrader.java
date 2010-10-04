/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

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

		SharedPreferences prefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.VERSION.name, Context.MODE_PRIVATE );
		int lastKnownCodeVersion = prefs.getInt( LAST_KNOWN_CODE_VERSION, -1 );

		if ( thisCodeVersion > lastKnownCodeVersion ) {
			doUpgrade( thisCodeVersion, lastKnownCodeVersion );
		}

		final SharedPreferences.Editor editor = prefs.edit();
		editor.putInt( LAST_KNOWN_CODE_VERSION, thisCodeVersion );
	}


	private static void doUpgrade(
		int thisCodeVersion,
		int lastKnownCodeVersion
	) {

		if ( -1 == lastKnownCodeVersion ) {
			// Nothing to do...we're upgrading from some prior version when we didn't store anything...
		}

	}

}  // End of AppUpgrader class
