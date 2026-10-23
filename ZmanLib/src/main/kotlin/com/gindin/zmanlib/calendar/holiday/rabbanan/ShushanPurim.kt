/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val SHUSHAN_PURIM = "Shushan Purim"

/**  */
@Suppress("MagicNumber", "FeatureEnvy")
public class ShushanPurim : Observance() {

  override fun getName(): String {
    return SHUSHAN_PURIM
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (
      (date.isHebrewLeapYear() && (HebrewMonth.ADAR_II == date.hebrewMonth)) ||
        (!date.isHebrewLeapYear() && (HebrewMonth.ADAR == date.hebrewMonth))
    ) {

      if (15 == date.hebrewDayOfMonth) {
        return object : Details.Yom(date, SHUSHAN_PURIM) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      }
    }

    return null
  }
} // End of ShushanPurim class
