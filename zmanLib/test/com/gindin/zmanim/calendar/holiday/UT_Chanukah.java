/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.rabbanan.Chanukah;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Chanukah {

	private static final Chanukah                             CHANUKAH = new Chanukah();

	@Test
	public void test2010() {
		runTest( 2010, Calendar.DECEMBER, 2 );
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.DECEMBER, 21 );
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.DECEMBER, 9 );
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.NOVEMBER, 28 );
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.DECEMBER, 17 );
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.DECEMBER, 7 );
	}

	private void runTest(
		int year,
		@Constants.CalendarMonth int chanukahMonth,
	  int startingDay
	) {

		HebrewDate date;
		Observance.Details  details;

		date = HebrewCalendar.getForDate( year, chanukahMonth, startingDay );
		date = date.yesterday();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "1" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "2" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "3" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "4" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "5" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "6" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "7" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.sayHavdalah() );
		Assert.assertTrue( details.getDescription().endsWith( "8" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNull( details );

	}

} // End of UT_Chanukah class
