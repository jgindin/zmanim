/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;

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
		return new ErevTaanitDetails( date, TISHA_B_AV );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, TISHA_B_AV );
	}


}  // End of TishaBAv class
