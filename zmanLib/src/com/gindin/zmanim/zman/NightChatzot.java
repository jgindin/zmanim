/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.ZmanimCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class NightChatzot
	extends Zmanim {

	public NightChatzot( ZmanimCalendar zmanimCalendar ) {
		super( Type.Chatzot, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "Mid-night", "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.", Zmanim.Type.NightChatzot, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getNightChatzos();
				}
			}
		);

		return zmanim;
	}



}  // End of NightChatzot class
