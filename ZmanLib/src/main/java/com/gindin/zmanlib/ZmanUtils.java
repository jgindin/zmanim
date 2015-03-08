/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilities for working with zmanim.
 */
public class ZmanUtils {

	/**
	 * Used in conjunction with #findNextZman to allow control over which of the various zmanim for the given
	 *  type is preferred by the user.
	 */
	public interface PreferredZmanTypeLookup {

		/**
		 * @return For the given type, what is the name of the zman the user prefers to use? May be null to indicate
		 *  the default is to be used.
		 *  E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
		 */
		String getPreferredZman( Zmanim.Type type );
	}


	/**
	 * Used in conjunction with #determineNextWakeup in case the user actually should be notified about an upcoming zman.
	 */
	public interface ZmanNotificationCallback {

		/**
		 * The user should be notified about the specified zman.
		 */
		void notifyUser(
			Zman nextZman
		);

		/**
		 * Indicates that the next check should be scheduled.
		 *  @param now The current time.
		 * @param timeUntilNextCheck How long from now until the next check is to be scheduled.
		 * @param nextZman
		 */
		void scheduleNextCheck(
			Calendar now,
			int timeUntilNextCheck,
			final Zman nextZman
		);
	}


	public static class NotifiedZmanim
		implements Serializable {

		/** The date this applies to. */
		private final int                                       year;
		private final int                                       month;
		private final int                                       date;

		/** Which of the zmanim types have already been notified? */
		private final Set<Zmanim.Type>                          notifiedTypes = new HashSet<Zmanim.Type>();


		public NotifiedZmanim(
			final Calendar day
		) {

			this.year = day.get( Calendar.YEAR );
			this.month = day.get( Calendar.MONTH );
			this.date = day.get( Calendar.DATE );
		}


		@SuppressWarnings( "FeatureEnvy" )
		public NotifiedZmanim( final HebrewDate hebrewDate ) {

			this.year = hebrewDate.getGregorianYear();
			this.month = hebrewDate.getGregorianMonth();
			this.date = hebrewDate.getHebrewDayOfMonth();
		}


		/**
		 * Once the user has been notified about the specified type, then this method
		 */
		private void notified(
			final Zman zman
		) {

			notifiedTypes.add( zman.type );
		}


		public boolean isForDate( final Calendar other ) {

			int otherYear = other.get( Calendar.YEAR );
			if ( year != otherYear ) {
				return false;
			}

			int otherMonth = other.get( Calendar.MONTH );
			if ( month != otherMonth ) {
				return false;
			}

			int otherDate = other.get( Calendar.DATE );
			if ( date != otherDate ) {
				return false;
			}

			return true;
		}


		boolean contains( final Zmanim.Type type ) {
			return notifiedTypes.contains( type );
		}
	}


	/**
	 * Finds the next zman of interest (given the specified location and time), and then determines what
	 *  action to take: wait a specified amount of time, and then check again and/or notify the user.
	 *
	 * @param location Where are we?
	 * @param now What time is it?
	 * @param notifiedZmanim Which zmanim have been notified already for the specified date?
	 * @param alarmCheckInterval How often do we plan on making these checks?
	 * @param timeToAlertBeforeZman How long before the next zman does the user want to be alerted?
	 * @param preferredZmanTypeLookup How to find out the preferred zman for a given type
	 * @param zmanNotificationCallback Whom to notify of the appropriate next action(s)
	 */
	public static void nextZmanAction(
		final ZmanimLocation            location,
		final Calendar                  now,
		final NotifiedZmanim            notifiedZmanim,
		final int                       alarmCheckInterval,
		final int                       timeToAlertBeforeZman,
		final PreferredZmanTypeLookup   preferredZmanTypeLookup,
		final ZmanNotificationCallback  zmanNotificationCallback
	) {

		Zman nextZman = findNextZman( location, now, notifiedZmanim, preferredZmanTypeLookup );
		int nextWakeupDelta = ZmanUtils.determineNextWakeup( nextZman, now, alarmCheckInterval, timeToAlertBeforeZman );

		if ( nextWakeupDelta <= 0 ) {
			zmanNotificationCallback.notifyUser( nextZman );
			notifiedZmanim.notified( nextZman );

			nextZman = ZmanUtils.findNextZman( location, nextZman.getTime(), notifiedZmanim, preferredZmanTypeLookup );
			nextWakeupDelta = ZmanUtils.determineNextWakeup( nextZman, now, alarmCheckInterval, timeToAlertBeforeZman );
		}

		zmanNotificationCallback.scheduleNextCheck( now, nextWakeupDelta, nextZman );
	}


	/**
	 * Given the specified location and time, what is the next zman of interest?
	 *
	 * @param location Where are we?
	 * @param now What time is it?
	 * @param notifiedZmanim Which zmanim have been notified already for the specified date?
	 * @param preferredZmanTypeLookup How to find out the preferred zman for a given type
	 *
	 * @return the next zman
	 */
	static Zman findNextZman(
		final ZmanimLocation          location,
		final Calendar                now,
		final NotifiedZmanim          notifiedZmanim,
		final PreferredZmanTypeLookup preferredZmanTypeLookup
	) {

		Zman nextZman = null;

		// It seems like there may be some location where night Chatzot is before midnight. In that case
		//  we won't find the next zman...
		// There *is* an issue where we're asked to find the next zman after, say midnight. Problem is that
		//  we miss night Chatzot, because that's on the previous day.
		HebrewDate hebrewDate = HebrewCalendar.getForDate( now );
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
			if ( time.after( now ) ) {
				// We found the next zman! BUT... there's a case like the following:
				//  1. Shkia is at 4:18pm, so we alert at 4:03pm
				//  2. Latest mincha is at 4:27pm, so we alert at 4:12pm
				//  3. When we perform the alert for Shkia, we set the next time to check as 4:12pm. Well, that's
				//    before shkia, so we can't tell that we don't need to alert.
				//
				// This data structure is intended to help with this problem.
				if ( !notifiedZmanim.contains( zman.type ) ) {
					nextZman = zman;
					break;
				}
			}
		}

		return nextZman;
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
	static int determineNextWakeup(
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

		return nextWakeupDelta;
	}


	/**
	 * Calculate the number of milliseconds between the given date and the zman. If it cannot be calculated for any
	 *  reason, we'll return Integer.MAX_VALUE.
	 */
	private static int howLongUntil(
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
	 * Private ctor so the class cannot be instantiated.
	 */
	private ZmanUtils() {
	}



} // End of ZmanUtils class
