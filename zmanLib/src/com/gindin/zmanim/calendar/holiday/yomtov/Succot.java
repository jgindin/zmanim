/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday.yomtov;

import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.AbstractCachingObservanceWithCholHaMoed;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class Succot
	extends AbstractCachingObservanceWithCholHaMoed {

	private static final String                               SUKKOT = "Sukkot";
	private static final String                               HOSHANA_RABBA = "Hoshana Rabba";
	private static final String                               SHMINI_ATZERET = "Shmini Atzeret";
	private static final String                               SIMCHAT_TORAH = "Simchat Torah";

	private ErevHoshanaRabbaDetails                           erevHoshanaRabbaDetails;
	private HoshanaRabbaDetails                               hoshanaRabbaDetails;


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
					return getErevDetails();

				case 15:
					return getYomDetails();

				case 16:
					return getYom2Details();

				case 17:
				case 18:
				case 19:
					return new AbstractCachingObservanceWithCholHaMoed.CholHaMoedDetails( hebrewDayOfMonth - 14, SUKKOT, false );

				case 20:
					return getErevHoshanaRabbaDetails();

				case 21:
					return getHoshanaRabbaDetails();

				case 22:
					return getPenultimateYomDetails();

				case 23:
					return getFinalYomDetails();
			}
		}

		return null;
	}


	@Override
	protected Details createErevDetails() {
		return new ErevDetails( SUKKOT );
	}


	@Override
	protected Details createYomDetails() {
		return new YomDetails( SUKKOT, true );
	}


	@Override
	protected Details createYom2Details() {
		return new YomDetails( SUKKOT, false );
	}


	@Override
	protected Details createPenultimateYomDetails() {
		return new YomDetails( SHMINI_ATZERET, true );
	}


	@Override
	protected Details createFinalYomDetails() {
		return new YomDetails( SIMCHAT_TORAH, false );
	}


	private Details getErevHoshanaRabbaDetails() {
		if ( null == erevHoshanaRabbaDetails ) {
			erevHoshanaRabbaDetails = new ErevHoshanaRabbaDetails();
		}

		return erevHoshanaRabbaDetails;
	}


	private Details getHoshanaRabbaDetails() {
		if ( null == hoshanaRabbaDetails ) {
			hoshanaRabbaDetails = new HoshanaRabbaDetails();
		}

		return hoshanaRabbaDetails;
	}


	private static final class ErevHoshanaRabbaDetails
		extends CholHaMoedDetails {

		private ErevHoshanaRabbaDetails() {
			super( 6, SUKKOT + " 6 / " + HOSHANA_RABBA, false );
		}
	}


	private static final class HoshanaRabbaDetails
		implements Details {

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
			return SUKKOT + " 7 / " + HOSHANA_RABBA + " / " + EREV + SHMINI_ATZERET;
		}
	}

}  // End of Succot class
