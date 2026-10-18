/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val CHANUKAH = "Chanukah"

/**
 *
 */
@Suppress("MagicNumber", "FeatureEnvy")
public class Chanukah : Observance() {

  override fun getName(): String {
    return CHANUKAH
  }


  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewDayOfMonth = date.hebrewDayOfMonth

    if (HebrewMonth.KISLEV == date.hebrewMonth) {
      if (24 == hebrewDayOfMonth) {
        return Details.Erev(date, CHANUKAH, false)
      } else if (hebrewDayOfMonth >= 25) {
        return ChanukahDetails(date, (hebrewDayOfMonth + 1) - 25)
      }
    } else if (HebrewMonth.TEVET == date.hebrewMonth) {
      if (date.isKislevShort()) {
        if (hebrewDayOfMonth <= 3) {
          return ChanukahDetails(date, hebrewDayOfMonth + 5)
        }
      } else {
        if (hebrewDayOfMonth <= 2) {
          return ChanukahDetails(date, hebrewDayOfMonth + 6)
        }
      }
    }

    return null
  }


  private class ChanukahDetails(date: HebrewDate, day: Int) : Details.Yom(date, "$CHANUKAH $day") {

    override fun isChanukah(): Boolean {
      return true
    }


    override fun sayTachanun(): Boolean {
      return false
    }
  }


}  // End of Chanukah class
