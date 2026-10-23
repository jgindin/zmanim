/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val NAME = "Lag B'Omer"

/**  */
public class LagBOmer : Observance() {

  override fun getName(): String {
    return NAME
  }

  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR != date.hebrewMonth) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      17 -> Details.Erev(date, NAME, false)
      18 ->
        object : Details.Yom(date, NAME) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      else -> null
    }
  }
} // End of LagBOmer class
