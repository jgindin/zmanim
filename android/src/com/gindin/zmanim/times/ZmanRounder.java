/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper class that handles rounding of zmanim.
 */
public class ZmanRounder {

	private static final Calendar scratchCalendar = Calendar.getInstance();

	/**
	 * Because the application does not show seconds to the user, we want to ensure that we're not going
	 *  to cause an error. Therefore, for some zmanim, we want to ensure that the time which gets displayed
	 *  is not rounded up. E.g., If the latest time for saying mincha is actually 4:33.35, we're much better
	 *  off if we report that time as 4:33, rather than 4:34.
	 *
	 * @param zman The zman to round
	 *
	 * @return the rounded zman.
	 */
	public Date roundToEarliestMinute(
		Date zman
	) {

	  scratchCalendar.setTime( zman );
		scratchCalendar.set( Calendar.SECOND, 0 );
		scratchCalendar.set( Calendar.MILLISECOND, 0 );

		return scratchCalendar.getTime();
	}


	/**
	 * Because the application does not show seconds to the user, we want to ensure that we're not going
	 *  to cause an error. Therefore, for some zmanim, we want to ensure that the time which gets displayed
	 *  is rounded up. E.g., If the earliest time for putting on tefiln is actually 6:33.05, we're much better
	 *  off if we report that time as 6:34, rather than 6:33.
	 *
	 * @param zman The zman to round
	 *
	 * @return the rounded zman.
	 */
	public Date roundToLatestMinute(
		Date zman
	) {

		scratchCalendar.setTime( zman );

		scratchCalendar.add( Calendar.MINUTE, 1 );
		scratchCalendar.set( Calendar.SECOND, 0 );
		scratchCalendar.set( Calendar.MILLISECOND, 0 );

		return scratchCalendar.getTime();
	}

} // End of ZmanRounder class
