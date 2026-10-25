/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.modern

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance
import java.util.Calendar

private const val NAME = "Yom HaShoah"

/**  */
@Suppress("MagicNumber")
public class YomHaShoah : Observance() {

  override fun getName(): String {
    return NAME
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.NISSAN != date.hebrewMonth || isMovedElsewhere(date)) {
      return null
    }

    return getDetailsForShiftedDay(date) ?: getDetailsForRegularDay(date)
  }

  /**
   * http://www.hebcal.com/holidays/yom-hashoah says: "It is held on the 27th of Nisan (April/May),
   * unless the 27th would be adjacent to Shabbat, in which case the date is shifted by a day."
   *
   * On a Friday or Saturday the 26th is where we push it forward.
   */
  private fun isMovedElsewhere(date: HebrewDate): Boolean {
    return when (date.dayOfWeek) {
      Calendar.FRIDAY,
      Calendar.SATURDAY -> 26 == date.hebrewDayOfMonth
      else -> false
    }
  }

  private fun getDetailsForShiftedDay(date: HebrewDate): Details? {
    val jewishDayOfMonth = date.hebrewDayOfMonth

    return when (date.dayOfWeek) {
      Calendar.SUNDAY -> if (27 == jewishDayOfMonth) Details.Erev(date, NAME, false) else null
      Calendar.MONDAY -> if (28 == jewishDayOfMonth) Details.Yom(date, NAME) else null
      Calendar.WEDNESDAY -> if (25 == jewishDayOfMonth) Details.Erev(date, NAME, false) else null
      Calendar.THURSDAY -> if (26 == jewishDayOfMonth) Details.Yom(date, NAME) else null
      else -> null
    }
  }

  /** The "regular" year. */
  private fun getDetailsForRegularDay(date: HebrewDate): Details? {
    return when (date.hebrewDayOfMonth) {
      26 -> Details.Erev(date, NAME, false)
      27 -> Details.Yom(date, NAME)
      else -> null
    }
  }
} // End of YomHaShoah class
