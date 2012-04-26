/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.yomtov.Shavuot;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Shavuot {

	private static final Shavuot                              SHAVUOT = new Shavuot();

	@Test
	public void test2010() {
		runTest( 2010, Calendar.MAY, 19 );
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.JUNE, 8 );
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.MAY, 27 );
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.MAY, 15 );
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.JUNE, 4 );
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.MAY, 24 );
	}

	@Test
	public void test2016() {
		runTest( 2016, Calendar.JUNE, 12 );
	}

	private void runTest(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Observance.Details  details;

		date = HebrewCalendar.getForDate( year, month, day );
		details = SHAVUOT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = SHAVUOT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertTrue( details.sayHavdalah() );

		date = date.yesterday().yesterday();
		details = SHAVUOT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

	}

} // End of UT_Shavuot class
