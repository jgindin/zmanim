/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import java.util.*;

/**
 * Utilities for working with zmanim.
 */
public class ZmanUtils {

	public static final int                                   MIN_TO_MS_CONVERSION = 60 * 1000;

	/** What's the longest time before a zman that we'll allow an alert? */
	public static final int                                   MAX_ALERT_TIME = 60 * MIN_TO_MS_CONVERSION;

	/** When we offer a default, this is what we start with. */
	public static final int                                   DEFAULT_ALERT_TIME = 15 * MIN_TO_MS_CONVERSION;

	/** Sentinel value to indicate the user doesn't want an alert for the zman. */
	public static final int                                   NO_ALERT = -1;

	/** How often should we be checking for something to do? */
	private static final int                                  CHECK_INTERVAL = MAX_ALERT_TIME / 2;

	/** If we wake up within a short time of needing to alert anyways, just do it. */
	private static final int                                  ALERT_FUDGE_FACTOR = 15 * 1000;


	/**
	 * Based on the location and current time, figure out which zman is the next for which the user wants a notification.
	 *
	 * @param location Where are we?
	 * @param now What time is it?
	 * @param zmanCallback Whom to notify of the appropriate next action(s)
	 */
	@SuppressWarnings( "FeatureEnvy" )
	public static void run(
		final ZmanimLocation    location,
		final Calendar          now,
		final ZmanCallback      zmanCallback
	) {

		// Get all the rest of the day's zmanim.
		List<ZmanNotification> restOfTheDaysZmanim = findTheRestOfTheDaysZmanim( location, now, zmanCallback );

		// If there are no more zmanim, then we have to roll to the next day, and check again.
		if ( restOfTheDaysZmanim.isEmpty() ) {
			now.add( Calendar.DATE, 1 );
			now.set( Calendar.HOUR_OF_DAY, 0 );
			now.set( Calendar.MINUTE, 0 );
			now.set( Calendar.SECOND, 0 );
			now.set( Calendar.MILLISECOND, 0 );

			restOfTheDaysZmanim = findTheRestOfTheDaysZmanim( location, now, zmanCallback );
		}


		//  If there are no candidates, then just schedule another check, and we're done.
		if ( restOfTheDaysZmanim.isEmpty() ) {
			zmanCallback.scheduleNextCheck( now, CHECK_INTERVAL );
			return;
		}


		// We have some candidates, and we know that they are sooner than the max alert time.
		final ZmanNotification nextZmanNotification = findNextZmanNotification( restOfTheDaysZmanim, zmanCallback );

		// If we have to notify now, do it. Note that if we're within a couple of seconds, we'll just notify now.
		if ( nextZmanNotification.timeToNotify <= ALERT_FUDGE_FACTOR ) {
			zmanCallback.notifyUser( now, nextZmanNotification.zman );

			// That zman will no longer be a candidate, so go run again...
			run( location, now, zmanCallback );
		}
		else {

			// Schedule the next check...
			zmanCallback.scheduleNextCheck( now, nextZmanNotification.timeToNotify );
		}

	}



	/**
	 * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
	 *
	 * @return For each remaining zman, the ZmanNotification.timeToNotify represents the amount of time (in ms) until the zman occurs.
	 */
	private static List<ZmanNotification> findTheRestOfTheDaysZmanim(
		final ZmanimLocation    location,
		final Calendar          now,
		final ZmanCallback      zmanCallback
	) {

		// Basic idea here...
		//  1. Get the zmanim for today
		HebrewDate hebrewDate = HebrewCalendar.getForDate( now );
		List<Zmanim> daysZmanim = hebrewDate.getZmanim( location );

		//  2. Find all the zmanim that:
		//    - We haven't already notified about;
		//    - Are after the current time;
		List<ZmanNotification> candidates = new ArrayList<>();

		for ( Zmanim zmanim : daysZmanim ) {
			String zmanName = zmanCallback.getPreferredZman( zmanim.type );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			if ( !zmanCallback.hasBeenNotified( now, zman ) ) {
				final int howLongUntil = howLongUntilZman( now, zman );

				// If we've missed this one by "just a bit" then since we didn't notify for it, we probably missed it...
				if ( howLongUntil > -ALERT_FUDGE_FACTOR ) {

					//noinspection AutoBoxing
					candidates.add( new ZmanNotification( zman, howLongUntil ) );
				}
			}
		}

		return candidates;
	}


	/**
	 * Figure out the zman which is next to be notified, and how long (in ms) until the notification.
	 */
	@SuppressWarnings( { "AutoUnboxing", "AutoBoxing" } )
	private static ZmanNotification findNextZmanNotification(
		final List<ZmanNotification>  restOfTodaysZmanim,
		final ZmanCallback            zmanCallback
	) {

		Zman nextZman = null;
		int msToNextNotification = CHECK_INTERVAL;

		for ( ZmanNotification zmanNotification : restOfTodaysZmanim ) {

			final Zman zman = zmanNotification.zman;
			final Integer msUntilZman = zmanNotification.timeToNotify;
			final int zmanAlertTime = zmanCallback.getZmanAlertTime( zman.type ) * MIN_TO_MS_CONVERSION;

			if ( zmanAlertTime >= 0 ) {
				final int msToZmanNotification;
				if ( zmanAlertTime > msUntilZman ) {
					msToZmanNotification = 0;
				}
				else {
					msToZmanNotification = msUntilZman - zmanAlertTime;
				}

				if ( msToZmanNotification < msToNextNotification ) {
					nextZman = zman;
					msToNextNotification = msToZmanNotification;
				}
			}
		}

		return new ZmanNotification( nextZman, msToNextNotification );
	}


	/**
	 * Calculate the milliseconds of minutes between the given date and the zman. If it cannot be calculated for any
	 *  reason, we'll return Integer.MAX_VALUE.
	 */
	private static int howLongUntilZman(
		final Calendar  now,
		final Zman      nextZman
	) {

		// Just in case something has gone wrong, it will be a long time...
		if ( ( nextZman == null ) || ( now == null ) ) {
			return Integer.MAX_VALUE;
		}

		long nowTime = now.getTime().getTime();
		long zmanTime = nextZman.getTime().getTime().getTime();

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


	private static final class ZmanNotification {

		final Zman                                              zman;
		final int                                               timeToNotify;


		ZmanNotification(
			Zman  zman,
			int   timeToNotify
		) {
			this.zman = zman;
			this.timeToNotify = timeToNotify;
		}
	}

} // End of ZmanUtils class
