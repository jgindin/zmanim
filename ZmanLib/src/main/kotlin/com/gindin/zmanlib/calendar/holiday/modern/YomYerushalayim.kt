/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.modern

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val NAME = "Yom Yerushalyim"

/**
 * An Israeli national holiday commemorating the reunification of Jerusalem and the establishment of Israeli control
 *  over the Old City in June 1967. The Chief Rabbinate of Israel declared Jerusalem Day a minor religious holiday to
 *  thank God for victory in the Six-Day War and for answering the 2,000-year-old prayer of "Next Year in Jerusalem".
 *
 * On May 12, 1968, the government proclaimed a new holiday—Jerusalem Day—to be celebrated on the 28th of Iyar,
 *  the Hebrew date on which the divided city of Jerusalem became one.
 *    -- http://en.wikipedia.org/wiki/Yom_Yerushalayim
 */
@Suppress("FeatureEnvy", "MagicNumber")
public class YomYerushalayim : Observance() {

  override fun getName(): String {
    return NAME
  }


  override fun getDetailsFor(date: HebrewDate): Details? {

    if (HebrewMonth.IYAR != date.hebrewMonth) {
      return null
    }

    if (27 == date.hebrewDayOfMonth) {
      return Details.Erev(date, NAME, false)
    }

    if (28 == date.hebrewDayOfMonth) {
      return object : Details.Yom(date, NAME) {
        override fun sayTachanun(): Boolean {
          return false
        }
      }
    }

    return null
  }


}  // End of YomYerushalayim class
