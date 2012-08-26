/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class NightChatzot
	extends Zmanim {

	public NightChatzot( ZmanimCalculator zmanimCalculator ) {
		super( Type.Chatzot, buildZmanim( zmanimCalculator ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalculator zmanimCalculator
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Mid-night", "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.", Zmanim.Type.NightChatzot, false ) {
				@Override protected Date calculate() {
					return zmanimCalculator.getNightChatzos();
				}
			}
		);

		return zmanim;
	}



}  // End of NightChatzot class
