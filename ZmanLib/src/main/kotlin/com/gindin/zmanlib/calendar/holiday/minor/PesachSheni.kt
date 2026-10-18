/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val PESACH_SHENI = "Pesach Sheni"

/**
 *
 */
public class PesachSheni : Observance() {

  override fun getName(): String {
    return PESACH_SHENI
  }


  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR == date.hebrewMonth) {
      val hebrewDayOfMonth = date.hebrewDayOfMonth

      if (13 == hebrewDayOfMonth) {
        return Details.Erev(date, PESACH_SHENI, false)
      }

      if (14 == hebrewDayOfMonth) {
        return object : Details.Yom(date, PESACH_SHENI) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      }
    }

    return null
  }

}  // End of PesachSheni class
