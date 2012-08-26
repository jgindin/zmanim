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
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.FastEnd, zmanimCalculator,
			mergeZmanimLists( buildStandardTzaitZmanim( Type.FastEnd, zmanimCalculator ), buildFastEndsZmanim(
				zmanimCalculator ) ) );
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private static List<Zman> buildFastEndsZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Seattle Sephardic", "Based on the Seattle Sephardic community's tradition.", Type.FastEnd ) {
				@Override protected Date calculate() {
					//noinspection MagicNumber
					return zmanimCalculator.getEndOfFast( 30 );
				}
			}
		);

		return zmanim;
	}


}  // End of TzaitHacochavim class
