/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val TU_B_SHVAT = "Tu B'Shvat"

/**
 *
 */
public class TuBShvat : Observance() {

  override fun getName(): String {
    return TU_B_SHVAT
  }


  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (date.hebrewMonth == HebrewMonth.SHEVAT) {
      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (14 == hebrewDayOfMonth) {
        return Details.Erev(date, TU_B_SHVAT, false)
      }

      if (15 == hebrewDayOfMonth) {
        return object : Details.Yom(date, TU_B_SHVAT) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      }
    }

    return null
  }


}  // End of TuBShvat class
