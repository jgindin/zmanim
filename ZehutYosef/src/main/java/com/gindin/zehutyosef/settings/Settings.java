package com.gindin.zehutyosef.settings;

import com.gindin.zehutyosef.FontInfo;
import com.gindin.zehutyosef.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Activity for the user to configure their preferences.
 *
 * See http://developer.android.com/guide/topics/ui/settings.html
 */
public class Settings
	extends Activity {


	public enum PreferenceKey {

		FontChoice( "pref_font_choice" ),
		FontSize( "pref_font_size" ),
		KeepScreenOn( "pref_keep_screen_on" ),
		FullScreen( "pref_full_screen_on" )
		;


		public final String                                     key;

		PreferenceKey( final String key ) {
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

		private SharedPreferences.OnSharedPreferenceChangeListener  sharedPreferenceChangeListener;

		@Override
		public void onCreate( final Bundle savedInstanceState ) {
			super.onCreate( savedInstanceState );
			addPreferencesFromResource( R.xml.preferences );
		}


		@Override
		public void onStart() {
			super.onStart();

			sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
				@Override
				public void onSharedPreferenceChanged(
					final SharedPreferences sharedPreferences,
					final String            key
				) {

					if ( key.equals( PreferenceKey.FontChoice.key ) ) {
						String fontIdName = sharedPreferences.getString( key, "" );
						FontInfo fontInfo = FontInfo.getByIdName( fontIdName );
						Preference pref = findPreference( key );
						pref.setSummary( fontInfo.getName() );
					}
					else if ( key.equals( PreferenceKey.FontSize.key ) ) {
						Preference pref = findPreference( key );
						pref.setSummary( sharedPreferences.getString( key, "14" ) );
					}
				}
			};

			PreferenceManager.getDefaultSharedPreferences( getActivity() )
				.registerOnSharedPreferenceChangeListener( sharedPreferenceChangeListener );
		}


		@Override
		public void onStop() {
			super.onStop();

			PreferenceManager.getDefaultSharedPreferences( getActivity() )
				.unregisterOnSharedPreferenceChangeListener( sharedPreferenceChangeListener );

			super.onPause();
		}

	}

} // End of SettingsActivity class
