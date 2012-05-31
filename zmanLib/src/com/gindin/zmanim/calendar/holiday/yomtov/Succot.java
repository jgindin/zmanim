/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Succot
	extends Observance {

	private static final String                               SUKKOT = "Sukkot";
	private static final String                               HOSHANA_RABBA = "Hoshana Rabba";
	private static final String                               SHMINI_ATZERET = "Shmini Atzeret";
	private static final String                               SIMCHAT_TORAH = "Simchat Torah";



	@Override
	public String getName() {
		return SUKKOT;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		if ( HebrewMonth.TISHREI == date.getHebrewMonth() ) {
			final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

			switch ( hebrewDayOfMonth ) {
				case 14:
					return new ErevDetails( date, SUKKOT );

				case 15:
					return new YomTovDetails( date, SUKKOT, true );

				case 16:
					return new YomTovDetails( date, SUKKOT, false );

				case 17:
				case 18:
				case 19:
				case 20:
					return new CholHaMoedDetails( date, hebrewDayOfMonth - 14, SUKKOT, false );

				case 21:
					return new HoshanaRabbaDetails( date );

				case 22:
					return new YomTovDetails( date, SHMINI_ATZERET, true );

				case 23:
					return new YomTovDetails( date, SIMCHAT_TORAH, false );
			}
		}

		return null;
	}


	private static final class HoshanaRabbaDetails
		extends Details {

		private HoshanaRabbaDetails( HebrewDate date ) {
			super( date );
		}


		@Override
		public boolean isErev() {
			// This is Erev Shmini Atzeret...
			return true;
		}


		@Override
		public boolean lightCandles() {
			return true;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return SUKKOT + " 7 (" + HOSHANA_RABBA + ")";
		}
	}

}  // End of Succot class
