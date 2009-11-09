package com.gindin.zmanim.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.gindin.zmanim.HebrewDateFormatter;
import com.gindin.zmanim.HebrewDateUtils;
import com.ibm.icu.ICUAndroidBridge;
import com.ibm.icu.util.HebrewCalendar;
import net.sourceforge.zmanim.ComplexZmanimCalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class ZmanimDisplay
	extends Activity {

	/**
	 * How many minutes after sunset can you still start mincha?
	 * <p/>
	 * Daily Halacha (http://www.dailyhalacha.com/Display.asp?ClipDate=6/29/2004): The latest time for mincha is: "The Ben
	 * Ish Chai says in one place that you should not pray the Amidah if you start the Amidah at the 13th minute [after
	 * sunset]. But in another place he says that you can start praying the Amidah so long as the majority of the Amidah is
	 * said before the 13.5 minute mark. But Chacham Ovadia's opinion is different. He says it would be permissible to
	 * start the Amidah at 13 minutes after sunset even if the majority is said after the 13.5 minute mark. Chacham Ovadia
	 * says it would be permissible so long as you start within the time. So technically, it would be permissible if you
	 * start the Amidah in Mincha even as late as 13 minutes and 25 seconds after sunset."
	 * <p/>
	 * NOTE: In order to be careful and avoid rounding issues, I'm NOT including the 25 seconds.
	 */
	private static final int                                  MINUTES_AFTER_SUNSET_FOR_MINCHA = 13;

	/**
	 * Used for our secular -> Hebrew date conversion. Cannot be final, because we have to get the ICUAndroidBridg
	 *  configured before we can instantiate.
	 */
	private HebrewCalendar                                    hebrewCalendar;

	private HebrewDateUtils                                   hebrewDateUtils;

	/** The calendar we use for displaying everything. */
	// TODO: Subclass to add earliest & latest mincha times directly, in a manner similar to the other times.
	private ComplexZmanimCalendar                             zmanimCalendar;

	/** Where are we? */
	private AndroidGeoLocation                                geoLocation;

	/** Used to format the zmanim times that get displayed. */
	private DateFormat                                        timeFormatter;

	/** Used to format the secular date in the screen's header. */
	private DateFormat                                        secularDateFormatter;

	/** We want to detect swiping. */
	private GestureDetector                                   gestureDetector;



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

		hebrewCalendar = new HebrewCalendar();
		hebrewDateUtils = new HebrewDateUtils( hebrewCalendar );		

		timeFormatter = android.text.format.DateFormat.getTimeFormat( this );
		secularDateFormatter = android.text.format.DateFormat.getDateFormat( this );

		// Figure out where we are, and get a calendar ready to use.
		geoLocation = new AndroidGeoLocation( this );
		zmanimCalendar = new ComplexZmanimCalendar( geoLocation );
		showZmanim();

		displayDebugMessages();
	}


	@Override
	public boolean onTouchEvent( MotionEvent event ) {
		return gestureDetector.onTouchEvent( event );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void showZmanim() {

		// Update the header
		updateField( R.id.hebrew_date, getHebrewDate() );
		updateField( R.id.secular_date, secularDateFormatter.format( zmanimCalendar.getCalendar().getTime() ) );
		updateField( R.id.location, geoLocation.getLocationName() );
		updateField( R.id.holiday, hebrewDateUtils.getTodaysHoliday() );

		updateFieldWithTime( R.id.alot, zmanimCalendar.getAlos16Point1Degrees() );
		updateFieldWithTime( R.id.talit, zmanimCalendar.getMisheyakir11Point5Degrees() );
		updateFieldWithTime( R.id.hanetz, zmanimCalendar.getSunrise() );
		updateFieldWithTime( R.id.shemaMGA, zmanimCalendar.getSofZmanShmaMGA() );
		updateFieldWithTime( R.id.tefilahMGA, zmanimCalendar.getSofZmanTfilaMGA() );
		updateFieldWithTime( R.id.tefilahGRA, zmanimCalendar.getSofZmanTfilaGRA() );
		updateFieldWithTime( R.id.chatzot, zmanimCalendar.getChatzos() );
		updateFieldWithTime( R.id.mincha_gedola, zmanimCalendar.getMinchaGedola() );
		updateFieldWithTime( R.id.mincha_kitana, zmanimCalendar.getMinchaKetana() );
		updateFieldWithTime( R.id.plag_hamincha, zmanimCalendar.getPlagHamincha() );

		if ( hebrewDateUtils.showCandlelighting() ) {
			updateFieldWithTime( R.id.candles, zmanimCalendar.getCandelLighting() );
		}
		else {
			findViewById( R.id.candles_row ).setVisibility( View.GONE );
		}

		updateFieldWithTime( R.id.shkiah, zmanimCalendar.getSunset() );

		if ( hebrewDateUtils.showHavdalah() ) {
		}
		else {
			findViewById( R.id.havdalah_row ).setVisibility( View.GONE );
		}


		Calendar latestMincha = Calendar.getInstance();
		latestMincha.setTime( zmanimCalendar.getSunset() );
		latestMincha.add( Calendar.MINUTE, MINUTES_AFTER_SUNSET_FOR_MINCHA );
		updateFieldWithTime( R.id.mincha, latestMincha.getTime() );

		updateFieldWithTime( R.id.tzait, zmanimCalendar.getTzais() );
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


	private String getHebrewDate() {

		Calendar secularCalendar = zmanimCalendar.getCalendar();
		hebrewCalendar.setTime( secularCalendar.getTime() );

		// If we're past sunset, then it's tomorrow (for the Hebrew date)
		Date now = secularCalendar.getTime();
		if ( now.after( zmanimCalendar.getSunset() ) ) {
			hebrewCalendar.add( Calendar.DAY_OF_MONTH, 1 );
		}

		return HebrewDateFormatter.format( hebrewCalendar );
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

		updateField( field, timeFormatter.format( zman ) );
	}


	private void moveToNextDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, 1 );

		zmanimCalendar.setCalendar( calendar );
		showZmanim();
	}


	private void moveToPrevDay() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.DATE, -1 );

		zmanimCalendar.setCalendar( calendar );
		showZmanim();
	}


	private void moveToNextMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, 1 );

		zmanimCalendar.setCalendar( calendar );
		showZmanim();
	}


	private void moveToPrevMonth() {

		final Calendar calendar = zmanimCalendar.getCalendar();
		calendar.add( Calendar.MONTH, -1 );

		zmanimCalendar.setCalendar( calendar );
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
