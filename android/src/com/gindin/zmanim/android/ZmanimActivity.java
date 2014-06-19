/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.BaseAsyncClass;
import com.gindin.util.LogUtils;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.display.daily.DateDisplayModule;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayer;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayerOneButton;
import com.gindin.zmanim.android.prefs.*;
import com.gindin.zmanim.android.zmanList.ZmanimDisplay;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import com.gindin.zmanim.zman.ZmanFormatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
	extends FragmentActivity {

	private static final int                                  UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID = 1000;

	/** What date is currently being viewed? */
	private HebrewDate                                        currentDate;

	/** Controls how we format the zmanim. */
	// TODO: Inject this
	private ZmanFormatter                                     zmanFormatter;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;

	// TODO: Inject this
	private ZmanimLocationManager                             locationManager;

	private LocationAcquiredEvent.Handler                     rawLocationAcquiredHandler;

	// TODO: Inject this
	private PersonalPrefsManager                              personalPrefsManager;

	// TODO: Inject this
	private EventBus                                          eventBus;


	/** We use a progress dialog to let the user know we're working. */
	private ProgressDialog                                    determiningLocationDialog;

	/**
	 * Task used to build the UI. While we're in the process of (re)building the UI, this will be set. As soon as
	 *  we're done, it's reset to null.
	 */
	private AsyncTask<LocationError, Void, LocationError>     buildUITask = null;

	private ZmanimDisplay                                     zmanimDisplay;

	/** The actual list view where the zmanim are displayed. Cached here to avoid multiple look ups since it should never change. */
	private ListView                                          listView;

	// TODO: Inject this
	private MessageDisplayer                                  messageDisplayer = new MessageDisplayer();

	// TODO: Inject this
	private MessageDisplayerOneButton                         messageDisplayerOneButton = new MessageDisplayerOneButton();

	private HebrewDateChangedEvent.Handler                    dateChangedHandler;

	private ZmanimLocation                                    lastKnownLocation;

	private LocationToAddressConverter                        locationToAddressConverter;

	/** When the user clicks on the location, they're indicating they want to change it. */
	private final View.OnClickListener                        locationClickListener = new View.OnClickListener() {
		public void onClick( View view ) {
			editLocationProviderPrefs();
		}
	};


	/** When the user long-clicks on the location, they're indicating they want to see some details about it. */
	private final View.OnLongClickListener                    locationLongClickListener = new View.OnLongClickListener() {
		public boolean onLongClick( View view ) {
			showLocationDetails();
			return true;
		}
	};


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

		personalPrefsManager = new PersonalPrefsManager( this );

		AppUpgrader.handle( this, personalPrefsManager, new ZmanimSelectionPrefsManager( this ) );

		setContentView( R.layout.main );

		determiningLocationDialog = new ProgressDialog( this );
		determiningLocationDialog.setIndeterminate( true );
		determiningLocationDialog.setMessage( "Determining location..." );
		determiningLocationDialog.show();

		eventBus = new EventBus();

		// todo: Move to background thread????????
		locationToAddressConverter = new LocationToAddressConverter( new Geocoder( this ), eventBus );

		/////
		// TODO: Move things things into Guice modules, etc....
		DateDisplayModule dateDisplayModule = new DateDisplayModule( ZmanimActivity.this, eventBus );
		dateDisplayModule.create();

		SharedPreferences sharedPreferences = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		LocationManager systemLocationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );
		locationManager = new ZmanimLocationManager( eventBus, sharedPreferences, systemLocationManager );

		setupViewListeners();

		dateChangedHandler = new HebrewDateChangeHandler();

		// Now start listening for location changes...
		rawLocationAcquiredHandler = new RawLocationAcquiredHandler();

		locationManager.determineLocation();

		// Let the location manager know about the field that contains the current location.
		final View locationField = findViewById( R.id.location );
		locationField.setOnClickListener( locationClickListener );
		locationField.setOnLongClickListener( locationLongClickListener );
	}


	@Override
	protected void onRestart() {
		super.onRestart();
	}


	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onResume() {
		super.onResume();

		// NOTE: We do NOT want any catchup events...that will throw us off, and have us off acquiring
		//  locations again when we don't want to...
		eventBus.addHandler( dateChangedHandler );
		eventBus.addHandler( rawLocationAcquiredHandler );
	}


	@Override
	protected void onPause() {
		super.onPause();

		eventBus.removeHandler( dateChangedHandler );
		eventBus.removeHandler( rawLocationAcquiredHandler );
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	protected void onStop() {

		// Might be null if we get stopped before we're fully started...
		if ( null != locationManager ) {
			locationManager.cancel();
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

		eventBus.removeHandler( dateChangedHandler );

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
				locationManager.determineLocation();
				break;

			case EDIT_LOCATION_PREFERENCES:
				if ( resultCode == RESULT_OK ) {
					locationManager.determineLocation();
				}
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
		rebuildUI( lastKnownLocation, null );
	}


	private void rebuildUI(
		ZmanimLocation  location,
	  LocationError   error
	) {

		lastKnownLocation = location;

		if ( null != buildUITask ) {
			buildUITask.cancel( false );
		}

		BaseAsyncClass.Callbacks<LocationError, Void, LocationError> callbacks = new BaseAsyncClass.Callbacks<LocationError, Void, LocationError>() {

			@Override protected void onPreExecute() {
				super.onPreExecute();
				determiningLocationDialog.setMessage( getString( R.string.updating_with_new_location ) );

				// Only show the dialog if we don't already have a location. If we do, then it's OK...we'll update
				//  everything once we've got the new data...
				if ( ( null == lastKnownLocation ) && !determiningLocationDialog.isShowing() ) {
					determiningLocationDialog.show();
				}
			}


			@Override protected LocationError doInBackground(
				BaseAsyncClass.ProgressPublisher<Void>  publisher,
				LocationError...                        params
			) {
				boolean showSeconds = personalPrefsManager.getBoolean( PersonalPreferences.Prefs.SHOW_SECONDS.name(), false );
				zmanFormatter = new ZmanFormatter( showSeconds );

				// Get the date here, but do NOT fire the event...we're on a background thread and that might not be good.
				currentDate = HebrewCalendar.today();

				return params[0];
			}


			@Override protected void onPostExecute( LocationError error ) {
				super.onPostExecute( error );
				try {
					super.onPostExecute( error );

					buildFooter( error );

					// Now go ahead and fire the date change event.
					eventBus.fire( new HebrewDateChangedEvent( currentDate ) );

					if ( null == zmanimDisplay ) {
						zmanimDisplay = new ZmanimDisplay( ZmanimActivity.this, eventBus, zmanFormatter );
					}
					else {
						zmanimDisplay.setZmanFormatter( zmanFormatter );
					}
				}
				finally {
					// Make sure to clean up!
					determiningLocationDialog.dismiss();
					buildUITask = null;
				}
			}
		};

		buildUITask = new BaseAsyncClass<LocationError, Void, LocationError>( callbacks );
		buildUITask.execute( error );
	}


	private void buildFooter(
		final LocationError error
	) {

		if ( null == error ) {
			updateField( R.id.messages, "" );
			//displayDebugMessages();
		}
		else {
			handleLocationError( error );
		}
	}


	private void handleLocationError(
		LocationError error
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

				if ( null != lastKnownLocation ) {
					extra += " " + getResources().getString( R.string.last_location_time, new Date( lastKnownLocation.locationTime ) );
				}

				// TODO: Need to add click listener that will invoke a dialog allowing the user to:
				//  1. try obtaining location again; or
				//  2. manually specify where they are
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

		// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
		// Note that it *must* be run on the UI thread...
		gestureDetector = new GestureDetector( ZmanimActivity.this, new ZmanimGestureListener() );
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
				editLocationProviderPrefs();
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
			case R.id.about:
			case R.id.help:
				return messageDisplayer.getInstance( this );

			case UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID:
				return messageDisplayerOneButton.getInstance( this );
		}

		return null;
	}


	@Override
	protected void onPrepareDialog(
		int     dialogID,
		Dialog  dialog
	) {
		super.onPrepareDialog( dialogID, dialog );

		switch ( dialogID ) {
			case R.id.about:
				messageDisplayer.update( R.string.about_header, "com/gindin/zmanim/android/about.html" );
				break;

			case R.id.help:
				messageDisplayer.update( R.string.help_header, "com/gindin/zmanim/android/help.html" );
				break;

			case UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID:
			  messageDisplayerOneButton.update( R.string.unable_to_determine_location_header,
				  "com/gindin/zmanim/android/enable_locations.html", R.string.unable_to_determine_location_button,
				  new DialogInterface.OnClickListener() {
					  @Override public void onClick( DialogInterface dialog, int button ) {
							// Launch the correct settings screen for the user. Saw this example from:
							//  http://stackoverflow.com/questions/623225/android-go-to-settings-screen
							startActivityForResult( new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS ),
								Activities.ENABLE_LOCATION_SERVICES.ordinal() );
						}
					}
			  );

			default:
				// nothing for the other dialogs.
		}
	}


	private void refresh() {
		// This will force us to go re-query for our location.
		locationManager.determineLocation();
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


	private void editLocationProviderPrefs() {
		startActivityForResult( new Intent( this, LocationProviderPreferences.class ),
			Activities.EDIT_LOCATION_PREFERENCES.ordinal() );
	}


	private void showLocationDetails() {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( R.string.current_location_title );

		String locationDetails;
		if ( lastKnownLocation == null ) {
			locationDetails = getResources().getString( R.string.unable_to_determine_location_error );
		}
		else {
			locationDetails = lastKnownLocation.toString();
		}

		builder.setMessage( locationDetails );

		final AlertDialog dialog = builder.create();
		dialog.show();
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
				HebrewDateChangedEvent event;
				if ( xDiff > 0 ) {
					event = new HebrewDateChangedEvent( currentDate.tomorrow() );
				}
				else {
					event = new HebrewDateChangedEvent( currentDate.yesterday() );
				}

				eventBus.fire( event );
				return true;
			}

			return false;
		}

	}



	private class HebrewDateChangeHandler
		extends HebrewDateChangedEvent.Handler {

		@Override
		public void onHebrewDateChanged( HebrewDate newDate ) {
			currentDate = newDate;
		}
	}


	private class RawLocationAcquiredHandler
		extends LocationAcquiredEvent.Handler {

		@Override
		public void onLocationAcquired( final ZmanimLocation location ) {
			runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						rebuildUI( location, null );
					}
				}
			);
		}


		@Override
		public void onError(
			final LocationError   error,
			final ZmanimLocation  lastKnownLocation
		) {

			runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						rebuildUI( lastKnownLocation, error );
					}
				}
			);
		}
	}

}  // End of ZmanimActivity class
