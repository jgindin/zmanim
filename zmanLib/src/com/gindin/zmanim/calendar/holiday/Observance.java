/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;

/**
 * Represents an observance of some kind in the Jewish Calendar.
 */
public interface Observance {

	String getName();

	/**
	 * If the observance falls on the specified date, returns details about that
	 *  particular instance of the observance.
	 */
	Details getDetailsFor( HebrewDate date );


	public interface Details {

		/**
		 * Is this actually erev of the observance? If so, today is NOT actually the
		 *  observance.
		 */
		boolean isErev();

		/**
		 * Does one light candles tonight for this observance? This could be the first night
		 *  of the observance, or in some cases the second or subsequent evenings too.
		 */
		boolean lightCandles();

		/**
		 * Does one say havdalah tonight at the end of this observance? Not all observances end
		 *  with havdalah.
		 */
		boolean sayHavdalah();

		/**
		 * A complete description of today's observance. Could be something simple like,
		 *  "Yom Kippur" or something more complex like "Pesach 4 Chol HaMoed / 3rd Day of the Omer"
		 */
		String getDescription();
	}

}  // End of Observance class
