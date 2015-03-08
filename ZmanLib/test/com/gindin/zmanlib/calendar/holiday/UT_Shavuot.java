/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.Constants;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.yomtov.Shavuot;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Shavuot
	extends UT_AbstractHoliday {

	private static final Shavuot                              SHAVUOT = new Shavuot();

	@Test
	public void test2010() {
		runTest( 2010, Calendar.MAY, 19,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/18/2010 T | Erev Shavuot                                  | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 5/19/2010 W | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 5/20/2010 R | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.JUNE, 8,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/ 7/2011 T | Erev Shavuot                                  | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 6/ 8/2011 W | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 6/ 9/2011 R | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.MAY, 27,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/26/2012 B | Erev Shavuot                                  | true  | true  | false        | false | true    | false   | false    | false  | false | false  |\n" +
			" 5/27/2012 S | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 5/28/2012 M | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.MAY, 15,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/14/2013 T | Erev Shavuot                                  | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 5/15/2013 W | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 5/16/2013 R | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.JUNE, 4,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/ 3/2014 T | Erev Shavuot                                  | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 6/ 4/2014 W | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 6/ 5/2014 R | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.MAY, 24,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/23/2015 B | Erev Shavuot                                  | true  | true  | false        | false | true    | false   | false    | false  | false | false  |\n" +
			" 5/24/2015 S | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 5/25/2015 M | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2016() {
		runTest( 2016, Calendar.JUNE, 12,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/11/2016 B | Erev Shavuot                                  | true  | true  | false        | false | true    | false   | false    | false  | false | false  |\n" +
			" 6/12/2016 S | Shavuot                                       | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 6/13/2016 M | Shavuot                                       | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}



	private void runTest(
		int year,
		@Constants.CalendarMonth int month,
		int day,
		String expected
	) {

		StringBuilder output = buildDetailsHeader();
		
		HebrewDate date;
		Details details;

		date = HebrewCalendar.getForDate( year, month, day );
		details = SHAVUOT.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		details = SHAVUOT.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = SHAVUOT.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}

} // End of UT_Shavuot class
