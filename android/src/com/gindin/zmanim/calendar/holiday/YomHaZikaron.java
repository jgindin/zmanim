/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * From Wikipedia:
 *
 * Yom Hazikaron is observed on the 4th day of the month of Iyar of the Hebrew calendar, always preceding the next
 *  day's celebrations of Israel Independence Day on the 5th day of Iyar, the anniversary of the Proclamation of the
 *  State of Israel in 1948. Both holidays may be observed one or two days earlier (the 3rd and 4th, or the 2nd and
 *  3rd, of Iyar) if the 5th happens to occur on a Friday or Saturday, the Shabbat. Similarly, both days are moved
 *  one day later if Yom Hazikaron would fall on Sunday.
 */
class YomHaZikaron
	extends AdjustableDateHoliday {

	YomHaZikaron() {
		super( HebrewCalendar.IYAR, 4, "Yom HaZikaron" );
	}


	@Override
	public boolean showCandlelighting() {
		return false;
	}


	@Override
	public boolean showHavdalah() {
		return false;
	}


	@Override
	protected JewishHoliday createErevOf() {
		return new Erev( this, false );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	@Override
	protected int getDateAdjustment( HebrewCalendar dateToTest ) {

		// Update the calendar for the test...
		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

		// If Yom Ha'Atzmaut were to fall on:
		//  Friday: move back 1 day
		//  Shabbat: move back 2 days
		//  Sunday: move forward 1 day
		int adjustment = 0;

		final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
		if ( HebrewCalendar.THURSDAY == dayOfWeek ) {
			adjustment = -1;
		}
		else if ( HebrewCalendar.FRIDAY == dayOfWeek ) {
			adjustment = -2;
		}
		else if ( HebrewCalendar.SUNDAY == dayOfWeek ) {
			adjustment = 1;
		}

		return adjustment;
	}
}
