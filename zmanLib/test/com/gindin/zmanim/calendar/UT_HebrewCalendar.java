/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.calendar;

import com.gindin.zmanim.calendar.holiday.Holidays;
import com.gindin.zmanim.calendar.holiday.Observance;
import org.junit.*;


/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_HebrewCalendar {

	@Test
	public void testYearType() {

		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5771 ) );
		Assert.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5772 ) );
		Assert.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5773 ) );
		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5774 ) );
		Assert.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5775 ) );
		Assert.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5776 ) );
		Assert.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5777 ) );
	}


	@Test
	public void testInvalidJewishDate() {

		// When asking for a HebrewDate, if we specify a date that doesn't exist in the
		//  desired month, the code is supposed to move it back to a sane date...
		HebrewDate date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.TEVET, 30 );
		Assert.assertEquals( HebrewMonth.TEVET, date.getHebrewMonth() );
		Assert.assertEquals( 29, date.getHebrewDayOfMonth() );

		date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 30 );
		Assert.assertEquals( HebrewMonth.ADAR, date.getHebrewMonth() );
		Assert.assertEquals( 29, date.getHebrewDayOfMonth() );
	}


	@Test
	public void testComparatorAndEquals() {

		HebrewDate date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
		HebrewDate date2 = HebrewCalendar.getForJewishDate( 5773, HebrewMonth.NISSAN, 1 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );
		Assert.assertFalse( date1.equals( date2 ) );


		date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
		date2 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 2 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );

		date2 = date2.yesterday();
		Assert.assertEquals( 0, date1.compareTo( date2 ) );
		Assert.assertEquals( 0, date2.compareTo( date1 ) );
		Assert.assertTrue( date1.equals( date2 ) );
		Assert.assertTrue( date2.equals( date1 ) );

		date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 29 );
		Assert.assertEquals( 1, date2.compareTo( date1 ) );
		Assert.assertEquals( -1, date1.compareTo( date2 ) );

		date2 = date2.yesterday();
		Assert.assertEquals( 0, date1.compareTo( date2 ) );
		Assert.assertEquals( 0, date2.compareTo( date1 ) );
	}


	@Test
	public void test5771() {

		HebrewDate date = HebrewCalendar.getForJewishDate( 5770, HebrewMonth.ELUL, 28 );

		StringBuilder output = new StringBuilder();
		output.append( "Year / Mo / Dt | Erev/Yom | Candles | Havdalah | Description\n" );
		output.append( "-------------------------------------------------------------\n" );

		while ( 5772 != date.getHebrewYear() ) {

			for ( Observance observance : Holidays.ALL ) {
				Observance.Details details = observance.getDetailsFor( date );
				printDateAndDetails( output, date, details );
			}

			date = date.tomorrow();
		}

		assertCalendar( output,
			"Year / Mo / Dt | Erev/Yom | Candles | Havdalah | Description\n" +
				"-------------------------------------------------------------\n" +
				"2010 /  9 /  8 | erev     | yes     | no       | Erev Rosh Hashana\n" +
				"2010 /  9 /  9 | yom      | yes     | no       | Rosh Hashana\n" +
				"2010 /  9 / 10 | yom      | no      | yes      | Rosh Hashana\n" +
				"2010 /  9 / 11 | erev     | no      | no       | Erev Tzom Gedaliah\n" +
				"2010 /  9 / 12 | yom      | no      | no       | Tzom Gedaliah\n" +
				"2010 /  9 / 17 | erev     | yes     | no       | Erev Yom Kippur\n" +
				"2010 /  9 / 18 | yom      | no      | yes      | Yom Kippur\n" +
				"2010 /  9 / 22 | erev     | yes     | no       | Erev Sukkot\n" +
				"2010 /  9 / 23 | yom      | yes     | no       | Sukkot\n" +
				"2010 /  9 / 24 | yom      | no      | yes      | Sukkot\n" +
				"2010 /  9 / 25 | yom      | no      | no       | Chol HaMoed Sukkot 3\n" +
				"2010 /  9 / 26 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
				"2010 /  9 / 27 | yom      | no      | no       | Chol HaMoed Sukkot 5\n" +
				"2010 /  9 / 28 | yom      | no      | no       | Chol HaMoed Sukkot 6\n" +
				"2010 /  9 / 29 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
				"2010 /  9 / 30 | yom      | yes     | no       | Shmini Atzeret\n" +
				"2010 / 10 /  1 | yom      | no      | yes      | Simchat Torah\n" +
				"2010 / 10 /  7 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
				"2010 / 10 /  8 | yom      | no      | no       | Rosh Chodesh Heshvan\n" +
				"2010 / 10 /  9 | yom      | no      | no       | Rosh Chodesh Heshvan\n" +
				"2010 / 11 /  6 | erev     | no      | no       | Erev Rosh Chodesh Kislev\n" +
				"2010 / 11 /  7 | yom      | no      | no       | Rosh Chodesh Kislev\n" +
				"2010 / 11 /  8 | yom      | no      | no       | Rosh Chodesh Kislev\n" +
				"2010 / 12 /  1 | erev     | no      | no       | Erev Chanukah\n" +
				"2010 / 12 /  2 | yom      | no      | no       | Chanukah 1\n" +
				"2010 / 12 /  3 | yom      | no      | no       | Chanukah 2\n" +
				"2010 / 12 /  4 | yom      | no      | no       | Chanukah 3\n" +
				"2010 / 12 /  5 | yom      | no      | no       | Chanukah 4\n" +
				"2010 / 12 /  6 | yom      | no      | no       | Chanukah 5\n" +
				"2010 / 12 /  6 | erev     | no      | no       | Erev Rosh Chodesh Tevet\n" +
				"2010 / 12 /  7 | yom      | no      | no       | Chanukah 6\n" +
				"2010 / 12 /  7 | yom      | no      | no       | Rosh Chodesh Tevet\n" +
				"2010 / 12 /  8 | yom      | no      | no       | Chanukah 7\n" +
				"2010 / 12 /  8 | yom      | no      | no       | Rosh Chodesh Tevet\n" +
				"2010 / 12 /  9 | yom      | no      | no       | Chanukah 8\n" +
				"2010 / 12 / 16 | erev     | no      | no       | Erev Asara B'Tevet\n" +
				"2010 / 12 / 17 | yom      | no      | no       | Asara B'Tevet\n" +
				"2011 /  1 /  5 | erev     | no      | no       | Erev Rosh Chodesh Shevat\n" +
				"2011 /  1 /  6 | yom      | no      | no       | Rosh Chodesh Shevat\n" +
				"2011 /  1 / 19 | erev     | no      | no       | Erev Tu B'Shvat\n" +
				"2011 /  1 / 20 | yom      | no      | no       | Tu B'Shvat\n" +
				"2011 /  2 /  3 | erev     | no      | no       | Erev Rosh Chodesh Adar I\n" +
				"2011 /  2 /  4 | yom      | no      | no       | Rosh Chodesh Adar I\n" +
				"2011 /  2 /  5 | yom      | no      | no       | Rosh Chodesh Adar I\n" +
				"2011 /  3 /  5 | erev     | no      | no       | Erev Rosh Chodesh Adar II\n" +
				"2011 /  3 /  6 | yom      | no      | no       | Rosh Chodesh Adar II\n" +
				"2011 /  3 /  7 | yom      | no      | no       | Rosh Chodesh Adar II\n" +
				"2011 /  3 / 16 | erev     | no      | no       | Erev Ta'anit Esther\n" +
				"2011 /  3 / 17 | yom      | no      | no       | Ta'anit Esther\n" +
				"2011 /  3 / 19 | erev     | no      | no       | Erev Purim\n" +
				"2011 /  3 / 20 | yom      | no      | no       | Purim\n" +
				"2011 /  3 / 21 | yom      | no      | no       | Shushan Purim\n" +
				"2011 /  4 /  4 | erev     | no      | no       | Erev Rosh Chodesh Nissan\n" +
				"2011 /  4 /  5 | yom      | no      | no       | Rosh Chodesh Nissan\n" +
				"2011 /  4 / 17 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
				"2011 /  4 / 18 | erev     | yes     | no       | Erev Pesach\n" +
				"2011 /  4 / 18 | yom      | no      | no       | Ta'anit Bechorot\n" +
				"2011 /  4 / 19 | yom      | yes     | no       | Pesach\n" +
				"2011 /  4 / 20 | yom      | no      | yes      | Pesach\n" +
				"2011 /  4 / 20 | yom      | no      | no       | Omer 1\n" +
				"2011 /  4 / 21 | yom      | no      | no       | Chol HaMoed Pesach 3\n" +
				"2011 /  4 / 21 | yom      | no      | no       | Omer 2\n" +
				"2011 /  4 / 22 | yom      | no      | no       | Chol HaMoed Pesach 4\n" +
				"2011 /  4 / 22 | yom      | no      | no       | Omer 3\n" +
				"2011 /  4 / 23 | yom      | no      | no       | Chol HaMoed Pesach 5\n" +
				"2011 /  4 / 23 | yom      | no      | no       | Omer 4\n" +
				"2011 /  4 / 24 | yom      | yes     | no       | Chol HaMoed Pesach 6\n" +
				"2011 /  4 / 24 | yom      | no      | no       | Omer 5\n" +
				"2011 /  4 / 25 | yom      | yes     | no       | Pesach\n" +
				"2011 /  4 / 25 | yom      | no      | no       | Omer 6\n" +
				"2011 /  4 / 26 | yom      | no      | yes      | Pesach\n" +
				"2011 /  4 / 26 | yom      | no      | no       | Omer 7\n" +
				"2011 /  4 / 27 | yom      | no      | no       | Omer 8\n" +
				"2011 /  4 / 28 | yom      | no      | no       | Omer 9\n" +
				"2011 /  4 / 29 | yom      | no      | no       | Omer 10\n" +
				"2011 /  4 / 30 | yom      | no      | no       | Omer 11\n" +
				"2011 /  4 / 30 | erev     | no      | no       | Erev Yom HaShoah\n" +
				"2011 /  5 /  1 | yom      | no      | no       | Omer 12\n" +
				"2011 /  5 /  1 | yom      | no      | no       | Yom HaShoah\n" +
				"2011 /  5 /  2 | yom      | no      | no       | Omer 13\n" +
				"2011 /  5 /  3 | yom      | no      | no       | Omer 14\n" +
				"2011 /  5 /  3 | erev     | no      | no       | Erev Rosh Chodesh Iyar\n" +
				"2011 /  5 /  4 | yom      | no      | no       | Omer 15\n" +
				"2011 /  5 /  4 | yom      | no      | no       | Rosh Chodesh Iyar\n" +
				"2011 /  5 /  5 | yom      | no      | no       | Omer 16\n" +
				"2011 /  5 /  5 | yom      | no      | no       | Rosh Chodesh Iyar\n" +
				"2011 /  5 /  6 | yom      | no      | no       | Omer 17\n" +
				"2011 /  5 /  7 | yom      | no      | no       | Omer 18\n" +
				"2011 /  5 /  8 | yom      | no      | no       | Omer 19\n" +
				"2011 /  5 /  8 | erev     | no      | no       | Erev Yom HaZikaron\n" +
				"2011 /  5 /  9 | yom      | no      | no       | Omer 20\n" +
				"2011 /  5 /  9 | erev     | no      | no       | Erev Yom HaAtzmaut\n" +
				"2011 /  5 /  9 | yom      | no      | no       | Yom HaZikaron\n" +
				"2011 /  5 / 10 | yom      | no      | no       | Omer 21\n" +
				"2011 /  5 / 10 | yom      | no      | no       | Yom HaAtzmaut\n" +
				"2011 /  5 / 11 | yom      | no      | no       | Omer 22\n" +
				"2011 /  5 / 12 | yom      | no      | no       | Omer 23\n" +
				"2011 /  5 / 13 | yom      | no      | no       | Omer 24\n" +
				"2011 /  5 / 14 | yom      | no      | no       | Omer 25\n" +
				"2011 /  5 / 15 | yom      | no      | no       | Omer 26\n" +
				"2011 /  5 / 16 | yom      | no      | no       | Omer 27\n" +
				"2011 /  5 / 17 | yom      | no      | no       | Omer 28\n" +
				"2011 /  5 / 17 | erev     | no      | no       | Erev Pesach Sheni\n" +
				"2011 /  5 / 18 | yom      | no      | no       | Omer 29\n" +
				"2011 /  5 / 18 | yom      | no      | no       | Pesach Sheni\n" +
				"2011 /  5 / 19 | yom      | no      | no       | Omer 30\n" +
				"2011 /  5 / 20 | yom      | no      | no       | Omer 31\n" +
				"2011 /  5 / 21 | erev     | no      | no       | Erev Lag B'Omer\n" +
				"2011 /  5 / 21 | yom      | no      | no       | Omer 32\n" +
				"2011 /  5 / 22 | yom      | no      | no       | Lag B'Omer\n" +
				"2011 /  5 / 22 | yom      | no      | no       | Omer 33\n" +
				"2011 /  5 / 23 | yom      | no      | no       | Omer 34\n" +
				"2011 /  5 / 24 | yom      | no      | no       | Omer 35\n" +
				"2011 /  5 / 25 | yom      | no      | no       | Omer 36\n" +
				"2011 /  5 / 26 | yom      | no      | no       | Omer 37\n" +
				"2011 /  5 / 27 | yom      | no      | no       | Omer 38\n" +
				"2011 /  5 / 28 | yom      | no      | no       | Omer 39\n" +
				"2011 /  5 / 29 | yom      | no      | no       | Omer 40\n" +
				"2011 /  5 / 30 | yom      | no      | no       | Omer 41\n" +
				"2011 /  5 / 31 | yom      | no      | no       | Omer 42\n" +
				"2011 /  5 / 31 | erev     | no      | no       | Erev Yom Yerushalyim\n" +
				"2011 /  6 /  1 | yom      | no      | no       | Omer 43\n" +
				"2011 /  6 /  1 | yom      | no      | no       | Yom Yerushalyim\n" +
				"2011 /  6 /  2 | yom      | no      | no       | Omer 44\n" +
				"2011 /  6 /  2 | erev     | no      | no       | Erev Rosh Chodesh Sivan\n" +
				"2011 /  6 /  3 | yom      | no      | no       | Omer 45\n" +
				"2011 /  6 /  3 | yom      | no      | no       | Rosh Chodesh Sivan\n" +
				"2011 /  6 /  4 | yom      | no      | no       | Omer 46\n" +
				"2011 /  6 /  5 | yom      | no      | no       | Omer 47\n" +
				"2011 /  6 /  6 | yom      | no      | no       | Omer 48\n" +
				"2011 /  6 /  7 | erev     | yes     | no       | Erev Shavuot\n" +
				"2011 /  6 /  7 | yom      | no      | no       | Omer 49\n" +
				"2011 /  6 /  8 | yom      | yes     | no       | Shavuot\n" +
				"2011 /  6 /  9 | yom      | no      | yes      | Shavuot\n" +
				"2011 /  7 /  1 | erev     | no      | no       | Erev Rosh Chodesh Tammuz\n" +
				"2011 /  7 /  2 | yom      | no      | no       | Rosh Chodesh Tammuz\n" +
				"2011 /  7 /  3 | yom      | no      | no       | Rosh Chodesh Tammuz\n" +
				"2011 /  7 / 18 | erev     | no      | no       | Erev Ta'anit 17 Tamuz\n" +
				"2011 /  7 / 19 | yom      | yes     | no       | Ta'anit 17 Tamuz\n" +
				"2011 /  7 / 31 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
				"2011 /  8 /  1 | yom      | no      | no       | Rosh Chodesh Av\n" +
				"2011 /  8 /  8 | erev     | no      | no       | Erev Tisha B'Av\n" +
				"2011 /  8 /  9 | yom      | no      | no       | Tisha B'Av\n" +
				"2011 /  8 / 14 | erev     | no      | no       | Erev Tu B'Av\n" +
				"2011 /  8 / 15 | yom      | no      | no       | Tu B'Av\n" +
				"2011 /  8 / 29 | erev     | no      | no       | Erev Rosh Chodesh Elul\n" +
				"2011 /  8 / 30 | yom      | no      | no       | Rosh Chodesh Elul\n" +
				"2011 /  8 / 31 | yom      | no      | no       | Rosh Chodesh Elul\n" +
				"2011 /  9 / 28 | erev     | yes     | no       | Erev Rosh Hashana\n"
		);
	}


	private static void printDateAndDetails(
		StringBuilder       output,
		HebrewDate          date,
		Observance.Details  details
	) {

		if ( null == details ) {
			return;
		}

		//noinspection AutoBoxing
		output.append( String.format( "%d / %2d / %2d | ", date.getGregorianYear(), date.getGregorianMonth() + 1, date.getGregorianDayOfMonth() ) );

		output.append( details.isErev() ? "erev" : "yom " );
		output.append( "     | " );
		output.append( details.lightCandles() ? "yes" : "no " );
		output.append( "     | " );
		output.append( details.sayHavdalah() ? "yes" : "no " );
		output.append( "      | " );
		output.append( details.getDescription() );
		output.append( "\n" );
	}


	private void assertCalendar(
		StringBuilder output,
		String        expected
	) {

		Assert.assertEquals( expected, output.toString());
	}

} // End of UT_HebrewCalendar class
