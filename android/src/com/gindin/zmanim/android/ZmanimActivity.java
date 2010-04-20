/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.gindin.zmanim.calendar.HebrewZmanimCalendar;
import com.ibm.icu.ICUAndroidBridge;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimActivity
	extends ListActivity {

	/** Is the manual location selection stuff ready to go yet? */
	private static final boolean                              ALLOW_MANUAL_LOCATION_CHANGE = false;

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


	@Override
	protected void onStart() {

		super.onStart();
		FlurryAgent.onStartSession( this, "HDK5S96L5E7C638N91S1" );
	}


	@Override
	protected void onStop() {

		FlurryAgent.onEndSession( this );
		super.onStop();
	}


	/** Called when the activity is first created. */
	@SuppressWarnings( { "FeatureEnvy" } )
	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		timeFormatter = android.text.format.DateFormat.getTimeFormat( this );
		secularDateFormatter = android.text.format.DateFormat.getMediumDateFormat( this );

		// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
		gestureDetector = new GestureDetector( this, new ZmanimGestureListener() );
		setupListeners();

		ICUAndroidBridge.create( getAssets() );

		rebuildUI();
	}


	@Override
	protected void onDestroy() {

		cleanupCalendarListeners();

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
				cleanupCalendarListeners();
				rebuildUI();
				break;
		}
	}


	private void rebuildUI() {

		// Figure out where we are, and get a calendar ready to use.
		AndroidGeoLocation location = new AndroidGeoLocation( this );
		zmanimCalendar = new HebrewZmanimCalendar( location, timeFormatter, secularDateFormatter );

		buildHeader( location );
		buildFooter( location );

		// We just need a display...don't need to hold on to it...
		new ZmanimDisplay( this, zmanimCalendar );


		zmanimCalendarListener = new HebrewZmanimCalendar.Listener() {
			public void dateChanged() {
				updateHeader();
			}
		};
		zmanimCalendar.addListener( zmanimCalendarListener );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void buildHeader(
		AndroidGeoLocation location
	) {

		updateField( R.id.location, "Determining Location Name..." );

		AsyncTask<AndroidGeoLocation, Void, String> getLocationTask = new AsyncTask<AndroidGeoLocation, Void, String>() {
			@Override
			protected String doInBackground( AndroidGeoLocation... geoLocations ) {
				return geoLocations[0].getLocationName();
			}

			@Override
			protected void onPostExecute( String locationName ) {
				super.onPostExecute( locationName );

				if ( ALLOW_MANUAL_LOCATION_CHANGE ) {
					updateField( R.id.location, Html.fromHtml( "<u>" + locationName + "</u>" ) );
				}
				else {
					updateField( R.id.location, locationName );
				}
			}
		};

		getLocationTask.execute( location );

		// Listen for 'clicks' indicating the user wants to change the date being viewed.
		final View secularDate = findViewById( R.id.secular_date );
		secularDate.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View v) {
					showDialog( R.id.secular_date );
				}
			}
		);


		// TODO: Listen for 'clicks' indicating the user wants to change their location.
//		final View locationField = findViewById( R.id.location );
//		locationField.setOnClickListener(
//			new View.OnClickListener() {
//				public void onClick( View view ) {
//					showDialog( R.id.location );
//				}
//			}
//		);

		// Update the header
		updateHeader();
	}


	private void buildFooter(
		AndroidGeoLocation location
	) {

		if ( location.isLocationDetermined() ) {
			updateField( R.id.messages, "" );
		}
		else {

			String error = getResources().getString( R.string.unable_to_determine_location_error );

			Toast toast = Toast.makeText( this, error, Toast.LENGTH_LONG );
			toast.show();

			// Now...why is it we couldn't determine our location?
			LocationManager lm = (LocationManager)getSystemService( LOCATION_SERVICE );
			if ( !lm.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) && !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

				// Well, gosh..looks like the user hasn't turned on location management!
				String extra = getResources().getString( R.string.no_location_management );
				error = error + " <u>" + extra + "</u>";

				View messages = findViewById( R.id.messages );
				messages.setOnClickListener(
					new View.OnClickListener() {
						public void onClick( View view ) {

							showDialog( UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID );
						}
					}
				);
			}
			else {

				// Hmmm... Not really sure what's going on here...
			}
			
			updateField( R.id.messages, Html.fromHtml( error + "<br/>" ) );
		}

		//displayDebugMessages();
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


	private void setupListeners() {

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

		// TODO: This is nice to have, but the problem is that we wind up getting confused between scrolling the
		//        list and swiping (to change the date)...
//		View listing = findViewById( android.R.id.list );
//		listing.setOnTouchListener( touchListener );
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

			case R.id.location:
				return createLocationSelectionDialog();

			case R.id.about:
				return createSimpleDialog( R.string.about_header, R.layout.about, R.id.about_text, R.string.about_text );

			case R.id.help:
				return createSimpleDialog( R.string.help_header, R.layout.help, R.id.help_text, R.string.help_text );

			case UNABLE_TO_DETERMINE_LOCATION_DIALOG_ID:
				return createEnableLocationSourcesDialog();
		}

		return null;
	}


	private Dialog createSimpleDialog(
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


	private Dialog createLocationSelectionDialog() {
		// TODO: Just for testing...
		return null;
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


	private void moveToNextMonth() {

		zmanimCalendar.nextMonth();
	}


	private void moveToPrevMonth() {

		zmanimCalendar.previousMonth();
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
			//  (swiping up or down).
			float xDiff = startMotionEvent.getRawX() - endMotionEvent.getRawX();
			float yDiff = startMotionEvent.getRawY() - endMotionEvent.getRawY();

			if ( ( Math.abs( xDiff ) < SWIPE_MIN_DISTANCE ) && ( Math.abs( yDiff ) < SWIPE_MIN_DISTANCE ) ) {
				return false;
			}

			if ( Math.abs( xVelocity ) > Math.abs( yVelocity ) ) {
				if ( xDiff > 0 ) {
					moveToNextDay();
				}
				else {
					moveToPrevDay();
				}
			}
			else {
				if ( yDiff > 0 ) {
					moveToNextMonth();
				}
				else {
					moveToPrevMonth();
				}
			}

			return true;
		}

	}

}  // End of ZmanimActivity class
