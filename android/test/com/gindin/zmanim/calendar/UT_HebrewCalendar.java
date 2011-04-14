/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.calendar.holiday.Erev;
import com.gindin.zmanim.calendar.holiday.JewishDay;
import com.gindin.zmanim.calendar.holiday.RoshHashana;
import com.gindin.zmanim.calendar.holiday.Taanit;
import org.junit.*;

/**
 *
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_HebrewCalendar {


	private HebrewCalendar                                    calendar;


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
		Assert.assertTrue( today instanceof Erev );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		// 1st of Tishri
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
		Assert.assertTrue( today instanceof Erev );
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
		Assert.assertTrue( today instanceof Erev );
		Assert.assertTrue( today.showCandlelighting() );
		Assert.assertFalse( today.showHavdalah() );

		calendar.add( HebrewCalendar.DAY_OF_MONTH, 1 );
		today = calendar.getToday();
		Assert.assertFalse( today.showCandlelighting() );
		Assert.assertTrue( today.showHavdalah() );

		// Sukkot
		calendar.set( HebrewCalendar.DAY_OF_MONTH, 14 );
		today = calendar.getToday();
		Assert.assertTrue( today instanceof Erev );
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


		// Purim


		// Pesach


		// Omer -- test that candles & havdalah show up


		// Shavuot


		//

	}




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
