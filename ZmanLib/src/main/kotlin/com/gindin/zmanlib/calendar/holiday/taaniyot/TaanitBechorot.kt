/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details

/**  */
@Suppress("MagicNumber")
public class TaanitBechorot : Taanit(HebrewMonth.NISSAN, 14) {

  override fun getName(): String {
    return BECHOROT
  }

  override fun canFallOnFriday(date: HebrewDate): Boolean {

    // This one is complicated. In a year when Pesach starts Friday night, then this fast WILL
    //  be observed on Friday (the 14th of Nisan). However, if Pesach starts Saturday night, then
    //  it gets pulled all the way back to Thursday (the 12th of Nisan).
    //
    // We return true here ONLY if today IS the 14th, and handle the exceptions in the
    //  checkWednesday & checkThursday methods.
    //
    // NOTE: This means that if this method is ever called to check a date which is NOT ITSELF
    //  the normal date of the fast, we'll return false. What this really means is that if the
    //  base class' checkWednesday or checkThursday were ever to be called, they would NOT work
    //  correctly.
    return date.hebrewDayOfMonth == 14
  }

  override fun checkWednesday(date: HebrewDate): Details? {

    if (date.hebrewDayOfMonth == 11) {
      return createErevDetails(date)
    }

    // DO NOT CALL SUPER. See note in canFallOnFriday.
    return null
  }

  override fun checkThursday(date: HebrewDate): Details? {

    if (date.hebrewDayOfMonth == 12) {
      return createYomDetails(date)
    }

    if (date.hebrewDayOfMonth == 13) {
      return createErevDetails(date)
    }

    // DO NOT CALL SUPER. See note in canFallOnFriday.
    return null
  }

  override fun createErevDetails(date: HebrewDate): Details {
    return ErevTaanitDetails(date, BECHOROT)
  }

  override fun createYomDetails(date: HebrewDate): Details {
    return TaanitDetails(date, BECHOROT)
  }

  override fun canPushToSunday(): Boolean {
    return false
  }

  private companion object {

    private const val BECHOROT: String = TAANIT + "Bechorot"
  }
} // End of TaanitBechorot class
