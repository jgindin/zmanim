/*
 * Copyright (c) 2012. Jay R. Gindin
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
public class Chanukah
	extends Observance {


	private static final String                               CHANUKAH = "Chanukah";


	@Override
	public String getName() {
		return CHANUKAH;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( HebrewMonth.KISLEV == date.getHebrewMonth() ) {
			if ( 24 == hebrewDayOfMonth ) {
				return new Details.Erev( date, CHANUKAH, false );
			}
			else if ( hebrewDayOfMonth >= 25 ) {
				return new ChanukahDetails( date, ( hebrewDayOfMonth + 1 ) - 25 );
			}
		}
		else if ( HebrewMonth.TEVET == date.getHebrewMonth() ) {
			if ( date.isKislevShort() ) {
				if ( hebrewDayOfMonth <= 3 ) {
					return new ChanukahDetails( date, hebrewDayOfMonth + 5 );
				}
			}
			else {
				if ( hebrewDayOfMonth <= 2 ) {
					return new ChanukahDetails( date, hebrewDayOfMonth + 6 );
				}
			}
		}

		return null;
	}


	private static class ChanukahDetails
		extends Details.Yom {

		private ChanukahDetails( HebrewDate date, int day ) {
			super( date, CHANUKAH + " " + day );
		}
	}


}  // End of Chanukah class
