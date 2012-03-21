/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class RoshHashana
	implements Observance {

	private static final String                               NAME = "Rosh Hashana";

	private static Details                                    erevDetails;
	private static Details                                    day1Details;
	private static Details                                    day2Details;



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
				return getDay1Details();
			}
			else if ( 2 == hebrewDayOfMonth ) {
				return getDay2Details();
			}
		}

		return null;
	}


	private Details getErevDetails() {
		if ( null == erevDetails ) {
			erevDetails = new ErevDetails();
		}

		return erevDetails;
	}


	private Details getDay1Details() {
		if ( null == day1Details ) {
			day1Details = new YomDetails( 1 );
		}

		return day1Details;
	}


	private Details getDay2Details() {
		if ( null == day2Details ) {
			day2Details = new YomDetails( 2 );
		}

		return day2Details;
	}


	private static class ErevDetails
		implements Details {

		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return true;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return EREV + NAME;
		}
	}

	private static final class YomDetails
		implements Details {

		private final int                                       day;


		private YomDetails( int day ) {
			this.day = day;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return ( 1 == day );
		}


		@Override
		public boolean sayHavdalah() {
			return ( 2 == day );
		}


		@Override
		public String getDescription() {
			return NAME;
		}
	}

}  // End of RoshHashana class
