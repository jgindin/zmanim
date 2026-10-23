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

/**
 * Yom Ha'atzmaut is nominally celebrated on the 5th day of Iyar (ה' באייר) in the Hebrew calendar, the anniversary of
 * the day in which Israel independence was proclaimed, when David Ben Gurion publicly read the Israeli Declaration of
 * Independence. The corresponding Gregorian date was May 14, 1948.
 *
 * If the 5th of Iyar falls on a Friday or Saturday, the celebrations are moved up to the preceding Thursday. This rule
 * has been effective since 1951.
 *
 * If the 5th of Iyar is on a Monday, the festival is postponed to Tuesday. This rule has been effective since 2004, in
 * order to avoid having Yom Hazikaron or Yom Ha'atzmaut on (or following) a Shabbat.
 *
 * Since the only other day of the week that the 5th of Iyar can fall on is a Wednesday, it is actually now relatively
 * rare for the festival to be observed on the 5th Iyar itself. -- http://en.wikipedia.org/wiki/Yom_Ha%27atzmaut
 *
 * This means that if Yom HaAtzmaut falls on:
 * - Friday, it gets pushed back to Thursday the 4th, and Yom HaZikaron goes to Wednesday the 3rd (2012)
 * - Shabbat, it gets pushed back to Thursday the 3rd, and Yom HaZikaron goes to Wednesday the 2nd (2001, 2005, 2008)
 * - Monday, it gets pushed out to Tuesday the 6th, and Yom HaZikaron goes to Monday the 5th (2004, 2007, 2010, 2011)
 */
@Suppress("FeatureEnvy")
public class YomHaAtzmaut : Observance() {

  override fun getName(): String {
    return NAME
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR != date.hebrewMonth) {
      return null
    }

    // In order to prevent Yom HaZikaron or Yom HaAtzmaut from falling on or immediately after Shabbat,
    //  if the 5th of Iyar falls on:
    //    Friday:   Moved back to the 4th
    //    Shabbat:  Moved back to the 3rd
    //    Monday:   Moved forward to the 6th
    // See http://en.wikipedia.org/wiki/Yom_Ha-Atzma%27ut
    val jewishDayOfMonth = date.hebrewDayOfMonth

    // A Sunday also gets Monday's treatment of the 5th.
    when (date.dayOfWeek) {
      Calendar.SUNDAY -> {
        if (4 == jewishDayOfMonth) {
          // This is a case where we're pushing it forward...
          return null
        }

        if (5 == jewishDayOfMonth) {
          return createErevDetails(date)
        }
      }

      Calendar.MONDAY -> {
        if (5 == jewishDayOfMonth) {
          return createErevDetails(date)
        }
      }

      Calendar.TUESDAY -> {
        if (6 == jewishDayOfMonth) {
          return createYomDetails(date)
        }
      }

      Calendar.WEDNESDAY -> {
        if ((2 == jewishDayOfMonth) || (3 == jewishDayOfMonth)) {
          return createErevDetails(date)
        }
      }

      Calendar.THURSDAY -> {
        if ((3 == jewishDayOfMonth) || (4 == jewishDayOfMonth)) {
          return createYomDetails(date)
        }
      }

      Calendar.FRIDAY -> {
        if ((4 == jewishDayOfMonth) || (5 == jewishDayOfMonth)) {
          // In this case, we pushed it back to the Thursday the 4th...
          return null
        }
      }

      Calendar.SATURDAY -> {
        if (5 == jewishDayOfMonth) {
          // In this case, we pushed it back to the Thursday the 3rd...
          return null
        }
      }
    }

    // We've handled the special cases, so if we get here, we're dealing with a regular year...
    if (4 == jewishDayOfMonth) {
      return createErevDetails(date)
    } else if (5 == jewishDayOfMonth) {
      return createYomDetails(date)
    }

    return null
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
