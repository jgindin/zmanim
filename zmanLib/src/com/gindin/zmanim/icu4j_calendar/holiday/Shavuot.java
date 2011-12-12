/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
*
*/
class Shavuot
	extends MultiDayHoliday {

	Shavuot() {
		this( null );
	}

	Shavuot(
		HebrewCalendar forDate
	) {
		super( HebrewCalendar.SIVAN, 6, 2, "Shavuot", forDate );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar hebrewCalendar ) {
		return new Shavuot( hebrewCalendar );
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, true );
	}


	@Override
	public boolean showCandlelighting() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.SIVAN == forDate.get( HebrewCalendar.MONTH );

		// Only light candles on the 6th.
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );
		return ( 6 == dayOfMonth ) || isErevShabbat();
	}


	@Override
	public boolean showHavdalah() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.SIVAN == forDate.get( HebrewCalendar.MONTH );

		// Only have Havdalah on the 7th
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );
		return ( 7 == dayOfMonth ) && !isErevShabbat();
	}
}
