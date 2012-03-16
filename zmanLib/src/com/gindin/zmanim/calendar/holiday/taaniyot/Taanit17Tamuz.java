/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
public class Taanit17Tamuz
	extends Taanit {


	private static final String                               TAANIT_17_TAMUZ = TAANIT + "17 Tamuz";

	private static final ErevTaanit17TamuzDetails             EREV_TAANIT_17_TAMUZ_DETAILS = new ErevTaanit17TamuzDetails();
	private static final Taanit17TamuzDetails                 TAANIT_17_TAMUZ_DETAILS = new Taanit17TamuzDetails();


	public Taanit17Tamuz() {
		super( HebrewMonth.TAMMUZ, 17 );
	}


	@Override
	public String getName() {
		return TAANIT_17_TAMUZ;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details getDetailsForErevTaanit() {
		return EREV_TAANIT_17_TAMUZ_DETAILS;
	}


	@Override
	protected Details getDetailsForTaanit() {
		return TAANIT_17_TAMUZ_DETAILS;
	}


	private static class ErevTaanit17TamuzDetails
		implements Observance.Details {

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
			return "Erev " + TAANIT_17_TAMUZ;
		}
	}

	private static class Taanit17TamuzDetails
		implements Observance.Details {

		@Override public boolean isErev() {
			return false;
		}

		@Override public boolean lightCandles() {
			return true;
		}

		@Override public boolean sayHavdalah() {
			return false;
		}

		@Override public String getDescription() {
			return TAANIT_17_TAMUZ;
		}
	}

}  // End of Taanit class
