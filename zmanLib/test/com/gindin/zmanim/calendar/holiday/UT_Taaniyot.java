/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.taaniyot.*;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Taaniyot {


	@Test
	public void testAsaraBTevet() {

		final AsaraBTevet asaraBTevet = new AsaraBTevet();

		runTest( asaraBTevet, 2010, Calendar.DECEMBER, 17 );
		runTest( asaraBTevet, 2012, Calendar.JANUARY, 5 );
		runTest( asaraBTevet, 2012, Calendar.DECEMBER, 23 );
		runTest( asaraBTevet, 2013, Calendar.DECEMBER, 13 );
		runTest( asaraBTevet, 2015, Calendar.JANUARY, 1 );
		runTest( asaraBTevet, 2015, Calendar.DECEMBER, 22 );
	}


	@Test
	public void testTaanitEsther() {

		final TaanitEsther taanitEsther = new TaanitEsther();

		runTest( taanitEsther, 2011, Calendar.MARCH, 17 );
		runTest( taanitEsther, 2012, Calendar.MARCH, 7 );
		runTest( taanitEsther, 2013, Calendar.FEBRUARY, 21 );
		runTest( taanitEsther, 2014, Calendar.MARCH, 13 );
		runTest( taanitEsther, 2015, Calendar.MARCH, 4 );
		runTest( taanitEsther, 2016, Calendar.MARCH, 23 );
	}


	@Test
	public void testTaanitBechorot() {

		final TaanitBechorot taanitBechorot = new TaanitBechorot();

		runTest( taanitBechorot, 2011, Calendar.APRIL, 18 );
		runTest( taanitBechorot, 2012, Calendar.APRIL, 5 );
		runTest( taanitBechorot, 2013, Calendar.MARCH, 25 );
		runTest( taanitBechorot, 2014, Calendar.APRIL, 14 );
		runTest( taanitBechorot, 2015, Calendar.APRIL, 2 );
		runTest( taanitBechorot, 2016, Calendar.APRIL, 21 );
	}


	@Test
	public void testTaanit17Tamuz() {

		final Taanit17Tamuz taanit17Tamuz = new Taanit17Tamuz();

		runTest( taanit17Tamuz, 2011, Calendar.JULY, 19 );
		runTest( taanit17Tamuz, 2012, Calendar.JULY, 8 );
		runTest( taanit17Tamuz, 2013, Calendar.JUNE, 25 );
		runTest( taanit17Tamuz, 2014, Calendar.JULY, 15 );
		runTest( taanit17Tamuz, 2015, Calendar.JULY, 5 );
		runTest( taanit17Tamuz, 2016, Calendar.JULY, 24 );
	}


	@Test
	public void testTishaBAv() {

		final TishaBAv tishaBAv = new TishaBAv();

		runTest( tishaBAv, 2011, Calendar.AUGUST, 9 );
		runTest( tishaBAv, 2012, Calendar.JULY, 29 );
		runTest( tishaBAv, 2013, Calendar.JULY, 16 );
		runTest( tishaBAv, 2014, Calendar.AUGUST, 5 );
		runTest( tishaBAv, 2015, Calendar.JULY, 26 );
		runTest( tishaBAv, 2016, Calendar.AUGUST, 14 );
	}


	@Test
	public void testTzomGedaliah() {

		final TzomGedaliah tzomGedaliah = new TzomGedaliah();

		runTest( tzomGedaliah, 2010, Calendar.SEPTEMBER, 12 );
		runTest( tzomGedaliah, 2011, Calendar.OCTOBER, 2 );
		runTest( tzomGedaliah, 2012, Calendar.SEPTEMBER, 19 );
		runTest( tzomGedaliah, 2013, Calendar.SEPTEMBER, 8 );
		runTest( tzomGedaliah, 2014, Calendar.SEPTEMBER, 28 );
		runTest( tzomGedaliah, 2015, Calendar.SEPTEMBER, 16 );
	}


	private void runTest(
		Observance  observance,
		int         year,
	  @Constants.CalendarMonth
	  int         month,
	  int         dayOfMonth
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, dayOfMonth );
		Observance.Details details = observance.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );

		date = date.yesterday();
		details = observance.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );
	}


} // End of UT_Taaniyot class
