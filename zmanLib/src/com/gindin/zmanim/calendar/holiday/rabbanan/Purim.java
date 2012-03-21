/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.rabbanan;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class Purim
	extends AbstractCachingObservance {


	@Override
	public String getName() {
		return "Purim";
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR_II == date.getHebrewMonth() ) ) ||
			   ( !date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) ) {

			if ( 13 == date.getHebrewDayOfMonth() ) {
				return getErevDetails();
			}
			if ( 14 == date.getHebrewDayOfMonth() ) {
				return getYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevPurimDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new PurimDetails();
	}


	private static class ErevPurimDetails
		implements Details {

		@Override public boolean isErev() {
			return true;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return "Erev Purim";
		}
	}

	private static class PurimDetails
		implements Details {

		@Override public boolean isErev() {
			return false;
		}

		@Override public boolean lightCandles() {
			return false;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return "Purim";
		}
	}

}  // End of Purim class
