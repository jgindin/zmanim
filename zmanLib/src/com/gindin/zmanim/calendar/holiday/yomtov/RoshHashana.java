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
@SuppressWarnings( "MagicNumber" )
public class RoshHashana
	extends AbstractCachingTwoDayObservance {

	private static final String                               NAME = "Rosh Hashana";



	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewMonth = date.getHebrewMonth();
		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( ( HebrewMonth.ELUL == hebrewMonth ) && ( 29 == hebrewDayOfMonth ) ) {
			return getErevDetails();
		}
		else if ( HebrewMonth.TISHREI == hebrewMonth ) {
			if ( 1 == hebrewDayOfMonth ) {
				return getYomDetails();
			}
			else if ( 2 == hebrewDayOfMonth ) {
				return getYom2Details();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails( NAME );
	}


	@Override
	protected Details createYomDetails() {
		return new RoshHashanaYomDetails( 1 );
	}


	@Override
	protected Details createYom2Details() {
		return new RoshHashanaYomDetails( 2 );
	}


	private static final class RoshHashanaYomDetails
		extends YomDetails {

		private RoshHashanaYomDetails(
			int day
		) {
			super( NAME, day );
		}


		@Override
		public boolean sayHavdalah() {
			// Israel or not...it's always a two day holiday...
			return ( 2 == day );
		}
	}

}  // End of RoshHashana class
