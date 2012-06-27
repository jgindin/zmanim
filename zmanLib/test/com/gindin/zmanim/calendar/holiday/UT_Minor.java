/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.minor.TuBAv;
import com.gindin.zmanim.calendar.holiday.minor.TuBShvat;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Minor {

	private static final TuBShvat                             TU_B_SHVAT = new TuBShvat();
	private static final TuBAv                                TU_B_AV = new TuBAv();

	
	@Test
	public void test2010() {
		runTuBAvTest( 2010, Calendar.JULY, 26 );
		runTuBShvatTest( 2010, Calendar.JANUARY, 30 );
	}

	@Test
	public void test2011() {
		runTuBAvTest( 2011, Calendar.AUGUST, 15 );
		runTuBShvatTest( 2011, Calendar.JANUARY, 20 );
	}

	@Test
	public void test2012() {
		runTuBAvTest( 2012, Calendar.AUGUST, 3 );
		runTuBShvatTest( 2012, Calendar.FEBRUARY, 8 );
	}

	@Test
	public void test2013() {
		runTuBAvTest( 2013, Calendar.JULY, 22 );
		runTuBShvatTest( 2013, Calendar.JANUARY, 26 );
	}

	@Test
	public void test2014() {
		runTuBAvTest( 2014, Calendar.AUGUST, 11 );
		runTuBShvatTest( 2014, Calendar.JANUARY, 16 );
	}

	@Test
	public void test2015() {
		runTuBAvTest( 2015, Calendar.JULY, 31 );
		runTuBShvatTest( 2015, Calendar.FEBRUARY, 4 );
	}

	@Test
	public void test2016() {
		runTuBAvTest( 2016, Calendar.AUGUST, 19 );
		runTuBShvatTest( 2016, Calendar.JANUARY, 25 );
	}
	
	
	private void runTuBShvatTest(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Details details;
		
		date = HebrewCalendar.getForDate( year, month, day );
		details = TU_B_SHVAT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
		
		date = date.yesterday();
		details = TU_B_SHVAT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
	}	
	
	
	private void runTuBAvTest(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Details details;
		
		date = HebrewCalendar.getForDate( year, month, day );
		details = TU_B_AV.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
		
		date = date.yesterday();
		details = TU_B_AV.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );
	}	

} // End of UT_Minor class
