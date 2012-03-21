/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_HebrewDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		HebrewDate hebrewDate = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.NISSAN, 1 );
		Calendar cal = Calendar.getInstance();
		cal.setTime( hebrewDate.getTime() );
		
		Assert.assertEquals( 5, cal.get( Calendar.DAY_OF_MONTH ) );
		Assert.assertEquals( Calendar.APRIL, cal.get( Calendar.MONTH ) );
		Assert.assertEquals( 2011, cal.get( Calendar.YEAR ) );
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the HebrewCalendar class spun through a never-ending loop...

		HebrewDate hebrewDate = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.TISHREI, 1 );
		Calendar cal = Calendar.getInstance();
		cal.setTime( hebrewDate.getTime() );

		Assert.assertEquals( 9, cal.get( Calendar.DAY_OF_MONTH ) );
		Assert.assertEquals( Calendar.SEPTEMBER, cal.get( Calendar.MONTH ) );
		Assert.assertEquals( 2010, cal.get( Calendar.YEAR ) );
	}

	@Test
	public void testCrossingYears() {

		HebrewDate hebrewDate = HebrewCalendar.getForJewishDate( 5771, HebrewMonth.TISHREI, 1 );
		hebrewDate = hebrewDate.yesterday();
		Assert.assertEquals( 5770, hebrewDate.getHebrewYear() );
		Assert.assertEquals( HebrewMonth.ELUL, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 29, hebrewDate.getHebrewDayOfMonth() );

		hebrewDate = HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 13 );
		hebrewDate = hebrewDate.tomorrow();
		Assert.assertEquals( 5776, hebrewDate.getHebrewYear() );
		Assert.assertEquals( HebrewMonth.TISHREI, hebrewDate.getHebrewMonth() );
		Assert.assertEquals( 1, hebrewDate.getHebrewDayOfMonth() );
	}


} // End of UT_HebrewDateNavigation class
