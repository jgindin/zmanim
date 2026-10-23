package com.gindin.zmanlib.calendar.holiday.other

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

private const val FIRST_DAY = 11

/** We don't say tachanun from Erev Yom Kippur until the end of the month. */
public class Tishri : Observance() {

  override fun getName(): String {
    return "Tishri"
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    if (HebrewMonth.TISHREI == hebrewMonth) {

      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (hebrewDayOfMonth >= FIRST_DAY) {
        return TishriDetails(date)
      }
    }

    return null
  }

  private class TishriDetails(date: HebrewDate) :
    // There is no real description for this day.
    Details.Yom(date, "") {

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of Tishri class
