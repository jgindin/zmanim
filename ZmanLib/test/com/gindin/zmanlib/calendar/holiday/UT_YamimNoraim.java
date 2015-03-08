/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.Constants;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.yomtov.RoshHashana;
import com.gindin.zmanlib.calendar.holiday.yomtov.YomKippur;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_YamimNoraim
	extends UT_AbstractHoliday {

	private static final RoshHashana                          ROSH_HASHANA = new RoshHashana();
	private static final YomKippur                            YOM_KIPPUR = new YomKippur();


	@Test
	public void test2010() {
		runTest( 2010, Calendar.SEPTEMBER, 9,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/ 8/2010 W | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/ 9/2010 R | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/10/2010 F | Rosh Hashana                                  | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/17/2010 F | Erev Yom Kippur                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/18/2010 B | Yom Kippur                                    | true  | false | false        | false | true    | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2011() {
		runTest( 2011, Calendar.SEPTEMBER, 29,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/28/2011 W | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/29/2011 R | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/30/2011 F | Rosh Hashana                                  | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
			"10/ 7/2011 F | Erev Yom Kippur                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/ 8/2011 B | Yom Kippur                                    | true  | false | false        | false | true    | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		runTest( 2012, Calendar.SEPTEMBER, 17,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/16/2012 S | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/17/2012 M | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/18/2012 T | Rosh Hashana                                  | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n" +
			" 9/25/2012 T | Erev Yom Kippur                               | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/26/2012 W | Yom Kippur                                    | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2013() {
		runTest( 2013, Calendar.SEPTEMBER, 5,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/ 4/2013 W | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/ 5/2013 R | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/ 6/2013 F | Rosh Hashana                                  | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/13/2013 F | Erev Yom Kippur                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/14/2013 B | Yom Kippur                                    | true  | false | false        | false | true    | true    | true     | false  | false | false  |\n"
		);
	}

	@Test
	public void test2014() {
		runTest( 2014, Calendar.SEPTEMBER, 25,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/24/2014 W | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/25/2014 R | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/26/2014 F | Rosh Hashana                                  | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
			"10/ 3/2014 F | Erev Yom Kippur                               | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/ 4/2014 B | Yom Kippur                                    | true  | false | false        | false | true    | true    | true     | false  | false | false  |\n"
		);
	}
	@Test
	public void test2015() {
		runTest( 2015, Calendar.SEPTEMBER, 14,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/13/2015 S | Erev Rosh Hashana                             | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/14/2015 M | Rosh Hashana                                  | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
			" 9/15/2015 T | Rosh Hashana                                  | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n" +
			" 9/22/2015 T | Erev Yom Kippur                               | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/23/2015 W | Yom Kippur                                    | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
		);
	}



	private void runTest(
		int year,
		@Constants.CalendarMonth int month,
		int day,
		String expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate  date;
		Details details;

		date = HebrewCalendar.getForDate( year, month, day );

		date = date.yesterday();
		details = ROSH_HASHANA.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = ROSH_HASHANA.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = ROSH_HASHANA.getDetailsFor( date );
		outputDetails( output, details );

		date = date.forward( 7 );
		details = YOM_KIPPUR.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = YOM_KIPPUR.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}


} // End of UT_YamimNoraim class
