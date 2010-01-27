/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.gindin.zmanim.HebrewZmanimCalendar;
import com.gindin.zmanim.ZmanRounder;
import com.ibm.icu.ICUAndroidBridge;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Entry point to the Android application which displays zmanim.
 */
public class ZmanimDisplay
	extends Activity {

	/** Is the manual location selection stuff ready to go yet? */
	private static final boolean                              ALLOW_MANUAL_LOCATION_CHANGE = false;

	/** Message when we can't figure out where the user is. */
	private static final String                               UNABLE_TO_DETERMINE_CURRENT_LOCATION = "Unable to determine your current location.";
//	private static final String                               UNABLE_TO_DETERMINE_CURRENT_LOCATION = "Unable to determine your current location. Please click on the location heading to set where you are.";

	/** The calendar we use for displaying everything. */
	private HebrewZmanimCalendar                              zmanimCalendar;

	/** When we need to round a zman up or down, this is the guy to do it. */
	private final ZmanRounder                                 zmanRounder = new ZmanRounder();

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;



	public ZmanimDisplay() {
	}


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

		// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
		gestureDetector = new GestureDetector( this, new ZmanimGestureListener() );
		setupListeners();


		ICUAndroidBridge.create( getAssets() );

		DateFormat timeFormatter = android.text.format.DateFormat.getTimeFormat( this );
		DateFormat secularDateFormatter = android.text.format.DateFormat.getMediumDateFormat( this );

		// Figure out where we are, and get a calendar ready to use.
		AndroidGeoLocation location = new AndroidGeoLocation( this );
		zmanimCalendar = new HebrewZmanimCalendar( location, timeFormatter, secularDateFormatter );

		if ( ALLOW_MANUAL_LOCATION_CHANGE ) {
			updateField( R.id.location, Html.fromHtml( "<u>" + location.getLocationName() + "</u>" ) );
		}
		else {
			updateField( R.id.location, location.getLocationName() );
		}
		
		showZmanim();

		if ( !location.isLocationDetermined() ) {
			Toast toast = Toast.makeText( this, UNABLE_TO_DETERMINE_CURRENT_LOCATION, Toast.LENGTH_LONG );
			toast.show();

			updateField( R.id.messages, UNABLE_TO_DETERMINE_CURRENT_LOCATION );
		}

		displayDebugMessages();
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


		// Listen for 'clicks' indicating the user wants to change the date being viewed.
		final View secularDate = findViewById( R.id.secular_date );
		secularDate.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View v) {
					showDialog( R.id.secular_date );
				}
			}
		);


		// Listen for 'clicks' indicating the user wants to change their location.
		final View location = findViewById( R.id.location );
		location.setOnClickListener(
			new View.OnClickListener() {
				public void onClick( View view ) {
					showDialog( R.id.location );
				}
			}
		);
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
				return createSimpleDialog( R.string.about_header, R.layout.about );

			case R.id.help:
				return createHelpDialog();
		}

		return null;
	}


	private Dialog createSimpleDialog(
		int   titleResource,
	  int   layoutResource
	) {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( titleResource );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( layoutResource, (ViewGroup)findViewById( R.id.layout_root ) );
		builder.setView( layout );

		return builder.create();
	}


	private Dialog createHelpDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( R.string.help_header );

		LayoutInflater inflater = (LayoutInflater)getSystemService( LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate( R.layout.help, (ViewGroup)findViewById( R.id.layout_root ) );

		WebView helpText = (WebView)layout.findViewById( R.id.help_text );
		helpText.loadData( getResources().getString( R.string.help_text ), "text/html", "utf-8" );

		builder.setView( layout );
		return builder.create();
	}


	private Dialog createLocationSelectionDialog() {
		// TODO: Just for testing...
		return createSimpleDialog( R.string.about_header, R.layout.about );
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
				showZmanim();
			}
		};

		Calendar secularDate = zmanimCalendar.getSecularDate();

		return new DatePickerDialog( this, dateChangedListener, secularDate.get( Calendar.YEAR ),
			secularDate.get( Calendar.MONTH ), secularDate.get( Calendar.DAY_OF_MONTH ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void showZmanim() {

		String description = zmanimCalendar.getTodaysDescription();

		// Update the header
		updateField( R.id.hebrew_date, zmanimCalendar.formatHebrewDate() );
		updateField( R.id.secular_date, Html.fromHtml( "<u>" + zmanimCalendar.formatSecularDate() + "</u>" ) );
		updateField( R.id.holiday, description );

		updateFieldWithTime( R.id.alot, zmanimCalendar.getAlot() );
		updateFieldWithTime( R.id.talit, zmanRounder.roundToLatestMinute( zmanimCalendar.getTalit() ) );
		updateFieldWithTime( R.id.hanetz, zmanimCalendar.getHanetz() );
		updateFieldWithTime( R.id.shemaMGA, zmanRounder.roundToEarliestMinute( zmanimCalendar.getSofZmanShma() ) );
		updateFieldWithTime( R.id.tefilahMGA, zmanRounder.roundToEarliestMinute( zmanimCalendar.getSofZmanTefila() ) );
		updateFieldWithTime( R.id.chatzot, zmanimCalendar.getChatzot() );
		updateFieldWithTime( R.id.mincha_gedola, zmanimCalendar.getMinchaGedola() );
		updateFieldWithTime( R.id.mincha_ketana, zmanimCalendar.getMinchaKetana() );
		updateFieldWithTime( R.id.plag_hamincha, zmanimCalendar.getPlagHamincha() );

		if ( zmanimCalendar.showCandlelighting() ) {
			findViewById( R.id.candles_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.candles, zmanRounder.roundToEarliestMinute( zmanimCalendar.getCandlelighting() ) );
		}
		else {
			findViewById( R.id.candles_row ).setVisibility( View.GONE );
		}

		updateFieldWithTime( R.id.shkiah, zmanimCalendar.getShkiah() );

		updateFieldWithTime( R.id.mincha, zmanRounder.roundToEarliestMinute( zmanimCalendar.getLatestMincha() ) );


		if ( zmanimCalendar.showHavdalah() ) {
			findViewById( R.id.tzait_row ).setVisibility( View.GONE );
			findViewById( R.id.havdalah_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.havdalah, zmanRounder.roundToLatestMinute( zmanimCalendar.getTzaitHachocavim() ) );
		}
		else {
			findViewById( R.id.havdalah_row ).setVisibility( View.GONE );
			findViewById( R.id.tzait_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.tzait, zmanRounder.roundToLatestMinute( zmanimCalendar.getTzaitHachocavim() ) );
		}

	}


	private void displayDebugMessages() {

//		List<String> strings= ICUAndroidBridge.get().whatDidWeTryToLoad;
//		String loaded = "";
//		for ( String string : strings ) {
//			loaded = loaded + "\n";
//			loaded = loaded + string;
//		}
//		updateField( R.id.messages, loaded );
	}


	private void updateField(
		int     field,
		String  text
	) {

		TextView view = (TextView)findViewById( field );
		view.setText( text );
	}


	private void updateFieldWithTime(
		int   field,
		Date  zman
	) {

		updateField( field, zmanimCalendar.formatZmanTime( zman ) );
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
		showZmanim();
	}


	private void moveToPrevDay() {

		zmanimCalendar.previousDay();
		showZmanim();
	}


	private void moveToNextMonth() {

		zmanimCalendar.nextMonth();
		showZmanim();
	}


	private void moveToPrevMonth() {

		zmanimCalendar.previousMonth();
		showZmanim();
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

}  // End of ZmanimDisplay class
