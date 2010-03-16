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
class MinchaKetana
	extends Zmanim {

	MinchaKetana(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.MinchaKetana, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "GR'A 9.5 Hours", "9.5 solar hours after sunrise, based on the opinion of the GR'A and the Baal Hatanya that the day is calculated from sunrise to sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "Based on the day starting and ending 16.1° below the horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "Based on the day starting 72 minutes before sunrise and ending 72 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetanaAteretTorah();
				}
			}
		);

		return zmanim;
	}
}  // End of MinchaKetana class
