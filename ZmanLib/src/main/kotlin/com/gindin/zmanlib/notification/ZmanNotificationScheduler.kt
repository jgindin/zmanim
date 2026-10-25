/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.notification

import com.gindin.zmanlib.ZmanCallback
import com.gindin.zmanlib.calendar.HebrewCalendar
import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.location.ZmanimLocation
import com.gindin.zmanlib.zman.Zman
import java.util.Calendar

/** Utilities for working with zmanim. */
public class ZmanNotificationScheduler(
  /** Whom to notify of the appropriate next action(s) */
  private val zmanCallback: ZmanCallback
) {

  /**
   * Based on the location and current time, figure out which zman is the next for which the user
   * wants a notification.
   *
   * <p/>
   *
   * Because of the work done in this method, it is assumed that it will NOT be invoked on the main
   * thread.
   *
   * @param location Where are we?
   * @param now What time is it?
   */
  public fun run(
    location: ZmanimLocation,
    now: Calendar,
  ) {

    var hebrewDate = HebrewCalendar.getForDate(now)

    // Get all the rest of the day's zmanim.
    var restOfTheDaysZmanim = findTheRestOfTheDaysZmanim(location, now, hebrewDate)

    // If there are no more zmanim, then we have to roll to the next day, and check again.
    if (restOfTheDaysZmanim.isEmpty()) {
      now.add(Calendar.DATE, 1)
      now.set(Calendar.HOUR_OF_DAY, 0)
      now.set(Calendar.MINUTE, 0)
      now.set(Calendar.SECOND, 0)
      now.set(Calendar.MILLISECOND, 0)

      hebrewDate = HebrewCalendar.getForDate(now)
      restOfTheDaysZmanim = findTheRestOfTheDaysZmanim(location, now, hebrewDate)
    }

    // NOTE: It's possible that we rolled to the next Gregorian date, and so now when we go to find
    //  the day's zmanim, we're not going to find last night's chatzot.
    //  For now, this is going to be a bug...

    // If there are no candidates, we're done.
    if (restOfTheDaysZmanim.isEmpty()) {
      return
    }

    val timeInMillis = now.timeInMillis

    for (zmanNotification in restOfTheDaysZmanim) {

      val alertTime = computeAlertTime(zmanNotification)

      if (alertTime >= 0) {
        // NOTE: We have to use a new calendar instance each time, otherwise we'd wind up
        // overwriting the time
        //  each iteration through the loop...
        val alertAt = Calendar.getInstance()
        alertAt.timeZone = now.timeZone
        alertAt.timeInMillis = timeInMillis + alertTime
        zmanCallback.scheduleAlert(zmanNotification.zman, location, alertAt)
      }
    }
  }

  /**
   * Find all the rest of the zmanim for the day represented by 'now' which are after 'now'.
   *
   * @return For each remaining zman, the ZmanNotification.msUntilZman represents the amount of time
   *   (in ms) until the zman occurs.
   */
  private fun findTheRestOfTheDaysZmanim(
    location: ZmanimLocation,
    now: Calendar,
    hebrewDate: HebrewDate,
  ): List<ZmanNotification> {

    // Basic idea here...
    //  1. Get the zmanim for today
    val daysZmanim = hebrewDate.getZmanim(location)

    //  2. Find all the zmanim that:
    //    - We haven't already notified about;
    //    - Are after the current time;
    val candidates = ArrayList<ZmanNotification>()

    // Note that we're going to pretend that the current time is just a bit earlier than it actually
    // is. This will
    //  compensate in case Android got a bit sloppy and woke us up just a smidge late...
    val nowInMillis = now.timeInMillis - DEFAULT_ALERT_FUDGE_FACTOR

    for (zmanim in daysZmanim) {
      val zmanName = zmanCallback.getPreferredZman(zmanim.type)
      val zman: Zman
      if (zmanName == null) {
        zman = zmanim.getDefault()!!
      } else {
        zman = zmanim.getByName(zmanName)!!
      }

      // Make sure the zman is before the current time and we haven't already notified...
      //  Note that we move the zman time forward by a fudge factor so that if we would have
      // otherwise
      //  just missed it, we'll still add it as a candidate.
      val zmanTime = zman.getTime()
      if (zmanTime != null) {

        val msUntilZman = zmanTime.timeInMillis - nowInMillis
        val isZmanAfterNow = (msUntilZman >= 0)
        val notYetNotified = !zmanCallback.hasBeenNotified(zmanTime, zman.type)

        if (isZmanAfterNow && notYetNotified) {
          // Note that the time until the zman we calculated above was based on our fudging the
          // current
          //  time. So now, we have to undo that fudge.
          candidates.add(ZmanNotification(zman, msUntilZman - DEFAULT_ALERT_FUDGE_FACTOR))
        }
      }
    }

    return candidates
  }

  /**
   * Figure out the milliseconds until an alert for the specified zman should be raised.
   *
   * @return the number of milliseconds until the alert, or Long.MIN_VALUE if not alert should be
   *   scheduled.
   */
  private fun computeAlertTime(zmanNotification: ZmanNotification): Long {

    val minutesToAlertBefore = zmanCallback.howManyMinutesToAlertBefore(zmanNotification.zman.type)
    if (minutesToAlertBefore >= 0) {
      val msToAlertBefore = minutesToAlertBefore * MIN_TO_MS_CONVERSION

      val msToZmanNotification: Long

      if (msToAlertBefore > zmanNotification.msUntilZman) {
        msToZmanNotification = 0
      } else {
        msToZmanNotification = zmanNotification.msUntilZman - msToAlertBefore
      }

      return msToZmanNotification
    }

    return Long.MIN_VALUE
  }

  public companion object {

    public const val FIFTEEN_MINUTES: Int = 15

    public const val ONE_HOUR: Int = 60

    public const val MIN_TO_MS_CONVERSION: Int = 60 * 1000

    /** What's the longest time before a zman that we'll allow an alert? */
    public const val MAX_ALERT_TIME: Int = ONE_HOUR * MIN_TO_MS_CONVERSION

    /** Sentinel value to indicate the user doesn't want an alert for the zman. */
    public const val NO_ALERT: Int = -1

    /** If we wake up within a short time of needing to alert anyways, just do it. */
    private const val DEFAULT_ALERT_FUDGE_FACTOR = 30 * 1000
  }
} // End of ZmanNotificationScheduler class
