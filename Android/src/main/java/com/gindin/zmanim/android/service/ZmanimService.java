package com.gindin.zmanim.android.service;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.ZmanimActivity;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.NotifiedZmanim;
import com.gindin.zmanlib.ZmanCallback;
import com.gindin.zmanlib.ZmanNotificationScheduler;
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
import android.os.AsyncTask;
import android.os.Build;
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

	private ZmanCallback                                      zmanCallback;

	/**
	 * DO NOT access this directly! Always use the getNotifiedZmanim method to ensure using for the correct date!
	 */
	private NotifiedZmanim                                    notifiedZmanim;



	public void onCreate() {
		super.onCreate();

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

		final SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		final SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		final SharedPreferences zmanimSelectionPrefs = getSharedPreferences( ZmanimPrefs.ZMANIM_SELECTION.name(), Context.MODE_PRIVATE );

		LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		locationManager = new ZmanimLocationManager( this, eventBus, locationManagementPrefs, personalPrefs, systemLocationManager );

		zmanCallback = new PrefsBasedZmanCallback( zmanimSelectionPrefs, personalPrefs );
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

			boolean showNotifications = userWantsNotifications();
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

    if ( !showNotifications ) {
      releaseWakefulIntent();
    }
    else {

      AsyncTask<Void, Void, Void> schedulerTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground( Void... params ) {

          Calendar now = Calendar.getInstance();
          now.setTimeZone( location.getTimeZone() );
          ZmanNotificationScheduler.run( location, now, zmanCallback );

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

  }


	private void scheduleNextWakeup(
		final long      nextWakeupDelta,
		final Calendar  now
	) {

    getNotifiedZmanim( now );
    SaveNotifiedZmanimTask task = new SaveNotifiedZmanimTask();
    task.execute( notifiedZmanim );

    Intent alarmIntent = new Intent( this, ZmanimReceiver.class );
		PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );

		// If we're close enough to the next zman, then we need to schedule a more precise alarm so we make
		//  sure not to miss it...
		boolean needPreciseWakeup = ( nextWakeupDelta < ZmanNotificationScheduler.CHECK_INTERVAL );

		long nextWakeup = now.getTimeInMillis() + nextWakeupDelta;

		AlarmManager alarmManager = (AlarmManager)getSystemService( Context.ALARM_SERVICE );

		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
			alarmManager.setExactAndAllowWhileIdle( AlarmManager.RTC_WAKEUP, nextWakeup, pendingIntent );
		}
		else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
			alarmManager.setExact( AlarmManager.RTC_WAKEUP, nextWakeup, pendingIntent );
		}
		else {
			alarmManager.set( needPreciseWakeup ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC, nextWakeup, pendingIntent );
		}
	}


  /**
   * Because of the process to possibly load the previously notified zmanim, it is assumed that this method will NOT
   *  be invoked on the main thread.
   */
	private void getNotifiedZmanim( final Calendar now ) {

		if ( notifiedZmanim == null ) {
			notifiedZmanim = NotifiedZmanim.restore( getFilesDir() );
		}

		if ( notifiedZmanim != null ) {
			if ( !notifiedZmanim.isForDate( now ) ) {
				notifiedZmanim = null;
			}
		}

		if ( notifiedZmanim == null ) {
			notifiedZmanim = new NotifiedZmanim( now );
		}
	}


  /**
	 * Displays a notification of the upcoming zman.
	 *<p/>
	 *
	 * This method assumes that if it is called, it's because the notification is supposed to be shown. In other words,
	 *  it's up to the caller to ensure that we should really be notifying the user.
	 *
	 * @param nextZman the upcoming zman to notify the user about.
	 */
	private void notifyUserOfUpcomingZman(
		final Zman nextZman
	) {

		Intent openZmanim = new Intent( this, ZmanimActivity.class );

		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );
		boolean showSeconds = personalPrefs.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
		ZmanFormatter zmanFormatter = new ZmanFormatter( showSeconds );
		String notificationText = getString( R.string.notification, nextZman.type.name, zmanFormatter.formatZmanTime( nextZman ) );

		if ( BuildConfig.DEBUG ) {
			notificationText += " (" + zmanCallback.howManyMinutesToAlertBefore( nextZman.type ) + ")";
		}

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



	private class PrefsBasedZmanCallback
		implements ZmanCallback {

		private final SharedPreferences                         zmanimSelectionPrefs;

		private final SharedPreferences                         personalPrefs;


		PrefsBasedZmanCallback(
			SharedPreferences zmanimSelectionPrefs,
			SharedPreferences personalPrefs
		) {
			this.zmanimSelectionPrefs = zmanimSelectionPrefs;
			this.personalPrefs = personalPrefs;
		}


		@Override
		public String getPreferredZman( final Zmanim.Type type ) {
			return zmanimSelectionPrefs.getString( type.name, null );
		}


		@Override
		public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
			return personalPrefs.getInt( type.name(), -1 );
		}


		@Override
		public boolean hasBeenNotified(
			Calendar  now,
			Zman      zman
		) {

			getNotifiedZmanim( now );
			return notifiedZmanim.contains( zman.type );
		}


		@Override
		public void notifyUser(
			final Calendar  now,
			final Zman      nextZman
		) {

			getNotifiedZmanim( now );
			notifiedZmanim.notified( nextZman );

			// If the zman has already passed, do not bother the user...
	   if ( nextZman.getTime().compareTo( now ) >= 0 ) {
	     notifyUserOfUpcomingZman( nextZman );
	   }
		}


		@Override
		public void scheduleNextCheck(
			final Calendar  now,
			final int       timeUntilNextCheck
		) {
			scheduleNextWakeup( timeUntilNextCheck, now );
		}
  }


  private class SaveNotifiedZmanimTask
    extends AsyncTask<NotifiedZmanim, Void, Void> {

    @Override
    protected Void doInBackground( NotifiedZmanim... params ) {
      params[0].save( getFilesDir() );

      return null;
    }
  }

}
