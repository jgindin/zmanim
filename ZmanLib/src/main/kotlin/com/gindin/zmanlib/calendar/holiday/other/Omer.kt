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

    val dayOfOmer =
      when {
        HebrewMonth.NISSAN == hebrewMonth && hebrewDayOfMonth >= 16 -> hebrewDayOfMonth - 15
        HebrewMonth.IYAR == hebrewMonth -> 15 + hebrewDayOfMonth
        HebrewMonth.SIVAN == hebrewMonth && hebrewDayOfMonth < 6 ->
          15 + HebrewMonth.SHORT_MONTH_LENGTH + hebrewDayOfMonth
        else -> return null
      }

    return OmerDetails(date, dayOfOmer)
  }

  private class OmerDetails(date: HebrewDate, dayOfOmer: Int) : Details.Yom(date, "$OMER $dayOfOmer") {

    override fun isOmer(): Boolean {
      return true
    }
  }
} // End of Omer class
