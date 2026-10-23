/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.service.ZmanimService;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.VisibleForTesting;

/**
 * Decides whether to ask the user to allow exact alarms, remembers that they were asked, and reschedules the alerts
 *  once exact alarms become allowed.
 *<p/>
 *
 * From Android 12 (API 31) on, an app may only set exact alarms while the user allows it to (the
 *  {@code SCHEDULE_EXACT_ALARM} special app access), and from Android 14 on a fresh install starts out without that
 *  access. Without it, {@link ZmanimService} can only set inexact alarms, which Android may deliver minutes late, and
 *  later still while the device is idle. An alert a few minutes before candle lighting is of little use once it is
 *  late, so the app explains this and offers the system page where the user allows it. The system never asks on the
 *  app's behalf, and the app asks at most once: the personal preferences screen offers the way back.
 *<p/>
 *
 * {@link AlarmManager#canScheduleExactAlarms()} is also true while the app is exempt from battery optimization, so
 *  such users are never asked.
 */
public final class ExactAlarmPermission {

  /**
   * Key, in the {@link ZmanimPrefs#PERSONAL} preferences, that is true once the user has answered the explanation of
   *  why the app wants to set exact alarms.
   */
  public static final String                                ASKED_KEY = "EXACT_ALARM_PERMISSION_ASKED";

  /** Schedules the upcoming alerts again. */
  interface Rescheduler {
    void reschedule( Context context );
  }

  /**
   * What {@link #rescheduleAlerts(Context)} does. The service's work queue cannot be inspected from a Robolectric
   *  test, and the activity queues other work for the same service, so tests replace this to see the request.
   */
  @VisibleForTesting
  static Rescheduler                                        rescheduler =
    context -> ZmanimService.enqueueWork( context, ZmanimService.Actions.SCHEDULE_ALARM, false );


  private ExactAlarmPermission() {
  }


  /**
   * True when the app should explain why its alerts need exact alarms and offer the system page that allows them: the
   *  platform restricts exact alarms, the user wants notifications, exact alarms are not allowed, and the user was
   *  never asked.
   */
  static boolean shouldAsk(
    int     sdkInt,
    boolean wantsNotifications,
    boolean exactAlarmsAllowed,
    boolean alreadyAsked
  ) {
    return ( sdkInt >= Build.VERSION_CODES.S ) && wantsNotifications && !exactAlarmsAllowed && !alreadyAsked;
  }


  /** Applies {@link #shouldAsk(int, boolean, boolean, boolean)} to the running device and the saved preferences. */
  public static boolean shouldAsk( Context context ) {

    final SharedPreferences prefs = personalPrefs( context );
    return shouldAsk(
      Build.VERSION.SDK_INT,
      wantsNotifications( context ),
      !isDenied( context ),
      prefs.getBoolean( ASKED_KEY, false )
    );
  }


  /** Records that the user has answered, so that they are not asked again. */
  public static void markAsked( Context context ) {
    personalPrefs( context ).edit().putBoolean( ASKED_KEY, true ).apply();
  }


  /** True when the user wants notifications, which is the only reason the app sets alarms for the zmanim. */
  public static boolean wantsNotifications( Context context ) {
    return personalPrefs( context ).getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true );
  }


  /**
   * True when the system will only let this app set inexact alarms. Never reported below Android 12, where every app
   *  may set exact alarms.
   */
  public static boolean isDenied( Context context ) {

    if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.S ) {
      return false;
    }

    final AlarmManager alarmManager = context.getSystemService( AlarmManager.class );
    return ( alarmManager != null ) && !alarmManager.canScheduleExactAlarms();
  }


  /** Opens the system page where the user allows or denies exact alarms for this app. */
  public static Intent exactAlarmSettings( Context context ) {
    return new Intent( Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse( "package:" + context.getPackageName() ) );
  }


  /**
   * Schedules the upcoming alerts again. Each alert's pending intent is identified by the zman and the day (see
   *  {@link ZmanimService}), so scheduling the same alert again replaces the queued inexact alarm with an exact one.
   */
  public static void rescheduleAlerts( Context context ) {
    rescheduler.reschedule( context );
  }


  private static SharedPreferences personalPrefs( Context context ) {
    return context.getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
  }

}  // End of ExactAlarmPermission class
