/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

private const val TZOM_GEDALIAH = "Tzom Gedaliah"
private const val DAY_OF_FAST = 3

/**  */
public class TzomGedaliah : Taanit(HebrewMonth.TISHREI, DAY_OF_FAST) {

  override fun getName(): String {
    return TZOM_GEDALIAH
  }

  override fun canFallOnFriday(date: HebrewDate): Boolean {
    return false
  }

  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, TZOM_GEDALIAH)
  }

  override fun createYomDetails(date: HebrewDate): Details {
    return TaanitDetails(date, TZOM_GEDALIAH)
  }
} // End of TzomGedaliah class
