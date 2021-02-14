/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.location.ZmanLocationJSON;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;


/**
 * An {@link IntentService} subclass for handling asynchronous requests to display a notification
 *  to the user about an upcoming zman.
 */
public class DisplayNotificationService
  extends BroadcastReceiver {
  
  public static final String                                DISPLAY_NOTIFICATION = "com.gindin.zmanim.android.service.action.NOTIFICATION";

  /**
   * If any change is needed to the channel, you have to create a new channel, as once a channel is created it cannot
   *  be changed.
   *
   * NOTE: Deleting a channel IS VISIBLE TO THE USER. If you look at the Notifications settings for the app, you'll
   *  see something like, "1 category deleted". :-(
   */
  public static final String                                NOTIFICATION_CHANNEL_ID = "zmanim-notifications_3";

  private static final String                               EXTRA_ZMAN_LOCATION = "com.gindin.zmanim.android.service.extra.ZMAN_LOCATION";
  private static final String                               EXTRA_ZMAN_TIME = "com.gindin.zmanim.android.service.extra.ZMAN_TIME";
  private static final String                               EXTRA_ZMAN_TYPE = "com.gindin.zmanim.android.service.extra.ZMAN_TYPE";
  private static final String                               EXTRA_ZMAN_ALERT_AT = "com.gindin.zmanim.android.service.extra.ZMAN_NOW";

  private ZmanCallback                                      zmanCallback;




  public static Intent createIntentForService(
    Context         context,
    Zman            nextZman,
    ZmanimLocation  location,
    Calendar        alertAt
  ) {

    Intent intent = new Intent( context, DisplayNotificationService.class );
    intent.setAction( DISPLAY_NOTIFICATION );

    // NOTE: I tried making the Zman serializable, but there are things there that I can't control.
    // NOTE: I tried storing the Zmanim.Type, but even though it's an enum and should work just fine,
    //  when we tried to pull it out, it was always null.
    intent.putExtra( EXTRA_ZMAN_TYPE, nextZman.type.name() );
    intent.putExtra( EXTRA_ZMAN_TIME, nextZman.getTime() );
    intent.putExtra( EXTRA_ZMAN_ALERT_AT, alertAt );
    intent.putExtra( EXTRA_ZMAN_LOCATION, ZmanLocationJSON.toJSON( location ) );

    return intent;
  }


  @Override
  public void onReceive(
    Context context,
    Intent  intent
  ) {

    if ( zmanCallback == null ) {
      zmanCallback = new PrefsBasedZmanCallback( context ) {
        @Override public void scheduleAlert(
          Zman              zman,
          ZmanimLocation    location,
          Calendar          alertAt
        ) {
          // This implementation will NEVER schedule any alerts...;
        }
      };
    }


    final String action = intent.getAction();
    if ( DISPLAY_NOTIFICATION.equals( action ) ) {

      Zmanim.Type type = Zmanim.Type.valueOf( intent.getStringExtra( EXTRA_ZMAN_TYPE ) );
      Calendar time = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_TIME );
      Calendar alertAt = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_ALERT_AT );

      try {
        ZmanimLocation location = ZmanLocationJSON.fromJSON( intent.getStringExtra( EXTRA_ZMAN_LOCATION ) );
        handleNotificationAction( context, type, time, location, alertAt );
      }
      catch ( Exception e ) {
        // Something happened and we can't translate it back...
        LogUtils.logError( "DisplayNotificationService::onHandleWork", "JSONException", e );
      }
    }
  }


  public static void createNotificationChannel(
    Context       context,
    ZmanCallback  zmanCallback
  ) {

    // Notification channel should only be created for devices running Android 26
    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
      NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID, "Zmanim Notifications", NotificationManager.IMPORTANCE_HIGH );

      // Boolean value to set if lights are enabled for Notifications from this Channel
      notificationChannel.enableLights( true );

      // Boolean value to set if vibration is enabled for Notifications from this Channel
      notificationChannel.enableVibration( true );

      // Sets whether notifications from these Channel should be visible on Lockscreen or not
      notificationChannel.setLockscreenVisibility( Notification.VISIBILITY_PUBLIC );

      // Older versions of Zmanim (where we had a somewhat simpler time with notifications) the user could just set
      //  the sound. So, if they did, preserve that...
      Uri toneUri = null;
      String tone = zmanCallback.getNotificationTone();
      if ( tone != null ) {
        toneUri = Uri.parse( tone );
      }

      AudioAttributes audioAttributes = new AudioAttributes.Builder()
        .setContentType( AudioAttributes.CONTENT_TYPE_UNKNOWN )
        .setUsage( AudioAttributes.USAGE_NOTIFICATION )
        .build();
      notificationChannel.setSound( toneUri, audioAttributes );

      NotificationManager notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
      notificationManager.createNotificationChannel( notificationChannel );

      // Removing old channels...
      notificationManager.deleteNotificationChannel( "zmanim-notifications" );
      notificationManager.deleteNotificationChannel( "zmanim-notifications_2" );
    }
  }


  /**
   * Handle notification action in the provided background thread with the provided
   * parameters.
   */
  @SuppressWarnings( "FeatureEnvy" )
  private void handleNotificationAction(
    final Context         context,
    final Zmanim.Type     zmanType,
    final Calendar        zmanTime,
    final ZmanimLocation  location,
    final Calendar        alertAt
  ) {

    // Ensure the user wants to see this notification!!!
    boolean shouldNotify = zmanCallback.shouldActuallyNotify( location, alertAt );

    // Mark this zman as having been notified. (even if we wind up not showing it...)
    zmanCallback.completed( alertAt, zmanType );

    // We use a different notification ID for each notification. This means that as they occur, they will
    //  "stack".
    final int notificationID = zmanType.ordinal();


    Intent openZmanim = new Intent( context, ZmanimActivity.class );

    ZmanFormatter zmanFormatter = new ZmanFormatter( () -> zmanCallback.formatTimeWithSeconds() );
    String notificationText = context.getString( R.string.notification, zmanType.name, zmanFormatter.formatZmanTime( zmanType, zmanTime ) );

    // Keep this...it's very useful at times...
    if ( BuildConfig.DEBUG ) {

      LogUtils.logError( "DisplayNotificationService::handleNotificationAction", "handling", null,
        new Pair<>( "zman type", zmanType.name ),
        new Pair<>( "zman time", new Date( zmanTime.getTimeInMillis() ).toString() ),
        new Pair<>( "alert time", new Date( alertAt.getTimeInMillis() ).toString() ),
        new Pair<>( "should notify", Boolean.toString( shouldNotify ) ),
        new Pair<>( "notify reason", zmanCallback.actuallyNotifyReasoning( location, alertAt ) ),
        new Pair<>( "message", notificationText ),
        new Pair<>( "notify location", location.toString() )
      );
    }

    if ( !shouldNotify ) {
      return;
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder( context, NOTIFICATION_CHANNEL_ID );
    builder
      .setAutoCancel( true )
      .setContentTitle( context.getString( R.string.app_name ) )
      .setContentText( notificationText )
      .setSubText( location.getLocationName() )
      .setSmallIcon( R.drawable.ic_stat_zman_notification )
      .setTicker( notificationText )
      .setCategory( NotificationCompat.CATEGORY_REMINDER )
      .setPriority( NotificationCompat.PRIORITY_HIGH )
      .setVisibility( NotificationCompat.VISIBILITY_PUBLIC )
      .setContentIntent( PendingIntent.getActivity( context, 0, openZmanim, 0 ) );

    // TODO: Group the notifications. To support API 24 and lower, we have to create our own summary.
    //  See https://developer.android.com/training/notify-user/group.

    // Setting a sound only works on pre-Oreo builds. Oreo and after we have to deal with channels...
    if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.O ) {

      String tone = zmanCallback.getNotificationTone();
      if ( tone != null ) {
        Uri toneUri = Uri.parse( tone );
        builder.setSound( toneUri );
        builder.setDefaults( ~Notification.DEFAULT_SOUND );
      }
    }

    NotificationManager notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
    notificationManager.notify( notificationID, builder.build() );
  }

}
