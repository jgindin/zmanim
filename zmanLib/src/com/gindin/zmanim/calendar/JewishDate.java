/*
 * Copyright (c) 2011. Jay R. Gindin
 */
package com.gindin.zmanim.calendar;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The JewishDate class allows one to maintain an instance of a Gregorian date along with the corresponding Jewish date.
 * This class can use the standard Java Date and Calendar classes for setting it, but does not subclass these classes or
 * use them internally to any extensive use. This class also does not have a concept of a time (which the Date class
 * does). Please note that the calendar does not currently support dates prior to 1/1/1 Gregorian. Also keep in mind
 * that the Gregorian calendar started on October 15, 1582, so any calculations prior to that are suspect (at least from
 * a Gregorian perspective). While 1/1/1 Gregorian and forward are technically supported, any calculations prior to <a
 * href="http://en.wikipedia.org/wiki/Hillel_II">Hillel II's (Hakatan's</a>) calendar (4119 in the Jewish Calendar / 359
 * CE Julian as recorded by <a href="http://en.wikipedia.org/wiki/Hai_Gaon">Rav Hai Gaon</a>) would be just an
 * approximation.
 * <p/>
 * This open source Java code was written by <a href="http://www.facebook.com/avromf">Avrom Finkelstien</a> from his C++
 * code. It was refactored to fit the KosherJava Zmanim API with simplification of the code, enhancements and some bug
 * fixing.
 * <p/>
 * Some of Avrom's original C++ code was translated from <a href="http://emr.cs.uiuc.edu/~reingold/calendar.C">C/C++
 * code</a> in <a href="http://www.calendarists.com">Calendrical Calculations</a> by Nachum Dershowitz and Edward M.
 * Reingold, Software-- Practice & Experience, vol. 20, no. 9 (September, 1990), pp. 899- 928. Any method with the mark
 * "ND+ER" indicates that the method was taken from this source with minor modifications.
 * <p/>
 * If you are looking for a class that implements a Jewish calendar version of the Calendar class, one is available from
 * the <a href="http://site.icu-project.org/" >ICU (International Components for Unicode)</a> project, formerly part of
 * IBM's DeveloperWorks.
 *
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.2.6
 * @see net.sourceforge.zmanim.hebrewcalendar.JewishCalendar
 * @see net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter
 * @see java.util.Date
 * @see java.util.Calendar
 */
