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
class AlotHashachar
	extends Zmanim {

	AlotHashachar(
		final ZmanimCalendar  zmanimCalendar
	) {

		super( Type.Alot, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "120 Minutes", "120 minutes before sea level sunrise (no adjustment for elevation is made) based on the time to walk the distance of 5 Mil( Ula) at 24 minutes a Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120();
				}
			}
		);

		zmanim.add(
			new Zman( "120 Minutes Zmaniyot", "120 minutes zmaniyot (GR'A and the Baal Hatanya) or 1/6th of the day before sea level sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1°", "When the sun is 16.1° below the eastern geometric horizon before sunrise.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "18°", "When the sun is 18° below the eastern geometric horizon before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "19.8°", "When the sun is 19.8° below the eastern geometric horizon before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "26°", "When the sun is 26° below the eastern geometric horizon before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "60 Minutes", "60 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 15 minutes a Mil (the opinion of the Chavas Yair)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos60();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes", "72 minutes before sea level sunrise (no adjustment for elevation) based on the time to walk the distance of 4 Mil at 18 minutes a Mil.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes Zmaniyot", "72 minutes zmaniyot (GR'A and the Baal Hatanya) or 1/10th of the day before sea level sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "90 Minutes", "90 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 22.5 minutes a Mil.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90();
				}
			}
		);

		zmanim.add(
			new Zman( "90 Minutes Zmaniyot", "90 minutes zmaniyot (GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "96 minutes before sea level sunrise based on the time to walk the distance of 4 Mil at 24 minutes a Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "96 minutes zmaniyot (GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96Zmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of AlotHashachar class
