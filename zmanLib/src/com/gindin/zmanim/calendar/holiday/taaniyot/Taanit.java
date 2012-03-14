/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

import java.util.Calendar;

/**
 *
 */
abstract class Taanit
	implements Observance {

	protected static final String                             TAANIT = "Ta'anit ";

	/** Which month does the fast fall in? */
	private int                                               monthOfFast;

	/** Absent any reason to postpone the fast, what day of the month does it fall on? */
	private int                                               normalDayOfFast;


	/** Can the fast take place on a Friday? */
	protected abstract boolean canFallOnFriday();

	protected abstract Details getDetailsForErevTaanit();

	protected abstract Details getDetailsForTaanit();


	protected Taanit(
		int   monthOfFast,
		int   normalDayOfFast
	) {
		this.monthOfFast = monthOfFast;
		this.normalDayOfFast = normalDayOfFast;
	}


	@Override
	public final Details getDetailsFor( HebrewDate date ) {

		int actualMonthOfFast = monthOfFast;
		if ( date.isJewishLeapYear() && ( HebrewMonth.ADAR == monthOfFast ) ) {
			actualMonthOfFast = HebrewMonth.ADAR_II;
		}

		if ( actualMonthOfFast != date.getJewishMonth() ) {
			return null;
		}

		int actualDayOfFast = normalDayOfFast;

		// Cannot fall out on Shabbat! If this Shabbat, then move it to Sunday.
		if ( Calendar.SATURDAY == date.getDayOfWeek() ) {
			actualDayOfFast++;
		}
		else if ( ( Calendar.FRIDAY == date.getDayOfWeek() ) && !canFallOnFriday() ) {
			actualDayOfFast--;
		}

		if ( actualDayOfFast == date.getJewishDayOfMonth() ) {
			return getDetailsForTaanit();
		}

		if ( ( actualDayOfFast - 1 ) == date.getJewishDayOfMonth() ) {
			return getDetailsForErevTaanit();
		}

		return null;
	}


}  // End of Taanit class
