/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


	public boolean isRoshChodesh() {
		return false;
	}

	public boolean isTaanit() {
		return false;
	}

	public boolean isYomTov() { return false; }

	public boolean isCholHaMoed() { return false; }

	public boolean isPesach() {
		return false;
	}

	public boolean isSukkot() { return false; }

	public boolean isShminiAtzeret() { return false; }

	public boolean isSimchatTorah() { return false; }

	public boolean isShavuot() { return false; }

	public boolean isPurim() { return false; }

	public boolean isChanukah() { return false; }

	public boolean isRoshHashana() { return false; }

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


	public boolean isShabbat() {

		Details shabbatDetails = Holidays.SHABBAT.getDetailsFor( date );
		return null != shabbatDetails && !shabbatDetails.isErev();
	}


	/**
	 * Is this a day of the omer?
	 */
	protected boolean isOmer() {
		return false;
	}


	protected final HebrewDate getTodaysDate() {
		return date;
	}


	public static class Erev
		extends Details {

		private final String                                    name;

		private boolean                                         candles;


		public Erev(
			HebrewDate  date,
			String      name
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
			// No candle lighting if we're coming straight out of Shabbat...
			return ( candles && !isShabbat() ) || isErevShabbat();
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


	public static class ErevYomTov
		extends Erev {

		public ErevYomTov(
			HebrewDate  date,
			String      name
		) {
			super( date, name );
		}


		public ErevYomTov(
			HebrewDate  date,
			String      name,
			boolean     candles
		) {
			super( date, name, candles );
		}


		@Override
		public boolean sayHavdalah() {
			// Erev of a holiday doesn't get havdalah, even when the erev is motsa'e Shabbat.
			return false;
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
		public boolean isYomTov() {
			return true;
		}


		@Override
		public boolean lightCandles() {

			// If we don't want to light candles for this yom tov, but it's erev Shabbat, then we DO light candles.
			//  Of course, if it is Shabbat then we do not light...
			return ( !isShabbat() && ( lightCandles || isErevShabbat() ) );
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


		@Override
		public boolean isCholHaMoed() {
			return true;
		}
	}


	private static class CompositeDetails
		extends Details {

		/**
		 * This comparator is used to arrange the dates which are composed into this one date.
		 *  The way CompositeDetails is used, we can assert the following.
		 *  Every Details represented in this composite:
		 *    <ul>
		 *      <li>is for the SAME date
		 *      <li>represents *something*, whether it be Erev or actual Yom.
		 *    </ul>
		 */
		private static final Comparator<Details>                COMPARATOR = new Comparator<Details>() {

			/**
			 * @return an integer < 0 if lhs is less than rhs, 0 if they are equal, and > 0 if lhs is greater than rhs
			 */
			@Override
			public int compare(
				Details lhs,
				Details rhs
			) {

				if ( !lhs.isAnything() && !rhs.isAnything() ) {
					// Both are nothing... This should not happen.
					return 0;
				}

				if ( lhs.isAnything() && !rhs.isAnything() ) {
					// The one that has something should come first. This should not happen.
					return -1;
				}

				if ( !lhs.isAnything() && rhs.isAnything() ) {
					// The one that has something should come first. This should not happen.
					return 1;
				}

				// OK, so now we KNOW that both are *something*.
				//  * Omer should always come LAST
				if ( lhs.isOmer() && !rhs.isOmer() ) {
					return 1;
				}

				if ( rhs.isOmer() && !lhs.isOmer() ) {
					return -1;
				}

				// We always want the erev of something to come AFTER a day which *is* something itself.
				if ( lhs.isErev() && rhs.isErev() ) {
					return 0;
				}

				if ( lhs.isErev() && !rhs.isErev() ) {
					return 1;
				}

				if ( !lhs.isErev() && rhs.isErev() ) {
					return -1;
				}

				return 0;
			}
		};


		/** The details of the dates that are composed into this. */
		private final List<Details>                             composedDates = new ArrayList<Details>();

		/**
		 * There are cases where the only way to determine something about today is by looking at tomorrow.
		 *  E.g., should we say havdalah? Well, if today is Shabbat, then yes. Unless it's the first day
		 *    of Pesach or Sukkot, that is...
		 *  This field will be filledin lazily if it's needed.
		 */
		private Details                                         tomorrowsDetails;


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
		public boolean isYomTov() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isYomTov() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isCholHaMoed() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isCholHaMoed() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean lightCandles() {

			boolean light = false;
			for ( Details composedDate : composedDates ) {
				if ( composedDate.lightCandles() ) {
					light = true;
					break;
				}
			}

			// No candle lighting if we're coming straight out of Shabbat...
			return light && !isShabbat();
		}


		@Override
		public boolean sayHavdalah() {

			boolean havdalah = false;
			for ( Details composedDate : composedDates ) {
				if ( composedDate.sayHavdalah() ) {
					havdalah = true;
					break;
				}
			}

			// If we want to say havdalah, but it's erev Shabbat, then we don't say havdalah.
			// Also, if tomorrow is something (e.g., 2nd day of Pesach or Sukkot), then we also don't want to say
			//  havdalah.
			if ( tomorrowsDetails == null ) {
				tomorrowsDetails = getTodaysDate().tomorrow().getDetails();
			}

			return ( havdalah && !isErevShabbat() && !tomorrowsDetails.isYomTov() );
		}


		@Override
		public boolean isRoshChodesh() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isRoshChodesh() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isShabbat() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isShabbat() ) {
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
		public boolean isPesach() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isPesach() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isSukkot() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isSukkot() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isShminiAtzeret() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isShminiAtzeret() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isSimchatTorah() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isSimchatTorah() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isShavuot() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isShavuot() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isPurim() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isPurim() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isChanukah() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isChanukah() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public boolean isRoshHashana() {
			for ( Details composedDate : composedDates ) {
				if ( composedDate.isRoshHashana() ) {
					return true;
				}
			}

			return false;
		}


		@Override
		public String getDescription() {
			StringBuilder sb = new StringBuilder();

			Collections.sort( composedDates, COMPARATOR );

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
	} // End of CompositeDetails class

} // End of Details class
