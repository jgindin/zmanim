/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.modern

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance
import java.util.Calendar

private const val NAME = "Yom HaZikaron"

private const val IYAR_2 = 2
private const val IYAR_3 = 3
private const val IYAR_4 = 4
private const val IYAR_5 = 5

/**
 * Yom Hazikaron is observed on the 4th day of the month of Iyar of the Hebrew calendar, always one
 * day before Israel Independence Day on the 5th day of Iyar, the anniversary of the Proclamation of
 * the State of Israel in 1948. Both holidays are observed one or two days earlier (the 3rd and 4th,
 * or the 2nd and 3rd, of Iyar) when the 5th occurs on a Friday or Saturday, the Shabbat. Similarly,
 * both days are moved one day later when Yom Hazikaron falls on Sunday. --
 * http://en.wikipedia.org/wiki/Yom_Hazikaron
 *
 * This means:
 * - If Yom HaAtzmaut falls on Friday, it gets pushed back to Thursday the 4th, and Yom HaZikaron
 *   goes to Wednesday the 3rd
 * - If Yom HaAtzmaut falls on Shabbat, it gets pushed back to Thursday the 3rd, and Yom HaZikaron
 *   goes to Wednesday the 2nd (2008)
 * - If Yom HaAtzmaut falls on Monday, it gets pushed out to Tuesday the 6th, and Yom HaZikaron goes
 *   to Monday the 5th (2011)
 */
@Suppress("FeatureEnvy")
public class YomHaZikaron : Observance() {

  override fun getName(): String {
    return NAME
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR != date.hebrewMonth || isMovedElsewhere(date)) {
      return null
    }

    return getDetailsForShiftedDay(date) ?: getDetailsForRegularDay(date)
  }

  /**
   * In order to prevent Yom HaZikaron or Yom HaAtzmaut from falling on or immediately after
   * Shabbat, if the 4th of Iyar falls on:
   * - Thursday: Moved back to the 3rd
   * - Friday: Moved back to the 2nd
   * - Sunday: Moved forward to the 5th
   *
   * See http://en.wikipedia.org/wiki/Yom_Hazikaron
   *
   * Those are the days on which the regular observance does not fall out any more, because it was
   * moved to another day.
   */
  private fun isMovedElsewhere(date: HebrewDate): Boolean {
    val jewishDayOfMonth = date.hebrewDayOfMonth

    return when (date.dayOfWeek) {
      // On a Thursday, Wednesday was the 2nd (or 3rd), and that's when Yom HaZikaron should fall
      // out for this year.
      Calendar.THURSDAY -> (IYAR_3 == jewishDayOfMonth) || (IYAR_4 == jewishDayOfMonth)
      // A Friday also gets Saturday's treatment of the 3rd (pushing it forward), and the 4th was
      // pushed back to the
      // 2nd.
      Calendar.FRIDAY -> (IYAR_3 == jewishDayOfMonth) || (IYAR_4 == jewishDayOfMonth)
      // This is a case where we're pushing it forward...
      Calendar.SATURDAY -> IYAR_3 == jewishDayOfMonth
      else -> false
    }
  }

  private fun getDetailsForShiftedDay(date: HebrewDate): Details? {
    val jewishDayOfMonth = date.hebrewDayOfMonth

    return when (date.dayOfWeek) {
      Calendar.SUNDAY -> if (IYAR_4 == jewishDayOfMonth) createErevDetails(date) else null
      Calendar.MONDAY -> if (IYAR_5 == jewishDayOfMonth) createYomDetails(date) else null
      Calendar.TUESDAY ->
        if ((1 == jewishDayOfMonth) || (IYAR_2 == jewishDayOfMonth)) createErevDetails(date)
        else null
      Calendar.WEDNESDAY ->
        if ((IYAR_2 == jewishDayOfMonth) || (IYAR_3 == jewishDayOfMonth)) createYomDetails(date)
        else null
      else -> null
    }
  }

  /** The "regular" year. */
  private fun getDetailsForRegularDay(date: HebrewDate): Details? {
    return when (date.hebrewDayOfMonth) {
      IYAR_3 -> createErevDetails(date)
      IYAR_4 -> createYomDetails(date)
      else -> null
    }
  }

  private fun createErevDetails(date: HebrewDate): Details {
    return Details.Erev(date, NAME, false)
  }

  private fun createYomDetails(date: HebrewDate): Details {
    return object : Details.Yom(date, NAME) {
      override fun sayTachanun(): Boolean {
        return false
      }
    }
  }
} // End of YomHaZikaron class
