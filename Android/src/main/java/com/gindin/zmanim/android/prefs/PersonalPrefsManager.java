/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.ZmanimActivity;

import android.content.SharedPreferences;

/**
 * Takes care of dealing with the user's preferences--invoking the activity, notifying the main app
 *  that it needs to update, etc...
 */
public class PersonalPrefsManager
	extends AbstractPrefsManager<Class<PersonalPreferences>> {


	public PersonalPrefsManager( ZmanimActivity activity ) {
		super( activity, ZmanimPrefs.PERSONAL, PersonalPreferences.class );

	}


	@Override
	@SuppressWarnings( { "MagicNumber" } )
	public void onHandleUpgrade(
		int   thisCodeVersion,
		int   lastKnownCodeVersion
	) {

		if ( lastKnownCodeVersion <= 66 ) {
			final SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
			editor.apply();
		}

		if ( lastKnownCodeVersion <= 229 ) {
			final SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true );
			editor.apply();
		}
	}


	@Override
	protected Activities getPrefActivity() {
		return Activities.PERSONAL_PREFERENCS;
	}

}  // End of PersonalPrefsManager class
