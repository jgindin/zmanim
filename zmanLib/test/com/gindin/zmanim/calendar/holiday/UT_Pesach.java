/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.minor.LagBOmer;
import com.gindin.zmanim.calendar.holiday.minor.Omer;
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
	private static final Omer                                 OMER = new Omer();
	private static final LagBOmer                             LAG_B_OMER = new LagBOmer();

	@Test
	public void test2010() {
		assertPesach( 2010, Calendar.MARCH, 30 );
		assertPesachSheni( 2010, Calendar.APRIL, 28 );

		assertOmer( 2010, Calendar.MARCH, 31, 1 );
		assertOmer( 2010, Calendar.APRIL, 4, 5 );
		assertOmer( 2010, Calendar.APRIL, 19, 20 );
		assertOmer( 2010, Calendar.MAY, 1, 32 );
		assertOmer( 2010, Calendar.MAY, 18, 49 );

		assertNotOmer( 2010, Calendar.MARCH, 30 );
		assertNotOmer( 2010, Calendar.MAY, 19 );

		assertLagBOmer( 2010, Calendar.MAY, 2 );
	}

	@Test
	public void test2011() {
		assertPesach( 2011, Calendar.APRIL, 19 );
		assertPesachSheni( 2011, Calendar.MAY, 18 );

		assertOmer( 2011, Calendar.APRIL, 21, 2 );
		assertOmer( 2011, Calendar.APRIL, 28, 9 );
		assertOmer( 2011, Calendar.MAY, 11, 22 );
		assertOmer( 2011, Calendar.MAY, 30, 41 );
		assertOmer( 2011, Calendar.JUNE, 3, 45 );

		assertNotOmer( 2011, Calendar.APRIL, 18 );
		assertNotOmer( 2011, Calendar.APRIL, 19 );
		assertNotOmer( 2011, Calendar.JUNE, 8 );
		assertNotOmer( 2011, Calendar.JULY, 4 );

		assertLagBOmer( 2011, Calendar.MAY, 22 );
	}

	@Test
	public void test2012() {
		assertPesach( 2012, Calendar.APRIL, 7 );
		assertPesachSheni( 2012, Calendar.MAY, 6 );

		assertOmer( 2012, Calendar.APRIL, 8, 1 );
		assertOmer( 2012, Calendar.APRIL, 25, 18 );
		assertOmer( 2012, Calendar.MAY, 2, 25 );
		assertOmer( 2012, Calendar.MAY, 7, 30 );
		assertOmer( 2012, Calendar.MAY, 19, 42 );
		assertOmer( 2012, Calendar.MAY, 25, 48 );

		assertNotOmer( 2012, Calendar.APRIL, 7 );
		assertNotOmer( 2012, Calendar.MAY, 27 );

		assertLagBOmer( 2012, Calendar.MAY, 10 );
	}

	@Test
	public void test2013() {
		assertPesach( 2013, Calendar.MARCH, 26 );
		assertPesachSheni( 2013, Calendar.APRIL, 24 );

		assertOmer( 2013, Calendar.MARCH, 27, 1 );
		assertOmer( 2013, Calendar.APRIL, 1, 6 );
		assertOmer( 2013, Calendar.APRIL, 10, 15 );
		assertOmer( 2013, Calendar.APRIL, 11, 16 );
		assertOmer( 2013, Calendar.MAY, 9, 44 );
		assertOmer( 2013, Calendar.MAY, 10, 45 );

		assertNotOmer( 2013, Calendar.MARCH, 20 );
		assertNotOmer( 2013, Calendar.MARCH, 25 );
		assertNotOmer( 2013, Calendar.MAY, 16 );

		assertLagBOmer( 2013, Calendar.APRIL, 28 );
	}

	@Test
	public void test2014() {
		assertPesach( 2014, Calendar.APRIL, 15 );
		assertPesachSheni( 2014, Calendar.MAY, 14 );

		assertOmer( 2014, Calendar.APRIL, 23, 8 );
		assertOmer( 2014, Calendar.APRIL, 30, 15 );
		assertOmer( 2014, Calendar.MAY, 1, 16 );
		assertOmer( 2014, Calendar.MAY, 30, 45 );
		assertOmer( 2014, Calendar.JUNE, 3, 49 );

		assertNotOmer( 2014, Calendar.APRIL, 14 );
		assertNotOmer( 2014, Calendar.JUNE, 4 );

		assertLagBOmer( 2014, Calendar.MAY, 18 );
	}

	@Test
	public void test2015() {
		assertPesach( 2015, Calendar.APRIL, 4 );
		assertPesachSheni( 2015, Calendar.MAY, 3 );

		assertLagBOmer( 2015, Calendar.MAY, 7 );
	}

	@Test
	public void test2016() {
		assertPesach( 2016, Calendar.APRIL, 23 );
		assertPesachSheni( 2016, Calendar.MAY, 22 );

		assertLagBOmer( 2016, Calendar.MAY, 26 );
	}

	private void assertPesach(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Details details;

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


	private void assertPesachSheni(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Details details;

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


	private void assertOmer(
		int year,
		@Constants.CalendarMonth int month,
		int day,
		int expectedDayOfOmer
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = OMER.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().contains( "" + expectedDayOfOmer ) );
	}


	private void assertNotOmer(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = OMER.getDetailsFor( date );

		Assert.assertNull( details );
	}


	private void assertLagBOmer(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {


		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = LAG_B_OMER.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.yesterday();
		details = LAG_B_OMER.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
	}

} // End of UT_Pesach class
