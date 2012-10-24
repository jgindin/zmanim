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

		assertIsTaanit( asaraBTevet, 2010, Calendar.DECEMBER, 17 );
		assertIsTaanit( asaraBTevet, 2012, Calendar.JANUARY, 5 );
		assertIsTaanit( asaraBTevet, 2012, Calendar.DECEMBER, 23 );
		assertIsTaanit( asaraBTevet, 2013, Calendar.DECEMBER, 13 );
		assertIsTaanit( asaraBTevet, 2015, Calendar.JANUARY, 1 );
		assertIsTaanit( asaraBTevet, 2015, Calendar.DECEMBER, 22 );
	}


	@Test
	public void testTaanitEsther() {

		final TaanitEsther taanitEsther = new TaanitEsther();

		assertIsTaanit( taanitEsther, 2011, Calendar.MARCH, 17 );
		assertIsTaanit( taanitEsther, 2012, Calendar.MARCH, 7 );
		assertIsTaanit( taanitEsther, 2013, Calendar.FEBRUARY, 21 );
		assertIsTaanit( taanitEsther, 2014, Calendar.MARCH, 13 );
		assertIsTaanit( taanitEsther, 2015, Calendar.MARCH, 4 );
		assertIsTaanit( taanitEsther, 2016, Calendar.MARCH, 23 );

		assertIsNotTaanit( taanitEsther, 2011, Calendar.MARCH, 18 );
		assertIsNotTaanit( taanitEsther, 2011, Calendar.MARCH, 19 );
		assertIsNotTaanit( taanitEsther, 2011, Calendar.MARCH, 20 );
	}


	@Test
	public void testTaanitBechorot() {

		final TaanitBechorot taanitBechorot = new TaanitBechorot();

		assertIsTaanit( taanitBechorot, 2010, Calendar.MARCH, 29 );
		assertIsTaanit( taanitBechorot, 2011, Calendar.APRIL, 18 );
		assertIsTaanit( taanitBechorot, 2012, Calendar.APRIL, 6 );
		assertIsTaanit( taanitBechorot, 2013, Calendar.MARCH, 25 );
		assertIsTaanit( taanitBechorot, 2014, Calendar.APRIL, 14 );
		assertIsTaanit( taanitBechorot, 2015, Calendar.APRIL, 3 );
		assertIsTaanit( taanitBechorot, 2016, Calendar.APRIL, 22 );
	}


	@Test
	public void testTaanit17Tamuz() {

		final Taanit17Tamuz taanit17Tamuz = new Taanit17Tamuz();

		assertIsTaanit( taanit17Tamuz, 2011, Calendar.JULY, 19 );
		assertIsTaanit( taanit17Tamuz, 2012, Calendar.JULY, 8 );
		assertIsTaanit( taanit17Tamuz, 2013, Calendar.JUNE, 25 );
		assertIsTaanit( taanit17Tamuz, 2014, Calendar.JULY, 15 );
		assertIsTaanit( taanit17Tamuz, 2015, Calendar.JULY, 5 );
		assertIsTaanit( taanit17Tamuz, 2016, Calendar.JULY, 24 );
	}


	@Test
	public void testTishaBAv() {

		final TishaBAv tishaBAv = new TishaBAv();

		assertIsTaanit( tishaBAv, 2011, Calendar.AUGUST, 9 );
		assertIsTaanit( tishaBAv, 2012, Calendar.JULY, 29 );
		assertIsTaanit( tishaBAv, 2013, Calendar.JULY, 16 );
		assertIsTaanit( tishaBAv, 2014, Calendar.AUGUST, 5 );
		assertIsTaanit( tishaBAv, 2015, Calendar.JULY, 26 );
		assertIsTaanit( tishaBAv, 2016, Calendar.AUGUST, 14 );
	}


	@Test
	public void testTzomGedaliah() {

		final TzomGedaliah tzomGedaliah = new TzomGedaliah();

		assertIsTaanit( tzomGedaliah, 2010, Calendar.SEPTEMBER, 12 );
		assertIsTaanit( tzomGedaliah, 2011, Calendar.OCTOBER, 2 );
		assertIsTaanit( tzomGedaliah, 2012, Calendar.SEPTEMBER, 19 );
		assertIsTaanit( tzomGedaliah, 2013, Calendar.SEPTEMBER, 8 );
		assertIsTaanit( tzomGedaliah, 2014, Calendar.SEPTEMBER, 28 );
		assertIsTaanit( tzomGedaliah, 2015, Calendar.SEPTEMBER, 16 );

		assertIsNotTaanit( tzomGedaliah, 2010, Calendar.SEPTEMBER, 8 );
		assertIsNotTaanit( tzomGedaliah, 2010, Calendar.SEPTEMBER, 9 );
		assertIsNotTaanit( tzomGedaliah, 2010, Calendar.SEPTEMBER, 10 );
	}


	private void assertIsTaanit(
		Observance  observance,
		int         year,
		@Constants.CalendarMonth
		int         month,
		int         dayOfMonth
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, dayOfMonth );
		Details details = observance.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertFalse( details.isErev() );

		date = date.yesterday();
		details = observance.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.isErev() );

		date = date.yesterday();
		details = observance.getDetailsFor( date );
		Assert.assertNull( details );

		date = HebrewCalendar.getForDate( year, month, dayOfMonth );
		date = date.tomorrow();
		details = observance.getDetailsFor( date );
		Assert.assertNull( details );
	}


	private void assertIsNotTaanit(
		Observance  observance,
		int         year,
		@Constants.CalendarMonth
		int         month,
		int         dayOfMonth
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, dayOfMonth );
		Details details = observance.getDetailsFor( date );
		Assert.assertNull( details );
	}

} // End of UT_Taaniyot class
