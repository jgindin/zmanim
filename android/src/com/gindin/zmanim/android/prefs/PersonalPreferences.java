/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity for editing personal preferences.
 */
public class PersonalPreferences
	extends PreferenceActivity {

	public static enum Prefs {
		SHOW_SECONDS,
		USE_ELEVATION
	}


	@Override
	public void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.PERSONAL.name() );
		addPreferencesFromResource( R.xml.personal_prefs );
	}

}  // End of PersonalPreferences class
