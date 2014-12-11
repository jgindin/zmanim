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
import com.gindin.zmanim.zman.ZmanFormatter;
import com.gindin.zmanim.zman.Zmanim;
import org.jetbrains.annotations.NotNull;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

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


	public static enum Actions {
		/** Request the service to start for the Zmanim Activity. */
		ACTIVITY_STARTED,

		/** Notify the service that the Zmanim Activity is done. */
		ACTIVITY_STOPPED,

		/** Ask the service to schedule the next alarm. May optionally have a boolean extra to force the alarm scheduling. */
		SCHEDULE_ALARM
	}


	public static enum ActionParams {

		/** Can be specified with the SCHEDULE_ALARM action to indicate the alarm should be scheduled no matter what. */
		FORCE_SCHEDULE,

		/** Specified by the Zmanim Activity when it's first created and invoking the ACTIVIT_STARTED action. */
		SCHEDULE_IF_NECESSARY
	}


	public static final EventBus                              ZMANIM_EVENT_BUS = new EventBus();

	/**
	 * If we check for the next zman, and it's more than 30 minutes away, just go ahead and schedule another
	 *  check in 30 minutes.
	 */
	private static final int                                  MINUTES_BETWEEN_ALARMS = 30;
	private static final long                                 NEXT_ALARM_CHECK_INTERVAL = ( MINUTES_BETWEEN_ALARMS * 60 * 1000L );

	/**
	 * In the absence of any directive from the user, we'll notify 15 minutes before an upcoming zman.
	 */
	private static final int                                  DEFAULT_MINUTES_BEFORE_NEXT_ZMAN = 15;
	private static final long                                 DEFAULT_TIME_BEFORE_NEXT_ZMAN = ( DEFAULT_MINUTES_BEFORE_NEXT_ZMAN * 60 * 1000L );


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

		String action = intent.getAction();
		if ( Actions.ACTIVITY_STARTED.name().equals( action ) ) {
			servicingUser = true;

			if ( intent.getBooleanExtra( ActionParams.SCHEDULE_IF_NECESSARY.name(), false ) ) {
				listenForLocation( null );
			}

			eventBus.fire( new ServiceStartedEvent() );
		}
		else if ( Actions.ACTIVITY_STOPPED.name().equals( action ) ) {
			servicingUser = false;
			if ( !servicingAlarm ) {
				stopSelf();
			}
		}
		else if ( Actions.SCHEDULE_ALARM.name().equals( action ) ) {

			// If we already have a handler, then we're already in the process of figuring out where we are to be
			//  able to schedule the next alarm.
			if ( locationAcquiredHandler == null ) {
				servicingAlarm = true;
				findCurrentLocation( intent );
			}
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


	private void findCurrentLocation( final Intent wakefulIntent ) {

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		if ( !personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true ) ) {
			return;
		}

		// We have to figure out where we are in order to see when the next alarm is.
		listenForLocation( wakefulIntent );

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


	private void listenForLocation( final Intent wakefulIntent ) {
		locationAcquiredHandler = new LocationAcquiredHandler( wakefulIntent );
		eventBus.addHandler( locationAcquiredHandler );
	}


	private void handleLocation(
		final ZmanimLocation  location,
		final Intent          wakefulIntent
	) {

		eventBus.removeHandler( locationAcquiredHandler );
		locationAcquiredHandler = null;

		Date now = new Date();
		Zman nextZman = findNextZman( location, now );

		long timeForNextAlaram = System.currentTimeMillis();
		if ( nextZman == null ) {
			// Uh oh...
			LogUtils.logError( "schedule next alarm", "Unable to determine next zman!", new Throwable() );

			// Well, try again in 30 minutes...
			timeForNextAlaram += NEXT_ALARM_CHECK_INTERVAL;
		}
		else {

			// A few cases here:
			//  1. There's no zman in the next 30 minutes: Just set the next alarm for 30 minutes from now.
			//  2. There is a zman within 30 minutes:
			//    a. The zman is within 15 minutes: Show a notification. Find the next zman after that.
			//        If it's less than 30 minutes away, schedule the next alarm so we wake up 15 minutes before
			//          that next one.
			//        Otherwise, schedule the alarm for 30 minutes from now.
			//    b. The zman is more than 15 minutes away, but less than 30: Schedule the next alarm so we wake
			//        up 15 minutes before.
			long timeUntilNextZman = howLongUntil( now, nextZman );
			if ( timeUntilNextZman >= NEXT_ALARM_CHECK_INTERVAL ) {
				timeForNextAlaram += NEXT_ALARM_CHECK_INTERVAL;
			}
			else if ( timeUntilNextZman < DEFAULT_TIME_BEFORE_NEXT_ZMAN ) {
				// Show the notification!
				notifyUserOfUpcomingZman( nextZman );

				// Find the next zman (after the one we're notifying about) and get our next alarm schedule appropriately...
				nextZman = findNextZman( location, new Date( timeForNextAlaram + DEFAULT_TIME_BEFORE_NEXT_ZMAN ) );
				if ( nextZman == null ) {
					timeForNextAlaram += NEXT_ALARM_CHECK_INTERVAL;
				}
				else {
					timeUntilNextZman = howLongUntil( now, nextZman );
					if ( timeUntilNextZman >= NEXT_ALARM_CHECK_INTERVAL ) {
						timeForNextAlaram += NEXT_ALARM_CHECK_INTERVAL;
					}
					else {
						// We're close...reschedule for when we're going to want to show the notification...
						timeForNextAlaram += ( timeUntilNextZman - DEFAULT_TIME_BEFORE_NEXT_ZMAN );
					}
				}
			}
			else {
				// We're close...reschedule for when we're going to want to show the notification...
				timeForNextAlaram += ( timeUntilNextZman - DEFAULT_TIME_BEFORE_NEXT_ZMAN );
			}

		}


		Intent alarmIntent = new Intent( this, ZmanimReceiver.class );
		PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0, alarmIntent, 0 );

		AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );
		alarmManager.set( AlarmManager.RTC, timeForNextAlaram, pendingIntent );

		if ( servicingAlarm ) {
			servicingAlarm = false;
		}

		// Could be null if we're scheduling alarms because the Activity was created.
		if ( wakefulIntent != null ) {
			WakefulBroadcastReceiver.completeWakefulIntent( wakefulIntent );
		}

	}


	/**
	 * Calculate the number of milliseconds between the given date and the zman.
	 */
	private long howLongUntil(
		final Date now,
		final Zman nextZman
	) {

		long nowTime = now.getTime();
		long zmanTime = nextZman.getTime().getTime().getTime();

		if ( nowTime > zmanTime ) {
			return 0;
		}

		long diff = zmanTime - nowTime;
		return diff;
	}


	private Zman findNextZman(
		final ZmanimLocation  location,
		final Date            notAfterTime
	) {

		SharedPreferences zmanimSelectionPrefs = getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

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
			if ( time.after( notAfterTime ) ) {
				// We found the next zman!
				nextZman = zman;
				break;
			}
		}

		return nextZman;
	}


	private void notifyUserOfUpcomingZman(
		final Zman nextZman
	) {

		Intent openZmanim = new Intent( this, ZmanimActivity.class );

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		boolean showSeconds = personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
		ZmanFormatter zmanFormatter = new ZmanFormatter( showSeconds );

		String notificationText = getString( R.string.notification, nextZman.type.name, zmanFormatter.formatZmanTime( nextZman ) );
		NotificationCompat.Builder builder = new NotificationCompat.Builder( this )
			.setContentTitle( getString( R.string.app_name ) )
			.setContentText( notificationText )
			.setSmallIcon( R.drawable.icon )
			.setTicker( notificationText )
			.setAutoCancel( true )
			.setDefaults( Notification.DEFAULT_ALL )
			.setContentIntent( PendingIntent.getActivity( this, 0, openZmanim, 0 ) );

		NotificationManager notificationManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
		notificationManager.notify( 1, builder.build() );
	}


	private class LocationAcquiredHandler
		extends LocationAcquiredEvent.Handler {

		/**
		 * The wakeful intent which caused us to need to find out where we are. May be null if we're scheduling the
		 *  alarm because the Activity has started.
		 */
		private final Intent                                    wakefulIntent;


		LocationAcquiredHandler( final Intent wakefulIntent ) {
			this.wakefulIntent = wakefulIntent;
		}


		@Override
		public void onLocationAcquired(
			ZmanimLocation  location,
			boolean         isCachedLocation
		) {
			handleLocation( location, wakefulIntent );
		}


		@Override
		public void onError(
			@NotNull LocationError  error,
			@NotNull ZmanimLocation lastKnownLocation
		) {
			handleLocation( lastKnownLocation, wakefulIntent );
		}
	}


}
