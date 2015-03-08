/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Sukkot
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
					return new Details.ErevYomTov( date, SUKKOT );

				case 15:
					return new Details.YomTovDetails( date, SUKKOT, true );

				case 16:
					return new Details.YomTovDetails( date, SUKKOT, false );

				case 17:
				case 18:
				case 19:
				case 20:
					return new Details.CholHaMoedDetails( date, hebrewDayOfMonth - 14, SUKKOT, false );

				case 21:
					return new HoshanaRabbaDetails( date );

				case 22:
					return new Details.YomTovDetails( date, SHMINI_ATZERET, true );

				case 23:
					return new Details.YomTovDetails( date, SIMCHAT_TORAH, false );
			}
		}

		return null;
	}


	private static final class HoshanaRabbaDetails
		extends Details.ErevYomTov {

		HoshanaRabbaDetails( HebrewDate date ) {
			super( date, HOSHANA_RABBA );
		}


		@Override
		public String getDescription() {
			return SUKKOT + " 7 (" + HOSHANA_RABBA + ")";
		}
	}

}  // End of Sukkot class
