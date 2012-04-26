/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservance;

/**
 *
 */
public class PesachSheni
	extends AbstractCachingObservance {

	private static final String                               PESACH_SHENI = "Pesach Sheni";


	@Override
	public String getName() {
		return PESACH_SHENI;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();

			if ( 13 == hebrewDayOfMonth ) {
				return getErevDetails();
			}

			if ( 14 == hebrewDayOfMonth ) {
				return getYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevPesachSheniDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new PesachSheniDetails();
	}


	private static final class ErevPesachSheniDetails
		implements Details {

		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return EREV + PESACH_SHENI;
		}
	}


	private static final class PesachSheniDetails
		implements Details {

		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return PESACH_SHENI;
		}
	}

}  // End of PesachSheni class
