/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.Assert;
import org.junit.Test;

/**
 * Validate the days in a Hebrew month (in various types of years) are correct.
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_DaysInJewishMonth {

	@Test
	public void daysInMonthsInHaserYear() {

		assertHaser( 5773 );
		assertHaser( 5777 );
		assertHaser( 5781 );
		assertHaserLeap( 5784 );
		assertHaserLeap( 5790 );
		assertHaserLeap( 5793 );
	}


	@Test
	public void daysInMonthsInQesidrahYear() {

		assertQesidrah( 5769 );
		assertQesidrah( 5772 );
		assertQesidrah( 5778 );
		assertQesidrah( 5786 );
		assertQesidrah( 5789 );
		assertQesidrah( 5792 );

		assertQesidrahLeap( 5782 );
	}


	@Test
	public void daysInMonthsInShalemYear() {

		assertShalem( 5770 );
		assertShalem( 5780 );
		assertShalem( 5783 );
		assertShalem( 5785 );
		assertShalem( 5788 );
		assertShalem( 5791 );
		assertShalem( 5794 );

		assertShalemLeap( 5771 );
		assertShalemLeap( 5774 );
		assertShalemLeap( 5776 );
		assertShalemLeap( 5779 );
		assertShalemLeap( 5787 );
		assertShalemLeap( 5795 );
	}


	private void assertHaser( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		Assert.assertFalse( date.isCheshvanLong() );
		Assert.assertTrue( date.isKislevShort() );
	}


	private void assertHaserLeap( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		assertHaser( year );
		Assert.assertTrue( date.isJewishLeapYear() );
	}


	private void assertQesidrah( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		Assert.assertFalse( date.isCheshvanLong() );
		Assert.assertFalse( date.isKislevShort() );
	}


	private void assertQesidrahLeap( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		assertQesidrah( year );
		Assert.assertTrue( date.isJewishLeapYear() );
	}


	private void assertShalem( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		Assert.assertTrue( date.isCheshvanLong() );
		Assert.assertFalse( date.isKislevShort() );
	}


	private void assertShalemLeap( int year ) {

		JewishDate date = JewishCalendar.getForJewishDate( year, HebrewMonth.TISHREI, 1 );

		assertShalem( year );
		Assert.assertTrue( date.isJewishLeapYear() );
	}

} // End of UT_DaysInJewishMonth class
