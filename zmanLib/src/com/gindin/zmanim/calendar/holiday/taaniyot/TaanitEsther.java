/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 * The Fast is observed on the 13th day of the Hebrew month of Adar. If the date of the Fast of Esther falls on
 *  Shabbat (Saturday), the fast is instead observed on the preceding Thursday.
 *    -- http://en.wikipedia.org/wiki/Fast_of_Esther
 */
public class TaanitEsther
	extends Taanit {

	private static final String                               ESTHER = TAANIT + "Esther";

	private static final ErevTaanitEstherDetails              EREV_TAANIT_ESTHER_DETAILS = new ErevTaanitEstherDetails();
	private static final TaanitEstherDetails                  TAANIT_ESTHER_DETAILS = new TaanitEstherDetails();


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
	protected Details getDetailsForErevTaanit() {
		return EREV_TAANIT_ESTHER_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return TAANIT_ESTHER_DETAILS;
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
