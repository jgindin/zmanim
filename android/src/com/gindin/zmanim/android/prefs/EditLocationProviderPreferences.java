/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.*;
import com.gindin.zmanim.android.R;

import java.util.*;

/**
 * Activity which is invoked when the user wants to edit their preferences around determining location.
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

	/**
	 * We split the time zones (e.g., "America/Los_Angeles") into two separate strings to make it easier
	 *  to find what you're looking for.
	 */
	private CheckBoxPreference                                overrideSystemTimeZone;
	private ListPreference                                    timeZoneAreas;
	private ListPreference                                    timeZoneLocales;

	protected SharedPreferences                               prefs;

	/** Only want to build this map once. */
	private static final Map<String, List<String>>            timeZoneAreasToLocales = new HashMap<String, List<String>>();



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
		overrideSystemTimeZone = (CheckBoxPreference)findPreference( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name() );
		timeZoneAreas = (ListPreference)findPreference( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name() );
		timeZoneLocales = (ListPreference)findPreference( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name() );

		configureDependencies();
		configureTimeZones();

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

		
		overridingSystemTimeZone( overrideSystemTimeZone.isChecked() );
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
					else if ( overrideSystemTimeZone == preference ) {
						overridingSystemTimeZone( ((Boolean)newValue).booleanValue() );
					}
					else if ( timeZoneAreas == preference ) {
						updateSummary( timeZoneAreas, "", newValue.toString() );
						updateLocaleSelector();
					}
					else if ( timeZoneLocales == preference ) {
						updateSummary( timeZoneLocales, "", newValue.toString() );
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
		overrideSystemTimeZone.setOnPreferenceChangeListener( prefChangeListener );
		timeZoneAreas.setOnPreferenceChangeListener( prefChangeListener );
		timeZoneLocales.setOnPreferenceChangeListener( prefChangeListener );
	}


	private void configureTimeZones() {

		timeZoneAreas.setEntries( new CharSequence[] { "N/A" } );
		timeZoneAreas.setEntryValues( new CharSequence[] { "-- Loading --" } );
		timeZoneAreas.setEnabled( false );

		AsyncTask<Void, Void, Map<String, List<String>>> lookupTimeZones = new AsyncTask<Void, Void, Map<String, List<String>>>() {
			@Override
			protected Map<String, List<String>> doInBackground( Void... voids ) {

				if ( !timeZoneAreasToLocales.isEmpty() ) {
					return timeZoneAreasToLocales;
				}

				final String[] timeZoneIDs = TimeZone.getAvailableIDs();
				for ( String timeZoneID : timeZoneIDs ) {

					int slash = timeZoneID.indexOf( "/" );
					if ( -1 == slash ) {
						continue;
					}

					String area = timeZoneID.substring( 0, slash );
					String locale = timeZoneID.substring( slash + 1 );
					List<String> locales = timeZoneAreasToLocales.get( area );
					if ( null == locales ) {
						locales = new ArrayList<String>();
						timeZoneAreasToLocales.put( area, locales );
					}
					locales.add( locale );
				}

				return timeZoneAreasToLocales;
			}


			@Override
			protected void onPostExecute( Map<String, List<String>> timeZoneAreasToLocales ) {

				super.onPostExecute( timeZoneAreasToLocales );

				// Only enable if the user has said to override the system time zone AND we have sufficient info...
				boolean shouldEnable = overrideSystemTimeZone.isChecked();

				final Set<String> keys = timeZoneAreasToLocales.keySet();
				final String[] areaNames = keys.toArray( new String[ keys.size() ] );
				Arrays.sort( areaNames );

				timeZoneAreas.setEntries( areaNames );
				timeZoneAreas.setEntryValues( areaNames );

				final String selectedArea = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
				if ( null == selectedArea ) {
					shouldEnable = false;
				}
				else {
					updateLocaleSelector();
				}

				timeZoneAreas.setEnabled( shouldEnable );
				timeZoneLocales.setEnabled( shouldEnable );
			}
		};

		lookupTimeZones.execute( (Void)null );
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


	private void overridingSystemTimeZone(
		boolean override
	) {

		String area = null;
		String locale = null;

		if ( override ) {
			area = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
			locale = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name(), null );

			// We want these enabled, but if there's no entries (yet) then they'll get enabled after we're done
			//  filling them in...
			final CharSequence[] timeZoneAreaEntries = timeZoneAreas.getEntries();
			if ( ( null != timeZoneAreaEntries ) && timeZoneAreaEntries.length > 1 ) {
				timeZoneAreas.setEnabled( true );
				updateLocaleSelector();
			}
		}
		else {
			timeZoneAreas.setEnabled( false );
			timeZoneLocales.setEnabled( false );
		}

		if ( null == area ) {
			area = getString( R.string.time_zone_area_summary );
			locale = getString( R.string.time_zone_locale_summary );
		}

		updateSummary( timeZoneAreas, "", area );
		updateSummary( timeZoneLocales, "", locale );
	}


	private void updateLocaleSelector() {

		final String selectedArea = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
		final String[] localeNames;
		if ( null != selectedArea ) {
			final List<String> locales = timeZoneAreasToLocales.get( selectedArea );
			localeNames = locales.toArray( new String[ locales.size() ] );
			Arrays.sort( localeNames );
			timeZoneLocales.setEnabled( true );
		}
		else {
			localeNames = new String[] { "--No Area Selected--" };
			timeZoneLocales.setEnabled( false );
		}

		timeZoneLocales.setEntries( localeNames );
		timeZoneLocales.setEntryValues( localeNames );
	}


	private void updateSummary(
		Preference  preference,
	  String      prefix,
	  String      suffix
	) {

		preference.setSummary( prefix + " " + suffix );
	}

}  // End of EditLocationProviderPreferences class
