/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val CHANUKAH = "Chanukah"

/**  */
@Suppress("MagicNumber", "FeatureEnvy")
public class Chanukah : Observance() {

  override fun getName(): String {
    return CHANUKAH
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewDayOfMonth = date.hebrewDayOfMonth

    // Chanukah starts on 25 Kislev, so it has run for 5 or 6 days when Tevet begins (Kislev has 29 or 30 days),
    //  and it ends on 3 or 2 Tevet.
    val lastNightsOfKislev = if (date.isKislevShort()) 5 else 6
    val lastDayOfChanukahInTevet = if (date.isKislevShort()) 3 else 2

    return when (date.hebrewMonth) {
      HebrewMonth.KISLEV ->
        when {
          24 == hebrewDayOfMonth -> Details.Erev(date, CHANUKAH, false)
          hebrewDayOfMonth >= 25 -> ChanukahDetails(date, (hebrewDayOfMonth + 1) - 25)
          else -> null
        }
      HebrewMonth.TEVET ->
        if (hebrewDayOfMonth <= lastDayOfChanukahInTevet) {
          ChanukahDetails(date, hebrewDayOfMonth + lastNightsOfKislev)
        } else {
          null
        }
      else -> null
    }
  }

  private class ChanukahDetails(date: HebrewDate, day: Int) : Details.Yom(date, "$CHANUKAH $day") {

    override fun isChanukah(): Boolean {
      return true
    }

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of Chanukah class
