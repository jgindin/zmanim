/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.eventBus.EventBus;
import com.gindin.mvp.Presenter;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.datePicker.HebrewDateChangedEvent;
import com.gindin.zmanim.android.display.daily.header.DateDisplayModule;
import com.gindin.zmanim.android.display.daily.header.EditLocationPrefsEvent;
import com.gindin.zmanim.android.display.daily.header.LocationDisplayModule;
import com.gindin.zmanim.android.display.daily.main.ZmanimDisplayModule;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.location.resolvers.LocationToAddressConverter;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayer;
import com.gindin.zmanim.android.messageDisplay.MessageDisplayerOneButton;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.PersonalPrefsManager;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.android.prefs.ZmanimSelectionPrefsManager;
import com.gindin.zmanim.android.service.ServiceStartedEvent;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
	extends AppCompatActivity {

	private static final int                                  UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID = 1000;

	private final List<Presenter<?>>                          loadedPresenters = new ArrayList<>();

	/** What date is currently being viewed? */
	private HebrewDate                                        currentDate;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;

	private LocationAcquiredEvent.Handler                     rawLocationAcquiredHandler;

	// TODO: Inject this
	private PersonalPrefsManager                              personalPrefsManager;

	// TODO: Inject this
	private EventBus                                          eventBus;


	/** We use a progress dialog to let the user know we're working. */
	private ProgressDialog                                    determiningLocationDialog;

	// TODO: Inject this
	private MessageDisplayer                                  messageDisplayer = new MessageDisplayer();

	// TODO: Inject this
	private MessageDisplayerOneButton                         messageDisplayerOneButton = new MessageDisplayerOneButton();

	private HebrewDateChangedEvent.Handler                    dateChangedHandler;

	private ZmanimLocation                                    lastKnownLocation;

	private LocationToAddressConverter                        locationToAddressConverter;

	private ServiceStartedEvent.Handler                       serviceStartedHandler;

	private boolean                                           needToScheduleAlarms = true;


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
		determiningLocationDialog.setMessage( getString( R.string.determining_location ) );
		determiningLocationDialog.show();

		eventBus = ZmanimService.ZMANIM_EVENT_BUS;

		// todo: Move to background thread????????
		/////
		// TODO: Move things things into Guice modules, etc....
		SharedPreferences personalPrefs = getSharedPreferences( ZmanimPrefs.PERSONAL.name(), Context.MODE_PRIVATE );

		locationToAddressConverter = new LocationToAddressConverter( this, eventBus, new Geocoder( this ), personalPrefs );

		DateDisplayModule dateDisplayModule = new DateDisplayModule( this, eventBus );
		loadedPresenters.add( dateDisplayModule.create() );

		LocationDisplayModule locationDisplayModule = new LocationDisplayModule( this, eventBus );
		loadedPresenters.add( locationDisplayModule.create() );

		ZmanimDisplayModule zmanimDisplayModule = new ZmanimDisplayModule( this, eventBus, personalPrefsManager);
		loadedPresenters.add( zmanimDisplayModule.create() );

		setupViewListeners();

		dateChangedHandler = new HebrewDateChangeHandler();
		eventBus.addHandler( dateChangedHandler );

		// Now start listening for location changes...
		rawLocationAcquiredHandler = new RawLocationAcquiredHandler();
		eventBus.addHandler( rawLocationAcquiredHandler );

		// Have to get setup for today.
		// TODO:  We should actually be storing the date the user is viewing in the savedInstanceState!
		currentDate = HebrewCalendar.today();
		eventBus.fire( new HebrewDateChangedEvent( currentDate ) );
	}


	@Override
	protected void onRestart() {
		super.onRestart();
	}


	@Override
	protected void onStart() {

		super.onStart();

		serviceStartedHandler = new ServiceStartedEvent.Handler() {
			@Override
			public void onServiceStarted() {
				refresh();
			}
		};

		eventBus.addHandler( serviceStartedHandler );

		Intent zmanimService = new Intent( this, ZmanimService.class );
		zmanimService.setAction( ZmanimService.Actions.ACTIVITY_STARTED.name() );
		zmanimService.putExtra( ZmanimService.ActionParams.SCHEDULE_IF_NECESSARY.name(), needToScheduleAlarms );
		startService( zmanimService );

		needToScheduleAlarms = false;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	protected void onStop() {

		eventBus.removeHandler( serviceStartedHandler );

		Intent zmanimService = new Intent( this, ZmanimService.class );
		zmanimService.setAction( ZmanimService.Actions.ACTIVITY_STOPPED.name() );
		startService( zmanimService );

		super.onStop();
	}


	@Override
	protected void onDestroy() {

		eventBus.removeHandler( dateChangedHandler );
		eventBus.removeHandler( rawLocationAcquiredHandler );

		determiningLocationDialog.dismiss();

		for ( Presenter<?> presenter : loadedPresenters ) {
			presenter.destroy();
		}
		loadedPresenters.clear();

		locationToAddressConverter.onDestroy();
		locationToAddressConverter = null;

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
				refresh();
				break;

			case PERSONAL_PREFERENCS:
				personalPrefsManager.personalPrefsChanged();
				break;
		}
	}


	public void rebuildUI() {
		rebuildUI( lastKnownLocation, null );
	}


	private void rebuildUI(
		ZmanimLocation            location,
		@Nullable LocationError   error
	) {

		if ( !location.isInvalid ) {
			lastKnownLocation = location;
		}

		buildFooter( error );

		// Make sure to clean up!
		determiningLocationDialog.dismiss();
	}


	private void buildFooter(
		@Nullable final LocationError error
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
				return true;

			case R.id.location_prefs:
				eventBus.fire( new EditLocationPrefsEvent() );
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

		SharedPreferences locationManagementPrefs = getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		boolean preferCachedLocation = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_CACHED_LOCATION.name(), true );

		RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage;
		if ( !preferCachedLocation ) {
			cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.NEVER;
		}
		else {
			cachedLocationUsage = RequestLocationUpdateEvent.CachedLocationUsage.PREFER;
		}

		// This will force us to go re-query for our location.
		eventBus.fire( new RequestLocationUpdateEvent( cachedLocationUsage ) );
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
		public void onLocationAcquired(
			final ZmanimLocation  location,
			final boolean         isCachedLocation
		) {
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
			@NotNull final LocationError  error,
			@NotNull final ZmanimLocation lastKnownLocation
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
