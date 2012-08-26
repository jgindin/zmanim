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
class Shkia
	extends Zmanim {

	Shkia(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Shkia, buildZmanim( zmanimCalculator ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Sunset", "The exact sunset time.", Zmanim.Type.Shkia, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getSunset();
				}
			}
		);

		return zmanim;
	}

}  // End of Shkia class
