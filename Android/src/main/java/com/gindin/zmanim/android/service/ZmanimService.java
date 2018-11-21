package com.gindin.zmanim.android.service;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.prefs.LocationProviders;
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
import android.location.LocationManager;
import android.os.Build;
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

  private ZmanNotificationScheduler                         zmanNotificationScheduler;

  private LocationToAddressConverter                        locationToAddressConverter;



  public static void enqueueWork(
    Actions   action,
    Context   context
  ) {

    enqueueWork( action, false, context );
  }


  public static void enqueueWork(
    Actions action,
    boolean force,
    Context context
  ) {

    Intent zmanimService = new Intent( context, ZmanimService.class );
    zmanimService.setAction( action.name() );

    if ( force ) {
      zmanimService.putExtra( ZmanimService.ActionParams.FORCE_SCHEDULE.name(), true );
    }

    enqueueWork( context, ZmanimService.class, 5170, zmanimService );
  }



  public void onCreate() {
    super.onCreate();

    final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler( exceptionHandler );
  }


  @Override
  protected void onHandleWork( @NonNull Intent intent ) {

    final SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
    final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

    LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
    locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs, systemLocationManager );

    locationToAddressConverter = new LocationToAddressConverter( this, eventBus, new Geocoder( this ) );

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

    String action = intent.getAction();

    if ( Actions.ACTIVITY_STARTED.name().equals( action ) ) {
      findCurrentLocation();

      DisplayNotificationService.createNotificationChannel( this, zmanCallback );
    }
    else if ( Actions.ACTIVITY_STOPPED.name().equals( action ) ) {
      // Nothing to do...
    }
    else if ( Actions.SCHEDULE_ALARM.name().equals( action ) ) {

      // If we already have a handler, then we're already in the process of figuring out where we are to be
      //  able to schedule the next alarm.
      if ( locationAcquiredHandler == null ) {
        findCurrentLocation();
      }
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

    super.onDestroy();
  }


  private void findCurrentLocation() {

    // We have to figure out where we are in order to see when the next alarm is.
    locationAcquiredHandler = new LocationAcquiredHandler();
    eventBus.addHandler( locationAcquiredHandler );

    SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    boolean preferCachedLocation = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_CACHED_LOCATION.name(), true );

    RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage;
    if ( preferCachedLocation ) {
      cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.PREFER;
    }
    else {
      cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.NEVER;
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
    int intentId = ( ( (byte)alertAt.get( Calendar.DAY_OF_MONTH ) << 8 ) | ( zman.type.ordinal() & 0xFF ) );

    //noinspection MagicNumber
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
