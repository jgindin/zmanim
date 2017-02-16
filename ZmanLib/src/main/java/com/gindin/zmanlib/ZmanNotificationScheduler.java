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
import java.util.List;

/**
 * Utilities for working with zmanim.
 */
public class ZmanNotificationScheduler {

	static final int                                          FIFTEEN_MINUTES = 15;

	static final int                                          ONE_HOUR = 60;

	public static final int                                   MIN_TO_MS_CONVERSION = 60 * 1000;

	/** What's the longest time before a zman that we'll allow an alert? */
	public static final int                                   MAX_ALERT_TIME = ONE_HOUR * MIN_TO_MS_CONVERSION;

	/** Sentinel value to indicate the user doesn't want an alert for the zman. */
	public static final int                                   NO_ALERT = -1;

	/** How often should we be checking for something to do? */
	public static final int                                   CHECK_INTERVAL = MAX_ALERT_TIME / 2;

  /** If we wake up within a short time of needing to alert anyways, just do it. */
	private static final int                                  DEFAULT_ALERT_FUDGE_FACTOR = 30 * 1000;

  private final ZmanCallback                                zmanCallback;


  /**
	 *
   * @param zmanCallback Whom to notify of the appropriate next action(s)
	 */
	public ZmanNotificationScheduler(
    final ZmanCallback      zmanCallback
  ) {
    this.zmanCallback = zmanCallback;
  }


	/**
	 * Based on the location and current time, figure out which zman is the next for which the user wants a notification.
	 *<p/>
   *
   * Because of the work done in this method, it is assumed that it will NOT be invoked on the main thread.
	 *
	 * @param location Where are we?
	 * @param now What time is it?
	 */
	@SuppressWarnings( "FeatureEnvy" )
	public void run(
		final ZmanimLocation    location,
		final Calendar          now
	) {

    HebrewDate hebrewDate = HebrewCalendar.getForDate( now );

		// Get all the rest of the day's zmanim.
		List<ZmanNotification> restOfTheDaysZmanim = findTheRestOfTheDaysZmanim( location, now, hebrewDate );

		// If there are no more zmanim, then we have to roll to the next day, and check again.
		if ( restOfTheDaysZmanim.isEmpty() ) {
			now.add( Calendar.DATE, 1 );
			now.set( Calendar.HOUR_OF_DAY, 0 );
			now.set( Calendar.MINUTE, 0 );
			now.set( Calendar.SECOND, 0 );
			now.set( Calendar.MILLISECOND, 0 );

      hebrewDate = HebrewCalendar.getForDate( now );
			restOfTheDaysZmanim = findTheRestOfTheDaysZmanim( location, now, hebrewDate );
		}

    // NOTE: It's possible that we rolled to the next Gregorian date, and so now when we go to find
    //  the day's zmanim, we're not going to find last night's chatzot.
    //  For now, this is going to be a bug...


		// If there are no candidates, we're done.
		if ( restOfTheDaysZmanim.isEmpty() ) {
			return;
		}

    for ( ZmanNotification zmanNotification : restOfTheDaysZmanim ) {
      zmanCallback.scheduleAlert( zmanNotification, location, now );
    }
	}


  /**
	 * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
	 *
	 * @return For each remaining zman, the ZmanNotification.msUntilAction represents the amount of
   *  time (in ms) until the ALERT for the zman occurs.
	 */
	private List<ZmanNotification> findTheRestOfTheDaysZmanim(
		final ZmanimLocation    location,
		final Calendar          now,
    final HebrewDate        hebrewDate
	) {

		// Basic idea here...
		//  1. Get the zmanim for today
		List<Zmanim> daysZmanim = hebrewDate.getZmanim( location );

		//  2. Find all the zmanim that:
		//    - We haven't already notified about;
		//    - Are after the current time;
		List<ZmanNotification> candidates = new ArrayList<>();

    final long nowInMillis = now.getTimeInMillis();

		for ( Zmanim zmanim : daysZmanim ) {
			String zmanName = zmanCallback.getPreferredZman( zmanim.type );
			Zman zman;
			if ( zmanName == null ) {
				zman = zmanim.getDefault();
			}
			else {
				zman = zmanim.getByName( zmanName );
			}

			// Make sure the zman is before the current time and we haven't already notified...
      //  Note that we move the zman time forward by a fudge factor so that if we would have otherwise
      //  just missed it, we'll still add it as a candidate.
      long timeDiff = ( zman.getTime().getTimeInMillis() + DEFAULT_ALERT_FUDGE_FACTOR ) - nowInMillis;
      if ( ( timeDiff > 0 ) && !zmanCallback.hasBeenNotified( now, zman.type ) ) {

				// Yes...if we haven't notified for this zman, then add it to the list. Even if we managed to miss the
				//  time we wanted to alert, at least we can alert as soon as we know about it.
	      // We have to subtract off the fudge factor that was added above. In that case we want to be a bit
	      //  sloppy, but now we want the actual time until the zman.
        timeDiff -= DEFAULT_ALERT_FUDGE_FACTOR;

        final ZmanNotification zmanNotification = computeAlertTime( zman, timeDiff );
        if ( zmanNotification != null ) {
          candidates.add( zmanNotification );
        }
			}
		}

		return candidates;
	}


  /**
   * Figure out the milliseconds until an alert for the specified zman should be raised.
   *
   * @return null if no alert is configured for the zman.
   */
	private ZmanNotification computeAlertTime(
	  final Zman  zman,
    final long  msUntilZman
  ) {

    final int minutesToAlertBefore = zmanCallback.howManyMinutesToAlertBefore( zman.type );
    if ( minutesToAlertBefore >= 0 ) {
      final int msToAlertBefore = minutesToAlertBefore * MIN_TO_MS_CONVERSION;

      final long msToZmanNotification;

      if ( msToAlertBefore > msUntilZman ) {
        msToZmanNotification = 0;
      }
      else {
        msToZmanNotification = msUntilZman - msToAlertBefore;
      }

      return new ZmanNotification( zman, msToZmanNotification );
    }

    return null;
  }


} // End of ZmanUtils class
