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
public class ShushanPurim
	implements Observance {

	private static final ShushanPurimDetails                  SHUSHAN_PURIM_DETAILS = new ShushanPurimDetails();


	@Override
	public String getName() {
		return "Shushan Purim";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( ( date.isJewishLeapYear() && ( HebrewMonth.ADAR_II == date.getJewishMonth() ) ) ||
			   ( !date.isJewishLeapYear() && ( HebrewMonth.ADAR == date.getJewishMonth() ) ) ) {

			if ( 15 == date.getJewishDayOfMonth() ) {
				return SHUSHAN_PURIM_DETAILS;
			}
		}

		return null;
	}


	private static class ShushanPurimDetails
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
			return "Shushan Purim";
		}
	}


}  // End of ShushanPurim class
