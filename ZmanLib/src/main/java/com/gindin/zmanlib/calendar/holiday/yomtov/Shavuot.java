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
				return new Details.ErevYomTov( date, SHAVUOT );
			}

			if ( 6 == hebrewDayOfMonth ) {
				return new Details.YomTovDetails( date, SHAVUOT, true );
			}

			if ( 7 == hebrewDayOfMonth ) {
				return new Details.YomTovDetails( date, SHAVUOT, false );
			}
		}

		return null;
	}

}  // End of Shavuot class
