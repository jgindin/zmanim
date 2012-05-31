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
	extends Observance {



	@Override
	public String getName() {
		return "Shabbat";
	}


	@Override
	public Details getDetailsFor( final HebrewDate date ) {

		if ( Calendar.FRIDAY == date.getDayOfWeek() ) {
			return new ErevShabbatDetails( date );
		}

		if ( Calendar.SATURDAY == date.getDayOfWeek() ) {
			return new ShabbatDetails( date );
		}

		return null;
	}


	private static class ErevShabbatDetails
		extends Details {

		private ErevShabbatDetails( HebrewDate date ) {
			super( date );
		}


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
		extends Details {

		private ShabbatDetails( HebrewDate date ) {
			super( date );
		}


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
