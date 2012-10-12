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
@SuppressWarnings( "MagicNumber" )
public class UT_Modern
	extends UT_AbstractHoliday {

	private static final YomHaShoah                           YOM_HA_SHOAH = new YomHaShoah();
	private static final YomHaZikaron                         YOM_HA_ZIKARON = new YomHaZikaron();
	private static final YomHaAtzmaut                         YOM_HA_ATZMAUT = new YomHaAtzmaut();
	private static final YomYerushalayim                      YOM_YERUSHALAYIM = new YomYerushalayim();


	@Test
	public void test1970() {
		// NOTE: www.hebcal.com shows Yom HaZikaron falling on *Sunday* May 10, 1970. However, the rules seem
		//  to say that if it falls on Sunday, it gets pushed off to Monday. Maybe they're being smart and
		//  handling some older version of the rules?
		testModernHolidays(
			1970,
			Calendar.MAY, 3,
			Calendar.MAY, 11,
			Calendar.JUNE, 3
		);
	}


	@Test
	public void test2008() {
		testModernHolidays(
			2008,
			Calendar.MAY, 2,
			Calendar.MAY, 7,
			Calendar.JUNE, 2
		);
	}

	@Test
	public void test2009() {
		testModernHolidays(
			2009,
			Calendar.APRIL, 21,
			Calendar.APRIL, 28,
			Calendar.MAY, 22
		);
	}

	@Test
	public void test2011() {
		testModernHolidays(
			2011,
			Calendar.MAY, 1,
			Calendar.MAY, 9,
			Calendar.JUNE, 1
		);
	}


	@Test
	public void test2012() {
		testModernHolidays(
			2012,
			Calendar.APRIL, 19,
			Calendar.APRIL, 25,
			Calendar.MAY, 20
		);
	}


	@Test
	public void test2015() {
		testModernHolidays(
			2015,
			Calendar.APRIL, 16,
			Calendar.APRIL, 22,
			Calendar.MAY, 17
		);
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
		Details details;

		date = HebrewCalendar.getForDate( year, yomHaShoahMonth, yomHaShoahDay );
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_SHOAH.getName() ) );
		checkDate( details, false, false, false );

		date = date.yesterday();
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_SHOAH.getName() ) );
		checkDate( details, true, false, false );

		date = HebrewCalendar.getForDate( year, yomHaZikaronMonth, yomHaZikaronDay );
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_ZIKARON.getName() ) );
		checkDate( details, true, false, false ); // Note that we KNOW this is Erev Yom HaAtzma'ut...

		date = date.yesterday();
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_ZIKARON.getName() ) );
		checkDate( details, true, false, false );

		// Yes, Erev Yom HaAtzmaut is the same day as Yom HaZikaron...
		date = date.tomorrow();
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_ATZMAUT.getName() ) );
		checkDate( details, true, false, false );

		date = date.tomorrow();
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_HA_ATZMAUT.getName() ) );
		checkDate( details, false, false, false );

		date = HebrewCalendar.getForDate( year, yomYerushalyimMonth, yomYerushalyimDay );
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_YERUSHALAYIM.getName() ) );
		checkDate( details, false, false, false );

		date = date.yesterday();
		details = date.getDetails();
		Assert.assertTrue( details.getDescription().contains( YOM_YERUSHALAYIM.getName() ) );
		checkDate( details, true, false, false );

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
