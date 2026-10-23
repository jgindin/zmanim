/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

private const val TISHA_B_AV = "Tisha B'Av"

/**  */
public class TishaBAv : Taanit(HebrewMonth.AV, 9) {

  override fun getName(): String {
    return TISHA_B_AV
  }

  override fun canFallOnFriday(date: HebrewDate): Boolean {
    return false
  }

  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, TISHA_B_AV)
  }

  override fun createYomDetails(date: HebrewDate): Details {
    return object : TaanitDetails(date, TISHA_B_AV) {
      override fun sayTachanun(): Boolean {
        return false
      }
    }
  }
} // End of TishaBAv class
