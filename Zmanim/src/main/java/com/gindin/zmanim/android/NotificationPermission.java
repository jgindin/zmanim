/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

/**
 * Decides whether to ask the user for permission to post notifications, and remembers that they were asked.
 *<p/>
 *
 * From Android 13 (API 33) on, an app may only post notifications once the user grants
 *  {@code POST_NOTIFICATIONS}, and the system never prompts for it on the app's behalf. The app
 *  asks at most once: whatever the user answers, the choice is theirs, and the personal preferences screen offers the
 *  way back through the system settings.
 */
public final class NotificationPermission {

  /**
   * Key, in the {@link ZmanimPrefs#PERSONAL} preferences, that is true once the user has answered the explanation of
   *  why the app wants to post notifications.
   */
  public static final String                                ASKED_KEY = "NOTIFICATION_PERMISSION_ASKED";


  private NotificationPermission() {
  }


  /**
   * True when the app should explain why it wants to post notifications and then ask for the permission: the platform
   *  requires the permission, the user wants notifications, it has not been granted, and the user was never asked.
   */
  static boolean shouldAsk(
    int     sdkInt,
    boolean wantsNotifications,
    boolean granted,
    boolean alreadyAsked
  ) {
    return ( sdkInt >= Build.VERSION_CODES.TIRAMISU ) && wantsNotifications && !granted && !alreadyAsked;
  }


  /** Applies {@link #shouldAsk(int, boolean, boolean, boolean)} to the running device and the saved preferences. */
  public static boolean shouldAsk( Context context ) {

    final SharedPreferences prefs = personalPrefs( context );
    return shouldAsk(
      Build.VERSION.SDK_INT,
      prefs.getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true ),
      ContextCompat.checkSelfPermission( context, Manifest.permission.POST_NOTIFICATIONS ) == PackageManager.PERMISSION_GRANTED,
      prefs.getBoolean( ASKED_KEY, false )
    );
  }


  /** Records that the user has answered, so that they are not asked again. */
  public static void markAsked( Context context ) {
    personalPrefs( context ).edit().putBoolean( ASKED_KEY, true ).apply();
  }


  /**
   * True when the system will not show this app's notifications. Only reported from Android 13 on, where a fresh
   *  install starts out blocked until the user grants the permission.
   */
  public static boolean isBlocked( Context context ) {
    return ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
      && !NotificationManagerCompat.from( context ).areNotificationsEnabled();
  }


  /** Opens the system screen where the user allows or blocks this app's notifications. */
  public static Intent appNotificationSettings( Context context ) {
    return new Intent( Settings.ACTION_APP_NOTIFICATION_SETTINGS )
      .putExtra( Settings.EXTRA_APP_PACKAGE, context.getPackageName() );
  }


  private static SharedPreferences personalPrefs( Context context ) {
    return context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
  }

}  // End of NotificationPermission class
