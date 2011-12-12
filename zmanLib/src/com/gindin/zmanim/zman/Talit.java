/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
class Talit
	extends Zmanim {

	private static final String                               DEFAULT_ZMAN_NAME = "10.2 deg";


	Talit(
		final ZmanimCalendar  zmanimCalendar
	) {

		super( Type.Talit, buildZmanim( zmanimCalendar ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "Based on the position of the sun when it is 10.2 degrees below geometric zenith.", Zmanim.Type.Talit, false )  {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir10Point2Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11 deg", "Based on the position of the sun when it is 11 degrees below geometric zenith.", Zmanim.Type.Talit, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "11.5 deg", "Based on the position of the sun when it is 11.5 degrees below geometric zenith.", Zmanim.Type.Talit ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMisheyakir11Point5Degrees();
				}
			}
		);

		return zmanim;
	}

}  // End of Talit class
