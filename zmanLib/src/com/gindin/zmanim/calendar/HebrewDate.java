/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * A HebrewDate represents a specific date obtained from a HebrewCalendar. Each instance is immutable once created.
 */
@SuppressWarnings( "UnusedDeclaration" )
public class HebrewDate
	implements Comparable<HebrewDate> {


	private int                                               hebrewYear;
	@HebrewMonth.Constant
	private int                                               hebrewMonth;
	private int                                               hebrewDay;

	private int                                               moladHours;
	private int                                               moladMinutes;
	private int                                               moladChalakim;

	/** The Gregorian year */
	private int                                               gregorianYear;

	/**
	 * The month, in a format compatible with java.util.Calendar's definition of month zero being January, through to
	 *  month 11 being December.
	 */
	@Constants.CalendarMonth
	private int                                               gregorianMonth;

	/** The day of the Gregorian month */
	private int                                               gregorianDayOfMonth;

	/** 1 == Sunday, 2 == Monday, etc... */
	@Constants.CalendarDay
	private int                                               dayOfWeek;

	/** Internal representation of the date. */
	private int                                               gregorianAbsDate;


	HebrewDate(
		int hebrewYear,
		@HebrewMonth.Constant int hebrewMonth,
		int hebrewDay,
		int gregorianYear,
		@Constants.CalendarMonth int gregorianMonth,
		int gregorianDayOfMonth,
		@Constants.CalendarDay int dayOfWeek,
		int gregorianAbsDate
	) {
		this( hebrewYear, hebrewMonth, hebrewDay, gregorianYear, gregorianMonth, gregorianDayOfMonth, dayOfWeek, gregorianAbsDate, 0, 0, 0 );
	}


	HebrewDate(
		int hebrewYear,
		@HebrewMonth.Constant int hebrewMonth,
		int hebrewDay,
		int gregorianYear,
		@Constants.CalendarMonth int gregorianMonth,
		int gregorianDayOfMonth,
		@Constants.CalendarDay int dayOfWeek,
		int gregorianAbsDate,
		int moladHours,
		int moladMinutes,
		int moladChalakim
	) {

		this.hebrewYear = hebrewYear;
		this.hebrewMonth = hebrewMonth;
		this.hebrewDay = hebrewDay;
		this.gregorianYear = gregorianYear;
		this.gregorianMonth = gregorianMonth;
		this.gregorianDayOfMonth = gregorianDayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.gregorianAbsDate = gregorianAbsDate;
		this.moladHours = moladHours;
		this.moladMinutes = moladMinutes;
		this.moladChalakim = moladChalakim;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Queries about this date.

	/**
	 * @return the Hebrew year (e.g., 5772).
	 */
	public int getHebrewYear() {
		return hebrewYear;
	}


	/**
	 * @return  the Hebrew month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes to 13
	 * for Adar II
	 */
	@HebrewMonth.Constant
	public int getHebrewMonth() {
		return hebrewMonth;
	}


	/**
	 * @return the Hebrew day of month.
	 */
	public int getHebrewDayOfMonth() {
		return hebrewDay;
	}


	/**
	 * @return the day of the week as a number between 1-7.
	 */
	@Constants.CalendarDay
	public int getDayOfWeek() {
		return dayOfWeek;
	}


	/**
	 * @return the gregorian year (e.g., 2012).
	 */
	public int getGregorianYear() {
		return gregorianYear;
	}


	/**
	 * @return A month which can be directly compared to the months as defined in java.util.Calendar.
	 */
	@Constants.CalendarMonth
	public int getGregorianMonth() {
		return gregorianMonth;
	}


	public int getGregorianDayOfMonth() {
		return gregorianDayOfMonth;
	}


	int getGregorianAbsDate() {
		return gregorianAbsDate;
	}


	/**
	 * Returns this object's date as a java.util.Date object. <b>Note</b>: This class does not have a concept of time.
	 *
	 * @return The <code>Date</code>
	 */
	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set( gregorianYear, gregorianMonth, gregorianDayOfMonth );
		return cal.getTime();
	}


	/**
	 * Returns if the year the calendar is set to is a Hebrew leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
	 * cycle are leap years.
	 *
	 * @return true if it is a leap year
	 */
	public boolean isHebrewLeapYear() {
		return HebrewCalendar.isHebrewLeapYear( getHebrewYear() );
	}

	
	/**
	 * Returns the number of days for the current year that the calendar is set to.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 * @see #isHebrewLeapYear
	 */
	public int getDaysInHebrewYear() {
		return HebrewCalendar.getElapsedDays( getHebrewYear() + 1 ) - HebrewCalendar.getElapsedDays( getHebrewYear() );
	}


	/**
	 * Returns if Cheshvan is long (30 days VS 29 days) for the current year that the calendar is set to. The method name
	 * isLong is done since in a Qesidrah (ordered) year Cheshvan is short.
	 *
	 * @return true if Cheshvan is long for the current year that the calendar is set to
	 *
	 * @see #isCheshvanLong()
	 */
	public boolean isCheshvanLong() {
		return HebrewCalendar.isCheshvanLong( getHebrewYear() );
	}


	/**
	 * Returns if the Kislev is short for the year that this class is set to. The method name isShort is done since in a
	 * Qesidrah (ordered) year Kislev is long.
	 *
	 * @return true if Kislev is short for the year that this class is set to
	 *
	 */
	public boolean isKislevShort() {
		return HebrewCalendar.isKislevShort( getHebrewYear() );
	}


	/**
	 * Returns the Cheshvan and Kislev kviah (whether a Hebrew year is short, regular or long). It will return {@link
	 * HebrewYearType#SHELAIMIM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#KESIDRAN} if Cheshvan is 29
	 * days and Kislev is 30 days and {@link HebrewYearType#CHASERIM} if both are 29 days.
	 *
	 * @return {@link HebrewYearType#SHELAIMIM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#KESIDRAN} if
	 *         Cheshvan is 29 days and Kislev is 30 days and {@link HebrewYearType#CHASERIM} if both are 29 days.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 */
	public HebrewYearType getYearType() {

		final boolean cheshvanLong = isCheshvanLong();
		final boolean kislevShort = isKislevShort();

		if ( cheshvanLong && !kislevShort ) {
			return HebrewYearType.SHELAIMIM;
		}
		else if ( !cheshvanLong && kislevShort ) {
			return HebrewYearType.CHASERIM;
		}
		else {
			return HebrewYearType.KESIDRAN;
		}
	}


	/**
	 * @return the number of days for a given Heberw month that the calendar is currently set to.
	 */
	public int getDaysInHebrewMonth() {
		return HebrewCalendar.getDaysInHebrewMonth( hebrewMonth, hebrewYear );
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Molad info

	/**
	 * Returns the molad for the year and month of the specified HebrewDate. Returns a HebrewDate set to the date of
	 * the molad with the molad fields properly set.
	 *
	 * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
	 * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
	 * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
	 */
	@SuppressWarnings( "FeatureEnvy" )
	public HebrewDate getMolad( HebrewDate date ) {

		HebrewDate moladDate = HebrewCalendar
			.getForMolad( HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth ) );

		if ( moladDate.getMoladHours() >= 6 ) {
			moladDate = moladDate.tomorrow();
		}
		
		moladHours = ( moladDate.getMoladHours() + HebrewCalendar.CHALAKIM_PER_MINUTE ) % HebrewCalendar.HOURS_PER_DAY;
		return moladDate;
	}

	
	
	/**
	 * Returns the molad hours. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HeberwDates will have this field set to 0.
	 *
	 * @return the molad hours
	 */
	public int getMoladHours() {
		return moladHours;
	}


	/**
	 * Returns the molad minutes. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HebrewDates will have this field set to 0.
	 *
	 * @return the molad minutes
	 */
	public int getMoladMinutes() {
		return moladMinutes;
	}


	/**
	 * Returns the molad chalakim/parts. Only a HebrewDate created via the getMolad() method will have this field set. Other
	 *  HebrewDates will have this field set to 0.
	 *
	 * @return the molad chalakim/parts
	 */
	public int getMoladChalakim() {
		return moladChalakim;
	}

	
	/**
	 * Returns the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu to the year and
	 * month passed in.
	 *
	 * @return the number of chalakim (parts - 1080 to the hour) from the original hypothetical Molad Tohu
	 */
	public long getChalakimSinceMoladTohu() {
		return HebrewCalendar.getChalakimSinceMoladTohu( hebrewYear, hebrewMonth );
	}


	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Date navigation

	public HebrewDate tomorrow() {
		return HebrewCalendar.forward( this );
	}
	
	public HebrewDate yesterday() {
		return HebrewCalendar.back( this );
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Base class overrides
	
	@Override
	public int compareTo( HebrewDate otherDate ) {
		
		if ( hebrewYear < otherDate.hebrewYear ) {
			return -1;
		}
		else if ( hebrewYear > otherDate.hebrewYear ) {
			return 1;
		}
		
		if ( hebrewMonth < otherDate.hebrewMonth ) {
			return -1;
		}
		else if ( hebrewMonth > otherDate.hebrewMonth ) {
			return 1;
		}
		
		if ( hebrewDay < otherDate.hebrewDay ) {
			return -1;
		}
		else if ( hebrewDay > otherDate.hebrewDay ) {
			return 1;
		}
		
		return 0;
	}


	@SuppressWarnings( "RedundantIfStatement" )
	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof HebrewDate ) ) {
			return false;
		}

		HebrewDate that = (HebrewDate)o;

		if ( hebrewDay != that.hebrewDay ) {
			return false;
		}
		if ( hebrewMonth != that.hebrewMonth ) {
			return false;
		}
		if ( hebrewYear != that.hebrewYear ) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		int result = hebrewMonth;
		result = 31 * result + hebrewYear;
		result = 31 * result + hebrewDay;
		return result;
	}
}  // End of HebrewDate class
