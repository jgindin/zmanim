/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import com.gindin.zmanim.HebrewDateFormatter;
import com.gindin.zmanim.HebrewZmanimCalendar;
import com.gindin.zmanim.JewishHoliday;
import com.ibm.icu.ICUAndroidBridge;
import net.sourceforge.zmanim.util.GeoLocation;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 */
public class ZmanimDisplay
	extends Activity {


	/** The calendar we use for displaying everything. */
	private HebrewZmanimCalendar                              zmanimCalendar;

	/** Used to format all of the dates and times. */
	private HebrewDateFormatter                               dateFormatter;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;


	public ZmanimDisplay() {
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		// How to make this gesture stuff work? See http://stackoverflow.com/questions/937313/android-basic-gesture-detection
		gestureDetector = new GestureDetector( this, new ZmanimGestureListener() );
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

		ICUAndroidBridge.create( getAssets() );

		DateFormat timeFormatter = android.text.format.DateFormat.getTimeFormat( this );
		DateFormat secularDateFormatter = android.text.format.DateFormat.getMediumDateFormat( this );
		dateFormatter = new HebrewDateFormatter( timeFormatter, secularDateFormatter );

		// Figure out where we are, and get a calendar ready to use.
		GeoLocation location = new AndroidGeoLocation( this );
		zmanimCalendar = new HebrewZmanimCalendar( location );
		updateField( R.id.location, location.getLocationName() );
		showZmanim();

		displayDebugMessages();
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
				AboutDialog about = new AboutDialog( this );
				about.show();
				return true;

			case R.id.help:
				HelpDialog help = new HelpDialog( this );
				help.show();
				return true;
		}

		return false;
	}



	@SuppressWarnings( { "FeatureEnvy" } )
	private void showZmanim() {

		StringBuffer holidayDescription = new StringBuffer();
		JewishHoliday holiday = zmanimCalendar.isTodayAHoliday();
		if ( null != holiday ) {
			holidayDescription.append( holiday.name );
		}

		holiday = zmanimCalendar.isRoshChodesh();
		if ( null != holiday ) {
			if ( 0 == holidayDescription.length() ) {
				holidayDescription.append( holiday.name );
			}
			else {
				holidayDescription.append( " / " ).append( holiday.name );
			}
		}


		// Update the header
		updateField( R.id.hebrew_date, dateFormatter.formatHebrewDate( zmanimCalendar ) );
		updateField( R.id.secular_date, dateFormatter.formatSecularDate( zmanimCalendar ) );
		updateField( R.id.holiday, holidayDescription.toString() );

		updateFieldWithTime( R.id.alot, zmanimCalendar.getAlot() );
		updateFieldWithTime( R.id.talit, zmanimCalendar.getTalit() );
		updateFieldWithTime( R.id.hanetz, zmanimCalendar.getHanetz() );
		updateFieldWithTime( R.id.shemaMGA, zmanimCalendar.getSofZmanShma() );
		updateFieldWithTime( R.id.tefilahMGA, zmanimCalendar.getSofZmanTefila() );
		updateFieldWithTime( R.id.chatzot, zmanimCalendar.getChatzot() );
		updateFieldWithTime( R.id.mincha_gedola, zmanimCalendar.getMinchaGedola() );
		updateFieldWithTime( R.id.mincha_ketana, zmanimCalendar.getMinchaKetana() );
		updateFieldWithTime( R.id.plag_hamincha, zmanimCalendar.getPlagHamincha() );

		if ( zmanimCalendar.showCandlelighting() ) {
			findViewById( R.id.candles_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.candles, zmanimCalendar.getCandlelighting() );
		}
		else {
			findViewById( R.id.candles_row ).setVisibility( View.GONE );
		}

		updateFieldWithTime( R.id.shkiah, zmanimCalendar.getShkiah() );

		updateFieldWithTime( R.id.mincha, zmanimCalendar.getLatestMincha() );


		if ( zmanimCalendar.showHavdalah() ) {
			findViewById( R.id.tzait_row ).setVisibility( View.GONE );
			findViewById( R.id.havdalah_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.havdalah, zmanimCalendar.getTzaitHachocavim() );
		}
		else {
			findViewById( R.id.havdalah_row ).setVisibility( View.GONE );
			findViewById( R.id.tzait_row ).setVisibility( View.VISIBLE );
			updateFieldWithTime( R.id.tzait, zmanimCalendar.getTzaitHachocavim() );
		}

	}


	private void displayDebugMessages() {

//		List<String> strings= ICUAndroidBridge.get().whatDidWeTryToLoad;
//		String loaded = "";
//		for ( String string : strings ) {
//			loaded = loaded + "\n";
//			loaded = loaded + string;
//		}
//		updateField( R.id.debug, loaded );

		findViewById( R.id.debug_row ).setVisibility( View.GONE );
	}


	private void updateField(
		int field,
		String text
	) {

		TextView view = (TextView)findViewById( field );
		view.setText( text );
	}


	private void updateFieldWithTime(
		int field,
		Date zman
	) {

		updateField( field, dateFormatter.formatZmanTime( zman ) );
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
