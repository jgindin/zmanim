package com.gindin.zmanim.android.service;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.ZmanUtils;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.NotNull;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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

		/** Specified by the Zmanim Activity when it's first created and invoking the ACTIVITY_STARTED action. */
		SCHEDULE_IF_NECESSARY,

		/** Used when there's a ZmanUtils.NotifiedZmanim attached to the intent. */
		NOTIFIED_ZMANIM_EXTRA
	}


	/**
	 * String used to store & retrieve the time of the last zman we notified the user of.
	 */
	private static final String                               TIME_OF_LAST_NOTIFIED_ZMAN = "lastNotifiedZman";



	public static final EventBus                              ZMANIM_EVENT_BUS = new EventBus();

	/**
	 * If we check for the next zman, and it's more than 45 minutes away, just go ahead and schedule another
	 *  check in 45 minutes.
	 */
	private static final int                                  NEXT_ALARM_CHECK_INTERVAL = ( 45 * 60 * 1000 );

	/**
	 * In the absence of any directive from the user, we'll notify 15 minutes before an upcoming zman.
	 */
	private static final int                                  DEFAULT_ALERT_TIME_BEFORE_NEXT_ZMAN = ( 15 * 60 * 1000 );

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

	private ZmanUtils.PreferredZmanTypeLookup                 preferredZmanTypeLookup;

	private ZmanUtils.ZmanNotificationCallback                zmanNotificationCallback;


	public void onCreate() {
		super.onCreate();

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs, systemLocationManager );

		Thread.setDefaultUncaughtExceptionHandler(
			new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(
					final Thread    thread,
					final Throwable throwable
				) {

					releaseWakefulIntent();
				}
			}
		);

		final SharedPreferences zmanimSelectionPrefs = getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );
		preferredZmanTypeLookup = new ZmanUtils.PreferredZmanTypeLookup() {
			@Override
			public String getPreferredZman( final Zmanim.Type type ) {
				return zmanimSelectionPrefs.getString( type.name, null );
			}
		};


		zmanNotificationCallback = new ZmanUtils.ZmanNotificationCallback() {
			@Override
			public void notifyUser(
				final Zman nextZman
			) {
				notifyUserOfUpcomingZman( nextZman );
			}


			@Override
			public void scheduleNextCheck(
				final Calendar  now,
				final int       timeUntilNextCheck,
				final Zman      nextZman
			) {
				scheduleNextWakeup( timeUntilNextCheck, now );
			}
		};
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

			boolean scheduleIfNecessary = intent.getBooleanExtra( ActionParams.SCHEDULE_IF_NECESSARY.name(), false );
			boolean showNotifications = scheduleIfNecessary && userWantsNotifications();
			findCurrentLocation( showNotifications );

			eventBus.fire( new ServiceStartedEvent() );
		}
		else if ( Actions.ACTIVITY_STOPPED.name().equals( action ) ) {
			servicingUser = false;
			if ( handlingAlarmsFor == null ) {
				stopSelf();
			}
		}
		else if ( Actions.SCHEDULE_ALARM.name().equals( action ) ) {

			// If we already have a handler, then we're already in the process of figuring out where we are to be
			//  able to schedule the next alarm.
			if ( locationAcquiredHandler == null ) {
				handlingAlarmsFor = intent;

				if ( userWantsNotifications() ) {
					findCurrentLocation( true );
				}
				else {
					releaseWakefulIntent();
				}
			}
			else {
				// We're not going to do anything for this request, so make sure to release the wake lock!
				WakefulBroadcastReceiver.completeWakefulIntent( intent );
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


	private void findCurrentLocation( boolean showNotifications ) {

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );

		// We have to figure out where we are in order to see when the next alarm is.
		locationAcquiredHandler = new LocationAcquiredHandler( showNotifications );
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
		final ZmanimLocation  location,
		final boolean         showNotifications
	) {

		eventBus.removeHandler( locationAcquiredHandler );
		locationAcquiredHandler = null;

		if ( showNotifications ) {

			Calendar now = Calendar.getInstance();
			now.setTimeZone( location.getTimeZone() );

			ZmanUtils.nextZmanAction( location, now, getNotifiedZmanim( now ), NEXT_ALARM_CHECK_INTERVAL, DEFAULT_ALERT_TIME_BEFORE_NEXT_ZMAN,
				preferredZmanTypeLookup, zmanNotificationCallback );
		}

		releaseWakefulIntent();
	}


	private void scheduleNextWakeup(
		final long      nextWakeupDelta,
		final Calendar  now
	) {

		final long nextWakeup = now.getTimeInMillis() + nextWakeupDelta;

		// If we're close enough to the next zman, then we need to schedule a more precise alarm so we make
		//  sure not to miss it...
		final boolean needPreciseWakeup = ( nextWakeupDelta < NEXT_ALARM_CHECK_INTERVAL );

		Intent alarmIntent = new Intent( this, ZmanimReceiver.class );
		alarmIntent.putExtra( ActionParams.NOTIFIED_ZMANIM_EXTRA.name(), getNotifiedZmanim( now ) );
		PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );

		AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );
		alarmManager.set( needPreciseWakeup ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC, nextWakeup, pendingIntent );
	}


	@NotNull
	private ZmanUtils.NotifiedZmanim getNotifiedZmanim( final Calendar now ) {

		ZmanUtils.NotifiedZmanim notifiedZmanim = null;
		if ( handlingAlarmsFor != null ) {
			notifiedZmanim = (ZmanUtils.NotifiedZmanim)handlingAlarmsFor.getSerializableExtra( ActionParams.NOTIFIED_ZMANIM_EXTRA.name() );
		}

		if ( notifiedZmanim != null ) {
			if ( !notifiedZmanim.isForDate( now ) ) {
				notifiedZmanim = null;
			}
		}

		if ( notifiedZmanim == null ) {
			notifiedZmanim = new ZmanUtils.NotifiedZmanim( now );
		}

		return notifiedZmanim;
	}


	private void notifyUserOfUpcomingZman(
		final Zman nextZman
	) {

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );

		// Make sure we didn't already notify of this zman...
		long timeOfLastNotifiedZman = personalPrefs.getLong( TIME_OF_LAST_NOTIFIED_ZMAN, 0 );
		long timeOfThisZman = nextZman.getTime().getTimeInMillis();

		if ( timeOfThisZman <= timeOfLastNotifiedZman ) {
			// Looks like we already notified...
			return;
		}

		personalPrefs.edit().putLong( TIME_OF_LAST_NOTIFIED_ZMAN, timeOfThisZman ).apply();


		// One more check that the user actually wants to know about this zman...
		boolean userWantsThisNotification = ( personalPrefs.getInt( nextZman.type.name(), -1 ) >= 0 );
		if ( !userWantsThisNotification ) {
			return;
		}

		Intent openZmanim = new Intent( this, ZmanimActivity.class );

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


	private boolean userWantsNotifications() {

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		return personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_NOTIFICATIONS.name(), true );
	}


	private void releaseWakefulIntent() {

		if ( handlingAlarmsFor != null ) {
			WakefulBroadcastReceiver.completeWakefulIntent( handlingAlarmsFor );
			handlingAlarmsFor = null;
			if ( !servicingUser ) {
				stopSelf();
			}
		}
	}



	private class LocationAcquiredHandler
		extends LocationAcquiredEvent.Handler {

		/** Does the user want us to show notifications? */
		private final boolean                                   showNotifications;


		LocationAcquiredHandler( final boolean showNotifications ) {
			this.showNotifications = showNotifications;
		}


		@Override
		public void onLocationAcquired(
			ZmanimLocation  location,
			boolean         isCachedLocation
		) {
			handleLocation( location, showNotifications );
		}


		@Override
		public void onError(
			@NotNull LocationError  error,
			@NotNull ZmanimLocation lastKnownLocation
		) {
			handleLocation( lastKnownLocation, showNotifications );
		}
	}


}
