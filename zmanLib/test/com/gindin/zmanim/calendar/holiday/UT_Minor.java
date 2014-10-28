/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.calendar.holiday;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.calendar.holiday.minor.TuBAv;
import com.gindin.zmanim.calendar.holiday.minor.TuBShvat;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Minor
	 extends UT_AbstractHoliday {

	private static final TuBShvat                             TU_B_SHVAT = new TuBShvat();
	private static final TuBAv                                TU_B_AV = new TuBAv();

	
	@Test
	public void test2010() {
		runTuBAvTest( 2010, Calendar.JULY, 26,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/25/2010 S | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/26/2010 M | Tu B'Av                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2010, Calendar.JANUARY, 30,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/29/2010 F | Erev Tu B'Shvat                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 1/30/2010 B | Tu B'Shvat                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2011() {
		runTuBAvTest( 2011, Calendar.AUGUST, 15,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/14/2011 S | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/15/2011 M | Tu B'Av                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2011, Calendar.JANUARY, 20,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/19/2011 W | Erev Tu B'Shvat                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/20/2011 R | Tu B'Shvat                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		runTuBAvTest( 2012, Calendar.AUGUST, 3,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/ 2/2012 R | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/ 3/2012 F | Tu B'Av                                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2012, Calendar.FEBRUARY, 8,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/ 7/2012 T | Erev Tu B'Shvat                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 8/2012 W | Tu B'Shvat                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2013() {
		runTuBAvTest( 2013, Calendar.JULY, 22,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/21/2013 S | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/22/2013 M | Tu B'Av                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2013, Calendar.JANUARY, 26,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/25/2013 F | Erev Tu B'Shvat                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 1/26/2013 B | Tu B'Shvat                                    | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2014() {
		runTuBAvTest( 2014, Calendar.AUGUST, 11,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/10/2014 S | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/11/2014 M | Tu B'Av                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2014, Calendar.JANUARY, 16,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/15/2014 W | Erev Tu B'Shvat                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/16/2014 R | Tu B'Shvat                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2015() {
		runTuBAvTest( 2015, Calendar.JULY, 31,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/30/2015 R | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/31/2015 F | Tu B'Av                                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2015, Calendar.FEBRUARY, 4,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/ 3/2015 T | Erev Tu B'Shvat                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 4/2015 W | Tu B'Shvat                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2016() {
		runTuBAvTest( 2016, Calendar.AUGUST, 19,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/18/2016 R | Erev Tu B'Av                                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/19/2016 F | Tu B'Av                                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);
		runTuBShvatTest( 2016, Calendar.JANUARY, 25,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/24/2016 S | Erev Tu B'Shvat                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/25/2016 M | Tu B'Shvat                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}
	
	
	private void runTuBShvatTest(
		int year,
		@Constants.CalendarMonth int month,
		int day,
		String expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date;
		Details details;
		
		date = HebrewCalendar.getForDate( year, month, day );
		details = TU_B_SHVAT.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		details = TU_B_SHVAT.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}
	
	
	private void runTuBAvTest(
		int year,
		@Constants.CalendarMonth int month,
		int day,
		String expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date;
		Details details;
		
		date = HebrewCalendar.getForDate( year, month, day );
		details = TU_B_AV.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		details = TU_B_AV.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}

} // End of UT_Minor class
