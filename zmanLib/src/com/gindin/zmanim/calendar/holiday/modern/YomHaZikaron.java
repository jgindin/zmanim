/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.modern;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

import java.util.Calendar;

/**
 * Yom Hazikaron is observed on the 4th day of the month of Iyar of the Hebrew calendar, always one day before Israel
 *  Independence Day on the 5th day of Iyar, the anniversary of the Proclamation of the State of Israel in 1948.
 *  Both holidays are observed one or two days earlier (the 3rd and 4th, or the 2nd and 3rd, of Iyar) when the 5th
 *  occurs on a Friday or Saturday, the Shabbat. Similarly, both days are moved one day later when Yom Hazikaron
 *  falls on Sunday.
 *    -- http://en.wikipedia.org/wiki/Yom_Hazikaron
 *
 * This means:
 *  - If Yom HaAtzmaut falls on Friday, it gets pushed back to Thursday the 4th, and Yom HaZikaron goes to Wednesday the 3rd
 *  - If Yom HaAtzmaut falls on Shabbat, it gets pushed back to Thursday the 3rd, and Yom HaZikaron goes to Wednesday the 2nd (2008)
 *  - If Yom HaAtzmaut falls on Monday, it gets pushed out to Tuesday the 6th, and Yom HaZikaron goes to Monday the 5th (2011)
 */
public class YomHaZikaron
	implements Observance {

	private static final String                               NAME = "Yom HaZikaron";

	private static final ErevYomHaZikaronDetails              EREV_YOM_HA_ZIKARON_DETAILS = new ErevYomHaZikaronDetails();
	private static final YomHaZikaronDetails                  YOM_HA_ZIKARON_DETAILS = new YomHaZikaronDetails();


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR != date.getJewishMonth() ) {
			return null;
		}

		// In order to prevent Yom HaZikaron or Yom HaAtzmaut from falling on or immediately after Shabbat,
		//  if the 4th of Iyar falls on:
		//    Thursday: Moved back to the 3rd
		//    Friday:   Moved back to the 2nd
		//    Sunday:   Moved forward to the 5th
		// See http://en.wikipedia.org/wiki/Yom_Hazikaron
		final int jewishDayOfMonth = date.getJewishDayOfMonth();

		switch ( date.getDayOfWeek() ) {
			case Calendar.SUNDAY:
				if ( 4 == jewishDayOfMonth ) {
					return EREV_YOM_HA_ZIKARON_DETAILS;
				}
				break;

			case Calendar.MONDAY:
				if ( 5 == jewishDayOfMonth ) {
					return YOM_HA_ZIKARON_DETAILS;
				}
				break;

			case Calendar.TUESDAY:
				if ( ( 1 == jewishDayOfMonth ) || ( 2 == jewishDayOfMonth ) ) {
					return EREV_YOM_HA_ZIKARON_DETAILS;
				}
				break;

			case Calendar.WEDNESDAY:
				if ( ( 2 == jewishDayOfMonth ) || ( 3 == jewishDayOfMonth ) ) {
					return YOM_HA_ZIKARON_DETAILS;
				}
				break;

		}


		// We've handled the special cases, so if we get here, we're just looking for the "regular" year...
		if ( 3 == jewishDayOfMonth ) {
			return EREV_YOM_HA_ZIKARON_DETAILS;
		}
		else if ( 4 == jewishDayOfMonth ) {
			return YOM_HA_ZIKARON_DETAILS;
		}

		return null;
	}


	private static class ErevYomHaZikaronDetails
		implements Details {

		@Override public boolean isErev() {
			return true;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return "Erev " + NAME;
		}
	}

	private static class YomHaZikaronDetails
		implements Details {

		@Override public boolean isErev() {
			return false;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return "Erev " + NAME;
		}
	}

}  // End of YomHaZikaron class
