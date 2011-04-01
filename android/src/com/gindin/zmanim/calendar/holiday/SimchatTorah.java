/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
class SimchatTorah
	extends JewishHoliday {

	SimchatTorah() {
		super( HebrewCalendar.TISHRI, 23, "Simchat Torah" );
	}


	@Override
	protected JewishHoliday createErevOf() {
		return new Erev( this, true );
	}


	@Override
	public boolean showCandlelighting() {
		return true;
	}


	@Override
	public boolean showHavdalah() {
		return true;
	}
}
