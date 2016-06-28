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
class NightChatzot
	extends Zmanim {

	public NightChatzot( ZmanimCalculator zmanimCalculator ) {
		super( Type.NightChatzot, zmanimCalculator );
	}


	@Override
	protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Mid-night", "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.", Zmanim.Type.NightChatzot, false ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getNightChatzos() );
				}
			}
		);

		zmanim.add(
			new Zman( "Fixed Local Mid-Night", "R. Moshe Feinstein and Aruch Ha-Shulchan, based on chatzot being fixed at solar noon.", Type.NightChatzot, true ) {
				@Override protected Calendar calculate() {
					return toCalendar( zmanimCalculator, zmanimCalculator.getFixedLocalNightChatzos() );
				}
			}
		);

		return zmanim;
	}


}  // End of NightChatzot class
