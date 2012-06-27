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
@SuppressWarnings( "MagicNumber" )
public class Taanit17Tamuz
	extends Taanit {


	private static final String                               TAANIT_17_TAMUZ = TAANIT + "17 Tamuz";


	public Taanit17Tamuz() {
		super( HebrewMonth.TAMMUZ, 17 );
	}


	@Override
	public String getName() {
		return TAANIT_17_TAMUZ;
	}


	@Override
	protected boolean canFallOnFriday() {
		return false;
	}


	@Override
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitDetails( date, TAANIT_17_TAMUZ );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, TAANIT_17_TAMUZ );
	}

}  // End of Taanit class
