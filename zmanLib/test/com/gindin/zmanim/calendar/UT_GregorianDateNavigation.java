/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_GregorianDateNavigation {

	@Test
	public void gregorianForwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2011 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		cal.set( Calendar.DATE, 31 );

		JewishDate jewishDate = JewishCalendar.getForDate( cal );
		Assert.assertEquals( 5771, jewishDate.getJewishYear() );
		Assert.assertEquals( 11, jewishDate.getJewishMonth() );
		Assert.assertEquals( 26, jewishDate.getJewishDayOfMonth() );

		jewishDate = jewishDate.tomorrow();
		cal.setTime( jewishDate.getTime() );
		Assert.assertEquals( Calendar.FEBRUARY, cal.get( Calendar.MONTH ) );
		Assert.assertEquals( 1, cal.get( Calendar.DAY_OF_MONTH ) );
		Assert.assertEquals( 11, jewishDate.getJewishMonth() );
		Assert.assertEquals( 27, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.FEBRUARY );
		cal.set( Calendar.DATE, 28 );
		jewishDate = JewishCalendar.getForDate( cal );
		Assert.assertEquals( 12, jewishDate.getJewishMonth() );
		Assert.assertEquals( 24, jewishDate.getJewishDayOfMonth() );

		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 12, jewishDate.getJewishMonth() );
		Assert.assertEquals( 25, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.MARCH );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 13, jewishDate.getJewishMonth() );
		Assert.assertEquals( 26, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.APRIL );
		cal.set( Calendar.DATE, 30 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 1, jewishDate.getJewishMonth() );
		Assert.assertEquals( 27, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.MAY );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 2, jewishDate.getJewishMonth() );
		Assert.assertEquals( 28, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.JUNE );
		cal.set( Calendar.DATE, 30 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 3, jewishDate.getJewishMonth() );
		Assert.assertEquals( 29, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.JULY );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 5, jewishDate.getJewishMonth() );
		Assert.assertEquals( 1, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.AUGUST );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 6, jewishDate.getJewishMonth() );
		Assert.assertEquals( 2, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
		cal.set( Calendar.DATE, 30 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 7, jewishDate.getJewishMonth() );
		Assert.assertEquals( 3, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.OCTOBER );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 5772, jewishDate.getJewishYear() );
		Assert.assertEquals( 8, jewishDate.getJewishMonth() );
		Assert.assertEquals( 4, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.NOVEMBER );
		cal.set( Calendar.DATE, 30 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 9, jewishDate.getJewishMonth() );
		Assert.assertEquals( 5, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.DECEMBER );
		cal.set( Calendar.DATE, 31 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.tomorrow();
		Assert.assertEquals( 10, jewishDate.getJewishMonth() );
		Assert.assertEquals( 6, jewishDate.getJewishDayOfMonth() );
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2011 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		cal.set( Calendar.DATE, 1 );

		JewishDate jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 10, jewishDate.getJewishMonth() );
		Assert.assertEquals( 24, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.DECEMBER );
		cal.set( Calendar.YEAR, 2010 );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 9, jewishDate.getJewishMonth() );
		Assert.assertEquals( 23, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.NOVEMBER );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 8, jewishDate.getJewishMonth() );
		Assert.assertEquals( 23, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.OCTOBER );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 7, jewishDate.getJewishMonth() );
		Assert.assertEquals( 22, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 5770, jewishDate.getJewishYear() );
		Assert.assertEquals( 6, jewishDate.getJewishMonth() );
		Assert.assertEquals( 21, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.AUGUST );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 5, jewishDate.getJewishMonth() );
		Assert.assertEquals( 20, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.JULY );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 4, jewishDate.getJewishMonth() );
		Assert.assertEquals( 18, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.JUNE );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 3, jewishDate.getJewishMonth() );
		Assert.assertEquals( 18, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.MAY );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 2, jewishDate.getJewishMonth() );
		Assert.assertEquals( 16, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.APRIL );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 1, jewishDate.getJewishMonth() );
		Assert.assertEquals( 16, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.MARCH );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 12, jewishDate.getJewishMonth() );
		Assert.assertEquals( 14, jewishDate.getJewishDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.FEBRUARY );
		jewishDate = JewishCalendar.getForDate( cal );
		jewishDate = jewishDate.yesterday();
		Assert.assertEquals( 11, jewishDate.getJewishMonth() );
		Assert.assertEquals( 16, jewishDate.getJewishDayOfMonth() );

	}



} // End of UT_GregorianDateNavigation class
