/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Pesach
	extends Observance {

	private static final String PESACH = "Pesach";


	@Override
	public String getName() {
		return PESACH;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewMonth = date.getHebrewMonth();
		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( HebrewMonth.NISSAN != hebrewMonth ) {
			return null;
		}

		switch ( hebrewDayOfMonth ) {
			case 14:
				return new Details.Erev( date, PESACH );

			case 15:
				return new Details.YomTovDetails( date, PESACH, true );

			case 16:
				return new Details.YomTovDetails( date, PESACH, false );

			case 17:
			case 18:
			case 19:
				return new Details.CholHaMoedDetails( date, hebrewDayOfMonth - 14, PESACH, false );

			case 20:
				return new Details.CholHaMoedDetails( date, hebrewDayOfMonth - 14, PESACH, true );

			case 21:
				return new Details.YomTovDetails( date, PESACH, true );

			case 22:
				return new Details.YomTovDetails( date, PESACH, false );
		}

		return null;
	}

}  // End of Pesach class
