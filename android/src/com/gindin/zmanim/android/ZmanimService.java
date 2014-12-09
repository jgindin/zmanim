package com.gindin.zmanim.android;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.Zman;
import com.gindin.zmanim.zman.Zmanim;
import org.jetbrains.annotations.NotNull;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;

import java.util.Date;
import java.util.List;

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


	public static enum Commands {
		/** Request the service to start for the Zmanim Activity. */
		ACTIVITY_STARTED,

		/** Notify the service that the Zmanim Activity is done. */
		ACTIVITY_STOPPED,

		/** Ask the service to schedule the next alarm. */
		SCHEDULE_ALARM,

		/** Force the (re)scheduling of the next alarm, as the device is coming back from a restart. */
		FORCE_SCHEDULE_NEXT_ALARM
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
	 * When we get a command coming in because an alarm has gone off, then we do want to shut ourselves down
	 *  once the notification is displayed and the next alarm scheduled. UNLESS we're also currently servicing
	 *  the user, in which case we must not shut down.
	 */
	private boolean                                           servicingAlarm = false;


	@Override
	public void onCreate() {
		super.onCreate();

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs, systemLocationManager );
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

		boolean startingForUser = intent.getBooleanExtra( Commands.ACTIVITY_STARTED.name(), false );
		boolean stoppingForuser = intent.getBooleanExtra( Commands.ACTIVITY_STOPPED.name(), false );
		boolean scheduleAlarm = intent.getBooleanExtra( Commands.SCHEDULE_ALARM.name(), false );
		boolean forceScheduleAlarm = intent.getBooleanExtra( Commands.FORCE_SCHEDULE_NEXT_ALARM.name(), false );

		if ( startingForUser ) {
			servicingUser = true;
			eventBus.fire( new ServiceStartedEvent() );
		}
		else if ( stoppingForuser ) {
			servicingUser = false;
			if ( !servicingAlarm ) {
				stopSelf();
			}
		}
		else if ( scheduleAlarm || forceScheduleAlarm ) {
			servicingAlarm = true;
		}

		return super.onStartCommand( intent, flags, startId );
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


	private void scheduleNextAlarm() {

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		if ( !personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true ) ) {
			return;
		}

		// We have to figure out where we are in order to see when the next alarm is.
		LocationAcquiredEvent.Handler locationAcquiredHandler = new LocationAcquiredHandler();
		eventBus.addHandler( locationAcquiredHandler );

		// Ask the location manager to do its best job figuring out where we are now, without using the cache.
		//  When it fires the location acquired event, we'll use the location to work out when to schedule the
		//  next alarm.
		LocationDeterminationOverride locationDeterminationOverride;
		if ( personalPrefs.getBoolean( PersonalPreferences.Prefs.NOTIFICATIONS_FOR_CURRENT_LOCATION.name(), true ) ) {
			locationDeterminationOverride = LocationDeterminationOverride.FORCE_TO_ACTUAL_LOCATION;
		}
		else {
			locationDeterminationOverride = LocationDeterminationOverride.NORMAL;
		}

		eventBus.fire( new RequestLocationUpdateEvent( locationDeterminationOverride, CachedLocationUsage.FALLBACK ) );
	}


	private void handleLocation( final ZmanimLocation location ) {

		SharedPreferences zmanimSelectionPrefs = getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

		Date now = new Date();
		Zman nextZman = null;

		HebrewDate today = HebrewCalendar.today();
		List<Zmanim> todaysZmanim = today.getZmanim( location );
		for ( Zmanim zmanim : todaysZmanim ) {
			String zmanName = zmanimSelectionPrefs.getString( zmanim.type.name, null );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			Date time = zman.getTime().getTime();
			if ( time.after( now ) ) {
				// We found the next zman!
				nextZman = zman;
				break;
			}
		}

		int delta;
		if ( nextZman == null ) {
			// Uh oh...
			LogUtils.logError( "schedule next alarm", "Unable to determine next zman!", new Throwable() );

			// TODO: Set this to ... maybe 30 minutes???
			delta = 0;
		}
		else {

			// TODO: Set this to the lesser of 30 minutes or 15 minutes before the next zman.
			delta = 0;
		}

		Intent runScheduledServiceIntent = new Intent( this, ZmanimService.class );
		runScheduledServiceIntent.putExtra( Commands.SCHEDULE_ALARM.name(), true );
		PendingIntent pendingIntent = PendingIntent.getService( this, 0, runScheduledServiceIntent, 0 );

		AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );

	}




	private class LocationAcquiredHandler
		extends LocationAcquiredEvent.Handler {

		@Override
		public void onLocationAcquired( ZmanimLocation location ) {
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
