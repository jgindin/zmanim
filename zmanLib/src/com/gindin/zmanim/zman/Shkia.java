/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class Shkia
	extends Zmanim {

	Shkia(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.Shkia, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Sunset", "The exact sunset time.", Zmanim.Type.Shkia, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSunset();
				}
			}
		);

		return zmanim;
	}

}  // End of Shkia class
