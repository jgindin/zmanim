/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservanceWithCholHaMoed;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Pesach
	extends AbstractCachingObservanceWithCholHaMoed {

	private static final String                               NAME = "Pesach";


	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewMonth = date.getHebrewMonth();
		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		if ( HebrewMonth.NISSAN != hebrewMonth ) {
			return null;
		}

		switch ( hebrewDayOfMonth ) {
			case 14:
				return getErevDetails();

			case 15:
				return getYomDetails();

			case 16:
				return getYom2Details();

			case 17:
			case 18:
			case 19:
			case 20:
				return new CholHaMoedDetails( hebrewDayOfMonth - 14, NAME );

			case 21:
				return getPenultimateYomDetails();

			case 22:
				return getFinalYomDetails();
		}

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
