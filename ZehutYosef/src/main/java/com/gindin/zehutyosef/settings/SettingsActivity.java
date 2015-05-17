package com.gindin.zehutyosef.settings;

import com.gindin.zehutyosef.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Activity for the user to configure their preferences.
 */
public class SettingsActivity
	extends PreferenceActivity {


	@Override
	public void onBuildHeaders( final List<Header> target ) {
		loadHeadersFromResource( R.xml.preference_headers, target );
	}


	@Override
	protected boolean isValidFragment( final String fragmentName ) {
		return Fonts.class.getName().equals( fragmentName );
	}


	public static class Fonts
		extends PreferenceFragment {


		@Override
		public void onCreate( final Bundle savedInstanceState ) {
			super.onCreate( savedInstanceState );
			getPreferenceManager().setSharedPreferencesName( "font_preference" );
			addPreferencesFromResource( R.xml.font_choice );
		}
	}

} // End of SettingsActivity class
