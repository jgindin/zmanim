/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

private const val TISHA_B_AV = "Tisha B'Av"
private const val DAY_OF_FAST = 9

/**  */
public class TishaBAv : Taanit(HebrewMonth.AV, DAY_OF_FAST) {

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
