/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.formatters

import com.gindin.zmanlib.calendar.HebrewDate
import com.gindin.zmanlib.location.ZmanimLocation

/**
 * Knows how to provide various formats of a HebrewDate.
 *
 * TODO: Refactor to allow for locale-based formatters?
 */
public abstract class HebrewDateFormatter {

  public enum class Style {
    DEFAULT,
    SHORT,
    MEDIUM,
    LONG
  }


  public abstract fun format(
    date: HebrewDate,
    location: ZmanimLocation?
  ): String


  /**
   * Formats a Hebrew date in a short fashion, which includes only the date. E.g., "1 Nissan 5772".
   */
  private open class ShortFormatter : HebrewDateFormatter() {

    override fun format(
      date: HebrewDate,
      location: ZmanimLocation?
    ): String {

      val buf = StringBuilder()

      var dateToFormat = date
      if ((location != null) && dateToFormat.isToday() && dateToFormat.isAfterSunset(location)) {
        dateToFormat = dateToFormat.tomorrow()
        buf.append("Or le ")
      }

      buf
        .append(dateToFormat.hebrewDayOfMonth)
        .append(" ")

        // subtract one because the hebrew month is stored as a one-based value...
        .append(HEBREW_MONTH_NAMES[dateToFormat.hebrewMonth - 1])
        .append(" ")
        .append(dateToFormat.hebrewYear)

      return buf.toString()
    }


  }  // End of ShortFormatter class


  /**
   * Builds on the format presented by the short format and adds information about the day. E.g., if it is a
   *  holiday, etc...
   */
  private class LongFormatter : ShortFormatter() {

    override fun format(
      date: HebrewDate,
      location: ZmanimLocation?
    ): String {

      var buf = super.format(date, location)
      buf += date.getDescription()

      return buf
    }
  }  // End of LongFormatter class


  public companion object {

    private val HEBREW_MONTH_NAMES: Array<String> = arrayOf(
      "Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul",
      "Tishrei", "Cheshvan", "Kislev", "Tevet", "Shevat", "Adar", "Adar II"
    )


    private var SHORT_FORMATTER: HebrewDateFormatter? = null
    private var LONG_FORMATTER: HebrewDateFormatter? = null


    @JvmStatic
    public fun getInstance(formatterStyle: Style): HebrewDateFormatter {

      when (formatterStyle) {
        Style.SHORT -> {
          var shortFormatter = SHORT_FORMATTER
          if (null == shortFormatter) {
            shortFormatter = ShortFormatter()
            SHORT_FORMATTER = shortFormatter
          }
          return shortFormatter
        }

        Style.DEFAULT,
        Style.MEDIUM,
        Style.LONG -> {
          var longFormatter = LONG_FORMATTER
          if (null == longFormatter) {
            longFormatter = LongFormatter()
            LONG_FORMATTER = longFormatter
          }
          return longFormatter
        }
      }
    }
  }

}  // End of HebrewDateFormatter class
