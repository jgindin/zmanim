/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.modern;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;


/**
 *
 */
public class YomHaShoah
	implements Observance {

	private static final String                               NAME = "Yom HaShoah";

	private static final ErevYomHaShoahDetails                EREV_YOM_HA_SHOAH_DETAILS = new ErevYomHaShoahDetails();
	private static final YomHaShoahDetails                    YOM_HA_SHOAH_DETAILS = new YomHaShoahDetails();


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.NISSAN != date.getJewishMonth() ) {
			return null;
		}

		// Note that http://en.wikipedia.org/wiki/Yom_HaShoah claims that if Yom HaShoah falls on a Friday, it gets moved
		//  back to Thursday, and if it falls on Sunday, it gets pushed to the following Monday.
		//
		// However, the reference they provide, http://www.yomhashoah.org.uk/dates clearly shows that it *can* fall on
		//  either a Friday or a Sunday...
		final int jewishDayOfMonth = date.getJewishDayOfMonth();

		if ( 26 == jewishDayOfMonth ) {
			return EREV_YOM_HA_SHOAH_DETAILS;
		}
		else if ( 27 == jewishDayOfMonth ) {
			return YOM_HA_SHOAH_DETAILS;
		}

		return null;
	}


	private static class ErevYomHaShoahDetails
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

	private static class YomHaShoahDetails
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

}  // End of YomHaShoah class
