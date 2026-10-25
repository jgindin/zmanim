/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.modern

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance
import java.util.Calendar

private const val NAME = "Yom HaAtzmaut"

private const val IYAR_2 = 2
private const val IYAR_3 = 3
private const val IYAR_4 = 4
private const val IYAR_5 = 5
private const val IYAR_6 = 6

/**
 * Yom Ha'atzmaut is nominally celebrated on the 5th day of Iyar (ה' באייר) in the Hebrew calendar,
 * the anniversary of the day in which Israel independence was proclaimed, when David Ben Gurion
 * publicly read the Israeli Declaration of Independence. The corresponding Gregorian date was May
 * 14, 1948.
 *
 * If the 5th of Iyar falls on a Friday or Saturday, the celebrations are moved up to the preceding
 * Thursday. This rule has been effective since 1951.
 *
 * If the 5th of Iyar is on a Monday, the festival is postponed to Tuesday. This rule has been
 * effective since 2004, in order to avoid having Yom Hazikaron or Yom Ha'atzmaut on (or following)
 * a Shabbat.
 *
 * Since the only other day of the week that the 5th of Iyar can fall on is a Wednesday, it is
 * actually now relatively rare for the festival to be observed on the 5th Iyar itself. --
 * http://en.wikipedia.org/wiki/Yom_Ha%27atzmaut
 *
 * This means that if Yom HaAtzmaut falls on:
 * - Friday, it gets pushed back to Thursday the 4th, and Yom HaZikaron goes to Wednesday the 3rd
 *   (2012)
 * - Shabbat, it gets pushed back to Thursday the 3rd, and Yom HaZikaron goes to Wednesday the 2nd
 *   (2001, 2005, 2008)
 * - Monday, it gets pushed out to Tuesday the 6th, and Yom HaZikaron goes to Monday the 5th (2004,
 *   2007, 2010, 2011)
 */
@Suppress("FeatureEnvy")
public class YomHaAtzmaut : Observance() {

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
   * Shabbat, if the 5th of Iyar falls on:
   * - Friday: Moved back to the 4th
   * - Shabbat: Moved back to the 3rd
   * - Monday: Moved forward to the 6th
   *
   * See http://en.wikipedia.org/wiki/Yom_Ha-Atzma%27ut
   *
   * Those are the days on which the regular observance does not fall out any more, because it was
   * moved to another day.
   */
  private fun isMovedElsewhere(date: HebrewDate): Boolean {
    val jewishDayOfMonth = date.hebrewDayOfMonth

    return when (date.dayOfWeek) {
      // A Sunday also gets Monday's treatment of the 5th, so the 4th is a case where we're pushing
      // it forward...
      Calendar.SUNDAY -> IYAR_4 == jewishDayOfMonth
      // In this case, we pushed it back to the Thursday the 4th (or, for the 5th, to the Thursday
      // the 3rd)...
      Calendar.FRIDAY -> (IYAR_4 == jewishDayOfMonth) || (IYAR_5 == jewishDayOfMonth)
      Calendar.SATURDAY -> IYAR_5 == jewishDayOfMonth
      else -> false
    }
  }

  private fun getDetailsForShiftedDay(date: HebrewDate): Details? {
    val jewishDayOfMonth = date.hebrewDayOfMonth

    return when (date.dayOfWeek) {
      Calendar.SUNDAY,
      Calendar.MONDAY -> if (IYAR_5 == jewishDayOfMonth) createErevDetails(date) else null
      Calendar.TUESDAY -> if (IYAR_6 == jewishDayOfMonth) createYomDetails(date) else null
      Calendar.WEDNESDAY ->
        if ((IYAR_2 == jewishDayOfMonth) || (IYAR_3 == jewishDayOfMonth)) createErevDetails(date)
        else null
      Calendar.THURSDAY ->
        if ((IYAR_3 == jewishDayOfMonth) || (IYAR_4 == jewishDayOfMonth)) createYomDetails(date)
        else null
      else -> null
    }
  }

  /** The "regular" year. */
  private fun getDetailsForRegularDay(date: HebrewDate): Details? {
    return when (date.hebrewDayOfMonth) {
      IYAR_4 -> createErevDetails(date)
      IYAR_5 -> createYomDetails(date)
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
} // End of YomHaAtzmaut class
