/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val PURIM = "Purim"

/**
 * Purim (and Shushan Purim) fall in Adar, or in Adar II of a leap year. Hidden from Java, since an
 * internal top-level function is otherwise compiled to a public static method.
 */
@JvmSynthetic
internal fun HebrewDate.isInPurimMonth(): Boolean {
  return hebrewMonth == if (isHebrewLeapYear()) HebrewMonth.ADAR_II else HebrewMonth.ADAR
}

/**  */
@Suppress("MagicNumber", "FeatureEnvy")
public class Purim : Observance() {

  override fun getName(): String {
    return PURIM
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (!date.isInPurimMonth()) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      13 -> Details.Erev(date, PURIM, false)
      14 -> PurimDetails(date)
      else -> null
    }
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
