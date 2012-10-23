/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
public class Omer
	extends Observance {

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
				return new OmerDetails( date, hebrewDayOfMonth - 15 );
			}
		}
		else if ( HebrewMonth.IYAR == hebrewMonth ) {
			return new OmerDetails( date, 15 + hebrewDayOfMonth );
		}
		else if ( HebrewMonth.SIVAN == hebrewMonth ) {
			if ( hebrewDayOfMonth < 6 ) {
				return new OmerDetails( date, 15 + HebrewMonth.SHORT_MONTH_LENGTH + hebrewDayOfMonth );
			}
		}

		return null;
	}


	private static final class OmerDetails
		extends Details.Yom {

		private OmerDetails( HebrewDate date, int dayOfOmer ) {
			super( date, OMER + " " + dayOfOmer );
		}


		@Override
		protected boolean isOmer() {
			return true;
		}
	}

}  // End of Omer class
