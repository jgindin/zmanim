/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
class HoshanahRabbah
	extends Observance {

	HoshanahRabbah() {
		super(  HebrewCalendar.TISHRI, 21, "Hoshanah Rabbah"  );
	}


	@Override
	public boolean showCandlelighting() {
		// We know the next day is Shemini Atzeret
		return true;
	}
}
