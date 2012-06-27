/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;

/**
 * Represents an observance of some kind in the Jewish Calendar.
 */
public abstract class Observance {


	public abstract String getName();

	/**
	 * If the observance falls on the specified date, returns details about that
	 *  particular instance of the observance.
	 */
	public abstract Details getDetailsFor( HebrewDate date );


}  // End of Observance class
