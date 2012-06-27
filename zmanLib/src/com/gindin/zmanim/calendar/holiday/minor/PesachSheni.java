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
public class PesachSheni
	extends Observance {

	private static final String                               PESACH_SHENI = "Pesach Sheni";


	@Override
	public String getName() {
		return PESACH_SHENI;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();

			if ( 13 == hebrewDayOfMonth ) {
				return new Details.Erev( date, PESACH_SHENI, false );
			}

			if ( 14 == hebrewDayOfMonth ) {
				return new Details.Yom( date, PESACH_SHENI );
			}
		}

		return null;
	}

}  // End of PesachSheni class
