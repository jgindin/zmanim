/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.HebrewMonth;
import com.gindin.zmanim.calendar.holiday.rabbanan.Purim;
import com.gindin.zmanim.calendar.holiday.rabbanan.PurimKatan;
import com.gindin.zmanim.calendar.holiday.rabbanan.ShushanPurim;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Purim {

	private static final Purim                                PURIM = new Purim();
	private static final PurimKatan                           PURIM_KATAN = new PurimKatan();
	private static final ShushanPurim                         SHUSHAN_PURIM = new ShushanPurim();


	@Test
	public void test2011() {
		runTest( 2011, Calendar.MARCH, 20 );
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.MARCH, 8);
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.FEBRUARY, 24 );
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.MARCH, 16 );
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.MARCH, 5 );
	}

	@Test
	public void test2016() {
		runTest( 2016, Calendar.MARCH, 24 );
	}


	private void runTest(
		int year,
		@Constants.CalendarMonth  int purimMonth,
	  int purimDay
	) {

		HebrewDate  date;
		Observance.Details  details;

		// Check today -- should be Purim.
		date = HebrewCalendar.getForDate( year, purimMonth, purimDay );
		details = PURIM.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Yesterday should be erev Purim.
		date = date.yesterday();
		details = PURIM.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Day after Purim is Shusha Purim.
	  date = date.tomorrow().tomorrow();
		details = SHUSHAN_PURIM.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		if ( date.isJewishLeapYear() ) {

			date = HebrewCalendar.getForJewishDate( date.getJewishYear(), HebrewMonth.ADAR, 13 );
			details = PURIM_KATAN.getDetailsFor( date );
			Assert.assertNotNull( details );
			Assert.assertTrue( details.isErev() );
			Assert.assertFalse( details.lightCandles() );
			Assert.assertFalse( details.sayHavdalah() );

			date = date.tomorrow();
			details = PURIM_KATAN.getDetailsFor( date );
			Assert.assertNotNull( details );
			Assert.assertFalse( details.isErev() );
			Assert.assertFalse( details.lightCandles() );
			Assert.assertFalse( details.sayHavdalah() );

		}
		else {

			date = HebrewCalendar.getForJewishDate( date.getJewishYear(), HebrewMonth.ADAR, 13 );
			details = PURIM_KATAN.getDetailsFor( date );
			Assert.assertNull( details );
		}
	}

} // End of UT_Purim class
