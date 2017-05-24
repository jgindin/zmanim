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

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous requests to display a notification
 *  to the user about an upcoming zman.
 */
public class DisplayNotificationService
  extends IntentService {
  
  private static final String                               ACTION_NOTIFICATION = "com.gindin.zmanim.android.service.action.NOTIFICATION";

  private static final String                               EXTRA_ZMAN_LOCATION = "com.gindin.zmanim.android.service.extra.ZMAN_LOCATION";
  private static final String                               EXTRA_ZMAN_TIME = "com.gindin.zmanim.android.service.extra.ZMAN_TIME";
  private static final String                               EXTRA_ZMAN_TYPE = "com.gindin.zmanim.android.service.extra.ZMAN_TYPE";
  private static final String                               EXTRA_ZMAN_ALERT_AT = "com.gindin.zmanim.android.service.extra.ZMAN_NOW";


  private ZmanCallback                                      zmanCallback;


  public DisplayNotificationService() {
    super( "DisplayNotificationService" );
  }



  public static Intent createIntentForService(
    Context         context,
    Zman            nextZman,
    ZmanimLocation  location,
    Calendar        alertAt
  ) {

    Intent intent = new Intent( context, DisplayNotificationService.class );
    intent.setAction( ACTION_NOTIFICATION );

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
  protected void onHandleIntent( Intent intent ) {
    if ( intent != null ) {

      if ( zmanCallback == null ) {
        zmanCallback = new PrefsBasedZmanCallback( this ) {
          @Override public void scheduleAlert(
            Zman zman,
            ZmanimLocation    location,
            Calendar          alertAt
          ) {
            // This implementation will NEVER schedule any alerts...;
          }
        };
      }


      final String action = intent.getAction();
      if ( ACTION_NOTIFICATION.equals( action ) ) {

        Zmanim.Type type = Zmanim.Type.valueOf( intent.getStringExtra( EXTRA_ZMAN_TYPE ) );
        Calendar time = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_TIME );
        Calendar alertAt = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_ALERT_AT );

        try {
          ZmanimLocation location = ZmanLocationJSON.fromJSON( intent.getStringExtra( EXTRA_ZMAN_LOCATION ) );
          handleNotificationAction( type, time, location, alertAt );
        }
        catch ( Exception e ) {
          // Something happened and we can't translate it back...
          LogUtils.logError( "DisplayNotificationService::onHandleIntent", "JSONException", e );
        }
      }
    }
  }


  /**
   * Handle notification action in the provided background thread with the provided
   * parameters.
   */
  @SuppressWarnings( "FeatureEnvy" )
  private void handleNotificationAction(
    final Zmanim.Type     zmanType,
    final Calendar        zmanTime,
    final ZmanimLocation  location,
    final Calendar        alertAt
  ) {

    // Ensure the user wants to see this notification!!!
    boolean shouldNotify = zmanCallback.shouldActuallyNotify( location, alertAt );

    // Mark this zman as having been notified. (even if we wind up not showing it...)
    zmanCallback.completed( alertAt, zmanType );

    // TODO: Re-enable this, and remove the same block of code from below...
//    if ( !shouldNotify ) {
//      return;
//    }


    // Make sure any previous notification is removed. It seems that this is normally not necessary,
    //  but sometimes I'm seeing that a new notification doesn't show up when the old one hasn't been
    //  dismissed...
    // Note that for now, on debug builds we're going to "stack up" the notifications. If this becomes
    //  interesting, we could add a preference for this...
    final int notificationID;
    if ( BuildConfig.DEBUG ) {
      notificationID = zmanType.ordinal();
    }
    else {
      notificationID = 1;
    }

    NotificationManager notificationManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
    notificationManager.cancel( notificationID );

    Intent openZmanim = new Intent( this, ZmanimActivity.class );

    ZmanFormatter zmanFormatter = new ZmanFormatter( zmanCallback.formatTimeWithSeconds() );
    String notificationText = getString( R.string.notification, zmanType.name, zmanFormatter.formatZmanTime( zmanType, zmanTime ) );

    if ( Build.BRAND.equalsIgnoreCase( "samsung" ) ) {

      LogUtils.logError( "DisplayNotificationService::handleNotificationAction", "handling", null,
        new Pair<String, String>( "zmanType", zmanType.name ),
        new Pair<String, String>( "zmanTime", new Date( zmanTime.getTimeInMillis() ).toString() ),
        new Pair<String, String>( "alert time", new Date( alertAt.getTimeInMillis() ).toString() ),
        new Pair<String, String>( "should notify", Boolean.toString( shouldNotify ) ),
        new Pair<String, String>( "message", notificationText )
      );
    }

    if ( !shouldNotify ) {
      return;
    }


    Notification notification = new NotificationCompat.Builder( this )
      .setContentTitle( getString( R.string.app_name ) )
      .setContentText( notificationText )
      .setSmallIcon( R.drawable.ic_stat_zman_notification )
      .setTicker( notificationText )
      .setAutoCancel( true )
      .setDefaults( Notification.DEFAULT_ALL )
      .setContentIntent( PendingIntent.getActivity( this, 0, openZmanim, 0 ) )
      .build();

    notificationManager.notify( notificationID, notification );
  }

}
