/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.rabbanan.Purim;
import com.gindin.zmanlib.calendar.holiday.minor.PurimKatan;
import com.gindin.zmanlib.calendar.holiday.rabbanan.ShushanPurim;
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
		runPurimTest( 2011, Calendar.MARCH, 20,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/19/2011 B | Erev Purim                                    | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 3/20/2011 S | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/21/2011 M | Shushan Purim                                 | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		runPurimKatanTest( 2011, Calendar.FEBRUARY, 18,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/17/2011 R | Erev Purim Katan                              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/18/2011 F | Purim Katan                                   | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		runPurimTest( 2012, Calendar.MARCH, 8,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/ 7/2012 W | Erev Purim                                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 8/2012 R | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 9/2012 F | Shushan Purim                                 | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);


		// doesn't matter...this isn't a leap year...
		runPurimKatanTest( 2012, Calendar.MARCH, 22,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n"
		);
	}

	@Test
	public void test2013() {
		runPurimTest( 2013, Calendar.FEBRUARY, 24,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/23/2013 B | Erev Purim                                    | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 2/24/2013 S | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/25/2013 M | Shushan Purim                                 | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// doesn't matter...this isn't a leap year...
		runPurimKatanTest( 2013, Calendar.FEBRUARY, 28,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n"
		);
	}

	@Test
	public void test2014() {
		runPurimTest( 2014, Calendar.MARCH, 16,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/15/2014 B | Erev Purim                                    | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 3/16/2014 S | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/17/2014 M | Shushan Purim                                 | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		runPurimKatanTest( 2014, Calendar.FEBRUARY, 14,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/13/2014 R | Erev Purim Katan                              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/14/2014 F | Purim Katan                                   | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2015() {
		runPurimTest( 2015, Calendar.MARCH, 5,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/ 4/2015 W | Erev Purim                                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 5/2015 R | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 6/2015 F | Shushan Purim                                 | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// doesn't matter...this isn't a leap year...
		runPurimKatanTest( 2015, Calendar.MARCH, 18,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n"
		);
	}

	@Test
	public void test2016() {
		runPurimTest( 2016, Calendar.MARCH, 24,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/23/2016 W | Erev Purim                                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/24/2016 R | Purim                                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/25/2016 F | Shushan Purim                                 | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		runPurimKatanTest( 2016, Calendar.FEBRUARY, 23,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/22/2016 M | Erev Purim Katan                              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/23/2016 T | Purim Katan                                   | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}


	private void runPurimTest(
		int                 year,
		@CalendarMonth int  purimMonth,
		int                 purimDay,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate  date;
		Details details;

		// Yesterday should be erev Purim.
		date = HebrewCalendar.getForDate( year, purimMonth, purimDay );
		details = PURIM.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		// Check today -- should be Purim.
		details = PURIM.getDetailsFor( date );
		outputDetails( output, details );

		// Day after Purim is Shushan Purim.
	  date = date.tomorrow();
		details = SHUSHAN_PURIM.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}


	private void runPurimKatanTest(
		int                 year,
		@CalendarMonth int  purimMonth,
		int                 purimDay,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate  date;
		Details details;

		date = HebrewCalendar.getForDate( year, purimMonth, purimDay );
		details = PURIM_KATAN.getDetailsFor( date.yesterday() );

		if ( date.isHebrewLeapYear() ) {
			outputDetails( output, details );
		}
		else {
			Assert.assertNull( details );
		}

		details = PURIM_KATAN.getDetailsFor( date );
		if ( date.isHebrewLeapYear() ) {
			outputDetails( output, details );
		}
		else {
			Assert.assertNull( details );
		}

		Assert.assertEquals( expected, output.toString() );
	}

} // End of UT_Purim class
