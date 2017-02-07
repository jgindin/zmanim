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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
  private static final int                                  LONG_ALERT_FUDGE_FACTOR = 5 * MIN_TO_MS_CONVERSION;

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
    if ( isTimeToNotify( now, nextZmanNotification ) ) {
      if ( shouldActuallyNotify( location, now, hebrewDate ) ) {
        zmanCallback.notifyUser( now, nextZmanNotification.zman );
      }

      // Regardless, mark this zman as notified.
      zmanCallback.completed( now, nextZmanNotification.zman );

      // That zman will no longer be a candidate, so go run again...
      run( location, now );
    }
		else {

			// Schedule the next check...
      long next = ( nextZmanNotification.msUntilAction > 0 ? nextZmanNotification.msUntilAction : CHECK_INTERVAL );
			zmanCallback.scheduleNextCheck( now, next );
		}
	}


  /**
	 * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
	 *
	 * @return For each remaining zman, the ZmanNotification.msUntilAction represents the amount of
   *  time (in ms) until the zman occurs. Note specifically, this is NOT the time until an alert
   *  is to be raised, but the time until the Zman!
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

    final long alarmFudge = getAlarmFudge();
    final long timeInMillis = now.getTimeInMillis();

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
      long timeDiff = ( zman.getTime().getTimeInMillis() + alarmFudge ) - timeInMillis;
      if ( ( timeDiff > 0 ) && !zmanCallback.hasBeenNotified( now, zman ) ) {

				// Yes...if we haven't notified for this zman, then add it to the list. Even if we managed to miss the
				//  time we wanted to alert, at least we can alert as soon as we know about it.
	      // We have to subtract off the fudge factor that was added above. In that case we want to be a bit
	      //  sloppy, but now we want the actual time until the zman.
				candidates.add( new ZmanNotification( zman, timeDiff - alarmFudge ) );
			}
		}

		return candidates;
	}


	/**
	 * Figure out the zman which is next to be notified, and how long (in ms) until the notification.
	 */
  @NotNull
	private ZmanNotification findNextZmanNotification(
    final List<ZmanNotification> restOfTodaysZmanim
  ) {

		Zman nextZman = null;
		long msToNextNotification = CHECK_INTERVAL;

		for ( ZmanNotification zmanNotification : restOfTodaysZmanim ) {

			final Zman zman = zmanNotification.zman;
			final int minutesToAlertBefore = zmanCallback.howManyMinutesToAlertBefore( zman.type );

			if ( minutesToAlertBefore >= 0 ) {
				final int msToAlertBefore = minutesToAlertBefore * MIN_TO_MS_CONVERSION;

				final long msToZmanNotification;

				final long msUntilZman = zmanNotification.msUntilAction;
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
   * Determine if it's time to notify for the specified notification.
   */
	private boolean isTimeToNotify(
    final Calendar          now,
	  final ZmanNotification  zmanNotification
  ) {

    // If there's no zman in the notification, then it's too far away.
    if ( zmanNotification.zman == null ) {
      return false;
    }

    // If we've already notified for this one, then don't notify again.
    if ( zmanCallback.hasBeenNotified( now, zmanNotification.zman ) ) {
      return false;
    }

    if ( zmanNotification.msUntilAction > getAlarmFudge() ) {
      return false;
    }

    return true;
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
    final HebrewDate        today
  ) {

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



  private long getAlarmFudge() {
    if ( zmanCallback.needExtraLongAlarmFudge() ) {
     return LONG_ALERT_FUDGE_FACTOR;
    }
    else {
      return DEFAULT_ALERT_FUDGE_FACTOR;
    }
  }



  /**
   * Contains information about an upcoming notification.
   */
  static final class ZmanNotification {

  	/** If null, then msUntilAction will be CHECK_INTERVAL. */
    @Nullable
    final Zman                                         zman;

    final long                                         msUntilAction;


    ZmanNotification(
	    @Nullable Zman  zman,
	    long            msUntilAction
    ) {
      this.zman = zman;
      this.msUntilAction = ( ( zman == null ) || ( msUntilAction < 0 ) ? CHECK_INTERVAL : msUntilAction );
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
