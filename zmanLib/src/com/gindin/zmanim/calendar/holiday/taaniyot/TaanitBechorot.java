/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class TaanitBechorot
	extends Taanit {

	private static final String                               BECHOROT = TAANIT + "Bechorot";



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
	protected Details createErevDetails() {
		return new ErevTaanitBechorotDetails();
	}


	@Override
	protected Details createYomDetails() {
		return new TaanitBechorotDetails();
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
			return EREV + BECHOROT;
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
