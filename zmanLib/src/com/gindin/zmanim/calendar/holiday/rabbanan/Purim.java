/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.rabbanan;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class Purim
	implements Observance {

	private static final ErevPurimDetails                     EREV_PURIM_DETAILS = new ErevPurimDetails();
	private static final PurimDetails                         PURIM_DETAILS = new PurimDetails();


	@Override
	public String getName() {
		return "Purim";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( ( date.isJewishLeapYear() && ( HebrewMonth.ADAR_II == date.getJewishMonth() ) ) ||
			   ( !date.isJewishLeapYear() && ( HebrewMonth.ADAR == date.getJewishMonth() ) ) ) {

			if ( 13 == date.getJewishDayOfMonth() ) {
				return EREV_PURIM_DETAILS;
			}
			if ( 14 == date.getJewishDayOfMonth() ) {
				return PURIM_DETAILS;
			}
		}

		return null;
	}


	private static class ErevPurimDetails
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
			return "Erev Purim";
		}
	}

	private static class PurimDetails
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
			return "Purim";
		}
	}

}  // End of Purim class
