package com.gindin.zehutyosef.settings;

import com.gindin.zehutyosef.R;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity for the user to configure their preferences.
 *
 * See http://developer.android.com/guide/topics/ui/settings.html
 */
public class Settings
	extends Activity {


	public enum Preference {

		FontChoice( "pref_font_choice" ),
		KeepScreenOn( "pref_keep_screen_on" ),
		FullScreen( "pref_full_screen_on" )
		;


		public final String                                     key;

		Preference( final String key ) {
			this.key = key;
		}
	}




	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		getFragmentManager()
			.beginTransaction()
			.replace( android.R.id.content, new SettingFragment() )
			.commit();
	}


	public static class SettingFragment
		extends PreferenceFragment {


		@Override
		public void onCreate( final Bundle savedInstanceState ) {
			super.onCreate( savedInstanceState );
			addPreferencesFromResource( R.xml.preferences );
		}
	}

} // End of SettingsActivity class
