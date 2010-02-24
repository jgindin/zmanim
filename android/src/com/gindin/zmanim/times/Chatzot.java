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
class Chatzot
	extends Zmanim {

	Chatzot(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.Alot, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Chatzot (mid-day)", "GR'A based on the day for Jewish halachic times starting at sunrise and ending at sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getChatzos();
				}
			}
		);

		return zmanim;
	}

}  // End of Chatzot class
