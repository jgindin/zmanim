/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
class YomKippur
	extends JewishHoliday {

	YomKippur() {
		super( HebrewCalendar.TISHRI, 10, "Yom Kippur" );
	}


	YomKippur( HebrewCalendar forDate ) {
		super( HebrewCalendar.TISHRI, 10, "Yom Kippur", forDate );
	}


	@Override
	protected JewishHoliday createForDate( HebrewCalendar forDate ) {
		return new YomKippur( forDate );
	}


	@Override
	protected  JewishHoliday createErevOf( HebrewCalendar forDate ) {
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

}  // End of YomKippur class
