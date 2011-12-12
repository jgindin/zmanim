/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Used for allowing the user to see when a full-day fast ends.
 */
class FastEnds
	extends TzaitHacochavim {

	FastEnds(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.FastEnd, zmanimCalendar,
			mergeZmanimLists( buildStandardTzaitZmanim( Type.FastEnd, zmanimCalendar ), buildFastEndsZmanim( zmanimCalendar ) ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private static List<Zman> buildFastEndsZmanim(
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
