/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.formatters

import com.gindin.zmanlib.calendar.HebrewDate
import java.util.Calendar

/** Knows how to format a HebrewDate as a secular date. */
public abstract class SecularDateFormatter {

  public enum class Style {
    DEFAULT,
    SHORT,
    MEDIUM,
    LONG,
  }

  public abstract fun format(date: HebrewDate): String

  private class DefaultFormatter : SecularDateFormatter() {

    override fun format(date: HebrewDate): String {

      val formattedDate = StringBuilder()
      when (date.dayOfWeek) {
        Calendar.SUNDAY -> formattedDate.append("Sun")

        Calendar.MONDAY -> formattedDate.append("Mon")

        Calendar.TUESDAY -> formattedDate.append("Tue")

        Calendar.WEDNESDAY -> formattedDate.append("Wed")

        Calendar.THURSDAY -> formattedDate.append("Thur")

        Calendar.FRIDAY -> formattedDate.append("Fri")

        Calendar.SATURDAY -> formattedDate.append("Shabbat")
      }

      formattedDate.append(", ")

      when (date.gregorianMonth) {
        Calendar.JANUARY -> formattedDate.append("Jan ")

        Calendar.FEBRUARY -> formattedDate.append("Feb ")

        Calendar.MARCH -> formattedDate.append("Mar ")

        Calendar.APRIL -> formattedDate.append("Apr ")

        Calendar.MAY -> formattedDate.append("May ")

        Calendar.JUNE -> formattedDate.append("Jun ")

        Calendar.JULY -> formattedDate.append("Jul ")

        Calendar.AUGUST -> formattedDate.append("Aug ")

        Calendar.SEPTEMBER -> formattedDate.append("Sep ")

        Calendar.OCTOBER -> formattedDate.append("Oct ")

        Calendar.NOVEMBER -> formattedDate.append("Nov ")

        Calendar.DECEMBER -> formattedDate.append("Dec ")
      }

      formattedDate.append(date.gregorianDayOfMonth).append(", ").append(date.gregorianYear)

      return formattedDate.toString()
    }
  }

  private open class ShortFormatter : SecularDateFormatter() {

    override fun format(date: HebrewDate): String {

      return String.format(
        "%2s/%2s/%2s",
        date.gregorianMonth + 1,
        date.gregorianDayOfMonth,
        date.gregorianYear,
      )
    }
  }

  private class MediumFormatter : ShortFormatter() {

    override fun format(date: HebrewDate): String {

      val formattedDate = StringBuilder(super.format(date))

      when (date.dayOfWeek) {
        Calendar.SUNDAY -> formattedDate.append(" S")

        Calendar.MONDAY -> formattedDate.append(" M")

        Calendar.TUESDAY -> formattedDate.append(" T")

        Calendar.WEDNESDAY -> formattedDate.append(" W")

        Calendar.THURSDAY -> formattedDate.append(" R")

        Calendar.FRIDAY -> formattedDate.append(" F")

        Calendar.SATURDAY -> formattedDate.append(" B")
      }

      return formattedDate.toString()
    }
  }

  public companion object {

    private var FORMATTER: SecularDateFormatter? = null

    private var MEDIUM_FORMATTER: SecularDateFormatter? = null

    private var SHORT_FORMATTER: SecularDateFormatter? = null

    @JvmStatic
    public fun getInstance(formatterStyle: Style): SecularDateFormatter {

      when (formatterStyle) {
        Style.SHORT -> {
          var shortFormatter = SHORT_FORMATTER
          if (shortFormatter == null) {
            shortFormatter = ShortFormatter()
            SHORT_FORMATTER = shortFormatter
          }
          return shortFormatter
        }

        Style.MEDIUM -> {
          var mediumFormatter = MEDIUM_FORMATTER
          if (mediumFormatter == null) {
            mediumFormatter = MediumFormatter()
            MEDIUM_FORMATTER = mediumFormatter
          }
          return mediumFormatter
        }

        Style.DEFAULT,
        Style.LONG -> {
          var defaultFormatter = FORMATTER
          if (null == defaultFormatter) {
            defaultFormatter = DefaultFormatter()
            FORMATTER = defaultFormatter
          }
          return defaultFormatter
        }
      }
    }
  }
} // End of SecularDateFormatter class
