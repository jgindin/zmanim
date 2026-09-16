/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * Checks that we can roll forward & backward the gregorian dates...
 */
@SuppressWarnings( { "MagicNumber" } )
public class UT_GregorianDateNavigation {

  @Test
  public void gregorianForwardMonthToMonth() {

    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, 2011 );
    cal.set( Calendar.MONTH, Calendar.JANUARY );
    cal.set( Calendar.DATE, 31 );

    HebrewDate hebrewDate = HebrewCalendar.getForDate( cal );
    Assertions.assertEquals( 5771, hebrewDate.getHebrewYear() );
    Assertions.assertEquals( 11, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 26, hebrewDate.getHebrewDayOfMonth() );

    hebrewDate = hebrewDate.tomorrow();
    cal.setTime( hebrewDate.getTime() );
    Assertions.assertEquals( Calendar.FEBRUARY, cal.get( Calendar.MONTH ) );
    Assertions.assertEquals( 1, cal.get( Calendar.DAY_OF_MONTH ) );
    Assertions.assertEquals( 11, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 27, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.FEBRUARY );
    cal.set( Calendar.DATE, 28 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    Assertions.assertEquals( 12, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 24, hebrewDate.getHebrewDayOfMonth() );

    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 12, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 25, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.MARCH );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 13, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 26, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.APRIL );
    cal.set( Calendar.DATE, 30 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 1, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 27, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.MAY );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 2, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 28, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.JUNE );
    cal.set( Calendar.DATE, 30 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 3, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 29, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.JULY );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 5, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 1, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.AUGUST );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 6, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 2, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
    cal.set( Calendar.DATE, 30 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 7, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 3, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.OCTOBER );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 5772, hebrewDate.getHebrewYear() );
    Assertions.assertEquals( 8, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 4, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.NOVEMBER );
    cal.set( Calendar.DATE, 30 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 9, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 5, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.MONTH, Calendar.DECEMBER );
    cal.set( Calendar.DATE, 31 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.tomorrow();
    Assertions.assertEquals( 10, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 6, hebrewDate.getHebrewDayOfMonth() );
    Assertions.assertEquals( Calendar.JANUARY, hebrewDate.getGregorianMonth() );
    Assertions.assertEquals( 1, hebrewDate.getGregorianDayOfMonth() );
  }


  @Test
  public void gregorianBackwardMonthToMonth() {

    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, 2011 );
    cal.set( Calendar.MONTH, Calendar.JANUARY );
    cal.set( Calendar.DATE, 1 );

    HebrewDate hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 10, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 24, hebrewDate.getHebrewDayOfMonth() );
    Assertions.assertEquals( Calendar.DECEMBER, hebrewDate.getGregorianMonth() );
    Assertions.assertEquals( 31, hebrewDate.getGregorianDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.DECEMBER );
    cal.set( Calendar.YEAR, 2010 );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 9, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 23, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.NOVEMBER );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 8, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 23, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.OCTOBER );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 7, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 22, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.SEPTEMBER );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 5770, hebrewDate.getHebrewYear() );
    Assertions.assertEquals( 6, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 21, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.AUGUST );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 5, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 20, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.JULY );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 4, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 18, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.JUNE );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 3, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 18, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.MAY );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 2, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.APRIL );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 1, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.MARCH );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 12, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 14, hebrewDate.getHebrewDayOfMonth() );

    cal.set( Calendar.DATE, 1 );
    cal.set( Calendar.MONTH, Calendar.FEBRUARY );
    hebrewDate = HebrewCalendar.getForDate( cal );
    hebrewDate = hebrewDate.yesterday();
    Assertions.assertEquals( 11, hebrewDate.getHebrewMonth() );
    Assertions.assertEquals( 16, hebrewDate.getHebrewDayOfMonth() );

  }


  @Test
  public void testMultiDayForward() {

    HebrewDate date = HebrewCalendar.getForDate( 2012, Calendar.MAY, 1 );
    date = date.forward( 5 );
    Assertions.assertEquals( Calendar.MAY, date.getGregorianMonth() );
    Assertions.assertEquals( 6, date.getGregorianDayOfMonth() );

    date = HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 28 );
    date = date.forward( 4 );
    Assertions.assertEquals( Calendar.OCTOBER, date.getGregorianMonth() );
    Assertions.assertEquals( 2, date.getGregorianDayOfMonth() );

    date = HebrewCalendar.getForDate( 2015, Calendar.DECEMBER, 30 );
    date = date.forward( 2 );
    Assertions.assertEquals( 2016, date.getGregorianYear() );
    Assertions.assertEquals( Calendar.JANUARY, date.getGregorianMonth() );
    Assertions.assertEquals( 1, date.getGregorianDayOfMonth() );
  }

  @Test
  public void testMultiDayBackward() {

    HebrewDate date = HebrewCalendar.getForDate( 2012, Calendar.MAY, 1 );
    date = date.back( 5 );
    Assertions.assertEquals( Calendar.APRIL, date.getGregorianMonth() );
    Assertions.assertEquals( 26, date.getGregorianDayOfMonth() );

    date = HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 28 );
    date = date.back( 4 );
    Assertions.assertEquals( Calendar.SEPTEMBER, date.getGregorianMonth() );
    Assertions.assertEquals( 24, date.getGregorianDayOfMonth() );

    date = HebrewCalendar.getForDate( 2015, Calendar.JANUARY, 1 );
    date = date.back( 2 );
    Assertions.assertEquals( 2014, date.getGregorianYear() );
    Assertions.assertEquals( Calendar.DECEMBER, date.getGregorianMonth() );
    Assertions.assertEquals( 30, date.getGregorianDayOfMonth() );


  }

} // End of UT_GregorianDateNavigation class
