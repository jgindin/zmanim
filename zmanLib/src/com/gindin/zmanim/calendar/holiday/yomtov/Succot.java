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
public class Succot
	extends AbstractCachingTwoDayObservance {

	private static final String                               NAME = "Sukkot";


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.TISHREI == date.getHebrewMonth() ) {
			final int dayOfMonth = date.getHebrewDayOfMonth();

			if ( 14 == dayOfMonth ) {
				return getErevDetails();
			}

			if ( 15 == dayOfMonth ) {
				return getYomDetails();
			}

			// TODO: Add a flag to check if we're in Israel or not. If yes, then we have to change the YomDetails
			// TODO:  (they will say Havdalah) and ignore the 16th...
			if ( 16 == dayOfMonth ) {
				return getYom2Details();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new YomDetails( 1 );
	}


	@Override
	protected Details createYom2Details() {
		return new YomDetails( 2 );
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

	private static final class CholHaMoedDetails
		implements Details {

		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return false;  //To change body of implemented methods use File | Settings | File Templates.
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return null;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}

}  // End of Succot class
