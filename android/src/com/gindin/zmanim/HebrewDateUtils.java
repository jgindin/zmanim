package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.HebrewHoliday;

import java.util.Date;

/**
 * Contains utilities for determining "stuff" about a Hebrew date.
 */
@SuppressWarnings( { "MagicNumber" } )
public class HebrewDateUtils {

	/**
	 * Set of the holidays. We're not using the com.ibm.icu.util.HebrewHoliday.getHolidays() because the
	 *  list declared in com.ibm.icu.impl.data.HolidayBundle_iw_IL is sadly lacking.
	 */
	private static HebrewHoliday[] JEWISH_HOLIDAYS = {
		HebrewHoliday.ROSH_HASHANAH,      HebrewHoliday.GEDALIAH,           HebrewHoliday.YOM_KIPPUR,
		HebrewHoliday.SUKKOT,             HebrewHoliday.HOSHANAH_RABBAH,    HebrewHoliday.SHEMINI_ATZERET,
		HebrewHoliday.SIMCHAT_TORAH,      HebrewHoliday.HANUKKAH,           HebrewHoliday.TEVET_10,
		HebrewHoliday.TU_BSHEVAT,         HebrewHoliday.ESTHER,             HebrewHoliday.PURIM,
		HebrewHoliday.SHUSHAN_PURIM,      HebrewHoliday.PASSOVER,           HebrewHoliday.YOM_HASHOAH,
		HebrewHoliday.YOM_HAZIKARON,      HebrewHoliday.YOM_HAATZMAUT,      HebrewHoliday.PESACH_SHEINI,
		HebrewHoliday.LAG_BOMER,          HebrewHoliday.YOM_YERUSHALAYIM,   HebrewHoliday.SHAVUOT,
		HebrewHoliday.TAMMUZ_17,          HebrewHoliday.TISHA_BAV
	};


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


	private static final HebrewHoliday ROSH_CHODESH_NISAN = new HebrewHoliday( HebrewCalendar.NISAN, 1, 1, "Rosh Chodesh Nisan" );
	private static final HebrewHoliday ROSH_CHODESH_IYAR = new HebrewHoliday( HebrewCalendar.NISAN, 30, 2, "Rosh Chodesh Iyar" );
	private static final HebrewHoliday ROSH_CHODESH_SIVAN = new HebrewHoliday( HebrewCalendar.SIVAN, 1, 1, "Rosh Chodesh Sivan" );
	private static final HebrewHoliday ROSH_CHODESH_TAMMUZ = new HebrewHoliday( HebrewCalendar.SIVAN, 30, 2, "Rosh Chodesh Tammuz" );
	private static final HebrewHoliday ROSH_CHODESH_AV = new HebrewHoliday( HebrewCalendar.AV, 1, 1, "Rosh Chodesh Av" );
	private static final HebrewHoliday ROSH_CHODESH_ELUL = new HebrewHoliday( HebrewCalendar.AV, 30, 2, "Rosh Chodesh Elul" );
	private static final HebrewHoliday ROSH_CHODESH_TISHRI = new HebrewHoliday( HebrewCalendar.ELUL, 30, 2, "Rosh Chodesh Tishri" );
	private static final HebrewHoliday ROSH_CHODESH_HESHVAN = new HebrewHoliday( HebrewCalendar.HESHVAN, 1, 1, "Rosh Chodesh Heshvan" );
	private static final HebrewHoliday ROSH_CHODESH_KISLEV = new HebrewHoliday( HebrewCalendar.KISLEV, 1, 1, "Rosh Chodesh Kislev" );
	private static final HebrewHoliday ROSH_CHODESH_TEVET = new HebrewHoliday( HebrewCalendar.TEVET, 1, 1, "Rosh Chodesh Tevet" );
	private static final HebrewHoliday ROSH_CHODESH_SHEVAT = new HebrewHoliday( HebrewCalendar.SHEVAT, 1, 1, "Rosh Chodesh Shevat" );
	private static final HebrewHoliday ROSH_CHODESH_ADAR = new HebrewHoliday( HebrewCalendar.SHEVAT, 30, 2, "Rosh Chodesh Adar" );
	private static final HebrewHoliday ROSH_CHODESH_ADAR_I = new HebrewHoliday( HebrewCalendar.SHEVAT, 30, 2, "Rosh Chodesh Adar I" );
	private static final HebrewHoliday ROSH_CHODESH_ADAR_II = new HebrewHoliday( HebrewCalendar.ADAR_1, 30, 2, "Rosh Chodesh Adar II" );

