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
public class Omer
	implements Observance {

	private static final String                               OMER = "Omer";

	@Override
	public String getName() {
		return OMER;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		int hebrewMonth = date.getHebrewMonth();
		int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( HebrewMonth.NISSAN == hebrewMonth ) {
			if ( hebrewDayOfMonth >= 16 ) {
				return new OmerDetails( hebrewDayOfMonth - 15 );
			}
		}
		else if ( HebrewMonth.IYAR == hebrewMonth ) {
			return new OmerDetails( 15 + hebrewDayOfMonth );
		}
		else if ( HebrewMonth.SIVAN == hebrewMonth ) {
			if ( hebrewDayOfMonth < 6 ) {
				return new OmerDetails( 15 + HebrewMonth.SHORT_MONTH_LENGTH + hebrewDayOfMonth );
			}
		}

		return null;
	}


	private static final class OmerDetails
		implements Details {

		private int                                             dayOfOmer;


		private OmerDetails( int dayOfOmer ) {
			this.dayOfOmer = dayOfOmer;
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
			return OMER + " " + dayOfOmer;
		}
	}

}  // End of Omer class
