package com.gindin.zmanim.android;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;

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

	private LocationAcquiredEvent.Handler locationAcquiredHandler;

	public static enum Commands {
		/** Request the service to start for the Zmanim Activity. */
		ACTIVITY_STARTED,

		/** Notify the service that the Zmanim Activity is done. */
		ACTIVITY_STOPPED,

		/** Ask the service to schedule the next alarm. */
		SCHEDULE_ALARM
	}


	public static final EventBus                              ZMANIM_EVENT_BUS = new EventBus();

	private EventBus                                          eventBus = ZMANIM_EVENT_BUS;

	// TODO: Inject this
	private ZmanimLocationManager                             locationManager;

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
		else if ( scheduleAlarm ) {
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
		eventBus.fire( new RequestLocationUpdateEvent( true, false ) );
	}


	private void handleLocation( final ZmanimLocation location ) {

		AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );
		Intent runScheduledServiceIntent = new Intent( this, ZmanimService.class );
		PendingIntent pendingIntent = PendingIntent.getService( this, 0, runScheduledServiceIntent, 0 );

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
