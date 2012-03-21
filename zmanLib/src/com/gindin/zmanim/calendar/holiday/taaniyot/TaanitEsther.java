/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 * The Fast is observed on the 13th day of the Hebrew month of Adar. If the date of the Fast of Esther falls on
 *  Shabbat (Saturday), the fast is instead observed on the preceding Thursday.
 *    -- http://en.wikipedia.org/wiki/Fast_of_Esther
 */
@SuppressWarnings( "MagicNumber" )
public class TaanitEsther
	extends Taanit {

	private static final String                               ESTHER = TAANIT + "Esther";



	public TaanitEsther() {
		super( HebrewMonth.ADAR, 13 );
	}


	@Override
	public String getName() {
		return ESTHER;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevTaanitEstherDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new TaanitEstherDetails();
	}


	private static class ErevTaanitEstherDetails
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
			return "Erev " + ESTHER;
		}
	}


	private static class TaanitEstherDetails
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
			return ESTHER;
		}
	}

}  // End of TaanitEsther class
