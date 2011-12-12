/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;



/**
 *
 */
class ShabbatEnds
	extends TzaitHacochavim {


	ShabbatEnds(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.ShabbatEnd, zmanimCalendar,
			mergeZmanimLists( buildStandardTzaitZmanim( Type.ShabbatEnd, zmanimCalendar ), buildExtraTzaitZmanim( Type.ShabbatEnd, zmanimCalendar ) ) );
	}

}  // End of TzaitHacochavim class
