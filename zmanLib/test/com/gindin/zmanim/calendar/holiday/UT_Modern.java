/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.modern.YomHaAtzmaut;
import com.gindin.zmanim.calendar.holiday.modern.YomHaShoah;
import com.gindin.zmanim.calendar.holiday.modern.YomHaZikaron;
import com.gindin.zmanim.calendar.holiday.modern.YomYerushalayim;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Tests the dates for the 'modern' holidays.
 */
public class UT_Modern {

	private static final YomHaShoah                           YOM_HA_SHOAH = new YomHaShoah();
	private static final YomHaZikaron                         YOM_HA_ZIKARON = new YomHaZikaron();
	private static final YomHaAtzmaut                         YOM_HA_ATZMAUT = new YomHaAtzmaut();
	private static final YomYerushalayim                      YOM_YERUSHALAYIM = new YomYerushalayim();


	@Test
	public void test1970() {
		// NOTE: www.hebcal.com shows Yom HaZikaron falling on *Sunday* May 10, 1970. However, the rules seem
		//  to say that if it falls on Sunday, it gets pushed off to Monday. Maybe they're being smart and
		//  handling some older version of the rules?
		testModernHolidays( 1970, Calendar.MAY, 3, Calendar.MAY, 11, Calendar.JUNE, 3 );
	}


	@Test
	public void test2008() {
		testModernHolidays( 2008, Calendar.MAY, 2, Calendar.MAY, 7, Calendar.JUNE, 2 );
	}

	@Test
	public void test2009() {
		testModernHolidays( 2009, Calendar.APRIL, 21, Calendar.APRIL, 28, Calendar.MAY, 22 );
	}

	@Test
	public void test2011() {
		testModernHolidays( 2011, Calendar.MAY, 1, Calendar.MAY, 9, Calendar.JUNE, 1 );
	}


	@Test
	public void test2012() {

	}



	private void testModernHolidays(
		int year,
	  @Constants.CalendarMonth int yomHaShoahMonth,
	  int yomHaShoahDay,
	  @Constants.CalendarMonth int yomHaZikaronMonth,
	  int yomHaZikaronDay,
	  @Constants.CalendarMonth int yomYerushalyimMonth,
	  int yomYerushalyimDay
	) {

		HebrewDate date;
		Observance.Details details;

		date = HebrewCalendar.getForDate( year, yomHaShoahMonth, yomHaShoahDay );
		details = YOM_HA_SHOAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.yesterday();
		details = YOM_HA_SHOAH.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = HebrewCalendar.getForDate( year, yomHaZikaronMonth, yomHaZikaronDay );
		details = YOM_HA_ZIKARON.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.yesterday();
		details = YOM_HA_ZIKARON.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Yes, Erev Yom HaAtzmaut is the same day as Yom HaZikaron...
		date = date.tomorrow();
		details = YOM_HA_ATZMAUT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.tomorrow();
		details = YOM_HA_ATZMAUT.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = HebrewCalendar.getForDate( year, yomYerushalyimMonth, yomYerushalyimDay );
		details = YOM_YERUSHALAYIM.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		date = date.yesterday();
		details = YOM_YERUSHALAYIM.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
		Assert.assertFalse( details.lightCandles() );
		Assert.assertFalse( details.sayHavdalah() );

		// Sprinkle some sanity checks in...
		date = HebrewCalendar.getForDate( year, yomHaShoahMonth - 1, 1 );
		Assert.assertNull( YOM_HA_SHOAH.getDetailsFor( date ) );
		Assert.assertNull( YOM_HA_ZIKARON.getDetailsFor( date ) );
		Assert.assertNull( YOM_HA_ATZMAUT.getDetailsFor( date ) );
		Assert.assertNull( YOM_YERUSHALAYIM.getDetailsFor( date ) );

		date = HebrewCalendar.getForDate( year, yomYerushalyimMonth + 2, 1 );
		Assert.assertNull( YOM_HA_SHOAH.getDetailsFor( date ) );
		Assert.assertNull( YOM_HA_ZIKARON.getDetailsFor( date ) );
		Assert.assertNull( YOM_HA_ATZMAUT.getDetailsFor( date ) );
		Assert.assertNull( YOM_YERUSHALAYIM.getDetailsFor( date ) );

	}

}  // End of UT_Modern class
