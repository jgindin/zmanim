package com.gindin.android.zmanim;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import il.ac.technion.math.Taarich;
import net.sourceforge.zmanim.Calendar.ComplexZmanimCalendar;
import net.sourceforge.zmanim.Calendar.ZmanimCalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class ZmanimDisplay
	extends Activity {

	private DateFormat timeFormatter;

	private DateFormat secularDateFormatter;


	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(
		Bundle savedInstanceState
	) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		timeFormatter = android.text.format.DateFormat.getTimeFormat( this );
		secularDateFormatter = android.text.format.DateFormat.getDateFormat( this );

		// Figure out where we are, and get a calendar ready to use.
		AndroidGeoLocation geoLocation = new AndroidGeoLocation( this );
		ComplexZmanimCalendar zmanimCalendar = new ComplexZmanimCalendar( geoLocation );

		// Update the header
		updateField( R.id.hebrew_date, getHebrewDate( zmanimCalendar ) );
		updateField( R.id.secular_date, secularDateFormatter.format( zmanimCalendar.getCalendar().getTime() ) );
		updateField( R.id.location, geoLocation.getLocationName() );

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
		updateFieldWithTime( R.id.shkiah, zmanimCalendar.getSunset() );
		updateFieldWithTime( R.id.tzait, zmanimCalendar.getTzais() );
	}


	private String getHebrewDate(
		ZmanimCalendar calendar
	) {

		Calendar secularCalendar = calendar.getCalendar();

		int date = secularCalendar.get( Calendar.DATE );
		int month = secularCalendar.get( Calendar.MONTH ) + 1;
		int year = secularCalendar.get( Calendar.YEAR );

		// If we're past sunset, then it's tomorrow (for the Hebrew date)
		Date now = secularCalendar.getTime();

		if ( now.after( calendar.getSunset() ) ) {
			date++;
		}

		Taarich taarich = new Taarich( date, month, year );
		return taarich.toString();
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


}  // End of ZmanimDisplay class
