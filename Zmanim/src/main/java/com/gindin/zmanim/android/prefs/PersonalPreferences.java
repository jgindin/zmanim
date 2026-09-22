/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.service.DisplayNotificationService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.XpPreferenceFragment;

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

      // On newer versions of Android, we have to direct the user over to the application notification
      //  settings if they want to change the sound...
      if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {

        addPreferencesFromResource( R.xml.personal_prefs_oreo );

        // https://stackoverflow.com/questions/48853948/intent-to-open-the-notification-channel-settings-from-my-app
        // If we wanted to open the screen for all the notification settings, then we' use
        //  Settings.ACTION_APP_NOTIFICATION_SETTINGS for the intent action, and we would NOT include the
        //  Settings.EXTRA_CHANNEL_ID parameter.
        Intent intent = new Intent( Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS );
        intent.putExtra( Settings.EXTRA_APP_PACKAGE, getContext().getPackageName() );
        intent.putExtra( Settings.EXTRA_CHANNEL_ID, DisplayNotificationService.NOTIFICATION_CHANNEL_ID );

        final Preference preference = findPreference( Prefs.NOTIFICATION_TONE.name() );
        preference.setIntent( intent );
      }
      else {

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

  }


}  // End of PersonalPreferences class
