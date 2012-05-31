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
@SuppressWarnings( "MagicNumber" )
public class Taanit17Tamuz
	extends Taanit {


	private static final String                               TAANIT_17_TAMUZ = TAANIT + "17 Tamuz";


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
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanit17TamuzDetails( date );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new Taanit17TamuzDetails( date );
	}


	private static class ErevTaanit17TamuzDetails
		extends Observance.Details {

		private ErevTaanit17TamuzDetails( HebrewDate date ) {
			super( date );
		}


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
			return EREV + TAANIT_17_TAMUZ;
		}
	}

	private static class Taanit17TamuzDetails
		extends Observance.Details {

		private Taanit17TamuzDetails( HebrewDate date ) {
			super( date );
		}


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
