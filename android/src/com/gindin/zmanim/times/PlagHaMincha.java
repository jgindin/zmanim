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

	private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


	PlagHaMincha(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.PlagHaMincha, buildZmanim( zmanimCalendar ) );
	}


	@Override
	public Zman getDefault() {
		return getByName( DEFAULT_ZMAN_NAME );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();

		zmanim.add(
			new Zman( DEFAULT_ZMAN_NAME, "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.PlagHaMincha, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "MG'A where the day runs from dawn to dusk. Dawn for this calculation is 120 minutes before sunrise and dusk is 120 minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes Zmaniyot", "MG'A where the day runs from dawn to dusk. Dawn for this calculation is 120 minutes zemaniyot before sunrise and dusk is 120 minutes zemaniyot after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha120MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 16.1 deg", "Based on the day starting at alot 16.1 degrees and ending at tzait 16.1 degrees. This is calculated as 10.75 hours zmaniyot after dawn", Zmanim.Type.PlagHaMincha, false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 18 deg", "Based on the day starting at alot 18 degrees and ending at tzait 18 degrees.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 19.8 deg", "Based on the the day starting at alot 19.8 degrees and ending at tzait 19.8 degrees.",Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 26 deg", "Based on the day starting at alot 26 degrees and ending at tzait 26 degrees.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 60 Minutes", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 60 minutes before sunrise and dusk is 60 minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha60Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 72 minutes before sunrise and dusk is 72 minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 72 Minutes Zmaniyot", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 72 minutes zemaniyot before sunrise and dusk is 72 zemaniyot minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha72MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 90 minutes before sunrise and dusk is 90 minutes after sunset.",Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes Zmaniyot", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 90 minutes zemaniyot before sunrise and dusk is 90 zemaniyot minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha90MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 96 minutes before sunrise and dusk is 96 minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96Minutes();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 96 Minutes Zmaniyot", "MG'A, based on the day running from dawn to dusk. Dawn for this calculation is 96 minutes zemaniyot before sunrise and dusk is 96 zemaniyot minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHamincha96MinutesZmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Based on Chacham Yosef Harari-Raful of Yeshivat Ateret Torah, that the day starts 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.PlagHaMincha ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getPlagHaminchaAteretTorah();
				}
			}
		);

		return zmanim;
	}

}  // End of PlagHaMincha class
