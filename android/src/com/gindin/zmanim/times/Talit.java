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
class Talit
	extends Zmanim {

	Talit(
		final ZmanimCalendar  zmanimCalendar
	) {

		super( Type.Talit, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "10.2 deg", "Based on the position of the sun when it is 10.2 degrees below geometric zenith (90 deg).", false )  {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir10Point2Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11 deg", "Based on the position of the sun when it is 11 degrees below geometric zenith (90 deg).", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11.5 deg", "based on the position of the sun when it is 11.5 degrees below geometric zenith (90 deg)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Point5Degrees();
				}
			}
		);

		return zmanim;
	}

}  // End of Talit class
