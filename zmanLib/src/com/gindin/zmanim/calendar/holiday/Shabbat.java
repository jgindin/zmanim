/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;

import java.util.Calendar;

/**
 * Represents Shabbat.
 */
public class Shabbat
	implements Observance {


	private static final ErevShabbatDetails                   EREV_SHABBAT_DETAILS = new ErevShabbatDetails();
	private static final ShabbatDetails                       SHABBAT_DETAILS = new ShabbatDetails();


	@Override
	public String getName() {
		return "Shabbat";
	}


	@Override
	public Details getDetailsFor( final HebrewDate date ) {

		if ( Calendar.FRIDAY == date.getDayOfWeek() ) {
			return EREV_SHABBAT_DETAILS;
		}

		if ( Calendar.SATURDAY == date.getDayOfWeek() ) {
			return SHABBAT_DETAILS;
		}

		return null;
	}


	private static class ErevShabbatDetails
		implements Details {

		@Override public boolean isErev() {
			return true;
		}

		@Override public boolean lightCandles() {
			return true;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return EREV + "Shabbat";
		}
	}

	private static class ShabbatDetails
		implements Details {

		@Override public boolean isErev() {
			return false;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return true;
		}

		@Override public String getDescription() {
			return "Shabbat";
		}
	}

}  // End of Shabbat class
