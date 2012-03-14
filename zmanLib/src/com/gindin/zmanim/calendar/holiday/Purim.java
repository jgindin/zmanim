/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Purim
	implements Observance {

	private static final ErevPurimKatanDetails                EREV_PURIM_KATAN_DETAILS = new ErevPurimKatanDetails();
	private static final PurimKatanDetails                    PURIM_KATAN_DETAILS = new PurimKatanDetails();
	private static final ErevPurimDetails                     EREV_PURIM_DETAILS = new ErevPurimDetails();
	private static final PurimDetails                         PURIM_DETAILS = new PurimDetails();
	private static final ShushanPurimDetails                  SHUSHAN_PURIM_DETAILS = new ShushanPurimDetails();


	@Override
	public String getName() {
		return "Purim";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( ( date.isJewishLeapYear() && ( HebrewMonth.ADAR_II != date.getJewishMonth() ) ) ||
			   ( !date.isJewishLeapYear() && ( HebrewMonth.ADAR != date.getJewishMonth() ) ) ) {

			if ( 13 == date.getJewishDayOfMonth() ) {
				return EREV_PURIM_DETAILS;
			}
			if ( 14 == date.getJewishDayOfMonth() ) {
				return PURIM_DETAILS;
			}
			if ( 15 == date.getJewishDayOfMonth() ) {
				return SHUSHAN_PURIM_DETAILS;
			}
		}

		if ( date.isJewishLeapYear() && ( HebrewMonth.ADAR != date.getJewishMonth() ) ) {
			if ( 13 == date.getJewishDayOfMonth() ) {
				return EREV_PURIM_KATAN_DETAILS;
			}
			if ( 14 == date.getJewishDayOfMonth() ) {
				return PURIM_KATAN_DETAILS;
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

}  // End of Purim class
