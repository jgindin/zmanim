/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

import java.util.Calendar;

/**
 * Fast days:
 *  - Gedaliah
 *  - 10 Tevet
 *  - Esther
 *  - 14 Nissan (Bechorot -- firstborn)
 *  - 17 Tammuz
 *  - Tisha B'Av
 *
 * These fast days may move around on the calendar such that, none of them can fall on Shabbat,
 *  and none, except 10 Tevet, can fall on Friday.
 *<p>
 * Note: The fact that these fast days push off to Sunday is from http://www.jewishgen.org/infofiles/m_calint.htm
 */
public abstract class Taanit
	extends Observance {

	protected static final String                             TAANIT = "Ta'anit ";

	/** Which month does the fast fall in? */
	@HebrewMonth.Constant
	private final int                                         monthOfFast;

	/** Absent any reason to postpone the fast, what day of the month does it fall on? */
	protected final int                                       normalDayOfFast;


	protected Taanit(
		@HebrewMonth.Constant
		int   monthOfFast,
		int   normalDayOfFast
	) {
		this.monthOfFast = monthOfFast;
		this.normalDayOfFast = normalDayOfFast;
	}


	/** Can the fast take place on a Friday? */
	protected abstract boolean canFallOnFriday();

	protected abstract Details createErevDetails( HebrewDate date );

	protected abstract Details createYomDetails( HebrewDate date );


	/**
	 * Can the fast be pushed back to Sunday if it were to fall on Shabbat? Usually yes, but Ta'anit Esther gets
	 *  pulled back instead.
	 */
	protected boolean canPushToSunday() {
		return true;
	}

	/**
	 * Allows the subclass to change the month of the fast based on the specified date.
	 */
	@HebrewMonth.Constant
	protected int getAdjustedMonthOfFast( HebrewDate date ) {
		return monthOfFast;
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	public final Details getDetailsFor( HebrewDate date ) {

		@HebrewMonth.Constant
		int actualMonthOfFast = getAdjustedMonthOfFast( date );

		if ( actualMonthOfFast != date.getHebrewMonth() ) {
			return null;
		}

		final int dayOfWeek = date.getDayOfWeek();
		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		if ( Calendar.WEDNESDAY == dayOfWeek ) {
			Details details = checkWednesday( date );
			if ( null != details ) {
				return details;
			}
		}

		if ( Calendar.THURSDAY == dayOfWeek ) {
			Details details = checkThursday( date );
			if ( null != details ) {
				return details;
			}
		}

		if ( Calendar.FRIDAY == dayOfWeek ) {

			if ( canFallOnFriday() ) {
				if ( normalDayOfFast == jewishDayOfMonth ) {
					return createYomDetails( date );
				}
			}
			else {
				// If we can't fall on Friday, then for sure this cannot be the Yom, but it also cannot be
				//  Erev, because we never have a Ta'anit on Shabbat (well, except Yom Kippur, but that's
				//  a whole separate case)...
				return null;
			}
		}

		if ( Calendar.SATURDAY == dayOfWeek ) {
			if ( canPushToSunday() ) {
				if ( normalDayOfFast == jewishDayOfMonth ) {
					return createErevDetails( date );
				}
			}
			else {
				// If we can't push it, then today is nothing.
				return null;
			}
		}

		if ( Calendar.SUNDAY == dayOfWeek ) {
			if ( ( ( normalDayOfFast + 1 ) == jewishDayOfMonth ) && canPushToSunday() ) {
				return createYomDetails( date );
			}
		}


		// We've handled all the special cases above...
		if ( normalDayOfFast == jewishDayOfMonth ) {
			return createYomDetails( date );
		}

		if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
			return createErevDetails( date );
		}


		return null;
	}


	protected Details checkWednesday(
		HebrewDate date
	) {

		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		if ( canFallOnFriday() ) {
			if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
				return createErevDetails( date );
			}
		}
		else {
			if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
				return createErevDetails( date );
			}
		}

		return null;
	}


	protected Details checkThursday(
		HebrewDate date
	) {

		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		if ( canFallOnFriday() ) {
			if ( ( normalDayOfFast - 1 ) == jewishDayOfMonth ) {
				return createErevDetails( date );
			}
		}
		else {
			if ( ( normalDayOfFast -1 ) == jewishDayOfMonth ) {
				return createYomDetails( date );
			}
		}

		return null;
	}


	protected static class ErevTaanitDetails
		extends Details.Erev {

		ErevTaanitDetails(
			HebrewDate  date,
			String      name
		) {

			// Can never have a fast that falls on Shabbat (except Yom Kippur, but that's handled by a separate
			//  Details class)
			super( date, name, false );
		}


		@Override
		public boolean sayHavdalah() {

			// We can have a fast day that falls on Sunday, in which case we would say Havdalah on erev of the fast...
			return isShabbat();
		}
	}


	protected static class TaanitDetails
		extends Details.Yom {

		public TaanitDetails(
			HebrewDate  date,
			String      name
		) {
			super( date, name );
		}


		@Override
		public boolean lightCandles() {

			// At least one fast can fall on Erev Shabbat...
			return isErevShabbat();
		}


		@Override
		public boolean sayHavdalah() {
			// No fast requires havdalah.
			return false;
		}


		@Override
		public boolean isTaanit() {
			return true;
		}
	}

}  // End of Taanit class
