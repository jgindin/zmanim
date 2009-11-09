package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.HebrewHoliday;

import java.util.Date;

/**
 * Given the hebrew date, figure out an appropriate messsage.
 * <p/>
 * See http://hebrewcalendar.tripod.com/ for tons more info...may have to write my own HY <--> GY converter.
 * <p/>
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
public class HebrewDateUtils {

	private final HebrewCalendar                              hebrewCalendar;


	public HebrewDateUtils( HebrewCalendar hebrewCalendar ) {
		this.hebrewCalendar = hebrewCalendar;
	}


	public String getTodaysHoliday() {

		Date today = hebrewCalendar.getTime();
		for ( HebrewHoliday hebrewHoliday : HebrewHolidays.JEWISH_HOLIDAYS ) {
			if ( hebrewHoliday.isOn( today ) ) {
				return hebrewHoliday.getDisplayName();
			}
		}

		// TODO: Check for Rosh Chodesh too.

		return "";
	}


	public boolean showCandlelighting() {


		return false;
	}


	public boolean showHavdalah() {


		return false;
	}


}  // End of HebrewDateUtils class
