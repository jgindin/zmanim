/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.yomtov.Pesach;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
public class UT_Pesach {

	private static final Pesach                               PESACH = new Pesach();

	@Test
	public void test2010() {
		runTest( 2010, Calendar.MARCH, 30 );
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.APRIL, 19 );
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.APRIL, 7 );
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.MARCH, 26 );
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.APRIL, 15 );
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.APRIL, 4 );
	}

	@Test
	public void test2016() {
		runTest( 2016, Calendar.APRIL, 23 );
	}

	private void runTest(
		int year,
	  @Constants.CalendarMonth  int month,
	  int day
	) {

		HebrewDate date;
		Observance.Details  details;

		date = HebrewCalendar.getForDate( year, month, day );

		// Erev Pesach
		date = date.yesterday();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach I
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach II
		// TODO: Handle Israel...
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertTrue( details.sayHavdalah() );

		// Pesach III
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach IV
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );


		// Pesach V
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach VI
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach VII
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Pesach VIII
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertTrue( details.sayHavdalah() );

	}

} // End of UT_Pesach class
