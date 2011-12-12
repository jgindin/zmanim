/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.icu4j_calendar.holiday;

import com.gindin.zmanim.icu4j_calendar.HebrewCalendar;

/**
 * A multi-day holiday.
 *
 * For those observances where we want a different description based on which day of the observance it is.
 *  E.g., "Hanukkah 3", or "Omer 22".
 */
public abstract class MultiDayHoliday
	extends JewishDay {


	private final int                                       length;



	MultiDayHoliday(
		int             month,
		int             date,
		int             length,
		String          name,
		HebrewCalendar  forDate
	) {

		super( month, date, name, forDate );
		this.length = length;
	}


	@Override
	public String getDescription() {
		String description = super.getDescription();

		if ( null == forDate ) {
			return description;
		}

		int dayOf = getDayOf();
		if ( dayOf > 0 ) {
			return description + " " + getDayOf();
		}

		// Should not have been able to get here...
		return description;
	}


	@Override
	public JewishDay isOn( HebrewCalendar dateToTest ) {

		JewishDay day = super.isOn( dateToTest );
		if ( null == day ) {

			for ( int i = 1; i < length; i++ ) {

				calendar.add( HebrewCalendar.DATE, 1 );
				if ( isSameDate( dateToTest, calendar ) ) {
					day = createForDate( dateToTest );
				}
			}
		}

		return day;
	}


	@Override
	public MultiDayHoliday isMultiDay() {

		// By definition!
		return this;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	public int getDayOf() {

		if ( null == forDate ) {
			assert false;
			return -1;
		}

		calendar.set( forDate.get( HebrewCalendar.YEAR ), month, date );
		if ( isSameDate( forDate, calendar ) ) {
			return ( 1 + getCountOffset() );
		}

		for ( int i = 1; i < length; i++ ) {

			calendar.add( HebrewCalendar.DATE, 1 );
			if ( isSameDate( forDate, calendar ) ) {
				return ( i + 1 + getCountOffset() );
			}
		}

		// Better not get here!
		assert false : forDate.toString();
		return -1;
	}


	protected int getCountOffset() {
		return 0;
	}
}
