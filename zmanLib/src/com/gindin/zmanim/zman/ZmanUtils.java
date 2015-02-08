/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;

import java.util.Calendar;
import java.util.List;

/**
 * Utilities for working with zmanim.
 */
public class ZmanUtils {

	public interface PreferredZmanTypeLookup {

		/**
		 * For the given type, what is the name of the zman the user prefers to use?
		 *
		 * E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
		 *
		 */
		String getPreferredZman( Zmanim.Type type );
	}


	public static Zman findNextZman(
		final ZmanimLocation          location,
		final Calendar                mustBeAfter,
		final PreferredZmanTypeLookup preferredZmanTypeLookup
	) {

		Zman nextZman = null;

		// It seems like there may be some location where night Chatzot is before midnight. In that case
		//  we won't find the next zman...
		// There *is* an issue where we're asked to find the next zman after, say midnight. Problem is that
		//  we miss night Chatzot, because that's on the previous day.
		HebrewDate hebrewDate = HebrewCalendar.getForDate( mustBeAfter );
		List<Zmanim> daysZmanim = hebrewDate.getZmanim( location );
		for ( Zmanim zmanim : daysZmanim ) {
			String zmanName = preferredZmanTypeLookup.getPreferredZman( zmanim.type );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			Calendar time = zman.getTime();
			if ( time.after( mustBeAfter ) ) {
				// We found the next zman!
				nextZman = zman;
				break;
			}
		}

		return nextZman;
	}


	/**
	 * Calculate the number of milliseconds between the given date and the zman. If it cannot be calculated for any
	 *  reason, we'll return Integer.MAX_VALUE.
	 */
	public static int howLongUntil(
		final Zman      nextZman,
		final Calendar  now
	) {

		// Just in case something has gone wrong, it will be a long time...
		if ( ( nextZman == null ) || ( now == null ) ) {
			return Integer.MAX_VALUE;
		}

		long nowTime = now.getTime().getTime();
		long zmanTime = nextZman.getTime().getTime().getTime();

		if ( nowTime > zmanTime ) {
			return 0;
		}

		long delta = zmanTime - nowTime;
		if ( delta > Integer.MAX_VALUE ) {
			return Integer.MAX_VALUE;
		}

		return (int)delta;
	}


	/**
	 * Determine how long until the next time we ought to check if the user needs to be notified of an upcoming zman.
	 *
	 * @param nextZman What is the next zman?
	 * @param now What time is it now?
	 * @param alarmCheckInterval How often do we plan on making these checks?
	 * @param timeToAlertBeforeZman How long before the next zman does the user want to be alerted?
	 *
	 * @return The number of milliseconds from now until the next check to see if its time to notify the user. May
	 *  be negative if there isn't enough time between the next zman and now to satisfy the desired timeToAlertBeforeZman.
	 */
	public static int determineNextWakeup(
		final Zman      nextZman,
		final Calendar  now,
		final int       alarmCheckInterval,
		final int       timeToAlertBeforeZman
	) {

		final int timeUntilNextZman = ZmanUtils.howLongUntil( nextZman, now );
		final int timeUntilDesiredAlert = timeUntilNextZman - timeToAlertBeforeZman;

		// What time is the next wake-up?
		final int nextWakeupDelta;

		// There are a few possibilities. The amount of time until the next desired alert might be:
		//  1. Greater than our alarm check interval.
		//      --> Just schedule the next wake-up at the alarmCheckInterval.
		//  2. Less than our wake-up interval, but greater than the desired alarm time. Schedule the next
		//      wake-up for when we want to show the alert.

		if ( timeUntilDesiredAlert > alarmCheckInterval ) {
			// E.g., wake up 45 minutes from now.
			nextWakeupDelta = alarmCheckInterval;
		}
		else {
			// E.g., wake up right when we want to alert.
			nextWakeupDelta = timeUntilDesiredAlert;
		}

		//noinspection AutoBoxing
		return nextWakeupDelta;
	}




	/**
	 * Private ctor so the class cannot be instantiated.
	 */
	private ZmanUtils() {
	}



} // End of ZmanUtils class
