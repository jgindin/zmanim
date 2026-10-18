/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val NAME = "Lag B'Omer"

/**
 *
 */
public class LagBOmer : Observance() {

  override fun getName(): String {
    return NAME
  }


  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR == date.hebrewMonth) {
      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (17 == hebrewDayOfMonth) {
        return Details.Erev(date, NAME, false)
      } else if (18 == hebrewDayOfMonth) {
        return object : Details.Yom(date, NAME) {
          override fun sayTachanun(): Boolean {
            return false
          }
        }
      }
    }

    return null
  }

}  // End of LagBOmer class
