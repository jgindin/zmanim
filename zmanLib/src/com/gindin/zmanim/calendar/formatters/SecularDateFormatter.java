/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.formatters;

import com.gindin.zmanim.calendar.HebrewDate;

import java.util.Calendar;

/**
 * Knows how to format a HebrewDate as a secular date.
 */
public abstract class SecularDateFormatter {

	public enum Style {
		DEFAULT,
		SHORT,
		MEDIUM,
		LONG
	}

	private static SecularDateFormatter                       FORMATTER;


	public static SecularDateFormatter getInstance( Style formatterStyle ) {

		switch ( formatterStyle ) {
			case SHORT:
			case DEFAULT:
			case MEDIUM:
			case LONG:
				if ( null == FORMATTER ) {
					FORMATTER = new DefaultFormatter();
				}
				return FORMATTER;
		}

		return null;
	}

	public abstract String format( HebrewDate date );


	static class DefaultFormatter
		extends SecularDateFormatter {

		DefaultFormatter() {
		}


		@SuppressWarnings( "FeatureEnvy" )
		@Override
		public String format( HebrewDate date ) {

			final StringBuilder formattedDate = new StringBuilder();
			switch ( date.getDayOfWeek() ) {
				case Calendar.SUNDAY:
					formattedDate.append( "Sun" );
					break;

				case Calendar.MONDAY:
					formattedDate.append( "Mon" );
					break;

				case Calendar.TUESDAY:
					formattedDate.append( "Tue" );
					break;

				case Calendar.WEDNESDAY:
					formattedDate.append( "Wed" );
					break;

				case Calendar.THURSDAY:
					formattedDate.append( "Thur" );
					break;

				case Calendar.FRIDAY:
					formattedDate.append( "Fri" );
					break;

				case Calendar.SATURDAY:
					formattedDate.append( "Shabbat" );
					break;

			}

			formattedDate.append( ", " );

			switch ( date.getGregorianMonth() ) {
				case Calendar.JANUARY:
					formattedDate.append( "Jan " );
					break;

				case Calendar.FEBRUARY:
					formattedDate.append( "Feb " );
					break;

				case Calendar.MARCH:
					formattedDate.append( "Mar " );
					break;

				case Calendar.APRIL:
					formattedDate.append( "Apr " );
					break;

				case Calendar.MAY:
					formattedDate.append( "May " );
					break;

				case Calendar.JUNE:
					formattedDate.append( "Jun " );
					break;

				case Calendar.JULY:
					formattedDate.append( "Jul " );
					break;

				case Calendar.AUGUST:
					formattedDate.append( "Aug " );
					break;

				case Calendar.SEPTEMBER:
					formattedDate.append( "Sep " );
					break;

				case Calendar.OCTOBER:
					formattedDate.append( "Oct " );
					break;

				case Calendar.NOVEMBER:
					formattedDate.append( "Nov " );
					break;

				case Calendar.DECEMBER:
					formattedDate.append( "Dec " );
					break;
			}

			formattedDate.append( date.getGregorianDayOfMonth() ).append( ", " ).append( date.getGregorianYear() );

			return formattedDate.toString();
		}
	}

}  // End of SecularDateFormatter class
