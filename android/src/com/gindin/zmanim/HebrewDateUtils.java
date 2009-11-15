package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;

import java.util.Date;

/**
 * Contains utilities for determining "stuff" about a Hebrew date.
 */
@SuppressWarnings( { "MagicNumber" } )
public class HebrewDateUtils {


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




	private final HebrewCalendar                              hebrewCalendar;
	
	public HebrewDateUtils(
		HebrewCalendar hebrewCalendar
	) {

		this.hebrewCalendar = hebrewCalendar;
	}


	public JewishHoliday isTodayAHoliday() {

		for ( JewishHoliday jewishHoliday : JewishHoliday.JEWISH_HOLIDAYS ) {
			if ( jewishHoliday.isOn( hebrewCalendar ) ) {
				return jewishHoliday;
			}

			if ( jewishHoliday.isErev( hebrewCalendar ) ) {
				return JewishHoliday.createErevOf( jewishHoliday );
			}
		}

		return null;
	}


	public boolean showCandlelighting() {

		//noinspection SimplifiableIfStatement
		if ( null != isTodayAHoliday() ) {
			return true;
		}

		return HebrewCalendar.FRIDAY == hebrewCalendar.get( HebrewCalendar.DAY_OF_WEEK );
	}


	public boolean showHavdalah() {

		boolean show = false;

		// Is today a holiday, but not tomorrow?
		if ( null != isTodayAHoliday() ) {

			Date today = hebrewCalendar.getTime();
			hebrewCalendar.add( HebrewCalendar.DATE, 1 );
			if ( null == isTodayAHoliday() ) {
				show = true;
			}

			hebrewCalendar.setTime( today );
		}

		if ( !show ) {
			show = ( HebrewCalendar.SATURDAY == hebrewCalendar.get( HebrewCalendar.DAY_OF_WEEK ) );
		}

		return show;
	}


	/**
	 * We treat Rosh Chodesh as a holiday too.
	 *
	 * @return the Rosh Chodesh holiday, or null if this isn't a Rosh Chodesh.
	 */
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
			if ( roshChodesh.isOn( hebrewCalendar ) ) {
				return roshChodesh;
			}

			if ( roshChodesh.isErev( hebrewCalendar ) ) {
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
			if ( roshChodesh.isOn( hebrewCalendar ) ) {
				return roshChodesh;
			}

			if ( roshChodesh.isErev( hebrewCalendar ) ) {
				return JewishHoliday.createErevOf( roshChodesh );
			}
		}

		return null;
	}


	private YearType getYearType() {

		// Year lengths can have 2 values, based on whether we're in a leap year or not.
		int yearLength = hebrewCalendar.getActualMaximum( HebrewCalendar.DAY_OF_YEAR );
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


	private boolean isLeapYear() {

		int yearLength = hebrewCalendar.getActualMaximum( HebrewCalendar.DAY_OF_YEAR );
		return yearLength > 355;
	}


}  // End of HebrewDateUtils class
