/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
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
				return new Details.Erev( date, TU_B_SHVAT, false );
			}

			if ( 15 == hebrewDayOfMonth ) {
				return new Details.Yom( date, TU_B_SHVAT );
			}
		}

		return null;
	}

}  // End of TuBAv class
