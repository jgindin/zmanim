/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.formatters;

import com.gindin.zmanim.calendar.HebrewDate;

import java.text.DateFormat;
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

		private final DateFormat                                  secularDateFormatter;


		DefaultFormatter() {
			secularDateFormatter = DateFormat.getDateInstance( DateFormat.MEDIUM );
		}


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
			formattedDate.append( secularDateFormatter.format( date.getTime() ) );

			return formattedDate.toString();
		}
	}

}  // End of SecularDateFormatter class
