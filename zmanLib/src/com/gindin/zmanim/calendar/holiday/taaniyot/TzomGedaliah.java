/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
public class TzomGedaliah
	extends Taanit {

	private static final String                               TZOM_GEDALIAH = "Tzom Gedaliah";



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
	protected Details createErevDetails() {
		return new ErevTzomGedaliahDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new TzomGedaliahDetails();
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
			return EREV + TZOM_GEDALIAH;
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
