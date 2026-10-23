/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.prefs;

import com.gindin.util.EdgeToEdgeUtil;
import com.gindin.zmanim.android.ExactAlarmPermission;
import com.gindin.zmanim.android.NotificationPermission;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.service.DisplayNotificationService;

import android.app.AlertDialog;
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
    NOTIFICATION_TONE,

    /** Not a stored value: a row that only appears while the system blocks the notifications the user wants. */
    NOTIFICATIONS_BLOCKED,

    /**
     * Not a stored value: a row that only appears while the user wants notifications but the system lets the app set
     *  only inexact alarms, so the alerts may be late.
     */
    EXACT_ALARMS_DENIED
  }


  @Override
  protected void onCreate( @Nullable Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    EdgeToEdgeUtil.padForActionBarOverlay( this, findViewById( android.R.id.content ) );

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

        final Preference blocked = findPreference( Prefs.NOTIFICATIONS_BLOCKED.name() );
        blocked.setIntent( NotificationPermission.appNotificationSettings( getContext() ) );

        final Preference exactAlarmsDenied = findPreference( Prefs.EXACT_ALARMS_DENIED.name() );
        exactAlarmsDenied.setIntent( ExactAlarmPermission.exactAlarmSettings( getContext() ) );

        findPreference( Prefs.SHOW_NOTIFICATIONS.name() ).setOnPreferenceChangeListener( ( pref, newValue ) -> {

          final boolean showNotifications = Boolean.TRUE.equals( newValue );
          updateBlockedNotice( showNotifications );

          // One explanation at a time: blocked notifications matter more than late ones, and the row for the other
          //  stays visible.
          if ( showNotifications && NotificationPermission.isBlocked( getContext() ) ) {
            explainBlockedNotifications();
          }
          else if ( showNotifications && ExactAlarmPermission.isDenied( getContext() ) ) {
            // Notifications are not blocked here, so this is the one problem left.
            explainDeniedExactAlarms();
          }
          return true;
        } );
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


    @Override
    public void onResume() {
      super.onResume();

      // Coming back from the system settings lands here, so the notice reflects whatever the user just chose there.
      updateBlockedNotice(
        getPreferenceManager().getSharedPreferences().getBoolean( Prefs.SHOW_NOTIFICATIONS.name(), true )
      );
    }


    /**
     * Shows the rows that lead to the system settings only while the user wants notifications that are blocked, or
     *  that are allowed but can only be alerted about with inexact (possibly late) alarms.
     */
    private void updateBlockedNotice( boolean showNotifications ) {

      final Preference blocked = findPreference( Prefs.NOTIFICATIONS_BLOCKED.name() );
      if ( blocked != null ) {
        blocked.setVisible( showNotifications && NotificationPermission.isBlocked( getContext() ) );
      }

      final Preference exactAlarmsDenied = findPreference( Prefs.EXACT_ALARMS_DENIED.name() );
      if ( exactAlarmsDenied != null ) {
        // While notifications are blocked, the row above says so; late alerts only matter once they can appear.
        exactAlarmsDenied.setVisible( showNotifications && !NotificationPermission.isBlocked( getContext() )
          && ExactAlarmPermission.isDenied( getContext() ) );
      }
    }


    private void explainBlockedNotifications() {

      new AlertDialog.Builder( getContext() )
        .setTitle( R.string.notifications_blocked_pref_title )
        .setMessage( R.string.notifications_blocked_message )
        .setPositiveButton( R.string.open_settings,
          ( dialog, which ) -> startActivity( NotificationPermission.appNotificationSettings( getContext() ) )
        )
        .setNegativeButton( R.string.no_thanks, null )
        .show();
    }


    private void explainDeniedExactAlarms() {

      new AlertDialog.Builder( getContext() )
        .setTitle( R.string.exact_alarms_denied_pref_title )
        .setMessage( R.string.exact_alarms_denied_message )
        .setPositiveButton( R.string.open_settings,
          ( dialog, which ) -> startActivity( ExactAlarmPermission.exactAlarmSettings( getContext() ) )
        )
        .setNegativeButton( R.string.no_thanks, null )
        .show();
    }

  }


}  // End of PersonalPreferences class
