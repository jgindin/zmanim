/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val TU_B_SHVAT = "Tu B'Shvat"

/**  */
public class TuBShvat : Observance() {

  override fun getName(): String {
    return TU_B_SHVAT
  }

  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (date.hebrewMonth != HebrewMonth.SHEVAT) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      14 -> Details.Erev(date, TU_B_SHVAT, false)
      15 ->
        object : Details.Yom(date, TU_B_SHVAT) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      else -> null
    }
  }
} // End of TuBShvat class
