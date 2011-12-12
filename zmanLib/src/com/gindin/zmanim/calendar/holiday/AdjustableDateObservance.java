/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Class to handle those observances which may have to shift dates to ensure they don't fall on certain days.
 */
abstract class AdjustableDateObservance
	extends Observance {

	AdjustableDateObservance(
		int month,
		int date,
		String name
	) {
		super( month, date, name );
	}


	@Override
	public JewishDay isOn( HebrewCalendar dateToTest ) {

		// Wrong month? Then can't be!
		if ( month != dateToTest.get( HebrewCalendar.MONTH ) ) {
			return null;
		}

		int adjustment = getDateAdjustment( dateToTest );
		calendar.add( HebrewCalendar.DATE, adjustment );

		if ( isSameDate( dateToTest, calendar ) ) {
			return createForDate( dateToTest );
		}

		return null;
	}


	@Override
	public JewishDay isErev( HebrewCalendar dateToTest ) {

		// Wrong month? Then can't be!
		if ( month != dateToTest.get( HebrewCalendar.MONTH ) ) {
			return null;
		}

		int adjustment = getDateAdjustment( dateToTest );
		calendar.add( HebrewCalendar.DATE, adjustment - 1 );

		if ( isSameDate( dateToTest, calendar ) ) {
			return createErevOf( dateToTest );
		}

		return null;
	}


	/**
	 * Given the date to test, should any adjustments be made?
	 *
	 * @param dateToTest What date is being tested?
	 * @return The number of days (positive OR negative) to adjust the date of the observance.
	 */
	protected abstract int getDateAdjustment( HebrewCalendar dateToTest );
}
