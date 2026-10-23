/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val PURIM = "Purim"

/**  */
@Suppress("MagicNumber", "FeatureEnvy")
public class Purim : Observance() {

  override fun getName(): String {
    return PURIM
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (
      (date.isHebrewLeapYear() && (HebrewMonth.ADAR_II == date.hebrewMonth)) ||
        (!date.isHebrewLeapYear() && (HebrewMonth.ADAR == date.hebrewMonth))
    ) {

      if (13 == date.hebrewDayOfMonth) {
        return Details.Erev(date, PURIM, false)
      }
      if (14 == date.hebrewDayOfMonth) {
        return PurimDetails(date)
      }
    }

    return null
  }

  private class PurimDetails(date: HebrewDate) : Details.Yom(date, PURIM) {

    override fun isPurim(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of Purim class
