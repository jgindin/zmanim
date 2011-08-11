/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Fast days:
 *  - Gedaliah
 *  - 10 Tevet
 *  - Esther
 *  - 17 Tammuz
 *  - Tisha B'Av
 *
 * These fast days may move around on the calendar such that, none of them can fall on Shabbat,
 *  and none, except 10 Tevet, can fall on Friday.
 *<p>
 * Note: The fact that these fast days push off to Sunday is from http://www.jewishgen.org/infofiles/m_calint.htm
 */
@SuppressWarnings( { "MagicNumber" } )
public class Taanit
	extends AdjustableDateObservance {

	private static final Taanit                               GEDALIAH  = new Taanit( HebrewCalendar.TISHRI,  3,      "Gedaliah" );
	private static final Taanit                               TEVET_10  = new Taanit( HebrewCalendar.TEVET,  10,      "Tevet 10" );
	private static final Taanit                               ESTHER    = new TaanitEsther();
	private static final Taanit                               TAMMUZ_17 = new Taanit( HebrewCalendar.TAMUZ,  17,      "Tammuz 17" );
	private static final Taanit                               TISHA_BAV = new Taanit( HebrewCalendar.AV,      9,      "Tisha B'Av" );

	public static final Taanit[]                              TAANIOT = { GEDALIAH, TEVET_10, ESTHER, TAMMUZ_17, TISHA_BAV };



	Taanit(
		int month,
		int date,
		String name
	) {
		super( month, date, "Ta'anit " + name );
	}


	@Override
	public boolean isTaanit() {
		return true;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	protected int getDateAdjustment( HebrewCalendar dateToTest ) {

		// Update the calendar for the test...
		calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

		// Cannot fall out on Shabbat! If this Shabbat, then move it to Sunday.
		int adjustment = 0;

		final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
		if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
			adjustment = 1;
		}

		return adjustment;
	}


	/**
	 * Special class to handle the date changes associated with Ta'anit Esther.
	 */
	public static class TaanitEsther
		extends Taanit {

		TaanitEsther() {
			super( HebrewCalendar.ADAR, 13, "Esther" );
		}


		@SuppressWarnings( { "FeatureEnvy" } )
		protected int getDateAdjustment( HebrewCalendar dateToTest ) {

			// Update the calendar for the test...
			calendar.set( dateToTest.get( HebrewCalendar.YEAR ), month, date );

			int adjustment = 0;

			// Now, if the fast is supposed to fall out on a Friday or Shabbat, move it back to Thursday.
			final int dayOfWeek = calendar.get( HebrewCalendar.DAY_OF_WEEK );
			if ( HebrewCalendar.FRIDAY == dayOfWeek ) {
				adjustment = -1;
			}
			else if ( HebrewCalendar.SATURDAY == dayOfWeek ) {
				adjustment = -2;
			}

			return adjustment;
		}

	} // End of TaanitEsther class
} // End of Taanit
