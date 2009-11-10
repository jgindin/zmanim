package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.HebrewHoliday;

import java.util.Date;

/**
 * Contains utilities for determining "stuff" about a Hebrew date.
 */
public class HebrewDateUtils {

	/**
	 * In Hebrew, the six single year lengths give rise to the terms haser, qesidrah, and shalem.
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
   */
	private static enum YearType {
		Deficient,
		Normal,
		Complete
	}


	private final HebrewCalendar                              hebrewCalendar;

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
	 * We treat Rosh Chodesh as a holiday too.
	 *
	 * From http://www.jewishvirtuallibrary.org/jsource/Judaism/calendar.html: The Jewish calendar has the following months:
	 * Month     Length          Gregorian Equivalent
	 * =======   =============   ====================
	 * Nissan 	  30 days         March-April
	 * Iyar 	    29 days 	      April-May
	 * Sivan 	    30 days 	      May-June
	 * Tammuz 	  29 days 	      June-July
	 * Av         30 days 	      July-August
	 * Elul 	    29 days 	      August-September
	 * Tishri 	  30 days         September-October
	 * Heshvan    29 or 30 days 	October-November
	 * Kislev 	  30 or 29 days 	November-December
	 * Tevet 	    29 days         December-January
	 * Shevat 	  30 days 	      January-February
	 * Adar 	    29 or 30 days 	February-March
	 * Adar II    29 days         March-April
	 * <p/>
	 * In leap years, Adar has 30 days. In non-leap years, Adar has 29 days.
	 */
	private static final HebrewHoliday                        ROSH_CHODESH_NISSAN_SHORT = new HebrewHoliday( HebrewCalendar.NISAN, 1, 1, "Rosh Chodesh Nisan" );
	private static final HebrewHoliday                        ROSH_CHODESH_NISSAN_LONG = new HebrewHoliday( HebrewCalendar.ADAR, 30, 2, "Rosh Chodesh Nisan" );
	private static final HebrewHoliday                        ROSH_CHODESH_IYAR = new HebrewHoliday( HebrewCalendar.NISAN, 30, 2, "Rosh Chodesh Iyar" );
	private static final HebrewHoliday                        ROSH_CHODESH_SIVAN = new HebrewHoliday( HebrewCalendar.SIVAN, 1, 1, "Rosh Chodesh Sivan" );
	private static final HebrewHoliday                        ROSH_CHODESH_TAMMUZ = new HebrewHoliday( HebrewCalendar.SIVAN, 30, 2, "Rosh Chodesh Tammuz" );
	private static final HebrewHoliday                        ROSH_CHODESH_AV = new HebrewHoliday( HebrewCalendar.AV, 1, 1, "Rosh Chodesh Av" );
	private static final HebrewHoliday                        ROSH_CHODESH_ELUL = new HebrewHoliday( HebrewCalendar.AV, 30, 2, "Rosh Chodesh Elul" );
//	private static final HebrewHoliday                        ROSH_CHODESH_TISHRI = new HebrewHoliday( HebrewCalendar., 30, 2, "Rosh Chodesh Tishri" );
	private static final HebrewHoliday                        ROSH_CHODESH_HESHVAN = new HebrewHoliday( HebrewCalendar.TISHRI, 30, 2, "Rosh Chodesh Heshvan" );
	private static final HebrewHoliday                        ROSH_CHODESH_KISLEV_SHORT = new HebrewHoliday( HebrewCalendar.KISLEV, 1, 1, "Rosh Chodesh Kislev" );
	private static final HebrewHoliday                        ROSH_CHODESH_KISLEV_LONG = new HebrewHoliday( HebrewCalendar.HESHVAN, 30, 2, "Rosh Chodesh Kislev" );
	private static final HebrewHoliday                        ROSH_CHODESH_TEVET_SHORT = new HebrewHoliday( HebrewCalendar.TEVET, 1, 1, "Rosh Chodesh Tevet" );
	private static final HebrewHoliday                        ROSH_CHODESH_TEVET_LONG = new HebrewHoliday( HebrewCalendar.KISLEV, 30, 2, "Rosh Chodesh Tevet" );
	private static final HebrewHoliday                        ROSH_CHODESH_SHEVAT = new HebrewHoliday( HebrewCalendar.SHEVAT, 1, 1, "Rosh Chodesh Shevat" );
	private static final HebrewHoliday                        ROSH_CHODESH_ADAR = new HebrewHoliday( HebrewCalendar.SHEVAT, 30, 2, "Rosh Chodesh Adar" );
	private static final HebrewHoliday                        ROSH_CHODESH_ADAR2_SHORT = new HebrewHoliday( HebrewCalendar.ADAR, 1, 1, "Rosh Chodesh Adar II" );
	private static final HebrewHoliday                        ROSH_CHODESH_ADAR2_LONG = new HebrewHoliday( HebrewCalendar.ADAR_1, 30, 2, "Rosh Chodesh Adar II" );

	private static final HebrewHoliday[] ROSH_CHODESH = {

	};


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

		if ( null != isTodayAHoliday() ) {
			return true;
		}

		if ( HebrewCalendar.FRIDAY == hebrewCalendar.get( HebrewCalendar.DAY_OF_WEEK ) ) {
			return true;
		}

		return false;
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

		return null;
	}


	private YearType getYearType() {

		// Year lengths can have 2 values, based on whether we're in a leap year or not.
		int yearLength = hebrewCalendar.getActualMaximum( HebrewCalendar.DAY_OF_YEAR );
		switch ( yearLength ) {
			case 353:
			case 383:
				return YearType.Deficient;
			case 354:
			case 384:
				return YearType.Normal;
			case 355:
			case 385:
				return YearType.Complete;
			default:
				throw new IllegalArgumentException( "Illegal year length " + yearLength );
		}
	}


	private boolean isLeapYear() {

		int yearLength = hebrewCalendar.getActualMaximum( HebrewCalendar.DAY_OF_YEAR );
		return yearLength > 355;
	}


}  // End of HebrewDateUtils class
