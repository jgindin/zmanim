package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.HebrewHoliday;

/**
 *
 */
public class HebrewHolidays {

	/**
	 * Set of the holidays. We're not using the com.ibm.icu.util.HebrewHoliday.getHolidays() because the
	 *  list declared in com.ibm.icu.impl.data.HolidayBundle_iw_IL is sadly lacking.
	 */
	public static HebrewHoliday[] JEWISH_HOLIDAYS = {
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
	public static final HebrewHoliday                         ROSH_CHODESH_NISSAN_1 = new HebrewHoliday( HebrewCalendar.NISAN, 30, "Rosh Chodesh Nisan" );


}  // End of HebrewHolidays class
