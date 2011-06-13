/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.calendar.holiday.*;

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


	/**
	 * @return Obtain a representation of the current day, or null if there's nothing "special" about today.
	 */
	public JewishDay getToday() {


		// Today could be a combination of things, so we look at each and then combine as necessary.
		JewishDay holiday     = isTodayAHoliday();
		JewishDay taanit      = isTodayATaanit();
		JewishDay roshChodesh = isTodayRoshChodesh();
		JewishDay omer        = isTodayInTheOmer();

		JewishDay today = null;
		if ( null != holiday ) {
			today = holiday;
		}

		if ( null != taanit ) {
			if ( null == today ) {
				today = taanit;
			}
			else {
				today = new AggregateDay( today, taanit );
			}
		}

		if ( null != roshChodesh ) {
			if ( null == today ) {
				today = roshChodesh;
			}
			else {
				today = new AggregateDay( today, roshChodesh );
			}
		}

		if ( null != omer ) {
			if ( null == today ) {
				today = omer;
			}
			else {
				today = new AggregateDay( today, omer );
			}
		}


		return today;
	}

	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishDay isTodayAHoliday() {

		for ( JewishDay jewishDay : Holidays.JEWISH_HOLIDAYS ) {

			final JewishDay day = jewishDay.isOn( this );
			if ( null != day ) {
				return day;
			}

			final JewishDay erev = jewishDay.isErev( this );
			if ( null != erev ) {
				return erev;
			}
		}

		return null;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	public JewishDay isTodayATaanit() {

		for ( Taanit taanit : Taanit.TAANIOT ) {

			final JewishDay day = taanit.isOn( this );
			if ( null != day ) {
				return day;
			}

			final JewishDay erev = taanit.isErev( this );
			if ( null != erev ) {
				return erev;
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
	public JewishDay isTodayRoshChodesh() {

		RoshChodesh[] roshChodeshim;

		switch ( getYearType() ) {
			case Haser:
				roshChodeshim = RoshChodesh.ROSH_CHODESH_HASER;
				break;

			case Qesidrah:
				roshChodeshim = RoshChodesh.ROSH_CHODESH_QESIDRAH;
				break;

			case Shalem:
				roshChodeshim = RoshChodesh.ROSH_CHODESH_SHALEM;
				break;

			default:
				return null;
		}


		for ( RoshChodesh roshChodesh : roshChodeshim ) {

			final JewishDay day = roshChodesh.isOn( this );
			if ( null != day ) {
				return day;
			}

			final JewishDay erev = roshChodesh.isErev( this );
			if ( null != erev ) {
				return erev;
			}
		}

		if ( isLeapYear() ) {
			roshChodeshim = RoshChodesh.ROSH_CHODESH_ADAR_LEAP;
		}
		else {
			roshChodeshim = RoshChodesh.ROSH_CHODESH_ADAR_REGULAR;
		}

		for ( RoshChodesh roshChodesh : roshChodeshim ) {
			final JewishDay day = roshChodesh.isOn( this );
			if ( null != day ) {
				return day;
			}

			final JewishDay erev = roshChodesh.isErev( this );
			if ( null != erev ) {
				return erev;
			}
		}

		return null;
	}


	public JewishDay isTodayInTheOmer() {

		for ( Omer omer : Omer.OMER ) {

			final JewishDay day = omer.isOn( this );
			if ( null != day ) {
				return day;
			}

			// Explicitly NOT checking erev...
		}

		return null;
	}


	public boolean showCandlelighting() {

		boolean show = false;

		JewishDay todaysDay = isTodayAHoliday();
		if ( null != todaysDay ) {
			show = todaysDay.showCandlelighting();
		}

		if ( !show ) {
			show = ( FRIDAY == get( DAY_OF_WEEK ) );
		}

		return show;
	}


	public boolean showHavdalah() {

		boolean show = false;

		// Is today a holiday, but not tomorrow? Make sure that it's a holiday for which we need to show Havadalah,
		//  and not, say, Chol HaMoed...
		final JewishDay todaysDay = isTodayAHoliday();
		if ( null != todaysDay ) {
			show = todaysDay.showHavdalah();
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


	/**
	 * Used when one day is, for example, Rosh Chodesh and a day of the Omer...
	 */
	private static class AggregateDay
		extends JewishDay {

		private final JewishDay                                 firstDay;
		private final JewishDay                                 secondDay;


		AggregateDay(
			JewishDay firstDay,
			JewishDay secondDay
		) {
			// Just something that'll quickly clue is in that something's wrong if we see these values...
			super( 0, 0, "" );

			this.firstDay = firstDay;
			this.secondDay = secondDay;
		}


		@Override
		protected JewishDay createForDate( HebrewCalendar forDate ) {
			return this;
		}


		@Override
		protected JewishDay createErevOf( HebrewCalendar forDate ) {
			return new Erev( this, forDate, showCandlelighting() );
		}


		@Override
		public String getName() {
			return firstDay.getName() + " / " + secondDay.getName();
		}


		@Override
		public String getDescription() {
			return firstDay.getDescription() + " / " + secondDay.getDescription();
		}


		@Override
		public boolean showCandlelighting() {
			return firstDay.showCandlelighting() || secondDay.showCandlelighting();
		}


		@Override
		public boolean showHavdalah() {
			return firstDay.showHavdalah() || secondDay.showHavdalah();
		}


		@Override
		public Erev isErev() {
			if ( null != firstDay.isErev() ) {
				return firstDay.isErev();
			}

			if ( null != secondDay.isErev() ) {
				return secondDay.isErev();
			}

			return null;
		}


		@Override
		public MultiDayHoliday isMultiDay() {
			if ( null != firstDay.isMultiDay() ) {
				return firstDay.isMultiDay();
			}

			if ( null != secondDay.isMultiDay() ) {
				return secondDay.isMultiDay();
			}

			return null;
		}


		@Override
		public RoshChodesh isRoshChodesh() {
			if ( null != firstDay.isRoshChodesh() ) {
				return firstDay.isRoshChodesh();
			}

			if ( null != secondDay.isRoshChodesh() ) {
				return secondDay.isRoshChodesh();
			}

			return null;
		}
	}

}  // End of HebrewCalendar class
