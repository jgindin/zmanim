/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val SHAVUOT = "Shavuot"

/**
 *
 */
public class Shavuot : Observance() {

  override fun getName(): String {
    return SHAVUOT
  }


  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.SIVAN == date.hebrewMonth) {
      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (5 == hebrewDayOfMonth) {
        return Details.ErevYomTov(date, SHAVUOT)
      }

      if (6 == hebrewDayOfMonth) {
        return ShavuotDetails(date, true)
      }

      if (7 == hebrewDayOfMonth) {
        return ShavuotDetails(date, false)
      }
    }

    return null
  }


  private class ShavuotDetails(
    date: HebrewDate,
    lightCandles: Boolean
  ) : Details.YomTovDetails(date, SHAVUOT, lightCandles) {

    override fun isShavuot(): Boolean {
      return true
    }
  }

}  // End of Shavuot class
