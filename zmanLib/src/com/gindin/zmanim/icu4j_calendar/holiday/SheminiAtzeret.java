/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 *
 */
class SheminiAtzeret
	extends Holiday {

	SheminiAtzeret() {
		super( HebrewCalendar.TISHRI, 22, "Shemini Atzeret" );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		return new SheminiAtzeret();
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, true );
	}


	@Override
	public boolean showHavdalah() {
		// We know the next day is Simchat Torah
		return false;
	}

}
