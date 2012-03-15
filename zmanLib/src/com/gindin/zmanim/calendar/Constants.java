/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.intellij.lang.annotations.MagicConstant;

import java.util.Calendar;

/**
 *
 */
public interface Constants {

	@MagicConstant( intValues = { Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER })
	@interface CalendarMonth {
	}

	@MagicConstant( intValues = { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY })
	@interface CalendarDay {
	}

} // End of Constants interface
