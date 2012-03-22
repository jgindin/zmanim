/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservance;

/**
 *
 */
public class YomKippur
	extends AbstractCachingObservance {

	private static final String                               NAME = "Yom Kippur";


	@Override
	public String getName() {
		return NAME;
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.TISHREI == date.getHebrewMonth() ) {
			if ( 9 == date.getHebrewDayOfMonth() ) {
				return getErevDetails();
			}

			if ( 10 == date.getHebrewDayOfMonth() ) {
				return getYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new YomDetails();
	}


	private static class ErevDetails
		implements Details {

		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return true;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return EREV + NAME;
		}
	}

	private static class YomDetails
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
			return true;
		}


		@Override
		public String getDescription() {
			return NAME;
		}
	}

}  // End of YomKippur class
