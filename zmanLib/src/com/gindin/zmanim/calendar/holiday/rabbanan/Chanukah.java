/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.rabbanan;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class Chanukah
	extends Observance {


	@Override
	public String getName() {
		return "Chanukah";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( HebrewMonth.KISLEV == date.getHebrewMonth() ) {
			if ( 24 == hebrewDayOfMonth ) {
				return new ErevChanukahDetails( date );
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


	private static class ErevChanukahDetails
		extends Details {

		private ErevChanukahDetails( HebrewDate date ) {
			super( date );
		}


		@Override public boolean isErev() {
			return true;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return EREV + "Chanukah";
		}
	}

	private static class ChanukahDetails
		extends Details {

		private final int                                       day;


		private ChanukahDetails( HebrewDate date, int day ) {
			super( date );
			this.day = day;
		}


		@Override public boolean isErev() {
			return false;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return "Chanukah " + day;
		}
	}


}  // End of Chanukah class
