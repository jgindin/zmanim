/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.formatters

import com.gindin.zmanlib.calendar.HebrewDate
import java.util.Calendar

private val DAY_NAMES =
  mapOf(
    Calendar.SUNDAY to "Sun",
    Calendar.MONDAY to "Mon",
    Calendar.TUESDAY to "Tue",
    Calendar.WEDNESDAY to "Wed",
    Calendar.THURSDAY to "Thur",
    Calendar.FRIDAY to "Fri",
    Calendar.SATURDAY to "Shabbat",
  )

private val MONTH_NAMES =
  mapOf(
    Calendar.JANUARY to "Jan ",
    Calendar.FEBRUARY to "Feb ",
    Calendar.MARCH to "Mar ",
    Calendar.APRIL to "Apr ",
    Calendar.MAY to "May ",
    Calendar.JUNE to "Jun ",
    Calendar.JULY to "Jul ",
    Calendar.AUGUST to "Aug ",
    Calendar.SEPTEMBER to "Sep ",
    Calendar.OCTOBER to "Oct ",
    Calendar.NOVEMBER to "Nov ",
    Calendar.DECEMBER to "Dec ",
  )

private val DAY_INITIALS =
  mapOf(
    Calendar.SUNDAY to " S",
    Calendar.MONDAY to " M",
    Calendar.TUESDAY to " T",
    Calendar.WEDNESDAY to " W",
    Calendar.THURSDAY to " R",
    Calendar.FRIDAY to " F",
    Calendar.SATURDAY to " B",
  )

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
      DAY_NAMES[date.dayOfWeek]?.let { formattedDate.append(it) }

      formattedDate.append(", ")

      MONTH_NAMES[date.gregorianMonth]?.let { formattedDate.append(it) }

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

      DAY_INITIALS[date.dayOfWeek]?.let { formattedDate.append(it) }

      return formattedDate.toString()
    }
  }

  public companion object {

    private var FORMATTER: SecularDateFormatter? = null

    private var MEDIUM_FORMATTER: SecularDateFormatter? = null

    private var SHORT_FORMATTER: SecularDateFormatter? = null

    @JvmStatic
    public fun getInstance(formatterStyle: Style): SecularDateFormatter {

      return when (formatterStyle) {
        Style.SHORT -> SHORT_FORMATTER ?: ShortFormatter().also { SHORT_FORMATTER = it }
        Style.MEDIUM -> MEDIUM_FORMATTER ?: MediumFormatter().also { MEDIUM_FORMATTER = it }
        Style.DEFAULT,
        Style.LONG -> FORMATTER ?: DefaultFormatter().also { FORMATTER = it }
      }
    }
  }
} // End of SecularDateFormatter class
