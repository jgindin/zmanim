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
public class PurimKatan
	implements Observance {

	private static final ErevPurimKatanDetails                EREV_PURIM_KATAN_DETAILS = new ErevPurimKatanDetails();
	private static final PurimKatanDetails                    PURIM_KATAN_DETAILS = new PurimKatanDetails();


	@Override
	public String getName() {
		return "Purim Katan";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( date.isJewishLeapYear() && ( HebrewMonth.ADAR == date.getJewishMonth() ) ) {
			if ( 13 == date.getJewishDayOfMonth() ) {
				return EREV_PURIM_KATAN_DETAILS;
			}
			if ( 14 == date.getJewishDayOfMonth() ) {
				return PURIM_KATAN_DETAILS;
			}
		}

		return null;
	}


	private static class ErevPurimKatanDetails
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
			return "Erev Purim Katan";
		}
	}

	private static class PurimKatanDetails
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
			return "Purim Katan";
		}
	}

}  // End of PurimKatan class
