/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.yomtov.RoshHashana;
import com.gindin.zmanim.calendar.holiday.yomtov.YomKippur;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_YamimNoraim {

	private static final RoshHashana                          ROSH_HASHANA = new RoshHashana();
	private static final YomKippur                            YOM_KIPPUR = new YomKippur();


	@Test
	public void test2010() {
		runTest( 2010, Calendar.SEPTEMBER, 9 );
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.SEPTEMBER, 29 );
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.SEPTEMBER, 17 );
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.SEPTEMBER, 5 );
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.SEPTEMBER, 25 );
	}
	@Test
	public void test2015() {
		runTest( 2015, Calendar.SEPTEMBER, 14 );
	}



	private void runTest(
		int year,
	  @Constants.CalendarMonth  int month,
	  int day
	) {

		HebrewDate  date;
		Observance.Details  details;

		date = HebrewCalendar.getForDate( year, month, day );

		date = date.yesterday();
		details = ROSH_HASHANA.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = ROSH_HASHANA.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = ROSH_HASHANA.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertTrue( details.sayHavdalah() );

		date = date.forward( 7 );
		details = YOM_KIPPUR.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertTrue( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = YOM_KIPPUR.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertTrue( details.sayHavdalah() );
	}


} // End of UT_YamimNoraim class
