/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.modern.YomHaAtzmaut;
import com.gindin.zmanlib.calendar.holiday.modern.YomHaShoah;
import com.gindin.zmanlib.calendar.holiday.modern.YomHaZikaron;
import com.gindin.zmanlib.calendar.holiday.modern.YomYerushalayim;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Tests the dates for the 'modern' holidays.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Modern
  extends UT_AbstractHoliday {

  private static final YomHaShoah                           YOM_HA_SHOAH = new YomHaShoah();
  private static final YomHaZikaron                         YOM_HA_ZIKARON = new YomHaZikaron();
  private static final YomHaAtzmaut                         YOM_HA_ATZMAUT = new YomHaAtzmaut();
  private static final YomYerushalayim                      YOM_YERUSHALAYIM = new YomYerushalayim();


  @Test
  public void test1970() {
    // NOTE: www.hebcal.com shows Yom HaZikaron falling on *Sunday* May 10, 1970. However, the rules seem
    //  to say that if it falls on Sunday, it gets pushed off to Monday. Maybe they're being smart and
    //  handling some older version of the rules?
    testModernHolidays(
      1970,
      Calendar.MAY, 4,
      Calendar.MAY, 11,
      Calendar.JUNE, 3,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 5/ 3/1970 S | Erev Yom HaShoah / Omer 12                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 4/1970 M | Yom HaShoah / Omer 13                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 5/1970 T | Erev Rosh Chodesh Iyar / Omer 14              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 6/1970 W | Rosh Chodesh Iyar (1) / Omer 15               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/10/1970 S | Erev Yom HaZikaron / Omer 19                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/11/1970 M | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/11/1970 M | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/12/1970 T | Yom HaAtzmaut / Omer 21                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/13/1970 W | Omer 22                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/14/1970 R | Omer 23                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 2/1970 T | Erev Yom Yerushalyim / Omer 42                | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 3/1970 W | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 4/1970 R | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }


  @Test
  public void test2008() {
    testModernHolidays(
      2008,
      Calendar.MAY, 1,
      Calendar.MAY, 7,
      Calendar.JUNE, 2,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 4/30/2008 W | Erev Yom HaShoah / Omer 10                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 1/2008 R | Yom HaShoah / Omer 11                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 2/2008 F | Yom HaShoah / Omer 12                         | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 3/2008 B | Shabbat / Omer 13                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/ 6/2008 T | Rosh Chodesh Iyar (2) / Erev Yom HaZikaron / Omer 16 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 7/2008 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 17  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 7/2008 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 17  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 8/2008 R | Yom HaAtzmaut / Omer 18                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 9/2008 F | Omer 19                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 5/10/2008 B | Shabbat / Omer 20                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 6/ 1/2008 S | Erev Yom Yerushalyim / Omer 42                | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 2/2008 M | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 3/2008 T | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }

  @Test
  public void test2009() {
    testModernHolidays(
      2009,
      Calendar.APRIL, 21,
      Calendar.APRIL, 28,
      Calendar.MAY, 22,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 4/20/2009 M | Erev Yom HaShoah / Omer 11                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/21/2009 T | Yom HaShoah / Omer 12                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/22/2009 W | Omer 13                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/23/2009 R | Erev Rosh Chodesh Iyar / Omer 14              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/27/2009 M | Erev Yom HaZikaron / Omer 18                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/28/2009 T | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 19  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/28/2009 T | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 19  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/29/2009 W | Yom HaAtzmaut / Omer 20                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/30/2009 R | Omer 21                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 1/2009 F | Omer 22                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 5/21/2009 R | Erev Yom Yerushalyim / Omer 42                | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/22/2009 F | Yom Yerushalyim / Omer 43                     | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 5/23/2009 B | Shabbat / Erev Rosh Chodesh Sivan / Omer 44   | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n"
    );
  }

  @Test
  public void test2011() {
    testModernHolidays(
      2011,
      Calendar.MAY, 2,
      Calendar.MAY, 9,
      Calendar.JUNE, 1,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 5/ 1/2011 S | Erev Yom HaShoah / Omer 12                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 2/2011 M | Yom HaShoah / Omer 13                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 3/2011 T | Erev Rosh Chodesh Iyar / Omer 14              | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 4/2011 W | Rosh Chodesh Iyar (1) / Omer 15               | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 8/2011 S | Erev Yom HaZikaron / Omer 19                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 9/2011 M | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/ 9/2011 M | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 20  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/10/2011 T | Yom HaAtzmaut / Omer 21                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/11/2011 W | Omer 22                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/12/2011 R | Omer 23                                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/31/2011 T | Erev Yom Yerushalyim / Omer 42                | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 1/2011 W | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 6/ 2/2011 R | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }


  @Test
  public void test2012() {
    testModernHolidays(
      2012,
      Calendar.APRIL, 19,
      Calendar.APRIL, 25,
      Calendar.MAY, 20,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 4/18/2012 W | Erev Yom HaShoah / Omer 11                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/19/2012 R | Yom HaShoah / Omer 12                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/20/2012 F | Omer 13                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/21/2012 B | Shabbat / Erev Rosh Chodesh Iyar / Omer 14    | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 4/24/2012 T | Erev Yom HaZikaron / Omer 17                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/25/2012 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/25/2012 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/26/2012 R | Yom HaAtzmaut / Omer 19                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/27/2012 F | Omer 20                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/28/2012 B | Shabbat / Omer 21                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/19/2012 B | Shabbat / Erev Yom Yerushalyim / Omer 42      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/20/2012 S | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/21/2012 M | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }


  @Test
  public void test2015() {
    testModernHolidays(
      2015,
      Calendar.APRIL, 16,
      Calendar.APRIL, 22,
      Calendar.MAY, 17,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 4/15/2015 W | Erev Yom HaShoah / Omer 11                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/16/2015 R | Yom HaShoah / Omer 12                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/17/2015 F | Omer 13                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/18/2015 B | Shabbat / Erev Rosh Chodesh Iyar / Omer 14    | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 4/21/2015 T | Erev Yom HaZikaron / Omer 17                  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/22/2015 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/22/2015 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 18  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/23/2015 R | Yom HaAtzmaut / Omer 19                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/24/2015 F | Omer 20                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/25/2015 B | Shabbat / Omer 21                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/16/2015 B | Shabbat / Erev Yom Yerushalyim / Omer 42      | true  | true  | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/17/2015 S | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/18/2015 M | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }

  @Test
  public void test2021() {
    testModernHolidays(
      2021,
      Calendar.APRIL, 8,
      Calendar.APRIL, 14,
      Calendar.MAY, 10,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
        "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
        " 4/ 7/2021 W | Erev Yom HaShoah / Omer 10                    | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/ 8/2021 R | Yom HaShoah / Omer 11                         | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/ 9/2021 F | Yom HaShoah / Omer 12                         | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/10/2021 B | Shabbat / Omer 13                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 4/13/2021 T | Rosh Chodesh Iyar (2) / Erev Yom HaZikaron / Omer 16 | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/14/2021 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 17  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/14/2021 W | Yom HaZikaron / Erev Yom HaAtzmaut / Omer 17  | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/15/2021 R | Yom HaAtzmaut / Omer 18                       | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 4/16/2021 F | Omer 19                                       | true  | true  | true         | true  | false   | false   | false    | false  | false | false  |\n" +
        " 4/17/2021 B | Shabbat / Omer 20                             | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
        " 5/ 9/2021 S | Erev Yom Yerushalyim / Omer 42                | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/10/2021 M | Yom Yerushalyim / Omer 43                     | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
        " 5/11/2021 T | Erev Rosh Chodesh Sivan / Omer 44             | true  | true  | false        | false | false   | false   | false    | false  | false | false  |\n"
    );
  }


  private void testModernHolidays(
    int                 year,
    @CalendarMonth int  yomHaShoahMonth,
    int                 yomHaShoahDay,
    @CalendarMonth int  yomHaZikaronMonth,
    int                 yomHaZikaronDay,
    @CalendarMonth int  yomYerushalyimMonth,
    int                 yomYerushalyimDay,
    String              expected
  ) {

    StringBuilder output = buildDetailsHeader();

    HebrewDate date;
    Details details;

    /////////////////////////////////////////////////////////////////////////////////////
    // Yom HaShoah
    date = HebrewCalendar.getForDate( year, yomHaShoahMonth, yomHaShoahDay );
    details = date.yesterday().getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_HA_SHOAH.getName() ) );
    outputDetails( output, details );

    details = date.getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_HA_SHOAH.getName() ) );
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    outputDetails( output, details );

    /////////////////////////////////////////////////////////////////////////////////////
    // Yom HaZikaron
    // Note that we KNOW this is Erev Yom HaAtzma'ut...
    date = HebrewCalendar.getForDate( year, yomHaZikaronMonth, yomHaZikaronDay );
    details = date.yesterday().getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_HA_ZIKARON.getName() ) );
    outputDetails( output, details );

    details = date.getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_HA_ZIKARON.getName() ) );
    outputDetails( output, details );



    /////////////////////////////////////////////////////////////////////////////////////
    // Yom HaAtzmaut
    // Yes, Erev Yom HaAtzmaut is the same day as Yom HaZikaron...
    Assert.assertTrue( details.getDescription().contains( YOM_HA_ATZMAUT.getName() ) );
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_HA_ATZMAUT.getName() ) );
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    outputDetails( output, details );

    /////////////////////////////////////////////////////////////////////////////////////
    // Yom Yerushalyim
    date = HebrewCalendar.getForDate( year, yomYerushalyimMonth, yomYerushalyimDay );
    details = date.yesterday().getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_YERUSHALAYIM.getName() ) );
    outputDetails( output, details );

    details = date.getDetails();
    Assert.assertTrue( details.getDescription().contains( YOM_YERUSHALAYIM.getName() ) );
    outputDetails( output, details );

    date = date.tomorrow();
    details = date.getDetails();
    outputDetails( output, details );

    /////////////////////////////////////////////////////////////////////////////////////
    // Now check the full set of expectations
    Assert.assertEquals( expected, output.toString() );


    // Sprinkle some sanity checks in...
    date = HebrewCalendar.getForDate( year, yomHaShoahMonth - 1, 1 );
    Assert.assertNull( YOM_HA_SHOAH.getDetailsFor( date ) );
    Assert.assertNull( YOM_HA_ZIKARON.getDetailsFor( date ) );
    Assert.assertNull( YOM_HA_ATZMAUT.getDetailsFor( date ) );
    Assert.assertNull( YOM_YERUSHALAYIM.getDetailsFor( date ) );

    date = HebrewCalendar.getForDate( year, yomYerushalyimMonth + 2, 1 );
    Assert.assertNull( YOM_HA_SHOAH.getDetailsFor( date ) );
    Assert.assertNull( YOM_HA_ZIKARON.getDetailsFor( date ) );
    Assert.assertNull( YOM_HA_ATZMAUT.getDetailsFor( date ) );
    Assert.assertNull( YOM_YERUSHALAYIM.getDetailsFor( date ) );

  }

}  // End of UT_Modern class
