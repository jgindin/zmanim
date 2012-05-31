/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
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
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitBechorotDetails( date );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitBechorotDetails( date );
	}


	private static class ErevTaanitBechorotDetails
		extends Details {

		private ErevTaanitBechorotDetails( HebrewDate date ) {
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
			return EREV + BECHOROT;
		}
	}

	private static class TaanitBechorotDetails
		extends Details {

		private TaanitBechorotDetails( HebrewDate date ) {
			super( date );
		}


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
