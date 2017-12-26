/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.XpPreferenceFragment;

/**
 * Activity for editing personal preferences.
 */
public class PersonalPreferences
  extends AppCompatActivity {

  public enum Prefs {
    SHOW_SECONDS,
    @Deprecated USE_ELEVATION,
    SHOW_NOTIFICATIONS,
    ENABLE_NOTIFICATIONS_ON_SHABBAT,
    USE_ALARM_CLOCK,
    NOTIFICATION_TONE
  }


  @Override
  protected void onCreate( @Nullable Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    if ( getSupportFragmentManager().findFragmentById( android.R.id.content ) == null ) {
      getSupportFragmentManager()
        .beginTransaction()
        .add( android.R.id.content, new PersonalPrefsFragment() )
        .commit();
    }
  }


  public static class PersonalPrefsFragment
    extends XpPreferenceFragment {

    @Override
    public void onCreatePreferences2(
      Bundle savedInstanceState,
      String rootKey
    ) {

      getPreferenceManager().setSharedPreferencesName( ZmanimPrefs.PERSONAL.name() );
      addPreferencesFromResource( R.xml.personal_prefs );

      // On older versions of Android, don't offer the Alarm Clock option...
      if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
        final Preference preference = findPreference( Prefs.USE_ALARM_CLOCK.name() );
        final String notifications = getString( R.string.notifications_category );

        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        final int preferenceCount = preferenceScreen.getPreferenceCount();
        for ( int i = 0; i < preferenceCount; i++ ) {
          final Preference pref = preferenceScreen.getPreference( i );
          if ( notifications.equals( pref.getTitle() ) ) {
            ((PreferenceCategory)pref).removePreference( preference );
            break;
          }
        }
      }
    }

  }


}  // End of PersonalPreferences class
