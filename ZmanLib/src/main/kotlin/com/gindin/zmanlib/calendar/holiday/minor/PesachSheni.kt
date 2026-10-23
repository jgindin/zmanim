/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val PESACH_SHENI = "Pesach Sheni"

/**  */
public class PesachSheni : Observance() {

  override fun getName(): String {
    return PESACH_SHENI
  }

  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR != date.hebrewMonth) {
      return null
    }

    return when (date.hebrewDayOfMonth) {
      13 -> Details.Erev(date, PESACH_SHENI, false)
      14 ->
        object : Details.Yom(date, PESACH_SHENI) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      else -> null
    }
  }
} // End of PesachSheni class
