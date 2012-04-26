/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingTwoDayObservance;

/**
 *
 */
public class Shavuot
	extends AbstractCachingTwoDayObservance {

	private static final String                               SHAVUOT = "Shavuot";

	@Override
	public String getName() {
		return SHAVUOT;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.SIVAN == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 5 == hebrewDayOfMonth ) {
				return getErevDetails();
			}

			if ( 6 == hebrewDayOfMonth ) {
				return getYomDetails();
			}

			if ( 7 == hebrewDayOfMonth ) {
				return getYom2Details();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails( SHAVUOT );
	}


	@Override
	protected Details createYomDetails() {
		return new YomDetails( SHAVUOT, 1 );
	}


	@Override
	protected Details createYom2Details() {
		return new YomDetails( SHAVUOT, 2 );
	}


}  // End of Shavuot class
