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
public class TuBAv
	extends Observance {

	private static final String                               TU_BAV = "Tu B'Av";

	@Override
	public String getName() {
		return TU_BAV;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( date.getHebrewMonth() == HebrewMonth.AV ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 14 == hebrewDayOfMonth ) {
				return new ErevTuBAvDetails( date );
			}

			if ( 15 == hebrewDayOfMonth ) {
				return new TuBAvDetails( date );
			}
		}

		return null;
	}


	private static final class ErevTuBAvDetails
		extends Details {

		private ErevTuBAvDetails( HebrewDate date ) {
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
			return EREV + TU_BAV;
		}
	}


	private static final class TuBAvDetails
		extends Details {

		private TuBAvDetails( HebrewDate date ) {
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
			return TU_BAV;
		}
	}

}  // End of TuBAv class
