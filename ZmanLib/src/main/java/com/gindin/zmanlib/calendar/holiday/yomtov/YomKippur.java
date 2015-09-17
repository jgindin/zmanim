/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
public class YomKippur
	extends Observance {

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
				return new Details.Erev( date, NAME );
			}

			if ( 10 == date.getHebrewDayOfMonth() ) {
				return new YomKippurDetails( date );
			}
		}

		return null;
	}


	private static class YomKippurDetails
		extends Details {

		private YomKippurDetails( HebrewDate date ) {
			super( date );
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean isYomTov() {
			return true;
		}


		@Override
		public boolean isYomKippur() {
			return true;
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
