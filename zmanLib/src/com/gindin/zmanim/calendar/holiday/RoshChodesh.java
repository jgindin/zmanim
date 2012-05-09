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
	implements Observance {

	private static final String                               ROSH_HODESH = "Rosh Hodesh";

	@Override
	public String getName() {
		return ROSH_HODESH;
	}


	@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
	@Override
	public Details getDetailsFor( HebrewDate date ) {

		int hebrewMonth = date.getHebrewMonth();
		int hebrewDayOfMonth = date.getHebrewDayOfMonth();

		switch ( hebrewMonth ) {
			case HebrewMonth.NISSAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Nissan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Iyar" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Iyar" );
				}
				break;

			case HebrewMonth.IYAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Iyar" );
				}
				else if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Sivan" );
				}
				break;

			case HebrewMonth.SIVAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Sivan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Tammuz" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Tammuz" );
				}
				break;

			case HebrewMonth.TAMMUZ:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Tammuz" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Av" );
				}
				break;

			case HebrewMonth.AV:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Av" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Elul" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Elul" );
				}
				break;

			case HebrewMonth.ELUL:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Elul" );
				}
				break;

			case HebrewMonth.TISHREI:
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Heshvan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Heshvan" );
				}
				break;

			case HebrewMonth.CHESHVAN:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Heshvan" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Kislev" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Will only happen in a Shalem year
					assert date.isCheshvanLong();
					assert date.getYearType() == HebrewYearType.SHALEM;
					return new RoshChodeshDetails( "Kislev" );
				}
				break;

			case HebrewMonth.KISLEV:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Kislev" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Tevet" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					assert !date.isKislevShort();
					assert date.getYearType() == HebrewYearType.SHALEM || date.getYearType() == HebrewYearType.QESIDRAH;
					return new RoshChodeshDetails( "Tevet" );
				}
				break;

			case HebrewMonth.TEVET:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Tevet" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Shevat" );
				}
				break;

			case HebrewMonth.SHEVAT:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Shevat" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				break;

			case HebrewMonth.ADAR:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( date.isHebrewLeapYear() ? "Adar I" : "Adar" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( date.isHebrewLeapYear() ? "Adar II" : "Nissan" );
				}
				if ( 30 == hebrewDayOfMonth ) {
					// Can only happen if this is a leap year
					assert date.isHebrewLeapYear();
					return new RoshChodeshDetails( "Adar II" );
				}
				break;

			case HebrewMonth.ADAR_II:
				if ( 1 == hebrewDayOfMonth ) {
					return new RoshChodeshDetails( "Adar II" );
				}
				if ( 29 == hebrewDayOfMonth ) {
					return new ErevRoshChodeshDetails( "Nissan" );
				}
				break;
		}

		return null;
	}


	private static final class ErevRoshChodeshDetails
		implements Details {

		private final String                                    month;


		ErevRoshChodeshDetails( String month ) {
			this.month = month;
		}


		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return EREV + ROSH_HODESH + " " + month;
		}
	}


	private static final class RoshChodeshDetails
		implements Details {

		private final String                                    month;


		RoshChodeshDetails( String month ) {
			this.month = month;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return false;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return ROSH_HODESH + " " + month;
		}
	}

}  // End of RoshChodesh class
