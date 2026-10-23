/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.other

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val OMER = "Omer"

/**  */
public class Omer : Observance() {

  override fun getName(): String {
    return OMER
  }

  @Suppress("MagicNumber")
  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    val hebrewDayOfMonth = date.hebrewDayOfMonth

    if (HebrewMonth.NISSAN == hebrewMonth) {
      if (hebrewDayOfMonth >= 16) {
        return OmerDetails(date, hebrewDayOfMonth - 15)
      }
    } else if (HebrewMonth.IYAR == hebrewMonth) {
      return OmerDetails(date, 15 + hebrewDayOfMonth)
    } else if (HebrewMonth.SIVAN == hebrewMonth) {
      if (hebrewDayOfMonth < 6) {
        return OmerDetails(date, 15 + HebrewMonth.SHORT_MONTH_LENGTH + hebrewDayOfMonth)
      }
    }

    return null
  }

  private class OmerDetails(date: HebrewDate, dayOfOmer: Int) : Details.Yom(date, "$OMER $dayOfOmer") {

    override fun isOmer(): Boolean {
      return true
    }
  }
} // End of Omer class
