/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar;

import com.gindin.zmanlib.calendar.holiday.Details;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_HebrewCalendar {

  @Test
  public void testYearType() {

    Assertions.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5771 ) );
    Assertions.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5772 ) );
    Assertions.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5773 ) );
    Assertions.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5774 ) );
    Assertions.assertEquals( HebrewYearType.QESIDRAH, HebrewCalendar.getYearType( 5775 ) );
    Assertions.assertEquals( HebrewYearType.SHALEM, HebrewCalendar.getYearType( 5776 ) );
    Assertions.assertEquals( HebrewYearType.HASER, HebrewCalendar.getYearType( 5777 ) );
  }


  @Test
  public void testInvalidJewishDate() {

    // When asking for a HebrewDate, if we specify a date that doesn't exist in the
    //  desired month, the code is supposed to move it back to a sane date...
    HebrewDate date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.TEVET, 30 );
    Assertions.assertEquals( HebrewMonth.TEVET, date.getHebrewMonth() );
    Assertions.assertEquals( 29, date.getHebrewDayOfMonth() );

    date = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 30 );
    Assertions.assertEquals( HebrewMonth.ADAR, date.getHebrewMonth() );
    Assertions.assertEquals( 29, date.getHebrewDayOfMonth() );
  }


  @Test
  public void testComparatorAndEquals() {

    HebrewDate date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
    HebrewDate date2 = HebrewCalendar.getForJewishDate( 5773, HebrewMonth.NISSAN, 1 );
    Assertions.assertEquals( 1, date2.compareTo( date1 ) );
    Assertions.assertEquals( -1, date1.compareTo( date2 ) );
    Assertions.assertFalse( date1.equals( date2 ) );


    date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 1 );
    date2 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.NISSAN, 2 );
    Assertions.assertEquals( 1, date2.compareTo( date1 ) );
    Assertions.assertEquals( -1, date1.compareTo( date2 ) );

    date2 = date2.yesterday();
    Assertions.assertEquals( 0, date1.compareTo( date2 ) );
    Assertions.assertEquals( 0, date2.compareTo( date1 ) );
    Assertions.assertTrue( date1.equals( date2 ) );
    Assertions.assertTrue( date2.equals( date1 ) );

    date1 = HebrewCalendar.getForJewishDate( 5772, HebrewMonth.ADAR, 29 );
    Assertions.assertEquals( 1, date2.compareTo( date1 ) );
    Assertions.assertEquals( -1, date1.compareTo( date2 ) );

    date2 = date2.yesterday();
    Assertions.assertEquals( 0, date1.compareTo( date2 ) );
    Assertions.assertEquals( 0, date2.compareTo( date1 ) );
  }


  @Test
  public void test5771() {

    runTest( 5771,
      "2010 /  9 /  8 | erev     | yes     | no       | Erev Rosh Hashana\n" +
      "2010 /  9 /  9 | yom      | yes     | no       | Rosh Hashana\n" +
      "2010 /  9 / 10 | erev     | yes     | no       | Rosh Hashana\n" +
      "2010 /  9 / 11 | erev     | no      | yes      | Shabbat / Erev Tzom Gedaliah\n" +
      "2010 /  9 / 12 | yom      | no      | no       | Tzom Gedaliah\n" +
      "2010 /  9 / 17 | erev     | yes     | no       | Erev Yom Kippur\n" +
      "2010 /  9 / 18 | yom      | no      | yes      | Shabbat / Yom Kippur\n" +
      "2010 /  9 / 19 | yom      | no      | no       | \n" +
      "2010 /  9 / 20 | yom      | no      | no       | \n" +
      "2010 /  9 / 21 | yom      | no      | no       | \n" +
      "2010 /  9 / 22 | erev     | yes     | no       | Erev Sukkot\n" +
      "2010 /  9 / 23 | yom      | yes     | no       | Sukkot\n" +
      "2010 /  9 / 24 | erev     | yes     | no       | Sukkot\n" +
      "2010 /  9 / 25 | yom      | no      | yes      | Shabbat / Chol HaMoed Sukkot 3\n" +
      "2010 /  9 / 26 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
      "2010 /  9 / 27 | yom      | no      | no       | Chol HaMoed Sukkot 5\n" +
      "2010 /  9 / 28 | yom      | no      | no       | Chol HaMoed Sukkot 6\n" +
      "2010 /  9 / 29 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
      "2010 /  9 / 30 | yom      | yes     | no       | Shmini Atzeret\n" +
      "2010 / 10 /  1 | erev     | yes     | no       | Simchat Torah\n" +
      "2010 / 10 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 10 /  3 | yom      | no      | no       | \n" +
      "2010 / 10 /  4 | yom      | no      | no       | \n" +
      "2010 / 10 /  5 | yom      | no      | no       | \n" +
      "2010 / 10 /  6 | yom      | no      | no       | \n" +
      "2010 / 10 /  7 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
      "2010 / 10 /  8 | erev     | yes     | no       | Rosh Chodesh Heshvan (1)\n" +
      "2010 / 10 /  9 | yom      | no      | yes      | Shabbat / Rosh Chodesh Heshvan (2)\n" +
      "2010 / 10 / 15 | erev     | yes     | no       | \n" +
      "2010 / 10 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 10 / 22 | erev     | yes     | no       | \n" +
      "2010 / 10 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 10 / 29 | erev     | yes     | no       | \n" +
      "2010 / 10 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 11 /  5 | erev     | yes     | no       | \n" +
      "2010 / 11 /  6 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Kislev\n" +
      "2010 / 11 /  7 | yom      | no      | no       | Rosh Chodesh Kislev (1)\n" +
      "2010 / 11 /  8 | yom      | no      | no       | Rosh Chodesh Kislev (2)\n" +
      "2010 / 11 / 12 | erev     | yes     | no       | \n" +
      "2010 / 11 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 11 / 19 | erev     | yes     | no       | \n" +
      "2010 / 11 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 11 / 26 | erev     | yes     | no       | \n" +
      "2010 / 11 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 12 /  1 | erev     | no      | no       | Erev Chanukah\n" +
      "2010 / 12 /  2 | yom      | no      | no       | Chanukah 1\n" +
      "2010 / 12 /  3 | erev     | yes     | no       | Chanukah 2\n" +
      "2010 / 12 /  4 | yom      | no      | yes      | Shabbat / Chanukah 3\n" +
      "2010 / 12 /  5 | yom      | no      | no       | Chanukah 4\n" +
      "2010 / 12 /  6 | erev     | no      | no       | Chanukah 5 / Erev Rosh Chodesh Tevet\n" +
      "2010 / 12 /  7 | yom      | no      | no       | Rosh Chodesh Tevet (1) / Chanukah 6\n" +
      "2010 / 12 /  8 | yom      | no      | no       | Rosh Chodesh Tevet (2) / Chanukah 7\n" +
      "2010 / 12 /  9 | yom      | no      | no       | Chanukah 8\n" +
      "2010 / 12 / 10 | erev     | yes     | no       | \n" +
      "2010 / 12 / 11 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 12 / 16 | erev     | no      | no       | Erev Asara B'Tevet\n" +
      "2010 / 12 / 17 | erev     | yes     | no       | Asara B'Tevet\n" +
      "2010 / 12 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 12 / 24 | erev     | yes     | no       | \n" +
      "2010 / 12 / 25 | yom      | no      | yes      | Shabbat\n" +
      "2010 / 12 / 31 | erev     | yes     | no       | \n" +
      "2011 /  1 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  1 /  5 | erev     | no      | no       | Erev Rosh Chodesh Shevat\n" +
      "2011 /  1 /  6 | yom      | no      | no       | Rosh Chodesh Shevat\n" +
      "2011 /  1 /  7 | erev     | yes     | no       | \n" +
      "2011 /  1 /  8 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  1 / 14 | erev     | yes     | no       | \n" +
      "2011 /  1 / 15 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  1 / 19 | erev     | no      | no       | Erev Tu B'Shvat\n" +
      "2011 /  1 / 20 | yom      | no      | no       | Tu B'Shvat\n" +
      "2011 /  1 / 21 | erev     | yes     | no       | \n" +
      "2011 /  1 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  1 / 28 | erev     | yes     | no       | \n" +
      "2011 /  1 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  2 /  3 | erev     | no      | no       | Erev Rosh Chodesh Adar I\n" +
      "2011 /  2 /  4 | erev     | yes     | no       | Rosh Chodesh Adar I (1)\n" +
      "2011 /  2 /  5 | yom      | no      | yes      | Shabbat / Rosh Chodesh Adar I (2)\n" +
      "2011 /  2 / 11 | erev     | yes     | no       | \n" +
      "2011 /  2 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  2 / 17 | erev     | no      | no       | Erev Purim Katan\n" +
      "2011 /  2 / 18 | erev     | yes     | no       | Purim Katan\n" +
      "2011 /  2 / 19 | yom      | no      | yes      | Shabbat / Purim Katan\n" +
      "2011 /  2 / 25 | erev     | yes     | no       | \n" +
      "2011 /  2 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  3 /  4 | erev     | yes     | no       | \n" +
      "2011 /  3 /  5 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Adar II\n" +
      "2011 /  3 /  6 | yom      | no      | no       | Rosh Chodesh Adar II (1)\n" +
      "2011 /  3 /  7 | yom      | no      | no       | Rosh Chodesh Adar II (2)\n" +
      "2011 /  3 / 11 | erev     | yes     | no       | \n" +
      "2011 /  3 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  3 / 16 | erev     | no      | no       | Erev Ta'anit Esther\n" +
      "2011 /  3 / 17 | yom      | no      | no       | Ta'anit Esther\n" +
      "2011 /  3 / 18 | erev     | yes     | no       | \n" +
      "2011 /  3 / 19 | erev     | no      | yes      | Shabbat / Erev Purim\n" +
      "2011 /  3 / 20 | yom      | no      | no       | Purim\n" +
      "2011 /  3 / 21 | yom      | no      | no       | Shushan Purim\n" +
      "2011 /  3 / 25 | erev     | yes     | no       | \n" +
      "2011 /  3 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  4 /  1 | erev     | yes     | no       | \n" +
      "2011 /  4 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  4 /  4 | erev     | no      | no       | Erev Rosh Chodesh Nissan\n" +
      "2011 /  4 /  5 | yom      | no      | no       | Rosh Chodesh Nissan\n" +
      "2011 /  4 /  6 | yom      | no      | no       | \n" +
      "2011 /  4 /  7 | yom      | no      | no       | \n" +
      "2011 /  4 /  8 | erev     | yes     | no       | \n" +
      "2011 /  4 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  4 / 10 | yom      | no      | no       | \n" +
      "2011 /  4 / 11 | yom      | no      | no       | \n" +
      "2011 /  4 / 12 | yom      | no      | no       | \n" +
      "2011 /  4 / 13 | yom      | no      | no       | \n" +
      "2011 /  4 / 14 | yom      | no      | no       | \n" +
      "2011 /  4 / 15 | erev     | yes     | no       | \n" +
      "2011 /  4 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  4 / 17 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
      "2011 /  4 / 18 | erev     | yes     | no       | Ta'anit Bechorot / Erev Pesach\n" +
      "2011 /  4 / 19 | yom      | yes     | no       | Pesach\n" +
      "2011 /  4 / 20 | yom      | no      | yes      | Pesach / Omer 1\n" +
      "2011 /  4 / 21 | yom      | no      | no       | Chol HaMoed Pesach 3 / Omer 2\n" +
      "2011 /  4 / 22 | erev     | yes     | no       | Chol HaMoed Pesach 4 / Omer 3\n" +
      "2011 /  4 / 23 | yom      | no      | yes      | Shabbat / Chol HaMoed Pesach 5 / Omer 4\n" +
      "2011 /  4 / 24 | yom      | yes     | no       | Chol HaMoed Pesach 6 / Omer 5\n" +
      "2011 /  4 / 25 | yom      | yes     | no       | Pesach / Omer 6\n" +
      "2011 /  4 / 26 | yom      | no      | yes      | Pesach / Omer 7\n" +
      "2011 /  4 / 27 | yom      | no      | no       | Omer 8\n" +
      "2011 /  4 / 28 | yom      | no      | no       | Omer 9\n" +
      "2011 /  4 / 29 | erev     | yes     | no       | Omer 10\n" +
      "2011 /  4 / 30 | yom      | no      | yes      | Shabbat / Omer 11\n" +
      "2011 /  5 /  1 | erev     | no      | no       | Erev Yom HaShoah / Omer 12\n" +
      "2011 /  5 /  2 | yom      | no      | no       | Yom HaShoah / Omer 13\n" +
      "2011 /  5 /  3 | erev     | no      | no       | Erev Rosh Chodesh Iyar / Omer 14\n" +
      "2011 /  5 /  4 | yom      | no      | no       | Rosh Chodesh Iyar (1) / Omer 15\n" +
      "2011 /  5 /  5 | yom      | no      | no       | Rosh Chodesh Iyar (2) / Omer 16\n" +
      "2011 /  5 /  6 | erev     | yes     | no       | Omer 17\n" +
      "2011 /  5 /  7 | yom      | no      | yes      | Shabbat / Omer 18\n" +
      "2011 /  5 /  8 | erev     | no      | no       | Erev Yom HaZikaron / Omer 19\n" +
      "2011 /  5 /  9 | erev     | no      | no       | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20\n" +
      "2011 /  5 / 10 | yom      | no      | no       | Yom HaAtzmaut / Omer 21\n" +
      "2011 /  5 / 11 | yom      | no      | no       | Omer 22\n" +
      "2011 /  5 / 12 | yom      | no      | no       | Omer 23\n" +
      "2011 /  5 / 13 | erev     | yes     | no       | Omer 24\n" +
      "2011 /  5 / 14 | yom      | no      | yes      | Shabbat / Omer 25\n" +
      "2011 /  5 / 15 | yom      | no      | no       | Omer 26\n" +
      "2011 /  5 / 16 | yom      | no      | no       | Omer 27\n" +
      "2011 /  5 / 17 | erev     | no      | no       | Erev Pesach Sheni / Omer 28\n" +
      "2011 /  5 / 18 | yom      | no      | no       | Pesach Sheni / Omer 29\n" +
      "2011 /  5 / 19 | yom      | no      | no       | Omer 30\n" +
      "2011 /  5 / 20 | erev     | yes     | no       | Omer 31\n" +
      "2011 /  5 / 21 | erev     | no      | yes      | Shabbat / Erev Lag B'Omer / Omer 32\n" +
      "2011 /  5 / 22 | yom      | no      | no       | Lag B'Omer / Omer 33\n" +
      "2011 /  5 / 23 | yom      | no      | no       | Omer 34\n" +
      "2011 /  5 / 24 | yom      | no      | no       | Omer 35\n" +
      "2011 /  5 / 25 | yom      | no      | no       | Omer 36\n" +
      "2011 /  5 / 26 | yom      | no      | no       | Omer 37\n" +
      "2011 /  5 / 27 | erev     | yes     | no       | Omer 38\n" +
      "2011 /  5 / 28 | yom      | no      | yes      | Shabbat / Omer 39\n" +
      "2011 /  5 / 29 | yom      | no      | no       | Omer 40\n" +
      "2011 /  5 / 30 | yom      | no      | no       | Omer 41\n" +
      "2011 /  5 / 31 | erev     | no      | no       | Erev Yom Yerushalyim / Omer 42\n" +
      "2011 /  6 /  1 | yom      | no      | no       | Yom Yerushalyim / Omer 43\n" +
      "2011 /  6 /  2 | erev     | no      | no       | Erev Rosh Chodesh Sivan / Omer 44\n" +
      "2011 /  6 /  3 | erev     | yes     | no       | Rosh Chodesh Sivan / Omer 45\n" +
      "2011 /  6 /  4 | yom      | no      | yes      | Shabbat / Omer 46\n" +
      "2011 /  6 /  5 | yom      | no      | no       | Omer 47\n" +
      "2011 /  6 /  6 | yom      | no      | no       | Omer 48\n" +
      "2011 /  6 /  7 | erev     | yes     | no       | Erev Shavuot / Omer 49\n" +
      "2011 /  6 /  8 | yom      | yes     | no       | Shavuot\n" +
      "2011 /  6 /  9 | yom      | no      | yes      | Shavuot\n" +
      "2011 /  6 / 10 | erev     | yes     | no       | \n" +
      "2011 /  6 / 11 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  6 / 12 | yom      | no      | no       | \n" +
      "2011 /  6 / 13 | yom      | no      | no       | \n" +
      "2011 /  6 / 14 | yom      | no      | no       | \n" +
      "2011 /  6 / 17 | erev     | yes     | no       | \n" +
      "2011 /  6 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  6 / 24 | erev     | yes     | no       | \n" +
      "2011 /  6 / 25 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  7 /  1 | erev     | yes     | no       | Erev Rosh Chodesh Tammuz\n" +
      "2011 /  7 /  2 | yom      | no      | yes      | Shabbat / Rosh Chodesh Tammuz (1)\n" +
      "2011 /  7 /  3 | yom      | no      | no       | Rosh Chodesh Tammuz (2)\n" +
      "2011 /  7 /  8 | erev     | yes     | no       | \n" +
      "2011 /  7 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  7 / 15 | erev     | yes     | no       | \n" +
      "2011 /  7 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  7 / 18 | erev     | no      | no       | Erev Ta'anit 17 Tamuz\n" +
      "2011 /  7 / 19 | yom      | no      | no       | Ta'anit 17 Tamuz\n" +
      "2011 /  7 / 22 | erev     | yes     | no       | \n" +
      "2011 /  7 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  7 / 29 | erev     | yes     | no       | \n" +
      "2011 /  7 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  7 / 31 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
      "2011 /  8 /  1 | yom      | no      | no       | Rosh Chodesh Av\n" +
      "2011 /  8 /  5 | erev     | yes     | no       | \n" +
      "2011 /  8 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  8 /  8 | erev     | no      | no       | Erev Tisha B'Av\n" +
      "2011 /  8 /  9 | yom      | no      | no       | Tisha B'Av\n" +
      "2011 /  8 / 12 | erev     | yes     | no       | \n" +
      "2011 /  8 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  8 / 14 | erev     | no      | no       | Erev Tu B'Av\n" +
      "2011 /  8 / 15 | yom      | no      | no       | Tu B'Av\n" +
      "2011 /  8 / 19 | erev     | yes     | no       | \n" +
      "2011 /  8 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  8 / 26 | erev     | yes     | no       | \n" +
      "2011 /  8 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  8 / 29 | erev     | no      | no       | Erev Rosh Chodesh Elul\n" +
      "2011 /  8 / 30 | yom      | no      | no       | Rosh Chodesh Elul (1)\n" +
      "2011 /  8 / 31 | yom      | no      | no       | Rosh Chodesh Elul (2)\n" +
      "2011 /  9 /  2 | erev     | yes     | no       | \n" +
      "2011 /  9 /  3 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  9 /  9 | erev     | yes     | no       | \n" +
      "2011 /  9 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  9 / 16 | erev     | yes     | no       | \n" +
      "2011 /  9 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  9 / 23 | erev     | yes     | no       | \n" +
      "2011 /  9 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2011 /  9 / 28 | erev     | yes     | no       | Erev Rosh Hashana\n"
    );
  }


  @Test
  public void test5773() {

    runTest( 5773,
      "2012 /  9 / 15 | yom      | no      | yes      | Shabbat\n" +
      "2012 /  9 / 16 | erev     | yes     | no       | Erev Rosh Hashana\n" +
      "2012 /  9 / 17 | yom      | yes     | no       | Rosh Hashana\n" +
      "2012 /  9 / 18 | erev     | no      | yes      | Rosh Hashana / Erev Tzom Gedaliah\n" +
      "2012 /  9 / 19 | yom      | no      | no       | Tzom Gedaliah\n" +
      "2012 /  9 / 21 | erev     | yes     | no       | \n" +
      "2012 /  9 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2012 /  9 / 25 | erev     | yes     | no       | Erev Yom Kippur\n" +
      "2012 /  9 / 26 | yom      | no      | yes      | Yom Kippur\n" +
      "2012 /  9 / 27 | yom      | no      | no       | \n" +
      "2012 /  9 / 28 | erev     | yes     | no       | \n" +
      "2012 /  9 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2012 /  9 / 30 | erev     | yes     | no       | Erev Sukkot\n" +
      "2012 / 10 /  1 | yom      | yes     | no       | Sukkot\n" +
      "2012 / 10 /  2 | yom      | no      | yes      | Sukkot\n" +
      "2012 / 10 /  3 | yom      | no      | no       | Chol HaMoed Sukkot 3\n" +
      "2012 / 10 /  4 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
      "2012 / 10 /  5 | erev     | yes     | no       | Chol HaMoed Sukkot 5\n" +
      "2012 / 10 /  6 | yom      | no      | yes      | Shabbat / Chol HaMoed Sukkot 6\n" +
      "2012 / 10 /  7 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
      "2012 / 10 /  8 | yom      | yes     | no       | Shmini Atzeret\n" +
      "2012 / 10 /  9 | yom      | no      | yes      | Simchat Torah\n" +
      "2012 / 10 / 10 | yom      | no      | no       | \n" +
      "2012 / 10 / 11 | yom      | no      | no       | \n" +
      "2012 / 10 / 12 | erev     | yes     | no       | \n" +
      "2012 / 10 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 10 / 14 | yom      | no      | no       | \n" +
      "2012 / 10 / 15 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
      "2012 / 10 / 16 | yom      | no      | no       | Rosh Chodesh Heshvan (1)\n" +
      "2012 / 10 / 17 | yom      | no      | no       | Rosh Chodesh Heshvan (2)\n" +
      "2012 / 10 / 19 | erev     | yes     | no       | \n" +
      "2012 / 10 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 10 / 26 | erev     | yes     | no       | \n" +
      "2012 / 10 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 11 /  2 | erev     | yes     | no       | \n" +
      "2012 / 11 /  3 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 11 /  9 | erev     | yes     | no       | \n" +
      "2012 / 11 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 11 / 14 | erev     | no      | no       | Erev Rosh Chodesh Kislev\n" +
      "2012 / 11 / 15 | yom      | no      | no       | Rosh Chodesh Kislev\n" +
      "2012 / 11 / 16 | erev     | yes     | no       | \n" +
      "2012 / 11 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 11 / 23 | erev     | yes     | no       | \n" +
      "2012 / 11 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 11 / 30 | erev     | yes     | no       | \n" +
      "2012 / 12 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2012 / 12 /  7 | erev     | yes     | no       | \n" +
      "2012 / 12 /  8 | erev     | no      | yes      | Shabbat / Erev Chanukah\n" +
      "2012 / 12 /  9 | yom      | no      | no       | Chanukah 1\n" +
      "2012 / 12 / 10 | yom      | no      | no       | Chanukah 2\n" +
      "2012 / 12 / 11 | yom      | no      | no       | Chanukah 3\n" +
      "2012 / 12 / 12 | yom      | no      | no       | Chanukah 4\n" +
      "2012 / 12 / 13 | erev     | no      | no       | Chanukah 5 / Erev Rosh Chodesh Tevet\n" +
      "2012 / 12 / 14 | erev     | yes     | no       | Rosh Chodesh Tevet / Chanukah 6\n" +
      "2012 / 12 / 15 | yom      | no      | yes      | Shabbat / Chanukah 7\n" +
      "2012 / 12 / 16 | yom      | no      | no       | Chanukah 8\n" +
      "2012 / 12 / 21 | erev     | yes     | no       | \n" +
      "2012 / 12 / 22 | erev     | no      | yes      | Shabbat / Erev Asara B'Tevet\n" +
      "2012 / 12 / 23 | yom      | no      | no       | Asara B'Tevet\n" +
      "2012 / 12 / 28 | erev     | yes     | no       | \n" +
      "2012 / 12 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  1 /  4 | erev     | yes     | no       | \n" +
      "2013 /  1 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  1 / 11 | erev     | yes     | no       | Erev Rosh Chodesh Shevat\n" +
      "2013 /  1 / 12 | yom      | no      | yes      | Shabbat / Rosh Chodesh Shevat\n" +
      "2013 /  1 / 18 | erev     | yes     | no       | \n" +
      "2013 /  1 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  1 / 25 | erev     | yes     | no       | Erev Tu B'Shvat\n" +
      "2013 /  1 / 26 | yom      | no      | yes      | Shabbat / Tu B'Shvat\n" +
      "2013 /  2 /  1 | erev     | yes     | no       | \n" +
      "2013 /  2 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  2 /  8 | erev     | yes     | no       | \n" +
      "2013 /  2 /  9 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Adar\n" +
      "2013 /  2 / 10 | yom      | no      | no       | Rosh Chodesh Adar\n" +
      "2013 /  2 / 11 | yom      | no      | no       | Rosh Chodesh Adar\n" +
      "2013 /  2 / 15 | erev     | yes     | no       | \n" +
      "2013 /  2 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  2 / 20 | erev     | no      | no       | Erev Ta'anit Esther\n" +
      "2013 /  2 / 21 | yom      | no      | no       | Ta'anit Esther\n" +
      "2013 /  2 / 22 | erev     | yes     | no       | \n" +
      "2013 /  2 / 23 | erev     | no      | yes      | Shabbat / Erev Purim\n" +
      "2013 /  2 / 24 | yom      | no      | no       | Purim\n" +
      "2013 /  2 / 25 | yom      | no      | no       | Shushan Purim\n" +
      "2013 /  3 /  1 | erev     | yes     | no       | \n" +
      "2013 /  3 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  3 /  8 | erev     | yes     | no       | \n" +
      "2013 /  3 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  3 / 11 | erev     | no      | no       | Erev Rosh Chodesh Nissan\n" +
      "2013 /  3 / 12 | yom      | no      | no       | Rosh Chodesh Nissan\n" +
      "2013 /  3 / 13 | yom      | no      | no       | \n" +
      "2013 /  3 / 14 | yom      | no      | no       | \n" +
      "2013 /  3 / 15 | erev     | yes     | no       | \n" +
      "2013 /  3 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  3 / 17 | yom      | no      | no       | \n" +
      "2013 /  3 / 18 | yom      | no      | no       | \n" +
      "2013 /  3 / 19 | yom      | no      | no       | \n" +
      "2013 /  3 / 20 | yom      | no      | no       | \n" +
      "2013 /  3 / 21 | yom      | no      | no       | \n" +
      "2013 /  3 / 22 | erev     | yes     | no       | \n" +
      "2013 /  3 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  3 / 24 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
      "2013 /  3 / 25 | erev     | yes     | no       | Ta'anit Bechorot / Erev Pesach\n" +
      "2013 /  3 / 26 | yom      | yes     | no       | Pesach\n" +
      "2013 /  3 / 27 | yom      | no      | yes      | Pesach / Omer 1\n" +
      "2013 /  3 / 28 | yom      | no      | no       | Chol HaMoed Pesach 3 / Omer 2\n" +
      "2013 /  3 / 29 | erev     | yes     | no       | Chol HaMoed Pesach 4 / Omer 3\n" +
      "2013 /  3 / 30 | yom      | no      | yes      | Shabbat / Chol HaMoed Pesach 5 / Omer 4\n" +
      "2013 /  3 / 31 | yom      | yes     | no       | Chol HaMoed Pesach 6 / Omer 5\n" +
      "2013 /  4 /  1 | yom      | yes     | no       | Pesach / Omer 6\n" +
      "2013 /  4 /  2 | yom      | no      | yes      | Pesach / Omer 7\n" +
      "2013 /  4 /  3 | yom      | no      | no       | Omer 8\n" +
      "2013 /  4 /  4 | yom      | no      | no       | Omer 9\n" +
      "2013 /  4 /  5 | erev     | yes     | no       | Omer 10\n" +
      "2013 /  4 /  6 | yom      | no      | yes      | Shabbat / Omer 11\n" +
      "2013 /  4 /  7 | erev     | no      | no       | Erev Yom HaShoah / Omer 12\n" +
      "2013 /  4 /  8 | yom      | no      | no       | Yom HaShoah / Omer 13\n" +
      "2013 /  4 /  9 | erev     | no      | no       | Erev Rosh Chodesh Iyar / Omer 14\n" +
      "2013 /  4 / 10 | yom      | no      | no       | Rosh Chodesh Iyar (1) / Omer 15\n" +
      "2013 /  4 / 11 | yom      | no      | no       | Rosh Chodesh Iyar (2) / Omer 16\n" +
      "2013 /  4 / 12 | erev     | yes     | no       | Omer 17\n" +
      "2013 /  4 / 13 | yom      | no      | yes      | Shabbat / Omer 18\n" +
      "2013 /  4 / 14 | erev     | no      | no       | Erev Yom HaZikaron / Omer 19\n" +
      "2013 /  4 / 15 | erev     | no      | no       | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20\n" +
      "2013 /  4 / 16 | yom      | no      | no       | Yom HaAtzmaut / Omer 21\n" +
      "2013 /  4 / 17 | yom      | no      | no       | Omer 22\n" +
      "2013 /  4 / 18 | yom      | no      | no       | Omer 23\n" +
      "2013 /  4 / 19 | erev     | yes     | no       | Omer 24\n" +
      "2013 /  4 / 20 | yom      | no      | yes      | Shabbat / Omer 25\n" +
      "2013 /  4 / 21 | yom      | no      | no       | Omer 26\n" +
      "2013 /  4 / 22 | yom      | no      | no       | Omer 27\n" +
      "2013 /  4 / 23 | erev     | no      | no       | Erev Pesach Sheni / Omer 28\n" +
      "2013 /  4 / 24 | yom      | no      | no       | Pesach Sheni / Omer 29\n" +
      "2013 /  4 / 25 | yom      | no      | no       | Omer 30\n" +
      "2013 /  4 / 26 | erev     | yes     | no       | Omer 31\n" +
      "2013 /  4 / 27 | erev     | no      | yes      | Shabbat / Erev Lag B'Omer / Omer 32\n" +
      "2013 /  4 / 28 | yom      | no      | no       | Lag B'Omer / Omer 33\n" +
      "2013 /  4 / 29 | yom      | no      | no       | Omer 34\n" +
      "2013 /  4 / 30 | yom      | no      | no       | Omer 35\n" +
      "2013 /  5 /  1 | yom      | no      | no       | Omer 36\n" +
      "2013 /  5 /  2 | yom      | no      | no       | Omer 37\n" +
      "2013 /  5 /  3 | erev     | yes     | no       | Omer 38\n" +
      "2013 /  5 /  4 | yom      | no      | yes      | Shabbat / Omer 39\n" +
      "2013 /  5 /  5 | yom      | no      | no       | Omer 40\n" +
      "2013 /  5 /  6 | yom      | no      | no       | Omer 41\n" +
      "2013 /  5 /  7 | erev     | no      | no       | Erev Yom Yerushalyim / Omer 42\n" +
      "2013 /  5 /  8 | yom      | no      | no       | Yom Yerushalyim / Omer 43\n" +
      "2013 /  5 /  9 | erev     | no      | no       | Erev Rosh Chodesh Sivan / Omer 44\n" +
      "2013 /  5 / 10 | erev     | yes     | no       | Rosh Chodesh Sivan / Omer 45\n" +
      "2013 /  5 / 11 | yom      | no      | yes      | Shabbat / Omer 46\n" +
      "2013 /  5 / 12 | yom      | no      | no       | Omer 47\n" +
      "2013 /  5 / 13 | yom      | no      | no       | Omer 48\n" +
      "2013 /  5 / 14 | erev     | yes     | no       | Erev Shavuot / Omer 49\n" +
      "2013 /  5 / 15 | yom      | yes     | no       | Shavuot\n" +
      "2013 /  5 / 16 | yom      | no      | yes      | Shavuot\n" +
      "2013 /  5 / 17 | erev     | yes     | no       | \n" +
      "2013 /  5 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  5 / 19 | yom      | no      | no       | \n" +
      "2013 /  5 / 20 | yom      | no      | no       | \n" +
      "2013 /  5 / 21 | yom      | no      | no       | \n" +
      "2013 /  5 / 24 | erev     | yes     | no       | \n" +
      "2013 /  5 / 25 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  5 / 31 | erev     | yes     | no       | \n" +
      "2013 /  6 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  6 /  7 | erev     | yes     | no       | Erev Rosh Chodesh Tammuz\n" +
      "2013 /  6 /  8 | yom      | no      | yes      | Shabbat / Rosh Chodesh Tammuz (1)\n" +
      "2013 /  6 /  9 | yom      | no      | no       | Rosh Chodesh Tammuz (2)\n" +
      "2013 /  6 / 14 | erev     | yes     | no       | \n" +
      "2013 /  6 / 15 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  6 / 21 | erev     | yes     | no       | \n" +
      "2013 /  6 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  6 / 24 | erev     | no      | no       | Erev Ta'anit 17 Tamuz\n" +
      "2013 /  6 / 25 | yom      | no      | no       | Ta'anit 17 Tamuz\n" +
      "2013 /  6 / 28 | erev     | yes     | no       | \n" +
      "2013 /  6 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  7 /  5 | erev     | yes     | no       | \n" +
      "2013 /  7 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  7 /  7 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
      "2013 /  7 /  8 | yom      | no      | no       | Rosh Chodesh Av\n" +
      "2013 /  7 / 12 | erev     | yes     | no       | \n" +
      "2013 /  7 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  7 / 15 | erev     | no      | no       | Erev Tisha B'Av\n" +
      "2013 /  7 / 16 | yom      | no      | no       | Tisha B'Av\n" +
      "2013 /  7 / 19 | erev     | yes     | no       | \n" +
      "2013 /  7 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  7 / 21 | erev     | no      | no       | Erev Tu B'Av\n" +
      "2013 /  7 / 22 | yom      | no      | no       | Tu B'Av\n" +
      "2013 /  7 / 26 | erev     | yes     | no       | \n" +
      "2013 /  7 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  8 /  2 | erev     | yes     | no       | \n" +
      "2013 /  8 /  3 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  8 /  5 | erev     | no      | no       | Erev Rosh Chodesh Elul\n" +
      "2013 /  8 /  6 | yom      | no      | no       | Rosh Chodesh Elul (1)\n" +
      "2013 /  8 /  7 | yom      | no      | no       | Rosh Chodesh Elul (2)\n" +
      "2013 /  8 /  9 | erev     | yes     | no       | \n" +
      "2013 /  8 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  8 / 16 | erev     | yes     | no       | \n" +
      "2013 /  8 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  8 / 23 | erev     | yes     | no       | \n" +
      "2013 /  8 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  8 / 30 | erev     | yes     | no       | \n" +
      "2013 /  8 / 31 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  9 /  4 | erev     | yes     | no       | Erev Rosh Hashana\n"
    );
  }


  @Test
  public void test5774() {

    runTest( 5774,
      "2013 /  9 /  4 | erev     | yes     | no       | Erev Rosh Hashana\n" +
      "2013 /  9 /  5 | yom      | yes     | no       | Rosh Hashana\n" +
      "2013 /  9 /  6 | erev     | yes     | no       | Rosh Hashana\n" +
      "2013 /  9 /  7 | erev     | no      | yes      | Shabbat / Erev Tzom Gedaliah\n" +
      "2013 /  9 /  8 | yom      | no      | no       | Tzom Gedaliah\n" +
      "2013 /  9 / 13 | erev     | yes     | no       | Erev Yom Kippur\n" +
      "2013 /  9 / 14 | yom      | no      | yes      | Shabbat / Yom Kippur\n" +
      "2013 /  9 / 15 | yom      | no      | no       | \n" +
      "2013 /  9 / 16 | yom      | no      | no       | \n" +
      "2013 /  9 / 17 | yom      | no      | no       | \n" +
      "2013 /  9 / 18 | erev     | yes     | no       | Erev Sukkot\n" +
      "2013 /  9 / 19 | yom      | yes     | no       | Sukkot\n" +
      "2013 /  9 / 20 | erev     | yes     | no       | Sukkot\n" +
      "2013 /  9 / 21 | yom      | no      | yes      | Shabbat / Chol HaMoed Sukkot 3\n" +
      "2013 /  9 / 22 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
      "2013 /  9 / 23 | yom      | no      | no       | Chol HaMoed Sukkot 5\n" +
      "2013 /  9 / 24 | yom      | no      | no       | Chol HaMoed Sukkot 6\n" +
      "2013 /  9 / 25 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
      "2013 /  9 / 26 | yom      | yes     | no       | Shmini Atzeret\n" +
      "2013 /  9 / 27 | erev     | yes     | no       | Simchat Torah\n" +
      "2013 /  9 / 28 | yom      | no      | yes      | Shabbat\n" +
      "2013 /  9 / 29 | yom      | no      | no       | \n" +
      "2013 /  9 / 30 | yom      | no      | no       | \n" +
      "2013 / 10 /  1 | yom      | no      | no       | \n" +
      "2013 / 10 /  2 | yom      | no      | no       | \n" +
      "2013 / 10 /  3 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
      "2013 / 10 /  4 | erev     | yes     | no       | Rosh Chodesh Heshvan (1)\n" +
      "2013 / 10 /  5 | yom      | no      | yes      | Shabbat / Rosh Chodesh Heshvan (2)\n" +
      "2013 / 10 / 11 | erev     | yes     | no       | \n" +
      "2013 / 10 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 10 / 18 | erev     | yes     | no       | \n" +
      "2013 / 10 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 10 / 25 | erev     | yes     | no       | \n" +
      "2013 / 10 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 11 /  1 | erev     | yes     | no       | \n" +
      "2013 / 11 /  2 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Kislev\n" +
      "2013 / 11 /  3 | yom      | no      | no       | Rosh Chodesh Kislev (1)\n" +
      "2013 / 11 /  4 | yom      | no      | no       | Rosh Chodesh Kislev (2)\n" +
      "2013 / 11 /  8 | erev     | yes     | no       | \n" +
      "2013 / 11 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 11 / 15 | erev     | yes     | no       | \n" +
      "2013 / 11 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 11 / 22 | erev     | yes     | no       | \n" +
      "2013 / 11 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 11 / 27 | erev     | no      | no       | Erev Chanukah\n" +
      "2013 / 11 / 28 | yom      | no      | no       | Chanukah 1\n" +
      "2013 / 11 / 29 | erev     | yes     | no       | Chanukah 2\n" +
      "2013 / 11 / 30 | yom      | no      | yes      | Shabbat / Chanukah 3\n" +
      "2013 / 12 /  1 | yom      | no      | no       | Chanukah 4\n" +
      "2013 / 12 /  2 | erev     | no      | no       | Chanukah 5 / Erev Rosh Chodesh Tevet\n" +
      "2013 / 12 /  3 | yom      | no      | no       | Rosh Chodesh Tevet (1) / Chanukah 6\n" +
      "2013 / 12 /  4 | yom      | no      | no       | Rosh Chodesh Tevet (2) / Chanukah 7\n" +
      "2013 / 12 /  5 | yom      | no      | no       | Chanukah 8\n" +
      "2013 / 12 /  6 | erev     | yes     | no       | \n" +
      "2013 / 12 /  7 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 12 / 12 | erev     | no      | no       | Erev Asara B'Tevet\n" +

      // You might think, "Here's a bug! This should be **yom**" But notice it's Erev Shabbat...
      "2013 / 12 / 13 | erev     | yes     | no       | Asara B'Tevet\n" +
      "2013 / 12 / 14 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 12 / 20 | erev     | yes     | no       | \n" +
      "2013 / 12 / 21 | yom      | no      | yes      | Shabbat\n" +
      "2013 / 12 / 27 | erev     | yes     | no       | \n" +
      "2013 / 12 / 28 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  1 /  1 | erev     | no      | no       | Erev Rosh Chodesh Shevat\n" +
      "2014 /  1 /  2 | yom      | no      | no       | Rosh Chodesh Shevat\n" +
      "2014 /  1 /  3 | erev     | yes     | no       | \n" +
      "2014 /  1 /  4 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  1 / 10 | erev     | yes     | no       | \n" +
      "2014 /  1 / 11 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  1 / 15 | erev     | no      | no       | Erev Tu B'Shvat\n" +
      "2014 /  1 / 16 | yom      | no      | no       | Tu B'Shvat\n" +
      "2014 /  1 / 17 | erev     | yes     | no       | \n" +
      "2014 /  1 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  1 / 24 | erev     | yes     | no       | \n" +
      "2014 /  1 / 25 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  1 / 30 | erev     | no      | no       | Erev Rosh Chodesh Adar I\n" +
      "2014 /  1 / 31 | erev     | yes     | no       | Rosh Chodesh Adar I (1)\n" +
      "2014 /  2 /  1 | yom      | no      | yes      | Shabbat / Rosh Chodesh Adar I (2)\n" +
      "2014 /  2 /  7 | erev     | yes     | no       | \n" +
      "2014 /  2 /  8 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  2 / 13 | erev     | no      | no       | Erev Purim Katan\n" +

      // You might think, "Here's a bug! This should be **yom**" But notice it's Erev Shabbat...
      "2014 /  2 / 14 | erev     | yes     | no       | Purim Katan\n" +
      "2014 /  2 / 15 | yom      | no      | yes      | Shabbat / Purim Katan\n" +
      "2014 /  2 / 21 | erev     | yes     | no       | \n" +
      "2014 /  2 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  2 / 28 | erev     | yes     | no       | \n" +
      "2014 /  3 /  1 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Adar II\n" +
      "2014 /  3 /  2 | yom      | no      | no       | Rosh Chodesh Adar II (1)\n" +
      "2014 /  3 /  3 | yom      | no      | no       | Rosh Chodesh Adar II (2)\n" +
      "2014 /  3 /  7 | erev     | yes     | no       | \n" +
      "2014 /  3 /  8 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  3 / 12 | erev     | no      | no       | Erev Ta'anit Esther\n" +
      "2014 /  3 / 13 | yom      | no      | no       | Ta'anit Esther\n" +
      "2014 /  3 / 14 | erev     | yes     | no       | \n" +
      "2014 /  3 / 15 | erev     | no      | yes      | Shabbat / Erev Purim\n" +
      "2014 /  3 / 16 | yom      | no      | no       | Purim\n" +
      "2014 /  3 / 17 | yom      | no      | no       | Shushan Purim\n" +
      "2014 /  3 / 21 | erev     | yes     | no       | \n" +
      "2014 /  3 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  3 / 28 | erev     | yes     | no       | \n" +
      "2014 /  3 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  3 / 31 | erev     | no      | no       | Erev Rosh Chodesh Nissan\n" +
      "2014 /  4 /  1 | yom      | no      | no       | Rosh Chodesh Nissan\n" +
      "2014 /  4 /  2 | yom      | no      | no       | \n" +
      "2014 /  4 /  3 | yom      | no      | no       | \n" +
      "2014 /  4 /  4 | erev     | yes     | no       | \n" +
      "2014 /  4 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  4 /  6 | yom      | no      | no       | \n" +
      "2014 /  4 /  7 | yom      | no      | no       | \n" +
      "2014 /  4 /  8 | yom      | no      | no       | \n" +
      "2014 /  4 /  9 | yom      | no      | no       | \n" +
      "2014 /  4 / 10 | yom      | no      | no       | \n" +
      "2014 /  4 / 11 | erev     | yes     | no       | \n" +
      "2014 /  4 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  4 / 13 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
      "2014 /  4 / 14 | erev     | yes     | no       | Ta'anit Bechorot / Erev Pesach\n" +
      "2014 /  4 / 15 | yom      | yes     | no       | Pesach\n" +
      "2014 /  4 / 16 | yom      | no      | yes      | Pesach / Omer 1\n" +
      "2014 /  4 / 17 | yom      | no      | no       | Chol HaMoed Pesach 3 / Omer 2\n" +
      "2014 /  4 / 18 | erev     | yes     | no       | Chol HaMoed Pesach 4 / Omer 3\n" +
      "2014 /  4 / 19 | yom      | no      | yes      | Shabbat / Chol HaMoed Pesach 5 / Omer 4\n" +
      "2014 /  4 / 20 | yom      | yes     | no       | Chol HaMoed Pesach 6 / Omer 5\n" +
      "2014 /  4 / 21 | yom      | yes     | no       | Pesach / Omer 6\n" +
      "2014 /  4 / 22 | yom      | no      | yes      | Pesach / Omer 7\n" +
      "2014 /  4 / 23 | yom      | no      | no       | Omer 8\n" +
      "2014 /  4 / 24 | yom      | no      | no       | Omer 9\n" +
      "2014 /  4 / 25 | erev     | yes     | no       | Omer 10\n" +
      "2014 /  4 / 26 | yom      | no      | yes      | Shabbat / Omer 11\n" +
      "2014 /  4 / 27 | erev     | no      | no       | Erev Yom HaShoah / Omer 12\n" +
      "2014 /  4 / 28 | yom      | no      | no       | Yom HaShoah / Omer 13\n" +
      "2014 /  4 / 29 | erev     | no      | no       | Erev Rosh Chodesh Iyar / Omer 14\n" +
      "2014 /  4 / 30 | yom      | no      | no       | Rosh Chodesh Iyar (1) / Omer 15\n" +
      "2014 /  5 /  1 | yom      | no      | no       | Rosh Chodesh Iyar (2) / Omer 16\n" +
      "2014 /  5 /  2 | erev     | yes     | no       | Omer 17\n" +
      "2014 /  5 /  3 | yom      | no      | yes      | Shabbat / Omer 18\n" +
      "2014 /  5 /  4 | erev     | no      | no       | Erev Yom HaZikaron / Omer 19\n" +
      "2014 /  5 /  5 | erev     | no      | no       | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20\n" +
      "2014 /  5 /  6 | yom      | no      | no       | Yom HaAtzmaut / Omer 21\n" +
      "2014 /  5 /  7 | yom      | no      | no       | Omer 22\n" +
      "2014 /  5 /  8 | yom      | no      | no       | Omer 23\n" +
      "2014 /  5 /  9 | erev     | yes     | no       | Omer 24\n" +
      "2014 /  5 / 10 | yom      | no      | yes      | Shabbat / Omer 25\n" +
      "2014 /  5 / 11 | yom      | no      | no       | Omer 26\n" +
      "2014 /  5 / 12 | yom      | no      | no       | Omer 27\n" +
      "2014 /  5 / 13 | erev     | no      | no       | Erev Pesach Sheni / Omer 28\n" +
      "2014 /  5 / 14 | yom      | no      | no       | Pesach Sheni / Omer 29\n" +
      "2014 /  5 / 15 | yom      | no      | no       | Omer 30\n" +
      "2014 /  5 / 16 | erev     | yes     | no       | Omer 31\n" +
      "2014 /  5 / 17 | erev     | no      | yes      | Shabbat / Erev Lag B'Omer / Omer 32\n" +
      "2014 /  5 / 18 | yom      | no      | no       | Lag B'Omer / Omer 33\n" +
      "2014 /  5 / 19 | yom      | no      | no       | Omer 34\n" +
      "2014 /  5 / 20 | yom      | no      | no       | Omer 35\n" +
      "2014 /  5 / 21 | yom      | no      | no       | Omer 36\n" +
      "2014 /  5 / 22 | yom      | no      | no       | Omer 37\n" +
      "2014 /  5 / 23 | erev     | yes     | no       | Omer 38\n" +
      "2014 /  5 / 24 | yom      | no      | yes      | Shabbat / Omer 39\n" +
      "2014 /  5 / 25 | yom      | no      | no       | Omer 40\n" +
      "2014 /  5 / 26 | yom      | no      | no       | Omer 41\n" +
      "2014 /  5 / 27 | erev     | no      | no       | Erev Yom Yerushalyim / Omer 42\n" +
      "2014 /  5 / 28 | yom      | no      | no       | Yom Yerushalyim / Omer 43\n" +
      "2014 /  5 / 29 | erev     | no      | no       | Erev Rosh Chodesh Sivan / Omer 44\n" +
      "2014 /  5 / 30 | erev     | yes     | no       | Rosh Chodesh Sivan / Omer 45\n" +
      "2014 /  5 / 31 | yom      | no      | yes      | Shabbat / Omer 46\n" +
      "2014 /  6 /  1 | yom      | no      | no       | Omer 47\n" +
      "2014 /  6 /  2 | yom      | no      | no       | Omer 48\n" +
      "2014 /  6 /  3 | erev     | yes     | no       | Erev Shavuot / Omer 49\n" +
      "2014 /  6 /  4 | yom      | yes     | no       | Shavuot\n" +
      "2014 /  6 /  5 | yom      | no      | yes      | Shavuot\n" +
      "2014 /  6 /  6 | erev     | yes     | no       | \n" +
      "2014 /  6 /  7 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  6 /  8 | yom      | no      | no       | \n" +
      "2014 /  6 /  9 | yom      | no      | no       | \n" +
      "2014 /  6 / 10 | yom      | no      | no       | \n" +
      "2014 /  6 / 13 | erev     | yes     | no       | \n" +
      "2014 /  6 / 14 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  6 / 20 | erev     | yes     | no       | \n" +
      "2014 /  6 / 21 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  6 / 27 | erev     | yes     | no       | Erev Rosh Chodesh Tammuz\n" +
      "2014 /  6 / 28 | yom      | no      | yes      | Shabbat / Rosh Chodesh Tammuz (1)\n" +
      "2014 /  6 / 29 | yom      | no      | no       | Rosh Chodesh Tammuz (2)\n" +
      "2014 /  7 /  4 | erev     | yes     | no       | \n" +
      "2014 /  7 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  7 / 11 | erev     | yes     | no       | \n" +
      "2014 /  7 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  7 / 14 | erev     | no      | no       | Erev Ta'anit 17 Tamuz\n" +
      "2014 /  7 / 15 | yom      | no      | no       | Ta'anit 17 Tamuz\n" +
      "2014 /  7 / 18 | erev     | yes     | no       | \n" +
      "2014 /  7 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  7 / 25 | erev     | yes     | no       | \n" +
      "2014 /  7 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  7 / 27 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
      "2014 /  7 / 28 | yom      | no      | no       | Rosh Chodesh Av\n" +
      "2014 /  8 /  1 | erev     | yes     | no       | \n" +
      "2014 /  8 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  8 /  4 | erev     | no      | no       | Erev Tisha B'Av\n" +
      "2014 /  8 /  5 | yom      | no      | no       | Tisha B'Av\n" +
      "2014 /  8 /  8 | erev     | yes     | no       | \n" +
      "2014 /  8 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  8 / 10 | erev     | no      | no       | Erev Tu B'Av\n" +
      "2014 /  8 / 11 | yom      | no      | no       | Tu B'Av\n" +
      "2014 /  8 / 15 | erev     | yes     | no       | \n" +
      "2014 /  8 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  8 / 22 | erev     | yes     | no       | \n" +
      "2014 /  8 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  8 / 25 | erev     | no      | no       | Erev Rosh Chodesh Elul\n" +
      "2014 /  8 / 26 | yom      | no      | no       | Rosh Chodesh Elul (1)\n" +
      "2014 /  8 / 27 | yom      | no      | no       | Rosh Chodesh Elul (2)\n" +
      "2014 /  8 / 29 | erev     | yes     | no       | \n" +
      "2014 /  8 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  9 /  5 | erev     | yes     | no       | \n" +
      "2014 /  9 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  9 / 12 | erev     | yes     | no       | \n" +
      "2014 /  9 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  9 / 19 | erev     | yes     | no       | \n" +
      "2014 /  9 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2014 /  9 / 24 | erev     | yes     | no       | Erev Rosh Hashana\n"
    );
  }


  @Test
  public void test5775() {

    runTest( 5775,
      "2014 /  9 / 24 | erev     | yes     | no       | Erev Rosh Hashana\n" +
      "2014 /  9 / 25 | yom      | yes     | no       | Rosh Hashana\n" +
      "2014 /  9 / 26 | erev     | yes     | no       | Rosh Hashana\n" +
      "2014 /  9 / 27 | erev     | no      | yes      | Shabbat / Erev Tzom Gedaliah\n" +
      "2014 /  9 / 28 | yom      | no      | no       | Tzom Gedaliah\n" +
      "2014 / 10 /  3 | erev     | yes     | no       | Erev Yom Kippur\n" +
      "2014 / 10 /  4 | yom      | no      | yes      | Shabbat / Yom Kippur\n" +
      "2014 / 10 /  5 | yom      | no      | no       | \n" +
      "2014 / 10 /  6 | yom      | no      | no       | \n" +
      "2014 / 10 /  7 | yom      | no      | no       | \n" +
      "2014 / 10 /  8 | erev     | yes     | no       | Erev Sukkot\n" +
      "2014 / 10 /  9 | yom      | yes     | no       | Sukkot\n" +
      "2014 / 10 / 10 | erev     | yes     | no       | Sukkot\n" +
      "2014 / 10 / 11 | yom      | no      | yes      | Shabbat / Chol HaMoed Sukkot 3\n" +
      "2014 / 10 / 12 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
      "2014 / 10 / 13 | yom      | no      | no       | Chol HaMoed Sukkot 5\n" +
      "2014 / 10 / 14 | yom      | no      | no       | Chol HaMoed Sukkot 6\n" +
      "2014 / 10 / 15 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
      "2014 / 10 / 16 | yom      | yes     | no       | Shmini Atzeret\n" +
      "2014 / 10 / 17 | erev     | yes     | no       | Simchat Torah\n" +
      "2014 / 10 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 10 / 19 | yom      | no      | no       | \n" +
      "2014 / 10 / 20 | yom      | no      | no       | \n" +
      "2014 / 10 / 21 | yom      | no      | no       | \n" +
      "2014 / 10 / 22 | yom      | no      | no       | \n" +
      "2014 / 10 / 23 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
      "2014 / 10 / 24 | erev     | yes     | no       | Rosh Chodesh Heshvan (1)\n" +
      "2014 / 10 / 25 | yom      | no      | yes      | Shabbat / Rosh Chodesh Heshvan (2)\n" +
      "2014 / 10 / 31 | erev     | yes     | no       | \n" +
      "2014 / 11 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 11 /  7 | erev     | yes     | no       | \n" +
      "2014 / 11 /  8 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 11 / 14 | erev     | yes     | no       | \n" +
      "2014 / 11 / 15 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 11 / 21 | erev     | yes     | no       | \n" +
      "2014 / 11 / 22 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Kislev\n" +
      "2014 / 11 / 23 | yom      | no      | no       | Rosh Chodesh Kislev\n" +
      "2014 / 11 / 28 | erev     | yes     | no       | \n" +
      "2014 / 11 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 12 /  5 | erev     | yes     | no       | \n" +
      "2014 / 12 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 12 / 12 | erev     | yes     | no       | \n" +
      "2014 / 12 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 12 / 16 | erev     | no      | no       | Erev Chanukah\n" +
      "2014 / 12 / 17 | yom      | no      | no       | Chanukah 1\n" +
      "2014 / 12 / 18 | yom      | no      | no       | Chanukah 2\n" +
      "2014 / 12 / 19 | erev     | yes     | no       | Chanukah 3\n" +
      "2014 / 12 / 20 | yom      | no      | yes      | Shabbat / Chanukah 4\n" +
      "2014 / 12 / 21 | erev     | no      | no       | Chanukah 5 / Erev Rosh Chodesh Tevet\n" +
      "2014 / 12 / 22 | yom      | no      | no       | Rosh Chodesh Tevet (1) / Chanukah 6\n" +
      "2014 / 12 / 23 | yom      | no      | no       | Rosh Chodesh Tevet (2) / Chanukah 7\n" +
      "2014 / 12 / 24 | yom      | no      | no       | Chanukah 8\n" +
      "2014 / 12 / 26 | erev     | yes     | no       | \n" +
      "2014 / 12 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2014 / 12 / 31 | erev     | no      | no       | Erev Asara B'Tevet\n" +
      "2015 /  1 /  1 | yom      | no      | no       | Asara B'Tevet\n" +
      "2015 /  1 /  2 | erev     | yes     | no       | \n" +
      "2015 /  1 /  3 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  1 /  9 | erev     | yes     | no       | \n" +
      "2015 /  1 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  1 / 16 | erev     | yes     | no       | \n" +
      "2015 /  1 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  1 / 20 | erev     | no      | no       | Erev Rosh Chodesh Shevat\n" +
      "2015 /  1 / 21 | yom      | no      | no       | Rosh Chodesh Shevat\n" +
      "2015 /  1 / 23 | erev     | yes     | no       | \n" +
      "2015 /  1 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  1 / 30 | erev     | yes     | no       | \n" +
      "2015 /  1 / 31 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  2 /  3 | erev     | no      | no       | Erev Tu B'Shvat\n" +
      "2015 /  2 /  4 | yom      | no      | no       | Tu B'Shvat\n" +
      "2015 /  2 /  6 | erev     | yes     | no       | \n" +
      "2015 /  2 /  7 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  2 / 13 | erev     | yes     | no       | \n" +
      "2015 /  2 / 14 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  2 / 18 | erev     | no      | no       | Erev Rosh Chodesh Adar\n" +
      "2015 /  2 / 19 | yom      | no      | no       | Rosh Chodesh Adar\n" +
      "2015 /  2 / 20 | erev     | yes     | no       | Rosh Chodesh Adar\n" +
      "2015 /  2 / 21 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  2 / 27 | erev     | yes     | no       | \n" +
      "2015 /  2 / 28 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  3 /  3 | erev     | no      | no       | Erev Ta'anit Esther\n" +
      "2015 /  3 /  4 | erev     | no      | no       | Ta'anit Esther / Erev Purim\n" +
      "2015 /  3 /  5 | yom      | no      | no       | Purim\n" +
      "2015 /  3 /  6 | erev     | yes     | no       | Shushan Purim\n" +
      "2015 /  3 /  7 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  3 / 13 | erev     | yes     | no       | \n" +
      "2015 /  3 / 14 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  3 / 20 | erev     | yes     | no       | Erev Rosh Chodesh Nissan\n" +
      "2015 /  3 / 21 | yom      | no      | yes      | Shabbat / Rosh Chodesh Nissan\n" +
      "2015 /  3 / 22 | yom      | no      | no       | \n" +
      "2015 /  3 / 23 | yom      | no      | no       | \n" +
      "2015 /  3 / 24 | yom      | no      | no       | \n" +
      "2015 /  3 / 25 | yom      | no      | no       | \n" +
      "2015 /  3 / 26 | yom      | no      | no       | \n" +
      "2015 /  3 / 27 | erev     | yes     | no       | \n" +
      "2015 /  3 / 28 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  3 / 29 | yom      | no      | no       | \n" +
      "2015 /  3 / 30 | yom      | no      | no       | \n" +
      "2015 /  3 / 31 | yom      | no      | no       | \n" +
      "2015 /  4 /  1 | yom      | no      | no       | \n" +
      "2015 /  4 /  2 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
      "2015 /  4 /  3 | erev     | yes     | no       | Ta'anit Bechorot / Erev Pesach\n" +
      "2015 /  4 /  4 | yom      | no      | no       | Shabbat / Pesach\n" +
      "2015 /  4 /  5 | yom      | no      | yes      | Pesach / Omer 1\n" +
      "2015 /  4 /  6 | yom      | no      | no       | Chol HaMoed Pesach 3 / Omer 2\n" +
      "2015 /  4 /  7 | yom      | no      | no       | Chol HaMoed Pesach 4 / Omer 3\n" +
      "2015 /  4 /  8 | yom      | no      | no       | Chol HaMoed Pesach 5 / Omer 4\n" +
      "2015 /  4 /  9 | yom      | yes     | no       | Chol HaMoed Pesach 6 / Omer 5\n" +
      "2015 /  4 / 10 | erev     | yes     | no       | Pesach / Omer 6\n" +
      "2015 /  4 / 11 | yom      | no      | yes      | Shabbat / Pesach / Omer 7\n" +
      "2015 /  4 / 12 | yom      | no      | no       | Omer 8\n" +
      "2015 /  4 / 13 | yom      | no      | no       | Omer 9\n" +
      "2015 /  4 / 14 | yom      | no      | no       | Omer 10\n" +
      "2015 /  4 / 15 | erev     | no      | no       | Erev Yom HaShoah / Omer 11\n" +
      "2015 /  4 / 16 | yom      | no      | no       | Yom HaShoah / Omer 12\n" +
      "2015 /  4 / 17 | erev     | yes     | no       | Omer 13\n" +
      "2015 /  4 / 18 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Iyar / Omer 14\n" +
      "2015 /  4 / 19 | yom      | no      | no       | Rosh Chodesh Iyar (1) / Omer 15\n" +
      "2015 /  4 / 20 | yom      | no      | no       | Rosh Chodesh Iyar (2) / Omer 16\n" +
      "2015 /  4 / 21 | erev     | no      | no       | Erev Yom HaZikaron / Omer 17\n" +
      "2015 /  4 / 22 | erev     | no      | no       | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18\n" +
      "2015 /  4 / 23 | yom      | no      | no       | Yom HaAtzmaut / Omer 19\n" +
      "2015 /  4 / 24 | erev     | yes     | no       | Omer 20\n" +
      "2015 /  4 / 25 | yom      | no      | yes      | Shabbat / Omer 21\n" +
      "2015 /  4 / 26 | yom      | no      | no       | Omer 22\n" +
      "2015 /  4 / 27 | yom      | no      | no       | Omer 23\n" +
      "2015 /  4 / 28 | yom      | no      | no       | Omer 24\n" +
      "2015 /  4 / 29 | yom      | no      | no       | Omer 25\n" +
      "2015 /  4 / 30 | yom      | no      | no       | Omer 26\n" +
      "2015 /  5 /  1 | erev     | yes     | no       | Omer 27\n" +
      "2015 /  5 /  2 | erev     | no      | yes      | Shabbat / Erev Pesach Sheni / Omer 28\n" +
      "2015 /  5 /  3 | yom      | no      | no       | Pesach Sheni / Omer 29\n" +
      "2015 /  5 /  4 | yom      | no      | no       | Omer 30\n" +
      "2015 /  5 /  5 | yom      | no      | no       | Omer 31\n" +
      "2015 /  5 /  6 | erev     | no      | no       | Erev Lag B'Omer / Omer 32\n" +
      "2015 /  5 /  7 | yom      | no      | no       | Lag B'Omer / Omer 33\n" +
      "2015 /  5 /  8 | erev     | yes     | no       | Omer 34\n" +
      "2015 /  5 /  9 | yom      | no      | yes      | Shabbat / Omer 35\n" +
      "2015 /  5 / 10 | yom      | no      | no       | Omer 36\n" +
      "2015 /  5 / 11 | yom      | no      | no       | Omer 37\n" +
      "2015 /  5 / 12 | yom      | no      | no       | Omer 38\n" +
      "2015 /  5 / 13 | yom      | no      | no       | Omer 39\n" +
      "2015 /  5 / 14 | yom      | no      | no       | Omer 40\n" +
      "2015 /  5 / 15 | erev     | yes     | no       | Omer 41\n" +
      "2015 /  5 / 16 | erev     | no      | yes      | Shabbat / Erev Yom Yerushalyim / Omer 42\n" +
      "2015 /  5 / 17 | yom      | no      | no       | Yom Yerushalyim / Omer 43\n" +
      "2015 /  5 / 18 | erev     | no      | no       | Erev Rosh Chodesh Sivan / Omer 44\n" +
      "2015 /  5 / 19 | yom      | no      | no       | Rosh Chodesh Sivan / Omer 45\n" +
      "2015 /  5 / 20 | yom      | no      | no       | Omer 46\n" +
      "2015 /  5 / 21 | yom      | no      | no       | Omer 47\n" +
      "2015 /  5 / 22 | erev     | yes     | no       | Omer 48\n" +
      "2015 /  5 / 23 | erev     | no      | no       | Shabbat / Erev Shavuot / Omer 49\n" +
      "2015 /  5 / 24 | yom      | yes     | no       | Shavuot\n" +
      "2015 /  5 / 25 | yom      | no      | yes      | Shavuot\n" +
      "2015 /  5 / 26 | yom      | no      | no       | \n" +
      "2015 /  5 / 27 | yom      | no      | no       | \n" +
      "2015 /  5 / 28 | yom      | no      | no       | \n" +
      "2015 /  5 / 29 | erev     | yes     | no       | \n" +
      "2015 /  5 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  6 /  5 | erev     | yes     | no       | \n" +
      "2015 /  6 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  6 / 12 | erev     | yes     | no       | \n" +
      "2015 /  6 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  6 / 16 | erev     | no      | no       | Erev Rosh Chodesh Tammuz\n" +
      "2015 /  6 / 17 | yom      | no      | no       | Rosh Chodesh Tammuz (1)\n" +
      "2015 /  6 / 18 | yom      | no      | no       | Rosh Chodesh Tammuz (2)\n" +
      "2015 /  6 / 19 | erev     | yes     | no       | \n" +
      "2015 /  6 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  6 / 26 | erev     | yes     | no       | \n" +
      "2015 /  6 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  7 /  3 | erev     | yes     | no       | \n" +
      "2015 /  7 /  4 | erev     | no      | yes      | Shabbat / Erev Ta'anit 17 Tamuz\n" +
      "2015 /  7 /  5 | yom      | no      | no       | Ta'anit 17 Tamuz\n" +
      "2015 /  7 / 10 | erev     | yes     | no       | \n" +
      "2015 /  7 / 11 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  7 / 16 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
      "2015 /  7 / 17 | erev     | yes     | no       | Rosh Chodesh Av\n" +
      "2015 /  7 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  7 / 24 | erev     | yes     | no       | \n" +
      "2015 /  7 / 25 | erev     | no      | yes      | Shabbat / Erev Tisha B'Av\n" +
      "2015 /  7 / 26 | yom      | no      | no       | Tisha B'Av\n" +
      "2015 /  7 / 30 | erev     | no      | no       | Erev Tu B'Av\n" +
      "2015 /  7 / 31 | erev     | yes     | no       | Tu B'Av\n" +
      "2015 /  8 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  8 /  7 | erev     | yes     | no       | \n" +
      "2015 /  8 /  8 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  8 / 14 | erev     | yes     | no       | Erev Rosh Chodesh Elul\n" +
      "2015 /  8 / 15 | yom      | no      | yes      | Shabbat / Rosh Chodesh Elul (1)\n" +
      "2015 /  8 / 16 | yom      | no      | no       | Rosh Chodesh Elul (2)\n" +
      "2015 /  8 / 21 | erev     | yes     | no       | \n" +
      "2015 /  8 / 22 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  8 / 28 | erev     | yes     | no       | \n" +
      "2015 /  8 / 29 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 /  4 | erev     | yes     | no       | \n" +
      "2015 /  9 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 / 11 | erev     | yes     | no       | \n" +
      "2015 /  9 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 / 13 | erev     | yes     | no       | Erev Rosh Hashana\n"
    );
  }


  @Test
  public void test5776() {

    runTest( 5776,
      "2015 /  9 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 / 13 | erev     | yes     | no       | Erev Rosh Hashana\n" +
      "2015 /  9 / 14 | yom      | yes     | no       | Rosh Hashana\n" +
      "2015 /  9 / 15 | erev     | no      | yes      | Rosh Hashana / Erev Tzom Gedaliah\n" +
      "2015 /  9 / 16 | yom      | no      | no       | Tzom Gedaliah\n" +
      "2015 /  9 / 18 | erev     | yes     | no       | \n" +
      "2015 /  9 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 / 22 | erev     | yes     | no       | Erev Yom Kippur\n" +
      "2015 /  9 / 23 | yom      | no      | yes      | Yom Kippur\n" +
      "2015 /  9 / 24 | yom      | no      | no       | \n" +
      "2015 /  9 / 25 | erev     | yes     | no       | \n" +
      "2015 /  9 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2015 /  9 / 27 | erev     | yes     | no       | Erev Sukkot\n" +
      "2015 /  9 / 28 | yom      | yes     | no       | Sukkot\n" +
      "2015 /  9 / 29 | yom      | no      | yes      | Sukkot\n" +
      "2015 /  9 / 30 | yom      | no      | no       | Chol HaMoed Sukkot 3\n" +
      "2015 / 10 /  1 | yom      | no      | no       | Chol HaMoed Sukkot 4\n" +
      "2015 / 10 /  2 | erev     | yes     | no       | Chol HaMoed Sukkot 5\n" +
      "2015 / 10 /  3 | yom      | no      | yes      | Shabbat / Chol HaMoed Sukkot 6\n" +
      "2015 / 10 /  4 | erev     | yes     | no       | Sukkot 7 (Hoshana Rabba)\n" +
      "2015 / 10 /  5 | yom      | yes     | no       | Shmini Atzeret\n" +
      "2015 / 10 /  6 | yom      | no      | yes      | Simchat Torah\n" +
      "2015 / 10 /  7 | yom      | no      | no       | \n" +
      "2015 / 10 /  8 | yom      | no      | no       | \n" +
      "2015 / 10 /  9 | erev     | yes     | no       | \n" +
      "2015 / 10 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 10 / 11 | yom      | no      | no       | \n" +
      "2015 / 10 / 12 | erev     | no      | no       | Erev Rosh Chodesh Heshvan\n" +
      "2015 / 10 / 13 | yom      | no      | no       | Rosh Chodesh Heshvan (1)\n" +
      "2015 / 10 / 14 | yom      | no      | no       | Rosh Chodesh Heshvan (2)\n" +
      "2015 / 10 / 16 | erev     | yes     | no       | \n" +
      "2015 / 10 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 10 / 23 | erev     | yes     | no       | \n" +
      "2015 / 10 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 10 / 30 | erev     | yes     | no       | \n" +
      "2015 / 10 / 31 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 11 /  6 | erev     | yes     | no       | \n" +
      "2015 / 11 /  7 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 11 / 11 | erev     | no      | no       | Erev Rosh Chodesh Kislev\n" +
      "2015 / 11 / 12 | yom      | no      | no       | Rosh Chodesh Kislev (1)\n" +
      "2015 / 11 / 13 | erev     | yes     | no       | Rosh Chodesh Kislev (2)\n" +
      "2015 / 11 / 14 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 11 / 20 | erev     | yes     | no       | \n" +
      "2015 / 11 / 21 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 11 / 27 | erev     | yes     | no       | \n" +
      "2015 / 11 / 28 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 12 /  4 | erev     | yes     | no       | \n" +
      "2015 / 12 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 12 /  6 | erev     | no      | no       | Erev Chanukah\n" +
      "2015 / 12 /  7 | yom      | no      | no       | Chanukah 1\n" +
      "2015 / 12 /  8 | yom      | no      | no       | Chanukah 2\n" +
      "2015 / 12 /  9 | yom      | no      | no       | Chanukah 3\n" +
      "2015 / 12 / 10 | yom      | no      | no       | Chanukah 4\n" +
      "2015 / 12 / 11 | erev     | yes     | no       | Chanukah 5 / Erev Rosh Chodesh Tevet\n" +
      "2015 / 12 / 12 | yom      | no      | yes      | Shabbat / Rosh Chodesh Tevet (1) / Chanukah 6\n" +
      "2015 / 12 / 13 | yom      | no      | no       | Rosh Chodesh Tevet (2) / Chanukah 7\n" +
      "2015 / 12 / 14 | yom      | no      | no       | Chanukah 8\n" +
      "2015 / 12 / 18 | erev     | yes     | no       | \n" +
      "2015 / 12 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2015 / 12 / 21 | erev     | no      | no       | Erev Asara B'Tevet\n" +
      "2015 / 12 / 22 | yom      | no      | no       | Asara B'Tevet\n" +
      "2015 / 12 / 25 | erev     | yes     | no       | \n" +
      "2015 / 12 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  1 /  1 | erev     | yes     | no       | \n" +
      "2016 /  1 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  1 /  8 | erev     | yes     | no       | \n" +
      "2016 /  1 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  1 / 10 | erev     | no      | no       | Erev Rosh Chodesh Shevat\n" +
      "2016 /  1 / 11 | yom      | no      | no       | Rosh Chodesh Shevat\n" +
      "2016 /  1 / 15 | erev     | yes     | no       | \n" +
      "2016 /  1 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  1 / 22 | erev     | yes     | no       | \n" +
      "2016 /  1 / 23 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  1 / 24 | erev     | no      | no       | Erev Tu B'Shvat\n" +
      "2016 /  1 / 25 | yom      | no      | no       | Tu B'Shvat\n" +
      "2016 /  1 / 29 | erev     | yes     | no       | \n" +
      "2016 /  1 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  2 /  5 | erev     | yes     | no       | \n" +
      "2016 /  2 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  2 /  8 | erev     | no      | no       | Erev Rosh Chodesh Adar I\n" +
      "2016 /  2 /  9 | yom      | no      | no       | Rosh Chodesh Adar I (1)\n" +
      "2016 /  2 / 10 | yom      | no      | no       | Rosh Chodesh Adar I (2)\n" +
      "2016 /  2 / 12 | erev     | yes     | no       | \n" +
      "2016 /  2 / 13 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  2 / 19 | erev     | yes     | no       | \n" +
      "2016 /  2 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  2 / 22 | erev     | no      | no       | Erev Purim Katan\n" +
      "2016 /  2 / 23 | yom      | no      | no       | Purim Katan\n" +
      "2016 /  2 / 24 | yom      | no      | no       | Purim Katan\n" +
      "2016 /  2 / 26 | erev     | yes     | no       | \n" +
      "2016 /  2 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  3 /  4 | erev     | yes     | no       | \n" +
      "2016 /  3 /  5 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  3 /  9 | erev     | no      | no       | Erev Rosh Chodesh Adar II\n" +
      "2016 /  3 / 10 | yom      | no      | no       | Rosh Chodesh Adar II (1)\n" +
      "2016 /  3 / 11 | erev     | yes     | no       | Rosh Chodesh Adar II (2)\n" +
      "2016 /  3 / 12 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  3 / 18 | erev     | yes     | no       | \n" +
      "2016 /  3 / 19 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  3 / 22 | erev     | no      | no       | Erev Ta'anit Esther\n" +
      "2016 /  3 / 23 | erev     | no      | no       | Ta'anit Esther / Erev Purim\n" +
      "2016 /  3 / 24 | yom      | no      | no       | Purim\n" +
      "2016 /  3 / 25 | erev     | yes     | no       | Shushan Purim\n" +
      "2016 /  3 / 26 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  4 /  1 | erev     | yes     | no       | \n" +
      "2016 /  4 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  4 /  8 | erev     | yes     | no       | Erev Rosh Chodesh Nissan\n" +
      "2016 /  4 /  9 | yom      | no      | yes      | Shabbat / Rosh Chodesh Nissan\n" +
      "2016 /  4 / 10 | yom      | no      | no       | \n" +
      "2016 /  4 / 11 | yom      | no      | no       | \n" +
      "2016 /  4 / 12 | yom      | no      | no       | \n" +
      "2016 /  4 / 13 | yom      | no      | no       | \n" +
      "2016 /  4 / 14 | yom      | no      | no       | \n" +
      "2016 /  4 / 15 | erev     | yes     | no       | \n" +
      "2016 /  4 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  4 / 17 | yom      | no      | no       | \n" +
      "2016 /  4 / 18 | yom      | no      | no       | \n" +
      "2016 /  4 / 19 | yom      | no      | no       | \n" +
      "2016 /  4 / 20 | yom      | no      | no       | \n" +
      "2016 /  4 / 21 | erev     | no      | no       | Erev Ta'anit Bechorot\n" +
      "2016 /  4 / 22 | erev     | yes     | no       | Ta'anit Bechorot / Erev Pesach\n" +
      "2016 /  4 / 23 | yom      | no      | no       | Shabbat / Pesach\n" +
      "2016 /  4 / 24 | yom      | no      | yes      | Pesach / Omer 1\n" +
      "2016 /  4 / 25 | yom      | no      | no       | Chol HaMoed Pesach 3 / Omer 2\n" +
      "2016 /  4 / 26 | yom      | no      | no       | Chol HaMoed Pesach 4 / Omer 3\n" +
      "2016 /  4 / 27 | yom      | no      | no       | Chol HaMoed Pesach 5 / Omer 4\n" +
      "2016 /  4 / 28 | yom      | yes     | no       | Chol HaMoed Pesach 6 / Omer 5\n" +
      "2016 /  4 / 29 | erev     | yes     | no       | Pesach / Omer 6\n" +
      "2016 /  4 / 30 | yom      | no      | yes      | Shabbat / Pesach / Omer 7\n" +
      "2016 /  5 /  1 | yom      | no      | no       | Omer 8\n" +
      "2016 /  5 /  2 | yom      | no      | no       | Omer 9\n" +
      "2016 /  5 /  3 | yom      | no      | no       | Omer 10\n" +
      "2016 /  5 /  4 | erev     | no      | no       | Erev Yom HaShoah / Omer 11\n" +
      "2016 /  5 /  5 | yom      | no      | no       | Yom HaShoah / Omer 12\n" +
      "2016 /  5 /  6 | erev     | yes     | no       | Omer 13\n" +
      "2016 /  5 /  7 | erev     | no      | yes      | Shabbat / Erev Rosh Chodesh Iyar / Omer 14\n" +
      "2016 /  5 /  8 | yom      | no      | no       | Rosh Chodesh Iyar (1) / Omer 15\n" +
      "2016 /  5 /  9 | yom      | no      | no       | Rosh Chodesh Iyar (2) / Omer 16\n" +
      "2016 /  5 / 10 | erev     | no      | no       | Erev Yom HaZikaron / Omer 17\n" +
      "2016 /  5 / 11 | erev     | no      | no       | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18\n" +
      "2016 /  5 / 12 | yom      | no      | no       | Yom HaAtzmaut / Omer 19\n" +
      "2016 /  5 / 13 | erev     | yes     | no       | Omer 20\n" +
      "2016 /  5 / 14 | yom      | no      | yes      | Shabbat / Omer 21\n" +
      "2016 /  5 / 15 | yom      | no      | no       | Omer 22\n" +
      "2016 /  5 / 16 | yom      | no      | no       | Omer 23\n" +
      "2016 /  5 / 17 | yom      | no      | no       | Omer 24\n" +
      "2016 /  5 / 18 | yom      | no      | no       | Omer 25\n" +
      "2016 /  5 / 19 | yom      | no      | no       | Omer 26\n" +
      "2016 /  5 / 20 | erev     | yes     | no       | Omer 27\n" +
      "2016 /  5 / 21 | erev     | no      | yes      | Shabbat / Erev Pesach Sheni / Omer 28\n" +
      "2016 /  5 / 22 | yom      | no      | no       | Pesach Sheni / Omer 29\n" +
      "2016 /  5 / 23 | yom      | no      | no       | Omer 30\n" +
      "2016 /  5 / 24 | yom      | no      | no       | Omer 31\n" +
      "2016 /  5 / 25 | erev     | no      | no       | Erev Lag B'Omer / Omer 32\n" +
      "2016 /  5 / 26 | yom      | no      | no       | Lag B'Omer / Omer 33\n" +
      "2016 /  5 / 27 | erev     | yes     | no       | Omer 34\n" +
      "2016 /  5 / 28 | yom      | no      | yes      | Shabbat / Omer 35\n" +
      "2016 /  5 / 29 | yom      | no      | no       | Omer 36\n" +
      "2016 /  5 / 30 | yom      | no      | no       | Omer 37\n" +
      "2016 /  5 / 31 | yom      | no      | no       | Omer 38\n" +
      "2016 /  6 /  1 | yom      | no      | no       | Omer 39\n" +
      "2016 /  6 /  2 | yom      | no      | no       | Omer 40\n" +
      "2016 /  6 /  3 | erev     | yes     | no       | Omer 41\n" +
      "2016 /  6 /  4 | erev     | no      | yes      | Shabbat / Erev Yom Yerushalyim / Omer 42\n" +
      "2016 /  6 /  5 | yom      | no      | no       | Yom Yerushalyim / Omer 43\n" +
      "2016 /  6 /  6 | erev     | no      | no       | Erev Rosh Chodesh Sivan / Omer 44\n" +
      "2016 /  6 /  7 | yom      | no      | no       | Rosh Chodesh Sivan / Omer 45\n" +
      "2016 /  6 /  8 | yom      | no      | no       | Omer 46\n" +
      "2016 /  6 /  9 | yom      | no      | no       | Omer 47\n" +
      "2016 /  6 / 10 | erev     | yes     | no       | Omer 48\n" +
      "2016 /  6 / 11 | erev     | no      | no       | Shabbat / Erev Shavuot / Omer 49\n" +
      "2016 /  6 / 12 | yom      | yes     | no       | Shavuot\n" +
      "2016 /  6 / 13 | yom      | no      | yes      | Shavuot\n" +
      "2016 /  6 / 14 | yom      | no      | no       | \n" +
      "2016 /  6 / 15 | yom      | no      | no       | \n" +
      "2016 /  6 / 16 | yom      | no      | no       | \n" +
      "2016 /  6 / 17 | erev     | yes     | no       | \n" +
      "2016 /  6 / 18 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  6 / 24 | erev     | yes     | no       | \n" +
      "2016 /  6 / 25 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  7 /  1 | erev     | yes     | no       | \n" +
      "2016 /  7 /  2 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  7 /  5 | erev     | no      | no       | Erev Rosh Chodesh Tammuz\n" +
      "2016 /  7 /  6 | yom      | no      | no       | Rosh Chodesh Tammuz (1)\n" +
      "2016 /  7 /  7 | yom      | no      | no       | Rosh Chodesh Tammuz (2)\n" +
      "2016 /  7 /  8 | erev     | yes     | no       | \n" +
      "2016 /  7 /  9 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  7 / 15 | erev     | yes     | no       | \n" +
      "2016 /  7 / 16 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  7 / 22 | erev     | yes     | no       | \n" +
      "2016 /  7 / 23 | erev     | no      | yes      | Shabbat / Erev Ta'anit 17 Tamuz\n" +
      "2016 /  7 / 24 | yom      | no      | no       | Ta'anit 17 Tamuz\n" +
      "2016 /  7 / 29 | erev     | yes     | no       | \n" +
      "2016 /  7 / 30 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  8 /  4 | erev     | no      | no       | Erev Rosh Chodesh Av\n" +
      "2016 /  8 /  5 | erev     | yes     | no       | Rosh Chodesh Av\n" +
      "2016 /  8 /  6 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  8 / 12 | erev     | yes     | no       | \n" +
      "2016 /  8 / 13 | erev     | no      | yes      | Shabbat / Erev Tisha B'Av\n" +
      "2016 /  8 / 14 | yom      | no      | no       | Tisha B'Av\n" +
      "2016 /  8 / 18 | erev     | no      | no       | Erev Tu B'Av\n" +
      "2016 /  8 / 19 | erev     | yes     | no       | Tu B'Av\n" +
      "2016 /  8 / 20 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  8 / 26 | erev     | yes     | no       | \n" +
      "2016 /  8 / 27 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  9 /  2 | erev     | yes     | no       | Erev Rosh Chodesh Elul\n" +
      "2016 /  9 /  3 | yom      | no      | yes      | Shabbat / Rosh Chodesh Elul (1)\n" +
      "2016 /  9 /  4 | yom      | no      | no       | Rosh Chodesh Elul (2)\n" +
      "2016 /  9 /  9 | erev     | yes     | no       | \n" +
      "2016 /  9 / 10 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  9 / 16 | erev     | yes     | no       | \n" +
      "2016 /  9 / 17 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  9 / 23 | erev     | yes     | no       | \n" +
      "2016 /  9 / 24 | yom      | no      | yes      | Shabbat\n" +
      "2016 /  9 / 30 | erev     | yes     | no       | \n" +
      "2016 / 10 /  1 | yom      | no      | yes      | Shabbat\n" +
      "2016 / 10 /  2 | erev     | yes     | no       | Erev Rosh Hashana\n"
    );
  }

  private void runTest( int year, String expected ) {

    HebrewDate date = HebrewCalendar.getForJewishDate( year - 1, HebrewMonth.ELUL, 28 );

    StringBuilder output = new StringBuilder();
    output.append( "Year / Mo / Dt | Erev/Yom | Candles | Havdalah | Description\n" );
    output.append( "-------------------------------------------------------------\n" );

    int endYear = year + 1;
    while ( endYear != date.getHebrewYear() ) {

      printDateAndDetails( output, date );
      date = date.tomorrow();
    }

    assertCalendar( output,
      "Year / Mo / Dt | Erev/Yom | Candles | Havdalah | Description\n" +
      "-------------------------------------------------------------\n" +
      expected
    );
  }


  private static void printDateAndDetails(
    StringBuilder output,
    HebrewDate    date
  ) {

    Details details = date.getDetails();

    if ( ( null == details ) || !details.isAnything() ) {
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

    Assertions.assertEquals( expected, output.toString());
  }

} // End of UT_HebrewCalendar class
