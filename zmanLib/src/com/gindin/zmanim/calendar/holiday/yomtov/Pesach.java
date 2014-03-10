/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.Details;
import com.gindin.zmanim.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Pesach
	extends Observance {

	private static final String PESACH = "Pesach";


	@Override
	public String getName() {
		return PESACH;
	}


	@Override
	public Details getDetailsFor( HebrewDate date ) {

		final int hebrewMonth = date.getHebrewMonth();

		if ( HebrewMonth.NISSAN != hebrewMonth ) {
			return null;
		}

		final int hebrewDayOfMonth = date.getHebrewDayOfMonth();
		switch ( hebrewDayOfMonth ) {
			case 14:
				return new ErevPesachDetails( date );

			case 15:
				return new PesachYomTovDetails( date, true );

			case 16:
				return new PesachYomTovDetails( date, false );

			case 17:
			case 18:
			case 19:
				return new PesachCholHaMoedDetails( date, hebrewDayOfMonth - 14, false );

			case 20:
				return new PesachCholHaMoedDetails( date, hebrewDayOfMonth - 14, true );

			case 21:
				return new PesachYomTovDetails( date, true );

			case 22:
				return new PesachYomTovDetails( date, false );
		}

		return null;
	}


	protected static class ErevPesachDetails
		extends Details.Erev {

		ErevPesachDetails(
			HebrewDate  date
		) {
			super( date, PESACH );
		}


		@Override
		public boolean isPesach() {
			return true;
		}
	}


	protected static class PesachYomTovDetails
		extends Details.YomTovDetails {

		PesachYomTovDetails(
			HebrewDate  date,
			boolean     lightCandles
		) {
			super( date, PESACH, lightCandles );
		}


		@Override
		public boolean isPesach() {
			return true;
		}
	}


	protected static class PesachCholHaMoedDetails
		extends Details.CholHaMoedDetails {

		PesachCholHaMoedDetails(
			HebrewDate  date,
			int         dayOfChag,
			boolean     lightCandles
		) {
			super( date, dayOfChag, PESACH, lightCandles );
		}


		@Override
		public boolean isPesach() {
			return true;
		}
	}

}  // End of Pesach class
