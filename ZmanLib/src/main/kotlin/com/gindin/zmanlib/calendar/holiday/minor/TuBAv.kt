/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val TU_BAV = "Tu B'Av"

/**  */
public class TuBAv : Observance() {

  override fun getName(): String {
    return TU_BAV
  }

  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (date.hebrewMonth != HebrewMonth.AV) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      14 -> Details.Erev(date, TU_BAV, false)
      15 -> TuBAvDetails(date)
      else -> null
    }
  }

  private class TuBAvDetails(date: HebrewDate) : Details.Yom(date, TU_BAV) {

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of TuBAv class
