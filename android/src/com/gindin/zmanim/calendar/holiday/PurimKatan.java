/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/** Special class to ensure that Purim katan only shows up in leap years. */
@SuppressWarnings( { "MagicNumber" } )
class PurimKatan
	extends Observance {

	PurimKatan() {
		super( HebrewCalendar.ADAR_1, 14, "Purim Katan" );
	}


	@Override
	public JewishDay isOn( HebrewCalendar dateToTest ) {

		// Not a leap year? Then this "holiday" doesn't exist...
		if ( !dateToTest.isLeapYear() ) {
			return null;
		}

		return super.isOn( dateToTest );
	}


	@Override
	public JewishDay isErev( HebrewCalendar dateToTest ) {

		// Not a leap year? Then this "holiday" doesn't exist...
		if ( !dateToTest.isLeapYear() ) {
			return null;
		}

		return super.isErev( dateToTest );
	}

}  // End of PurimKatan class
