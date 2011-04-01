/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

/**
 * A single-day "holiday" which does not require candle lighting or havdalah.
 */
class Observance
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
	protected JewishHoliday createErevOf() {
		return new Erev( this, false );
	}
}  // End of Observance class
