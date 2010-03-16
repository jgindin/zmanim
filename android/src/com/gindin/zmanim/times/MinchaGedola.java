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
			new Zman( "GR'A", "Based on the opinion of the GR'A and the Baal Hatanya that the day is calculated from sunrise to sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "This method returns the time of mincha gedola according to the Magen Avraham with the day starting and ending 16.1° below the horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola16Point1Degrees();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedola30Minutes(), "30 minutes after chatzot", "30 minutes after chatzos and not 1/2 of a shaah zmanis after chatzos as calculated by getMinchaGedola." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "This method returns the time of mincha gedola according to the Magen Avraham with the day starting 72 minutes before sunrise and ending 72 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedola72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "This method returns the time of mincha gedola based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaGedolaAteretTorah();
				}
			}
		);

//		minchaGedola.addZman( zmanimCalendar.getMinchaGedolaGreaterThan30(), "", "" );

		return zmanim;
	}

}  // End of MinchaGedola class
