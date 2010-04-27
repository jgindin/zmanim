/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import java.util.Date;

/**
 * Adds support for determining additional information about the current day, including whether or not
 *  the day is a holiday.
 */
public class HebrewCalendar 
	extends com.ibm.icu.util.HebrewCalendar {

	/** Indicates the getDayOfOmer() is not returning a day that is part of the omer. */
	public static final int                                   NOT_COUNTING_OMER = -1;


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
	 *    Month     Gregorian Equivalent  Haser Length    Qesidrah Length   Shalem Length
	 *    =======   ====================  =============   ================  =============
	 *    Nissan 	  March-April           30 days         30 days           30 days
	 *    Iyar 	    April-May             29 days 	      29 days 	        29 days
	 *    Sivan 	  May-June              30 days 	      30 days 	        30 days
	 *    Tammuz 	  June-July             29 days 	      29 days 	        29 days
	 *    Av        July-August           30 days 	      30 days 	        30 days
	 *    Elul 	    August-September      29 days 	      29 days 	        29 days
	 *    Tishri 	  September-October     30 days         30 days           30 days
	 *    Heshvan   October-November      29 days 	      29 days 	        30 days
	 *    Kislev 	  November-December     29 days 	      30 days 	        30 days
	 *    Tevet 	  December-January      29 days         29 days           29 days
	 *    Shevat 	  January-February      30 days 	      30 days 	        30 days
	 *    Adar      February-March        29 days 	      29 days 	        29 days
	 *    Adar I    February-March        30 days 	      30 days 	        30 days
	 *    Adar II   March-April           29 days         29 days           29 days
	 *                                    ----------      ----------        ---------
	 *                                    353 / 383       354/384           355/385
	 *<p>
	 * (Partially taken from http://www.jewishvirtuallibrary.org/jsource/Judaism/calendar.html.)
   */
	private static enum YearType {

		Haser,
		Qesidrah,
		Shalem
	}

	private static final int                                  HASER_REGULAR_LENGTH = 353;
	private static final int                                  HASER_LEAP_LENGTH = 383;
	private static final int                                  QESIDRAH_REGULAR_LENGTH = 354;
	private static final int                                  QESIDRAH_LEAP_LENGTH = 384;
	private static final int                                  SHALEM_REGULAR_LENGTH = 355;
	private static final int                                  SHALEM_LEAP_LENGTH = 385;

	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishHoliday isTodayAHoliday() {

		for ( JewishHoliday jewishHoliday : JewishHoliday.JEWISH_HOLIDAYS ) {
			if ( jewishHoliday.isOn( this ) ) {
				return jewishHoliday;
			}

			if ( jewishHoliday.isErev( this ) ) {
				return JewishHoliday.createErevOf( jewishHoliday );
			}
		}

		return null;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishHoliday isTodayATaanit() {

		for ( JewishHoliday jewishHoliday : JewishHoliday.ARBA_TAANIOT ) {
			if ( jewishHoliday.isOn( this ) ) {
				return jewishHoliday;
			}

			if ( jewishHoliday.isErev( this ) ) {
				return JewishHoliday.createErevOf( jewishHoliday );
			}
		}

		return null;
	}
	

	public boolean showCandlelighting() {

		JewishHoliday todaysHoliday = isTodayAHoliday();
		if ( null != todaysHoliday ) {
			return todaysHoliday.showCandlelighting();
		}

		return FRIDAY == get( DAY_OF_WEEK );
	}


	public boolean showHavdalah() {

		boolean show = false;

		// Is today a holiday, but not tomorrow?
		if ( null != isTodayAHoliday() ) {

			Date today = getTime();
			add( DATE, 1 );
			if ( null == isTodayAHoliday() ) {
				show = true;
			}

			setTime( today );
		}

		if ( !show ) {
			show = ( SATURDAY == get( DAY_OF_WEEK ) );
		}

		return show;
	}


	/**
	 * We treat Rosh Chodesh as a holiday too.
	 *
	 * @return the Rosh Chodesh holiday, or null if this isn't a Rosh Chodesh.
	 */
	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishHoliday isRoshChodesh() {

		JewishHoliday[] roshChodeshim;

		switch ( getYearType() ) {
			case Haser:
				roshChodeshim = JewishHoliday.ROSH_CHODESH_HASER;
				break;

			case Qesidrah:
				roshChodeshim = JewishHoliday.ROSH_CHODESH_QESIDRAH;
				break;

			case Shalem:
				roshChodeshim = JewishHoliday.ROSH_CHODESH_SHALEM;
				break;

			default:
				return null;
		}


		for ( JewishHoliday roshChodesh : roshChodeshim ) {
			if ( roshChodesh.isOn( this ) ) {
				return roshChodesh;
			}

			if ( roshChodesh.isErev( this ) ) {
				return JewishHoliday.createErevOf( roshChodesh );
			}
		}

		if ( isLeapYear() ) {
			roshChodeshim = JewishHoliday.ROSH_CHODESH_ADAR_LEAP;
		}
		else {
			roshChodeshim = JewishHoliday.ROSH_CHODESH_ADAR_REGULAR;
		}

		for ( JewishHoliday roshChodesh : roshChodeshim ) {
			if ( roshChodesh.isOn( this ) ) {
				return roshChodesh;
			}

			if ( roshChodesh.isErev( this ) ) {
				return JewishHoliday.createErevOf( roshChodesh );
			}
		}

		return null;
	}


	/**
	 * @return -1 if today is not part of the omer; the daily count, otherwise.
	 */
	public int getDayOfOmer() {

		final int month = get( MONTH );
		final int day = get( DAY_OF_MONTH );

		if ( NISAN == month ) {
			if ( day > 15 ) {
				return day - 15;
			}
		}
		else if ( IYAR == month ) {
			// Only counting the omer for the last 15 days of Nisan.
			return 15 + day;
		}
		else if ( SIVAN == month ) {
			if ( day < 6 ) {
				// Only counting the omer for the last 15 days of Nisan,, 29 days Iyar, and only counting for the first 5 days of Sivan.
				return 15 + 29 + day; 
			}
		}

		return NOT_COUNTING_OMER;
	}


	private YearType getYearType() {

		// Year lengths can have 2 values, based on whether we're in a leap year or not.
		int yearLength = getActualMaximum( DAY_OF_YEAR );
		switch ( yearLength ) {
			case HASER_REGULAR_LENGTH:
			case HASER_LEAP_LENGTH:
				return YearType.Haser;

			case QESIDRAH_REGULAR_LENGTH:
			case QESIDRAH_LEAP_LENGTH:
				return YearType.Qesidrah;

			case SHALEM_REGULAR_LENGTH:
			case SHALEM_LEAP_LENGTH:
				return YearType.Shalem;

			default:
				throw new IllegalArgumentException( "Illegal year length " + yearLength );
		}
	}


	public boolean isLeapYear() {

		int yearLength = getActualMaximum( DAY_OF_YEAR );

		// Shalem is the longest of the regular lenth years...
		return yearLength > SHALEM_REGULAR_LENGTH;
	}
	
	
}  // End of HebrewCalendar class
