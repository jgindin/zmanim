/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package net.sourceforge.zmanim.hebrewcalendar;

import org.junit.*;

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

		HebrewDate hebrewDate = new HebrewDate( cal );
		Assert.assertEquals( 5771, hebrewDate.getHebrewYear() );
		Assert.assertEquals( 11, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 26, hebrewDate.getHebrewDayOfMonth() );

		hebrewDate.forward();
		Assert.assertEquals( 2, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 11, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 27, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.FEBRUARY );
		cal.set( Calendar.DATE, 28 );
		hebrewDate.setDate( cal );
		Assert.assertEquals( 2, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 28, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 12, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 24, hebrewDate.getHebrewDayOfMonth() );

		hebrewDate.forward();
		Assert.assertEquals( 3, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 12, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 25, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.MARCH );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 4, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 13, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 26, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.APRIL );
		cal.set( Calendar.DATE, 30 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 5, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 1, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 27, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.MAY );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 6, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 2, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 28, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.JUNE );
		cal.set( Calendar.DATE, 30 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 7, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 3, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 29, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.JULY );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 8, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 5, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 1, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.AUGUST );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 9, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 6, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 2, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
		cal.set( Calendar.DATE, 30 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 10, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 7, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 3, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.OCTOBER );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 11, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 5772, hebrewDate.getHebrewYear() );
		Assert.assertEquals( 8, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 4, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.NOVEMBER );
		cal.set( Calendar.DATE, 30 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 12, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 9, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 5, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.MONTH, Calendar.DECEMBER );
		cal.set( Calendar.DATE, 31 );
		hebrewDate.setDate( cal );
		hebrewDate.forward();
		Assert.assertEquals( 2012, hebrewDate.getGregorianYear() );
		Assert.assertEquals( 1, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 10, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 6, hebrewDate.getHebrewDayOfMonth() );
	}


	@Test
	public void gregorianBackwardMonthToMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 2011 );
		cal.set( Calendar.MONTH, Calendar.JANUARY );
		cal.set( Calendar.DATE, 1 );

		HebrewDate hebrewDate = new HebrewDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 2010, hebrewDate.getGregorianYear() );
		Assert.assertEquals( 12, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 10, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 24, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.DECEMBER );
		cal.set( Calendar.YEAR, 2010 );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 11, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 30, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 9, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 23, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.NOVEMBER );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 10, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 8, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 23, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.OCTOBER );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 9, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 30, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 7, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 22, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 8, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 5770, hebrewDate.getHebrewYear() );
		Assert.assertEquals( 6, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 21, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.AUGUST );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 7, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 5, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 20, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.JULY );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 6, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 30, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 4, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 18, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.JUNE );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 5, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 3, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 18, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.MAY );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 4, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 30, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 2, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.APRIL );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 3, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 1, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.MARCH );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 2, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 28, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 12, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 14, hebrewDate.getHebrewDayOfMonth() );

		cal.set( Calendar.DATE, 1 );
		cal.set( Calendar.MONTH, Calendar.FEBRUARY );
		hebrewDate.setDate( cal );
		hebrewDate.back();
		Assert.assertEquals( 1, hebrewDate.getGregorianMonth() );
		Assert.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );
		Assert.assertEquals( 11, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

	}



} // End of UT_GregorianDateNavigation class
