/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;


/**
 * The Fast is observed on the 13th day of the Hebrew month of Adar. If the date of the Fast of Esther falls on
 *  Shabbat (Saturday), the fast is instead observed on the preceding Thursday.
 *    -- http://en.wikipedia.org/wiki/Fast_of_Esther
 */
@SuppressWarnings( "MagicNumber" )
public class TaanitEsther
	extends Taanit {

	private static final String                               ESTHER = TAANIT + "Esther";



	public TaanitEsther() {
		super( HebrewMonth.ADAR, 13 );
	}


	@Override
	public String getName() {
		return ESTHER;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected boolean canPushToSunday() {
		return false;
	}


	@Override
	protected int getAdjustedMonthOfFast( HebrewDate date ) {
		if ( date.isHebrewLeapYear() ) {
			return HebrewMonth.ADAR_II;
		}

		return HebrewMonth.ADAR;
	}


	@Override
	protected Details checkWednesday(
		HebrewDate date
	) {

		Details details = super.checkWednesday( date );

		if ( null == details ) {

			final int jewishDayOfMonth = date.getHebrewDayOfMonth();
			if ( ( normalDayOfFast - 3 ) == jewishDayOfMonth ) {
				// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
				return createErevDetails( date );
			}

		}

		return null;
	}


	@Override
	protected Details checkThursday(
		HebrewDate date
	) {

		Details details = super.checkThursday( date );
		if ( null == details ) {

			final int jewishDayOfMonth = date.getHebrewDayOfMonth();
			if ( ( normalDayOfFast - 2 ) == jewishDayOfMonth ) {
				// Only the Ta'anit Esther will fall into this case, of getting pushed back from Shabbat to Thursday...
				return createYomDetails( date );
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitDetails( date, ESTHER );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, ESTHER );
	}


}  // End of TaanitEsther class
