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
			new Zman( "2 Hours before Chatzot", "2 hours before (regular and not zmaniyos) chatzot. This is based on the opinions that calculate sof zman krias shema as getSofZmanShma3HoursBeforeChatzos(). This returns the time of 2 hours before ZmanimCalendar.getChatzos()" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfila2HoursBeforeChatzos();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( "GR'A", "This time is 4 hours into the day based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 4 * getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 4 shaos zmaniyos (temporal hours) after alos 72 zmaniyos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilahAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "MG\"A based on alos being 72 minutes before sunrise. This time is 4 shaos zmaniyos (temporal hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes before sunrise to nightfall of 72 minutes after sunset. This returns the time of 4 * getShaahZmanisMGA() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "MG\"A based on alos being 120 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 120 minutes before sunrise to nightfall of 120 minutes after sunset. This returns the time of 4 *getShaahZmanis120Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "MG\"A based on alos being ( ) 16.1° before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 16.1° below sunrise or sunset. This returns the time of 4 *getShaahZmanis16Point1Degrees() after dawn" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanTefila.addZman( zmanimCalendar.getSofZmanTfilaMGA72Minutes(), "", "" );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG\"A based on alos being 72 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos before sunrise to nightfall of 72 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis72MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "MG\"A based on alos being 90 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes before sunrise to nightfall of 90 minutes after sunset. This returns the time of 4 * getShaahZmanis90Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG\"A based on alos being 90 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes zmaniyos before sunrise to nightfall of 90 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis90MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "MG\"A based on alos being 96 minutes before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes before sunrise to nightfall of 96 minutes after sunset. This returns the time of 4 * getShaahZmanis96Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "MG\"A based on alos being 96 minutes zmaniyos before sunrise. This time is 4 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes zmaniyos before sunrise to nightfall of 96 minutes zmaniyos after sunset. This returns the time of 4 * getShaahZmanis96MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanTfilaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanTefila class
