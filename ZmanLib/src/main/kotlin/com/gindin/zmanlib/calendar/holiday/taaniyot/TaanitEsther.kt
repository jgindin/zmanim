/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

/**
 * The Fast is observed on the 13th day of the Hebrew month of Adar. If the date of the Fast of Esther falls on
 *  Shabbat (Saturday), the fast is instead observed on the preceding Thursday.
 *    -- http://en.wikipedia.org/wiki/Fast_of_Esther
 */
@Suppress("MagicNumber")
public class TaanitEsther : Taanit(HebrewMonth.ADAR, 13) {

  override fun getName(): String {
    return ESTHER
  }


  override fun canFallOnFriday(date: HebrewDate): Boolean {
    return false
  }


  override fun canPushToSunday(): Boolean {
    return false
  }


  override fun getAdjustedMonthOfFast(date: HebrewDate): Int {
    if (date.isHebrewLeapYear()) {
      return HebrewMonth.ADAR_II
    }

    return HebrewMonth.ADAR
  }


  override fun checkWednesday(
    date: HebrewDate
  ): Details? {

    val details = super.checkWednesday(date)

    if (null == details) {

      val jewishDayOfMonth = date.hebrewDayOfMonth
      if ((normalDayOfFast - 3) == jewishDayOfMonth) {
        // Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
        return createErevDetails(date)
      }

    }

    return null
  }


  override fun checkThursday(
    date: HebrewDate
  ): Details? {

    val details = super.checkThursday(date)
    if (null == details) {

      val jewishDayOfMonth = date.hebrewDayOfMonth
      if ((normalDayOfFast - 2) == jewishDayOfMonth) {
        // Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
        return createYomDetails(date)
      }
    }

    return null
  }


  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, ESTHER)
  }


  override fun createYomDetails(date: HebrewDate): Details {
    return TaanitDetails(date, ESTHER)
  }


  private companion object {

    private const val ESTHER: String = TAANIT + "Esther"
  }

}  // End of TaanitEsther class
