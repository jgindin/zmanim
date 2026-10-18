package com.gindin.zmanlib.calendar;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Calendar;

/**
 * List of constants for use with java.util.Calendar to identify the day of the week.
 */
@MagicConstant( intValues = { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY } )
public @interface CalendarDay {
}
