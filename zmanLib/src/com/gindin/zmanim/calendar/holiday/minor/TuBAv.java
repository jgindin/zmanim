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
public class TuBAv
	extends AbstractCachingObservance {

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
		return new ErevTuBAvDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new TuBAvDetails();
	}


	private static final class ErevTuBAvDetails
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
			return EREV + TU_BAV;
		}
	}


	private static final class TuBAvDetails
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
			return TU_BAV;
		}
	}

}  // End of TuBAv class
