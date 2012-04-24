/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

/**
 * A two day Observance.
 */
public abstract class AbstractCachingTwoDayObservance
	extends AbstractCachingObservance {

	private Details                                           yom2Details;


	protected abstract Details createYom2Details();


	protected final Details getYom2Details() {
		if ( null == yom2Details ) {
			yom2Details = createYom2Details();
		}

		return yom2Details;
	}


	protected static class ErevDetails
		implements Details {

		private final String                                    name;


		public ErevDetails( String name ) {
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


	protected static class YomDetails
		implements Details {

		protected final String                                  name;
		protected final int                                     day;


		public YomDetails(
			String  name,
			int     day
		) {
			this.name = name;
			this.day = day;
		}


		@Override
		public boolean isErev() {
			return false;
		}


		@Override
		public boolean lightCandles() {
			// TODO: In Israel, this is false.
			return ( 1 == day );
		}


		@Override
		public boolean sayHavdalah() {
			// TODO: In Israel, this would be 1 == day.
			return ( 2 == day );
		}


		@Override
		public String getDescription() {
			return name;
		}
	}


}  // End of AbstractCachingTwoDayObservance class
