/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.minor.PesachSheni;
import com.gindin.zmanim.calendar.holiday.yomtov.Pesach;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Pesach {

	private static final Pesach                               PESACH = new Pesach();
	private static final PesachSheni                          PESACH_SHENI = new PesachSheni();

	@Test
	public void test2010() {
		runPesachTest( 2010, Calendar.MARCH, 30 );
		runPesachSheniTest( 2010, Calendar.APRIL, 28 );
	}

	@Test
	public void test2011() {
		runPesachTest( 2011, Calendar.APRIL, 19 );
		runPesachSheniTest( 2011, Calendar.MAY, 18 );
	}

	@Test
	public void test2012() {
		runPesachTest( 2012, Calendar.APRIL, 7 );
		runPesachSheniTest( 2012, Calendar.MAY, 6 );
	}

	@Test
	public void test2013() {
		runPesachTest( 2013, Calendar.MARCH, 26 );
		runPesachSheniTest( 2013, Calendar.APRIL, 24 );
	}

	@Test
	public void test2014() {
		runPesachTest( 2014, Calendar.APRIL, 15 );
		runPesachSheniTest( 2014, Calendar.MAY, 14 );
	}

	@Test
	public void test2015() {
		runPesachTest( 2015, Calendar.APRIL, 4 );
		runPesachSheniTest( 2015, Calendar.MAY, 3 );
	}

	@Test
	public void test2016() {
		runPesachTest( 2016, Calendar.APRIL, 23 );
		runPesachSheniTest( 2016, Calendar.MAY, 22 );
	}

	private void runPesachTest(
		int year,
		@Constants.CalendarMonth int month,
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


	private void runPesachSheniTest(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Observance.Details  details;

		date = HebrewCalendar.getForDate( year, month, day );
		details = PESACH_SHENI.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.yesterday();
		details = PESACH_SHENI.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
	}

} // End of UT_Pesach class