	/**
	 * We treat Rosh Chodesh as a holiday too.
	 */
	private static final HebrewHoliday[]                      ROSH_CHODESH_HASER = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,    ROSH_CHODESH_TEVET,     ROSH_CHODESH_SHEVAT,
	};

	private static final HebrewHoliday[]                      ROSH_CHODESH_QESIDRAH = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
		ROSH_CHODESH_KISLEV,
		new HebrewHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	private static final HebrewHoliday[]                      ROSH_CHODESH_SHALEM = {
		ROSH_CHODESH_NISAN,     ROSH_CHODESH_IYAR,      ROSH_CHODESH_SIVAN,     ROSH_CHODESH_TAMMUZ,
		ROSH_CHODESH_AV,        ROSH_CHODESH_ELUL,      ROSH_CHODESH_TISHRI,    ROSH_CHODESH_HESHVAN,
		new HebrewHoliday( HebrewCalendar.HESHVAN, 30, 2, "Rosh Chodesh Kislev" ),  // Add a day compared to Qesidrah
		new HebrewHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" ),    // Add a day compared to Haser
		ROSH_CHODESH_SHEVAT,
	};

	private static final HebrewHoliday[]                      ROSH_CHODESH_ADAR_REGULAR = {
		ROSH_CHODESH_ADAR,
	};

	private static final HebrewHoliday[]                      ROSH_CHODESH_ADAR_LEAP = {
		ROSH_CHODESH_ADAR_I,    ROSH_CHODESH_ADAR_II
	};


	private final HebrewCalendar                              hebrewCalendar;
	
	public HebrewDateUtils(
		HebrewCalendar hebrewCalendar
	) {

		this.hebrewCalendar = hebrewCalendar;
	}


	public HebrewHoliday isTodayAHoliday() {

		Date today = hebrewCalendar.getTime();
		for ( HebrewHoliday hebrewHoliday : JEWISH_HOLIDAYS ) {
			if ( hebrewHoliday.isOn( today ) ) {
				return hebrewHoliday;
			}
		}

		HebrewHoliday roshChodesh = isRoshChodesh();
		if ( null != roshChodesh ) {
			return roshChodesh;
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

			hebrewCalendar.add( 1, HebrewCalendar.DATE );
			if ( null == isTodayAHoliday() ) {
				show = true;
			}

			hebrewCalendar.add( -1, HebrewCalendar.DATE );
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
	public HebrewHoliday isRoshChodesh() {

		HebrewHoliday[] roshChodeshim;

		switch ( getYearType() ) {
			case Haser:
				roshChodeshim = ROSH_CHODESH_HASER;
				break;

			case Qesidrah:
				roshChodeshim = ROSH_CHODESH_QESIDRAH;
				break;

			case Shalem:
				roshChodeshim = ROSH_CHODESH_SHALEM;
				break;

			default:
				return null;
		}


		final Date today = hebrewCalendar.getTime();
		for ( HebrewHoliday roshChodesh : roshChodeshim ) {
			if ( roshChodesh.isOn( today ) ) {
				return roshChodesh;
			}
		}

		if ( isLeapYear() ) {
			roshChodeshim = ROSH_CHODESH_ADAR_LEAP;
		}
		else {
			roshChodeshim = ROSH_CHODESH_ADAR_REGULAR;
		}

		for ( HebrewHoliday roshChodesh : roshChodeshim ) {
			if ( roshChodesh.isOn( today ) ) {
				return roshChodesh;
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
