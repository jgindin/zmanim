/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.HebrewDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details about a given Observance.
 */
public abstract class Details {

	protected static final String                             EREV = "Erev ";

	private static final String                               CHOL_HA_MOED = "Chol HaMoed ";

	private final HebrewDate                                  date;


	public Details( HebrewDate date ) {
		this.date = date;
	}


	/**
	 * Is this actually erev of the observance? If so, today is NOT actually the observance.
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

	public boolean isTaanit() {
		return false;
	}

	/**
	 * A complete description of today's observance. Could be something simple like,
	 *  "Yom Kippur" or something more complex like "Pesach 4 Chol HaMoed / 3rd Day of the Omer"
	 */
	public abstract String getDescription();


	/**
	 * A non-null Details may still not be anything interesting.
	 */
	public boolean isAnything() {
		// If someone wants to create a 'nothing' details, they'll have to override this.
		return true;
	}

	public Details composeWith( Details otherDetails ) {
		return new CompositeDetails( date, this, otherDetails );
	}


	protected final boolean isErevShabbat() {

		Details shabbatDetails = Holidays.SHABBAT.getDetailsFor( date );
		return null != shabbatDetails && shabbatDetails.isErev();
	}


	protected final boolean isShabbat() {

		Details shabbatDetails = Holidays.SHABBAT.getDetailsFor( date );
		return null != shabbatDetails && !shabbatDetails.isErev();
	}


	public static class Erev
		extends Details {

		private final String                                    name;

		private boolean                                         candles;


		public Erev(
			HebrewDate date,
			String name
		) {
			this( date, name, true );
		}

		public Erev(
			HebrewDate  date,
			String      name,
			boolean     candles
		) {
			super( date );
			this.name = name;
			this.candles = candles;
		}


		@Override
		public boolean isErev() {
			return true;
		}


		@Override
		public boolean lightCandles() {
			return candles;
		}


		@Override
		public boolean sayHavdalah() {

			// Could be that Shabbat is erev something...
			return isShabbat();
		}


		@Override
		public String getDescription() {
			return EREV + name;
		}
	}


	/**
	 * Intended for simple details about a day.
	 */
	public static class Yom
		extends Details {

		protected final String                                  name;


		public Yom(
			HebrewDate  date,
			String      name
		) {
			super( date );
			this.name = name;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			return isErevShabbat();
		}


		@Override
		public boolean sayHavdalah() {
			return isShabbat();
		}


		@Override
		public String getDescription() {
			return name;
		}
	}


	public static class YomTovDetails
		extends Details {

		protected final String                                  name;
		private final boolean                                   lightCandles;


		public YomTovDetails(
			HebrewDate  date,
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

			// If we don't want to light candles for this yom tov, but it's erev Shabbat, then we DO light candles.
			return ( lightCandles || isErevShabbat() );
		}


		@Override
		public boolean sayHavdalah() {

			// If we want to say havdalah for this yom tov, but it's erev Shabbat, then we don't say havdalah.
			return ( !lightCandles && !isErevShabbat() );
		}


		@Override
		public String getDescription() {
			return name;
		}
	}

	public static class CholHaMoedDetails
		extends Details {

		/** This is the day of the chag, NOT the day of chol hamoed. */
		private int                                             dayOfChag;
		private String                                          name;
		private boolean                                         lightCandles;


		public CholHaMoedDetails(
			HebrewDate  date,
			int         dayOfChag,
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
			return lightCandles || isErevShabbat();
		}


		@Override
		public boolean sayHavdalah() {
			return isShabbat();
		}


		@Override
		public String getDescription() {
			return CHOL_HA_MOED + name + " " + dayOfChag;
		}
	}


	private static class CompositeDetails
		extends Details {

		private final List<Details>                             composedDates = new ArrayList<Details>();

		CompositeDetails(
			HebrewDate  date,
			Details     details,
			Details     otherDetails
		) {
			super( date );
			composeWith( details );
			composeWith( otherDetails );
		}


		@Override
		public boolean isErev() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isErev() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean lightCandles() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.lightCandles() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean sayHavdalah() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.sayHavdalah() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isTaanit() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isTaanit() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public String getDescription() {
			StringBuilder sb = new StringBuilder();

			for ( Details composedDate : composedDates ) {
				String composedDateDescription = composedDate.getDescription();

				if ( ( sb.length() > 0 ) && ( composedDateDescription.length() > 0 ) ) {
					sb.append( " / " );
				}
				sb.append( composedDateDescription );
			}

			return sb.toString();
		}


		@Override
		public Details composeWith( Details otherDetails ) {
			composedDates.add( otherDetails );
			return this;
		}


		@Override
		public boolean isAnything() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isAnything() ) {
					return true;
				}
			}

			return false;
		}
	}
}
