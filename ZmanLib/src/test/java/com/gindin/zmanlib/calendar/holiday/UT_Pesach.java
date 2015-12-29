/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.minor.LagBOmer;
import com.gindin.zmanlib.calendar.holiday.minor.Omer;
import com.gindin.zmanlib.calendar.holiday.minor.PesachSheni;
import com.gindin.zmanlib.calendar.holiday.yomtov.Pesach;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Pesach
	extends UT_AbstractHoliday {

	private static final Pesach                               PESACH = new Pesach();
	private static final PesachSheni                          PESACH_SHENI = new PesachSheni();
	private static final Omer                                 OMER = new Omer();
	private static final LagBOmer                             LAG_B_OMER = new LagBOmer();

	@Test
	public void test2010() {
		assertPesach( 2010, Calendar.MARCH, 30,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/29/2010 M | Erev Pesach                                   | true  | true  | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 3/30/2010 T | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 3/31/2010 W | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/ 1/2010 R | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 2/2010 F | Chol HaMoed Pesach 4                          | true  | false | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 3/2010 B | Chol HaMoed Pesach 5                          | true  | false | false        | false | true    | false   | true     | true   | false | false  |\n" +
			" 4/ 4/2010 S | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 5/2010 M | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/ 6/2010 T | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2010, Calendar.APRIL, 28,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/27/2010 T | Erev Pesach Sheni                             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/28/2010 W | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertOmer( 2010, Calendar.MARCH, 31, 1 );
		assertOmer( 2010, Calendar.APRIL, 4, 5 );
		assertOmer( 2010, Calendar.APRIL, 19, 20 );
		assertOmer( 2010, Calendar.MAY, 1, 32 );
		assertOmer( 2010, Calendar.MAY, 18, 49 );

		assertNotOmer( 2010, Calendar.MARCH, 30 );
		assertNotOmer( 2010, Calendar.MAY, 19 );

		assertLagBOmer( 2010, Calendar.MAY, 2,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 1/2010 B | Erev Lag B'Omer                               | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/ 2/2010 S | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2011() {
		assertPesach( 2011, Calendar.APRIL, 19,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/18/2011 M | Erev Pesach                                   | true  | true  | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/19/2011 T | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/20/2011 W | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/21/2011 R | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/22/2011 F | Chol HaMoed Pesach 4                          | true  | false | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/23/2011 B | Chol HaMoed Pesach 5                          | true  | false | false        | false | true    | false   | true     | true   | false | false  |\n" +
			" 4/24/2011 S | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/25/2011 M | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/26/2011 T | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2011, Calendar.MAY, 18,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/17/2011 T | Erev Pesach Sheni                             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/18/2011 W | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertOmer( 2011, Calendar.APRIL, 21, 2 );
		assertOmer( 2011, Calendar.APRIL, 28, 9 );
		assertOmer( 2011, Calendar.MAY, 11, 22 );
		assertOmer( 2011, Calendar.MAY, 30, 41 );
		assertOmer( 2011, Calendar.JUNE, 3, 45 );

		assertNotOmer( 2011, Calendar.APRIL, 18 );
		assertNotOmer( 2011, Calendar.APRIL, 19 );
		assertNotOmer( 2011, Calendar.JUNE, 8 );
		assertNotOmer( 2011, Calendar.JULY, 4 );

		assertLagBOmer( 2011, Calendar.MAY, 22,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/21/2011 B | Erev Lag B'Omer                               | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/22/2011 S | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2012() {
		assertPesach( 2012, Calendar.APRIL, 7,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/ 6/2012 F | Erev Pesach                                   | true  | true  | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 7/2012 B | Pesach                                        | true  | false | false        | false | true    | true    | false    | true   | false | false  |\n" +
			" 4/ 8/2012 S | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/ 9/2012 M | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/10/2012 T | Chol HaMoed Pesach 4                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/11/2012 W | Chol HaMoed Pesach 5                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/12/2012 R | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/13/2012 F | Pesach                                        | true  | false | true         | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/14/2012 B | Pesach                                        | true  | false | false        | false | true    | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2012, Calendar.MAY, 6,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 5/2012 B | Erev Pesach Sheni                             | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/ 6/2012 S | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertOmer( 2012, Calendar.APRIL, 8, 1 );
		assertOmer( 2012, Calendar.APRIL, 25, 18 );
		assertOmer( 2012, Calendar.MAY, 2, 25 );
		assertOmer( 2012, Calendar.MAY, 7, 30 );
		assertOmer( 2012, Calendar.MAY, 19, 42 );
		assertOmer( 2012, Calendar.MAY, 25, 48 );

		assertNotOmer( 2012, Calendar.APRIL, 7 );
		assertNotOmer( 2012, Calendar.MAY, 27 );

		assertLagBOmer( 2012, Calendar.MAY, 10,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 9/2012 W | Erev Lag B'Omer                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/10/2012 R | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2013() {
		assertPesach( 2013, Calendar.MARCH, 26,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 3/25/2013 M | Erev Pesach                                   | true  | true  | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 3/26/2013 T | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 3/27/2013 W | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 3/28/2013 R | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 3/29/2013 F | Chol HaMoed Pesach 4                          | true  | false | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 3/30/2013 B | Chol HaMoed Pesach 5                          | true  | false | false        | false | true    | false   | true     | true   | false | false  |\n" +
			" 3/31/2013 S | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 1/2013 M | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/ 2/2013 T | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2013, Calendar.APRIL, 24,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/23/2013 T | Erev Pesach Sheni                             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 4/24/2013 W | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertOmer( 2013, Calendar.MARCH, 27, 1 );
		assertOmer( 2013, Calendar.APRIL, 1, 6 );
		assertOmer( 2013, Calendar.APRIL, 10, 15 );
		assertOmer( 2013, Calendar.APRIL, 11, 16 );
		assertOmer( 2013, Calendar.MAY, 9, 44 );
		assertOmer( 2013, Calendar.MAY, 10, 45 );

		assertNotOmer( 2013, Calendar.MARCH, 20 );
		assertNotOmer( 2013, Calendar.MARCH, 25 );
		assertNotOmer( 2013, Calendar.MAY, 16 );

		assertLagBOmer( 2013, Calendar.APRIL, 28,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/27/2013 B | Erev Lag B'Omer                               | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 4/28/2013 S | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2014() {
		assertPesach( 2014, Calendar.APRIL, 15,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/14/2014 M | Erev Pesach                                   | true  | true  | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/15/2014 T | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/16/2014 W | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/17/2014 R | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/18/2014 F | Chol HaMoed Pesach 4                          | true  | false | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/19/2014 B | Chol HaMoed Pesach 5                          | true  | false | false        | false | true    | false   | true     | true   | false | false  |\n" +
			" 4/20/2014 S | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/21/2014 M | Pesach                                        | true  | false | false        | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/22/2014 T | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2014, Calendar.MAY, 14,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/13/2014 T | Erev Pesach Sheni                             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/14/2014 W | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertOmer( 2014, Calendar.APRIL, 23, 8 );
		assertOmer( 2014, Calendar.APRIL, 30, 15 );
		assertOmer( 2014, Calendar.MAY, 1, 16 );
		assertOmer( 2014, Calendar.MAY, 30, 45 );
		assertOmer( 2014, Calendar.JUNE, 3, 49 );

		assertNotOmer( 2014, Calendar.APRIL, 14 );
		assertNotOmer( 2014, Calendar.JUNE, 4 );

		assertLagBOmer( 2014, Calendar.MAY, 18,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/17/2014 B | Erev Lag B'Omer                               | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/18/2014 S | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2015() {
		assertPesach( 2015, Calendar.APRIL, 4,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/ 3/2015 F | Erev Pesach                                   | true  | true  | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 4/2015 B | Pesach                                        | true  | false | false        | false | true    | true    | false    | true   | false | false  |\n" +
			" 4/ 5/2015 S | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/ 6/2015 M | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 7/2015 T | Chol HaMoed Pesach 4                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 8/2015 W | Chol HaMoed Pesach 5                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/ 9/2015 R | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/10/2015 F | Pesach                                        | true  | false | true         | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/11/2015 B | Pesach                                        | true  | false | false        | false | true    | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2015, Calendar.MAY, 3,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 2/2015 B | Erev Pesach Sheni                             | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/ 3/2015 S | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertLagBOmer( 2015, Calendar.MAY, 7,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/ 6/2015 W | Erev Lag B'Omer                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/ 7/2015 R | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	@Test
	public void test2016() {
		assertPesach( 2016, Calendar.APRIL, 23,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 4/22/2016 F | Erev Pesach                                   | true  | true  | true         | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/23/2016 B | Pesach                                        | true  | false | false        | false | true    | true    | false    | true   | false | false  |\n" +
			" 4/24/2016 S | Pesach                                        | true  | false | false        | false | false   | true    | true     | true   | false | false  |\n" +
			" 4/25/2016 M | Chol HaMoed Pesach 3                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/26/2016 T | Chol HaMoed Pesach 4                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/27/2016 W | Chol HaMoed Pesach 5                          | true  | false | false        | false | false   | false   | false    | true   | false | false  |\n" +
			" 4/28/2016 R | Chol HaMoed Pesach 6                          | true  | false | false        | true  | false   | false   | false    | true   | false | false  |\n" +
			" 4/29/2016 F | Pesach                                        | true  | false | true         | true  | false   | true    | false    | true   | false | false  |\n" +
			" 4/30/2016 B | Pesach                                        | true  | false | false        | false | true    | true    | true     | true   | false | false  |\n"
		);

		assertPesachSheni( 2016, Calendar.MAY, 22,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/21/2016 B | Erev Pesach Sheni                             | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
			" 5/22/2016 S | Pesach Sheni                                  | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);

		assertLagBOmer( 2016, Calendar.MAY, 26,
			"Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
			"-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
			" 5/25/2016 W | Erev Lag B'Omer                               | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
			" 5/26/2016 R | Lag B'Omer                                    | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n"
		);
	}

	private void assertPesach(
		int                 year,
		@CalendarMonth int  month,
		int                 day,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date;
		Details details;

		date = HebrewCalendar.getForDate( year, month, day );

		// Erev Pesach
		date = date.yesterday();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach I
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach II
		// TODO: Handle Israel...
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach III
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach IV
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach V
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach VI
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach VII
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		// Pesach VIII
		date = date.tomorrow();
		details = PESACH.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}


	private void assertPesachSheni(
		int                 year,
		@CalendarMonth int  month,
		int                 day,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date;
		Details details;

		date = HebrewCalendar.getForDate( year, month, day );
		details = PESACH_SHENI.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		details = PESACH_SHENI.getDetailsFor( date );
		outputDetails( output, details );


		Assert.assertEquals( expected, output.toString() );
	}


	private void assertOmer(
		int                 year,
		@CalendarMonth int  month,
		int                 day,
		int                 expectedDayOfOmer
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = OMER.getDetailsFor( date );
		Assert.assertNotNull( details );
		Assert.assertTrue( details.getDescription().contains( "" + expectedDayOfOmer ) );
	}


	private void assertNotOmer(
		int                 year,
		@CalendarMonth int  month,
		int                 day
	) {

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = OMER.getDetailsFor( date );

		Assert.assertNull( details );
	}


	private void assertLagBOmer(
		int                 year,
		@CalendarMonth int  month,
		int                 day,
		String              expected
	) {

		StringBuilder output = buildDetailsHeader();

		HebrewDate date = HebrewCalendar.getForDate( year, month, day );
		Details details = LAG_B_OMER.getDetailsFor( date.yesterday() );
		outputDetails( output, details );

		details = LAG_B_OMER.getDetailsFor( date );
		outputDetails( output, details );

		Assert.assertEquals( expected, output.toString() );
	}

} // End of UT_Pesach class
