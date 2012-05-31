/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.minor;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
public class PesachSheni
	extends Observance {

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
				return new ErevPesachSheniDetails( date );
			}

			if ( 14 == hebrewDayOfMonth ) {
				return new PesachSheniDetails( date );
			}
		}

		return null;
	}


	private static final class ErevPesachSheniDetails
		extends Details {

		private ErevPesachSheniDetails( HebrewDate date ) {
			super( date );
		}


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
		extends Details {

		private PesachSheniDetails( HebrewDate date ) {
			super( date );
		}


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
