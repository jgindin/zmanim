/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.*;
import androidx.core.app.ActivityCompat;

import java.util.*;

/**
 * Activity which is invoked when the user wants to edit their preferences around determining location.
 *<p/>
 *
 * When this activity completes, it will set a result code of RESULT_OK to indicate the user has made some changes.
 * If no changes were made, then RESULT_CANCELED will be set.
 */
public class LocationProviderPreferences
  extends PreferenceActivity {

  /**
   * Only want to build this map once. Using a TreeMap so that the time zones get sorted upon addition,
   *  and we don't have to do it again..
   */
  private static final Map<String, List<String>>            timeZoneAreasToLocales = new TreeMap<String, List<String>>();

  private boolean                                           handlingPrefChange = false;

  private CheckBoxPreference                                useDynamicPos;
  private CheckBoxPreference                                useStaticLocation;
  private EditTextPreference                                place;
  private EditTextPreference                                placeElevation;
  private CheckBoxPreference                                useLatLong;
  private LongitudeLatitudePreference                       latitude;
  private LongitudeLatitudePreference                       longitude;
  private EditTextPreference                                latLongElevation;

  /**
   * We split the time zones (e.g., "America/Los_Angeles") into two separate strings to make it easier
   *  to find what you're looking for.
   */
  private CheckBoxPreference                                overrideSystemTimeZone;
  private ListPreference                                    timeZoneAreas;
  private ListPreference                                    timeZoneLocales;

  private SharedPreferences                                 prefs;


  /**
   * Prior to invoking the location configuration activity, tuck away the current prefs so that if
   *  the user doesn't make any changes, then we don't do anything upon return.
   */
  private Map<String,?>                                     prefsBeforeEditing;


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
    placeElevation = (EditTextPreference)findPreference( LocationProviders.StaticProviderOptions.PLACE_ELEVATION.name() );
    latitude = (LongitudeLatitudePreference)findPreference( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name() );
    longitude = (LongitudeLatitudePreference)findPreference( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name() );
    latLongElevation = (EditTextPreference)findPreference( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name() );
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
      prefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).apply();
    }

    overridingSystemTimeZone( overrideSystemTimeZone.isChecked() );

    // Capture all the prefs so that if the user doesn't make any changes, we don't do a bunch of recalculations.
    prefsBeforeEditing = prefs.getAll();
  }


  @Override
  public void onBackPressed() {

    boolean changed = false;

    final Map<String, ?> prefsAfterEditing = prefs.getAll();
    if ( !prefsAfterEditing.equals( prefsBeforeEditing ) ) {
      changed = true;
    }

    setResult( changed ? RESULT_OK : RESULT_CANCELED );
    finish();

    super.onBackPressed();
  }


  private void configureDependencies() {

    Preference.OnPreferenceChangeListener prefChangeListener = ( preference, newValue ) -> {

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
            prefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).apply();
          }
        }
        else if ( useStaticLocation == preference ) {
          if ( ((Boolean)newValue).booleanValue() ) {
            staticProviderSelected();
          }
          else {
            prefs.edit().putBoolean( LocationProviders.STATIC_POSITION.name(), true ).apply();
          }
        }
        else if ( useLatLong == preference ) {
          if ( ( (Boolean)newValue ).booleanValue() ) {
            latLongProviderSelected();
          }
          else {
            prefs.edit().putBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), true ).apply();
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
        else if ( ( latLongElevation == preference ) || ( placeElevation == preference ) ) {
          String str = newValue.toString();
          if ( str.isEmpty() || "0".equals( str ) ) {
            updateSummary( preference, getString( R.string.sea_level ), ""  );
          }
          else {
            updateSummary( preference, str, getString( R.string.meters ) );
          }
        }
        else if ( overrideSystemTimeZone == preference ) {
          overridingSystemTimeZone( ( (Boolean)newValue ).booleanValue() );
        }
        else if ( timeZoneAreas == preference ) {
          updateSummary( timeZoneAreas, "", newValue.toString() );
          updateLocaleSelector( newValue.toString() );
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
    };

    useDynamicPos.setOnPreferenceChangeListener( prefChangeListener );
    useStaticLocation.setOnPreferenceChangeListener( prefChangeListener );
    useLatLong.setOnPreferenceChangeListener( prefChangeListener );
    place.setOnPreferenceChangeListener( prefChangeListener );
    placeElevation.setOnPreferenceChangeListener( prefChangeListener );
    latitude.setOnPreferenceChangeListener( prefChangeListener );
    longitude.setOnPreferenceChangeListener( prefChangeListener );
    latLongElevation.setOnPreferenceChangeListener( prefChangeListener );
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

        // Sort the locales now so we don't have to do it later...
        for ( Map.Entry<String, List<String>> entry : timeZoneAreasToLocales.entrySet() ) {
          Collections.sort( entry.getValue() );
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

        timeZoneAreas.setEntries( areaNames );
        timeZoneAreas.setEntryValues( areaNames );

        final String selectedArea = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
        if ( null == selectedArea ) {
          shouldEnable = false;
        }
        else {
          updateLocaleSelector( selectedArea );
        }

        timeZoneAreas.setEnabled( shouldEnable );
        timeZoneLocales.setEnabled( shouldEnable );
      }
    };

    lookupTimeZones.execute( (Void)null );
  }


  private void dynamicProviderSelected() {

    boolean hasFineAccess = ( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED );
    boolean hasCoarseAccess = ( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED );
    if ( !hasFineAccess && !hasCoarseAccess ) {
      new AlertDialog.Builder( this )
        .setTitle( R.string.dynamic_position_pref_title )
        .setMessage( R.string.dynamic_position__requirements )
        .setPositiveButton( R.string.ok, null )
        .show();
    }

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

    String elevationStr = prefs.getString( LocationProviders.StaticProviderOptions.PLACE_ELEVATION.name(), "0" );
    placeElevation.setText( elevationStr );
    if ( "0".equals( elevationStr ) ) {
      updateSummary( placeElevation, getString( R.string.sea_level ), ""  );
    }
    else {
      updateSummary( placeElevation, elevationStr, getString( R.string.meters ) );
    }
  }


  private void latLongProviderSelected() {

    useDynamicPos.setChecked( false );
    useStaticLocation.setChecked( false );
    updateSummary( place, "", getString( R.string.static_location_pref_summary ) );

    String latitudeStr = prefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "-1" );
    if ( !"-1".equals( latitudeStr ) ) {
      updateSummary( latitude, latitudeStr, getString( R.string.use_latitude_pref_summary ) );
    }
    latitude.init( latitudeStr, true );

    String longitudeStr = prefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "-1" );
    if ( !"-1".equals( latitudeStr ) ) {
      updateSummary( longitude, longitudeStr, getString( R.string.use_longitude_pref_summary ) );
    }
    longitude.init( longitudeStr, false );

    String elevationStr = prefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name(), "0" );
    latLongElevation.setText( elevationStr );
    if ( "0".equals( elevationStr ) ) {
      updateSummary( latLongElevation, getString( R.string.sea_level ), ""  );
    }
    else {
      updateSummary( latLongElevation, elevationStr, getString( R.string.meters ) );
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
        updateLocaleSelector( area );
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


  private void updateLocaleSelector( String timeZoneArea ) {

    final String[] localeNames;
    if ( null != timeZoneArea ) {
      final List<String> locales = timeZoneAreasToLocales.get( timeZoneArea );
      localeNames = locales.toArray( new String[ locales.size() ] );
      timeZoneLocales.setEnabled( true );

      if ( !locales.contains( timeZoneLocales.getValue() ) ) {
        updateSummary( timeZoneLocales, "", getString( R.string.time_zone_locale_summary ) );
      }
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
