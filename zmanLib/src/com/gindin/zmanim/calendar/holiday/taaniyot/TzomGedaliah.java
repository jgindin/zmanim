/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.taaniyot;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;

/**
 *
 */
public class TzomGedaliah
	extends Taanit {

	private static final String                               TZOM_GEDALIAH = "Tzom Gedaliah";



	public TzomGedaliah() {
		super( HebrewMonth.TISHREI, 3 );
	}


	@Override
	public String getName() {
		return TZOM_GEDALIAH;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitDetails( date, TZOM_GEDALIAH );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, TZOM_GEDALIAH );
	}


}  // End of TzomGedaliah class
