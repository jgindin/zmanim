/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utilities for working with zmanim.
 */
public class ZmanNotificationScheduler {

	/** If we wake up within a short time of needing to alert anyways, just do it. */
	public static final int                                   ALERT_FUDGE_FACTOR = 30 * 1000;

	public static final int                                   MIN_TO_MS_CONVERSION = 60 * 1000;

	public static final int                                   FIFTEEN_MINUTES = 15;

	public static final int                                   ONE_HOUR = 60;

	/** What's the longest time before a zman that we'll allow an alert? */
	public static final int                                   MAX_ALERT_TIME = ONE_HOUR * MIN_TO_MS_CONVERSION;

	/** Sentinel value to indicate the user doesn't want an alert for the zman. */
	public static final int                                   NO_ALERT = -1;

	/** How often should we be checking for something to do? */
	public static final int                                   CHECK_INTERVAL = MAX_ALERT_TIME / 2;

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


		//  If there are no candidates, then just schedule another check, and we're done.
		if ( restOfTheDaysZmanim.isEmpty() ) {
			zmanCallback.scheduleNextCheck( now, CHECK_INTERVAL );
			return;
		}

		// We have some candidates, and we know that they are sooner than the max alert time.
		final ZmanNotification nextZmanNotification = findNextZmanNotification( restOfTheDaysZmanim );

		// If we have to notify now, do it. Note that if we're within a couple of seconds, we'll just notify now.
		if ( shouldActuallyNotify( location, now, nextZmanNotification, hebrewDate ) ) {

      zmanCallback.notifyUser( location, now, nextZmanNotification.zman );

			// That zman will no longer be a candidate, so go run again...
			run( location, now );
		}
		else {

			// Schedule the next check...
      int next = ( nextZmanNotification.msUntilAction > 0 ? nextZmanNotification.msUntilAction : CHECK_INTERVAL );
			zmanCallback.scheduleNextCheck( now, next );
		}
	}


  /**
	 * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
	 *
	 * @return For each remaining zman, the ZmanNotification.msUntilAction represents the amount of time (in ms) until the zman occurs.
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
      long timeDiff = ( zman.getTime().getTimeInMillis() + ALERT_FUDGE_FACTOR ) - now.getTimeInMillis();
      if ( ( timeDiff > 0 ) && !zmanCallback.hasBeenNotified( now, zman ) ) {

				// Yes...if we haven't notified for this zman, then add it to the list. Even if we managed to miss the
				//  time we wanted to alert, at least we can alert as soon as we know about it.
				//noinspection AutoBoxing
				candidates.add( new ZmanNotification( zman, now ) );
			}
		}

		return candidates;
	}


	/**
	 * Figure out the zman which is next to be notified, and how long (in ms) until the notification.
	 */
	@SuppressWarnings( { "AutoUnboxing", "AutoBoxing" } )
	private ZmanNotification findNextZmanNotification(
    final List<ZmanNotification> restOfTodaysZmanim
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
   * Prior to actually notifying the user, figure out if this is a notification the user
   *  should receive.
   *
   * @return true if the user should be notified; false otherwise.
   */
  @SuppressWarnings( "FeatureEnvy" )
  private boolean shouldActuallyNotify(
    final ZmanimLocation    location,
    final Calendar          now,
    final ZmanNotification  zmanNotification,
    final HebrewDate        today
  ) {

    // If there's no zman in the notification, then it's too far away.
    if ( zmanNotification.zman == null ) {
      return false;
    }

    // If we've already notified for this one, then don't notify again.
    if ( zmanCallback.hasBeenNotified( now, zmanNotification.zman ) ) {
      return false;
    }

    if ( zmanNotification.msUntilAction > ALERT_FUDGE_FACTOR ) {
      return false;
    }

    if ( zmanCallback.shouldNotifyOnShabbat() ) {
      return true;
    }

    Details todayDetails = today.getDetails();
    HebrewDate tomorrow = today.tomorrow();
    final Details tomorrowDetails = tomorrow.getDetails();

    // If today is erev something, and it's after candle lighting, then don't notify...
    if ( todayDetails.isErev() && today.isAfter( now, Zmanim.Type.CandleLighting, location ) ) {
      return false;
    }

    // If today is Shabbat, but it's after sunset, AND tomorrow's not a Yom Tov, then go ahead...
    if ( todayDetails.isShabbat() && today.isAfter( now, Zmanim.Type.ShabbatEnd, location ) && !tomorrowDetails.isYomTov() ) {
      return true;
    }

    // If today is YomTov, it's after sunset, and tomorrow's not a Yom Tov, then go ahead...
    if ( todayDetails.isYomTov() && today.isAfter( now, Zmanim.Type.ShabbatEnd, location ) && !tomorrowDetails.isYomTov() ) {
      return true;
    }

    return ( !todayDetails.isShabbat() && !todayDetails.isYomTov() );
  }



  /**
   * Contains information about an upcoming notification.
   */
  static final class ZmanNotification {

    final Zman                                         zman;

    final int                                          msUntilAction;


    ZmanNotification(
	    @Nullable Zman  zman,
	    int             msToNextNotification
    ) {
      this.zman = zman;
      this.msUntilAction = msToNextNotification;
    }


    ZmanNotification(
      Zman      zman,
      Calendar  now
    ) {
      this( zman, computeMsUntilZman( zman, now.getTimeInMillis() ) );
    }


    @Override
    public String toString() {
      return "ZmanNotification{" +
        "zman=" + ( zman == null ? "<next zman too far away>" : zman.type ) +
        ", msUntilAction=" + msUntilAction + " (" + new Date( System.currentTimeMillis() + msUntilAction ) + ")" +
        '}';
    }


    /**
     * Calculate the milliseconds of minutes between the given date and the zman. If it cannot be calculated for any
     *  reason, we'll return Integer.MAX_VALUE.
     *
     * @param now Expected to be the result of Calendar.getTime().getTime(); this is done since the common case is
     *              to call this method in a loop, and it seemed quite unnecessary to call this so many times.
     */
    private static int computeMsUntilZman(
      final Zman nextZman,
      final long now
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

      long zmanTime = nextZmanTime.getTimeInMillis();

      long delta = zmanTime - now;
      if ( delta > Integer.MAX_VALUE ) {
        return Integer.MAX_VALUE;
      }

      return (int)delta;
    }
  }

} // End of ZmanUtils class
