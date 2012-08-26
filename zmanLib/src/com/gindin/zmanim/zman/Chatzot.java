/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class Chatzot
	extends Zmanim {

	Chatzot(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Chatzot, buildZmanim( zmanimCalculator ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Mid-day", "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.", Zmanim.Type.Chatzot, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getChatzos();
				}
			}
		);

		return zmanim;
	}

}  // End of Chatzot class
