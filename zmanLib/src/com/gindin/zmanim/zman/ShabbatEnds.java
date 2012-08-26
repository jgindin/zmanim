/*
 * Copyright (c) 2011. Jay R. Gindin
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

		super( Type.ShabbatEnd, zmanimCalculator,
			mergeZmanimLists(
				buildStandardTzaitZmanim( Type.ShabbatEnd, zmanimCalculator ),
				buildExtraTzaitZmanim( Type.ShabbatEnd, zmanimCalculator ) )
		);
	}

}  // End of TzaitHacochavim class
