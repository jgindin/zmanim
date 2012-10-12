/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import org.junit.*;

/**
 *
 */
public class UT_AbstractHoliday {

	protected UT_AbstractHoliday() {
		// Not instantiatable
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected void checkDate(
		Details   details,
		boolean   isErev,
		boolean   lightCandles,
		boolean   sayHavdalah
	) {

		Assert.assertNotNull( details );

		// The caller may thing that this isn't an erev anything, but actually it's erev Shabbat...
		Assert.assertTrue( isErev == details.isErev() || details.isErevShabbat() );

		// Caller thinks we should (not) light candles, but we have to check:
		//  - If the caller thinks we don't, but actually we do because it's Erev Shabbat?
		Assert.assertTrue(
			( lightCandles == details.lightCandles() ) ||
			( !lightCandles && details.isErevShabbat() )
		);

		// Caller thinks we should (not) say havdalah, but we have to check:
		//  - If the caller thinks we do, but actually we don't because it's Erev Shabbat?
		//  - If the caller thinks we don't, but actually we do because it is Motzae Shabbat?
		Assert.assertTrue(
			( sayHavdalah == details.sayHavdalah() )    ||
			( sayHavdalah && details.isErevShabbat() )  ||
			( !sayHavdalah && details.isShabbat() )
		);

	}

} // End of UT_AbstractHoliday class
