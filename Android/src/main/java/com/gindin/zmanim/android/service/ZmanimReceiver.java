package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.BuildConfig;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receives broadcast notifications, such as after the device has booted so that we can schedule our next alarm.
 */
public class ZmanimReceiver
  extends WakefulBroadcastReceiver {

  private static final long                                 DEBUG_ALARM_INTERVAL = 2 * 60 * 1000;



  @Override
  public void onReceive(
    Context context,
    Intent  intent
  ) {

    Intent startZmanimServiceIntent = ZmanimService.createIntentForSchedulingAlarms( context );

    String action = intent.getAction();
    if ( Intent.ACTION_BOOT_COMPLETED.equals( action ) ) {
      startZmanimServiceIntent.putExtra( ZmanimService.ActionParams.FORCE_SCHEDULE.name(), true );
    }

    startWakefulService( context, startZmanimServiceIntent );
    scheduleRepeatingChecks( context );
  }


  /**
   * Setup a repeating alarm that will allow the proper scheduling of the notifications for upcoming zmanim.
   */
  public static void scheduleRepeatingChecks( Context context ) {

    // Broadcast back to ourselves in ~30 minutes, which will then start the ZmanimService
    //  as a wakeful service, and it will cause the alerts to be (re)scheduled.
    Intent alarmIntent = new Intent( context, ZmanimReceiver.class );
    PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 0, alarmIntent, 0 );

    // Make sure to reschedule alarms periodically...
    final long alarmInterval;
    if ( BuildConfig.DEBUG ) {
      alarmInterval = DEBUG_ALARM_INTERVAL;
    }
    else {
      alarmInterval = AlarmManager.INTERVAL_HALF_HOUR;
    }


    AlarmManager alarmManager = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    alarmManager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP,
      SystemClock.elapsedRealtime() + alarmInterval,
      alarmInterval,
      pendingIntent
    );
  }

}
