/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.rabbanan;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class Purim
	extends Observance {


	private static final String                               PURIM = "Purim";


	@Override
	public String getName() {
		return PURIM;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR_II == date.getHebrewMonth() ) ) ||
			   ( !date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) ) {

			if ( 13 == date.getHebrewDayOfMonth() ) {
				return new Details.Erev( date, PURIM, false );
			}
			if ( 14 == date.getHebrewDayOfMonth() ) {
				return new Details.Yom( date, PURIM );
			}
		}

		return null;
	}

}  // End of Purim class
