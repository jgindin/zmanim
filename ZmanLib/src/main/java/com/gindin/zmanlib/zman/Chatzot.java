/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * See http://dinonline.org/2015/01/17/parshas-bo-the-halachic-time-of-chatzos/
 */
class Chatzot
	extends Zmanim {

	Chatzot(
		final ZmanimCalculator zmanimCalculator
	) {

		super( Type.Chatzot, zmanimCalculator );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Mid-Day", "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.", Zmanim.Type.Chatzot, false ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getChatzos() );
				}
			}
		);

		zmanim.add(
			new Zman( "Fixed Local Mid-Day", "R. Moshe Feinstein and Aruch Ha-Shulchan, based on chatzot being fixed at solar noon.", Type.Chatzot, true ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getFixedLocalChatzos() );
				}
			}
		);

		return zmanim;
	}


}  // End of Chatzot class
