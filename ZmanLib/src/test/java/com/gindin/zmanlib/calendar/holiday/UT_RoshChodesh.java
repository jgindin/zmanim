/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
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

		// Shevat
		assertIsRoshChodesh( 2010, Calendar.JANUARY, 15, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/15/2010 F | Erev Rosh Chodesh Shevat                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 1/16/2010 B | Rosh Chodesh Shevat                           | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Adar
		assertIsRoshChodesh( 2010, Calendar.FEBRUARY, 13, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/13/2010 B | Erev Rosh Chodesh Adar                        | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 2/14/2010 S | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/15/2010 M | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Nisan
		assertIsRoshChodesh( 2010, Calendar.MARCH, 15, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/15/2010 M | Erev Rosh Chodesh Nissan                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/16/2010 T | Rosh Chodesh Nissan                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Iyar
		assertIsRoshChodesh( 2010, Calendar.APRIL, 13, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/13/2010 T | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/14/2010 W | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/15/2010 R | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Sivan
		assertIsRoshChodesh( 2010, Calendar.MAY, 13, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/13/2010 R | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/14/2010 F | Rosh Chodesh Sivan                            | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Tamuz
		assertIsRoshChodesh( 2010, Calendar.JUNE, 11, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/11/2010 F | Erev Rosh Chodesh Tammuz                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 6/12/2010 B | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 6/13/2010 S | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Av
		assertIsRoshChodesh( 2010, Calendar.JULY, 11, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/11/2010 S | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/12/2010 M | Rosh Chodesh Av                               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Elul
		assertIsRoshChodesh( 2010, Calendar.AUGUST, 9, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/ 9/2010 M | Erev Rosh Chodesh Elul                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/10/2010 T | Rosh Chodesh Elul (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/11/2010 W | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 8 );
		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 9 );
		assertIsNotRoshChodesh( 2010, Calendar.SEPTEMBER, 10 );
	}

	@Test
	public void test5771() {

		// Heshvan
		assertIsRoshChodesh( 2010, Calendar.OCTOBER, 7, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/ 7/2010 R | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/ 8/2010 F | Rosh Chodesh Heshvan (1)                      | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/ 9/2010 B | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Kislev
		assertIsRoshChodesh( 2010, Calendar.NOVEMBER, 6, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/ 6/2010 B | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"11/ 7/2010 S | Rosh Chodesh Kislev (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/ 8/2010 M | Rosh Chodesh Kislev (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Tevet
		assertIsRoshChodesh( 2010, Calendar.DECEMBER, 6, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/ 6/2010 M | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 7/2010 T | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 8/2010 W | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Shevat
		assertIsRoshChodesh( 2011, Calendar.JANUARY, 5, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/ 5/2011 W | Erev Rosh Chodesh Shevat                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/ 6/2011 R | Rosh Chodesh Shevat                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Adar I
		assertIsRoshChodesh( 2011, Calendar.FEBRUARY, 3, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/ 3/2011 R | Erev Rosh Chodesh Adar I                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 4/2011 F | Rosh Chodesh Adar I (1)                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 5/2011 B | Rosh Chodesh Adar I (2)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Adar II
		assertIsRoshChodesh( 2011, Calendar.MARCH, 5, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/ 5/2011 B | Erev Rosh Chodesh Adar II                     | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 3/ 6/2011 S | Rosh Chodesh Adar II (1)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 7/2011 M | Rosh Chodesh Adar II (2)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Nissan
		assertIsRoshChodesh( 2011, Calendar.APRIL, 4, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/ 4/2011 M | Erev Rosh Chodesh Nissan                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/ 5/2011 T | Rosh Chodesh Nissan                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Iyar
		assertIsRoshChodesh( 2011, Calendar.MAY, 3, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 3/2011 T | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/ 4/2011 W | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/ 5/2011 R | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Sivan
		assertIsRoshChodesh( 2011, Calendar.JUNE, 2, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/ 2/2011 R | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/ 3/2011 F | Rosh Chodesh Sivan                            | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Tammuz
		assertIsRoshChodesh( 2011, Calendar.JULY, 1, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/ 1/2011 F | Erev Rosh Chodesh Tammuz                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 7/ 2/2011 B | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 7/ 3/2011 S | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Av
		assertIsRoshChodesh( 2011, Calendar.JULY, 31, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/31/2011 S | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/ 1/2011 M | Rosh Chodesh Av                               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Elul
		assertIsRoshChodesh( 2011, Calendar.AUGUST, 29, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/29/2011 M | Erev Rosh Chodesh Elul                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/30/2011 T | Rosh Chodesh Elul (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/31/2011 W | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 28 );
		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 29 );
		assertIsNotRoshChodesh( 2011, Calendar.SEPTEMBER, 30 );
	}


	@Test
	public void test5772() {

		// Heshvan
		assertIsRoshChodesh( 2011, Calendar.OCTOBER, 27, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/27/2011 R | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/28/2011 F | Rosh Chodesh Heshvan (1)                      | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/29/2011 B | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Kislev
		assertIsRoshChodesh( 2011, Calendar.NOVEMBER, 26, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/26/2011 B | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"11/27/2011 S | Rosh Chodesh Kislev                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Tevet
		assertIsRoshChodesh( 2011, Calendar.DECEMBER, 25, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/25/2011 S | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/26/2011 M | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/27/2011 T | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Shevat
		assertIsRoshChodesh( 2012, Calendar.JANUARY, 24, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/24/2012 T | Erev Rosh Chodesh Shevat                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/25/2012 W | Rosh Chodesh Shevat                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Adar
		assertIsRoshChodesh( 2012, Calendar.FEBRUARY, 22, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/22/2012 W | Erev Rosh Chodesh Adar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/23/2012 R | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/24/2012 F | Rosh Chodesh Adar                             | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Nisan
		assertIsRoshChodesh( 2012, Calendar.MARCH, 23, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/23/2012 F | Erev Rosh Chodesh Nissan                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 3/24/2012 B | Rosh Chodesh Nissan                           | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Iyar
		assertIsRoshChodesh( 2012, Calendar.APRIL, 21, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/21/2012 B | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 4/22/2012 S | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/23/2012 M | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Sivan
		assertIsRoshChodesh( 2012, Calendar.MAY, 21, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/21/2012 M | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/22/2012 T | Rosh Chodesh Sivan                            | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Tamuz
		assertIsRoshChodesh( 2012, Calendar.JUNE, 19, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/19/2012 T | Erev Rosh Chodesh Tammuz                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/20/2012 W | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/21/2012 R | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Av
		assertIsRoshChodesh( 2012, Calendar.JULY, 19, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/19/2012 R | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/20/2012 F | Rosh Chodesh Av                               | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Elul
		assertIsRoshChodesh( 2012, Calendar.AUGUST, 17, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/17/2012 F | Erev Rosh Chodesh Elul                        | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 8/18/2012 B | Rosh Chodesh Elul (1)                         | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 8/19/2012 S | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 16 );
		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 17 );
		assertIsNotRoshChodesh( 2012, Calendar.SEPTEMBER, 18 );
	}


	@Test
	public void test5773() {

		// Heshvan
		assertIsRoshChodesh( 2012, Calendar.OCTOBER, 15, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/15/2012 M | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/16/2012 T | Rosh Chodesh Heshvan (1)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/17/2012 W | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
	);

		// Kislev
		assertIsRoshChodesh( 2012, Calendar.NOVEMBER, 14, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/14/2012 W | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/15/2012 R | Rosh Chodesh Kislev                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Tevet
		assertIsRoshChodesh( 2012, Calendar.DECEMBER, 13, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/13/2012 R | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/14/2012 F | Rosh Chodesh Tevet                            | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Shevat
		assertIsRoshChodesh( 2013, Calendar.JANUARY, 11, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/11/2013 F | Erev Rosh Chodesh Shevat                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 1/12/2013 B | Rosh Chodesh Shevat                           | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Adar
		assertIsRoshChodesh( 2013, Calendar.FEBRUARY, 9, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/ 9/2013 B | Erev Rosh Chodesh Adar                        | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 2/10/2013 S | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/11/2013 M | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Nisan
		assertIsRoshChodesh( 2013, Calendar.MARCH, 11, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/11/2013 M | Erev Rosh Chodesh Nissan                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/12/2013 T | Rosh Chodesh Nissan                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Iyar
		assertIsRoshChodesh( 2013, Calendar.APRIL, 9, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/ 9/2013 T | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/10/2013 W | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/11/2013 R | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Sivan
		assertIsRoshChodesh( 2013, Calendar.MAY, 9, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 9/2013 R | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/10/2013 F | Rosh Chodesh Sivan                            | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Tammuz
		assertIsRoshChodesh( 2013, Calendar.JUNE, 7, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/ 7/2013 F | Erev Rosh Chodesh Tammuz                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 6/ 8/2013 B | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 6/ 9/2013 S | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Av
		assertIsRoshChodesh( 2013, Calendar.JULY, 7, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/ 7/2013 S | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/ 8/2013 M | Rosh Chodesh Av                               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Elul
		assertIsRoshChodesh( 2013, Calendar.AUGUST, 5, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/ 5/2013 M | Erev Rosh Chodesh Elul                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/ 6/2013 T | Rosh Chodesh Elul (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/ 7/2013 W | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 4 );
		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 5 );
		assertIsNotRoshChodesh( 2013, Calendar.SEPTEMBER, 6 );
	}


	@Test
	public void test5774() {

		// Heshvan
		assertIsRoshChodesh( 2013, Calendar.OCTOBER, 3, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/ 3/2013 R | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/ 4/2013 F | Rosh Chodesh Heshvan (1)                      | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/ 5/2013 B | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Kislev
		assertIsRoshChodesh( 2013, Calendar.NOVEMBER, 2, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/ 2/2013 B | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"11/ 3/2013 S | Rosh Chodesh Kislev (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/ 4/2013 M | Rosh Chodesh Kislev (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Tevet
		assertIsRoshChodesh( 2013, Calendar.DECEMBER, 2, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/ 2/2013 M | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 3/2013 T | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/ 4/2013 W | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Shevat
		assertIsRoshChodesh( 2014, Calendar.JANUARY, 1, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/ 1/2014 W | Erev Rosh Chodesh Shevat                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/ 2/2014 R | Rosh Chodesh Shevat                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Adar I
		assertIsRoshChodesh( 2014, Calendar.JANUARY, 30, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/30/2014 R | Erev Rosh Chodesh Adar I                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/31/2014 F | Rosh Chodesh Adar I (1)                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 1/2014 B | Rosh Chodesh Adar I (2)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		// Adar II
		assertIsRoshChodesh( 2014, Calendar.MARCH, 1, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/ 1/2014 B | Erev Rosh Chodesh Adar II                     | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 3/ 2/2014 S | Rosh Chodesh Adar II (1)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/ 3/2014 M | Rosh Chodesh Adar II (2)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Nisan
		assertIsRoshChodesh( 2014, Calendar.MARCH, 31, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/31/2014 M | Erev Rosh Chodesh Nissan                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/ 1/2014 T | Rosh Chodesh Nissan                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Iyar
		assertIsRoshChodesh( 2014, Calendar.APRIL, 29, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/29/2014 T | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/30/2014 W | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/ 1/2014 R | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Sivan
		assertIsRoshChodesh( 2014, Calendar.MAY, 29, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/29/2014 R | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/30/2014 F | Rosh Chodesh Sivan                            | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		// Tamuz
		assertIsRoshChodesh( 2014, Calendar.JUNE, 27, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/27/2014 F | Erev Rosh Chodesh Tammuz                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 6/28/2014 B | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 6/29/2014 S | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Av
		assertIsRoshChodesh( 2014, Calendar.JULY, 27, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/27/2014 S | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/28/2014 M | Rosh Chodesh Av                               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		// Elul
		assertIsRoshChodesh( 2014, Calendar.AUGUST, 25, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/25/2014 M | Erev Rosh Chodesh Elul                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/26/2014 T | Rosh Chodesh Elul (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/27/2014 W | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 24 );
		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 25 );
		assertIsNotRoshChodesh( 2014, Calendar.SEPTEMBER, 26 );
	}


	@Test
	public void test5775() {

		assertIsRoshChodesh( 2014, Calendar.OCTOBER, 23, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/23/2014 R | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/24/2014 F | Rosh Chodesh Heshvan (1)                      | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"10/25/2014 B | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2014, Calendar.NOVEMBER, 22, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/22/2014 B | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"11/23/2014 S | Rosh Chodesh Kislev                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2014, Calendar.DECEMBER, 21, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/21/2014 S | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/22/2014 M | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/23/2014 T | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.JANUARY, 20, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/20/2015 T | Erev Rosh Chodesh Shevat                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/21/2015 W | Rosh Chodesh Shevat                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.FEBRUARY, 18, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/18/2015 W | Erev Rosh Chodesh Adar                        | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/19/2015 R | Rosh Chodesh Adar                             | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/20/2015 F | Rosh Chodesh Adar                             | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.MARCH, 20, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/20/2015 F | Erev Rosh Chodesh Nissan                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 3/21/2015 B | Rosh Chodesh Nissan                           | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.APRIL, 18, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/18/2015 B | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 4/19/2015 S | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/20/2015 M | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.MAY, 18, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/18/2015 M | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/19/2015 T | Rosh Chodesh Sivan                            | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.JUNE, 16, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/16/2015 T | Erev Rosh Chodesh Tammuz                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/17/2015 W | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/18/2015 R | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.JULY, 16, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/16/2015 R | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/17/2015 F | Rosh Chodesh Av                               | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.AUGUST, 14, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/14/2015 F | Erev Rosh Chodesh Elul                        | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 8/15/2015 B | Rosh Chodesh Elul (1)                         | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 8/16/2015 S | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 13 );
		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 14 );
		assertIsNotRoshChodesh( 2015, Calendar.SEPTEMBER, 15 );
	}


	@Test
	public void test5776() {

		assertIsRoshChodesh( 2015, Calendar.OCTOBER, 12, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"10/12/2015 M | Erev Rosh Chodesh Heshvan                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/13/2015 T | Rosh Chodesh Heshvan (1)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"10/14/2015 W | Rosh Chodesh Heshvan (2)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.NOVEMBER, 11, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"11/11/2015 W | Erev Rosh Chodesh Kislev                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/12/2015 R | Rosh Chodesh Kislev (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"11/13/2015 F | Rosh Chodesh Kislev (2)                       | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2015, Calendar.DECEMBER, 11, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/11/2015 F | Erev Rosh Chodesh Tevet                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			"12/12/2015 B | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			"12/13/2015 S | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.JANUARY, 10, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 1/10/2016 S | Erev Rosh Chodesh Shevat                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/11/2016 M | Rosh Chodesh Shevat                           | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.FEBRUARY, 8, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 2/ 8/2016 M | Erev Rosh Chodesh Adar I                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/ 9/2016 T | Rosh Chodesh Adar I (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 2/10/2016 W | Rosh Chodesh Adar I (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.MARCH, 9, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/ 9/2016 W | Erev Rosh Chodesh Adar II                     | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/10/2016 R | Rosh Chodesh Adar II (1)                      | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 3/11/2016 F | Rosh Chodesh Adar II (2)                      | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.APRIL, 8, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/ 8/2016 F | Erev Rosh Chodesh Nissan                      | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 4/ 9/2016 B | Rosh Chodesh Nissan                           | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.MAY, 7, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 7/2016 B | Erev Rosh Chodesh Iyar                        | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/ 8/2016 S | Rosh Chodesh Iyar (1)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/ 9/2016 M | Rosh Chodesh Iyar (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.JUNE, 6, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 6/ 6/2016 M | Erev Rosh Chodesh Sivan                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 6/ 7/2016 T | Rosh Chodesh Sivan                            | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.JULY, 5, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 7/ 5/2016 T | Erev Rosh Chodesh Tammuz                      | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/ 6/2016 W | Rosh Chodesh Tammuz (1)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 7/ 7/2016 R | Rosh Chodesh Tammuz (2)                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.AUGUST, 4, false,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 8/ 4/2016 R | Erev Rosh Chodesh Av                          | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 8/ 5/2016 F | Rosh Chodesh Av                               | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsRoshChodesh( 2016, Calendar.SEPTEMBER, 2, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 9/ 2/2016 F | Erev Rosh Chodesh Elul                        | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
			" 9/ 3/2016 B | Rosh Chodesh Elul (1)                         | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 9/ 4/2016 S | Rosh Chodesh Elul (2)                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 2 );
		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 3 );
		assertIsNotRoshChodesh( 2016, Calendar.OCTOBER, 4 );
	}


	@Test
	public void test2024() {

		// This is interesting because Rosh Chodesh Tevet starts in December, but carries over
		//  into January...
		assertIsRoshChodesh( 2024, Calendar.DECEMBER, 30, true,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			"12/30/2024 M | Erev Rosh Chodesh Tevet                       | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			"12/31/2024 T | Rosh Chodesh Tevet (1)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 1/ 1/2025 W | Rosh Chodesh Tevet (2)                        | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}


	private void assertIsRoshChodesh(
		int                 year,
		@CalendarMonth int  month,
		int                 day,
		boolean             twoDays,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = ROSH_CHODESH.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = ROSH_CHODESH.getDetailsFor( date );
		outputDetails( output, details );

		date = date.tomorrow();
		details = ROSH_CHODESH.getDetailsFor( date );
		if ( twoDays ) {
			outputDetails( output, details );
		}
		else {
			Assert.assertNull( details );
		}

		Assert.assertEquals( expected, output.toString() );
	}


	private void assertIsNotRoshChodesh(
		int                 year,
		@CalendarMonth int  month,
		int                 day
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = ROSH_CHODESH.getDetailsFor( date );
		Assert.assertNull( details );
	}

} // End of UT_RoshChodesh class
