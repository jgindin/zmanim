/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utilities for working with zmanim.
 */
public class ZmanNotificationScheduler {

	/** If we wake up within a short time of needing to alert anyways, just do it. */
	public static final int                                   ALERT_FUDGE_FACTOR = 90 * 1000;

	public static final int                                   MIN_TO_MS_CONVERSION = 60 * 1000;

	public static final int                                   FIFTEEN_MINUTES = 15;

	public static final int                                   ONE_HOUR = 60;

	/** What's the longest time before a zman that we'll allow an alert? */
	public static final int                                   MAX_ALERT_TIME = ONE_HOUR * MIN_TO_MS_CONVERSION;

	/** Sentinel value to indicate the user doesn't want an alert for the zman. */
	public static final int                                   NO_ALERT = -1;

	/** How often should we be checking for something to do? */
	public static final int                                   CHECK_INTERVAL = MAX_ALERT_TIME / 2;



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
		if ( nextZmanNotification.msUntilAction <= ALERT_FUDGE_FACTOR ) {
			zmanCallback.notifyUser( now, nextZmanNotification.zman );

			// That zman will no longer be a candidate, so go run again...
			run( location, now, zmanCallback );
		}
		else {

			// Schedule the next check...
			zmanCallback.scheduleNextCheck( now, nextZmanNotification.msUntilAction );
		}

	}



	/**
	 * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
	 *
	 * @return For each remaining zman, the ZmanNotification.msUntilAction represents the amount of time (in ms) until the zman occurs.
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

		final long nowTime = now.getTime().getTime();

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
				final int msUntilZman = computeMsUntilZman( nowTime, zman );

				// Yes...if we haven't notified for this zman, then add it to the list. Even if we managed to miss the
				//  time we wanted to alert, at least we can alert as soon as we know about it.
				//noinspection AutoBoxing
				candidates.add( new ZmanNotification( zman, msUntilZman ) );
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
			final int minutesToAlertBefore = zmanCallback.howManyMinutesToAlertBefore( zman.type );

			if ( minutesToAlertBefore >= 0 ) {
				final int msToAlertBefore = minutesToAlertBefore * MIN_TO_MS_CONVERSION;

				final int msToZmanNotification;

				final int msUntilZman = zmanNotification.msUntilAction;
				if ( msToAlertBefore > msUntilZman ) {
					msToZmanNotification = 0;
				}
				else {
					msToZmanNotification = msUntilZman - msToAlertBefore;
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
	 *
	 * @param now Expected to be the result of Calendar.getTime().getTime(); this is done since the common case is
	 *              to call this method in a loop, and it seemed quite unnecessary to call this so many times.
	 */
	private static int computeMsUntilZman(
		final long      now,
		final Zman      nextZman
	) {

		// Just in case something has gone wrong, it will be a long time...
		if ( nextZman == null ) {
			return Integer.MAX_VALUE;
		}

		final Calendar nextZmanTime = nextZman.getTime();
		if ( nextZmanTime == null ) {
			// There are some zmanim which just cannot be calculated for a given location.
			return Integer.MAX_VALUE;
		}

		long zmanTime = nextZmanTime.getTime().getTime();

		long delta = zmanTime - now;
		if ( delta > Integer.MAX_VALUE ) {
			return Integer.MAX_VALUE;
		}

		return (int)delta;
	}


	/**
	 * Private ctor so the class cannot be instantiated.
	 */
	private ZmanNotificationScheduler() {
	}



	public static final class NotificationDefaults {

		public int getDefaultZmanAlertTime( final Zmanim.Type type ) {

			switch ( type ) {
				case Alot:
					return ZmanNotificationScheduler.NO_ALERT;
				case Talit:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case Hanetz:
					return ZmanNotificationScheduler.NO_ALERT;
				case SofZmanShema:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case SofZmanTefila:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case SofZmanAchilatChametz:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case SofZmanBiurChametz:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case Chatzot:
					return ZmanNotificationScheduler.NO_ALERT;
				case MinchaGedola:
					return ZmanNotificationScheduler.NO_ALERT;
				case MinchaKetana:
					return ZmanNotificationScheduler.NO_ALERT;
				case PlagHaMincha:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case CandleLighting:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case Shkia:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case BeinHashmashot:
					return ZmanNotificationScheduler.NO_ALERT;
				case LatestMincha:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case FastEnd:
					return ZmanNotificationScheduler.FIFTEEN_MINUTES;
				case TzaitHacochavim:
					return ZmanNotificationScheduler.NO_ALERT;
				case ShabbatEnd:
					return ZmanNotificationScheduler.NO_ALERT;
				case NightChatzot:
					return ZmanNotificationScheduler.NO_ALERT;
			}

			assert false;
			return ZmanNotificationScheduler.NO_ALERT;
		}

	}

	private static final class ZmanNotification {

		final Zman                                              zman;
		final int                                               msUntilAction;


		ZmanNotification(
			Zman  zman,
			int msUntilAction
		) {
			this.zman = zman;
			this.msUntilAction = msUntilAction;
		}


		@Override
		public String toString() {
			return "ZmanNotification{" +
				"zman=" + ( zman == null ? "<next zman too far away>" : zman.type ) +
				", msUntilAction=" + msUntilAction + " (" + new Date( System.currentTimeMillis() + msUntilAction ) + ")" +
				'}';
		}
	}

} // End of ZmanUtils class
