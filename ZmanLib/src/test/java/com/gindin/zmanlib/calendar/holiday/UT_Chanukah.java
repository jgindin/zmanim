/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.Constants;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.rabbanan.Chanukah;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Chanukah
	extends UT_AbstractHoliday {

	private static final Chanukah                             CHANUKAH = new Chanukah();

	@Test
	public void test2010() {
		runTest( 2010, Calendar.DECEMBER, 2,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/ 1/2010 W | Erev Chanukah                                 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 2/2010 R | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 3/2010 F | Chanukah 2                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/ 4/2010 B | Chanukah 3                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/ 5/2010 S | Chanukah 4                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 6/2010 M | Chanukah 5                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 7/2010 T | Chanukah 6                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 8/2010 W | Chanukah 7                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 9/2010 R | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.DECEMBER, 21,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/20/2011 T | Erev Chanukah                                 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/21/2011 W | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/22/2011 R | Chanukah 2                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/23/2011 F | Chanukah 3                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/24/2011 B | Chanukah 4                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/25/2011 S | Chanukah 5                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/26/2011 M | Chanukah 6                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/27/2011 T | Chanukah 7                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/28/2011 W | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.DECEMBER, 9,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/ 8/2012 B | Erev Chanukah                                 | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/ 9/2012 S | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/10/2012 M | Chanukah 2                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/11/2012 T | Chanukah 3                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/12/2012 W | Chanukah 4                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/13/2012 R | Chanukah 5                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/14/2012 F | Chanukah 6                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/15/2012 B | Chanukah 7                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/16/2012 S | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.NOVEMBER, 28,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/27/2013 W | Erev Chanukah                                 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/28/2013 R | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/29/2013 F | Chanukah 2                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"11/30/2013 B | Chanukah 3                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/ 1/2013 S | Chanukah 4                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 2/2013 M | Chanukah 5                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 3/2013 T | Chanukah 6                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 4/2013 W | Chanukah 7                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 5/2013 R | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.DECEMBER, 17,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/16/2014 T | Erev Chanukah                                 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/17/2014 W | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/18/2014 R | Chanukah 2                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/19/2014 F | Chanukah 3                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/20/2014 B | Chanukah 4                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/21/2014 S | Chanukah 5                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/22/2014 M | Chanukah 6                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/23/2014 T | Chanukah 7                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/24/2014 W | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2015() {
		runTest( 2015, Calendar.DECEMBER, 7,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/ 6/2015 S | Erev Chanukah                                 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 7/2015 M | Chanukah 1                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 8/2015 T | Chanukah 2                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 9/2015 W | Chanukah 3                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/10/2015 R | Chanukah 4                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/11/2015 F | Chanukah 5                                    | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/12/2015 B | Chanukah 6                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/13/2015 S | Chanukah 7                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/14/2015 M | Chanukah 8                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	private void runTest(
		int year,
		@Constants.CalendarMonth int chanukahMonth,
		int startingDay,
		String expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date;
		Details details;

		date = HebrewCalendar.getForDate( year, chanukahMonth, startingDay );
		date = date.yesterday();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "1" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "2" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "3" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "4" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "5" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "6" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "7" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		outputDetails( output, details );
		Assert.assertTrue( details.getDescription().endsWith( "8" ) );

		date = date.tomorrow();
		details = CHANUKAH.getDetailsFor( date );
		Assert.assertNull( details );

		Assert.assertEquals( expected, output.toString() );

	}

} // End of UT_Chanukah class
