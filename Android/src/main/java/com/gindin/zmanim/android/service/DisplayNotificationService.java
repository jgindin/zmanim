package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.ZmanNotification;
import com.gindin.zmanlib.location.ZmanLocationJSON;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import org.json.JSONException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

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
  private static final String                               EXTRA_ZMAN_NOW = "com.gindin.zmanim.android.service.extra.ZMAN_NOW";


  private ZmanCallback                                      zmanCallback;


  public DisplayNotificationService() {
    super( "DisplayNotificationService" );
  }



  public static Intent createIntentForService(
    Context         context,
    Zman            nextZman,
    ZmanimLocation  location,
    Calendar        now
  ) {

    Intent intent = new Intent( context, DisplayNotificationService.class );
    intent.setAction( ACTION_NOTIFICATION );

    // NOTE: I tried making the Zman serializable, but there are things there that I can't control.
    // NOTE: I tried storing the Zmanim.Type, but even though it's an enum and should work just fine,
    //  when we tried to pull it out, it was always null.
    intent.putExtra( EXTRA_ZMAN_TYPE, nextZman.type.name() );
    intent.putExtra( EXTRA_ZMAN_TIME, nextZman.getTime() );
    intent.putExtra( EXTRA_ZMAN_NOW, now );
    intent.putExtra( EXTRA_ZMAN_LOCATION, ZmanLocationJSON.toJSON( location ) );

    return intent;
  }


  @Override
  protected void onHandleIntent( Intent intent ) {
    if ( intent != null ) {

      if ( zmanCallback == null ) {
        zmanCallback = new PrefsBasedZmanCallback( this ) {
          @Override public void scheduleAlert(
            ZmanNotification zmanNotification,
            ZmanimLocation    location,
            Calendar          now
          ) {
            // This implementation will NEVER schedule any alerts...;
          }
        };
      }


      final String action = intent.getAction();
      if ( ACTION_NOTIFICATION.equals( action ) ) {

        Zmanim.Type type = Zmanim.Type.valueOf( intent.getStringExtra( EXTRA_ZMAN_TYPE ) );
        Calendar time = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_TIME );
        Calendar now = (Calendar)intent.getSerializableExtra( EXTRA_ZMAN_NOW );

        try {
          ZmanimLocation location = ZmanLocationJSON.fromJSON( intent.getStringExtra( EXTRA_ZMAN_LOCATION ) );
          handleNotificationAction( type, time, location, now );
        }
        catch ( JSONException e ) {
          // Something happened and we can't translate it back...
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
    final Calendar        now
  ) {

    // Ensure the user wants to see this notification!!!
    boolean shouldNotify = zmanCallback.shouldActuallyNotify( location );

    // Mark this zman as having been notified. (even if we wind up not showing it...)
    zmanCallback.completed( now, zmanType );

    if ( !shouldNotify ) {
      return;
    }

    ZmanFormatter zmanFormatter = new ZmanFormatter( zmanCallback.formatTimeWithSeconds() );

    // Make sure any previous notification is removed. It seems that this is normally not necessary,
    //  but sometimes I'm seeing that a new notification doesn't show up when the old one hasn't been
    //  dismissed...
    NotificationManager notificationManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
    notificationManager.cancel( zmanType.ordinal() );

    Intent openZmanim = new Intent( this, ZmanimActivity.class );

    String notificationText = getString( R.string.notification, zmanType, zmanFormatter.formatZmanTime( zmanType, zmanTime ) );

    Notification notification = new NotificationCompat.Builder( this )
      .setContentTitle( getString( R.string.app_name ) )
      .setContentText( notificationText )
      .setSmallIcon( R.drawable.ic_stat_zman_notification )
      .setTicker( notificationText )
      .setAutoCancel( true )
      .setDefaults( Notification.DEFAULT_ALL )
      .setContentIntent( PendingIntent.getActivity( this, 0, openZmanim, 0 ) )
      .build();

    notificationManager.notify( zmanType.ordinal(), notification );
  }

}
