/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservance;

import java.util.Calendar;

/**
 *
 */
abstract class Taanit
	extends AbstractCachingObservance {

	protected static final String                             TAANIT = "Ta'anit ";

	/** Which month does the fast fall in? */
	@HebrewMonth.Constant
	private final int                                         monthOfFast;

	/** Absent any reason to postpone the fast, what day of the month does it fall on? */
	private final int                                         normalDayOfFast;


	/** Can the fast take place on a Friday? */
	protected abstract boolean canFallOnFriday();



	protected Taanit(
		@HebrewMonth.Constant
		int   monthOfFast,
		int   normalDayOfFast
	) {
		this.monthOfFast = monthOfFast;
		this.normalDayOfFast = normalDayOfFast;
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	public final Details getDetailsFor( HebrewDate date ) {

		@HebrewMonth.Constant int actualMonthOfFast = monthOfFast;
		if ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR == monthOfFast ) ) {
			actualMonthOfFast = HebrewMonth.ADAR_II;
		}

		if ( actualMonthOfFast != date.getHebrewMonth() ) {
			return null;
		}

		final int dayOfWeek = date.getDayOfWeek();
		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		if ( Calendar.WEDNESDAY == dayOfWeek ) {

			if ( canFallOnFriday() ) {
				if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
					return getErevDetails();
				}
			}
			else {
				if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
					return getErevDetails();
				}

				if ( ( normalDayOfFast - 3 ) == jewishDayOfMonth ) {
					// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
					return getErevDetails();
				}
			}
		}

		if ( Calendar.THURSDAY == dayOfWeek ) {

			if ( canFallOnFriday() ) {
				if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
					return getErevDetails();
				}
			}
			else {
				if ( ( normalDayOfFast -1 ) == jewishDayOfMonth ) {
					return getYomDetails();
				}

				if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
					// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
					return getYomDetails();
				}
			}
		}

		if ( ( Calendar.FRIDAY == dayOfWeek ) && canFallOnFriday() ) {
			if ( normalDayOfFast == jewishDayOfMonth ) {
				return getYomDetails();
			}
		}

		if ( Calendar.SATURDAY == dayOfWeek ) {
			if ( normalDayOfFast == jewishDayOfMonth ) {
				return getErevDetails();
			}
		}

		if ( Calendar.SUNDAY == dayOfWeek ) {
			if ( ( normalDayOfFast + 1 ) == jewishDayOfMonth ) {
				return getYomDetails();
			}
		}


		// We've handled all the special cases above...
		if ( normalDayOfFast == jewishDayOfMonth ) {
			return getYomDetails();
		}

		if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
			return getErevDetails();
		}


		return null;
	}


}  // End of Taanit class
