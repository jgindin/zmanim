/*
 * Copyright (c) 2014. Jay R. Gindin
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
					return new RoshChodeshDetails( date, "Iyar (1)" );
				}
				break;

			case HebrewMonth.IYAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Iyar (2)" );
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
					return new RoshChodeshDetails( date, "Tammuz (1)" );
				}
				break;

			case HebrewMonth.TAMMUZ:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Tammuz (2)" );
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
					return new RoshChodeshDetails( date, "Elul (1)" );
				}
				break;

			case HebrewMonth.ELUL:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Elul (2)" );
				}
				break;

			case HebrewMonth.TISHREI:
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Heshvan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Heshvan (1)" );
				}
				break;

			case HebrewMonth.CHESHVAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Heshvan (2)" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Kislev" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Will only happen in a Shalem year
					assert date.isCheshvanLong();
					assert date.getYearType() == HebrewYearType.SHALEM;
					return new RoshChodeshDetails( date, "Kislev (1)" );
				}
				break;

			case HebrewMonth.KISLEV:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Kislev" + ( date.getYearType() == HebrewYearType.SHALEM ? " (2)" : "" ) );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, "Tevet" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					assert !date.isKislevShort();
					HebrewYearType yearType = date.getYearType();
					assert HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType;
					return new RoshChodeshDetails( date, "Tevet (1)" );
				}
				break;

			case HebrewMonth.TEVET:
				if ( 1 == hebrewDayOfMonth ) {
					HebrewYearType yearType = date.getYearType();
					return new RoshChodeshDetails( date, "Tevet" + ( HebrewYearType.SHALEM == yearType || HebrewYearType.QESIDRAH == yearType ? " (2)" : "" ) );
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
					return new RoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar I (1)" : "Adar" );
				}
				break;

			case HebrewMonth.ADAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar I (2)" : "Adar" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date, date.isHebrewLeapYear() ? "Adar II" : "Nissan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Can only happen if this is a leap year
					assert date.isHebrewLeapYear();
					return new RoshChodeshDetails( date, "Adar II (1)" );
				}
				break;

			case HebrewMonth.ADAR_II:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date, "Adar II" + ( date.isHebrewLeapYear() ? " (2)" : "" ) );
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
