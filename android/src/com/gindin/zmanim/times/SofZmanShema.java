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
			new Zman( "3 Hours before Chatzot", "3 hours (regular and not zmaniyos) before chatzot. This is the opinion of the Shach in the Nekudas Hakesef (Yora Deah 184), Shevus Yaakov, Chasan Sofer and others." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShma3HoursBeforeChatzos();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1° to Sunset", "Based on the opinion that the day starts at alos 16.1° and ends at sunset. 3 shaos zmaniyos are calculated based on this day and added to alosto reach this time. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion that the day is calculated from a alos 16.1° to sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToSunset();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1° and ends at tzais 7.083°", "Based on the opinion that the day starts at alos 16.1° and ends at tzais 7.083°. 3 shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 3 shaos zmaniyos (temporal hours) after alos 16.1° based on the opinion that the day is calculated from a alos 16.1° to tzais 7.083°." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on the calculation of Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset. shaos zmaniyos are calculated based on this day and added to alos to reach this time. This time is 3 shaos zmaniyos (temporal hours) after alos 72 zmaniyos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaAteretTorah();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaFixedLocal(), "", "" );

		zmanim.add(
			new Zman( "GR'A", "3 shaos zmaniyos (solar hours) after sunrise based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 3 *getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaGRA();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'a Kol Eliyahu", "From the GR'A in Kol Eliyahu on Berachos #173 that states that zman krias shema is calculated as half the time from sunrise to fixed local chatzos. The GR\"A himself seems to contradic this when he stated that zman krias shema is 1/4 of the day from sunrise to sunset. See Sarah Lamoed #25 in Yisroel Vehazmanim Vol III page 1016." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaKolEliyahu();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A", "MG'A based on alos being 72 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes before sunrise to nightfall of 72 minutes after sunset. This returns the time of 3 * shaos zmaniyos after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "MG'A based on alos being 120 minutes or 1/6th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 120 minutes before sunrise to nightfall of 120 minutes after sunset. This returns the time of 3 *getShaahZmanis120Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "MG'A based on alos being 16.1° before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 16.1° below sunrise or sunset. This returns the time of 3 *getShaahZmanis16Point1Degrees() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8°", "MG'A based on alos being 19.8° before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from dawn to nightfall with both being 19.8° below sunrise or sunset. This returns the time of 3 *getShaahZmanis19Point8Degrees() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA19Point8Degrees();
				}
			}
		);

//		sofZmanShema.addZman( zmanimCalendar.getSofZmanShmaMGA72Minutes(), "MG'A 72 Minutes", "MG'A based on alos being 72 minutes zmaniyos, or 1/10th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyos after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." );

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG'A based on alos being 72 minutes zmaniyos, or 1/10th of the day before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 72 minutes zmaniyos, or 1/10th of the day before sea level sunrise to nightfall of 72 minutes zmaniyos after sea level sunset. This returns the time of 3 * getShaahZmanis72MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "MG\"A based on alos being 90 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes before sunrise to nightfall of 90 minutes after sunset. This returns the time of 3 * getShaahZmanis90Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG\"A based on alos being 90 minutes zmaniyos before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 90 minutes zmaniyos before sunrise to nightfall of 90 minutes zmaniyos after sunset. This returns the time of 3 * getShaahZmanis90MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "MG\"A based on alos being 96 minutes before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes before sunrise to nightfall of 96 minutes after sunset. This returns the time of 3 * getShaahZmanis96Minutes() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "MG\"A based on alos being 96 minutes zmaniyos before sunrise. This time is 3 shaos zmaniyos (solar hours) after dawn based on the opinion of the MG\"A that the day is calculated from a dawn of 96 minutes zmaniyos before sunrise to nightfall of 96 minutes zmaniyos after sunset. This returns the time of 3 * getShaahZmanis96MinutesZmanis() after dawn." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getSofZmanShmaMGA96MinutesZmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of SofZmanShema class
