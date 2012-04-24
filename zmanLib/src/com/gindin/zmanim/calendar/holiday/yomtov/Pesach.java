/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservanceWithCholHaMoed;

/**
 *
 */
public class Pesach
	extends AbstractCachingObservanceWithCholHaMoed {

	private static final String                               NAME = "Pesach";


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails( NAME );
	}


	@Override
	protected Details createYomDetails() {
		return new YomDetails( NAME, 1 );
	}


	@Override
	protected Details createYom2Details() {
		return new YomDetails( NAME, 2 );
	}


	@Override
	protected Details createPenultimateYomDetails() {
		return new LastDaysDetails( NAME, 7 );
	}


	@Override
	protected Details createFinalYomDetails() {
		// TODO: Not quite right for Israel...
		return new LastDaysDetails( NAME, 8 );
	}

}  // End of Pesach class
