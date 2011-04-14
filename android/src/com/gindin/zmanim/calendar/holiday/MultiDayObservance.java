/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * A multi-day "holiday" which does not require candles or havdalah.
 */
abstract class MultiDayObservance
	extends MultiDayHoliday {

	MultiDayObservance(
		int month,
		int date,
		int length,
		String name,
		HebrewCalendar forDate
	) {
		super( month, date, length, name, forDate );
	}


	@Override
	public boolean showCandlelighting() {

		// Could be Shabbat...
		return super.showCandlelighting();
	}


	@Override
	public boolean showHavdalah() {

		// Could be Motzae Shabbat...
		return super.showHavdalah();
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, false );
	}
}  // End of MultiDayObservance class
