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
class Hanetz
	extends Zmanim {

	Hanetz(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Hanetz, buildZmanim( zmanimCalculator ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Sunrise", "The exact sunrise time.", Zmanim.Type.Hanetz, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSunrise();
				}
			}
		);

		return zmanim;
	}


}  // End of Hanetz class
