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
		super( HebrewCalendar.TISHRI, 23, "Simchat Torah", null );
	}


	SimchatTorah(
		HebrewCalendar forDate
	) {
		super( HebrewCalendar.TISHRI, 23, "Simchat Torah", forDate );
	}


	@Override
	protected JewishHoliday createForDate( HebrewCalendar forDate ) {
		return new SimchatTorah( forDate );
	}


	@Override
	protected JewishHoliday createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, true );
	}


	@Override
	public boolean showCandlelighting() {
		return true;
	}


	@Override
	public boolean showHavdalah() {
		return !isShabbat();
	}
}
