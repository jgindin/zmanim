/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.modern;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 * An Israeli national holiday commemorating the reunification of Jerusalem and the establishment of Israeli control
 *  over the Old City in June 1967. The Chief Rabbinate of Israel declared Jerusalem Day a minor religious holiday to
 *  thank God for victory in the Six-Day War and for answering the 2,000-year-old prayer of "Next Year in Jerusalem".
 *
 * On May 12, 1968, the government proclaimed a new holiday—Jerusalem Day—to be celebrated on the 28th of Iyar,
 *  the Hebrew date on which the divided city of Jerusalem became one.
 *    -- http://en.wikipedia.org/wiki/Yom_Yerushalayim
 */
@SuppressWarnings( { "FeatureEnvy", "MagicNumber" } )
public class YomYerushalayim
	extends Observance {

	private static final String                               NAME = "Yom Yerushalyim";


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR != date.getHebrewMonth() ) {
			return null;
		}

		if ( 27 == date.getHebrewDayOfMonth() ) {
			return new Details.Erev( date, NAME, false );
		}

		if ( 28 == date.getHebrewDayOfMonth() ) {
			return new Details.Yom( date, NAME );
		}

		return null;
	}


}  // End of YomYerushalayim class
