/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class Hanetz
	extends Zmanim {


	private static final String                               DEFAULT_ZMAN_NAME = "Sunrise";

	Hanetz(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Hanetz, zmanimCalculator );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "The exact sunrise time.", Zmanim.Type.Hanetz, false ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getSeaLevelSunrise() );
				}
			}
		);

		zmanim.add(
			new Zman( "Sunrise (with elevation)", "The exact sunrise time, taking elevation into account..", Zmanim.Type.Hanetz, false ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getSunrise() );
				}
			}
		);

		return zmanim;
	}


}  // End of Hanetz class
