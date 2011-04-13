/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Used by the isErev() method to create a 'placeholder'.
 */
public class Erev
	extends JewishHoliday {

	private final boolean                                   showCandles;


	Erev(
		JewishHoliday holiday,
		boolean       showCandles
	) {

		super( holiday.month, holiday.date, "Erev " + holiday.getName() );
		this.showCandles = showCandles;
	}


	@Override
	public boolean showCandlelighting() {
		return showCandles;
	}


	@Override
	public boolean showHavdalah() {
		// We're erev...can't be Havdalah unless it's also motzae Shabbat!
		return false;
	}


	@Override
	protected JewishHoliday createErevOf( HebrewCalendar forDate ) {
		// Makes no sense!
		return null;
	}


	@Override
	protected JewishHoliday createForDate( HebrewCalendar forDate ) {
		// Makes no sense!
		return null;
	}
}
