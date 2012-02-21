/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Calendar;
import java.util.Date;

/**
 * A JewishDate represents a specific date obtained from a JewishCalendar. Each instance is immutable once created.
 */
public class JewishDate
	implements Comparable<JewishDate> {


	/** Must be a value from the HebrewMonth 'enum'. */
	private int                                               jewishMonth;
	private int                                               jewishYear;
	private int                                               jewishDay;

	private int                                               moladHours;
	private int                                               moladMinutes;
	private int                                               moladChalakim;

	/** The Gregorian year */
	private int                                               gregorianYear;

	/**
	 * The month, in a format compatible with java.util.Calendar's definition of month zero being January, through to
	 *  month 11 being December.
	 */
	private int                                               gregorianMonth;

	/** The day of the Gregorian month */
	private int                                               gregorianDayOfMonth;

	/** 1 == Sunday, 2 == Monday, etc... */
	private int                                               dayOfWeek;

	/** Internal representation of the date. */
	private int                                               gregorianAbsDate;


	JewishDate(
		int jewishYear,
		int jewishMonth,
		int jewishDay,
		int gregorianYear,
		int gregorianMonth,
		int gregorianDayOfMonth,
		int dayOfWeek,
		int gregorianAbsDate
	) {
		this( jewishYear, jewishMonth, jewishDay, gregorianYear, gregorianMonth, gregorianDayOfMonth, dayOfWeek, gregorianAbsDate, 0, 0, 0 );
	}


	JewishDate(
		int jewishYear,
		int jewishMonth,
		int jewishDay,
		int gregorianYear,
		int gregorianMonth,
		int gregorianDayOfMonth,
		int dayOfWeek,
		int gregorianAbsDate,
	  int moladHours,
	  int moladMinutes,
	  int moladChalakim
	) {

		this.jewishYear = jewishYear;
		this.jewishMonth = jewishMonth;
		this.jewishDay = jewishDay;
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
	 * Returns the Jewish month 1-12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes to 13
	 * for Adar II
	 *
	 * @return the Jewish month from 1 to 12 (or 13 years in a leap year). The month count starts with 1 for Nisan and goes
	 *         to 13 for Adar II
	 */
	public int getJewishMonth() {
		return jewishMonth;
	}


	/**
	 * Returns the Jewish day of month.
	 *
	 * @return the Jewish day of the month
	 */
	public int getJewishDayOfMonth() {
		return jewishDay;
	}


	/**
	 * Returns the Jewish year.
	 *
	 * @return the Jewish year
	 */
	public int getJewishYear() {
		return jewishYear;
	}


	/**
	 * Returns the day of the week as a number between 1-7.
	 *
	 * @return the day of the week as a number between 1-7.
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}


	public int getGregorianYear() {
		return gregorianYear;
	}


	/**
	 * @return A month which can be directly compared to the months as defined in java.util.Calendar.
	 */
	@MagicConstant( intValues = {Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER })
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
	 * Returns if the year the calendar is set to is a Jewish leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
	 * cycle are leap years.
	 *
	 * @return true if it is a leap year
	 */
	public boolean isJewishLeapYear() {
		return JewishCalendar.isJewishLeapYear( getJewishYear() );
	}

	
	/**
	 * Returns the number of days for the current year that the calendar is set to.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 * @see #isJewishLeapYear()
	 */
	public int getDaysInJewishYear() {

		return JewishCalendar.getJewishCalendarElapsedDays(
			getJewishYear() + 1 ) - JewishCalendar.getJewishCalendarElapsedDays( getJewishYear() );
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
		return JewishCalendar.isCheshvanLong( getJewishYear() );
	}


	/**
	 * Returns if the Kislev is short for the year that this class is set to. The method name isShort is done since in a
	 * Qesidrah (ordered) year Kislev is long.
	 *
	 * @return true if Kislev is short for the year that this class is set to
	 *
	 */
	public boolean isKislevShort() {
		return JewishCalendar.isKislevShort( getJewishYear() );
	}


	/**
	 * Returns the Cheshvan and Kislev kviah (whether a Jewish year is short, regular or long). It will return {@link
	 * HebrewYearType#SHELAIMIM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#KESIDRAN} if Cheshvan is 29
	 * days and Kislev is 30 days and {@link HebrewYearType#CHASERIM} if both are 29 days.
	 *
	 * @return {@link HebrewYearType#SHELAIMIM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#KESIDRAN} if
	 *         Cheshvan is 29 days and Kislev is 30 days and {@link HebrewYearType#CHASERIM} if both are 29 days.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 */
	public int getCheshvanKislevKviah() {

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
	 * Returns the number of days of the Jewish month that the calendar is currently set to.
	 *
	 * @return the number of days for a given Jewish month that the calendar is currently set to.
	 */
	public int getDaysInJewishMonth() {
		return JewishCalendar.getDaysInJewishMonth( jewishMonth, jewishYear );
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Molad info

	/**
	 * Returns the molad for the year and month of the specified JewishDate. Returns a JewishDate set to the date of
	 * the molad with the molad fields properly set.
	 *
	 * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
	 * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
	 * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
	 */
	@SuppressWarnings( "FeatureEnvy" )
	public JewishDate getMolad( JewishDate date ) {

		JewishDate moladDate = JewishCalendar.getForMolad( JewishCalendar.getChalakimSinceMoladTohu( jewishYear, jewishMonth ) );

		if ( moladDate.getMoladHours() >= 6 ) {
			moladDate = moladDate.tomorrow();
		}
		
		moladHours = ( moladDate.getMoladHours() + JewishCalendar.CHALAKIM_PER_MINUTE ) % JewishCalendar.HOURS_PER_DAY;
		return moladDate;
	}

	
	
	/**
	 * Returns the molad hours. Only a JewishDate created via the getMolad() method will have this field set. Other 
	 *  JewishDates will have this field set to 0.
	 *
	 * @return the molad hours
	 */
	public int getMoladHours() {
		return moladHours;
	}


	/**
	 * Returns the molad minutes. Only a JewishDate created via the getMolad() method will have this field set. Other 
	 *  JewishDates will have this field set to 0.
	 *
	 * @return the molad minutes
	 */
	public int getMoladMinutes() {
		return moladMinutes;
	}


	/**
	 * Returns the molad chalakim/parts. Only a JewishDate created via the getMolad() method will have this field set. Other 
	 *  JewishDates will have this field set to 0.
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
		return JewishCalendar.getChalakimSinceMoladTohu( jewishYear, jewishMonth );
	}


	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Date navigation

	public JewishDate tomorrow() {
		return JewishCalendar.forward( this );
	}
	
	public JewishDate yesterday() {
		return JewishCalendar.back( this );
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Base class overrides
	
	@Override
	public int compareTo( JewishDate otherDate ) {
		
		if ( jewishYear < otherDate.jewishYear ) {
			return -1;
		}
		else if ( jewishYear > otherDate.jewishYear ) {
			return 1;
		}
		
		if ( jewishMonth < otherDate.jewishMonth ) {
			return -1;
		}
		else if ( jewishMonth > otherDate.jewishMonth ) {
			return 1;
		}
		
		if ( jewishDay < otherDate.jewishDay ) {
			return -1;
		}
		else if ( jewishDay > otherDate.jewishDay ) {
			return 1;
		}
		
		return 0;
	}


	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( !( o instanceof JewishDate ) ) {
			return false;
		}

		JewishDate that = (JewishDate)o;

		if ( jewishDay != that.jewishDay ) {
			return false;
		}
		if ( jewishMonth != that.jewishMonth ) {
			return false;
		}
		if ( jewishYear != that.jewishYear ) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		int result = jewishMonth;
		result = 31 * result + jewishYear;
		result = 31 * result + jewishDay;
		return result;
	}
}  // End of JewishDate class
