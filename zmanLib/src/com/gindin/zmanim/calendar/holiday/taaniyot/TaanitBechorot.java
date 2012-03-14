/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
public class TaanitBechorot
	extends Taanit {

	private static final String                               BECHOROT = TAANIT + "Bechorot";

	private static final ErevTaanitBechorotDetails            EREV_TAANIT_BECHOROT_DETAILS = new ErevTaanitBechorotDetails();
	private static final TaanitBechorotDetails                TAANIT_BECHOROT_DETAILS = new TaanitBechorotDetails();


	public TaanitBechorot() {
		super( HebrewMonth.NISSAN, 14 );
	}


	@Override
	public String getName() {
		return BECHOROT;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details getDetailsForErevTaanit() {
		return EREV_TAANIT_BECHOROT_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return TAANIT_BECHOROT_DETAILS;
	}


	private static class ErevTaanitBechorotDetails
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
			return "Erev " + BECHOROT;
		}
	}

	private static class TaanitBechorotDetails
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
			return BECHOROT;
		}
	}

}  // End of TaanitBechorot class
