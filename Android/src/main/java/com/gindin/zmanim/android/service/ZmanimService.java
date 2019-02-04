package com.gindin.zmanim.android.service;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.*;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zman;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import java.util.Calendar;
import java.util.Date;

/**
 * This is the service we use to schedule notifications. In order to do that, however, the service has to have all
 *  of the location determination code.
 *
 * So, this service is used in two different ways:
 *  <ol>
 *    <li>When the user invokes the Zmanim application, the main activity will get a hold of this service in order to
 *          determine the user's current location.
 *    <li>When we schedule an alarm, this service gets invoked so that we can display the notification and then schedule
 *          the next alarm.
 *  </ol>
 */
public class ZmanimService
  extends JobIntentService {


  public enum Actions {
    /** Request to find the current location. */
    FIND_LOCATION,

    /** Ask the service to schedule the next alarm. May optionally have a boolean extra to force the alarm scheduling. */
    SCHEDULE_ALARM,

    /** A location request has (finally) come through. */
    LOCATION_ACQUIRED,

    /** An error occurred when trying to get the location. */
    LOCATION_NOT_ACQUIRED,

    PLACE_ACQUIRED
  }


  public enum ActionParams {

    /** Can be specified with the SCHEDULE_ALARM action to indicate the alarm should be scheduled no matter what. */
    FORCE_SCHEDULE,

    /** Can be specified with LOCATION_ACQUIRED */
    LOCATION,

    /** Can be specified with the LOCATION_NOT_ACQUIRED */
    ERROR
  }


  public static final EventBus                              ZMANIM_EVENT_BUS = new EventBus();

  private static final int                                  JOB_ID = 5170;

  private EventBus                                          eventBus = ZMANIM_EVENT_BUS;

  // TODO: Inject this
  private ZmanimLocationManager                             locationManager;

  private LocationAcquiredEvent.Handler                     locationAcquiredHandler;

  private ZmanNotificationScheduler                         zmanNotificationScheduler;

  private LocationToAddressConverter                        locationToAddressConverter;



  public static void enqueueWork(
    Context context,
    Actions action
  ) {

    enqueueWork( context, action, false );
  }


  public static void enqueueWork(
    Context context,
    Actions action,
    boolean force
  ) {

    Intent zmanimService = new Intent( context, ZmanimService.class );
    zmanimService.setAction( action.name() );

    if ( force ) {
      zmanimService.putExtra( ActionParams.FORCE_SCHEDULE.name(), true );
    }

    enqueueWork( context, ZmanimService.class, JOB_ID, zmanimService );
  }


  public static void enqueueWork(
    Context         context,
    ZmanimLocation  location
  ) {

    Intent zmanimService = new Intent( context, ZmanimService.class );

    if ( location.isResolved() ) {
      zmanimService.setAction( Actions.LOCATION_ACQUIRED.name() );
    }
    else {
      zmanimService.setAction( Actions.PLACE_ACQUIRED.name() );
    }

    zmanimService.putExtra( ActionParams.LOCATION.name(), ZmanLocationUtil.toBundle( location ) );

    enqueueWork( context, ZmanimService.class, JOB_ID, zmanimService );
  }


  public static void enqueueWork(
    Context       context,
    LocationError error
  ) {

    Intent zmanimService = new Intent( context, ZmanimService.class );
    zmanimService.setAction( Actions.LOCATION_NOT_ACQUIRED.name() );
    zmanimService.putExtra( ActionParams.ERROR.name(), error.toBundle() );

    enqueueWork( context, ZmanimService.class, JOB_ID, zmanimService );
  }



  public void onCreate() {
    super.onCreate();

    final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler( exceptionHandler );


    final SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
    final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

    if ( locationManager == null ) {
      locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs );
    }

    if ( locationToAddressConverter == null ) {
      locationToAddressConverter = new LocationToAddressConverter( this, eventBus, new Geocoder( this ) );
    }

    if ( zmanNotificationScheduler == null ) {
      ZmanCallback zmanCallback = new PrefsBasedZmanCallback( this ) {
        @Override public void scheduleAlert(
          Zman              zman,
          ZmanimLocation    location,
          Calendar          alertAt
        ) {
          scheduleUserAlert( zman, location, alertAt );
        }


        @Override
        public void log(
          String                  where,
          String                  message,
          Throwable               error,
          @Nullable Pair<String, String>... moreInfo
        ) {

          if ( BuildConfig.DEBUG ) {
            LogUtils.logError( where, message, error, moreInfo );
          }
        }
      };

      zmanNotificationScheduler = new ZmanNotificationScheduler( zmanCallback );

      DisplayNotificationService.createNotificationChannel( this, zmanCallback );
    }


    if ( locationAcquiredHandler == null ) {
      locationAcquiredHandler = new LocationAcquiredHandler();
      eventBus.addHandler( locationAcquiredHandler );
    }
  }


  @Override
  public void onDestroy() {

    // Might be null if we get stopped before we're fully started...
    if ( null != locationManager ) {
      locationManager.onStop();
      locationManager = null;
    }

    if ( locationToAddressConverter != null ) {
      locationToAddressConverter.onDestroy();
      locationToAddressConverter = null;
    }

    if ( locationAcquiredHandler != null ) {
      eventBus.removeHandler( locationAcquiredHandler );
      locationAcquiredHandler = null;
    }

    super.onDestroy();
  }


  @Override
  protected void onHandleWork( @NonNull Intent intent ) {

    // If we don't do this, then when we ask Android's location manager for a location update, it'll throw an exception
    //  'Can't create handler inside thread Thread[AsyncTask #1,5,main] that has not called Looper.prepare()'
    if ( Looper.myLooper() == null ) {
      Looper.prepare();
    }

    boolean force = intent.getBooleanExtra( ZmanimService.ActionParams.FORCE_SCHEDULE.name(), false );


    Actions action;
    try {
      action = Actions.valueOf( intent.getAction() );
    }
    catch ( Exception e ) {
      //Ignore this request...
      return;
    }

    switch ( action ) {
      case FIND_LOCATION:
        findCurrentLocation( force );
        break;

      case LOCATION_ACQUIRED:
        ZmanimLocation location = ZmanLocationUtil.fromBundle( intent.getBundleExtra( ActionParams.LOCATION.name() ) );
        eventBus.fire( new LocationAcquiredEvent( location ) );
        break;

      case LOCATION_NOT_ACQUIRED:
        eventBus.fire( new LocationAcquiredEvent( LocationError.fromBundle( intent.getBundleExtra( ActionParams.ERROR.name() ) ) ) );
        break;

      case PLACE_ACQUIRED:
        location = ZmanLocationUtil.fromBundle( intent.getBundleExtra( ActionParams.LOCATION.name() ) );
        eventBus.fire( new PlaceAcquiredEvent( location ) );
        break;

      case SCHEDULE_ALARM:
        findCurrentLocation( force );
        break;
    }

  }


  private void findCurrentLocation( boolean force ) {

    // We have to figure out where we are in order to see when the next alarm is.
    final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage;
    if ( force ) {
      cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.NEVER;
    }
    else {
      cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.PREFER;
    }

    eventBus.fire( new RequestLocationUpdateEvent( cachedLocationUsage ) );
  }


  @SuppressWarnings( "AutoUnboxing" )
  private void handleLocation(
    final ZmanimLocation  location
  ) {

    if ( locationAcquiredHandler == null ) {
      // This is NOT supposed to happen. Send some data to try and diagnose.
      //  UPDATE: Yet, now all of a sudden, I am seeing it! For me, this is happening after I go into the emulator
      //    settings and change the time to test some alerting functionality...
      return;
    }
    else {

      eventBus.removeHandler( locationAcquiredHandler );
      locationAcquiredHandler = null;

      if ( location.isInvalid ) {
        // Generally, this means that we don't have permission to acquire the location. This can happen
        //  after the initial installation, before the user has had a chance to respond to the prompt.
        return;
      }
    }


    Calendar now = Calendar.getInstance();
    now.setTimeZone( location.getTimeZone() );
    zmanNotificationScheduler.run( location, now );
  }


  private void scheduleUserAlert(
    final Zman              zman,
    final ZmanimLocation    location,
    final Calendar          alertAt
  ) {

    // This rather convoluted mechanism of identifying the pending intent is necessary so that we don't
    //  wind up replacing an intent just because we wind up scheduling a second alarm for the same
    //  zman type on a different day...
    //noinspection MagicNumber
    int intentId = ( ( (byte)alertAt.get( Calendar.DAY_OF_MONTH ) << 8 ) | ( zman.type.ordinal() & 0xFF ) );

    PendingIntent pendingIntent = PendingIntent.getBroadcast( this, intentId,

      DisplayNotificationService.createIntentForService( this, zman, location, alertAt ),

      // Why no flags here? http://stackoverflow.com/a/39926854/17941
       //    "Never use FLAG_CANCEL_CURRENT with PendingIntents that you use when setting alarms. If you want to
       //    reschedule the alarm for a different time you don't need any flag at all; just create a duplicate
       //    PendingIntent with flags of zero and then use it to set() an alarm: this will implicitly cancel the
       //    existing alarm and then set it for the newly-specified time."
      0
    );

    long alertTime = alertAt.getTimeInMillis();
    AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );
    if ( alarmManager == null ) {
      LogUtils.logError( "scheuduleUserAlert", "failed to get alarm manager", null );
      return;
    }

    SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
    boolean useAlarmClock = personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ALARM_CLOCK.name(), false );

    if ( Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && useAlarmClock ) {
      Intent openZmanim = new Intent( this, ZmanimActivity.class );

      AlarmManager.AlarmClockInfo  alarmClockInfo = new AlarmManager.AlarmClockInfo( alertTime,
        PendingIntent.getActivity( this, 0, openZmanim, 0 ) );
      alarmManager.setAlarmClock( alarmClockInfo, pendingIntent );
    }
    else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
      alarmManager.setExactAndAllowWhileIdle( AlarmManager.RTC_WAKEUP, alertTime, pendingIntent );
    }
    else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
      alarmManager.setExact( AlarmManager.RTC_WAKEUP, alertTime, pendingIntent );
    }
    else {
      alarmManager.set( AlarmManager.RTC_WAKEUP, alertTime, pendingIntent );
    }


    // Leave this here ... it comes in handy...
    if ( BuildConfig.DEBUG ) {
      //noinspection unchecked
      LogUtils.logError( "scheduleUserAlert", "scheduling alert", null,
        new Pair<>( "alert time", new Date( alertTime ).toString() ),
        new Pair<>( "zman type", zman.type.name ),
        new Pair<>( "zman", zman.name ),
        new Pair<>( "sched. location", location.toString() ),
        new Pair<>( "intent id", Integer.toString( intentId ) )
      );
    }

  }


  private class LocationAcquiredHandler
    extends LocationAcquiredEvent.Handler {


    @Override
    public void onLocationAcquired(
      ZmanimLocation location
    ) {
      handleLocation( location );
    }


    @Override
    public void onError(
      @NotNull LocationError  error
    ) {
    }
  }


}
