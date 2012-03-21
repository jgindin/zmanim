/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

/**
 * An Observance which is able to cache (and, therefore, reuse) its details.
 */
public abstract class AbstractCachingObservance
	implements Observance {

	private static Details                                    erevDetails;
	private static Details                                    yomDetails;


	protected abstract Details createErevDetails();

	protected abstract Details createYomDetails();


	protected final Details getErevDetails() {
		if ( null == erevDetails ) {
			erevDetails = createErevDetails();
		}

		return erevDetails;
	}


	protected final Details getYomDetails() {
		if ( null == yomDetails ) {
			yomDetails = createYomDetails();
		}

		return yomDetails;
	}

}  // End of AbstractCachingObservance class
