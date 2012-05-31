/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;

/**
 *
 */
public class TishaBAv
	extends Taanit {

	private static final String                               TISHA_B_AV = "Tisha B'Av";


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
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTishaBAvDetails( date );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TishaBAvDetails( date );
	}


	private static class ErevTishaBAvDetails
		extends Details {

		private ErevTishaBAvDetails( HebrewDate date ) {
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
			return EREV + TISHA_B_AV;
		}
	}

	private static class TishaBAvDetails
		extends Details {

		private TishaBAvDetails( HebrewDate date ) {
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
			return TISHA_B_AV;
		}
	}

}  // End of TishaBAv class
