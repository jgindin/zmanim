/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.times;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class CandleLighting
	extends Zmanim {

	CandleLighting(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.CandleLighting, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();


		zmanim.add(
				new Zman( "Candle Lighting", "", Zmanim.Type.CandleLighting, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getCandelLighting();
				}
			}
		);

		return zmanim;
	}

}  // End of CandleLighting class
