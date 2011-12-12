/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 * Specialized for Rosh Hashana, because when we try to check for Erev, the years will be different...
 */
public class RoshHashana
	extends MultiDayHoliday {

	RoshHashana() {
		this( null );
	}


	RoshHashana(
		HebrewCalendar forDate
	) {
		super( HebrewCalendar.TISHRI, 1, 2, "Rosh Hashanah", forDate );
	}


	@Override
	public JewishDay isErev( HebrewCalendar dateToTest ) {

		// Basically the same as the base class, but when we're actually checking for Erev Rosh Hashana,
		//  the date to test will be, e.g., "29 Elul 5770". When we subtract a day from our day (1 Tishri),
		//  then we wind up going "back" to 5769, so we don't match...
		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );
		calendar.add( HebrewCalendar.DATE, -1 );
		calendar.add( HebrewCalendar.YEAR, 1 );

		if ( isSameDate( dateToTest, calendar ) ) {
			return createErevOf( dateToTest );
		}

		return null;
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar hebrewCalendar ) {
		return new RoshHashana( hebrewCalendar );
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

		// Only light candles on the 1st of Tishri.
		int day = forDate.get( HebrewCalendar.DATE );
		return ( 1 == day ) || isErevShabbat();
	}


	@Override
	public boolean showHavdalah() {

		if ( null == forDate ) {
			return false;
		}

		assert HebrewCalendar.TISHRI == forDate.get( HebrewCalendar.MONTH );

		// 2nd of Tishri we show Havdalah.
		int day = forDate.get( HebrewCalendar.DATE );
		return ( 2 == day ) && !isErevShabbat();
	}
}
