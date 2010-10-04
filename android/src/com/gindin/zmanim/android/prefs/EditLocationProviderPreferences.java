/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.gindin.zmanim.android.R;

/**
 * Activity which is invoked when the user wants to edit their preferences around determining location.
 */
public class EditLocationProviderPreferences
	extends PreferenceActivity {

	private boolean                                           handlingPrefChange = false;


	@Override
	public void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.location_provider_prefs );

		configureDependencies();
	}


	private void configureDependencies() {

		final CheckBoxPreference useDynamicPos = (CheckBoxPreference)findPreference( LocationProviders.DYNAMIC_POSITION.providerType );
		final CheckBoxPreference useStaticLocation = (CheckBoxPreference)findPreference( LocationProviders.STATIC_POSITION.providerType );
		final CheckBoxPreference useLatLong = (CheckBoxPreference)findPreference( LocationProviders.LATITUDE_LONGITUDE_POSITION.providerType );

		Preference.OnPreferenceChangeListener prefChangeListener = new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(
				Preference  preference,
				Object      newValue
			) {

				if ( handlingPrefChange ) {
					return true;
				}

				handlingPrefChange = true;

				try {

					if ( useDynamicPos == preference ) {
						useStaticLocation.setChecked( false );
						useLatLong.setChecked( false );
					}
					else if ( useStaticLocation == preference ) {
						useDynamicPos.setChecked( false );
						useLatLong.setChecked( false );
					}
					else if ( useLatLong == preference ) {
						useDynamicPos.setChecked( false );
						useStaticLocation.setChecked( false );

					}
				}
				finally {
					handlingPrefChange = false;
				}

				// Returning true will apply the update.
				return true;
			}
		};

		useDynamicPos.setOnPreferenceChangeListener( prefChangeListener );
		useStaticLocation.setOnPreferenceChangeListener( prefChangeListener );
		useLatLong.setOnPreferenceChangeListener( prefChangeListener );
	}

}  // End of EditLocationProviderPreferences class
