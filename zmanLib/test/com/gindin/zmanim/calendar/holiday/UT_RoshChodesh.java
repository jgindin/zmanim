/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_RoshChodesh
	extends UT_AbstractHoliday {

	private static final RoshChodesh                          ROSH_CHODESH = new RoshChodesh();

	@Test
	public void test5770() {

		assertIsRoshChodesh( 2010, Calendar.JANUARY, 15, false );   // Shevat
		assertIsRoshChodesh( 2010, Calendar.FEBRUARY, 13, true );   // Adar
		assertIsRoshChodesh( 2010, Calendar.MARCH, 15, false );     // Nisan
		assertIsRoshChodesh( 2010, Calendar.APRIL, 13, true );      // Iyar
		assertIsRoshChodesh( 2010, Calendar.MAY, 13, false );       // Sivan
		assertIsRoshChodesh( 2010, Calendar.JUNE, 11, true );       // Tamuz
		assertIsRoshChodesh( 2010, Calendar.JULY, 11, false );      // Av
		assertIsRoshChodesh( 2010, Calendar.AUGUST, 9, true );      // Elul

		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 8 );
		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 9 );
		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 10 );
	}

	@Test
	public void test5771() {

		assertIsRoshChodesh( 2010, Calendar.OCTOBER, 7, true );     // Heshvan
		assertIsRoshChodesh( 2010, Calendar.NOVEMBER, 6, true );    // Kislev
		assertIsRoshChodesh( 2010, Calendar.DECEMBER, 6, true );    // Tevet
		assertIsRoshChodesh( 2011, Calendar.JANUARY, 5, false );    // Shevat
		assertIsRoshChodesh( 2011, Calendar.FEBRUARY, 3, true );    // Adar I
		assertIsRoshChodesh( 2011, Calendar.MARCH, 5, true );       // Adar II
		assertIsRoshChodesh( 2011, Calendar.APRIL, 4, false );      // Nissan
		assertIsRoshChodesh( 2011, Calendar.MAY, 3, true );         // Iyar
		assertIsRoshChodesh( 2011, Calendar.JUNE, 2, false );       // Sivan
		assertIsRoshChodesh( 2011, Calendar.JULY, 1, true );        // Tammuz
		assertIsRoshChodesh( 2011, Calendar.JULY, 31, false );      // Av
		assertIsRoshChodesh( 2011, Calendar.AUGUST, 29, true );     // Elul

		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 28 );
		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 29 );
		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 30 );
	}


	@Test
	public void test5772() {

		assertIsRoshChodesh( 2011, Calendar.OCTOBER, 27, true );    // Heshvan
		assertIsRoshChodesh( 2011, Calendar.NOVEMBER, 26, false );  // Kislev
		assertIsRoshChodesh( 2011, Calendar.DECEMBER, 25, true );   // Tevet
		assertIsRoshChodesh( 2012, Calendar.JANUARY, 24, false );   // Shevat
		assertIsRoshChodesh( 2012, Calendar.FEBRUARY, 22, true );   // Adar
		assertIsRoshChodesh( 2012, Calendar.MARCH, 23, false );     // Nisan
		assertIsRoshChodesh( 2012, Calendar.APRIL, 21, true );      // Iyar
		assertIsRoshChodesh( 2012, Calendar.MAY, 21, false );       // Sivan
		assertIsRoshChodesh( 2012, Calendar.JUNE, 19, true );       // Tamuz
		assertIsRoshChodesh( 2012, Calendar.JULY, 19, false );      // Av
		assertIsRoshChodesh( 2012, Calendar.AUGUST, 17, true );     // Elul

		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 16 );
		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 17 );
		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 18 );
	}


	@Test
	public void test5773() {

		assertIsRoshChodesh( 2012, Calendar.OCTOBER, 15, true );    // Heshvan
		assertIsRoshChodesh( 2012, Calendar.NOVEMBER, 14, false );  // Kislev
		assertIsRoshChodesh( 2012, Calendar.DECEMBER, 13, false );   // Tevet
		assertIsRoshChodesh( 2013, Calendar.JANUARY, 11, false );   // Shevat
		assertIsRoshChodesh( 2013, Calendar.FEBRUARY, 9, true );    // Adar
		assertIsRoshChodesh( 2013, Calendar.MARCH, 11, false );     // Nisan
		assertIsRoshChodesh( 2013, Calendar.APRIL, 9, true );       // Iyar
		assertIsRoshChodesh( 2013, Calendar.MAY, 9, false );        // Sivan
		assertIsRoshChodesh( 2013, Calendar.JUNE, 7, true );        // Tammuz
		assertIsRoshChodesh( 2013, Calendar.JULY, 7, false );       // Av
		assertIsRoshChodesh( 2013, Calendar.AUGUST, 5, true );      // Elul

		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 4 );
		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 5 );
		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 6 );
	}


	@Test
	public void test5774() {

		assertIsRoshChodesh( 2013, Calendar.OCTOBER, 3, true );     // Heshvan
		assertIsRoshChodesh( 2013, Calendar.NOVEMBER, 2, true );    // Kislev
		assertIsRoshChodesh( 2013, Calendar.DECEMBER, 2, true );    // Tevet
		assertIsRoshChodesh( 2014, Calendar.JANUARY, 1, false );    // Shevat
		assertIsRoshChodesh( 2014, Calendar.JANUARY, 30, true );    // Adar I
		assertIsRoshChodesh( 2014, Calendar.MARCH, 1, true );       // Adar II
		assertIsRoshChodesh( 2014, Calendar.MARCH, 31, false );     // Nisan
		assertIsRoshChodesh( 2014, Calendar.APRIL, 29, true );      // Iyar
		assertIsRoshChodesh( 2014, Calendar.MAY, 29, false );       // Sivan
		assertIsRoshChodesh( 2014, Calendar.JUNE, 27, true );       // Tamuz
		assertIsRoshChodesh( 2014, Calendar.JULY, 27, false );      // Av
		assertIsRoshChodesh( 2014, Calendar.AUGUST, 25, true );     // Elul

		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 24 );
		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 25 );
		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 26 );
	}


	@Test
	public void test5775() {

		assertIsRoshChodesh( 2014, Calendar.OCTOBER, 23, true );    // Heshvan
		assertIsRoshChodesh( 2014, Calendar.NOVEMBER, 22, false );  // Kislev
		assertIsRoshChodesh( 2014, Calendar.DECEMBER, 21, true );   // Tevet
		assertIsRoshChodesh( 2015, Calendar.JANUARY, 20, false );   // Shevat
		assertIsRoshChodesh( 2015, Calendar.FEBRUARY, 18, true );   // Adar
		assertIsRoshChodesh( 2015, Calendar.MARCH, 20, false );     // Nisan
		assertIsRoshChodesh( 2015, Calendar.APRIL, 18, true );      // Iyar
		assertIsRoshChodesh( 2015, Calendar.MAY, 18, false );       // Sivan
		assertIsRoshChodesh( 2015, Calendar.JUNE, 16, true );       // Tamuz
		assertIsRoshChodesh( 2015, Calendar.JULY, 16, false );      // Av
		assertIsRoshChodesh( 2015, Calendar.AUGUST, 14, true );     // Elul

		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 13 );
		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 14 );
		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 15 );
	}


	@Test
	public void test5776() {

		assertIsRoshChodesh( 2015, Calendar.OCTOBER, 12, true );    // Heshvan
		assertIsRoshChodesh( 2015, Calendar.NOVEMBER, 11, true );   // Kislev
		assertIsRoshChodesh( 2015, Calendar.DECEMBER, 11, true );   // Tevet
		assertIsRoshChodesh( 2016, Calendar.JANUARY, 10, false );   // Shevat
		assertIsRoshChodesh( 2016, Calendar.FEBRUARY, 8, true );    // Adar I
		assertIsRoshChodesh( 2016, Calendar.MARCH, 9, true );       // Adar II
		assertIsRoshChodesh( 2016, Calendar.APRIL, 8, false );      // Nisan
		assertIsRoshChodesh( 2016, Calendar.MAY, 7, true );         // Iyar
		assertIsRoshChodesh( 2016, Calendar.JUNE, 6, false );       // Sivan
		assertIsRoshChodesh( 2016, Calendar.JULY, 5, true );        // Tamuz
		assertIsRoshChodesh( 2016, Calendar.AUGUST, 4, false );     // Av
		assertIsRoshChodesh( 2016, Calendar.SEPTEMBER, 2, true );   // Elul

		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 2 );
		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 3 );
		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 4 );
	}


	@Test
	public void test2024() {

		// This is interesting because Rosh Chodesh Tevet starts in December, but carries over
		//  into January...
		assertIsRoshChodesh( 2024, Calendar.DECEMBER, 30, true );
	}


	private void assertIsRoshChodesh(
		int year,
		@Constants.CalendarMonth int month,
		int day,
	  boolean twoDays
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = ROSH_CHODESH.getDetailsFor( date );
		checkDate( details, true, false, false );

		date = date.tomorrow();
		details = ROSH_CHODESH.getDetailsFor( date );
		checkDate( details, false, false, false );

		date = date.tomorrow();
		details = ROSH_CHODESH.getDetailsFor( date );
		if ( twoDays ) {
			checkDate( details, false, false, false );
		}
		else {
			Assert.assertNull( details );
		}
	}


	private void assertIsNotRoshChodesh(
		int year,
		@Constants.CalendarMonth int month,
		int day
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = ROSH_CHODESH.getDetailsFor( date );
		Assert.assertNull( details );
	}

} // End of UT_RoshChodesh class
