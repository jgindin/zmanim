package com.gindin.zmanlib.calendar

import org.intellij.lang.annotations.MagicConstant
import java.util.Calendar

/**
 * List of constants for use with java.util.Calendar to identify the day of the week.
 */
@MagicConstant(
  intValues = [
    Calendar.SUNDAY.toLong(), Calendar.MONDAY.toLong(), Calendar.TUESDAY.toLong(), Calendar.WEDNESDAY.toLong(),
    Calendar.THURSDAY.toLong(), Calendar.FRIDAY.toLong(), Calendar.SATURDAY.toLong()
  ]
)
@Retention(AnnotationRetention.BINARY)
public annotation class CalendarDay