@SuppressWarnings( { "MagicNumber", "UnusedDeclaration" } )
public class JewishDate
	implements Comparable<JewishDate> {

	
	private static final int MINUTES_PER_HOUR = 60;
	private static final int HOURS_PER_DAY = 24;

	/**
	 * the Jewish epoch using the RD (Rata Die/Fixed Date or Reingold Dershowitz) day used in Calendrical Calculations. Day
	 * 1 is January 1, 0001 Gregorian
	 */
	private static final int JEWISH_EPOCH = -1373429;

	private static final int CHALAKIM_PER_MINUTE = 18;

	private static final int CHALAKIM_PER_HOUR = CHALAKIM_PER_MINUTE * MINUTES_PER_HOUR;

	private static final int CHALAKIM_PER_DAY = CHALAKIM_PER_HOUR * HOURS_PER_DAY;

	private static final long CHALAKIM_PER_MONTH = 765433; // (29 * 24 + 12) * 1080 + 793


	/**
	 * Days from the beginning of Sunday till molad BaHaRaD. Calculated as 1 day, 5 hours and 204 chalakim = (24 + 5) *
	 * 1080 + 204 = 31524
	 */
	private static final int CHALAKIM_MOLAD_TOHU = 31524;

	/** Reject dates prior to 18 Teves, 3761 (1/1/1 AD). This restriction can be relaxed if the date coding is changed/corrected. */
	private static final int EARLIEST_VALID_HEBREW_YEAR = 3761;

	private static final int YEARS_PER_CYCLE = 19;
	

	/** Must be a value from the HebrewMonth 'enum'. */
	private int jewishMonth;

	private int jewishDay;

	private int jewishYear;

	private int moladHours;

	private int moladMinutes;

	private int moladChalakim;

	/**
	 * The month, where 1 == January, 2 == February, etc... Note that this is different than the Java's Calendar class
	 * where January ==0
	 */
	private int gregorianMonth;

	/** The day of the Gregorian month */
	private int gregorianDayOfMonth;

	/** The Gregorian year */
	private int gregorianYear;

	/** 1 == Sunday, 2 == Monday, etc... */
	private int dayOfWeek;

	private int gregorianAbsDate;


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Constructors

	/** Default constructor will set a default date to the current system date. */
	public JewishDate() {
		Calendar calendar = Calendar.getInstance();
		setDate( calendar );
	}


	/**
	 * Creates a Jewish date based on a Jewish year, month and day of month.
	 *
	 * @param jewishYear       the Jewish year
	 * @param jewishMonth      the Jewish month. The method expects a 1 for Nissan ... 12 for Adar and 13 for Adar II. Use
	 *                         the constants {@link HebrewMonth#NISSAN} ... {@link HebrewMonth#ADAR} (or {@link
	 *                         HebrewMonth#ADAR_II} for a leap year Adar II) to avoid any confusion.
	 * @param jewishDayOfMonth the Jewish day of month. If 30 is passed in for a month with only 29 days (for example
	 *                         {@link HebrewMonth#IYAR}, or {@link HebrewMonth#KISLEV} in a year that {@link
	 *                         #isKislevShort()}), the 29th (last valid date of the month) will be set
	 *
	 * @throws IllegalArgumentException if the day of month is < 1 or > 30, or a year of < 0 is passed in.
	 */
	public JewishDate(
		int jewishYear,
		int jewishMonth,
		int jewishDayOfMonth
	) {
		setJewishDate( jewishYear, jewishMonth, jewishDayOfMonth );
	}


	/**
	 * A constructor that initializes the date to the {@link java.util.Date Date} paremeter.
	 *
	 * @param date the <code>Date</code> to set the calendar to
	 *
	 * @throws IllegalArgumentException if the date would fall prior to the January 1, 1 AD
	 */
	public JewishDate( Date date ) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		setDate( cal );
	}


	/**
	 * A constructor that initializes the date to the {@link java.util.Calendar Calendar} paremeter.
	 *
	 * @param calendar the <code>Calendar</code> to set the calendar to
	 *
	 * @throws IllegalArgumentException if the {@link java.util.Calendar#ERA} is {@link java.util.GregorianCalendar#BC}
	 */
	public JewishDate( Calendar calendar ) {
		setDate( calendar );
	}


	/**
	 * Constructor that creates a JewishDate based on a molad passed in. The molad would be the number of chalakim/parts
	 * starting at the begining of Sunday prior to the molad Tohu BeHaRaD (Be = Monday, Ha= 5 hours and Rad =204
	 * chalakim/parts) - prior to the start of the Jewish calendar. BeHaRaD is 23:11:20 on Sunday night(5 hours 204/1080
	 * chalakim after sunset on Sunday evening).
	 */
	public JewishDate( long molad ) {
		absDateToDate( moladToAbsDate( molad ) );
		// long chalakimSince = getChalakimSinceMoladTohu(year, JewishDate.TISHREI);// tishrei
		int conjunctionDay = (int)( molad / CHALAKIM_PER_DAY );
		int conjunctionParts = (int)( molad - conjunctionDay * CHALAKIM_PER_DAY );
		setMoladTime( conjunctionParts );
	}


	/**
	 * Construct a JewishDate such that it is initialized the same as the specified date.
	 *
	 * @param date the date to initialize this new JewishDate to.
	 */
	public JewishDate( JewishDate date ) {
		this( date.getTime() );
	}


	/**
	 * Constructor only used internally. 
	 */
	private JewishDate(
		int jewishYear,
		int jewishMonth,
		int jewishDay,
		int gregorianYear,
		int gregorianMonth,
		int gregorianDayOfMonth,
		int dayOfWeek,
		int gregorianAbsDate
	) {

		this.jewishYear = jewishYear;
		this.jewishMonth = jewishMonth;
		this.jewishDay = jewishDay;
		this.gregorianYear = gregorianYear;
		this.gregorianMonth = gregorianMonth;
		this.gregorianDayOfMonth = gregorianDayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.gregorianAbsDate = gregorianAbsDate;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Molad info

	/**
	 * Returns the molad hours. Only a JewishDate object populated with {@link #getMolad()}, {@link #setJewishDate(int,
	 * int, int, int, int, int)} will have this field populated. A regular JewishDate object
	 * will have this field set to 0.
	 *
	 * @return the molad hours
	 *
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 */
	public int getMoladHours() {
		return moladHours;
	}


	/**
	 * Returns the molad minutes. Only an object populated with {@link #getMolad()}, {@link #setJewishDate(int, int, int,
	 * int, int, int)} will have these fields populated. A regular JewishDate object will have this field set to 0.
	 *
	 * @return the molad minutes
	 *
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
	 */
	public int getMoladMinutes() {
		return moladMinutes;
	}


	/**
	 * Returns the molad chalakim/parts. Only an object populated with {@link #getMolad()}, {@link #setJewishDate(int, int,
	 * int, int, int, int)} will have these fields populated. A regular JewishDate object will have this field set to 0.
	 *
	 * @return the molad chalakim/parts
	 *
	 * @see #getMolad()
	 * @see #setJewishDate(int, int, int, int, int, int)
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
		return getChalakimSinceMoladTohu( jewishYear, jewishMonth );
	}


	/**
	 * Returns the molad for a given year and month. Returns a JewishDate {@link Object} set to the date of the molad with
	 * the {@link #getMoladHours() hours}, {@link #getMoladMinutes() minutes} and {@link #getMoladChalakim() chalakim} set.
	 * In the current implementation, it sets the molad time based on a midnight date rollover. This means that Rosh
	 * Chodesh Adar II, 5771 with a molad of 7 chalakim past midnight on Shabbos 29 Adar I / March 5, 2011 12:00 AM and 7
	 * chalakim, will have the following values: hours: 0, minutes: 0, Chalakim: 7.
	 *
	 * @return a JewishDate {@link Object} set to the date of the molad with the {@link #getMoladHours() hours}, {@link
	 *         #getMoladMinutes() minutes} and {@link #getMoladChalakim() chalakim} set.
	 */
	public JewishDate getMolad() {
		JewishDate moladDate = new JewishDate( getChalakimSinceMoladTohu() );
		if ( moladDate.getMoladHours() >= 6 ) {
			moladDate.forward();
		}
		this.moladHours = ( moladDate.getMoladHours() + CHALAKIM_PER_MINUTE ) % HOURS_PER_DAY;
		return moladDate;
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


	/**
	 * Returns this object's date as a java.util.Date object. <b>Note</b>: This class does not have a concept of time.
	 *
	 * @return The <code>Date</code>
	 */
	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set( gregorianYear, gregorianMonth - 1, gregorianDayOfMonth );
		return cal.getTime();
	}


	/**
	 * Returns if the year the calendar is set to is a Jewish leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year
	 * cycle are leap years.
	 *
	 * @return true if it is a leap year
	 *
	 * @see #isJewishLeapYear(int)
	 */
	public boolean isJewishLeapYear() {
		return isJewishLeapYear( getJewishYear() );
	}


	/**
	 * Returns the number of days for the current year that the calendar is set to.
	 *
	 * @see #isCheshvanLong()
	 * @see #isKislevShort()
	 * @see #isJewishLeapYear()
	 */
	public int getDaysInJewishYear() {

		return getJewishCalendarElapsedDays( getJewishYear() + 1 ) - getJewishCalendarElapsedDays( getJewishYear() );
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
		return isCheshvanLong( getJewishYear() );
	}


	/**
	 * Returns if the Kislev is short for the year that this class is set to. The method name isShort is done since in a
	 * Qesidrah (ordered) year Kislev is long.
	 *
	 * @return true if Kislev is short for the year that this class is set to
	 *
	 * @see #isKislevShort(int)
	 */
	public boolean isKislevShort() {
		return isKislevShort( getJewishYear() );
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
	 * @see #isKislevShort(int)
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
		return getDaysInJewishMonth( getJewishMonth(), getJewishYear() );
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Date navigation

	/**
	 * Rolls the date forward by 1 day. It modifies both the Gregorian and Jewish dates accordingly. The API does not
	 * currently offer the ability to forward more than one day at time, or to forward by month or year. If such
	 * manipulation is required use the {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or {@link
	 * java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the JewishDate
	 * 	cal.add(Calendar.MONTH, 3); // add 3 Gregorian months
	 * 	JewishDate newDate = new JewishDate(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 *
	 * @see #back()
	 * @see java.util.Calendar#add(int, int)
	 * @see java.util.Calendar#roll(int, int)
	 */
	public JewishDate forward() {
		
		// Change Gregorian date
		int newGregorianMonth = gregorianMonth;
		int newGregorianYear = gregorianYear;
		int newGregorianDayOfMonth = gregorianDayOfMonth;
		
		if ( newGregorianDayOfMonth == getLastDayOfGregorianMonth( newGregorianMonth, newGregorianYear ) ) {
			// if last day of year
			if ( newGregorianMonth == 12 ) {
				newGregorianYear++;
				newGregorianMonth = 1;
			}
			else {
				newGregorianMonth++;
			}

			newGregorianDayOfMonth = 1;
		}
		else { 
			// if not last day of month
			newGregorianDayOfMonth++;
		}

		// Change the Jewish Date
		int newJewishDay = jewishDay;
		int newJewishMonth = jewishMonth;
		int newJewishYear = jewishYear;
		
		if ( newJewishDay == getDaysInJewishMonth() ) {
			// if it last day of elul (i.e. last day of Jewish year)
			if ( newJewishMonth == HebrewMonth.ELUL ) {
				newJewishYear++;
				newJewishMonth++;
			}
			else if ( newJewishMonth == getLastMonthOfJewishYear( newJewishYear ) ) {
				// if it is the last day of Adar, or Adar II as case may be
				newJewishMonth = HebrewMonth.NISSAN;
			}
			else {
				newJewishMonth++;
			}

			newJewishDay = 1;

		}
		else { // if not last date of month
			newJewishDay++;
		}

		int newDayOfWeek = dayOfWeek;
		if ( newDayOfWeek == 7 ) { 
			// if last day of week, loop back to Sunday
			newDayOfWeek = 1;
		}
		else {
			newDayOfWeek++;
		}

		// increment the absolute date
		int newGregorianAbsDate = gregorianAbsDate + 1; 
		
		return new JewishDate( newJewishYear, newJewishMonth, newJewishDay,
			newGregorianYear, newGregorianMonth, newGregorianDayOfMonth, newDayOfWeek, newGregorianAbsDate );
	}


	/**
	 * Rolls the date back by 1 day. It modifies both the Gregorian and Jewish dates accordingly. The API does not
	 * currently offer the ability to forward more than one day t a time, or to forward by month or year. If such
	 * manipulation is required use the {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or {@link
	 * java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the JewishDate
	 * 	cal.add(Calendar.MONTH, -3); // subtract 3 Gregorian months
	 * 	JewishDate newDate = new JewishDate(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 *
	 * @see #back()
	 * @see java.util.Calendar#add(int, int)
	 * @see java.util.Calendar#roll(int, int)
	 */
	public JewishDate back() {

		// Change Gregorian date
		int newGregorianMonth = gregorianMonth;
		int newGregorianYear = gregorianYear;
		int newGregorianDayOfMonth = gregorianDayOfMonth;


		// if first day of month...
		if ( newGregorianDayOfMonth == 1 ) {

			// if first day of year...
			if ( newGregorianMonth == 1 ) {
				newGregorianMonth = 12;
				newGregorianYear--;
			}
			else {
				newGregorianMonth--;
			}
			// change to last day of previous month
			newGregorianDayOfMonth = getLastDayOfGregorianMonth( newGregorianMonth, newGregorianYear );
		}
		else {
			newGregorianDayOfMonth--;
		}

		// Change Jewish date
		int newJewishDay = jewishDay;
		int newJewishMonth = jewishMonth;
		int newJewishYear = jewishYear;

		// if first day of the Jewish month...
		if ( newJewishDay == 1 ) {
			if ( newJewishMonth == HebrewMonth.NISSAN ) {
				newJewishMonth = getLastMonthOfJewishYear( newJewishYear );
			}
			else if ( newJewishMonth == HebrewMonth.TISHREI ) {
				// if Rosh Hashana
				newJewishYear--;
			}

			newJewishYear--;
			newJewishDay = getDaysInJewishMonth();
		}
		else {
			newJewishDay--;
		}

		int newDayOfWeek = dayOfWeek;
		if ( newDayOfWeek == 1 ) {
			// if first day of week, loop back to Saturday
			newDayOfWeek = 7;
		}
		else {
			newDayOfWeek--;
		}

		// change the absolute date
		int newGregorianAbsDate = gregorianAbsDate - 1;

		return new JewishDate( newJewishYear, newJewishMonth, newJewishDay,
			newGregorianYear, newGregorianMonth, newGregorianDayOfMonth, newDayOfWeek, newGregorianAbsDate );
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//

	// Package local for unit tests.
	int getLastDayOfGregorianMonth(
		int month
	) {
		return getLastDayOfGregorianMonth( month, gregorianYear );
	}

	/**
	 * Returns the number of days in a given month in a given month and year.
	 *
	 * @param month the month. As with other cases in this class, this is 1-based, not zero-based.
	 * @param year  the year (only impacts February)
	 *
	 * @return the number of days in the month in the given year
	 */
	private static int getLastDayOfGregorianMonth(
		int month,
		int year
	) {
		switch ( month ) {
			case 2:
				if ( ( year % 4 == 0 && year % 100 != 0 ) || ( year % 400 == 0 ) ) {
					return 29;
				}
				else {
					return 28;
				}
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			default:
				return 31;
		}
	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Gregorian / Absolute date conversions


	/** Computes the Gregorian date from the absolute date. ND+ER */
	private void absDateToDate( int absDate ) {
		int year = absDate / 366; // Search forward year by year from approximate year
		while ( absDate >= gregorianDateToAbsDate( year + 1, 1, 1 ) ) {
			year++;
		}

		int month = 1; // Search forward month by month from January
		while ( absDate > gregorianDateToAbsDate( year, month, getLastDayOfGregorianMonth( month, year ) ) ) {
			month++;
		}

		int dayOfMonth = absDate - gregorianDateToAbsDate( year, month, 1 ) + 1;
		setInternalGregorianDate( year, month, dayOfMonth );
	}


	/**
	 * Computes the absolute date from a Gregorian date. ND+ER
	 *
	 * @param year       the Gregorian year
	 * @param month      the Gregorian month. Unlike the Java Calendar where January has the value of 0,This expects a 1
	 *                   for January
	 * @param dayOfMonth the day of the month (1st, 2nd, etc...)
	 *
	 * @return the absolute Gregorian day
	 */
	private static int gregorianDateToAbsDate(
		int year,
		int month,
		int dayOfMonth
	) {
		int absDate = dayOfMonth;
		for ( int m = month - 1; m > 0; m-- ) {
			absDate += getLastDayOfGregorianMonth( m, year ); // days in prior months of the year
		}
		return ( absDate // days this year
			+ 365 * ( year - 1 ) // days in previous years ignoring leap days
			+ ( year - 1 ) / 4 // Julian leap days before this year
			- ( year - 1 ) / 100 // minus prior century years
			+ ( year - 1 ) / 400 ); // plus prior years divisible by 400
	}


	/** Computes the Jewish date from the absolute date. ND+ER */
	private void absDateToJewishDate() {
		// Approximation from below
		jewishYear = ( gregorianAbsDate + JEWISH_EPOCH ) / 366;
		// Search forward for year from the approximation
		while ( gregorianAbsDate >= jewishDateToAbsDate( jewishYear + 1, HebrewMonth.TISHREI, 1 ) ) {
			jewishYear++;
		}
		// Search forward for month from either Tishri or Nisan.
		if ( gregorianAbsDate < jewishDateToAbsDate( jewishYear, HebrewMonth.NISSAN, 1 ) ) {
			jewishMonth = HebrewMonth.TISHREI;// Start at Tishri
		}
		else {
			jewishMonth = HebrewMonth.NISSAN;// Start at Nisan
		}
		while ( gregorianAbsDate > jewishDateToAbsDate( jewishYear, jewishMonth,
			getDaysInJewishMonth() ) ) {
			jewishMonth++;
		}
		// Calculate the day by subtraction
		jewishDay = gregorianAbsDate - jewishDateToAbsDate( jewishYear, jewishMonth, 1 ) + 1;
	}


	/**
	 * Returns the absolute date of Jewish date. ND+ER
	 *
	 * @param year       the Jewish year. The year can't be negative
	 * @param month      the Jewish month starting with Nisan. Nisan expects a value of 1 etc till Adar with a value of 12.
	 *                   For a leap year, 13 will be the expected value for Adar II. Use the constants {@link
	 *                   HebrewMonth#NISSAN} etc.
	 * @param dayOfMonth the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that
	 *                   only has 29 days, the day will be set as 29.
	 *
	 * @return the absolute date of the Jewish date.
	 */
	private static int jewishDateToAbsDate(
		int year,
		int month,
		int dayOfMonth
	) {
		int elapsed = getDaysSinceStartOfJewishYear( year, month, dayOfMonth );
		// add elapsed days this year + Days in prior years + Days elapsed before absolute year 1
		return elapsed + getJewishCalendarElapsedDays( year ) + JEWISH_EPOCH;
	}


	/**
	 * Returns the number of days from the Jewish epoch from the number of chalakim from the epoch passed in.
	 *
	 * @param chalakim the number of chalakim since the beginning of Sunday prior to BaHaRaD
	 *
	 * @return the number of days from the Jewish epoch
	 */
	private static int moladToAbsDate( long chalakim ) {
		return (int)( chalakim / CHALAKIM_PER_DAY ) + JEWISH_EPOCH;
	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//

	/**
	 * Returns if the year is a Jewish leap year. Years 3, 6, 8, 11, 14, 17 and 19 in the 19 year cycle are leap years.
	 *
	 * @param year the Jewish year.
	 *
	 * @return true if it is a leap year
	 *
	 * @see #isJewishLeapYear()
	 */
	private static boolean isJewishLeapYear( int year ) {
		return ( ( 7 * year ) + 1 ) % YEARS_PER_CYCLE < 7;
	}


	/**
	 * Returns the last month of a given Jewish year. This will be 12 on a non {@link #isJewishLeapYear(int) leap year} or
	 * 13 on a leap year.
	 *
	 * @param year the Jewish year.
	 *
	 * @return 12 on a non leap year or 13 on a leap year
	 *
	 * @see #isJewishLeapYear(int)
	 */
	private static int getLastMonthOfJewishYear( int year ) {
		return isJewishLeapYear( year ) ? HebrewMonth.ADAR_II : HebrewMonth.ADAR;
	}


	/**
	 * Returns the number of days elapsed from the Sunday prior to the start of the Jewish calendar to the mean conjunction
	 * of Tishri of the Jewish year.
	 *
	 * @param year the Jewish year
	 *
	 * @return the number of days elapsed from prior to the molad Tohu BaHaRaD (Be = Monday, Ha= 5 hours and Rad =204
	 *         chalakim/parts) prior to the start of the Jewish calendar, to the mean conjunction of Tishri of the Jewish
	 *         year. BeHaRaD is 23:11:20 on Sunday night(5 hours 204/1080 chalakim after sunset on Sunday evening).
	 */
	public static int getJewishCalendarElapsedDays( int year ) {
		long chalakimSince = getChalakimSinceMoladTohu( year, HebrewMonth.TISHREI );
		int moladDay = (int)( chalakimSince / CHALAKIM_PER_DAY );
		int moladParts = (int)( chalakimSince - moladDay * CHALAKIM_PER_DAY );
		// delay Rosh Hashana for the 4 dechiyos
		return addDechiyos( year, moladDay, moladParts );
	}


	/**
	 * Converts the the {@link HebrewMonth#NISSAN} based constants used by this class to numeric month starting from {@link
	 * HebrewMonth#TISHREI}. This is required for Molad claculations.
	 */
	private static int getJewishMonthOfYear(
		int jewishYear,
		int jewishMonth
	) {
		boolean isLeapYear = isJewishLeapYear( jewishYear );
		return ( jewishMonth + ( isLeapYear ? 6 : 5 ) ) % ( isLeapYear ? HebrewMonth.ADAR_II : HebrewMonth.ADAR ) + 1;
	}


	/**
	 * Returns the number of days for a given Jewish year. ND+ER
	 *
	 * @param year the Jewish year
	 *
	 * @return the number of days for a given Jewish year.
	 *
	 * @see #isCheshvanLong(int)
	 * @see #isKislevShort(int)
	 */
	public static int getDaysInJewishYear( int year ) {
		return getJewishCalendarElapsedDays( year + 1 ) - getJewishCalendarElapsedDays( year );
	}


	/**
	 * Returns if Cheshvan is long in a given Jewish year. The method name isLong is done since in a Qesidrah (ordered)
	 * year Cheshvan is short. ND+ER
	 *
	 * @param year the year
	 *
	 * @return true if Cheshvan is long in Jewish year.
	 *
	 * @see #isCheshvanLong()
	 * @see #getCheshvanKislevKviah()
	 */
	private static boolean isCheshvanLong( int year ) {
		return getDaysInJewishYear( year ) % 10 == 5;
	}



	/**
	 * Returns if Kislev is short (29 days VS 30 days) in a given Jewish year. The method name isShort is done since in a
	 * Qesidrah (ordered) year Kislev is long. ND+ER
	 *
	 * @param year the Jewish year
	 *
	 * @return true if Kislev is short for the given Jewish year.
	 *
	 * @see #isKislevShort()
	 * @see #getCheshvanKislevKviah()
	 */
	private static boolean isKislevShort( int year ) {
		return getDaysInJewishYear( year ) % 10 == 3;
	}


	/**
	 * Returns the number of days of a Jewish month for a given month and year.
	 *
	 * @param month the Jewish month
	 * @param year  the Jewish Year
	 *
	 * @return the number of days for a given Jewish month
	 */
	private static int getDaysInJewishMonth(
		int month,
		int year
	) {
		if ( ( month == HebrewMonth.IYAR ) || ( month == HebrewMonth.TAMMUZ ) || ( month == HebrewMonth.ELUL ) || ( ( month == HebrewMonth.CHESHVAN ) && !( isCheshvanLong( year ) ) )
			|| ( ( month == HebrewMonth.KISLEV ) && isKislevShort( year ) ) || ( month == HebrewMonth.TEVES )
			|| ( ( month == HebrewMonth.ADAR ) && !( isJewishLeapYear( year ) ) ) || ( month == HebrewMonth.ADAR_II ) ) {
			return HebrewMonth.SHORT_MONTH_LENGTH;
		}
		else {
			return HebrewMonth.LONG_MONTH_LENGTH;
		}
	}


	/**
	 * returns the number of days from Rosh Hashana of the date passed in, till the full date passed in.
	 *
	 * @param year       the Jewish year
	 * @param month      the Jewish month
	 * @param dayOfMonth the day in the Jewish month
	 *
	 * @return the number of days
	 */
	private static int getDaysSinceStartOfJewishYear(
		int year,
		int month,
		int dayOfMonth
	) {
		int elapsedDays = dayOfMonth;
		// Before Tishrei (from Nissan to Tishrei), add days in prior months
		if ( month < HebrewMonth.TISHREI ) {
			// this year before and after Nisan.
			for ( int m = HebrewMonth.TISHREI; m <= getLastMonthOfJewishYear( year ); m++ ) {
				elapsedDays += getDaysInJewishMonth( m, year );
			}
			for ( int m = HebrewMonth.NISSAN; m < month; m++ ) {
				elapsedDays += getDaysInJewishMonth( m, year );
			}
		}
		else { // Add days in prior months this year
			for ( int m = HebrewMonth.TISHREI; m < month; m++ ) {
				elapsedDays += getDaysInJewishMonth( m, year );
			}
		}
		return elapsedDays;
	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Internal Construction

	/**
	 * Adds the 4 dechiyos for molad Tishrei. These are: <ol> <li>Lo ADU Rosh - Rosh Hashana can't fall on a Sunday,
	 * Wednesday or Friday. If the molad fell on one of these days, Rosh Hashana is delayed to the following day.</li>
	 * <li>Molad Zaken - If the molad of Tishrei falls after 12 noon, Rosh Hashana is delayed to the following day. If the
	 * following day is ADU, it will be delayed an additional day.</li> <li>GaTRaD - If on a non leap year the molad of
	 * Tishrei falls on a Tuesday (Ga) on or after 9 hours (T) and 204 chalakim (TRaD) it is delayed till Thursday (one day
	 * delay, plus one day for Lo ADU Rosh)</li> <li>BeTuTaKFoT - if the year following a leap year falls on a Monday (Be)
	 * on or after 15 hours (Tu) and 589 chalakim (TaKFoT) it is delayed till Tuesday</li> </ol>
	 *
	 * @return the number of elapsed days in the JewishCalendar adjusted for the 4 dechiyos.
	 */
	private static int addDechiyos(
		int year,
		int moladDay,
		int moladParts
	) {
		int roshHashanaDay = moladDay; // if no dechiyos
		// delay Rosh Hashana for the dechiyos of the Molad - new moon 1 - Molad Zaken, 2- GaTRaD 3- BeTuTaKFoT
		if ( ( moladParts >= 19440 ) // Dechiya of Molad Zaken - molad is >= midday (18 hours * 1080 chalakim)
			|| ( ( ( moladDay % 7 ) == 2 ) // start Dechiya of GaTRaD - Ga = is a Tuesday
			&& ( moladParts >= 9924 ) // TRaD = 9 hours, 204 parts or later (9 * 1080 + 204)
			&& !isJewishLeapYear( year ) ) // of a non-leap year - end Dechiya of GaTRaD
			|| ( ( ( moladDay % 7 ) == 1 ) // start Dechiya of BeTuTaKFoT - Be = is on a Monday
			&& ( moladParts >= 16789 ) // TRaD = 15 hours, 589 parts or later (15 * 1080 + 589)
			&& ( isJewishLeapYear( year - 1 ) ) ) ) { // in a year following a leap year - end Dechiya of BeTuTaKFoT
			roshHashanaDay += 1; // Then postpone Rosh HaShanah one day
		}
		// start 4th Dechiya - Lo ADU Rosh - Rosh Hashana can't occur on A- sunday, D- Wednesday, U - Friday
		if ( ( ( roshHashanaDay % 7 ) == 0 )// If Rosh HaShanah would occur on Sunday,
			|| ( ( roshHashanaDay % 7 ) == 3 ) // or Wednesday,
			|| ( ( roshHashanaDay % 7 ) == 5 ) ) { // or Friday - end 4th Dechiya - Lo ADU Rosh
			roshHashanaDay = roshHashanaDay + 1; // Then postpone it one (more) day
		}
		return roshHashanaDay;
	}


	private static long getChalakimSinceMoladTohu(
		int year,
		int month
	) {
		// Jewish lunar month = 29 days, 12 hours and 793 chalakim
		// chalakim since Molad Tohu BeHaRaD - 1 day, 5 hours and 204 chalakim
		int monthOfYear = getJewishMonthOfYear( year, month );
		int monthsElapsed = ( 235 * ( ( year - 1 ) / YEARS_PER_CYCLE ) ) // Months in complete 19 year lunar (Metonic) cycles so far
			+ ( 12 * ( ( year - 1 ) % YEARS_PER_CYCLE ) ) // Regular months in this cycle
			+ ( ( 7 * ( ( year - 1 ) % YEARS_PER_CYCLE ) + 1 ) / YEARS_PER_CYCLE ) // Leap months this cycle
			+ ( monthOfYear - 1 ); // add elapsed months till the start of the molad of the month
		// return chalakim prior to BeHaRaD + number of chalakim since
		return CHALAKIM_MOLAD_TOHU + ( CHALAKIM_PER_MONTH * monthsElapsed );
	}


	/**
	 * Sets the molad time (hours minutes and chalakim) based on the number of chalakim since the start of the day.
	 *
	 * @param chalakim the number of chalakim since the start of the day.
	 */
	private void setMoladTime( int chalakim ) {
		int adjustedChalakim = chalakim;
		this.moladHours = adjustedChalakim / CHALAKIM_PER_HOUR;
		adjustedChalakim = adjustedChalakim - ( getMoladHours() * CHALAKIM_PER_HOUR );
		this.moladMinutes = adjustedChalakim / CHALAKIM_PER_MINUTE;
		this.moladChalakim = adjustedChalakim - moladMinutes * CHALAKIM_PER_MINUTE;
	}


	/**
	 * Sets the date based on a {@link java.util.Calendar Calendar} object. Modifies the Jewish date as well.
	 *
	 * @param calendar the <code>Calendar</code> to set the calendar to
	 *
	 * @throws IllegalArgumentException if the {@link java.util.Calendar#ERA} is {@link java.util.GregorianCalendar#BC}
	 */
	private void setDate( Calendar calendar ) {
		if ( calendar.get( Calendar.ERA ) == GregorianCalendar.BC ) {
			throw new IllegalArgumentException( "Calendars with a BC era are not supported. The year "
				+ calendar.get( Calendar.YEAR ) + " BC is invalid." );
		}
		gregorianMonth = calendar.get( Calendar.MONTH ) + 1;
		gregorianDayOfMonth = calendar.get( Calendar.DATE );
		gregorianYear = calendar.get( Calendar.YEAR );
		gregorianAbsDate = gregorianDateToAbsDate( gregorianYear, gregorianMonth, gregorianDayOfMonth ); // init the date
		absDateToJewishDate();

		dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // set day of week
	}


	/**
	 * Sets the hidden internal representation of the Gregorian date , and updates the Jewish date accordingly. While
	 * public getters and setters have 0 based months matching the Java Calendar classes, This class internally represents
	 * the Gregorian month starting at 1. When this is called it will not adjust the month to match the Java Calendar
	 * classes.
	 */
	private void setInternalGregorianDate(
		int year,
		int month,
		int dayOfMonth
	) {
		// make sure date is a valid date for the given month, if not, set to last day of month
		if ( dayOfMonth > getLastDayOfGregorianMonth( month, year ) ) {
			dayOfMonth = getLastDayOfGregorianMonth( month, year );
		}
		// init month, date, year
		gregorianMonth = month;
		gregorianDayOfMonth = dayOfMonth;
		gregorianYear = year;

		gregorianAbsDate = gregorianDateToAbsDate( gregorianYear, gregorianMonth, gregorianDayOfMonth ); // init date
		absDateToJewishDate();

		dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // set day of week
	}


	/**
	 * Sets the Jewish Date and updates the Gregorian date accordingly.
	 *
	 * @param year       the Jewish year. The year can't be negative
	 * @param month      the Jewish month starting with Nisan. A value of 1 is expected for Nissan ... 12 for Adar and 13
	 *                   for Adar II. Use the constants {@link HebrewMonth#NISSAN} ... {@link HebrewMonth#ADAR} (or {@link
	 *                   HebrewMonth#ADAR_II} for a leap year Adar II) to avoid any confusion.
	 * @param dayOfMonth the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that
	 *                   only has 29 days, the day will be set as 29.
	 *
	 * @throws IllegalArgumentException if a A Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month < 1 or >
	 *                                  12 (or 13 on a leap year) or the day of month is < 1 or > 30 is passed in
	 */
	private void setJewishDate(
		int year,
		int month,
		int dayOfMonth
	) {
		setJewishDate( year, month, dayOfMonth, 0, 0, 0 );
	}


	/**
	 * Sets the Jewish Date and updates the Gregorian date accordingly.
	 *
	 * @param year       the Jewish year. The year can't be negative
	 * @param month      the Jewish month starting with Nisan. A value of 1 is expected for Nissan ... 12 for Adar and 13
	 *                   for Adar II. Use the constants {@link HebrewMonth#NISSAN} ... {@link HebrewMonth#ADAR} (or {@link
	 *                   HebrewMonth#ADAR_II} for a leap year Adar II) to avoid any confusion.
	 * @param dayOfMonth the Jewish day of month. valid values are 1-30. If the day of month is set to 30 for a month that
	 *                   only has 29 days, the day will be set as 29.
	 * @param hours      the hour of the day. Used for Molad calculations
	 * @param minutes    the minutes. Used for Molad calculations
	 * @param chalakim   the chalakim/parts. Used for Molad calculations. The chalakim should not exceed 17. Minutes should
	 *                   be used for larger numbers.
	 *
	 * @throws IllegalArgumentException if a A Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month < 1 or >
	 *                                  12 (or 13 on a leap year), the day of month is < 1 or > 30, an hour < 0 or > 23, a
	 *                                  minute < 0 > 59 or chalakim < 0 > 17. For larger a larger number of chalakim such
	 *                                  as 793 (TaShTzaG) break the chalakim into minutes (18 chalakim per minutes, so it
	 *                                  would be 44 minutes and 1 chelek in the case of 793 (TaShTzaG).
	 */
	private void setJewishDate(
		int year,
		int month,
		int dayOfMonth,
		int hours,
		int minutes,
		int chalakim
	) {
		validateJewishDate( year, month, dayOfMonth, hours, minutes, chalakim );

		// if 30 is passed for a month that only has 29 days (for example by rolling the month from a month that had 30
		// days to a month that only has 29) set the date to 29th
		if ( dayOfMonth > getDaysInJewishMonth( month, year ) ) {
			dayOfMonth = getDaysInJewishMonth( month, year );
		}

		jewishMonth = month;
		jewishDay = dayOfMonth;
		jewishYear = year;
		moladHours = hours;
		moladMinutes = minutes;
		moladChalakim = chalakim;

		gregorianAbsDate = jewishDateToAbsDate( jewishYear, jewishMonth, jewishDay ); // reset Gregorian date
		absDateToDate( gregorianAbsDate );

		dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // reset day of week
	}



	/**
	 * Validates the components of a Jewish date for validity. It will throw an {@link IllegalArgumentException} if the
	 * Jewish date is earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month < 1 or > 12 (or 13 on a {@link
	 * #isJewishLeapYear(int) leap year}), the day of month is < 1 or > 30, an hour < 0 or > 23, a minute < 0 > 59 or
	 * chalakim < 0 > 17. For larger a larger number of chalakim such as 793 (TaShTzaG) break the chalakim into minutes (18
	 * chalakim per minutes, so it would be 44 minutes and 1 chelek in the case of 793/TaShTzaG).
	 *
	 * @param year       the Jewish year to validate. It will reject any year <= 3761 (lower than the year 1 Gregorian).
	 * @param month      the Jewish month to validate. It will reject a month < 1 or > 12 (or 13 on a leap year) .
	 * @param dayOfMonth the day of the Jewish month to validate. It will reject any value < 1 or > 30 TODO: check calling
	 *                   methods to see if there is any reason that the class can validate that 30 is invalid for some
	 *                   months.
	 * @param hours      the hours (for molad calculations). It will reject an hour < 0 or > 23
	 * @param minutes    the minutes (for molad calculations). It will reject a minute < 0 or > 59
	 * @param chalakim   the chalakim/parts (for molad calculations). It will reject a chalakim < 0 or > 17. For larger
	 *                   numbers such as 793 (TaShTzaG) break the chalakim into minutes (18 chalakim per minutes, so it
	 *                   would be 44 minutes and 1 chelek in the case of 793/TaShTzaG)
	 *
	 * @throws IllegalArgumentException if a A Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month < 1 or >
	 *                                  12 (or 13 on a leap year), the day of month is < 1 or > 30, an hour < 0 or > 23, a
	 *                                  minute < 0 > 59 or chalakim < 0 > 17. For larger a larger number of chalakim such
	 *                                  as 793 (TaShTzaG) break the chalakim into minutes (18 chalakim per minutes, so it
	 *                                  would be 44 minutes and 1 chelek in the case of 793 (TaShTzaG).
	 */
	private static void validateJewishDate(
		int year,
		int month,
		int dayOfMonth,
		int hours,
		int minutes,
		int chalakim
	) {
		if ( month < HebrewMonth.NISSAN || month > getLastMonthOfJewishYear( year ) ) {
			throw new IllegalArgumentException( "The Jewish month has to be between 1 and 12 (or 13 on a leap year). "
				+ month + " is invalid for the year " + year + "." );
		}
		if ( dayOfMonth < 1 || dayOfMonth > 30 ) {
			throw new IllegalArgumentException( "The Jewish day of month can't be < 1 or > 30.  " + dayOfMonth + " is invalid." );
		}
		// reject dates prior to 18 Teves, 3761 (1/1/1 AD). This restriction can be relaxed if the date coding is
		// changed/corrected
		if ( ( year < EARLIEST_VALID_HEBREW_YEAR ) ||
			( year == EARLIEST_VALID_HEBREW_YEAR && ( month >= HebrewMonth.TISHREI && month < HebrewMonth.TEVES ) ) ||
			( year == EARLIEST_VALID_HEBREW_YEAR && month == HebrewMonth.TEVES && dayOfMonth < 18 ) ) {
			throw new IllegalArgumentException(
				"A Jewish date earlier than 18 Teves, 3761 (1/1/1 Gregorian) can't be set. " + year + ", " + month +
					", " + dayOfMonth + " is invalid." );
		}
		if ( hours < 0 || hours > 23 ) {
			throw new IllegalArgumentException( "Hours < 0 > 23 can't be set. " + hours + " is invalid." );
		}

		if ( minutes < 0 || minutes > 59 ) {
			throw new IllegalArgumentException( "Minutes < 0 > 59 can't be set. " + minutes + " is invalid." );
		}

		if ( chalakim < 0 || chalakim > 17 ) {
			throw new IllegalArgumentException(
				"Chalakim/parts < 0 > 17 can't be set. " +
					chalakim + " is invalid. For larger numbers such as 793 (TaShTzaG) break the chalakim into minutes " +
					"(18 chalakim per minutes, so it would be 44 minutes and 1 chelek in the case of 793 (TaShTzaG)" );
		}
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Base class overrides

	/**
	 * Compares two dates as per the compareTo() method in the Comparable interface. Returns a value less than 0 if this
	 * date is "less than" (before) the date, greater than 0 if this date is "greater than" (after) the date, or 0 if they
	 * are equal.
	 */
	public int compareTo( JewishDate jewishDate ) {
		return gregorianAbsDate < jewishDate.gregorianAbsDate ? -1 : gregorianAbsDate > jewishDate.gregorianAbsDate ? 1 : 0;
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

		//noinspection RedundantIfStatement
		if ( jewishYear != that.jewishYear ) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		int result = jewishMonth;
		result = 31 * result + jewishDay;
		result = 31 * result + jewishYear;
		return result;
	}


	@Override
	public String toString() {
		return "JewishDate{" +
			"jewishMonth=" + jewishMonth +
			", jewishDay=" + jewishDay +
			", jewishYear=" + jewishYear +
			", moladHours=" + moladHours +
			", moladMinutes=" + moladMinutes +
			", moladChalakim=" + moladChalakim +
			", gregorianMonth=" + gregorianMonth +
			", gregorianDayOfMonth=" + gregorianDayOfMonth +
			", gregorianYear=" + gregorianYear +
			", dayOfWeek=" + dayOfWeek +
			", gregorianAbsDate=" + gregorianAbsDate +
			'}';
	}
}
