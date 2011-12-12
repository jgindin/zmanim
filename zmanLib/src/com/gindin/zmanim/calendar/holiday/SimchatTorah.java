/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
class SimchatTorah
	extends Holiday {

	SimchatTorah() {
		super( HebrewCalendar.TISHRI, 23, "Simchat Torah", null );
	}


	SimchatTorah(
		HebrewCalendar forDate
	) {
		super( HebrewCalendar.TISHRI, 23, "Simchat Torah", forDate );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		return new SimchatTorah( forDate );
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, true );
	}


	@Override
	public boolean showCandlelighting() {
		// We light candles the night before.
		return isErevShabbat();
	}


	@Override
	public boolean showHavdalah() {
		return !isErevShabbat();
	}
}
