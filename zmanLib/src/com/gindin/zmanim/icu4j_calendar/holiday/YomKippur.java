/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 *
 */
class YomKippur
	extends Holiday {

	YomKippur() {
		super( HebrewCalendar.TISHRI, 10, "Yom Kippur" );
	}


	YomKippur( HebrewCalendar forDate ) {
		super( HebrewCalendar.TISHRI, 10, "Yom Kippur", forDate );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		return new YomKippur( forDate );
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, true );
	}


	@Override
	public boolean showCandlelighting() {
		// We light candles the night before.
		return false;
	}


	@Override
	public boolean showHavdalah() {
		return true;
	}

}  // End of YomKippur class
