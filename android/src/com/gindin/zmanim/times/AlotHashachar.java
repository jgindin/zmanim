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
			new Zman( "120 Minutes", "120 minutes before sea level sunrise (no adjustment for elevation is made) based on the time to walk the distance of 5 Mil( Ula) at 24 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 5 Mil(Ula)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120();
				}
			}
		);

		zmanim.add(
			new Zman( "120 Minutes Zmaniyot", "120 minutes zmaniyos( GR'A and the Baal Hatanya) or 1/6th of the day before sea level sunrise. This is based on a 24 minute Mil so the time for 5 Mil is 120 minutes which is 1/6th of a day (12 * 60 = 720) / 6 =120 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 2)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos120Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1°", "When the sun is 16.1° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 72 minutes but uses a degree based calculation instead of 72 exact minutes. This calculation is based on the position of the sun 72 minutes before sunrise in Jerusalem in the equinox which calculates to 16.1° below geometric zenith." ) {
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
			new Zman( "19.8°", "When the sun is 19.8° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 90 minutes but uses a degree based calculation instead of 90 exact minutes. This calculation is based on the position of the sun 90 minutes before sunrise in Jerusalem in the equinox which calculates to 19.8° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "26°", "When the sun is 26° below the eastern geometric horizon before sunrise. This calculation is based on the same calculation of 120 minutes but uses a degree based calculation instead of 120 exact minutes. This calculation is based on the position of the sun 120 minutes before sunrise in Jerusalem in the equinox which calculates to 26° below geometric zenith." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "60 Minutes", "60 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 15 minutes a Mil (the opinion of the Chavas Yair. See the Divray Malkiel). This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos60();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes", "72 minutes before sea level sunrise (no adjustment for elevation) based on the time to walk the distance of 4 Mil at 18 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes Zmaniyot", "72 minutes zmaniyot ( GR'A and the Baal Hatanya) or 1/10th of the day before sea level sunrise. This is based on an 18 minute Mil so the time for 4 Mil is 72 minutes which is 1/10th of a day (12 * 60 = 720) based on the day starting at sea level sunrise and ending at sea level sunset. The actual alculation is getSeaLevelSunrise()- ( getShaahZmanisGra() * 1.2). This calculation is used in the calendars published by Hisachdus Harabanim D'Artzos Habris Ve'Kanada." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos72Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "90 Minutes", "90 minutes before sea level sunrise on the time to walk the distance of 4 Mil at 22.5 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90();
				}
			}
		);

		zmanim.add(
			new Zman( "90 Minutes Zmaniyot", "90 minutes zmaniyot ( GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise. This is based on a 22.5 minute Mil so the time for 4 Mil is 90 minutes which is 1/8th of a day (12 * 60 = 720) /8 =90 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 1.5)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos90Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "96 minutes before sea level sunrise based on the time to walk the distance of 4 Mil at 24 minutes a Mil. This is based on the opinion of most Rishonim who stated that the time of the Neshef (time between dawn and sunrise) does not vary by the time of year or location but purely depends on the time it takes to walk the distance of 4 Mil." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes Zmaniyot", "96 minutes zmaniyos( GR'A and the Baal Hatanya) or 1/8th of the day before sea level sunrise. This is based on a 24 minute Mil so the time for 4 Mil is 90 minutes which is 1/7.5th of a day (12 * 60 = 720) / 7.5 =96 based on the day starting at sunrise and ending at sunset. The actual calculation is getSunrise() - (getShaahZmanisGra() * 1.6)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getAlos96Zmanis();
				}
			}
		);

		return zmanim;
	}

}  // End of AlotHashachar class
