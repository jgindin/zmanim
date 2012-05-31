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
public class TuBShvat
	extends Observance {

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
				return new ErevTuBShvatDetails( date );
			}

			if ( 15 == hebrewDayOfMonth ) {
				return new TuBShvatDetails( date );
			}
		}

		return null;
	}



	private static final class ErevTuBShvatDetails
		extends Details {

		private ErevTuBShvatDetails( HebrewDate date ) {
			super( date );
		}


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
		extends Details {

		private TuBShvatDetails( HebrewDate date ) {
			super( date );
		}


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
