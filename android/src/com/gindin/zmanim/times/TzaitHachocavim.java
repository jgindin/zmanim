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
class TzaitHachocavim
	extends Zmanim {

	TzaitHachocavim(
		final ZmanimCalendar zmanimCalendar
	) {

		super( Type.TzaitHachocavim, buildZmanim( zmanimCalendar ) );
	}


	private static List<Zman> buildZmanim(
		final ZmanimCalendar  zmanimCalendar
	) {

		List<Zman>  zmanim = new ArrayList<Zman>();


		zmanim.add(
			new Zman( "8.5°", "When the sun is 8.5° below the western geometric horizon (90°) after sunset.", false ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Based on the time to walk a Mil in 24 minutes, for a total of 120 minutes based on the opinion of Ula who calculated tzait as 5 Mil after sea level shkiah (sunset)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 120 Minutes Zmaniyot", "120 minutes zmaniyot after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1°", "When the sun is 16.1° below the western geometric horizon after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "18°", "When the sun is 18° below the western geometric horizon after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "19.8°", "When the sun is 19.8° below the western geometric horizon after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "26°", "When the sun is 26° below the western geometric horizon after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Chavas Yair and Divray Malkiel", "Based on the time to walk 4 Mil at 15 minutes/Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais60();
				}
			}
		);

		zmanim.add(
			new Zman( "Rambam / Rabainu Tam", "Based on the time to walk 4 Mil at 18 minutes/Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes Zmaniyot", "Calculated as 72 minutes zmaniyot, or 1/10th of the day after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on the time to walk 5 Mil at 18 minutes/Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 90 Minutes Zmaniyot", "90 minutes zmaniyot after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "96 minutes after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 96 Minutes Zmaniyot", "96 minutes zmaniyot after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "Chacham Yosef Harari-Raful", "Usually calculated as 40 minutes after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 5.95°", "Based on the sun's position at 5.95° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim5Point95Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 7.083°", "Based on the sun's position at 7.083° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 8.5°", "Based on the sun's position at 8.5° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim8Point5Degrees();
				}
			}
		);

		return zmanim;
	}
}  // End of TzaitHachocavim class
