package com.gindin.zmanim.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import il.ac.technion.math.Taarich;
import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.ZmanimCalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class ZmanimDisplay
	extends Activity {

	/**
	 * Used to format the times that get displayed.
	 */
	private DateFormat timeFormatter;


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
		DateFormat secularDateFormatter = android.text.format.DateFormat.getDateFormat( this );

		// Figure out where we are, and get a calendar ready to use.
		AndroidGeoLocation geoLocation = new AndroidGeoLocation( this );
		ComplexZmanimCalendar zmanimCalendar = new ComplexZmanimCalendar( geoLocation );

		// Update the header
		final Taarich taarich = getHebrewDate( zmanimCalendar );
		updateField( R.id.hebrew_date, taarich.toString() );
		updateField( R.id.secular_date, secularDateFormatter.format( zmanimCalendar.getCalendar().getTime() ) );
		updateField( R.id.location, geoLocation.getLocationName() );
		updateField( R.id.holiday, getTodaysHoliday( taarich ) );

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

		// TODO: Only show this if it's Friday or erev a holiday...
		updateFieldWithTime( R.id.candles, zmanimCalendar.getCandelLighting() );
		updateFieldWithTime( R.id.shkiah, zmanimCalendar.getSunset() );

		// Daily Halacha (http://www.dailyhalacha.com/Display.asp?ClipDate=6/29/2004): The latest time for
		//  mincha is:
		//  "The Ben Ish Chai says in one place that you should not pray the Amidah if you start the Amidah at the
		//    13th minute [after sunset]. But in another place he says that you can start praying the Amidah so long
		//    as the majority of the Amidah is said before the 13.5 minute mark. But Chacham Ovadia’s opinion is
		//    different. He says it would be permissible to start the Amidah at 13 minutes after sunset even if the
		//    majority is said after the 13.5 minute mark. Chacham Ovadia says it would be permissible so long as
		//    you start within the time. So technically, it would be permissible if you start the Amidah in Mincha
		//    even as late as 13 minutes and 25 seconds after sunset."
		//  NOTE: In order to be careful and avoid rounding issues, I'm NOT including the 25 seconds.
		Calendar latestMincha = Calendar.getInstance();
		latestMincha.setTime( zmanimCalendar.getSunset() );
		latestMincha.add( Calendar.MINUTE, 13 );
		updateFieldWithTime( R.id.mincha, latestMincha.getTime() );

		updateFieldWithTime( R.id.tzait, zmanimCalendar.getTzais() );
	}


	private String getTodaysHoliday(
		Taarich taarich
	) {

		switch ( taarich.getHebrewMonth() ) {
			case NISAN:
				return todayInNisan( taarich );

			case IYYAR:
				return todayInIyyar( taarich );

			case SIVAN:
				return todayInSivan( taarich );

			case TAMMUZ:
				return todayInTammuz( taarich );

			case AV:
				return todayInAv( taarich );

			case ELUL:
				return todayInElul( taarich );

			case TISHRI:
				return todayInTishri( taarich );

			case HESHVAN:
				return todayInHeshvan( taarich );

			case KISLEV:
				return todayInKislev( taarich );

			case TEVET:
				return todayInTevet( taarich );

			case SHEVAT:
				return todayInShevat( taarich );

			case ADAR:
			case ADAR_II:
				return todayInAdar( taarich );

			case ADAR_I:
				return todayInAdarI( taarich );
		}

		return "";
	}


	private String todayInNisan(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 14:
				return "Taanich Bechorot";

			case 15:
				return "Pesach 1";

			case 16:
				return "Pesach 2";

			case 17:
				return "Pesach 3";

			case 18:
				return "Pesach 4";

			case 19:
				return "Pesach 5";

			case 20:
				return "Pesach 6";

			case 21:
				return "Pesach 7";

			case 22:
				return "Isru Chag";
		}

		return "";
	}


	private String todayInIyyar(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 14:
				return "Pesach Sheni";

			case 18:
				return "Lag Ba'Omer";
		}

		return "";
	}


	private String todayInSivan(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 6:
				return "Shavuot";

			case 7:
				return "Isru Chag";
		}

		return "";
	}


	private String todayInTammuz(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 17:
				return "Fast of Tammuz";
		}

		return "";
	}


	private String todayInAv(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 9:
				return "Tisha B'Av";

			case 15:
				return "Tu B'Av";
		}

		return "";
	}


	private String todayInElul(
		Taarich taarich
	) {
		return "";
	}


	private String todayInTishri(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 1:
			case 2:
				return "Rosh Hashana";

			case 3:
				return "Fast of Gedalia";

			case 10:
				return "Yom Kippur";

			case 15:
				return "Sukkot 1";

			case 16:
				return "Sukkot 2";

			case 17:
				return "Sukkot 3";

			case 18:
				return "Sukkot 4";

			case 19:
				return "Sukkot 5";

			case 20:
				return "Sukkot 6";

			case 21:
				return "Sukkot 7 / Hoshana Rabba";

			case 22:
				return "Shemini Atzeret";

			case 23:
				return "Isru Chag";
		}

		return "";
	}


	private String todayInHeshvan(
		Taarich taarich
	) {
		return "";
	}


	private String todayInKislev(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 25:
				return "Chanukah 1";

			case 26:
				return "Chanukah 2";

			case 27:
				return "Chanukah 3";

			case 28:
				return "Chanukah 4";

			case 29:
				return "Chanukah 5";

			case 30:
				return "Chanukah 6";
		}

		return "";
	}


	private String todayInTevet(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 1:
				return "Chanukah 7";

			case 2:
				return "Chanukah 8";

			case 10:
				return "Fast of Tevet";
		}

		return "";
	}


	private String todayInShevat(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 15:
				return "Tu B'Shvat";
		}

		return "";
	}


	private String todayInAdar(
		Taarich taarich
	) {

		switch ( taarich.getHebrewDayOfMonth() ) {
			case 13:
				// TODO: This is NOT correct if it falls out on a Friday or Shabbat...
				return "Taanit Esther";

			case 14:
				return "Purim";

			case 15:
				return "Shushan Purim";
		}

		return "";
	}


	private String todayInAdarI(
		Taarich taarich
	) {
		return "";
	}


	private Taarich getHebrewDate(
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
		return taarich;
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
