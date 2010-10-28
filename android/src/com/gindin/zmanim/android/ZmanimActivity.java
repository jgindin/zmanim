/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanimLocation;
import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.ibm.icu.ICUAndroidBridge;
import net.sourceforge.zmanim.util.GeoLocation;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
	extends ListActivity {

	private static final int                                  UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID = 1000;

	/**
	 * In case there are no location sources enabled, we'll make a request to the intent that will allow the
	 *  user to turn something on.
	 */
	private static final int                                  REQUEST_ENABLE_LOCATION_SOURCES = 0;

	/** The calendar we use for displaying everything. */
	private HebrewZmanimCalendar                              zmanimCalendar;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;

	private HebrewZmanimCalendar.Listener                     zmanimCalendarListener;

	/** The format for the times that we're going to display. */
	@SuppressWarnings( { "FieldCanBeLocal" } )
	private DateFormat                                        timeFormatter;

	/** The format for the secular dates that we're going to display. */
	@SuppressWarnings( { "FieldCanBeLocal" } )
	private DateFormat                                        secularDateFormatter;

	private ZmanimLocationManager                             locationManager;

	private ZmanimLocationManager.Listener                    locationListener;

	/** We use a progress dialog to let the user know we're working. */
	private ProgressDialog                                    determiningLocationDialog;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );

		LogUtils.startSession( this );
		AppUpgrader.handle( this );

		setContentView( R.layout.main );

		determiningLocationDialog = new ProgressDialog( this );
		determiningLocationDialog.setIndeterminate( true );
		determiningLocationDialog.setMessage( "Determining location..." );
		determiningLocationDialog.show();

		locationListener = new ZmanimLocationManager.Listener() {

			public void onProgress( String message ) {
				determiningLocationDialog.setMessage( message );
				if ( !determiningLocationDialog.isShowing() ) {
					determiningLocationDialog.show();
				}
			}


			public void locationChanged( ZmanimLocation newLocation ) {
				rebuildUI( newLocation, null );
			}


			public void error(
				LocationError   error,
				ZmanimLocation  lastKnownLocation
			) {
				rebuildUI( lastKnownLocation, error );
			}
		};

		AsyncTask<Void, Void, Void> initUITask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground( Void... voids ) {

				timeFormatter = android.text.format.DateFormat.getTimeFormat( ZmanimActivity.this );
				secularDateFormatter = android.text.format.DateFormat.getMediumDateFormat( ZmanimActivity.this );

				setupViewListeners();

				ICUAndroidBridge.create( getAssets() );

				locationManager = new ZmanimLocationManager( ZmanimActivity.this );
				LogUtils.setLocationHandler( locationManager );

				return null;
			}


			@Override
			protected void onPostExecute( Void aVoid ) {
				super.onPostExecute( aVoid );

				// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
				// Note that it *must* be run on the UI thread...
				gestureDetector = new GestureDetector( ZmanimActivity.this, new ZmanimGestureListener() );

				// Now start listening for location changes...
				locationManager.addListener( locationListener );
			}
		};
		initUITask.execute( (Void)null );

	}


	// TODO: Need to cleanup in onPause and restart when we get onResume.
	//  See the lifecycle: http://developer.android.com/reference/android/app/Activity.html
	//    onCreate
	//      onRestart
	//      onStart
	//        onResume
	//        onPause
	//      onStop
	//    onDestroy
	//
	// Specifically, http://www.screaming-penguin.com/node/7746 says that if you have an indeterminate dialog showing
	//  it must get cleaned up in the onPause, or else it will leak.

	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onDestroy() {

		locationManager.removeListener( locationListener );
		locationManager.onDestroy();

		cleanupCalendarListeners();

		LogUtils.endSession( this );
		
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(
		int requestCode,
		int resultCode,
		Intent data
	) {

		super.onActivityResult( requestCode, resultCode, data );

		switch ( requestCode ) {

			case REQUEST_ENABLE_LOCATION_SOURCES:
				determiningLocationDialog.setMessage( "Determining location..." );
				determiningLocationDialog.show();
				cleanupCalendarListeners();
				locationManager.onLocationProvidersChanged();
				break;
		}
	}


	private void rebuildUI(
		final ZmanimLocation  location,
	  final LocationError   error
	) {

		determiningLocationDialog.setMessage( "Updating with new location..." );
		if ( !determiningLocationDialog.isShowing() ) {
			determiningLocationDialog.show();
		}

		AsyncTask<Void, Void, Void> buildUITask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground( Void... v ) {

				zmanimCalendar = new HebrewZmanimCalendar( location, timeFormatter, secularDateFormatter );
				return null;
			}


			@Override
			protected void onPostExecute( Void v ) {

				try {
					super.onPostExecute( v );

					buildHeader( location );
					buildFooter( location, error );

					if ( null == location ) {
						zmanimCalendarListener = null;
						return;
					}

					// We just need a display...don't need to hold on to it...
					new ZmanimDisplay( ZmanimActivity.this, zmanimCalendar );

					zmanimCalendarListener = new HebrewZmanimCalendar.Listener() {
						public void dateChanged() {
							updateHeader();
						}
					};
					zmanimCalendar.addListener( zmanimCalendarListener );
				}
				finally {
					determiningLocationDialog.dismiss();
				}

			}
		};

		buildUITask.execute();
	}


	private void buildHeader(
		GeoLocation location
	) {

		// Listen for 'clicks' indicating the user wants to change the date being viewed.
		final View secularDate = findViewById( R.id.secular_date );
		secularDate.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View v) {
					showDialog( R.id.secular_date );
				}
			}
		);

		final String locationName;

		if ( null == location ) {
			locationName = getResources().getString( R.string.unknown_location );
		}
		else {
			locationName = location.getLocationName();
		}

		// Notify the location manager of the field that contains the current location.
		final View locationField = findViewById( R.id.location );
		locationManager.setLocationField( locationField );


		updateField( R.id.location, locationName );

		// Update the header
		updateHeader();
	}


	private void buildFooter(
		ZmanimLocation              location,
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
	private void updateHeader() {

		updateField( R.id.hebrew_date, zmanimCalendar.formatHebrewDate() );
		updateField( R.id.secular_date, Html.fromHtml( "<u>" + zmanimCalendar.formatSecularDate() + "</u>" ) );

		String description = zmanimCalendar.getTodaysDescription();
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


	private void cleanupCalendarListeners() {

		if ( ( null != zmanimCalendar ) && ( null != zmanimCalendarListener ) ) {
			zmanimCalendar.removeListener( zmanimCalendarListener );
		}
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
			case R.id.location_prefs:
				locationManager.editLocationPrefs();
				return true;

			case R.id.about:
				showDialog( id );
				return true;

			case R.id.help:
				showDialog( id );
				return true;
		}

		return false;
	}


	@Override
	protected Dialog onCreateDialog( int dialogID ) {

		switch ( dialogID ) {
			case R.id.secular_date:
				return createSecularDatePickerDialog();

			case R.id.about:
				return createHtmlTextDialog( R.string.about_header, R.layout.about, R.id.about_text, R.string.about_text );

			case R.id.help:
				return createHtmlTextDialog( R.string.help_header, R.layout.help, R.id.help_text, R.string.help_text );

			case UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID:
				return createEnableLocationSourcesDialog();
		}

		return null;
	}


	private Dialog createHtmlTextDialog(
		int   titleResource,
	  int   layoutResource,
	  int   webviewId,
	  int   textId
	) {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( titleResource );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( layoutResource, null );

		WebView helpText = (WebView)layout.findViewById( webviewId );
		final String text = getResources().getString( textId );
		helpText.loadData( text, "text/html", "utf-8" );

		builder.setView( layout );
		return builder.create();
	}


	private Dialog createSecularDatePickerDialog() {

		final DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(
				DatePicker datePicker,
				int year,
				int month,
				int date
			) {

				zmanimCalendar.goToSecularDate( year, month, date );
			}
		};

		Calendar secularDate = zmanimCalendar.getSecularDate();

		return new DatePickerDialog( this, dateChangedListener, secularDate.get( Calendar.YEAR ),
			secularDate.get( Calendar.MONTH ), secularDate.get( Calendar.DAY_OF_MONTH ) );
	}


	private Dialog createEnableLocationSourcesDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( R.string.unable_to_determine_location_header );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.help, null );

		WebView helpText = (WebView)layout.findViewById( R.id.help_text );
		final String text = getResources().getString( R.string.unable_to_determine_location_text );
		helpText.loadData( text, "text/html", "utf-8" );

		builder.setPositiveButton( R.string.unable_to_determine_location_button,
			new DialogInterface.OnClickListener() {
				public void onClick(
					DialogInterface dialogInterface,
					int             buttonId
				) {

					// Launch the correct settings screen for the user. Saw this example from:
					//  http://stackoverflow.com/questions/623225/android-go-to-settings-screen
					startActivityForResult( new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS ),
						REQUEST_ENABLE_LOCATION_SOURCES );
				}
			}
		);

		builder.setView( layout );
		return builder.create();
	}

//	private void displayDebugMessages() {
//
//		List<String> strings= ICUAndroidBridge.get().whatDidWeTryToLoad;
//		String loaded = "";
//		for ( String string : strings ) {
//			loaded = loaded + "\n";
//			loaded = loaded + string;
//		}
//		updateField( R.id.messages, loaded );
//	}


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


	private void moveToNextDay() {

		zmanimCalendar.nextDay();
	}


	private void moveToPrevDay() {

		zmanimCalendar.previousDay();
	}



	private class ZmanimGestureListener
		extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 120;


		@Override
		public boolean onFling(
			MotionEvent startMotionEvent,
			MotionEvent endMotionEvent,
			float xVelocity,
			float yVelocity
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
					moveToNextDay();
				}
				else {
					moveToPrevDay();
				}

				return true;
			}

			return false;
		}

	}

}  // End of ZmanimActivity class
