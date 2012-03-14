/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
public class TishaBAv
	extends Taanit {

	private static final String                               TISHA_B_AV = "Tisha B'Av";

	private static final ErevTishaBAvDetails                  EREV_TISHA_B_AV_DETAILS = new ErevTishaBAvDetails();
	private static final TishaBAvDetails                      TISHA_B_AV_DETAILS = new TishaBAvDetails();


	public TishaBAv() {
		super( HebrewMonth.AV, 9 );
	}


	@Override
	public String getName() {
		return TISHA_B_AV;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details getDetailsForErevTaanit() {
		return EREV_TISHA_B_AV_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return TISHA_B_AV_DETAILS;
	}


	private static class ErevTishaBAvDetails
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
			return "Erev " + TISHA_B_AV;
		}
	}

	private static class TishaBAvDetails
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
			return TISHA_B_AV;
		}
	}

}  // End of TishaBAv class
