/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib

import com.gindin.zmanlib.calendar.HebrewCalendar
import com.gindin.zmanlib.location.ZmanimLocation
import com.gindin.zmanlib.notification.ZmanNotificationScheduler
import com.gindin.zmanlib.zman.Zman
import com.gindin.zmanlib.zman.Zmanim
import java.util.Calendar

/**
 * Allows access to info about a given zman.
 */
public abstract class ZmanCallback {

  /**
   * Used in conjunction with #findNextZman to allow control over which of the various zmanim for the given
   *  type is preferred by the user.
   *
   * @return For the given type, what is the name of the zman the user prefers to use? May be null to indicate
   *  the default is to be used.
   *  E.g,. for the Zmanim.Type.ShabbatEnd the user may prefer "Seattle Sephardic".
   */
  public abstract fun getPreferredZman(type: Zmanim.Type): String?


  /**
   * @return For the given type, how far in advance (in minutes) does the user want to be notified? May be less than
   *  zero to indicate no notification.
   */
  public abstract fun howManyMinutesToAlertBefore(type: Zmanim.Type): Int

  /**
   * @return true if the user wants any notifications; false if they've been turned off.
   */
  public abstract fun shouldShowAnyNotifications(): Boolean

  /**
   * @return true if the user has indicated they want notifications even on Shabbat & Yom Tov.
   */
  public abstract fun shouldNotifyOnShabbat(): Boolean


  /**
   * @return a handle to the tone to be used when notifying of an upcoming zman. Use null to indicate the
   *  default tone should be used.
   */
  public abstract fun getNotificationTone(): String?


  /**
   * Check if a notification should actually be displayed right now.
   */
  public fun shouldActuallyNotify(
    location: ZmanimLocation,
    now: Calendar
  ): Boolean {

    if (!shouldShowAnyNotifications()) {
      return false
    }

    if (shouldNotifyOnShabbat()) {
      return true
    }

    now.timeZone = location.timeZone
    val today = HebrewCalendar.getForDate(now)

    val todayDetails = today.getDetails()
    val tomorrow = today.tomorrow()
    val tomorrowDetails = tomorrow.getDetails()

    // If today is erev Shabbat, and it's after candle lighting, then don't notify...
    val todayIsAfterCandleLighting = today.isAfter(now, Zmanim.Type.CandleLighting, location)
    if (tomorrowDetails.isShabbat() && todayIsAfterCandleLighting) {
      return false
    }

    // If today is erev Yom Tov, and it's after candle lighting, then don't notify...
    if (tomorrowDetails.isYomTov() && todayIsAfterCandleLighting) {
      return false
    }

    // If today is Shabbat, but it's after sunset, AND tomorrow's not a Yom Tov, then go ahead...
    val todayIsAfterShabbatEnd = today.isAfter(now, Zmanim.Type.ShabbatEnd, location)
    if (todayDetails.isShabbat() && todayIsAfterShabbatEnd && !tomorrowDetails.isYomTov()) {
      return true
    }

    // If today is YomTov, it's after sunset, and tomorrow's not a Yom Tov, then go ahead...
    if (todayDetails.isYomTov() && todayIsAfterShabbatEnd && !tomorrowDetails.isYomTov()) {
      return true
    }

    return !todayDetails.isShabbat() && !todayDetails.isYomTov()
  }


  public fun actuallyNotifyReasoning(
    location: ZmanimLocation,
    now: Calendar
  ): String {

    if (!shouldShowAnyNotifications()) {
      return "NO: Notifications are disabled"
    }

    if (shouldNotifyOnShabbat()) {
      return "YES: Notifying even Shabbat"
    }

    now.timeZone = location.timeZone
    val today = HebrewCalendar.getForDate(now)

    val todayDetails = today.getDetails()
    val tomorrow = today.tomorrow()
    val tomorrowDetails = tomorrow.getDetails()

    // If today is erev Shabbat, and it's after candle lighting, then don't notify...
    val todayIsAfterCandleLighting = today.isAfter(now, Zmanim.Type.CandleLighting, location)
    if (tomorrowDetails.isShabbat() && todayIsAfterCandleLighting) {
      return "NO: It's erev Shabbat and after candle lighting."
    }

    // If today is erev Yom Tov, and it's after candle lighting, then don't notify...
    if (tomorrowDetails.isYomTov() && todayIsAfterCandleLighting) {
      return "NO: It's erev Yom Tov and after candle lighting."
    }

    // If today is Shabbat, but it's after sunset, AND tomorrow's not a Yom Tov, then go ahead...
    val todayIsAfterShabbatEnd = today.isAfter(now, Zmanim.Type.ShabbatEnd, location)
    if (todayDetails.isShabbat() && todayIsAfterShabbatEnd && !tomorrowDetails.isYomTov()) {
      return "YES: Shabbat is over, and there's nothing tomorrow."
    }

    // If today is YomTov, it's after sunset, and tomorrow's not a Yom Tov, then go ahead...
    if (todayDetails.isYomTov() && todayIsAfterShabbatEnd && !tomorrowDetails.isYomTov()) {
      return "YES: Yom Tov is over, and there's nothing tomorrow."
    }

    if (!todayDetails.isShabbat() && !todayDetails.isYomTov()) {
      return "YES: There's nothing today."
    }

    return "NO: It must be Shabbat or a Yom Tov"
  }


  /**
   * Has the user been notified of the specified zman for the specified date?
   *
   * @param zmanTime The time of the zman
   * @param type The type of zman to check for
   *
   * @return true if notification has happened; false otherwise.
   */
  public abstract fun hasBeenNotified(
    zmanTime: Calendar,
    type: Zmanim.Type
  ): Boolean

  /**
   * Indicate the handling of the specified zman has been completed.
   */
  public abstract fun completed(
    now: Calendar,
    type: Zmanim.Type
  )


  /**
   * Schedule an alert for the specified zman.
   * @param zman Which zman is the alert being scheduled for?
   * @param location The current location (as determined when the zmanim were updated).
   * @param alertAt The date & time (with the correct time zone, based on the location) for when the
   */
  public abstract fun scheduleAlert(
    zman: Zman,
    location: ZmanimLocation,
    alertAt: Calendar
  )


  /**
   * @return true if the user wants to see the zman times formatted with seconds. If false, an
   *  appropriate rounding algorithm will be used.
   */
  public abstract fun formatTimeWithSeconds(): Boolean


  /**
   * Log some information. Should not do anything unless debugging...
   */
  public open fun log(
    where: String,
    message: String,
    error: Throwable?,
    vararg moreInfo: Pair<String, String>?
  ) {
  }


  internal open class Adapter : ZmanCallback() {

    override fun getPreferredZman(type: Zmanim.Type): String? {
      return null
    }


    override fun howManyMinutesToAlertBefore(type: Zmanim.Type): Int {
      return ZmanNotificationScheduler.NO_ALERT
    }


    override fun shouldShowAnyNotifications(): Boolean {
      return true
    }


    override fun shouldNotifyOnShabbat(): Boolean {
      return true
    }


    override fun getNotificationTone(): String? {
      return null
    }


    override fun hasBeenNotified(
      zmanTime: Calendar,
      type: Zmanim.Type
    ): Boolean {
      return false
    }


    override fun completed(
      now: Calendar,
      type: Zmanim.Type
    ) {
    }


    override fun scheduleAlert(
      zman: Zman,
      location: ZmanimLocation,
      alertAt: Calendar
    ) {
    }


    override fun formatTimeWithSeconds(): Boolean {
      return false
    }

  }
}
