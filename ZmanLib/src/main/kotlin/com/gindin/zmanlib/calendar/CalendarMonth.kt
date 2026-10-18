package com.gindin.zmanlib.calendar

import org.intellij.lang.annotations.MagicConstant
import java.util.Calendar

/**
 * List of possible constants for use with java.util.Calendar to identify months.
 */
@MagicConstant(
  intValues = [
    Calendar.JANUARY.toLong(), Calendar.FEBRUARY.toLong(), Calendar.MARCH.toLong(), Calendar.APRIL.toLong(),
    Calendar.MAY.toLong(), Calendar.JUNE.toLong(), Calendar.JULY.toLong(), Calendar.AUGUST.toLong(),
    Calendar.SEPTEMBER.toLong(), Calendar.OCTOBER.toLong(), Calendar.NOVEMBER.toLong(), Calendar.DECEMBER.toLong()
  ]
)
@Retention(AnnotationRetention.BINARY)
public annotation class CalendarMonth
