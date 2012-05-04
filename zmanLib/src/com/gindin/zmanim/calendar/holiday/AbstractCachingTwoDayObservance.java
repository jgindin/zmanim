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
		private boolean                                         lightCandles;


		public YomDetails(
			String  name,
			boolean lightCandles
		) {
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


}  // End of AbstractCachingTwoDayObservance class
