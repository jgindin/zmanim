/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

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
	protected JewishHoliday createForDate( HebrewCalendar hebrewCalendar ) {

		return new Pesach( hebrewCalendar );
	}


	@Override
	protected JewishHoliday createErevOf() {
		return new Erev( this, true );
	}


	@Override
	public boolean showCandlelighting() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.NISAN == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		return ( 15 == dayOfMonth ) || ( 21 == dayOfMonth );
	}


	@Override
	public boolean showHavdalah() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.NISAN == forDate.get( HebrewCalendar.MONTH );
		int dayOfMonth = forDate.get( HebrewCalendar.DATE );

		return ( 16 == dayOfMonth ) || ( 22 == dayOfMonth );
	}

}
