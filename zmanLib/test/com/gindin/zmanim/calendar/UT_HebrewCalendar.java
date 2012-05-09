/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.*;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_HebrewCalendar {

	@Test
	public void testYearType() {

		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5771 ) );
		Assert.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5772 ) );
		Assert.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5773 ) );
		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5774 ) );
		Assert.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5775 ) );
		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5776 ) );
		Assert.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5777 ) );
	}


	@Test
	public void testInvalidJewishDate() {

		// When asking for a HebrewDate, if we specify a date that doesn't exist in the
		//  desired month, the code is supposed to move it back to a sane date...
		HebrewDate date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.TEVET, 30 );
		Assert.assertEquals( HebrewMonth.TEVET, date.getHebrewMonth() );
		Assert.assertEquals( 29, date.getHebrewDayOfMonth() );

		date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 30 );
		Assert.assertEquals( HebrewMonth.ADAR, date.getHebrewMonth() );
		Assert.assertEquals( 29, date.getHebrewDayOfMonth() );
	}


	@Test
	public void testComparatorAndEquals() {

		HebrewDate date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
		HebrewDate date2 = HebrewCalendar.getForJewishDate( 5773, HebrewMonth.NISSAN, 1 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );
		Assert.assertFalse( date1.equals( date2 ) );


		date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
		date2 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 2 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );

		date2 = date2.yesterday();
		Assert.assertEquals( 0, date1.compareTo( date2 ) );
		Assert.assertEquals( 0, date2.compareTo( date1 ) );
		Assert.assertTrue( date1.equals( date2 ) );
		Assert.assertTrue( date2.equals( date1 ) );

		date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 29 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );

		date2 = date2.yesterday();
		Assert.assertEquals( 0, date1.compareTo( date2 ) );
		Assert.assertEquals( 0, date2.compareTo( date1 ) );
	}


} // End of UT_HebrewCalendar class
