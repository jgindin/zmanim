/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

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

		super( Type.FastEnd, zmanimCalculator );
	}


	protected List<Zman> buildZmanimList(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = buildStandardTzaitZmanim( Type.FastEnd, zmanimCalculator );

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
