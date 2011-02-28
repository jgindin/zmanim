/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import java.util.Date;

/**
 * Adds support for determining additional information about the current day, including whether or not
 *  the day is a holiday.
 */
public class HebrewCalendar 
	extends com.ibm.icu.util.HebrewCalendar {


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
				return jewishHoliday.getForDate( this );
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
				return jewishHoliday.getForDate( this );
			}

			if ( jewishHoliday.isErev( this ) ) {
				return JewishHoliday.createErevOf( jewishHoliday );
			}
		}

		return null;
	}


	/**
	 * We treat Rosh Chodesh as a holiday too.
	 *
	 * @return the Rosh Chodesh holiday, or null if this isn't a Rosh Chodesh.
	 */
	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishHoliday isTodayRoshChodesh() {

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
				return roshChodesh.getForDate( this );
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
				return roshChodesh.getForDate( this );
			}

			if ( roshChodesh.isErev( this ) ) {
				return JewishHoliday.createErevOf( roshChodesh );
			}
		}

		return null;
	}


	public JewishHoliday isTodayInTheOmer() {

		for ( JewishHoliday jewishHoliday : JewishHoliday.OMER ) {
			if ( jewishHoliday.isOn( this ) ) {
				return jewishHoliday.getForDate( this );
			}

			// Explicitly NOT checking erev...
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
