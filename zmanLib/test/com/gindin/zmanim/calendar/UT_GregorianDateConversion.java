/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_GregorianDateConversion {

	@Test
	public void testConversions() {

		Calendar cal = Calendar.getInstance();
		cal.set( 2011, Calendar.JANUARY, 13 );
		HebrewDate date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5771, HebrewMonth.SHEVAT, 8 );
		
		cal.set( 2011, Calendar.FEBRUARY, 4 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5771, HebrewMonth.SHEVAT, 30 );

		cal.set( 2011, Calendar.FEBRUARY, 10 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5771, HebrewMonth.ADAR, 6 );

		cal.set( 2011, Calendar.MARCH, 30 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5771, HebrewMonth.ADAR_II, 24 );

		cal.set( 2011, Calendar.OCTOBER, 1 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5772, HebrewMonth.TISHREI, 3 );

		cal.set( 1999, Calendar.NOVEMBER, 27 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5760, HebrewMonth.KISLEV, 18 );

		cal.set( 2002, Calendar.MAY, 7 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5762, HebrewMonth.IYAR, 25 );

		cal.set( 2004, Calendar.JUNE, 14 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5764, HebrewMonth.SIVAN, 25 );

		cal.set( 2008, Calendar.NOVEMBER, 15 );
		date = HebrewCalendar.getForDate( cal );
		assertJewishDate( date, 5769, HebrewMonth.CHESHVAN, 17 );

	}
	
	
	private void assertJewishDate( HebrewDate date, int year, int month, int dayOfMonth ) {
		
		Assert.assertEquals( year, date.getHebrewYear() );
		Assert.assertEquals( month, date.getHebrewMonth() );
		Assert.assertEquals( dayOfMonth, date.getHebrewDayOfMonth() );
	}

} // End of UT_GregorianDateConversion class
