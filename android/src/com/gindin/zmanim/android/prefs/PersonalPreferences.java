/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.gindin.zmanim.android.R;

/**
 *
 */
public class PersonalPreferences
	extends PreferenceActivity {

	public static enum Prefs {
		SHOW_SECONDS
	}


	@Override
	public void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.PERSONAL.name() );
		addPreferencesFromResource( R.xml.personal_prefs );
	}

}  // End of PersonalPreferences class
