/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 * A single-day "holiday" which does not require candle lighting or havdalah.
 */
abstract class Observance
	extends JewishDay {

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
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		// By default, there's no need for a special instance.
		return this;
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, false );
	}

}  // End of Observance class
