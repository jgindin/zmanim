/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.HebrewYearType;

/**
 *
 */
public class RoshChodesh
	extends Observance {

	private static final String                               ROSH_CHODESH = "Rosh Chodesh";

	@Override
	public String getName() {
		return ROSH_CHODESH;
	}


	@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		int hebrewMonth = date.getHebrewMonth();
		int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		switch ( hebrewMonth ) {
			case HebrewMonth.NISSAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Nissan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Iyar" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Iyar" );
				}
				break;

			case HebrewMonth.IYAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Iyar" );
				}
				else if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Sivan" );
				}
				break;

			case HebrewMonth.SIVAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Sivan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Tammuz" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Tammuz" );
				}
				break;

			case HebrewMonth.TAMMUZ:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Tammuz" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Av" );
				}
				break;

			case HebrewMonth.AV:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Av" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Elul" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Elul" );
				}
				break;

			case HebrewMonth.ELUL:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Elul" );
				}
				break;

			case HebrewMonth.TISHREI:
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Heshvan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Heshvan" );
				}
				break;

			case HebrewMonth.CHESHVAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Heshvan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Kislev" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Will only happen in a Shalem year
					assert date.isCheshvanLong();
					assert date.getYearType() == HebrewYearType.SHALEM;
					return new RoshChodeshDetails( date, "Kislev" );
				}
				break;

			case HebrewMonth.KISLEV:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Kislev" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Tevet" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					assert !date.isKislevShort();
					assert date.getYearType() == HebrewYearType.SHALEM || date.getYearType() == HebrewYearType.QESIDRAH;
					return new RoshChodeshDetails( date, "Tevet" );
				}
				break;

			case HebrewMonth.TEVET:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Tevet" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Shevat" );
				}
				break;

			case HebrewMonth.SHEVAT:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Shevat" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				break;

			case HebrewMonth.ADAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar II" : "Nissan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Can only happen if this is a leap year
					assert date.isHebrewLeapYear();
					return new RoshChodeshDetails( date, "Adar II" );
				}
				break;

			case HebrewMonth.ADAR_II:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Adar II" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Nissan" );
				}
				break;
		}

		return null;
	}


	private static final class ErevRoshChodeshDetails
		extends Details.Erev {

		ErevRoshChodeshDetails( HebrewDate date, String month ) {
			super( date, ROSH_CHODESH + " " + month, false );
		}
	}


	private static final class RoshChodeshDetails
		extends Details.Yom {

		RoshChodeshDetails( HebrewDate date, String month ) {
			super( date, ROSH_CHODESH + " " + month );
		}
	}

}  // End of RoshChodesh class
