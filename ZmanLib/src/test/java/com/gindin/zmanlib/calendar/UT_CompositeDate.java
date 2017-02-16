/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar;

import com.gindin.zmanlib.calendar.holiday.Details;
import org.junit.*;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_CompositeDate {

  @Test
  public void testSortingTaanitBeforeErev() {

    HebrewDate date = HebrewCalendar.getForDate( 2011, Calendar.APRIL, 18 );
    Details details = date.getDetails();
    Assert.assertEquals( "Ta'anit Bechorot / Erev Pesach", details.getDescription() );
  }

  @Test
  public void testSortingChanukahBeforeErevRoshChodesh() {

    HebrewDate date = HebrewCalendar.getForDate( 2010, Calendar.DECEMBER, 6 );
    Details details = date.getDetails();
    Assert.assertEquals( "Chanukah 5 / Erev Rosh Chodesh Tevet", details.getDescription() );
  }

  @Test
  public void testSortingRoshHashanaBeforeErevTaanit() {

    HebrewDate date = HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 15 );
    Details details = date.getDetails();
    Assert.assertEquals( "Rosh Hashana / Erev Tzom Gedaliah", details.getDescription() );
  }

} // End of UT_CompositeDate class
