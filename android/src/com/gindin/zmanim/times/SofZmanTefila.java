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
class SofZmanTefila
	extends Zmanim {

	SofZmanTefila(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.SofZmanTefila, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "2 Hours before Chatzot", "2 hours before chatzot." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfila2HoursBeforeChatzos();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( "GR'A", "This time is 4 hours into the day based on the opinion of the GR'A and the Baal Hatanya that the day is calculated from sunrise to sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilahAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "MG'A based on alot being 72 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "MG'A based on alot being 120 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "MG'A based on alot being 16.1° before sunrise.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8°", "MG'A based on alot being 19.8° before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaMGA72Minutes(), "", "" );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG'A based on alot being 72 minutes zmaniyot before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "MG'A based on alot being 90 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG'A based on alot being 90 minutes zmaniyot before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "MG'A based on alot being 96 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "MG'A based on alot being 96 minutes zmaniyot before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanTefila class
