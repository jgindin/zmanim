package com.gindin.zmanlib.calendar.holiday.other

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

/**
 * We don't say tachanun on the 1st thru 12th of Sivan.
 */
public class Sivan : Observance() {

  override fun getName(): String {
    return "First 12 days of Sivan"
  }


  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    if (HebrewMonth.SIVAN == hebrewMonth) {

      val hebrewDayOfMonth = date.hebrewDayOfMonth
      if (hebrewDayOfMonth <= 12) {
        return SivanDetails(date)
      }
    }

    return null
  }



  private class SivanDetails(date: HebrewDate) :
    // There is no real description for this day.
    Details.Yom(date, "") {

    override fun sayTachanun(): Boolean {
      return false
    }
  }


} // End of Sivan class
