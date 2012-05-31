/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;

/**
 * Represents an observance of some kind in the Jewish Calendar.
 */
public abstract class Observance {

	protected static final String                             EREV = "Erev ";

	static final String                               CHOL_HA_MOED = "Chol HaMoed ";


	public abstract String getName();

	/**
	 * If the observance falls on the specified date, returns details about that
	 *  particular instance of the observance.
	 */
	public abstract Details getDetailsFor( HebrewDate date );


	// todo: Have to move away from having static, cached details. Instead, have to have new instances
	// todo:  which are specific to the date they're created for. This way, we can get a proper answer
	// todo:  for whether we light candles or say havdalah...
	//
	// todo: Probably want to make lightCandles() and sayHavadalah() final. If it's Friday, then
	// todo:  lightCandles() is ALWAYS true. If it's Saturday, then sayHavdalah is only true if
	// todo:  the subclass says?
	public static abstract class Details {

		private final HebrewDate                                date;


		protected Details( HebrewDate date ) {
			this.date = date;
		}


		/**
		 * Is this actually erev of the observance? If so, today is NOT actually the
		 *  observance.
		 */
		public abstract boolean isErev();

		/**
		 * Does one light candles tonight for this observance? This could be the first night
		 *  of the observance, or in some cases the second or subsequent evenings too.
		 */
		public abstract boolean lightCandles();

		/**
		 * Does one say havdalah tonight at the end of this observance? Not all observances end
		 *  with havdalah.
		 */
		public abstract boolean sayHavdalah();

		/**
		 * A complete description of today's observance. Could be something simple like,
		 *  "Yom Kippur" or something more complex like "Pesach 4 Chol HaMoed / 3rd Day of the Omer"
		 */
		public abstract String getDescription();
	}


	protected static class ErevDetails
		extends Details {

		private final String                                    name;


		public ErevDetails(
			HebrewDate  date,
			String      name
		) {
			super( date );
			this.name = name;
		}


		@Override
		public boolean isErev() {
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
			return EREV + name;
		}
	}

	protected static class YomTovDetails
		extends Details {

		protected final String                                  name;
		private boolean                                         lightCandles;


		public YomTovDetails(
			HebrewDate date,
			String      name,
			boolean     lightCandles
		) {
			super( date );
			this.name = name;
			this.lightCandles = lightCandles;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return lightCandles;
		}


		@Override
		public boolean sayHavdalah() {
			return !lightCandles;
		}


		@Override
		public String getDescription() {
			return name;
		}
	}

	protected static class CholHaMoedDetails
		extends Details {

		/** This is the day of the chag, NOT the day of chol hamoed. */
		private int                                             dayOfChag;
		private String                                          name;
		private boolean                                         lightCandles;


		public CholHaMoedDetails(
			HebrewDate  date,
			int dayOfChag,
			String      name,
			boolean     lightCandles
		) {
			super( date );
			this.dayOfChag = dayOfChag;
			this.name = name;
			this.lightCandles = lightCandles;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return lightCandles;
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return CHOL_HA_MOED + name + " " + dayOfChag;
		}
	}
}  // End of Observance class
