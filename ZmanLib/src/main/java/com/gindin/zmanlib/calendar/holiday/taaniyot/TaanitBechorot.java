/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;

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
