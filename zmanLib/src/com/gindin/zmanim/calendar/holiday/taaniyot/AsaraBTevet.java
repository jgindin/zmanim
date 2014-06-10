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
public class AsaraBTevet
	extends Taanit {

	private static final String                               ASARA_BTEVET = "Asara B'Tevet";



	public AsaraBTevet() {
		super( HebrewMonth.TEVET, 10 );
	}


	@Override
	public String getName() {
		return ASARA_BTEVET;
	}


	@Override
	protected boolean canFallOnFriday() {
		return true;
	}


	@Override
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitDetails( date, ASARA_BTEVET );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, ASARA_BTEVET );
	}

}  // End of AsaraBTevet class
