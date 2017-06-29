package com.gindin.zmanim.android.service;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.ZmanNotificationScheduler;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;

import org.jetbrains.annotations.NotNull;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage;
import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.LocationDeterminationOverride;

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
  extends Service {


  public enum Actions {
    /** Request the service to start for the Zmanim Activity. */
    ACTIVITY_STARTED,

    /** Notify the service that the Zmanim Activity is done. */
    ACTIVITY_STOPPED,

    /** Ask the service to schedule the next alarm. May optionally have a boolean extra to force the alarm scheduling. */
    SCHEDULE_ALARM
  }


  public enum ActionParams {

    /** Can be specified with the SCHEDULE_ALARM action to indicate the alarm should be scheduled no matter what. */
    FORCE_SCHEDULE,
  }


  public static final EventBus                              ZMANIM_EVENT_BUS = new EventBus();

  private EventBus                                          eventBus = ZMANIM_EVENT_BUS;

  // TODO: Inject this
  private ZmanimLocationManager                             locationManager;

  private LocationAcquiredEvent.Handler                     locationAcquiredHandler;

  /**
   * When we get a command to start coming from the Zmanim activity, then we know we're providing services for
   *  the user. In that case, we must make sure not to shut down until we receive an explicit command to do so.
   */
  private boolean                                           servicingUser = false;

  /**
   * If we're invoked from some other intent, keep track of it. This way we can (a) not do something if we're already
   *  handling for an intent; and (b) cleanup if something goes really wrong.
   * In addition, this serves as a flag that we need to shut ourselves down after the notification has been
   *  displayed and the next alarm scheduled. Unless, that is, we're also currently servicing a user.
   */
  private Intent                                            handlingAlarmsFor;

  private ZmanNotificationScheduler                         zmanNotificationScheduler;



  /**
   * Creates an intent that can be used to have this service schedule alarms.
   */
  public static Intent createIntentForSchedulingAlarms(
    Context context
  ) {

    Intent startZmanimServiceIntent = new Intent( context, ZmanimService.class );
    startZmanimServiceIntent.setAction( ZmanimService.Actions.SCHEDULE_ALARM.name() );

    return startZmanimServiceIntent;
  }


  public void onCreate() {
    super.onCreate();

    final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(
      new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(
          final Thread    thread,
          final Throwable throwable
        ) {

          servicingUser = false;
          releaseWakefulIntent();

          exceptionHandler.uncaughtException( thread, throwable );
        }
      }
    );

    final SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
    final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

    LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
    locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs, systemLocationManager );

    ZmanCallback zmanCallback = new PrefsBasedZmanCallback( this ) {
      @Override public void scheduleAlert(
        Zman              zman,
        ZmanimLocation    location,
        Calendar          alertAt
      ) {
        scheduleUserAlert( zman, location, alertAt );
      }
    };
    zmanNotificationScheduler = new ZmanNotificationScheduler( zmanCallback );
  }


  @Override
  public IBinder onBind( Intent intent ) {
    return null;
  }


  @Override
  public int onStartCommand(
    Intent  intent,
    int     flags,
    int     startId
  ) {

    // NOTE: It helps to read the JavaDocs -- it clearly states that:
    //    [the intent parameter] may be null if the service is being restarted after
    //    its process has gone away...
    String action = ( intent == null ? "" : intent.getAction() );

    if ( Actions.ACTIVITY_STARTED.name().equals( action ) ) {
      servicingUser = true;

      findCurrentLocation();

      eventBus.fire( new ServiceStartedEvent() );
    }
    else if ( Actions.ACTIVITY_STOPPED.name().equals( action ) ) {
      servicingUser = false;
      releaseWakefulIntent();
    }
    else if ( Actions.SCHEDULE_ALARM.name().equals( action ) ) {

      // If we already have a handler, then we're already in the process of figuring out where we are to be
      //  able to schedule the next alarm.
      if ( locationAcquiredHandler == null ) {
        handlingAlarmsFor = intent;

        findCurrentLocation();
      }
      else {
        // We're not going to do anything for this request, so make sure to release the wake lock!
        WakefulBroadcastReceiver.completeWakefulIntent( intent );
      }
    }

    // In case the process is killed while running, what should happen? "The Busy Coder's Guide to
    //  Android Development", v6.9, explains in the "Services and the Command Pattern" chapter, that
    //  the options are:
    //  1. START_STICKY, meaning that the service should be moved back into the
    //      started state (as if onStartCommand() had been called), but do not re-deliver
    //      the Intent to onStartCommand()
    //  2. START_REDELIVER_INTENT, meaning that the service should be restarted via a
    //      call to onStartCommand() , supplying the same Intent as was delivered this time
    //  3. START_NOT_STICKY, meaning that the service should remain stopped until
    //      explicitly started by application code
    //
    // Note that by doing nothing, "The Guide" (in "Embedding a Web Server" chapter), goes on to
    //  say that:
    //    ...the default implementation of onStartCommand() returns START_STICKY, which means
    //    that Android will keep trying to restart our service after our process gets terminated.
    //    This is a ridiculous default value and goes a long way towards explaining Android’s memory issues.
    return START_NOT_STICKY;
  }


  @Override
  public void onDestroy() {

    // Might be null if we get stopped before we're fully started...
    if ( null != locationManager ) {
      locationManager.onStop();
      locationManager = null;
    }

    super.onDestroy();
  }


  private void findCurrentLocation() {

    SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );

    // We have to figure out where we are in order to see when the next alarm is.
    locationAcquiredHandler = new LocationAcquiredHandler();
    eventBus.addHandler( locationAcquiredHandler );

    // Ask the location manager to do its best job figuring out where we are now, without using the cache.
    //  When it fires the location acquired event, we'll use the location to work out when to schedule the
    //  next alarm.
    LocationDeterminationOverride locationDeterminationOverride;
    if ( personalPrefs.getBoolean( PersonalPreferences.Prefs.NOTIFICATIONS_FOR_CURRENT_LOCATION.name(), false ) ) {
      locationDeterminationOverride = LocationDeterminationOverride.FORCE_TO_ACTUAL_LOCATION;
    }
    else {
      locationDeterminationOverride = LocationDeterminationOverride.NORMAL;
    }

    eventBus.fire( new RequestLocationUpdateEvent( locationDeterminationOverride, CachedLocationUsage.FALLBACK ) );
  }


  @SuppressWarnings( "AutoUnboxing" )
  private void handleLocation(
    final ZmanimLocation  location
  ) {

    if ( locationAcquiredHandler == null ) {
      // This is NOT supposed to happen. Send some data to try and diagnose.
      //  UPDATE: Yet, now all of a sudden, I am seeing it! For me, this is happening after I go into the emulator
      //    settings and change the time to test some alerting functionality...
      releaseWakefulIntent();
      return;
    }
    else {

      eventBus.removeHandler( locationAcquiredHandler );
      locationAcquiredHandler = null;

      if ( location.isInvalid || !location.isResolved() ) {
        // Generally, this means that we don't have permission to acquire the location. This can happen
        //  after the initial installation, before the user has had a chance to respond to the prompt.
        releaseWakefulIntent();
        return;
      }
    }

    AsyncTask<Void, Void, Void> schedulerTask = new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground( Void... params ) {

        Calendar now = Calendar.getInstance();
        now.setTimeZone( location.getTimeZone() );
        zmanNotificationScheduler.run( location, now );

        return null;
      }


      @Override
      protected void onPostExecute( Void aVoid ) {
        super.onPostExecute( aVoid );
        releaseWakefulIntent();
      }
    };

    schedulerTask.execute( (Void[])null );
  }


  private void scheduleUserAlert(
    final Zman              zman,
    final ZmanimLocation    location,
    final Calendar          alertAt
  ) {

    //noinspection MagicNumber
    PendingIntent pendingIntent = PendingIntent.getService( this,

      // This rather convoluted mechanism of identifying the pending intent is necessary so that we don't
      //  wind up replacing an intent just because we wind up scheduling a second alarm for the same
      //  zman type on a different day...
      ( ( (byte)alertAt.get( Calendar.DAY_OF_MONTH ) << 8 ) | ( zman.type.ordinal() & 0xFF ) ),

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

  }


  private void releaseWakefulIntent() {

    if ( handlingAlarmsFor != null ) {
      WakefulBroadcastReceiver.completeWakefulIntent( handlingAlarmsFor );
      handlingAlarmsFor = null;
    }

    if ( !servicingUser ) {
      stopSelf();
    }
  }


  private class LocationAcquiredHandler
    extends LocationAcquiredEvent.Handler {


    @Override
    public void onLocationAcquired(
      ZmanimLocation  location,
      boolean         isCachedLocation
    ) {
      handleLocation( location );
    }


    @Override
    public void onError(
      @NotNull LocationError  error,
      @NotNull ZmanimLocation lastKnownLocation
    ) {
      handleLocation( lastKnownLocation );
    }
  }


}
