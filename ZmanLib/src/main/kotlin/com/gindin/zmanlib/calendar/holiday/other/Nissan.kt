package com.gindin.zmanlib.calendar.holiday.other

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.calendar.HebrewMonth
import com.gindin.zmanlib.calendar.holiday.Details
import com.gindin.zmanlib.calendar.holiday.Observance

/** We don't say tachanun for the entire month of Nissan. */
public class Nissan : Observance() {

  override fun getName(): String {
    return "Nissan"
  }

  override fun getDetailsFor(date: HebrewDate): Details? {

    val hebrewMonth = date.hebrewMonth
    if (HebrewMonth.NISSAN == hebrewMonth) {

      return NissanDetails(date)
    }

    return null
  }

  private class NissanDetails(date: HebrewDate) :
    // There is no real description for this day.
    Details.Yom(date, "") {

    override fun sayTachanun(): Boolean {
      return false
    }
  }
} // End of Nissan class
