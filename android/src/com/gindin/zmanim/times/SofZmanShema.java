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
class SofZmanShema
	extends Zmanim {

	SofZmanShema(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.SofZmanShema, buildZmanim( zmanimCalendar ) );
	}

	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( "3 Hours before Chatzot", "3 hours before chatzot. This is the opinion of the Shach in the Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer and others." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShma3HoursBeforeChatzos();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1° to Sunset", "Based on the opinion that the day starts at alot 16.1° and ends at sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToSunset();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1° and ends at tzait 7.083°", "Based on the opinion that the day starts at alot 16.1° and ends at tzait 7.083°." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAteretTorah();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( "GR'A", "3 shaot zmaniyot after sunrise based on the opinion of the GR'A and the Baal Hatanya that the day is calculated from sunrise to sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A Kol Eliyahu", "From the GR'A in Kol Eliyahu on Berachos #173 that states that zman kriat shema is calculated as half the time from sunrise to fixed local chatzos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaKolEliyahu();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "MG'A based on alot being 72 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "MG'A based on alot being 120 minutes or 1/6th of the day before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "MG'A based on alot being 16.1° before sunrise.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8°", "MG'A based on alot being 19.8° before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaMGA72Minutes(), "MG'A 72 Minutes", "MG'A based on alot being 72 minutes zmaniyot, or 1/10th of the day before sunrise. This time is 3 shaot zmaniyot after dawn based on the opinion of the MG'A that the day is calculated from a dawn of 72 minutes zmaniyot, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyot after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG'A based on alot being 72 minutes zmaniyot, or 1/10th of the day before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "MG'A based on alot being 90 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG'A based on alot being 90 minutes zmaniyot before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "MG'A based on alot being 96 minutes before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "MG'A based on alot being 96 minutes zmaniyot before sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanShema class
