/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.modern;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

import java.util.Calendar;


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

		// http://www.hebcal.com/holidays/yom-hashoah says:
		//  "It is held on the 27th of Nisan (April/May), unless the 27th would be adjacent to Shabbat, in which case
		//  the date is shifted by a day."
		final int jewishDayOfMonth = date.getHebrewDayOfMonth();

		switch ( date.getDayOfWeek() ) {
			case Calendar.SUNDAY:
				if ( 27 == jewishDayOfMonth ) {
					return new Details.Erev( date, NAME, false );
				}
				break;

			case Calendar.MONDAY:
				if ( 28 == jewishDayOfMonth ) {
					return new Details.Yom( date, NAME );
				}
				break;

			case Calendar.TUESDAY:
				break;

			case Calendar.WEDNESDAY:
				if ( 25 == jewishDayOfMonth ) {
					return new Details.Erev( date, NAME, false );
				}
				break;

			case Calendar.THURSDAY:
				if ( 26 == jewishDayOfMonth ) {
					return new Details.Yom( date, NAME );
				}

			case Calendar.FRIDAY:
			case Calendar.SATURDAY:
				if ( 26 == jewishDayOfMonth ) {
					// This is where we push it forward.
					return null;
				}
				break;
		}


		// We've handled the special cases, so if we get here, we're just looking for the "regular" year...
		if ( 26 == jewishDayOfMonth ) {
			return new Details.Erev( date, NAME, false );
		}
		else if ( 27 == jewishDayOfMonth ) {
			return new Details.Yom( date, NAME );
		}

		return null;
	}


}  // End of YomHaShoah class
