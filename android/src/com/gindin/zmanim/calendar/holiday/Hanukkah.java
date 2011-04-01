/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Special class for Hanukkah so that we can display which day of the holiday it is.
 */
@SuppressWarnings( { "MagicNumber" } )
class Hanukkah
	extends MultiDayObservance {


	Hanukkah() {
		this( null );
	}


	Hanukkah(
		HebrewCalendar forDate
	) {

		super( HebrewCalendar.KISLEV, 25, 8, "Hanukkah", forDate );
	}


	@Override
	public JewishHoliday createForDate( HebrewCalendar hebrewCalendar ) {
		return new Hanukkah( hebrewCalendar );
	}

}
