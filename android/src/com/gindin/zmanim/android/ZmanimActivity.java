/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import com.gindin.util.LogUtils;
import com.gindin.util.Version;
import com.gindin.util.events.EventManager;
import com.gindin.zmanim.android.location.LocationChangedEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.LocationNameUpdatedEvent;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.formatters.HebrewDateFormatter;
import com.gindin.zmanim.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.ZmanFormatter;

import java.util.Date;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
	extends Activity
	implements DateChangedEvent.HasListeners, LocationChangedEvent.HasListeners {

	private static final int                                  UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID = 1000;

	/** What date is currently being viewed? */
	private HebrewDate                                        currentDate;

	/** Controls how we format the zmanim. */
	private ZmanFormatter                                     zmanFormatter;

	/** How do we format Hebrew & Secular dates? */
	private HebrewDateFormatter                               hebrewDateFormatter;
	private SecularDateFormatter                              secularDateFormatter;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;

	private ZmanimLocationManager                             locationManager;

	private LocationChangedEvent.Listener                     locationListener;

	private PersonalPrefsManager                              personalPrefsManager;


	/** We use a progress dialog to let the user know we're working. */
	private ProgressDialog                                    determiningLocationDialog;

	/**
	 * Task used to build the UI. While we're in the process of (re)building the UI, this will be set. As soon as
	 *  we're done, it's reset to null.
	 */
	private AsyncTask<Void, Void, Void>                       buildUITask = null;

	private final DateChangeEventManager                      dateChangedEventManager = new DateChangeEventManager();

	private ZmanimDisplay                                     zmanimDisplay;

	/** The actual list view where the zmanim are displayed. Cached here to avoid multiple look ups since it should never change. */
	private ListView                                          listView;

	private DatePickerDialog                                  datePickerDialog;


	//  See the lifecycle: http://developer.android.com/reference/android/app/Activity.html
	//    onCreate
	//      onRestart
	//      onStart
	//        onResume
	//        onPause
	//      onStop
	//    onDestroy
	//
	// The entire lifetime of an activity happens between the first call to onCreate(Bundle) through to a single final
	//  call to onDestroy(). An activity will do all setup of "global" state in onCreate(), and release all remaining
	//  resources in onDestroy(). For example, if it has a thread running in the background to download data from the
	//  network, it may create that thread in onCreate() and then stop the thread in onDestroy().
	//
	// The visible lifetime of an activity happens between a call to onStart() until a corresponding call to onStop().
	//  During this time the user can see the activity on-screen, though it may not be in the foreground and interacting
	//  with the user. Between these two methods you can maintain resources that are needed to show the activity to the
	//  user. For example, you can register a BroadcastReceiver in onStart() to monitor for changes that impact your UI,
	//  and unregister it in onStop() when the user an no longer see what you are displaying. The onStart() and onStop()
	//  methods can be called multiple times, as the activity becomes visible and hidden to the user.
	//
	// The foreground lifetime of an activity happens between a call to onResume() until a corresponding call to onPause().
	//  During this time the activity is in front of all other activities and interacting with the user. An activity can
	//  frequently go between the resumed and paused states -- for example when the device goes to sleep, when an activity
	//  result is delivered, when a new intent is delivered -- so the code in these methods should be fairly lightweight.
	//
	// Specifically, http://www.screaming-penguin.com/node/7746 says that if you have an indeterminate dialog showing
	//  it must get cleaned up in the onPause, or else it will leak.


	/** Called when the activity is first created. */
	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		// Enable this to get more detailed info regarding disk or network access occurring on the UI thread.
		//  See http://stackoverflow.com/questions/704311/android-how-do-i-investigate-an-anr.
/*
		StrictMode.setVmPolicy(
			new StrictMode.VmPolicy.Builder()
				.detectAll()
        .penaltyLog()
        .penaltyDeath()
        .build()
		);
*/

		super.onCreate( savedInstanceState );

		LogUtils.startSession( this );
		EventManager.performListenerChecks = Version.isDevelopment();

		personalPrefsManager = new PersonalPrefsManager( this );

		AppUpgrader.handle( this, personalPrefsManager, new ZmanimSelectionPrefsManager( this ) );

		setContentView( R.layout.main );

		determiningLocationDialog = new ProgressDialog( this );
		determiningLocationDialog.setIndeterminate( true );
		determiningLocationDialog.setMessage( "Determining location..." );

		locationListener = new LocationChangedEvent.Listener() {

			@Override public void onProgress( String message ) {

				// If we already have a location, then do not show the dialog...
				if ( null == locationManager.getCurrentLocation() ) {
					determiningLocationDialog.setMessage( message );
					if ( !determiningLocationDialog.isShowing() ) {
						determiningLocationDialog.show();
					}
				}
			}

			@Override public void onLocationChanged( LocationChangedEvent event ) {
				rebuildUI( event.getEventInfo(), event.getError() );
			}


			@Override
			public void onLocationNameUpdated( LocationNameUpdatedEvent event ) {
				updateLocation( event.eventInfo );
			}
		};

		initializeActivity();
	}


	private void initializeActivity() {

		determiningLocationDialog.show();

		locationManager = new ZmanimLocationManager( ZmanimActivity.this );
		LogUtils.setLocationHandler( locationManager );

		setupViewListeners();

		// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
		// Note that it *must* be run on the UI thread...
		gestureDetector = new GestureDetector( ZmanimActivity.this, new ZmanimGestureListener() );

		// Now start listening for location changes...
		locationManager.addLocationChangeListener( locationListener );
	}


	@Override
	protected void onStart() {
		super.onStart();

		locationManager.onStart();
	}


	@Override
	protected void onRestart() {
		super.onRestart();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onStop() {

		// Might be null if we get stopped before we're fully started...
		if ( null != locationManager ) {
			locationManager.onStop();
		}

		determiningLocationDialog.dismiss();
		super.onStop();
	}


	@Override
	protected void onDestroy() {

		if ( null != zmanimDisplay ) {
			zmanimDisplay.onDestroy();
			zmanimDisplay = null;
		}

		// Might be null if we got stopped before we were fully started...
		//  Note that since we're going to be destroying the location manager, it had better be the last thing
		//  of meaning we do in this method.
		if ( null != locationManager ) {
			locationManager.removeLocationChangeListener( locationListener );
			locationManager.onDestroy();
			locationManager = null;
		}

		LogUtils.endSession( this );
		
		super.onDestroy();
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	@Override
	protected void onActivityResult(
		int     requestCode,
		int     resultCode,
		Intent  data
	) {

		super.onActivityResult( requestCode, resultCode, data );

		Activities activity = Activities.values()[ requestCode ];

		switch ( activity ) {

			case ENABLE_LOCATION_SERVICES:
				determiningLocationDialog.setMessage( getString( R.string.determining_location ) );
				determiningLocationDialog.show();
				locationManager.onLocationProvidersChanged();
				break;

			case EDIT_LOCATION_PREFERENCES:
				locationManager.locationPrefsChanged();
				break;

			case PERSONAL_PREFERENCS:
				personalPrefsManager.personalPrefsChanged();
				break;
		}
	}


	public ListView getListView() {

		if ( null == listView ) {
			listView = (ListView)findViewById( android.R.id.list );
		}

		return listView;
	}


	public void setAdapter( ListAdapter listAdapter ) {
		getListView().setAdapter( listAdapter );
	}


	public void rebuildUI() {
		rebuildUI( locationManager.getLastKnownLocation(), null );
	}


	private void rebuildUI(
		final ZmanimLocation  location,
	  final LocationError   error
	) {

		if ( null != buildUITask ) {
			buildUITask.cancel( false );
		}

		buildUITask = new BuildUIAsyncTask( location, error );
		buildUITask.execute( (Void)null );
	}


	private void buildHeader(
		ZmanimLocation location
	) {

		// Listen for 'clicks' indicating the user wants to change the date being viewed.
		final View secularDate = findViewById( R.id.secular_date );
		secularDate.setOnClickListener(
			new View.OnClickListener() {
				public void onClick( View v ) {
					showDialog( R.id.secular_date );
				}
			}
		);

		updateLocation( location );

		// Update the header
		updateHeader( location );
	}


	private void updateLocation( ZmanimLocation location ) {

		final String locationName;
		if ( null == location ) {
			locationName = getResources().getString( R.string.unknown_location );
		}
		else {
			locationName = location.getLocationName();
		}

		// Let the location manager know about the field that contains the current location.
		final View locationField = findViewById( R.id.location );

		// I'm seeing NPE in the logs, but I have no idea how this can be. It doesn't repro for me, so for now
		//  the best I can do is to guard against it...
		if ( null == locationManager ) {
			LogUtils.logError( "ZmanimActivity::updateLocation", "null location manager. zmanimDisplay?" + zmanimDisplay, null  );
		}
		else {
			locationManager.setLocationField( locationField );
		}

		// We may not have a location name. In that case, don't do anything. This is a bit of a hack, because we can
		//  determine the location name *before* we render the whole UI. And, in these cases, we may not have the
		//  location with a name (because maybe we've got a last-known location that didn't store the name).
		if ( ( null != locationName ) && ( locationName.length() > 0 ) ) {
			updateField( R.id.location, locationName );
		}
	}


	private void buildFooter(
		ZmanimLocation      location,
		final LocationError error
	) {

		if ( null == error ) {
			updateField( R.id.messages, "" );
			//displayDebugMessages();
		}
		else {
			handleLocationError( location, error );
		}
	}


	private void handleLocationError(
		ZmanimLocation  location,
		LocationError   error
	) {

		String errorMessage = getResources().getString( R.string.unable_to_determine_location_error );

		Toast toast = Toast.makeText( this, errorMessage, Toast.LENGTH_LONG );
		toast.show();

		// Now...why is it we couldn't determine our location?
		String extra = "";
		final View.OnClickListener clickListener;
		switch ( error.errorCode ) {

			case NO_LOCATION_MANAGER_SERVICE:
				// This is just weird...
				extra = "No location manager??? Internal error...";
				clickListener = null;
				break;


			case CANNOT_DETERMINE_LOCATION:
				extra = getResources().getString( R.string.cannot_determine_location );

				if ( null != location ) {
					extra += " " + getResources().getString( R.string.last_location_time, new Date( location.locationTime ) ); 
				}

				// TODO: Need to add click listener that will invoke a dialog allowing the user to:
				//  1. try obtaining location again; or
				//  2. manually specify where the are
				clickListener = null;
				break;

			
			case NO_PROVIDERS:
				// Well, gosh..looks like the user hasn't turned on location management!
				extra = getResources().getString( R.string.no_location_management );
				clickListener = new View.OnClickListener() {
					public void onClick( View view ) {
						showDialog( UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID );
					}
				};
				break;


			case BAD_INPUT:
				extra = error.message;
				clickListener = null;
				break;


			default:
				// Seems necessary to have a default case, even though we've specified all the cases, so that the
				//  clickListener gets initialized.
				clickListener = null;
		}


		if ( null == clickListener ) {
			errorMessage = errorMessage + extra;
		}
		else {
			errorMessage = errorMessage + " <u>" + extra + "</u>";

			View messages = findViewById( R.id.messages );
			messages.setOnClickListener( clickListener );
		}

		updateField( R.id.messages, Html.fromHtml( errorMessage + "<br/>" ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void updateHeader( ZmanimLocation location ) {

		updateField( R.id.hebrew_date, hebrewDateFormatter.format( currentDate, location ) );
		updateField( R.id.secular_date, Html.fromHtml( "<u>" + secularDateFormatter.format( currentDate ) + "</u>" ) );

		String description = currentDate.getDetails().getDescription();
		if ( ( null == description ) || ( 0 == description.trim().length() ) ) {
			findViewById( R.id.holiday_row ).setVisibility( View.GONE );
		}
		else {
			findViewById( R.id.holiday_row ).setVisibility( View.VISIBLE );
			updateField( R.id.holiday, description );
		}
	}


	private void setupViewListeners() {

		// Handle gestures. In our case, we're listening for swiping left, right, up & down...
		View.OnTouchListener touchListener = new View.OnTouchListener() {
			public boolean onTouch(
				View        view,
				MotionEvent event
			) {
				return gestureDetector.onTouchEvent( event );
			}
		};

		View mainView = findViewById( R.id.main_layout );
		mainView.setOnTouchListener( touchListener );

		View listing = findViewById( android.R.id.list );
		listing.setOnTouchListener( touchListener );
	}


	@Override
	public boolean onTouchEvent( MotionEvent event ) {
		return gestureDetector.onTouchEvent( event );
	}


	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.main_menu, menu );

		return true;
	}


	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		int id = item.getItemId();
		switch ( id ) {

			case R.id.personal_prefs:
				personalPrefsManager.editPrefs();
				break;

			case R.id.location_prefs:
				locationManager.editLocationPrefs();
				return true;

			case R.id.about:
				showDialog( id );
				return true;

			case R.id.help:
				showDialog( id );
				return true;

			case R.id.refresh:
				refresh();
				return true;
		}

		return false;
	}


	@Override
	protected Dialog onCreateDialog( int dialogID ) {

		switch ( dialogID ) {
			case R.id.secular_date:
				return getSecularDatePickerDialog();

			case R.id.about:
				return createHtmlTextDialog( R.string.about_header, R.layout.about, R.id.about_text );

			case R.id.help:
				return createHtmlTextDialog( R.string.help_header, R.layout.help, R.id.help_text );

			case UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID:
				return createEnableLocationSourcesDialog();
		}

		return null;
	}


	private Dialog createHtmlTextDialog(
		int   titleResource,
	  int   layoutResource,
	  int   webviewId
	) {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( titleResource );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( layoutResource, null );

		WebView helpText = (WebView)layout.findViewById( webviewId );
		final String textLocation = getTextLocation( webviewId );
		helpText.loadUrl( "file:///android_asset/" + textLocation );

		builder.setView( layout );
		return builder.create();
	}


	private String getTextLocation( int webviewId ) {


		if ( R.id.help_text == webviewId ) {
			return "com/gindin/zmanim/android/help.html";
		}

		if ( R.id.about_text == webviewId ) {
			return "com/gindin/zmanim/android/about.html";
		}

		if ( R.id.enable_locations_text == webviewId ) {
			return "com/gindin/zmanim/android/enable_locations.html";
		}

		return null;
	}


	@SuppressWarnings( "FeatureEnvy" )
	private Dialog getSecularDatePickerDialog() {

		if ( null == datePickerDialog ) {

			final DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(
					DatePicker datePicker,
					int year,
					int month,
					int date
				) {

					// Seems like this callback gets invoked multiple times. That's bad...we don't want to do all the work
					//  involved in changing dates if we're not actually changing dates...
					if ( ( currentDate.getGregorianYear() != year ) ||
							 ( currentDate.getGregorianMonth() != month ) ||
							 ( currentDate.getGregorianDayOfMonth() != date ) ) {

						changeDate( HebrewCalendar.getForDate( year, month, date ) );
					}
				}
			};

			datePickerDialog = new DatePickerDialog( this, dateChangedListener, currentDate.getGregorianYear(),
				currentDate.getGregorianMonth(), currentDate.getGregorianDayOfMonth() );
		}
		else {
			datePickerDialog.updateDate( currentDate.getGregorianYear(),
				currentDate.getGregorianMonth(), currentDate.getGregorianDayOfMonth() );
		}

		return datePickerDialog;
	}


	private Dialog createEnableLocationSourcesDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( R.string.unable_to_determine_location_header );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.enable_locations, null );

		WebView helpText = (WebView)layout.findViewById( R.id.enable_locations_text );
		final String textLocation = getTextLocation( R.id.enable_locations_text );
		helpText.loadUrl( "file:///android_asset/" + textLocation );

		builder.setPositiveButton( R.string.unable_to_determine_location_button,
			new DialogInterface.OnClickListener() {
				public void onClick(
					DialogInterface dialogInterface,
					int             buttonId
				) {

					// Launch the correct settings screen for the user. Saw this example from:
					//  http://stackoverflow.com/questions/623225/android-go-to-settings-screen
					startActivityForResult( new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS ),
						Activities.ENABLE_LOCATION_SERVICES.ordinal() );
				}
			}
		);

		builder.setView( layout );
		return builder.create();
	}


	private void refresh() {
		// This will force us to go re-query for our location.
		locationManager.onLocationProvidersChanged();
	}

	private void updateField(
		int     field,
		String  text
	) {

		TextView view = (TextView)findViewById( field );
		view.setText( text );
	}


	private void updateField(
		int     field,
		Spanned text
	) {

		TextView view = (TextView)findViewById( field );
		view.setText( text );
	}


	@Override
	public void addDateChangeListener( DateChangedEvent.Listener listener ) {
		dateChangedEventManager.addListener( listener );
		listener.onDateChanged( new DateChangedEvent( currentDate ) );
	}


	@Override
	public void removeDateChangeListener( DateChangedEvent.Listener listener ) {
		dateChangedEventManager.removeListener( listener );
	}


	@Override
	public void addLocationChangeListener( LocationChangedEvent.Listener listener ) {
		locationManager.addLocationChangeListener( listener );
		listener.onLocationChanged( new LocationChangedEvent( locationManager.getLastKnownLocation() ) );
	}


	@Override
	public void removeLocationChangeListener( LocationChangedEvent.Listener listener ) {
		locationManager.removeLocationChangeListener( listener );
	}


	private void changeDate( HebrewDate newDate ) {
		currentDate = newDate;
		dateChangedEventManager.fire( new DateChangedEvent( newDate ) );

		// todo: should we have a date change listener to do this???
		updateHeader( locationManager.getLastKnownLocation() );
	}


	private class ZmanimGestureListener
		extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 120;


		@Override
		public boolean onFling(
			MotionEvent startMotionEvent,
			MotionEvent endMotionEvent,
			float       xVelocity,
			float       yVelocity
		) {

			// Did the user want to move to the next/prev day (swiping right or left) or next/prev month
			//  (swiping up or down)?
			// TODO: Re-enable the up/down swiping if we can...
			float xDiff = startMotionEvent.getRawX() - endMotionEvent.getRawX();

			if ( Math.abs( xDiff ) < SWIPE_MIN_DISTANCE ) {
				return false;
			}

			if ( Math.abs( xVelocity ) > Math.abs( yVelocity ) ) {
				if ( xDiff > 0 ) {
					changeDate( currentDate.tomorrow() );
				}
				else {
					changeDate( currentDate.yesterday() );
				}

				return true;
			}

			return false;
		}

	}


	/**
	 * Task used for rebuilding the UI.
	 */
	private class BuildUIAsyncTask
		extends AsyncTask<Void, Void, Void> {

		private final ZmanimLocation                            location;

		private final LocationError                             error;


		public BuildUIAsyncTask(
			ZmanimLocation  location,
			LocationError   error
		) {

			// In some error cases (where we've never before received a previous location) it could be possible that
			//  the incoming location is null...
			if ( null == location ) {
				this.location = new ZmanimLocation();
			}
			else {
				this.location = location;
			}

			this.error = error;
		}


		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();

			determiningLocationDialog.setMessage( getString( R.string.updating_with_new_location ) );

			// Only show the dialog if we don't already have a location. If we do, then it's OK...we'll update
			//  everything once we've got the new data...
			if ( ( null == locationManager.getCurrentLocation() ) && !determiningLocationDialog.isShowing() ) {
				determiningLocationDialog.show();
			}
		}


		@Override
		protected Void doInBackground( Void... v ) {

			boolean showSeconds = personalPrefsManager.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
			zmanFormatter = new ZmanFormatter( showSeconds );

			currentDate = HebrewCalendar.today();
			hebrewDateFormatter = HebrewDateFormatter.getInstance( HebrewDateFormatter.Style.SHORT );
			secularDateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.DEFAULT );

			return null;
		}


		@Override
		protected void onPostExecute( Void v ) {

			try {
				super.onPostExecute( v );

				if ( isCancelled() ) {
					// I guess someone else is coming along with a new request to build the UI...
					return;
				}
				
				buildHeader( location );
				buildFooter( location, error );

				if ( null == zmanimDisplay ) {
					zmanimDisplay = new ZmanimDisplay( ZmanimActivity.this, zmanFormatter );
				}
			}
			finally {

				// Make sure to clean up!
				determiningLocationDialog.dismiss();
				buildUITask = null;
			}
		}
	}


	private static class DateChangeEventManager
		extends EventManager<DateChangedEvent.Listener, HebrewDate, DateChangedEvent> {

		@Override
		protected void dispatch(
			DateChangedEvent.Listener listener,
			DateChangedEvent event
		) {
			listener.onDateChanged( event );
		}


		@Override
		protected void handleException( Exception e ) {
			LogUtils.logError( "date change dispatch", e.getMessage(), e );
		}
	}

}  // End of ZmanimActivity class
