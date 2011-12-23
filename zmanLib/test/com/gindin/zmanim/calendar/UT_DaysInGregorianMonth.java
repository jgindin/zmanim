/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Verify the calculation of the number of days in a month. Not too hard...just the rules about when February
 *  has 28 or 29 days...
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_DaysInGregorianMonth {


	@Test
	public void testDaysInMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2011 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		JewishDate jewishDate = JewishCalendar.getForDate( cal );

		assertDaysInMonth( false, jewishDate.getGregorianYear() );
	}



	@Test
	public void testDaysInMonthLeapYear() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2012 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		JewishDate jewishDate = JewishCalendar.getForDate( cal );

		assertDaysInMonth( true, jewishDate.getGregorianYear() );
	}


	@Test
	public void testDaysInMonth100Year() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2100 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		JewishDate jewishDate = JewishCalendar.getForDate( cal );

		assertDaysInMonth( false, jewishDate.getGregorianYear() );
	}


	@Test
	public void testDaysInMonth400Year() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2000 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		JewishDate jewishDate = JewishCalendar.getForDate( cal );

		assertDaysInMonth( true, jewishDate.getGregorianYear() );
	}


	private void assertDaysInMonth(
		boolean   febIsLeap,
		int       year
	) {

		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 1, year ) );
		Assert.assertEquals( febIsLeap ? 29 : 28, JewishCalendar.getLastDayOfGregorianMonth( 2, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 3, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( 4, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 5, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( 6, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 7, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 8, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( 9, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 10, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( 11, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( 12, year ) );
	}


} // End of UT_DaysInGregorianMonth class
