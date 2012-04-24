/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

/**
 * An observance that spans a number of days with Chol HaMoed intervening.
 */
public abstract class AbstractCachingObservanceWithCholHaMoed
	extends AbstractCachingTwoDayObservance {

	private static final String                               CHOL_HA_MOED = "Chol HaMoed ";

	private Details                                           penultimateYomDetails;
	private Details                                           finalYomDetails;



	protected abstract Details createPenultimateYomDetails();
	protected abstract Details createFinalYomDetails();


	protected Details getPenultimateYomDetails() {
		if ( null == penultimateYomDetails ) {
			penultimateYomDetails = createPenultimateYomDetails();
		}

		return penultimateYomDetails;
	}


	protected Details getFinalYomDetails() {
		if ( null == finalYomDetails ) {
			finalYomDetails = createFinalYomDetails();
		}

		return finalYomDetails;
	}


	protected static final class CholHaMoedDetails
		implements Details {

		/** This is the day of the chag, NOT the day of chol hamoed. */
		private int                                             day;
		private String                                          name;


		public CholHaMoedDetails( int day, String name ) {
			this.day = day;
			this.name = name;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			// TODO: Have to override for dealing with Sukkot where, at least outside of Israel, this isn't right.
			return ( 6 == day );
		}


		@Override
		public boolean sayHavdalah() {
			return false;
		}


		@Override
		public String getDescription() {
			return CHOL_HA_MOED + name + " " + day;
		}
	}


	protected static class LastDaysDetails
		extends YomDetails {

		public LastDaysDetails(
			String  name,
			int     day
		) {
			super( name, day );
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			// TODO: In Israel, this would always be false;
			// TODO: Have to override for dealing with Sukkot where, at least outside of Israel, this isn't right.
			return ( 7 == day );
		}


		@Override
		public boolean sayHavdalah() {
			// TODO: In Israel, this would be 7 == day.
			// TODO: Have to override for dealing with Sukkot where, at least outside of Israel, this isn't right.
			return ( 8 == day );
		}
	}

}  // End of AbstractCachingObservanceWithCholHaMoed class
