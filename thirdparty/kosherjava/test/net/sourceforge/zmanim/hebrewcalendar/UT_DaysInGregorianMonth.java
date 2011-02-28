/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

import java.util.Calendar;

/**
 * Verify the calculation of the number of days in a month. Not too hard...just the rules about when February
 *  has 28 or 29 days...
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_DaysInGregorianMonth {


	@Test
	public void testDaysInMonth() {

		HebrewDate hebrewDate = new HebrewDate();

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2011 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		hebrewDate.setDate( cal );

		assertDaysInMonth( false, hebrewDate );
	}



	@Test
	public void testDaysInMonthLeapYear() {

		HebrewDate hebrewDate = new HebrewDate();

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2012 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		hebrewDate.setDate( cal );

		assertDaysInMonth( true, hebrewDate );
	}


	@Test
	public void testDaysInMonth100Year() {

		HebrewDate hebrewDate = new HebrewDate();

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2100 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		hebrewDate.setDate( cal );

		assertDaysInMonth( false, hebrewDate );
	}


	@Test
	public void testDaysInMonth400Year() {

		HebrewDate hebrewDate = new HebrewDate();

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2000 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		hebrewDate.setDate( cal );

		assertDaysInMonth( true, hebrewDate );
	}


	private void assertDaysInMonth(
		boolean     febIsLeap,
		HebrewDate  hebrewDate
	) {

		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 1 ) );
		Assert.assertEquals( febIsLeap ? 29 : 28, hebrewDate.getLastDayOfGregorianMonth( 2 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 3 ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 4 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 5 ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 6 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 7 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 8 ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 9 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 10 ) );
		Assert.assertEquals( 30, hebrewDate.getLastDayOfGregorianMonth( 11 ) );
		Assert.assertEquals( 31, hebrewDate.getLastDayOfGregorianMonth( 12 ) );
	}


} // End of UT_DaysInGregorianMonth class
