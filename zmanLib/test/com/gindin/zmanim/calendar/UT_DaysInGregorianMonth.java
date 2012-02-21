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

		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.JANUARY, year ) );
		Assert.assertEquals( febIsLeap ? 29 : 28, JewishCalendar.getLastDayOfGregorianMonth( Calendar.FEBRUARY, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.MARCH, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( Calendar.APRIL, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.MAY, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( Calendar.JUNE, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.JULY, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.AUGUST, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( Calendar.SEPTEMBER, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.OCTOBER, year ) );
		Assert.assertEquals( 30, JewishCalendar.getLastDayOfGregorianMonth( Calendar.NOVEMBER, year ) );
		Assert.assertEquals( 31, JewishCalendar.getLastDayOfGregorianMonth( Calendar.DECEMBER, year ) );
	}


} // End of UT_DaysInGregorianMonth class
