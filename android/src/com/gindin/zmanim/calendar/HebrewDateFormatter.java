/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.times.Zman;
import com.gindin.zmanim.times.ZmanRounder;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Just a simple, hard-coded formatting of the dates.
 */
public class HebrewDateFormatter {


	/** Used to format the zmanim times that get displayed. */
	private final DateFormat                                  timeFormatter;

	/** Used to format the secular date in the screen's header. */
	private final DateFormat                                  secularDateFormatter;

	/** Used to ensure certain zmanim are rounded properly. */
	private final ZmanRounder                                 zmanRounder;

	public HebrewDateFormatter(
		boolean showSeconds
	) {

		if ( showSeconds ) {
			timeFormatter = DateFormat.getTimeInstance( DateFormat.MEDIUM );
			zmanRounder = null;
		}
		else {
			timeFormatter = DateFormat.getTimeInstance( DateFormat.SHORT );
			zmanRounder = new ZmanRounder();
		}

		secularDateFormatter = DateFormat.getDateInstance( DateFormat.MEDIUM );
	}


	public String formatZmanTime(
		Zman zman
	) {

		// This can return null in case the zman just doesn't apply. E.g., there are places where the Sun never
		//  actually gets low enough to be able to calculate Alot @ 26degrees.
		Date time = zman.getTime();
		if ( null == time ) {
			return "N/A";
		}

		if ( null != zmanRounder ) {
			// Some of the zman types we need to do some rounding so that we avoid a situation of showing
			//  a time that can mislead someone into missing their mitzvah...
			switch ( zman.type ) {

				case Alot:
				case Talit:
				case Hanetz:
				case TzaitHacochavim:
					time = zmanRounder.roundToLatestMinute( time );
					break;

				case SofZmanShema:
				case SofZmanTefila:
				case MinchaGedola:
				case MinchaKetana:
				case PlagHaMincha:
				case CandleLighting:
				case LatestMincha:
					time = zmanRounder.roundToEarliestMinute( time );
					break;
			}
		}

		synchronized ( this ) {
			return timeFormatter.format( time );
		}
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


	public String formatSecularDate(
		HebrewCalendar calendar
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
