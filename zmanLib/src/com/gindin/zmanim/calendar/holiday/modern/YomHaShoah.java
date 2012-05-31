/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.modern;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;


/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class YomHaShoah
	extends Observance {

	private static final String                               NAME = "Yom HaShoah";


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.NISSAN != date.getHebrewMonth() ) {
			return null;
		}

		// Note that http://en.wikipedia.org/wiki/Yom_HaShoah claims that if Yom HaShoah falls on a Friday, it gets moved
		//  back to Thursday, and if it falls on Sunday, it gets pushed to the following Monday.
		//
		// However, the reference they provide, http://www.yomhashoah.org.uk/dates clearly shows that it *can* fall on
		//  either a Friday or a Sunday...
		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		if ( 26 == jewishDayOfMonth ) {
			return new ErevYomHaShoahDetails( date );
		}
		else if ( 27 == jewishDayOfMonth ) {
			return new YomHaShoahDetails( date );
		}

		return null;
	}


	private static class ErevYomHaShoahDetails
		extends Details {

		private ErevYomHaShoahDetails( HebrewDate date ) {
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
			return EREV + NAME;
		}
	}

	private static class YomHaShoahDetails
		extends Details {

		private YomHaShoahDetails( HebrewDate date ) {
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
			return NAME;
		}
	}

}  // End of YomHaShoah class
