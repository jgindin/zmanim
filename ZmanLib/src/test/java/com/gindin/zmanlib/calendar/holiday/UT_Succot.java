/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.holiday.yomtov.Sukkot;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_Succot
  extends UT_AbstractHoliday {

  private static final Sukkot SUKKOT = new Sukkot();
  
  @Test
  public void test2010() {
    assertSukkot( 2010, Calendar.SEPTEMBER, 23,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      " 9/22/2010 W | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      " 9/23/2010 R | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/24/2010 F | Sukkot                                        | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/25/2010 B | Chol HaMoed Sukkot 3                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      " 9/26/2010 S | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/27/2010 M | Chol HaMoed Sukkot 5                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/28/2010 T | Chol HaMoed Sukkot 6                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/29/2010 W | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      " 9/30/2010 R | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/ 1/2010 F | Simchat Torah                                 | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n"
    );
  } 
  

  @Test
  public void test2011() {
    assertSukkot( 2011, Calendar.OCTOBER, 13,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      "10/12/2011 W | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/13/2011 R | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/14/2011 F | Sukkot                                        | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/15/2011 B | Chol HaMoed Sukkot 3                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      "10/16/2011 S | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/17/2011 M | Chol HaMoed Sukkot 5                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/18/2011 T | Chol HaMoed Sukkot 6                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/19/2011 W | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/20/2011 R | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/21/2011 F | Simchat Torah                                 | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n"
    );
  } 
  
  @Test
  public void test2012() {
    assertSukkot( 2012, Calendar.OCTOBER, 1,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      " 9/30/2012 S | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 1/2012 M | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/ 2/2012 T | Sukkot                                        | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n" +
      "10/ 3/2012 W | Chol HaMoed Sukkot 3                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/ 4/2012 R | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/ 5/2012 F | Chol HaMoed Sukkot 5                          | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 6/2012 B | Chol HaMoed Sukkot 6                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      "10/ 7/2012 S | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 8/2012 M | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/ 9/2012 T | Simchat Torah                                 | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
    );
  } 
  
  @Test
  public void test2013() {
    assertSukkot( 2013, Calendar.SEPTEMBER, 19,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      " 9/18/2013 W | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      " 9/19/2013 R | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/20/2013 F | Sukkot                                        | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/21/2013 B | Chol HaMoed Sukkot 3                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      " 9/22/2013 S | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/23/2013 M | Chol HaMoed Sukkot 5                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/24/2013 T | Chol HaMoed Sukkot 6                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      " 9/25/2013 W | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      " 9/26/2013 R | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/27/2013 F | Simchat Torah                                 | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n"
    );
  } 
  
  @Test
  public void test2014() {
    assertSukkot( 2014, Calendar.OCTOBER, 9,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      "10/ 8/2014 W | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 9/2014 R | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/10/2014 F | Sukkot                                        | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/11/2014 B | Chol HaMoed Sukkot 3                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      "10/12/2014 S | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/13/2014 M | Chol HaMoed Sukkot 5                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/14/2014 T | Chol HaMoed Sukkot 6                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/15/2014 W | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/16/2014 R | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/17/2014 F | Simchat Torah                                 | true  | false | true         | true  | false   | true    | false    | false  | false | false  |\n"
    );
  } 
  
  @Test
  public void test2015() {
    assertSukkot( 2015, Calendar.SEPTEMBER, 28,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      " 9/27/2015 S | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      " 9/28/2015 M | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      " 9/29/2015 T | Sukkot                                        | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n" +
      " 9/30/2015 W | Chol HaMoed Sukkot 3                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/ 1/2015 R | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/ 2/2015 F | Chol HaMoed Sukkot 5                          | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 3/2015 B | Chol HaMoed Sukkot 6                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      "10/ 4/2015 S | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/ 5/2015 M | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/ 6/2015 T | Simchat Torah                                 | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
    );
  } 
  
  @Test
  public void test2016() {
    assertSukkot( 2016, Calendar.OCTOBER, 17,
      "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" +
      "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" +
      "10/16/2016 S | Erev Sukkot                                   | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/17/2016 M | Sukkot                                        | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/18/2016 T | Sukkot                                        | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n" +
      "10/19/2016 W | Chol HaMoed Sukkot 3                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/20/2016 R | Chol HaMoed Sukkot 4                          | true  | false | false        | false | false   | false   | false    | false  | false | false  |\n" +
      "10/21/2016 F | Chol HaMoed Sukkot 5                          | true  | false | true         | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/22/2016 B | Chol HaMoed Sukkot 6                          | true  | false | false        | false | true    | false   | true     | false  | false | false  |\n" +
      "10/23/2016 S | Sukkot 7 (Hoshana Rabba)                      | true  | true  | false        | true  | false   | false   | false    | false  | false | false  |\n" +
      "10/24/2016 M | Shmini Atzeret                                | true  | false | false        | true  | false   | true    | false    | false  | false | false  |\n" +
      "10/25/2016 T | Simchat Torah                                 | true  | false | false        | false | false   | true    | true     | false  | false | false  |\n"
    );
  } 
  
  
  private void assertSukkot(
    int                 year,
    @CalendarMonth int  month,
    int                 day,
    String              expected
  ) {

    StringBuilder output = buildDetailsHeader();

    HebrewDate date;
    Details details;

    date = HebrewCalendar.getForDate( year, month, day );

    // Erev Sukkot
    date = date.yesterday();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot I
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot II
    // TODO: Handle Israel...
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot III
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot IV
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot V
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot VI
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );

    // Sukkot VII -- Hoshana Rabba
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );
    Assert.assertTrue( details.getDescription().contains( "Hoshana Rabba" ) );

    // Shmini Atzeret
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );
    Assert.assertTrue( details.getDescription().contains( "Shmini Atzeret" ) );

    // Simchat Torah
    date = date.tomorrow();
    details = SUKKOT.getDetailsFor( date );
    outputDetails( output, details );
    Assert.assertTrue( details.getDescription().contains( "Simchat Torah" ) );

    Assert.assertEquals( expected, output.toString() );
  }
  
} // End of UT_Succot class
