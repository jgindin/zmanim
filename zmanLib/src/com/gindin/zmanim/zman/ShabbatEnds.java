/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.zman;


/**
 *
 */
class ShabbatEnds
	extends TzaitHacochavim {


	ShabbatEnds(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.ShabbatEnd,
			mergeZmanimLists(
				buildStandardTzaitZmanim( Type.ShabbatEnd, zmanimCalculator ),
				buildExtraTzaitZmanim( Type.ShabbatEnd, zmanimCalculator ) )
		);
	}

}  // End of TzaitHacochavim class
