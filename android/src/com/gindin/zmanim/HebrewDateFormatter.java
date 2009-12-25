package com.gindin.zmanim;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Just a simple, hard-coded formatting of the dates.
 */
class HebrewDateFormatter {


	/** Used to format the zmanim times that get displayed. */
	private DateFormat                                        timeFormatter;

	/** Used to format the secular date in the screen's header. */
	private DateFormat                                        secularDateFormatter;


	public HebrewDateFormatter(
		DateFormat timeFormatter,
		DateFormat secularDateFormatter
	) {
		this.timeFormatter = timeFormatter;
		this.secularDateFormatter = secularDateFormatter;
	}


	public String formatZmanTime(
		Date zman
	) {
		return timeFormatter.format( zman );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	public String formatHebrewDate(
		HebrewCalendar calendar
	) {

		final int day = calendar.get( HebrewCalendar.DAY_OF_MONTH );
		final int monthIndex = calendar.get( HebrewCalendar.MONTH );
		final int year = calendar.get( HebrewCalendar.YEAR );

		final String month;
		switch ( monthIndex ) {
			case HebrewCalendar.NISAN:
				month = "Nisan";
				break;

			case HebrewCalendar.IYAR:
				month = "Iyar";
				break;

			case HebrewCalendar.SIVAN:
				month = "Sivan";
				break;

			case HebrewCalendar.TAMUZ:
				month = "Tamuz";
				break;

			case HebrewCalendar.AV:
				month = "Av";
				break;

			case HebrewCalendar.ELUL:
				month = "Elul";
				break;

			case HebrewCalendar.TISHRI:
				month = "Tishri";
				break;

			case HebrewCalendar.HESHVAN:
				month = "Heshvan";
				break;

			case HebrewCalendar.KISLEV:
				month = "Kislev";
				break;

			case HebrewCalendar.TEVET:
				month = "Tevet";
				break;

			case HebrewCalendar.SHEVAT:
				month = "Shevat";
				break;

			case HebrewCalendar.ADAR:
				// If this is a leap year, then month is Adar II.
				if ( calendar.isLeapYear() ) {
					month = "Adar II";
				}
				else {
					month = "Adar";
				}
				break;

			case HebrewCalendar.ADAR_1:
				month = "Adar I";
				break;

			default:
				month = "?" + monthIndex;
		}

		return day + " " + month + " " + year;

	}


	public String formatSecularDate(
		Calendar calendar
	) {

		final StringBuffer date = new StringBuffer();
		switch ( calendar.get( Calendar.DAY_OF_WEEK ) ) {
			case Calendar.SUNDAY:
				date.append( "Sun" );
				break;

			case Calendar.MONDAY:
				date.append( "Mon" );
				break;

			case Calendar.TUESDAY:
				date.append( "Tue" );
				break;

			case Calendar.WEDNESDAY:
				date.append( "Wed" );
				break;

			case Calendar.THURSDAY:
				date.append( "Thur" );
				break;

			case Calendar.FRIDAY:
				date.append( "Fri" );
				break;

			case Calendar.SATURDAY:
				date.append( "Shabbat" );
				break;

		}

		date.append( ", " );
		date.append( secularDateFormatter.format( calendar.getTime() ) );

		return date.toString();
	}

}  // End of HebrewDateFormatter class
