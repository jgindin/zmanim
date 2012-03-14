/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
public class AsaraBTevet
	extends Taanit {

	private static final String                               ASARA_BTEVET = "Asara B'Tevet";

	private static final ErevAsaraBTevetDetails               EREV_ASARA_B_TEVET_DETAILS = new ErevAsaraBTevetDetails();
	private static final AsaraBTevetDetails                   ASARA_B_TEVET_DETAILS = new AsaraBTevetDetails();


	public AsaraBTevet() {
		super( HebrewMonth.TEVET, 10 );
	}


	@Override
	public String getName() {
		return ASARA_BTEVET;
	}


	@Override
	protected boolean canFallOnFriday() {
		return true;
	}


	@Override
	protected Details getDetailsForErevTaanit() {
		return EREV_ASARA_B_TEVET_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return ASARA_B_TEVET_DETAILS;
	}


	private static class ErevAsaraBTevetDetails
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
			return "Erev " + ASARA_BTEVET;
		}
	}


	private static class AsaraBTevetDetails
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
			return ASARA_BTEVET;
		}
	}

}  // End of AsaraBTevet class
