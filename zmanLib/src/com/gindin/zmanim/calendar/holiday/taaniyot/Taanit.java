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
	@HebrewMonth.Constant
	private final int                                         monthOfFast;

	/** Absent any reason to postpone the fast, what day of the month does it fall on? */
	private final int                                         normalDayOfFast;


	/** Can the fast take place on a Friday? */
	protected abstract boolean canFallOnFriday();

	protected abstract Details getDetailsForErevTaanit();

	protected abstract Details getDetailsForTaanit();


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
		if ( date.isJewishLeapYear() && ( HebrewMonth.ADAR == monthOfFast ) ) {
			actualMonthOfFast = HebrewMonth.ADAR_II;
		}

		if ( actualMonthOfFast != date.getJewishMonth() ) {
			return null;
		}

		final int dayOfWeek = date.getDayOfWeek();
		final int jewishDayOfMonth = date.getJewishDayOfMonth();

		if ( Calendar.WEDNESDAY == dayOfWeek ) {

			if ( canFallOnFriday() ) {
				if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
					return getDetailsForErevTaanit();
				}
			}
			else {
				if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
					return getDetailsForErevTaanit();
				}

				if ( ( normalDayOfFast - 3 ) == jewishDayOfMonth ) {
					// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
					return getDetailsForErevTaanit();
				}
			}
		}

		if ( Calendar.THURSDAY == dayOfWeek ) {

			if ( canFallOnFriday() ) {
				if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
					return getDetailsForErevTaanit();
				}
			}
			else {
				if ( ( normalDayOfFast -1 ) == jewishDayOfMonth ) {
					return getDetailsForTaanit();
				}

				if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
					// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
					return getDetailsForTaanit();
				}
			}
		}

		if ( ( Calendar.FRIDAY == dayOfWeek ) && canFallOnFriday() ) {
			if ( normalDayOfFast == jewishDayOfMonth ) {
				return getDetailsForTaanit();
			}
		}

		if ( Calendar.SATURDAY == dayOfWeek ) {
			if ( normalDayOfFast == jewishDayOfMonth ) {
				return getDetailsForErevTaanit();
			}
		}

		if ( Calendar.SUNDAY == dayOfWeek ) {
			if ( ( normalDayOfFast + 1 ) == jewishDayOfMonth ) {
				return getDetailsForTaanit();
			}
		}


		// We've handled all the special cases above...
		if ( normalDayOfFast == jewishDayOfMonth ) {
			return getDetailsForTaanit();
		}

		if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
			return getDetailsForErevTaanit();
		}


		return null;
	}


}  // End of Taanit class
