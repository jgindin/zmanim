/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservance;

/**
 *
 */
public class TuBShvat
	extends AbstractCachingObservance {

	private static final String                               TU_B_SHVAT = "Tu B'Shvat";

	@Override
	public String getName() {
		return TU_B_SHVAT;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( date.getHebrewMonth() == HebrewMonth.SHEVAT ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 14 == hebrewDayOfMonth ) {
				return getErevDetails();
			}

			if ( 15 == hebrewDayOfMonth ) {
				return getYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevTuBShvatDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new TuBShvatDetails();
	}


	private static final class ErevTuBShvatDetails
		implements Details {

		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return EREV + TU_B_SHVAT;
		}
	}


	private static final class TuBShvatDetails
		implements Details {

		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return TU_B_SHVAT;
		}
	}

}  // End of TuBAv class
