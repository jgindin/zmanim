/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.location.LastKnownLocationProvider;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.*;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Hanetz;
import com.gindin.zmanlib.zman.Shkia;
import com.gindin.zmanlib.zman.Zmanim;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Used from the main activity to check if any database, preferences, or other data needs to be upgraded.
 */
class AppUpgrader {

  private static final String                               LAST_KNOWN_CODE_VERSION = "lastKnownCodeVersion";

  public static void handle(
    ZmanimActivity              zmanimActivity,
    PersonalPrefsManager        personalPrefsManager,
    ZmanimSelectionPrefsManager zmanimSelectionPrefsManager
  ) {

    // Compare our version to the the last known version and see if anything needs to be upgraded.
    int thisCodeVersion = BuildConfig.VERSION_CODE;

    SharedPreferences prefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.VERSION.name(), Context.MODE_PRIVATE );
    int lastKnownCodeVersion = prefs.getInt( LAST_KNOWN_CODE_VERSION, -1 );

    if ( thisCodeVersion > lastKnownCodeVersion ) {
      try {

        int personalPrefsUpgradeId = personalPrefsManager.onHandleUpgrade( thisCodeVersion, lastKnownCodeVersion );
        int zmanPrefsUpgradeId = zmanimSelectionPrefsManager.onHandleUpgrade( thisCodeVersion, lastKnownCodeVersion );
        upgradeLocationPrefs( zmanimActivity, lastKnownCodeVersion );
        upgradeElevationPref( zmanimActivity, lastKnownCodeVersion );

        int thisUpgradeId = -1;
        
        //noinspection MagicNumber
        if ( lastKnownCodeVersion < 279 ) {
          thisUpgradeId = R.string.upgrade_from_279;
        }

        showUpgradeNotes( zmanimActivity, personalPrefsUpgradeId, zmanPrefsUpgradeId, thisUpgradeId );

      }
      catch ( Throwable e ) {
        //noinspection unchecked
        LogUtils.logError( "doUpgrade", "", e,
          new Pair<String, String>( "ver", Integer.toString( thisCodeVersion ) ),
          new Pair<String, String>( "old", Integer.toString( lastKnownCodeVersion ) )
        );
      }
    }

    final SharedPreferences.Editor editor = prefs.edit();
    editor.putInt( LAST_KNOWN_CODE_VERSION, thisCodeVersion );
    editor.apply();
  }


  @SuppressWarnings( "MagicNumber" )
  private static void upgradeElevationPref(
    ZmanimActivity  zmanimActivity,
    int             lastKnownCodeVersion
  ) {

    // In v320 we moved the Use Elevation property from the personal prefs into the location management prefs.
    if ( lastKnownCodeVersion < 320 ) {
      final SharedPreferences personalPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
      final SharedPreferences locationManagementPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

      final SharedPreferences.Editor editor = locationManagementPrefs.edit();
      editor.putBoolean( LocationProviders.DynamicProviderOptions.USE_ELEVATION.name(),
        personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false ) );
      editor.apply();
    }

    // In v335, we no longer use the Use Elevation setting. Instead, there are explicit zmanim for Hanetz & Shkia to
    //  account for elevation.
    if ( lastKnownCodeVersion < 335 ) {

      final SharedPreferences locationPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
      final boolean useElevation = locationPrefs.getBoolean( LocationProviders.DynamicProviderOptions.USE_ELEVATION.name(), false );
      if ( useElevation ) {
        final SharedPreferences zmanPrefs = zmanimActivity.getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
        final SharedPreferences.Editor editor = zmanPrefs.edit();
        editor.putString( Zmanim.Type.Hanetz.name , Hanetz.ZMAN_WITH_ELEVATION );
        editor.putString( Zmanim.Type.Shkia.name, Shkia.ZMAN_WITH_ELEVATION );
        editor.apply();
      }

      LocationToAddressConverter.wipeCacheOnStart = true;
    }
  }


  @SuppressWarnings( "FeatureEnvy" )
  private static void upgradeLocationPrefs(
    ZmanimActivity    activity,
    int               lastKnownCodeVersion
  ) {

    //noinspection MagicNumber
    if ( lastKnownCodeVersion > 230 ) {
      return;
    }

    Location lastKnownLocation;

    SharedPreferences locationPreferences = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    String lastFixProviderName = locationPreferences.getString( "lastFixProviderName", null );
    if ( null != lastFixProviderName ) {

      lastKnownLocation = new Location( lastFixProviderName );
      if ( locationPreferences.getBoolean( "lastFixHasAccuracy", false ) ) {
        lastKnownLocation.setAccuracy( locationPreferences.getFloat( "lastFixAccuracy", 0 ) );
      }

      if ( locationPreferences.getBoolean( "lastfixHasAltitude", false ) ) {
        lastKnownLocation.setAltitude( Double.parseDouble( locationPreferences.getString( "lastfixAltitude", "0" ) ) );
      }

      if ( locationPreferences.getBoolean( "lastfixHasBearing", false ) ) {
        lastKnownLocation.setBearing( locationPreferences.getFloat( "lastfixBearing", 0.0f ) );
      }

      lastKnownLocation.setLatitude( Double.parseDouble( locationPreferences.getString( "lastfixLatitude", "0" ) ) );
      lastKnownLocation.setLongitude( Double.parseDouble( locationPreferences.getString( "lastfixLongitude", "0" ) ) );

      if ( locationPreferences.getBoolean( "lastfixHasSpeed", false ) ) {
        lastKnownLocation.setSpeed( locationPreferences.getFloat( "lastfixSpeed", 0.0f ) );
      }

      lastKnownLocation.setTime( locationPreferences.getLong( "lastfixTime", 0 ) );

      ZmanimLocation location = ZmanLocationUtil.fromLocation( lastKnownLocation );

      OutputStream output = null;
      try {
        output = activity.openFileOutput( LastKnownLocationProvider.NAME, Context.MODE_PRIVATE );
        location.save( output );
      }
      catch ( IOException e ) {
        // Oh well, we'll lose the last known location, and we'll have to acquire the current location.
      }
      finally {
        if ( output != null ) {
          try {
            output.close();
          }
          catch ( IOException e ) {
            // too bad...
          }
        }
      }
    }

  }


  private static void showUpgradeNotes(
      final ZmanimActivity  zmanimActivity,
      final @StringRes int ... noteIds
  ) {

    if ( ( noteIds == null ) || ( noteIds.length == 0 ) ) {
      return;
    }

    boolean haveANote = false;
    for ( int noteId : noteIds ) {
      if ( noteId != -1 ) {
        haveANote = true;
        break;
      }
    }

    if ( !haveANote ) {
      return;
    }

    String header = zmanimActivity.getString( R.string.upgrade_header );
    final Snackbar snackbar = Snackbar
      .make( zmanimActivity.findViewById( android.R.id.content ), header, Snackbar.LENGTH_INDEFINITE );

    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick( final View v ) {

        StringBuilder message = new StringBuilder();
        for ( int noteId : noteIds ) {
          if ( noteId != -1 ) {
            message.append( zmanimActivity.getString( noteId ) ).append( "\n\n" );
          }
        }

        if ( message.length() == 0 ) {
          return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder( zmanimActivity );
        builder
          .setTitle( R.string.upgrade_header )
          .setMessage( message )
          .create()
          .show();
      }
    };


    snackbar.setAction( "View", listener );
    snackbar.show();
  }

}  // End of AppUpgrader class
