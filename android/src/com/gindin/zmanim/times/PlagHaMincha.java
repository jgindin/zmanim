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
class PlagHaMincha
	extends Zmanim {

	PlagHaMincha(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.PlagHaMincha, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "GR'A", "10.75 hours after sunrise. This calculation is calculated based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 10.75 *getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 18°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 26°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 60 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha60Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes Zmaniyot", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 10.75 shaos zmaniyos (temporal hours) after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHaminchaAteretTorah();
				}
			}
		);

		return zmanim;
	}

}  // End of PlagHaMincha class
