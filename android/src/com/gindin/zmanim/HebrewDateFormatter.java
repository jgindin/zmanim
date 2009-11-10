package com.gindin.zmanim;

import com.ibm.icu.util.HebrewCalendar;


/**
 * Just a simple, hard-coded formatting of the Hebrew date.
 */
public class HebrewDateFormatter {

	private HebrewDateFormatter() {
	}


	public static String format(
		HebrewCalendar calendar
	) {

		final int day = calendar.get( HebrewCalendar.DAY_OF_MONTH );
		final int monthIndex = calendar.get( HebrewCalendar.MONTH );
		final int year = calendar.get( HebrewCalendar.YEAR );

		final String month;
		switch( monthIndex ) {
			case HebrewCalendar.NISAN:
				month = "Nisan";
				break;

			case HebrewCalendar.IYAR:
				month = "Iyar";
				break;

			case HebrewCalendar.SIVAN:
				month = "Sivan";
				break;

			case HebrewCalendar.TAMUZ:
				month = "Tamuz";
				break;

			case HebrewCalendar.AV:
				month = "Av";
				break;

			case HebrewCalendar.ELUL:
				month = "Elul";
				break;

			case HebrewCalendar.TISHRI:
				month = "Tishri";
				break;

			case HebrewCalendar.HESHVAN:
				month = "Heshvan";
				break;

			case HebrewCalendar.KISLEV:
				month = "Kislev";
				break;

			case HebrewCalendar.TEVET:
				month = "Tevent";
				break;

			case HebrewCalendar.SHEVAT:
				month = "Shevat";
				break;

			case HebrewCalendar.ADAR:
				// If this is a leap year, then month is Adar II.
				calendar.add( -1, HebrewCalendar.MONTH );
				if ( HebrewCalendar.ADAR_1 == calendar.get( HebrewCalendar.MONTH ) ) {
					month = "Adar II";
				}
				else {
					month = "Adar";
				}
				calendar.add( 1, HebrewCalendar.MONTH );
				break;

			case HebrewCalendar.ADAR_1:
				month = "Adar I";
				break;

			default:
				month = "?" + monthIndex;
		}

		return day + " " + month + " " + year;

	}
}  // End of HebrewDateFormatter class
