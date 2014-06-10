/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.yomtov.Sukkot;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Succot
	extends UT_AbstractHoliday {

	private static final Sukkot SUKKOT = new Sukkot();
	
	@Test
	public void test2010() {
		assertSukkot( 2010, Calendar.SEPTEMBER, 23 );
	}	
	

	@Test
	public void test2011() {
		assertSukkot( 2011, Calendar.OCTOBER, 13 );
	}	
	
	@Test
	public void test2012() {
		assertSukkot( 2012, Calendar.OCTOBER, 1 );
	}	
	
	@Test
	public void test2013() {
		assertSukkot( 2013, Calendar.SEPTEMBER, 19 );
	}	
	
	@Test
	public void test2014() {
		assertSukkot( 2014, Calendar.OCTOBER, 9 );
	}	
	
	@Test
	public void test2015() {
		assertSukkot( 2015, Calendar.SEPTEMBER, 28 );
	}	
	
	@Test
	public void test2016() {
		assertSukkot( 2016, Calendar.OCTOBER, 17 );
	}	
	
	
	private void assertSukkot(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date;
		Details details;

		date = HebrewCalendar.getForDate( year, month, day );

		// Erev Sukkot
		date = date.yesterday();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, true, true, false );

		// Sukkot I
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, true, false );

		// Sukkot II
		// TODO: Handle Israel...
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, true );

		// Sukkot III
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, false );

		// Sukkot IV
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, false );


		// Sukkot V
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, false );

		// Sukkot VI
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, false );

		// Sukkot VII -- Hoshana Rabba
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, true, true, false );
		Assert.assertTrue( details.getDescription().contains( "Hoshana Rabba" ) );

		// Shmini Atzeret
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, true, false );
		Assert.assertTrue( details.getDescription().contains( "Shmini Atzeret" ) );

		// Simchat Torah
		date = date.tomorrow();
		details = SUKKOT.getDetailsFor( date );
		checkDate( details, false, false, true );
		Assert.assertTrue( details.getDescription().contains( "Simchat Torah" ) );
	}
	
} // End of UT_Succot class
