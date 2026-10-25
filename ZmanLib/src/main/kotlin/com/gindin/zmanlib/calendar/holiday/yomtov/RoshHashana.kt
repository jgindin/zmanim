/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val ROSH_HASHANA = "Rosh Hashana"

/**  */
@Suppress("MagicNumber")
public class RoshHashana : Observance() {

  override fun getName(): String {
    return ROSH_HASHANA
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    val hebrewDayOfMonth = date.hebrewDayOfMonth

    return when {
      (HebrewMonth.ELUL == hebrewMonth) && (29 == hebrewDayOfMonth) ->
        object : Details.Erev(date, ROSH_HASHANA) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      (HebrewMonth.TISHREI == hebrewMonth) && (1 == hebrewDayOfMonth) ->
        RoshHashanaYomDetails(date, true)
      (HebrewMonth.TISHREI == hebrewMonth) && (2 == hebrewDayOfMonth) ->
        RoshHashanaYomDetails(date, false)
      else -> null
    }
  }

  private class RoshHashanaYomDetails(
    date: HebrewDate,
    lightCandles: Boolean,
  ) : Details.YomTovDetails(date, ROSH_HASHANA, lightCandles) {

    override fun isRoshHashana(): Boolean {
      return true
    }
  }
} // End of RoshHashana class
