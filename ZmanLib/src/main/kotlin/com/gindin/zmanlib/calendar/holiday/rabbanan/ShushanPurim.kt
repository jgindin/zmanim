/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
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

    if (!date.isInPurimMonth() || 15 != date.hebrewDayOfMonth) {
      return null
    }

    return object : Details.Yom(date, SHUSHAN_PURIM) {
      override fun sayTachanun(): Boolean {
        return false
      }
    }
  }
} // End of ShushanPurim class
