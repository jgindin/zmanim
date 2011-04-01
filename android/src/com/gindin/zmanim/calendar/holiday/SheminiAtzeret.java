/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
class SheminiAtzeret
	extends JewishHoliday {

	SheminiAtzeret() {
		super( HebrewCalendar.TISHRI, 22, "Shemini Atzeret" );
	}


	@Override
	protected  JewishHoliday createErevOf() {
		return new Erev( this, true );
	}


	@Override
	public boolean showHavdalah() {
		// We know the next day is Simchat Torah
		return false;
	}


	@Override
	public boolean showCandlelighting() {
		return true;
	}
}
