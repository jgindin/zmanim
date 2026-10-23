/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

private const val ASARA_BTEVET = "Asara B'Tevet"

/**  */
public class AsaraBTevet : Taanit(HebrewMonth.TEVET, 10) {

  override fun getName(): String {
    return ASARA_BTEVET
  }

  override fun canFallOnFriday(date: HebrewDate): Boolean {
    return true
  }

  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, ASARA_BTEVET)
  }

  override fun createYomDetails(date: HebrewDate): Details {
    return TaanitDetails(date, ASARA_BTEVET)
  }
} // End of AsaraBTevet class
