/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class PurimKatan
	extends Observance {

	private static final String                               PURIM_KATAN = "Purim Katan";

	@Override
	public String getName() {
		return PURIM_KATAN;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) {
			if ( 13 == date.getHebrewDayOfMonth() ) {
				return new ErevPurimKatanDetails( date );
			}
			if ( 14 == date.getHebrewDayOfMonth() ) {
				return new PurimKatanDetails( date );
			}
		}

		return null;
	}


	private static class ErevPurimKatanDetails
		extends Details {

		private ErevPurimKatanDetails( HebrewDate date ) {
			super( date );
		}


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
			return EREV + PURIM_KATAN;
		}
	}

	private static class PurimKatanDetails
		extends Details {

		private PurimKatanDetails( HebrewDate date ) {
			super( date );
		}


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
