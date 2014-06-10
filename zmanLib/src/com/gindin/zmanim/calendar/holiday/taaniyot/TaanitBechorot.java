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
@SuppressWarnings( "MagicNumber" )
public class TaanitBechorot
	extends Taanit {

	private static final String                               BECHOROT = TAANIT + "Bechorot";



	public TaanitBechorot() {
		super( HebrewMonth.NISSAN, 14 );
	}


	@Override
	public String getName() {
		return BECHOROT;
	}


	@Override
	protected boolean canFallOnFriday() {
		return true;
	}


	@Override
	protected Details createErevDetails( HebrewDate date ) {
		return new ErevTaanitDetails( date, BECHOROT );
	}


	@Override
	protected Details createYomDetails( HebrewDate date ) {
		return new TaanitDetails( date, BECHOROT );
	}


}  // End of TaanitBechorot class
