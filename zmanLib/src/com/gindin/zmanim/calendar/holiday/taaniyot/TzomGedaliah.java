/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
public class TzomGedaliah
	extends Taanit {

	private static final String                               TZOM_GEDALIAH = "Tzom Gedaliah";

	private static final ErevTzomGedaliahDetails              EREV_TZOM_GEDALIAH_DETAILS = new ErevTzomGedaliahDetails();
	private static final TzomGedaliahDetails                  TZOM_GEDALIAH_DETAILS = new TzomGedaliahDetails();


	public TzomGedaliah() {
		super( HebrewMonth.TISHREI, 3 );
	}


	@Override
	public String getName() {
		return TZOM_GEDALIAH;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details getDetailsForErevTaanit() {
		return EREV_TZOM_GEDALIAH_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return TZOM_GEDALIAH_DETAILS;
	}


	private static class ErevTzomGedaliahDetails
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
			return "Erev " + TZOM_GEDALIAH;
		}
	}


	private static class TzomGedaliahDetails
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
			return TZOM_GEDALIAH;
		}
	}

}  // End of TzomGedaliah class
