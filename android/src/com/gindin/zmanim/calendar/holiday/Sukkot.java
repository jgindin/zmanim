/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Special class for Sukkot so we can display which day of the holiday it is.
 */
class Sukkot
	extends MultiDayHoliday {

	Sukkot() {
		this( null );
	}


	Sukkot(
		HebrewCalendar forDate
	) {
		// We say 6 days because the 7th is Shemini Atzeret and the 8th is Simchat Torah.
		super( HebrewCalendar.TISHRI, 15, 6, "Sukkot", forDate );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar hebrewCalendar ) {
		return new Sukkot( hebrewCalendar );
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		return new Erev( this, forDate, true );
	}


	@Override
	public boolean showCandlelighting() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.TISHRI == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		return ( 15 == dayOfMonth ) || isShabbat();
	}


	@Override
	public boolean showHavdalah() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.TISHRI == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		return ( ( 16 == dayOfMonth ) && !isShabbat() ) || isMotzaeShabbat();
	}
}
