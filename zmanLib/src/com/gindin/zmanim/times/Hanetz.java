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
class Hanetz
	extends Zmanim {

	Hanetz(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.Hanetz, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Sunrise", "The exact sunrise time.", Zmanim.Type.Hanetz, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSunrise();
				}
			}
		);

		return zmanim;
	}


}  // End of Hanetz class
