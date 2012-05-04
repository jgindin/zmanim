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
public class LagBOmer
	extends AbstractCachingObservance {

	private static final String                               NAME = "Lag B'Omer";


	@Override
	public String getName() {
		return NAME;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 17 == hebrewDayOfMonth ) {
				return getErevDetails();
			}
			else if ( 18 == hebrewDayOfMonth ) {
				return getYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevLagBOmerDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new LagBOmerDetails();
	}


	private static final class ErevLagBOmerDetails
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
			return NAME;
		}
	}



	private static final class LagBOmerDetails
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
			return NAME;
		}
	}

}  // End of LagBOmer class
