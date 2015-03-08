/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Verifies we can convert from a given Jewish date to the corresponding secular date.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_HebrewDateConversion {

	@Test
	public void testConversions() {
		
		HebrewDate date = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.SHEVAT, 8 );
		assertGregorianDate( date, 2011, Calendar.JANUARY, 13 );

		date = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.SHEVAT, 30 );
		assertGregorianDate( date, 2011, Calendar.FEBRUARY, 4 );
		
		date = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.ADAR, 6 );
		assertGregorianDate( date, 2011, Calendar.FEBRUARY, 10 );

		date = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.ADAR_II, 24 );
		assertGregorianDate( date, 2011, Calendar.MARCH, 30 );

		date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.TISHREI, 3 );
		assertGregorianDate( date, 2011, Calendar.OCTOBER, 1 );

		date = HebrewCalendar.getForJewishDate( 5760, HebrewMonth.KISLEV, 18 );
		assertGregorianDate( date, 1999, Calendar.NOVEMBER, 27 );

		date = HebrewCalendar.getForJewishDate( 5759, HebrewMonth.IYAR, 14 );
		assertGregorianDate( date, 1999, Calendar.APRIL, 30 );

		date = HebrewCalendar.getForJewishDate( 5762, HebrewMonth.IYAR, 25 );
		assertGregorianDate( date, 2002, Calendar.MAY, 7 );
		
		date = HebrewCalendar.getForJewishDate( 5764, HebrewMonth.SIVAN, 25 );
		assertGregorianDate( date, 2004, Calendar.JUNE, 14 );

		date = HebrewCalendar.getForJewishDate( 5769, HebrewMonth.CHESHVAN, 17 );
		assertGregorianDate( date, 2008, Calendar.NOVEMBER, 15 );
	}
	
	
	private void assertGregorianDate( HebrewDate date, int year, int month, int dayOfMonth ) {

		Assert.assertEquals( year, date.getGregorianYear() );
		Assert.assertEquals( month, date.getGregorianMonth() );
		Assert.assertEquals( dayOfMonth, date.getGregorianDayOfMonth() );
	}

} // End of UT_HebrewDateConversion class
