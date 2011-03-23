/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 */
class FastEnds
	extends TzaitHacochavim {


	FastEnds(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.FastEnd, zmanimCalendar, buildZmanim( zmanimCalendar ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Seattle Sephardic", "Based on the Seattle Sephardic community's tradition.", Type.FastEnd ) {
				@Override protected Date calculate() {
					//noinspection MagicNumber
					return zmanimCalendar.getEndOfFast( 30 );
				}
			}
		);

		return zmanim;
	}


}  // End of TzaitHacochavim class
