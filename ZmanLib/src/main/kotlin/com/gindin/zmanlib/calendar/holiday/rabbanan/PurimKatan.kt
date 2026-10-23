/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val PURIM_KATAN = "Purim Katan"

/**  */
@Suppress("MagicNumber", "FeatureEnvy")
public class PurimKatan : Observance() {

  override fun getName(): String {
    return PURIM_KATAN
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (date.isHebrewLeapYear() && (HebrewMonth.ADAR == date.hebrewMonth)) {

      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (13 == hebrewDayOfMonth) {
        return Details.Erev(date, PURIM_KATAN, false)
      }
      if ((14 == hebrewDayOfMonth) || (15 == hebrewDayOfMonth)) {
        return PurimKatanDetails(date)
      }
    }

    return null
  }

  private class PurimKatanDetails(date: HebrewDate) : Details.Yom(date, PURIM_KATAN) {

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of PurimKatan class
