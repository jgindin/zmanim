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
 *
 * TODO:
 *  1. Add new pref: Use system time zone?
 *  2. If false, enable a picker that allows user to set the timezone
 */
public class EditLocationProviderPreferences
	extends PreferenceActivity {

	private boolean                                           handlingPrefChange = false;

	private CheckBoxPreference                                useDynamicPos;
	private CheckBoxPreference                                useStaticLocation;
	private CheckBoxPreference                                useLatLong;
	private EditTextPreference                                latitude;
	private EditTextPreference                                longitude;
	private EditTextPreference                                place;

	protected SharedPreferences                               prefs;



	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );
		getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.LOCATION_MANAGEMENT.name() );
		addPreferencesFromResource( R.xml.location_provider_prefs );

		prefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );


		useDynamicPos = (CheckBoxPreference)findPreference( LocationProviders.DYNAMIC_POSITION.name() );
		useStaticLocation = (CheckBoxPreference)findPreference( LocationProviders.STATIC_POSITION.name() );
		useLatLong = (CheckBoxPreference)findPreference( LocationProviders.LATITUDE_LONGITUDE_POSITION.name() );
		place = (EditTextPreference)findPreference( LocationProviders.StaticProviderOptions.PLACE.name() );
		latitude = (EditTextPreference)findPreference( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name() );
		longitude = (EditTextPreference)findPreference( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name() );

		configureDependencies();

		if ( useDynamicPos.isChecked() ) {
			dynamicProviderSelected();
		}
		else if ( useStaticLocation.isChecked() ) {
			staticProviderSelected();
		}
		else if ( useLatLong.isChecked() ) {
			latLongProviderSelected();
		}
		else {
			// No preference set? Set the dynamic...
			prefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
		}
	}


	private void configureDependencies() {

		Preference.OnPreferenceChangeListener prefChangeListener = new Preference.OnPreferenceChangeListener() {

			@SuppressWarnings( { "UnnecessaryUnboxing" } )
			public boolean onPreferenceChange(
				Preference  preference,
				Object      newValue
			) {

				if ( handlingPrefChange ) {
					return true;
				}

				handlingPrefChange = true;

				try {

					// TODO: I'm trying to make the checkboxes behave like radio buttons. However, neither setting the
					//  preference value nor setting the field to be checked seems to actually make the checkbox stay
					//  checked.

					if ( useDynamicPos == preference ) {

						if ( ((Boolean)newValue).booleanValue() ) {
							dynamicProviderSelected();
						}
						else {
							prefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
						}
					}
					else if ( useStaticLocation == preference ) {
						if ( ((Boolean)newValue).booleanValue() ) {
							staticProviderSelected();
						}
						else {
							prefs.edit().putBoolean( LocationProviders.STATIC_POSITION.name(), true ).commit();
						}
					}
					else if ( useLatLong == preference ) {
						if ( ((Boolean)newValue).booleanValue() ) {
							latLongProviderSelected();
						}
						else {
							prefs.edit().putBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), true ).commit();
						}
					}
					else if ( place == preference ) {
						updateSummary( place, newValue.toString(), "" );
					}
					else if ( latitude == preference ) {
						updateSummary( latitude, newValue.toString(), getString( R.string.use_latitude_pref_summary ) );
					}
					else if ( longitude == preference ) {
						updateSummary( longitude, newValue.toString(), getString( R.string.use_longitude_pref_summary ) );
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
		place.setOnPreferenceChangeListener( prefChangeListener );
		latitude.setOnPreferenceChangeListener( prefChangeListener );
		longitude.setOnPreferenceChangeListener( prefChangeListener );
	}


	private void dynamicProviderSelected() {

		useStaticLocation.setChecked( false );
		useLatLong.setChecked( false );
		updateSummary( place, "", getString( R.string.static_location_pref_summary ) );
		updateSummary( latitude, "", getString( R.string.use_latitude_pref_summary ) );
		updateSummary( longitude, "", getString( R.string.use_longitude_pref_summary ) );
	}


	private void staticProviderSelected() {

		useDynamicPos.setChecked( false );
		useLatLong.setChecked( false );
		updateSummary( latitude, "", getString( R.string.use_latitude_pref_summary ) );
		updateSummary( longitude, "", getString( R.string.use_longitude_pref_summary ) );

		String placeName = prefs.getString( LocationProviders.StaticProviderOptions.PLACE.name(), "" );
		updateSummary( place, placeName, "" );
	}


	private void latLongProviderSelected() {

		useDynamicPos.setChecked( false );
		useStaticLocation.setChecked( false );
		updateSummary( place, "", getString( R.string.static_location_pref_summary ) );

		String latitudeDegrees = prefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "-1" );
		if ( !"-1".equals( latitudeDegrees ) ) {
			updateSummary( latitude, latitudeDegrees, getString( R.string.use_latitude_pref_summary ) );
		}

		String longitudeDegrees = prefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "-1" );
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
