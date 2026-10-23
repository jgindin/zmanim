/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday

import com.gindin.zmanlib.calendar.HebrewDate
import java.util.Calendar

private const val SHABBAT = "Shabbat"

/** Represents Shabbat. */
public class Shabbat : Observance() {

  override fun getName(): String {
    return SHABBAT
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    if (Calendar.FRIDAY == date.dayOfWeek) {
      return ErevShabbatDetails(date)
    }

    if (Calendar.SATURDAY == date.dayOfWeek) {
      return ShabbatDetails(date)
    }

    return null
  }

  private class ShabbatDetails(date: HebrewDate) : Details.Yom(date, SHABBAT) {

    override fun lightCandles(): Boolean {
      return false
    }

    override fun sayHavdalah(): Boolean {
      return true
    }
  }

  private class ErevShabbatDetails(date: HebrewDate) : Details.Erev(date, SHABBAT, true) {

    override fun getDescription(): String {
      // No one wants to see "Erev Shabbat" for every Friday...
      return ""
    }
  }
} // End of Shabbat class
