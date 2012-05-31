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
public class LagBOmer
	extends Observance {

	private static final String                               NAME = "Lag B'Omer";


	@Override
	public String getName() {
		return NAME;
	}


	@SuppressWarnings( "MagicNumber" )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
			int hebrewDayOfMonth = date.getHebrewDayOfMonth();
			if ( 17 == hebrewDayOfMonth ) {
				return new ErevLagBOmerDetails( date );
			}
			else if ( 18 == hebrewDayOfMonth ) {
				return new LagBOmerDetails( date );
			}
		}

		return null;
	}


	private static final class ErevLagBOmerDetails
		extends Details {

		private ErevLagBOmerDetails( HebrewDate date ) {
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
			return EREV + NAME;
		}
	}



	private static final class LagBOmerDetails
		extends Details {

		private LagBOmerDetails( HebrewDate date ) {
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
			return NAME;
		}
	}

}  // End of LagBOmer class
