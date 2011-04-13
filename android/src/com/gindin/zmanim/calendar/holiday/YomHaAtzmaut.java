/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
*
*/
class YomHaAtzmaut
	extends AdjustableDateObservance {

	YomHaAtzmaut() {
		super( HebrewCalendar.IYAR, 5, "Yom Ha'Atzmaut" );
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
		if ( HebrewCalendar.FRIDAY == dayOfWeek ) {
			adjustment = -1;
		}
		else if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
			adjustment = -2;
		}
		else if ( HebrewCalendar.MONDAY == dayOfWeek ) {
			adjustment = 1;
		}

		return adjustment;
	}
}
