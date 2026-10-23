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

    if (HebrewMonth.NISSAN != date.hebrewMonth) {
      return null
    }

    // http://www.hebcal.com/holidays/yom-hashoah says:
    //  "It is held on the 27th of Nisan (April/May), unless the 27th would be adjacent to Shabbat, in which case
    //  the date is shifted by a day."
    val jewishDayOfMonth = date.hebrewDayOfMonth

    when (date.dayOfWeek) {
      Calendar.SUNDAY -> {
        if (27 == jewishDayOfMonth) {
          return Details.Erev(date, NAME, false)
        }
      }

      Calendar.MONDAY -> {
        if (28 == jewishDayOfMonth) {
          return Details.Yom(date, NAME)
        }
      }

      Calendar.TUESDAY -> {}

      Calendar.WEDNESDAY -> {
        if (25 == jewishDayOfMonth) {
          return Details.Erev(date, NAME, false)
        }
      }

      Calendar.THURSDAY -> {
        if (26 == jewishDayOfMonth) {
          return Details.Yom(date, NAME)
        }
      }

      Calendar.FRIDAY,
      Calendar.SATURDAY -> {
        if (26 == jewishDayOfMonth) {
          // This is where we push it forward.
          return null
        }
      }
    }

    // We've handled the special cases, so if we get here, we're just looking for the "regular" year...
    if (26 == jewishDayOfMonth) {
      return Details.Erev(date, NAME, false)
    } else if (27 == jewishDayOfMonth) {
      return Details.Yom(date, NAME)
    }

    return null
  }
} // End of YomHaShoah class
