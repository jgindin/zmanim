/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.gindin.zmanim.android.R;

/**
 * Activity which is invoked when the user wants to edit their preferences around determining location.
 */
public class EditLocationProviderPreferences
	extends PreferenceActivity {

	private boolean                                           handlingPrefChange = false;

	private EditTextPreference                                latitude;

	private EditTextPreference                                longitude;



	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );
		getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.LOCATION_MANAGEMENT.name() );
		addPreferencesFromResource( R.xml.location_provider_prefs );

		latitude = (EditTextPreference)findPreference( LocationProviders.StaticPositionOptions.LATITUDE.name() );
		longitude = (EditTextPreference)findPreference( LocationProviders.StaticPositionOptions.LONGITUDE.name() );

		configureDependencies();

		initializeSummaries();
	}


	private void configureDependencies() {

		final CheckBoxPreference useDynamicPos = (CheckBoxPreference)findPreference( LocationProviders.DYNAMIC_POSITION.name() );
		final CheckBoxPreference useStaticLocation = (CheckBoxPreference)findPreference( LocationProviders.STATIC_POSITION.name() );
		final CheckBoxPreference useLatLong = (CheckBoxPreference)findPreference( LocationProviders.LATITUDE_LONGITUDE_POSITION.name() );


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
					else if ( latitude == preference ) {
						updateSummary( latitude, newValue.toString(), EditLocationProviderPreferences.this.getString( R.string.use_latitude_pref_summary ) );
					}
					else if ( longitude == preference ) {
						updateSummary( longitude, newValue.toString(), EditLocationProviderPreferences.this.getString( R.string.use_longitude_pref_summary ) );
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
		latitude.setOnPreferenceChangeListener( prefChangeListener );
		longitude.setOnPreferenceChangeListener( prefChangeListener );
	}


	private void initializeSummaries() {

		SharedPreferences prefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

		String latitudeDegrees = prefs.getString( LocationProviders.StaticPositionOptions.LATITUDE.name(), "-1" );
		if ( !"-1".equals( latitudeDegrees ) ) {
			updateSummary( latitude, latitudeDegrees, getString( R.string.use_latitude_pref_summary ) );
		}

		String longitudeDegrees = prefs.getString( LocationProviders.StaticPositionOptions.LONGITUDE.name(), "-1" );
		if ( !"-1".equals( latitudeDegrees ) ) {
			updateSummary( longitude, longitudeDegrees, getString( R.string.use_longitude_pref_summary ) );
		}
	}


	private void updateSummary(
		Preference  preference,
	  String      prefix,
	  String      suffix
	) {

		preference.setSummary( prefix + " " + suffix );
	}

}  // End of EditLocationProviderPreferences class
