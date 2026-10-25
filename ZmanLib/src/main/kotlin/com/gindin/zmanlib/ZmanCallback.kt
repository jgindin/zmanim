/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib

import com.gindin.zmanlib.calendar.HebrewCalendar
import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.location.ZmanimLocation
import com.gindin.zmanlib.notification.ZmanNotificationScheduler
import com.gindin.zmanlib.zman.Zman
import com.gindin.zmanlib.zman.Zmanim
import java.util.Calendar

/** Allows access to info about a given zman. */
public abstract class ZmanCallback {

  /**
   * Used in conjunction with #findNextZman to allow control over which of the various zmanim for
   * the given type is preferred by the user.
   *
   * @return For the given type, what is the name of the zman the user prefers to use? May be null
   *   to indicate the default is to be used. E.g,. for the Zmanim.Type.ShabbatEnd the user may
   *   prefer "Seattle Sephardic".
   */
  public abstract fun getPreferredZman(type: Zmanim.Type): String?

  /**
   * @return For the given type, how far in advance (in minutes) does the user want to be notified?
   *   May be less than zero to indicate no notification.
   */
  public abstract fun howManyMinutesToAlertBefore(type: Zmanim.Type): Int

  /** @return true if the user wants any notifications; false if they've been turned off. */
  public abstract fun shouldShowAnyNotifications(): Boolean

  /** @return true if the user has indicated they want notifications even on Shabbat & Yom Tov. */
  public abstract fun shouldNotifyOnShabbat(): Boolean

  /**
   * @return a handle to the tone to be used when notifying of an upcoming zman. Use null to
   *   indicate the default tone should be used.
   */
  public abstract fun getNotificationTone(): String?

  /** Check if a notification should actually be displayed right now. */
  public fun shouldActuallyNotify(
    location: ZmanimLocation,
    now: Calendar,
  ): Boolean {
    return decide(location, now).notify
  }

  public fun actuallyNotifyReasoning(
    location: ZmanimLocation,
    now: Calendar,
  ): String {
    return decide(location, now).reason
  }

  private fun decide(
    location: ZmanimLocation,
    now: Calendar,
  ): Decision {
    return when {
      !shouldShowAnyNotifications() -> Decision.DISABLED
      shouldNotifyOnShabbat() -> Decision.EVEN_ON_SHABBAT
      else -> decideForToday(location, now)
    }
  }

  private fun decideForToday(
    location: ZmanimLocation,
    now: Calendar,
  ): Decision {

    now.timeZone = location.timeZone
    val today = HebrewCalendar.getForDate(now)

    val todayDetails = today.getDetails()
    val tomorrowDetails = today.tomorrow().getDetails()

    return decideForErev(today, tomorrowDetails, now, location)
      ?: decideForAfterShabbatOrYomTov(today, todayDetails, tomorrowDetails, now, location)
      ?: if (!todayDetails.isShabbat() && !todayDetails.isYomTov()) Decision.NOTHING_TODAY
      else Decision.SHABBAT_OR_YOM_TOV
  }

  /**
   * If today is erev Shabbat or erev Yom Tov, and it's after candle lighting, then don't notify...
   */
  private fun decideForErev(
    today: HebrewDate,
    tomorrowDetails: Details,
    now: Calendar,
    location: ZmanimLocation,
  ): Decision? {

    if (!today.isAfter(now, Zmanim.Type.CandleLighting, location)) {
      return null
    }

    return when {
      tomorrowDetails.isShabbat() -> Decision.EREV_SHABBAT
      tomorrowDetails.isYomTov() -> Decision.EREV_YOM_TOV
      else -> null
    }
  }

  /**
   * If today is Shabbat or Yom Tov, but it's after sunset, AND tomorrow's not a Yom Tov, then go
   * ahead...
   */
  private fun decideForAfterShabbatOrYomTov(
    today: HebrewDate,
    todayDetails: Details,
    tomorrowDetails: Details,
    now: Calendar,
    location: ZmanimLocation,
  ): Decision? {

    if (tomorrowDetails.isYomTov() || !today.isAfter(now, Zmanim.Type.ShabbatEnd, location)) {
      return null
    }

    return when {
      todayDetails.isShabbat() -> Decision.SHABBAT_OVER
      todayDetails.isYomTov() -> Decision.YOM_TOV_OVER
      else -> null
    }
  }

  private enum class Decision(
    val notify: Boolean,
    val reason: String,
  ) {
    DISABLED(false, "NO: Notifications are disabled"),
    EVEN_ON_SHABBAT(true, "YES: Notifying even Shabbat"),
    EREV_SHABBAT(false, "NO: It's erev Shabbat and after candle lighting."),
    EREV_YOM_TOV(false, "NO: It's erev Yom Tov and after candle lighting."),
    SHABBAT_OVER(true, "YES: Shabbat is over, and there's nothing tomorrow."),
    YOM_TOV_OVER(true, "YES: Yom Tov is over, and there's nothing tomorrow."),
    NOTHING_TODAY(true, "YES: There's nothing today."),
    SHABBAT_OR_YOM_TOV(false, "NO: It must be Shabbat or a Yom Tov"),
  }

  /**
   * Has the user been notified of the specified zman for the specified date?
   *
   * @param zmanTime The time of the zman
   * @param type The type of zman to check for
   * @return true if notification has happened; false otherwise.
   */
  public abstract fun hasBeenNotified(
    zmanTime: Calendar,
    type: Zmanim.Type,
  ): Boolean

  /** Indicate the handling of the specified zman has been completed. */
  public abstract fun completed(
    now: Calendar,
    type: Zmanim.Type,
  )

  /**
   * Schedule an alert for the specified zman.
   *
   * @param zman Which zman is the alert being scheduled for?
   * @param location The current location (as determined when the zmanim were updated).
   * @param alertAt The date & time (with the correct time zone, based on the location) for when the
   */
  public abstract fun scheduleAlert(
    zman: Zman,
    location: ZmanimLocation,
    alertAt: Calendar,
  )

  /**
   * @return true if the user wants to see the zman times formatted with seconds. If false, an
   *   appropriate rounding algorithm will be used.
   */
  public abstract fun formatTimeWithSeconds(): Boolean

  /** Log some information. Should not do anything unless debugging... */
  public open fun log(
    where: String,
    message: String,
    error: Throwable?,
    vararg moreInfo: Pair<String, String>?,
  ) {}

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
      type: Zmanim.Type,
    ): Boolean {
      return false
    }

    override fun completed(
      now: Calendar,
      type: Zmanim.Type,
    ) {
      // Nothing to remember.
    }

    override fun scheduleAlert(
      zman: Zman,
      location: ZmanimLocation,
      alertAt: Calendar,
    ) {
      // Nothing to schedule.
    }

    override fun formatTimeWithSeconds(): Boolean {
      return false
    }
  }
}
