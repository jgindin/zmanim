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
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class PurimKatan
	extends Observance {

	private static final String                               PURIM_KATAN = "Purim Katan";

	@Override
	public String getName() {
		return PURIM_KATAN;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) {
			if ( 13 == date.getHebrewDayOfMonth() ) {
				return new Details.Erev( date, PURIM_KATAN, false );
			}
			if ( 14 == date.getHebrewDayOfMonth() ) {
				return new Details.Yom( date, PURIM_KATAN );
			}
		}

		return null;
	}

}  // End of PurimKatan class
