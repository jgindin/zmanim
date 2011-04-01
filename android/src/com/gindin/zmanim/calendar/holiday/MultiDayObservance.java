/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * A multi-day "holiday" which does not require candles or havdalah.
 */
class MultiDayObservance
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
		return false;
	}


	@Override
	public boolean showHavdalah() {
		return false;
	}


	@Override
	protected JewishHoliday createErevOf() {
		return new Erev( this, false );
	}
}  // End of MultiDayObservance class
