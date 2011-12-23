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
public class UT_JewishDateNavigation {


	@Test
	public void jewishForwardMonthToMonth() {

		JewishDate jewishDate = JewishCalendar.getForJewishDate( 5771, 1, 1 );
		Calendar cal = Calendar.getInstance();
		cal.setTime( jewishDate.getTime() );
		
		Assert.assertEquals( 5, cal.get( Calendar.DAY_OF_MONTH ) );
		Assert.assertEquals( Calendar.APRIL, cal.get( Calendar.MONTH ) );
		Assert.assertEquals( 2011, cal.get( Calendar.YEAR ) );
	}


	@Test
	public void computeRoshHashana5771() {

		// At one point, this test was failing as the JewishCalendar class spun through a never-ending loop...

		JewishDate jewishDate = JewishCalendar.getForJewishDate( 5771, 7, 1 );
		Calendar cal = Calendar.getInstance();
		cal.setTime( jewishDate.getTime() );

		Assert.assertEquals( 9, cal.get( Calendar.DAY_OF_MONTH ) );
		Assert.assertEquals( Calendar.SEPTEMBER, cal.get( Calendar.MONTH ) );
		Assert.assertEquals( 2010, cal.get( Calendar.YEAR ) );
	}


} // End of UT_JewishDateNavigation class
