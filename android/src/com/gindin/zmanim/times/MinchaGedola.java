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
class MinchaGedola
	extends Zmanim {

	MinchaGedola(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.MinchaGedola, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "GR'A", "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.MinchaGedola, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Magen Avraham, based on the day starting and ending 16.1 degrees below the horizon.", Zmanim.Type.MinchaGedola, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola16Point1Degrees();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedola30Minutes(), "30 minutes after chatzot", "30 minutes after chatzos and not 1/2 of a shaah zmanis after chatzos as calculated by getMinchaGedola." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "Magen Avraham, based onthe day starting 72 minutes before sunrise and ending 72 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedolaAteretTorah();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedolaGreaterThan30(), "", "" );

		return zmanim;
	}

}  // End of MinchaGedola class
