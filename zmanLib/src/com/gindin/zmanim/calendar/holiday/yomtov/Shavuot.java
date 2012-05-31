/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
public class Shavuot
	extends Observance {

	private static final String                               SHAVUOT = "Shavuot";

	@Override
	public String getName() {
		return SHAVUOT;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.SIVAN == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 5 == hebrewDayOfMonth ) {
				return new ErevDetails( date, SHAVUOT );
			}

			if ( 6 == hebrewDayOfMonth ) {
				return new YomTovDetails( date, SHAVUOT, true );
			}

			if ( 7 == hebrewDayOfMonth ) {
				return new YomTovDetails( date, SHAVUOT, false );
			}
		}

		return null;
	}

}  // End of Shavuot class
