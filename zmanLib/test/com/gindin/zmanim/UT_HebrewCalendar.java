/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim;

import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.holiday.*;
import org.junit.*;

/**
 *
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_HebrewCalendar {


	private HebrewCalendar calendar;


	/**
	 * Methods annotated with @Before will be run before each test. There can be as many @Before methods as you want. Note
	 * that it is possible to inherit @Before methods, as JUnit will execute @Before methods in superclass before the
	 * inherited @Before methods.
	 */
	@Before
	public void runBeforeEachTest() {

		// Initialize the calendar to some sane default.
		calendar = new HebrewCalendar();
		calendar.set( HebrewCalendar.HOUR_OF_DAY, 12 );
		calendar.set( HebrewCalendar.MINUTE, 0 );
		calendar.set( HebrewCalendar.SECOND, 0 );
		calendar.set( HebrewCalendar.MILLISECOND, 0 );
	}


	/**
	 * Methods annotated with @After will be run before each test. There can be as many @After methods as you want. Note
	 * that it is possible to inherit @After methods, as JUnit will execute @After methods in subclasses before the
	 * inherited @After methods.
	 * <p/>
	 * NOTE: All @After methods are guaranteed to run even if a @Before or @Test method throws an exception.
	 */
	@After
	public void runAfterEachTest() {

	}


	@Test
	public void holidays2010() {

		// Start on 28th day of Elul
		calendar.set( HebrewCalendar.YEAR, 5770 );
		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.ELUL );
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 28 );
		assertTodayIsNothing();

		// 29th day of Elul...Erev Rosh Hashana
		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		JewishDay today = calendar.getToday();
		Assert.assertNotNull( today );
		Assert.assertNotNull( today.isErev() );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		// 1st of Tishri  -- Now in 5771
		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertNotNull( today );
		Assert.assertTrue( today instanceof RoshHashana );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );


		// 2nd of Tishri
		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertNotNull( today );
		Assert.assertTrue( today instanceof RoshHashana );
		Assert.assertTrue( today.showCandlelighting() );      // Candles because it's Erev Shabbat
		Assert.assertFalse( today.showHavdalah() );           // No havdalah because it's Erev Shabbat

		// 3rd of Tishri -- Shabbat, so it can't be a fast day
		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertNotNull( today.isErev() );
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );

		// Fast of Gedalia
		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertTrue( today instanceof Taanit );
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		// Yom Kippur
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 9 );
		today = calendar.getToday();
		Assert.assertNotNull( today.isErev() );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );

		// Sukkot
		// TODO: Test the getDayOf() and isMultiDay() here too!!!

		calendar.set( HebrewCalendar.DAY_OF_MONTH, 14 );
		today = calendar.getToday();
		Assert.assertNotNull( today.isErev() );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 15th
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 16th
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );    // It's erev Shabbat
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 17th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );          // It's motzae Shabbat

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 18th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 19th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 20th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 21st - Hoshana Rabba
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 22nd - Shemin Atzeret
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 23rd - Simchat Torah
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );    // It's Shabbat
		Assert.assertFalse( today.showHavdalah() );


		// Chanukah
		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.KISLEV );
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 23 );
		assertTodayIsNothing();

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 24th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isErev() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 25th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 1 == today.isMultiDay().getDayOf() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 26th
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );   // Shabbat
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 2 == today.isMultiDay().getDayOf() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 27th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );          // Motzae Shabbat
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 3 == today.isMultiDay().getDayOf() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 28th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 4 == today.isMultiDay().getDayOf() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 29th
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 5 == today.isMultiDay().getDayOf() );
		Assert.assertNotNull( today.isErev() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 30th -- Also Rosh Chodesh Tevet
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 6 == today.isMultiDay().getDayOf() );
		Assert.assertNotNull( today.isRoshChodesh() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 1st of Tevet -- Also Rosh Chodesh Tevet
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 7 == today.isMultiDay().getDayOf() );
		Assert.assertNotNull( today.isRoshChodesh() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 2nd of Tevet
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNotNull( today.isMultiDay() );
		Assert.assertTrue( 8 == today.isMultiDay().getDayOf() );

		// Purim
		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.ADAR_1 );   // This is a leap year...so test out Adar I, then Adar II
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 13 );
		today = calendar.getToday();
		Assert.assertNotNull( today.isErev() );
		Assert.assertEquals( "Erev Purim Katan", today.getName() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 14th...Purim Katan
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertEquals( "Purim Katan", today.getName() );

		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.ADAR ); // Ta'anit Esther is pulled back this year...
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 10 );
		today = calendar.getToday();
		Assert.assertNotNull( today.isErev() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 11th...Ta'anit Esther
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertTrue( today.isTaanit() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 2 );     // 13th...Normally this is where Ta'anit Esther would fall...it's Shabbat...
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertFalse( today.isTaanit() );
		Assert.assertNotNull( today.isErev() );
		Assert.assertTrue( today.getName().contains( "Erev Purim" ) );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 14th...Purim
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertEquals( "Purim", today.getName() );


		// Pesach
		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.NISAN );
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 14 );            // Ta'anit Bechorot
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );            // It's also Erev Pesach!
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertTrue( today.isTaanit() );
		Assert.assertNotNull( today.isErev() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 15th... 1st day of Pesach!
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 16th... 2nd day of Pesach!
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 17th... 3rd day of Pesach!
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 18th... 4th day of Pesach, and Erev Shabbat
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 19th... 5th day of Pesach and Shabbat
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );
		Assert.assertTrue( today.isShabbat() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 20th... 6th day of Pesach!
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 21st... 7th day of Pesach!
		today = calendar.getToday();
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 22nd... 8th day of Pesach!
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );


		// Omer -- test that candles & havdalah show up for Shabbat, but not others


		// Shavuot


		// Tisha B'Av -- saw a bug where we were showing "Fast Ends" on Erev....
		calendar.set( HebrewCalendar.MONTH, HebrewCalendar.AV );
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 7 );
		assertTodayIsNothing();

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 8th of Av
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertNotNull( today.isErev() );
		Assert.assertFalse( today.isTaanit() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );     // 9th of Av
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );
		Assert.assertNull( today.isMultiDay() );
		Assert.assertNull( today.isErev() );
		Assert.assertTrue( today.isTaanit() );

	}


	// TODO: Make sure there's a test that we don't show Havdalah when a Chag falls on Shabbat & Sunday


	private void assertTodayIsNothing() {
		Assert.assertNull( calendar.isTodayAHoliday() );
		Assert.assertNull( calendar.isTodayInTheOmer() );
		Assert.assertNull( calendar.isTodayATaanit() );
		Assert.assertNull( calendar.isTodayRoshChodesh() );
	}


	/** Method annotated with @Test can also declare that an exception of a particular class should be thrown. */
	@Ignore( "Just for demonstration purposes" )
	@Test( expected = Exception.class )
	public void exceptionIsExpected() {

	}


	/** Methods annotated with @Test can also be annoated with @Ignore, and the test won't be executed. */
	@Ignore( "Just for demonstration purposes" )
	@Test
	public void notExecuted() {

	}

} // End of UT_HebrewCalendar class
