/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Format zmanim.
 */
public class ZmanFormatter {

	/** Used to format the zmanim times that get displayed. */
	private final DateFormat                                  timeFormatter;

	/** Used to ensure certain zmanim are rounded properly. */
	private final ZmanRounder                                 zmanRounder;

	
	public ZmanFormatter(
		boolean showSeconds
	) {

		if ( showSeconds ) {
			timeFormatter = DateFormat.getTimeInstance( DateFormat.MEDIUM );
			zmanRounder = null;
		}
		else {
			timeFormatter = DateFormat.getTimeInstance( DateFormat.SHORT );
			zmanRounder = new ZmanRounder();
		}
	}	
	
	
	public String formatZmanTime(
		Zman zman
	) {

		// This can return null in case the zman just doesn't apply. E.g., there are places where the Sun never
		//  actually gets low enough to be able to calculate Alot @ 26degrees.
		Calendar time = zman.getTime();
		if ( null == time ) {
			return "N/A";
		}

		if ( null != zmanRounder ) {
			// Some of the zman types we need to do some rounding so that we avoid a situation of showing
			//  a time that can mislead someone into missing their mitzvah...
			switch ( zman.type ) {

				case Alot:
				case Talit:
				case Hanetz:
				case FastEnd:
				case TzaitHacochavim:
				case ShabbatEnd:
					time = zmanRounder.roundToLatestMinute( time );
					break;

				case SofZmanShema:
				case SofZmanTefila:
				case MinchaGedola:
				case MinchaKetana:
				case PlagHaMincha:
				case CandleLighting:
				case LatestMincha:
					time = zmanRounder.roundToEarliestMinute( time );
					break;
			}
		}

		synchronized ( this ) {
			return timeFormatter.format( time );
		}
	}


	/**
	 * Helper class that handles rounding of zmanim.
	 */
	private static class ZmanRounder {
	

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
		private Calendar roundToEarliestMinute(
			Calendar zman
		) {

			zman.set( Calendar.SECOND, 0 );
			zman.set( Calendar.MILLISECOND, 0 );
	
			return zman;
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
		private Calendar roundToLatestMinute(
			Calendar zman
		) {
	
			zman.add( Calendar.MINUTE, 1 );
			zman.set( Calendar.SECOND, 0 );
			zman.set( Calendar.MILLISECOND, 0 );

			return zman;
		}
	
	} // End of ZmanRounder class
}  // End of ZmanFormatter class
