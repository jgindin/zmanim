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
			new Zman( "8.5°", "When the sun is 8.5° below the western geometric horizon (90°) after sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 120 Minutes", "Magen Avraham that the time to walk the distance of a Mil in the Ramba\"ms opinion is 2/5 of an hour (24 minutes) for a total of 120 minutes based on the opinion of Ula who calculated tzais as 5 Mil after sea level shkiah (sunset)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 120 Minutes Zmaniyot", "120 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais120Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "16.1°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais16Point1Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "18°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais18Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "19.8°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais19Point8Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "26°", "" ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais26Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Chavas Yair and Divray Malkiel", "Based on the opinion of the Chavas Yair and Divray Malkiel that the time to walk the distance of a Mil is 15 minutes for a total of 60 minutes for 4 mil after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais60();
				}
			}
		);

		zmanim.add(
			new Zman( "Rambam", "Based on the opinion of the Ramba\"m and Rabainu Tam that tzais is calculated as the time it takes to walk 4 Mil at 18 minutes a Mil for a total of 72 minutes." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72();
				}
			}
		);

		zmanim.add(
			new Zman( "72 Minutes Zmaniyot", "Calculated as 72 minutes zmaniyos, or 1/10th of the day after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais72Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "MG'A 90 Minutes", "Based on the opinion of the Magen Avraham that the time to walk the distance of a Mil in the Ramba\"m's opinion is 18 minutes for a total of 90 minutes based on the opinion of Ula who calculated tzais as 5 Mil after sea level shkiah (sunset)." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 90 Minutes Zmaniyot", "Calculated using 90 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais90Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "96 Minutes", "Calculated as 96 minutes after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96();
				}
			}
		);

		zmanim.add(
			new Zman( "GR'A 96 Minutes Zmaniyot", "Calculated using 96 minutes zmaniyos (GR\"A and the Baal Hatanya) after sea level sunset." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzais96Zmanis();
				}
			}
		);

		zmanim.add(
			new Zman( "40 Minutes After Sunset", "Usually calculated as 40 minutes after sunset. Please note that Chacham Yosef Harari-Raful of Yeshivat Ateret Torah who uses this time, does so only for calculating various other zmanai hayom such as Sof Zman Krias Shema and Plag Hamincha. His calendars do not publish a zman for Tzais. It should also be noted that Chacham Harari-Raful provided a 25 minute zman for Israel." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisAteretTorah();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 5.95°", "Based on the opinion of the Geonim calculated at the sun's position at 5.95° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim5Point95Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 7.083°", "Based on the opinion of the Geonim calculated at the sun's position at 7.083° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim7Point083Degrees();
				}
			}
		);

		zmanim.add(
			new Zman( "Geonim 8.5°", "Based on the opinion of the Geonim calculated at the sun's position at 8.5° below the western horizon." ) {
				@Override protected Date calculate() {
					return zmanimCalendar.getTzaisGeonim8Point5Degrees();
				}
			}
		);

		return zmanim;
	}
}  // End of TzaitHachocavim class
