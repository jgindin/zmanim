/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.rabbanan.Purim;
import com.gindin.zmanim.calendar.holiday.minor.PurimKatan;
import com.gindin.zmanim.calendar.holiday.rabbanan.ShushanPurim;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Purim
	extends UT_AbstractHoliday {

	private static final Purim                                PURIM = new Purim();
	private static final PurimKatan                           PURIM_KATAN = new PurimKatan();
	private static final ShushanPurim                         SHUSHAN_PURIM = new ShushanPurim();


	@Test
	public void test2011() {
		runPurimTest( 2011, Calendar.MARCH, 20 );
		runPurimKatanTest( 2011, Calendar.FEBRUARY, 18 );
	}

	@Test
	public void test2012() {
		runPurimTest( 2012, Calendar.MARCH, 8 );
		runPurimKatanTest( 2012, Calendar.MARCH, 22 ); // doesn't matter...this isn't a leap year...
	}

	@Test
	public void test2013() {
		runPurimTest( 2013, Calendar.FEBRUARY, 24 );
		runPurimKatanTest( 2013, Calendar.FEBRUARY, 28 ); // doesn't matter...this isn't a leap year...
	}

	@Test
	public void test2014() {
		runPurimTest( 2014, Calendar.MARCH, 16 );
		runPurimKatanTest( 2014, Calendar.FEBRUARY, 14 );
	}

	@Test
	public void test2015() {
		runPurimTest( 2015, Calendar.MARCH, 5 );
		runPurimKatanTest( 2015, Calendar.MARCH, 18 ); // doesn't matter...this isn't a leap year...
	}

	@Test
	public void test2016() {
		runPurimTest( 2016, Calendar.MARCH, 24 );
		runPurimKatanTest( 2016, Calendar.FEBRUARY, 23 );
	}


	private void runPurimTest(
		int year,
		@Constants.CalendarMonth int purimMonth,
		int purimDay
	) {

		HebrewDate  date;
		Details details;

		// Check today -- should be Purim.
		date = HebrewCalendar.getForDate( year, purimMonth, purimDay );
		details = PURIM.getDetailsFor( date );
		checkDate( details, false, false, false );

		// Yesterday should be erev Purim.
		date = date.yesterday();
		details = PURIM.getDetailsFor( date );
		checkDate( details, true, false, false );

		// Day after Purim is Shusha Purim.
	  date = date.tomorrow().tomorrow();
		details = SHUSHAN_PURIM.getDetailsFor( date );
		checkDate( details, false, false, false );
	}


	private void runPurimKatanTest(
		int year,
		@Constants.CalendarMonth int purimMonth,
		int purimDay
	) {

		HebrewDate  date;
		Details details;

		date = HebrewCalendar.getForDate( year, purimMonth, purimDay );
		details = PURIM_KATAN.getDetailsFor( date );

		if ( date.isHebrewLeapYear() ) {
			checkDate( details, false, false, false );
		}
		else {
			Assert.assertNull( details );
		}

		date = date.yesterday();
		details = PURIM_KATAN.getDetailsFor( date );
		if ( date.isHebrewLeapYear() ) {
			checkDate( details, true, false, false );
		}
		else {
			Assert.assertNull( details );
		}

	}

} // End of UT_Purim class
