/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar
import java.util.Date

/** Describes a single time. Intended to be reused as the date changes, without having to be recreated. */
public abstract class Zman
internal constructor(
  @JvmField public val name: String,

  /** Describes this zman. E.g., "Latest Shema according to the GR'A". */
  @JvmField public val description: String,

  /** What type of zman is this? */
  @JvmField public val type: Zmanim.Type,

  /** Advanced zmanim should not be shown unless the user has explicitly indicated they want to see them. */
  @JvmField public val advanced: Boolean,
) {

  /** Keep the calculation cached. */
  private var cachedTime: Calendar? = null

  internal constructor(
    name: String,
    description: String,
    type: Zmanim.Type,
  ) : this(name, description, type, true)

  /**
   * Obtain the time of the desired zman. Why a Calendar? Because there is no 'Time' class, and the Date class will
   * simply use the default time zone, which means the time zone actually being used gets ignored.
   *
   * @return The time for the requested zman. May return null in some cases where it is not possible to calculate the
   *   zman time. (For example, when you're far enough North or South, there are days where the sun does not set, and so
   *   sunrise / sunset cannot be calculated.)
   */
  public fun getTime(): Calendar? {

    synchronized(this) {
      if (null == cachedTime) {
        cachedTime = calculate()
      }
    }

    return cachedTime
  }

  final override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other == null || javaClass != other.javaClass) {
      return false
    }

    val zman = other as Zman

    if (name != zman.name) {
      return false
    }
    if (type != zman.type) {
      return false
    }

    return true
  }

  final override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + type.hashCode()
    return result
  }

  /**
   * @return Subclass must implement to return the actual time of the zman. May return null in some cases where it is
   *   not possible to calculate the zman time. (For example, when you're far enough North or South, there are days
   *   where the sun does not set, and so sunrise / sunset cannot be calculated.)
   */
  protected abstract fun calculate(): Calendar?

  /**
   * This method is the most convenient way to bridge the Date-based world of the KosherJava API and our need for
   * Calendar-based API.
   *
   * @return a calendar representing the date & time of this zman; May be null if the zman cannot be calculated. See
   *   detailed explanation on top of the [com.kosherjava.zmanim.AstronomicalCalendar] as to the particular cases when
   *   sunrise, sunset and twilight cannot be calculated.
   */
  protected fun toCalendar(
    zmanimCalculator: ZmanimCalculator,
    zmanDate: Date?,
  ): Calendar? {

    if (zmanDate == null) {
      return null
    }

    val calendar = Calendar.getInstance()
    calendar.timeZone = zmanimCalculator.geoLocation.timeZone
    calendar.time = zmanDate

    return calendar
  }
}
