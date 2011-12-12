/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 * Special class for Pesach so we can display which day of the holiday it is.
 */
class Pesach
	extends MultiDayHoliday {

	Pesach() {
		this( null );
	}

	Pesach(
		HebrewCalendar forDate
	) {
		super( HebrewCalendar.NISAN, 15, 8, "Pesach", forDate );
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar hebrewCalendar ) {

		return new Pesach( hebrewCalendar );
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

		assert HebrewCalendar.NISAN == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		return ( 15 == dayOfMonth ) || ( 20 == dayOfMonth ) || ( 21 == dayOfMonth ) || super.isErevShabbat();
	}


	@Override
	public boolean showHavdalah() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.NISAN == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		// Just because it's the end of the holiday, if it's Friday, then we won't want to show Havdalah...
		boolean showForHoliday = ( 16 == dayOfMonth ) || ( 22 == dayOfMonth );

		return showForHoliday && !isErevShabbat();
	}

}
