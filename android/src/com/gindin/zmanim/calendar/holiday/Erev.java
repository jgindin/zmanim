/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

/**
 * Used by the isErev() method to create a 'placeholder'.
 */
class Erev
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
		// We're erev...can't be Havdalah!
		return false;
	}


	@Override
	protected JewishHoliday createErevOf() {
		// Makes no sense!
		return null;
	}
}
