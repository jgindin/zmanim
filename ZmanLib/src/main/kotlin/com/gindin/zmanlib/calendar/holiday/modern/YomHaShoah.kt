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
   * The 27th of Nissan is never a Saturday, so the only shifts are:
   * - Friday the 27th: Moved back to Thursday the 26th
   * - Sunday the 27th: Moved forward to Monday the 28th
   *
   * Those are the days on which the regular observance does not fall out any more, because it was
   * moved to another day. On a Friday it is the 27th itself; on a Saturday it is the 26th that is
   * pushed forward.
   */
  private fun isMovedElsewhere(date: HebrewDate): Boolean {
    return when (date.dayOfWeek) {
      Calendar.FRIDAY -> 27 == date.hebrewDayOfMonth
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
