/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.modern.YomHaShoah;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * Yom HaShoah is the 27th of Nissan, except that it is observed on the 26th when the 27th is a Friday and on the 28th
 * when the 27th is a Sunday. The dates used below were cross-checked against the Hebcal holiday calendar
 * (www.hebcal.com), which lists the observed date for each year.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_YomHaShoah {

  private static final YomHaShoah                           YOM_HA_SHOAH = new YomHaShoah();

  private static final String                               YOM_NAME = "Yom HaShoah";

  private static final String                               EREV_NAME = "Erev Yom HaShoah";


  @Test
  public void friday27thMovesBackToThursday26th2025() {
    // 27 Nissan 5785 is Friday April 25, 2025; Hebcal lists Yom HaShoah on Thursday April 24, 2025.
    HebrewDate thursday = HebrewCalendar.getForDate( 2025, Calendar.APRIL, 24 );
    HebrewDate friday = thursday.tomorrow();

    assertNissan( thursday, 26, Calendar.THURSDAY );
    assertNissan( friday, 27, Calendar.FRIDAY );
    assertEquals( true, isYomHaShoah( thursday ) );
    assertEquals( false, isYomHaShoah( friday ) );
    assertEquals( true, isErevYomHaShoah( thursday.yesterday() ) );
    assertEquals( false, isErevYomHaShoah( thursday ) );
  }


  @Test
  public void friday27thMovesBackToThursday26th2008() {
    // 27 Nissan 5768 is Friday May 2, 2008; Hebcal lists Yom HaShoah on Thursday May 1, 2008.
    HebrewDate thursday = HebrewCalendar.getForDate( 2008, Calendar.MAY, 1 );
    HebrewDate friday = thursday.tomorrow();

    assertNissan( thursday, 26, Calendar.THURSDAY );
    assertNissan( friday, 27, Calendar.FRIDAY );
    assertEquals( true, isYomHaShoah( thursday ) );
    assertEquals( false, isYomHaShoah( friday ) );
  }


  @Test
  public void friday27thMovesBackToThursday26th2021() {
    // 27 Nissan 5781 is Friday April 9, 2021; Hebcal lists Yom HaShoah on Thursday April 8, 2021.
    HebrewDate thursday = HebrewCalendar.getForDate( 2021, Calendar.APRIL, 8 );
    HebrewDate friday = thursday.tomorrow();

    assertNissan( thursday, 26, Calendar.THURSDAY );
    assertNissan( friday, 27, Calendar.FRIDAY );
    assertEquals( true, isYomHaShoah( thursday ) );
    assertEquals( false, isYomHaShoah( friday ) );
  }


  @Test
  public void sunday27thMovesForwardToMonday28th() {
    // 27 Nissan 5788 is Sunday April 23, 2028; Hebcal lists Yom HaShoah on Monday April 24, 2028.
    HebrewDate sunday = HebrewCalendar.getForDate( 2028, Calendar.APRIL, 23 );
    HebrewDate monday = sunday.tomorrow();

    assertNissan( sunday, 27, Calendar.SUNDAY );
    assertNissan( monday, 28, Calendar.MONDAY );
    assertEquals( false, isYomHaShoah( sunday ) );
    assertEquals( true, isErevYomHaShoah( sunday ) );
    assertEquals( true, isYomHaShoah( monday ) );
  }


  @Test
  public void regularWeekdayStaysOn27th() {
    // 27 Nissan 5786 is Tuesday April 14, 2026, and 27 Nissan 5787 is Tuesday May 4, 2027; Hebcal agrees.
    HebrewDate tuesday = HebrewCalendar.getForDate( 2026, Calendar.APRIL, 14 );
    assertNissan( tuesday, 27, Calendar.TUESDAY );
    assertEquals( true, isYomHaShoah( tuesday ) );
    assertEquals( false, isYomHaShoah( tuesday.yesterday() ) );
    assertEquals( false, isYomHaShoah( tuesday.tomorrow() ) );
    assertEquals( true, isErevYomHaShoah( tuesday.yesterday() ) );

    tuesday = HebrewCalendar.getForDate( 2027, Calendar.MAY, 4 );
    assertNissan( tuesday, 27, Calendar.TUESDAY );
    assertEquals( true, isYomHaShoah( tuesday ) );
  }


  @Test
  public void exactlyOneObservancePerYear() {
    for ( int year = 5700; year <= 6000; year++ ) {
      int observances = 0;

      for ( int day = 1; day <= 30; day++ ) {
        HebrewDate date = HebrewCalendar.getForJewishDate( year, HebrewMonth.NISSAN, day );

        if ( isYomHaShoah( date ) ) {
          observances++;

          // Never before the 26th or after the 28th, and never on Friday, Shabbat or Sunday.
          Assertions.assertTrue( day >= 26 && day <= 28, "Year " + year + " observed on Nissan " + day );
          Assertions.assertNotEquals( Calendar.FRIDAY, date.getDayOfWeek(), "Year " + year );
          Assertions.assertNotEquals( Calendar.SATURDAY, date.getDayOfWeek(), "Year " + year );
          Assertions.assertNotEquals( Calendar.SUNDAY, date.getDayOfWeek(), "Year " + year );

          // The 27th is used unless it is a Friday (go back to the 26th) or a Sunday (go forward to the 28th).
          int dayOfWeekOf27th = HebrewCalendar.getForJewishDate( year, HebrewMonth.NISSAN, 27 ).getDayOfWeek();
          int expectedDay = 27;
          if ( Calendar.FRIDAY == dayOfWeekOf27th ) {
            expectedDay = 26;
          } else if ( Calendar.SUNDAY == dayOfWeekOf27th ) {
            expectedDay = 28;
          }
          Assertions.assertEquals( expectedDay, day, "Year " + year );
        }
      }

      Assertions.assertEquals( 1, observances, "Year " + year );
    }
  }


  private static boolean isYomHaShoah( HebrewDate date ) {
    Details details = date.getDetails();
    String description = details.getDescription();

    // The full calendar and the observance itself must agree.
    Details observance = YOM_HA_SHOAH.getDetailsFor( date );
    boolean observanceSaysYom = null != observance && !observance.isErev();
    boolean calendarSaysYom = description.contains( YOM_NAME ) && !description.contains( EREV_NAME );
    Assertions.assertEquals( observanceSaysYom, calendarSaysYom, date.toString() );

    return calendarSaysYom;
  }


  private static boolean isErevYomHaShoah( HebrewDate date ) {
    return date.getDetails().getDescription().contains( EREV_NAME );
  }


  private static void assertEquals( boolean expected, boolean actual ) {
    Assertions.assertEquals( expected, actual );
  }


  private static void assertNissan( HebrewDate date, int dayOfMonth, int dayOfWeek ) {
    Assertions.assertEquals( HebrewMonth.NISSAN, date.getHebrewMonth() );
    Assertions.assertEquals( dayOfMonth, date.getHebrewDayOfMonth() );
    Assertions.assertEquals( dayOfWeek, date.getDayOfWeek() );
  }

}  // End of UT_YomHaShoah class
