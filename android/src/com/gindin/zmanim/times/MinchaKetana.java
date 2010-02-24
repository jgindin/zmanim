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
			new Zman( "9.5 Hours", "9.5 solar hours after sunrise. This calculation is calculated based on the opinion of the GR\"A and the Baal Hatanya that the day is calculated from sunrise to sunset. This returns the time 9.5 * getShaahZmanisGra() after sunrise." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1°", "According to the Magen Avraham with the day starting and ending 16.1° below the horizon. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 solar hours after alos. The calculation used is 9.5 * getShaahZmanis16Point1Degrees() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "According to the Magen Avraham with the day starting 72 minutes before sunrise and ending 72 minutes after sunset. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 getShaahZmanis72Minutes() after alos. The calculation used is 9.5 * getShaahZmanis72Minutes() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetana72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset. This is the perfered earliest time to pray mincha in the opinion of the Ramba\"m and others. For more information on this see the documentation on mincha gedola. This is calculated as 9.5 solar hours after alos. The calculation used is 9.5 * getShaahZmanisAteretTorah() after alos." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getMinchaKetanaAteretTorah();
				}
			}
		);

		return zmanim;
	}
}  // End of MinchaKetana class
