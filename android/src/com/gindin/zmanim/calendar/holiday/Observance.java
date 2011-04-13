/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * A single-day "holiday" which does not require candle lighting or havdalah.
 */
abstract class Observance
	extends JewishHoliday {

	Observance(
		int month,
		int date,
		String name
	) {
		super( month, date, name );
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
	protected JewishHoliday createForDate( HebrewCalendar forDate ) {
		// By default, there's no need for a special instance.
		return this;
	}


	@Override
	protected JewishHoliday createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, false );
	}

}  // End of Observance class
