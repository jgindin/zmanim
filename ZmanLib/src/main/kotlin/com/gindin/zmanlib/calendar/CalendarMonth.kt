package com.gindin.zmanlib.calendar;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Calendar;

/**
 * List of possible constants for use with java.util.Calendar to identify months.
 */
@MagicConstant( intValues = { Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER } )
public @interface CalendarMonth {
}
