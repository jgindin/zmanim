/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewCalendar;

/**
 * Describes the 49 days of the Omer.
 */
@SuppressWarnings( { "MagicNumber" } )
public abstract class Omer<T extends Omer>
	extends MultiDayObservance {

	/**
	 * The omer count shows up as a "holiday".
	 */
	private static final Omer                                 NISAN_OMER = new Omer.NisanOmer();
	private static final Omer                                 IYAR_OMER = new Omer.IyarOmer();
	private static final Omer                                 SIVAN_OMER = new Omer.SivanOmer();

	public static  final Omer[]                               OMER = { NISAN_OMER, IYAR_OMER, SIVAN_OMER };


	Omer(
		int month,
		int date,
		int length,
		HebrewCalendar forDate
	) {
		super( month, date, length, "Omer", forDate );
	}


	@Override
	public JewishDay createForDate( HebrewCalendar hebrewCalendar ) {
		return newOmerForDate( hebrewCalendar );
	}


	protected abstract T newOmerForDate( HebrewCalendar hebrewCalendar );


	static class NisanOmer
		extends Omer<NisanOmer> {

		NisanOmer() {
			this( null );
		}

		NisanOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.NISAN, 16, 14, forDate );
		}


		@Override
		protected NisanOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new NisanOmer( hebrewCalendar );
		}
	}

	static class IyarOmer
		extends Omer<IyarOmer> {

		IyarOmer() {
			this( null );
		}


		IyarOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.IYAR, 1, 29, forDate );
		}


		@Override
		protected int getCountOffset() {
			// Already counted the omer for the last 15 days of Nisan.
			return 15;
		}


		@Override
		protected IyarOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new IyarOmer( hebrewCalendar );
		}
	}

	static class SivanOmer
		extends Omer<SivanOmer> {

		SivanOmer() {
			this( null );
		}


		SivanOmer(
			HebrewCalendar forDate
		) {
			super( HebrewCalendar.SIVAN, 1, 5, forDate );
		}


		@Override
		protected int getCountOffset() {
			// Already counted the omer for the last 15 days of Nisan and all of Iyar.
			return 15 + 29;
		}


		@Override
		protected SivanOmer newOmerForDate( HebrewCalendar hebrewCalendar ) {
			return new SivanOmer( hebrewCalendar );
		}
	}

}
