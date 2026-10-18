/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

/**
 *
 */
@Suppress("MagicNumber")
public class Taanit17Tamuz : Taanit(HebrewMonth.TAMMUZ, 17) {

  override fun getName(): String {
    return TAANIT_17_TAMUZ
  }


  override fun canFallOnFriday(date: HebrewDate): Boolean {
    return false
  }


  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, TAANIT_17_TAMUZ)
  }


  override fun createYomDetails(date: HebrewDate): Details {
    return TaanitDetails(date, TAANIT_17_TAMUZ)
  }


  private companion object {

    private const val TAANIT_17_TAMUZ: String = TAANIT + "17 Tamuz"
  }

}  // End of Taanit17Tamuz class
