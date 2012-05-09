/*
 * Copyright (c) 2011. Jay R. Gindin
 */
package com.gindin.zmanim.calendar;


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * In the Hebrew calendar, the six single year lengths give rise to the terms haser, qesidrah, and shalem.
 *
 * Haser is the term applied to Hebrew years that are is either 353 or 383 days long. Such year
 *  lengths are accommodated by removing the 30th day of the month of Kislev. In English, the
 *  term haser is very often translated either as deficient or imperfect. The Hebrew letter het
 *  is used to denote either one of these year lengths.
 *
 * Qesidrah is the term applied to Hebrew years that are is either 354 or 384 days long. Such
 *  year lengths are accommodated by keeping all of their month's lengths intact. In English,
 *  the term qesidrah is very often translated either as regular or intermediate. The Hebrew
 *  letter chof is used to denote either one of these year lengths.
 *
 * Shalem is the term applied to Hebrew years that are is either 355 or 385 days long. Such year
 *  lengths are accommodated by adding a 30th day to the month of Heshvan. In English, the term
 *  shalem is very often translated either as abundant or perfect. The Hebrew letter shiyyen is
 *  used to denote either one of these year lengths.
 *<p>
 * (Taken from http://www.geocities.com/Athens/1584/, which is unfortunately no longer available
 *  except via the Way Back Machine.)
 *<p>
 * Putting this together, we get the following table:
 *    Month       Gregorian Equivalent  Haser Length    Qesidrah Length   Shalem Length
 *    =======     ====================  =============   ================  =============
 *    1.  Nissan 	  March-April           30 days         30 days           30 days
 *    2.  Iyar 	    April-May             29 days 	      29 days 	        29 days
 *    3.  Sivan 	  May-June              30 days 	      30 days 	        30 days
 *    4.  Tammuz 	  June-July             29 days 	      29 days 	        29 days
 *    5.  Av        July-August           30 days 	      30 days 	        30 days
 *    6.  Elul 	    August-September      29 days 	      29 days 	        29 days
 *    7.  Tishri 	  September-October     30 days         30 days           30 days
 *    8.  Heshvan   October-November      29 days 	      29 days 	        30 days
 *    9.  Kislev 	  November-December     29 days 	      30 days 	        30 days
 *    10. Tevet 	  December-January      29 days         29 days           29 days
 *    11. Shevat 	  January-February      30 days 	      30 days 	        30 days
 *    12. Adar      February-March        29 days 	      29 days 	        29 days
 *    12. Adar I    February-March        30 days 	      30 days 	        30 days
 *    13. Adar II   March-April           29 days         29 days           29 days
 *                                    ----------      ----------        ---------
 *                                    353 / 383       354/384           355/385
 *<p>
 * (Partially taken from http://www.jewishvirtuallibrary.org/jsource/Judaism/calendar.html.)
 *<p></p>
 *
 * The HebrewCalendar class allows one to maintain an instance of a Gregorian date along with the corresponding Jewish date.
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
 * <p/>
 * This class has migrated to become a calculation engine, computing the necessary values to return instances of the
 * HebrewDate class. Some reasons for doing this include a clearer separation of concerns and thread safety.
 *
 * @author &copy; Avrom Finkelstien 2002
 * @author &copy; Eliyahu Hershfeld 2011
 * @author &copy; Jay Gindin 2011
 */
@SuppressWarnings( { "MagicNumber", "UnusedDeclaration" } )
public class HebrewCalendar {

	
	public static final int                                   MINUTES_PER_HOUR = 60;
	public static final int                                   HOURS_PER_DAY = 24;

	/**
	 * the Jewish epoch using the RD (Rata Die/Fixed Date or Reingold Dershowitz) day used in Calendrical Calculations. Day
	 * 1 is January 1, 0001 Gregorian
	 */
	private static final int                                  JEWISH_EPOCH = -1373429;

	public static final int                                   CHALAKIM_PER_MINUTE = 18;

	public static final int                                   CHALAKIM_PER_HOUR = CHALAKIM_PER_MINUTE * MINUTES_PER_HOUR; // =1080

	public static final int                                   CHALAKIM_PER_DAY = CHALAKIM_PER_HOUR * HOURS_PER_DAY; // =25920

	public static final long                                  CHALAKIM_PER_MONTH = 765433; // (29 * 24 + 12) * 1080 + 793


	/**
	 * Days from the beginning of Sunday till molad BaHaRaD. Calculated as 1 day, 5 hours and 204 chalakim = (24 + 5) *
	 * 1080 + 204 = 31524
	 */
	private static final int                                  CHALAKIM_MOLAD_TOHU = 31524;

	/** Reject dates prior to 18 Teves, 3761 (1/1/1 AD). This restriction can be relaxed if the date coding is changed/corrected. */
	private static final int                                  EARLIEST_VALID_HEBREW_YEAR = 3761;

	private static final int                                  YEARS_PER_CYCLE = 19;


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// 

	/** Default constructor to prevent creation */
	private HebrewCalendar() {
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Date navigation

	public static HebrewDate today() {
		Calendar calendar = Calendar.getInstance();
		return getForDate( calendar );
	}
	

	/**
	 * Creates a Jewish date based on a Jewish year, month and day of month.
	 *
	 * @param jewishYear       the Jewish year
	 * @param jewishMonth      the Jewish month. The method expects a 1 for Nissan ... 12 for Adar and 13 for Adar II. Use
	 *                         the constants {@link HebrewMonth#NISSAN} ... {@link HebrewMonth#ADAR} (or {@link
	 *                         HebrewMonth#ADAR_II} for a leap year Adar II) to avoid any confusion.
	 * @param jewishDayOfMonth the Jewish day of month. If 30 is passed in for a month with only 29 days (for example
	 *                         {@link HebrewMonth#IYAR}, or {@link HebrewMonth#KISLEV} in a year where Kislev is short,
	 *                         the 29th (last valid date of the month) will be set
	 *
	 * @throws IllegalArgumentException if the day of month is < 1 or > 30, or a year of < 0 is passed in.
	 */
	public static HebrewDate getForJewishDate(
		int jewishYear,
		@HebrewMonth.Constant int jewishMonth,
		int jewishDayOfMonth
	) {
		return getForJewishDate( jewishYear, jewishMonth, jewishDayOfMonth, 0, 0, 0 );
	}
	
	
	public static HebrewDate getForJewishDate(
		int jewishYear,
		@HebrewMonth.Constant int jewishMonth,
		int jewishDayOfMonth,
		int moladHours,
		int moladMinutes,
		int moladChalakim
	) {
		
		validateJewishDate( jewishYear, jewishMonth, jewishDayOfMonth, moladHours, moladMinutes, moladChalakim );

		// if 30 is passed for a month that only has 29 days (for example by rolling the month from a month that had 30
		// days to a month that only has 29) set the date to 29th
		if ( jewishDayOfMonth > getDaysInHebrewMonth( jewishMonth, jewishYear ) ) {
			jewishDayOfMonth = getDaysInHebrewMonth( jewishMonth, jewishYear );
		}

		int gregorianAbsDate = jewishDateToAbsDate( jewishYear, jewishMonth, jewishDayOfMonth );
		
		int year = computeGregorianYearFromAbsoluteDate( gregorianAbsDate );
		int month = computeGregorianMonthFromAbsoluteDate( gregorianAbsDate, year );
		int dayOfMonth = computeGregorianDayOfMonthFromAbsoluteDate( gregorianAbsDate, year, month );

		@Constants.CalendarDay int dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // reset day of week

		return new HebrewDate( jewishYear, jewishMonth, jewishDayOfMonth,
			year, month, dayOfMonth, dayOfWeek, gregorianAbsDate );
	}

		
	
	/**
	 * A constructor that initializes the date to the {@link java.util.Date Date} parameter.
	 *
	 * @param gregorianYear The Gregorian year, which must be >= 1. BCE dates are not supported.
	 * @param gregorianMonth The Gregorian month, using the same numbering format as the Calendar class does for months.
	 * @param gregorianDayOfMonth The day of the Gregorian month, the maximum value of which depends on the specified month.
	 *
	 * @throws IllegalArgumentException if the date would fall prior to the January 1, 1 AD
	 */
	public static HebrewDate getForDate(
		int gregorianYear,
	  @Constants.CalendarMonth int gregorianMonth,
	  int gregorianDayOfMonth
	) {

		if ( gregorianYear < 1 ) {
			throw new IllegalArgumentException( "Dates prior to year 1 of the CE are not supported." );
		}

		int gregorianAbsDate = gregorianDateToAbsDate( gregorianYear, gregorianMonth, gregorianDayOfMonth ); // init the date

		int jewishYear = computeJewishYearFromAbsoluteDate( gregorianAbsDate );
		int jewishMonth = computeJewishMonthFromAbsoluteDate( gregorianAbsDate, jewishYear );
		int jewishDay = computeJewishDayFromAbsoluteDate( gregorianAbsDate, jewishYear, jewishMonth );

		@Constants.CalendarDay int dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // set day of week

		return new HebrewDate( jewishYear, jewishMonth, jewishDay,
			gregorianYear, gregorianMonth, gregorianDayOfMonth,
			dayOfWeek, gregorianAbsDate );
	}
	
	
	/**
	 * A constructor that initializes the date to the {@link java.util.Calendar Calendar} parameter.
	 *
	 * @param calendar the <code>Calendar</code> to set the calendar to
	 *
	 * @throws IllegalArgumentException if the {@link java.util.Calendar#ERA} is {@link java.util.GregorianCalendar#BC}
	 */
	public static HebrewDate getForDate(
		Calendar calendar
	) {

		if ( calendar.get( Calendar.ERA ) == GregorianCalendar.BC ) {
			throw new IllegalArgumentException( "Calendars with a BCE era are not supported. The year "
				+ calendar.get( Calendar.YEAR ) + " BCE is invalid." );
		}

		@Constants.CalendarMonth int gregorianMonth = calendar.get( Calendar.MONTH );
		int gregorianDayOfMonth = calendar.get( Calendar.DATE );
		int gregorianYear = calendar.get( Calendar.YEAR );

		return getForDate( gregorianYear, gregorianMonth, gregorianDayOfMonth );
	}
	
	
	/**
	 * Constructor that creates a HebrewCalendar based on a molad passed in. The molad would be the number of chalakim/parts
	 * starting at the begining of Sunday prior to the molad Tohu BeHaRaD (Be = Monday, Ha= 5 hours and Rad =204
	 * chalakim/parts) - prior to the start of the Jewish calendar. BeHaRaD is 23:11:20 on Sunday night(5 hours 204/1080
	 * chalakim after sunset on Sunday evening).
	 */
	public static HebrewDate getForMolad( long molad ) {
		
		int absDate = moladToAbsDate( molad );
		int year = computeGregorianYearFromAbsoluteDate( absDate );
		@Constants.CalendarMonth int month = computeGregorianMonthFromAbsoluteDate( absDate, year );
		int dayOfMonth = computeGregorianDayOfMonthFromAbsoluteDate( absDate, year, month );
		int gregorianAbsDate = gregorianDateToAbsDate( year, month, dayOfMonth ); // init date


		int jewishYear = computeJewishYearFromAbsoluteDate( gregorianAbsDate );
		@HebrewMonth.Constant int jewishMonth = computeJewishMonthFromAbsoluteDate( gregorianAbsDate, jewishYear );
		int jewishDay = computeJewishDayFromAbsoluteDate( gregorianAbsDate, jewishYear, jewishMonth );

		@Constants.CalendarDay int dayOfWeek = Math.abs( gregorianAbsDate % 7 ) + 1; // set day of week

		// long chalakimSince = getChalakimSinceMoladTohu(year, HebrewCalendar.TISHREI);// tishrei
		int conjunctionDay = (int)( molad / CHALAKIM_PER_DAY );
		int adjustedChalakim = (int)( molad - conjunctionDay * CHALAKIM_PER_DAY );
		int moladHours = adjustedChalakim / CHALAKIM_PER_HOUR;
		adjustedChalakim = adjustedChalakim - ( moladHours * CHALAKIM_PER_HOUR );
		int moladMinutes = adjustedChalakim / CHALAKIM_PER_MINUTE;
		int moladChalakim = adjustedChalakim - moladMinutes * CHALAKIM_PER_MINUTE;

		return new HebrewDate( jewishYear, jewishMonth, jewishDay, year, month, dayOfMonth, dayOfWeek, gregorianAbsDate,
			moladHours, moladMinutes, moladChalakim);
	}
	
	
	/**
	 * Rolls the date forward by 1 day. It modifies both the Gregorian and Jewish dates accordingly. The API does not
	 *  currently offer the ability to forward month or year. If such manipulation is required use the
	 *  {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or
	 *  {@link java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the HebrewCalendar
	 * 	cal.add(Calendar.MONTH, 3); // add 3 Gregorian months
	 * 	HebrewCalendar newDate = new HebrewCalendar(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 */
	static HebrewDate forward(
		HebrewDate today
	) {
		return forward( today, 1 );
	}


	/**
	 * Rolls the date forward by the specified number of days. It modifies both the Gregorian and Jewish dates
	 *  accordingly. The API does not currently offer the ability to forward month or year. If such manipulation
	 *  is required use the {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or
	 *  {@link java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the HebrewCalendar
	 * 	cal.add(Calendar.MONTH, 3); // add 3 Gregorian months
	 * 	HebrewCalendar newDate = new HebrewCalendar(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 */
	@SuppressWarnings( "FeatureEnvy" )
	static HebrewDate forward(
		HebrewDate  today,
		int         numDays
	) {

		int newGregorianYear = today.getGregorianYear();
		int newGregorianMonth = today.getGregorianMonth();
		int newGregorianDayOfMonth = today.getGregorianDayOfMonth();

		int newJewishYear = today.getHebrewYear();
		int newJewishMonth = today.getHebrewMonth();
		int newJewishDay = today.getHebrewDayOfMonth();

		@Constants.CalendarDay int newDayOfWeek = today.getDayOfWeek();

		for ( int update = 0; update < numDays; update++ ) {

			// Change Gregorian date
			if ( newGregorianDayOfMonth == getLastDayOfGregorianMonth( newGregorianMonth, newGregorianYear ) ) {
				// if last day of year
				if ( newGregorianMonth == Calendar.DECEMBER ) {
					newGregorianYear++;
					newGregorianMonth = Calendar.JANUARY;
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
			if ( newJewishDay == getDaysInHebrewMonth( newJewishMonth, newJewishYear ) ) {
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

			if ( Calendar.SATURDAY == newDayOfWeek ) {
				// if last day of week, loop back to Sunday
				newDayOfWeek = Calendar.SUNDAY;
			}
			else {
				newDayOfWeek++;
			}
		}

		// increment the absolute date
		int newGregorianAbsDate = today.getGregorianAbsDate() + numDays;

		return new HebrewDate( newJewishYear, newJewishMonth, newJewishDay,
			newGregorianYear, newGregorianMonth, newGregorianDayOfMonth, newDayOfWeek, newGregorianAbsDate );
	}


	/**
	 * Rolls the date back by the specified number of days. It modifies both the Gregorian and Jewish dates
	 *  accordingly. The API does not currently offer the ability to forward month or year. If such manipulation
	 *  is required use the {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or
	 *  {@link java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the HebrewCalendar
	 * 	cal.add(Calendar.MONTH, -3); // subtract 3 Gregorian months
	 * 	HebrewCalendar newDate = new HebrewCalendar(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 */
	@SuppressWarnings( "FeatureEnvy" )
	static HebrewDate back(
		HebrewDate today
	) {
		return back( today, 1 );
	}

	/**
	 * Rolls the date back by the specified number of days. It modifies both the Gregorian and Jewish dates accordingly.
	 *  The API does not currently offer the ability to forward by month or year. If such manipulation is required use
	 *  the {@link java.util.Calendar} class {@link java.util.Calendar#add(int, int)} or {@link java.util.Calendar#roll(int, int)} methods in the following manner.
	 * <p/>
	 * <pre>
	 * <code>
	 * 	Calendar cal = jewishDate.getTime(); // get a java.util.Calendar representation of the HebrewCalendar
	 * 	cal.add(Calendar.MONTH, -3); // subtract 3 Gregorian months
	 * 	HebrewCalendar newDate = new HebrewCalendar(cal); // set the updated calendar back to this class
	 * </code>
	 * </pre>
	 */
	@SuppressWarnings( "FeatureEnvy" )
	static HebrewDate back(
		HebrewDate  today,
	  int         numDays
	) {

		int newGregorianYear = today.getGregorianYear();
		@Constants.CalendarMonth int newGregorianMonth = today.getGregorianMonth();
		int newGregorianDayOfMonth = today.getGregorianDayOfMonth();

		int newJewishYear = today.getHebrewYear();
		int newJewishMonth = today.getHebrewMonth();
		int newJewishDay = today.getHebrewDayOfMonth();
		@Constants.CalendarDay int newDayOfWeek = 0;


		for ( int update = 0; update < numDays; update++  ) {

			// Change Gregorian date
			// if first day of month...
			if ( newGregorianDayOfMonth == 1 ) {

				// if first day of year...
				if ( Calendar.JANUARY == newGregorianMonth ) {
					newGregorianMonth = Calendar.DECEMBER;
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
			// if first day of the Jewish month...
			if ( newJewishDay == 1 ) {
				if ( newJewishMonth == HebrewMonth.NISSAN ) {
					newJewishMonth = getLastMonthOfJewishYear( newJewishYear );
				}
				else if ( newJewishMonth == HebrewMonth.TISHREI ) {
					// if Rosh Hashana
					newJewishYear--;
				}

				newJewishMonth--;
				newJewishDay = getDaysInHebrewMonth( newJewishMonth, newJewishYear );
			}
			else {
				newJewishDay--;
			}

			newDayOfWeek = today.getDayOfWeek();
			if ( Calendar.SUNDAY == newDayOfWeek ) {
				// if first day of week, loop back to Saturday
				newDayOfWeek = Calendar.SATURDAY;
			}
			else {
				newDayOfWeek--;
			}
		}

		// change the absolute date
		int newGregorianAbsDate = today.getGregorianAbsDate() - numDays;

		return new HebrewDate( newJewishYear, newJewishMonth, newJewishDay,
			newGregorianYear, newGregorianMonth, newGregorianDayOfMonth, newDayOfWeek, newGregorianAbsDate );
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//


	/**
	 * Returns the number of days in a given month in a given month and year.
	 *
	 * @param month the month.
	 * @param year  the year (only impacts February)
	 *
	 * @return the number of days in the month in the given year
	 */
	static int getLastDayOfGregorianMonth(
		int month,
		int year
	) {
		switch ( month ) {
			case Calendar.FEBRUARY:
				if ( ( year % 4 == 0 && year % 100 != 0 ) || ( year % 400 == 0 ) ) {
					return 29;
				}
				else {
					return 28;
				}
			case Calendar.APRIL:
			case Calendar.JUNE:
			case Calendar.SEPTEMBER:
			case Calendar.NOVEMBER:
				return 30;
			default:
				return 31;
		}
	}



	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Gregorian / Absolute date conversions


	private static int computeGregorianYearFromAbsoluteDate( int absDate ) {
		int year = absDate / 366; // Search forward year by year from approximate year
		while ( absDate >= gregorianDateToAbsDate( year + 1, Calendar.JANUARY, 1 ) ) {
			year++;
		}
		return year;
	}


	@Constants.CalendarMonth
	private static int computeGregorianMonthFromAbsoluteDate(
		int absDate,
		int year
	) {

		@Constants.CalendarMonth int month = 0; // Search forward month by month from January
		while ( absDate > gregorianDateToAbsDate( year, month, getLastDayOfGregorianMonth( month, year ) ) ) {
			month++;
		}
		return month;
	}


	private static int computeGregorianDayOfMonthFromAbsoluteDate(
		int absDate,
		int year,
		int month
	) {
		int dayOfMonth = absDate - gregorianDateToAbsDate( year, month, 1 ) + 1;

		// make sure date is a valid date for the given month, if not, set to last day of month
		if ( dayOfMonth > getLastDayOfGregorianMonth( month, year ) ) {
			dayOfMonth = getLastDayOfGregorianMonth( month, year );
		}
		return dayOfMonth;
	}


	/**
	 * Computes the absolute date from a Gregorian date. ND+ER
	 *
	 * @param year       the Gregorian year
	 * @param month      the Gregorian month
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
		for ( int m = month - 1; m >= 0; m-- ) {
			absDate += getLastDayOfGregorianMonth( m, year ); // days in prior months of the year
		}
		return ( absDate // days this year
			+ 365 * ( year - 1 ) // days in previous years ignoring leap days
			+ ( year - 1 ) / 4 // Julian leap days before this year
			- ( year - 1 ) / 100 // minus prior century years
			+ ( year - 1 ) / 400 ); // plus prior years divisible by 400
	}


	private static int computeJewishYearFromAbsoluteDate( int gregorianAbsDate ) {
		// Approximation from below
		int jewishYear = ( gregorianAbsDate + JEWISH_EPOCH ) / 366;
		// Search forward for year from the approximation
		while ( gregorianAbsDate >= jewishDateToAbsDate( jewishYear + 1, HebrewMonth.TISHREI, 1 ) ) {
			jewishYear++;
		}
		return jewishYear;
	}


	@HebrewMonth.Constant
	private static int computeJewishMonthFromAbsoluteDate( int gregorianAbsDate, int jewishYear ) {
		
		int jewishMonth;
		
		// Search forward for month from either Tishri or Nisan.
		if ( gregorianAbsDate < jewishDateToAbsDate( jewishYear, HebrewMonth.NISSAN, 1 ) ) {
			jewishMonth = HebrewMonth.TISHREI;// Start at Tishri
		}
		else {
			jewishMonth = HebrewMonth.NISSAN;// Start at Nisan
		}
		while ( gregorianAbsDate > jewishDateToAbsDate( jewishYear, jewishMonth, getDaysInHebrewMonth( jewishMonth, jewishYear ) ) ) {
			jewishMonth++;
		}
		
		return jewishMonth;
	}


	private static int computeJewishDayFromAbsoluteDate(
		int gregorianAbsDate,
		int jewishYear,
		int jewishMonth
	) {
		// Calculate the day by subtraction
		return  gregorianAbsDate - jewishDateToAbsDate( jewishYear, jewishMonth, 1 ) + 1;
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
		return elapsed + getElapsedDays( year ) + JEWISH_EPOCH;
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
	 */
	static boolean isHebrewLeapYear( int year ) {
		return ( ( 7 * year ) + 1 ) % YEARS_PER_CYCLE < 7;
	}


	/**
	 * Returns the last month of a given Jewish year. This will be 12 on a non {@link #isHebrewLeapYear(int) leap year} or
	 * 13 on a leap year.
	 *
	 * @param year the Jewish year.
	 *
	 * @return 12 on a non leap year or 13 on a leap year
	 *
	 * @see #isHebrewLeapYear(int)
	 */
	private static int getLastMonthOfJewishYear( int year ) {
		return isHebrewLeapYear( year ) ? HebrewMonth.ADAR_II : HebrewMonth.ADAR;
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
	static int getElapsedDays( int year ) {
		long chalakimSince = getChalakimSinceMoladTohu( year, HebrewMonth.TISHREI );
		int moladDay = (int)( chalakimSince / CHALAKIM_PER_DAY );
		int moladParts = (int)( chalakimSince - moladDay * CHALAKIM_PER_DAY );
		// delay Rosh Hashana for the 4 dechiyos
		return addDechiyos( year, moladDay, moladParts );
	}


	/**
	 * Converts the the {@link HebrewMonth#NISSAN} based constants used by this class to numeric month starting from {@link
	 * HebrewMonth#TISHREI}. This is required for Molad calculations.
	 */
	private static int getJewishMonthOfYear(
		int jewishYear,
		int jewishMonth
	) {
		boolean isLeapYear = isHebrewLeapYear( jewishYear );
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
	private static int getDaysInJewishYear( int year ) {
		return getElapsedDays( year + 1 ) - getElapsedDays( year );
	}


	/**
	 * Returns if Cheshvan is long in a given Jewish year. The method name isLong is done since in a Qesidrah (ordered)
	 * year Cheshvan is short. ND+ER
	 *
	 * @param year the year
	 *
	 * @return true if Cheshvan is long in Jewish year.
	 */
	static boolean isCheshvanLong( int year ) {
		return getDaysInJewishYear( year ) % 10 == 5;
	}



	/**
	 * Returns if Kislev is short (29 days VS 30 days) in a given Jewish year. The method name isShort is done since in a
	 * Qesidrah (ordered) year Kislev is long. ND+ER
	 *
	 * @param year the Jewish year
	 *
	 * @return true if Kislev is short for the given Jewish year.
	 */
	static boolean isKislevShort( int year ) {
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
	static int getDaysInHebrewMonth(
		int month,
		int year
	) {
		if ( ( month == HebrewMonth.IYAR ) || ( month == HebrewMonth.TAMMUZ ) || ( month == HebrewMonth.ELUL ) || ( ( month == HebrewMonth.CHESHVAN ) && !( isCheshvanLong( year ) ) )
			|| ( ( month == HebrewMonth.KISLEV ) && isKislevShort( year ) ) || ( month == HebrewMonth.TEVET )
			|| ( ( month == HebrewMonth.ADAR ) && !( isHebrewLeapYear( year ) ) ) || ( month == HebrewMonth.ADAR_II ) ) {
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
				elapsedDays += getDaysInHebrewMonth( m, year );
			}
			for ( int m = HebrewMonth.NISSAN; m < month; m++ ) {
				elapsedDays += getDaysInHebrewMonth( m, year );
			}
		}
		else { // Add days in prior months this year
			for ( int m = HebrewMonth.TISHREI; m < month; m++ ) {
				elapsedDays += getDaysInHebrewMonth( m, year );
			}
		}
		return elapsedDays;
	}


	/**
	 * Returns the Cheshvan and Kislev kviah (whether a Hebrew year is short, regular or long). It will return {@link
	 * HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if Cheshvan is 29
	 * days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
	 *
	 * @return {@link HebrewYearType#SHALEM} if both Cheshvan and Kislev are 30 days, {@link HebrewYearType#QESIDRAH} if
	 *         Cheshvan is 29 days and Kislev is 30 days and {@link HebrewYearType#HASER} if both are 29 days.
	 *
	 * @see #isCheshvanLong(int)
	 * @see #isKislevShort(int)
	 */
	public static HebrewYearType getYearType( int year ) {

		final boolean cheshvanLong = isCheshvanLong( year );
		final boolean kislevShort = isKislevShort( year );

		if ( cheshvanLong && !kislevShort ) {
			return HebrewYearType.SHALEM;
		}
		else if ( !cheshvanLong && kislevShort ) {
			return HebrewYearType.HASER;
		}
		else {
			return HebrewYearType.QESIDRAH;
		}
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
	 * @return the number of elapsed days in the HebrewCalendar adjusted for the 4 dechiyos.
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
			&& !isHebrewLeapYear( year ) ) // of a non-leap year - end Dechiya of GaTRaD
			|| ( ( ( moladDay % 7 ) == 1 ) // start Dechiya of BeTuTaKFoT - Be = is on a Monday
			&& ( moladParts >= 16789 ) // TRaD = 15 hours, 589 parts or later (15 * 1080 + 589)
			&& ( isHebrewLeapYear( year - 1 ) ) ) ) { // in a year following a leap year - end Dechiya of BeTuTaKFoT
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


	static long getChalakimSinceMoladTohu(
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
	 * Validates the components of a Jewish date for validity. It will throw an {@link IllegalArgumentException} if the
	 * Jewish date is earlier than 18 Teves, 3761 (1/1/1 Gregorian), a month < 1 or > 12 (or 13 on a {@link
	 * #isHebrewLeapYear(int) leap year}), the day of month is < 1 or > 30, an hour < 0 or > 23, a minute < 0 > 59 or
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
			( year == EARLIEST_VALID_HEBREW_YEAR && ( month >= HebrewMonth.TISHREI && month < HebrewMonth.TEVET ) ) ||
			( year == EARLIEST_VALID_HEBREW_YEAR && month == HebrewMonth.TEVET && dayOfMonth < 18 ) ) {
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

}
