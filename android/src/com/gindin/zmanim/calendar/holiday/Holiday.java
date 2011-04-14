/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 *
 */
public abstract class Holiday
	extends JewishDay {

	protected Holiday(
		int     month,
		int     date,
		String  name
	) {
		super( month, date, name );
	}


	protected Holiday(
		int             month,
		int             date,
		String          name,
		HebrewCalendar  forDate
	) {
		super( month, date, name, forDate );
	}


	@Override
	public boolean showCandlelighting() {
		return true;
	}


}  // End of Holiday class
