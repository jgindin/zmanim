/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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



	/**
	 * Finds the next zman of interest (given the specified location and time), and then determines what
	 *  action to take: wait a specified amount of time, and then check again and/or notify the user.
	 *
	 * @param location Where are we?
	 * @param now What time is it?
	 * @param notifiedZmanim Which zmanim have been notified already for the specified date?
	 * @param alarmCheckInterval How often do we plan on making these checks?
	 * @param zmanCallback Whom to notify of the appropriate next action(s)
	 */
	public static void nextZmanAction(
		final ZmanimLocation            location,
		final Calendar                  now,
		final NotifiedZmanim            notifiedZmanim,
		final int                       alarmCheckInterval,
		final ZmanCallback              zmanCallback
	) {

		Zman nextZman = findNextZman( location, now, notifiedZmanim, zmanCallback );
		int nextWakeupDelta = determineNextWakeup( nextZman, now, alarmCheckInterval, zmanCallback );

		if ( nextWakeupDelta <= 0 ) {
			zmanCallback.notifyUser( nextZman );
			notifiedZmanim.notified( nextZman );

			nextZman = findNextZman( location, nextZman.getTime(), notifiedZmanim, zmanCallback );
			nextWakeupDelta = determineNextWakeup( nextZman, now, alarmCheckInterval, zmanCallback );
		}

		zmanCallback.scheduleNextCheck( now, nextWakeupDelta, nextZman );
	}


	/**
	 * Given the specified location and time, what is the next zman of interest?
	 *
	 * @param location Where are we?
	 * @param now What time is it? May be rolled to the next day in case there's nothing left today.
	 * @param notifiedZmanim Which zmanim have been notified already for the specified date?
	 * @param zmanCallback How to find out the preferred zman for a given type
	 *
	 * @return the next zman
	 */
	@NotNull
	static Zman findNextZman(
		final ZmanimLocation      location,
		final Calendar            now,
		final NotifiedZmanim      notifiedZmanim,
		final ZmanCallback        zmanCallback
	) {

		Zman nextZman = findNextZman_internal( location, now, notifiedZmanim, zmanCallback );

		// if nextZman is null, we need to roll to tomorrow and re-check...
		if ( nextZman == null ) {
			// move to the next day and try again!
			now.roll( Calendar.DAY_OF_MONTH, true );
			now.set( Calendar.HOUR_OF_DAY, 0 );
			now.set( Calendar.MINUTE, 0 );
			now.set( Calendar.SECOND, 0 );
			nextZman = findNextZman_internal( location, now, notifiedZmanim, zmanCallback );
		}

		assert nextZman != null;
		return nextZman;
	}


	/**
	 * Determine how long until the next time we ought to check if the user needs to be notified of an upcoming zman.
	 *
	 * @param nextZman What is the next zman?
	 * @param now What time is it now?
	 * @param alarmCheckInterval How often do we plan on making these checks?
	 * @param zmanCallback How long before the next zman does the user want to be alerted?
	 *
	 * @return The number of milliseconds from now until the next check to see if its time to notify the user. May
	 *  be negative if there isn't enough time between the next zman and now to satisfy the desired timeToAlertBeforeZman.
	 */
	static int determineNextWakeup(
		final Zman          nextZman,
		final Calendar      now,
		final int           alarmCheckInterval,
		final ZmanCallback  zmanCallback
	) {

		final int timeUntilNextZman = howLongUntilZman( now, nextZman );
		int timeToAlertBeforeZman = zmanCallback.getZmanAlertTime( nextZman.type ) * MIN_TO_MS_CONVERSION;
		if ( timeToAlertBeforeZman < 0 ) {
			timeToAlertBeforeZman = 0;
		}

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


	@SuppressWarnings( "FeatureEnvy" )
	@Nullable
	private static Zman findNextZman_internal(
		final ZmanimLocation    location,
		final Calendar          now,
		final NotifiedZmanim    notifiedZmanim,
		final ZmanCallback      zmanCallback
	) {

		Zman nextZman = null;

		// It seems like there may be some location where night Chatzot is before midnight. In that case
		//  we won't find the next zman...
		// There *is* an issue where we're asked to find the next zman after, say midnight. Problem is that
		//  we miss night Chatzot, because that's on the previous day.
		HebrewDate hebrewDate = HebrewCalendar.getForDate( now );
		List<Zmanim> daysZmanim = hebrewDate.getZmanim( location );
		for ( Zmanim zmanim : daysZmanim ) {
			String zmanName = zmanCallback.getPreferredZman( zmanim.type );
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
				//
				// However, if the zman is actually for the next day, then we haven't started tracking those yet,
				//  so...go ahead and select that one.
				//
				// We also have to handle the case where there's another zman that's actually earlier than this
				//  one (especially when we take into account the interval for alerting).
				if ( nextZman != null ) {
					if ( zman.getTime().before( nextZman.getTime() )) {
						// Yeah, this one is before, but we've already notified for it, so move on to the next one.
						if ( notifiedZmanim.contains( zman.type ) ) {
							continue;
						}
					}
					else {
						// This zman is after the one we already found. It's not what we want...
						continue;
					}
				}

				if ( !notifiedZmanim.isForDate( time ) ) {
					nextZman = zman;
				}
				else if ( !notifiedZmanim.contains( zman.type ) ) {
					nextZman = zman;
				}
			}
		}

		return nextZman;
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
