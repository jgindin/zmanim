/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Used by the isErev() method to create a 'placeholder'.
 */
public class Erev
	extends JewishDay {

	private final boolean                                   showCandles;


	public Erev(
		JewishDay       day,
		HebrewCalendar  forDate,
		boolean         showCandles
	) {

		super( day.month, day.date, "Erev " + day.getName(), forDate );
		this.showCandles = showCandles;
	}


	@Override
	public boolean showCandlelighting() {
		return showCandles;
	}


	@Override
	public boolean showHavdalah() {
		// We're erev...can't be Havdalah unless it's also motzae Shabbat!
		return isShabbat();
	}


	@Override
	protected JewishDay createErevOf( HebrewCalendar forDate ) {
		// Makes no sense!
		return null;
	}


	@Override
	protected JewishDay createForDate( HebrewCalendar forDate ) {
		// Makes no sense!
		return null;
	}


	@Override
	public Erev isErev() {
		// By definition!
		return this;
	}
}
