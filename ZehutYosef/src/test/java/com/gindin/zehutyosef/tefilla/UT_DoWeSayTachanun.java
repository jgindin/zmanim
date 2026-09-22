package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * Using page 43 of Zehut Yosef, there are no tachanun on the following days:
 *  <ul>
 *    <li>Shabbat
 *    <li>Rosh Chodesh
 *    <li>Moed (all the days of Pesach, Shavuot and Sukkot)
 *    <li>The whole month of Nissan
 *    <li>Yom Ha'atsmaut (5 Iyar)
 *    <li>Pesach Sheni (14 Iyar)
 *    <li>Lag LaOmer (18 Iyar)
 *    <li>Yom Yerushalayim (28 Iyar)
 *    <li>The first 12 days of Sivan
 *    <li>Tisha B'Av (9th)
 *    <li>Tu B'Av (15th)
 *    <li>Erev Rosh Hashana
 *    <li>From erev Yom Kippur until the end of Tishri
 *    <li>All of Chanukah
 *    <li>Tu B'Shvat (15)
 *    <li>14 & 15 of Adar Rishon
 *    <li>Purim
 *    <li>Shushan Purim
 *  </ul>
 *
 *  On the following days, tachanun is not said during mincha:
 *  <ul>
 *    <li>Erev Shabbat
 *    <li>Erev Rosh Chodesh
 *    <li>Erev Tisha B'Av
 *    <li>Erev Chanukah
 *    <li>Erev Purim
 *  </ul>
 */
@SuppressWarnings( "MagicNumber" )
public class UT_DoWeSayTachanun {

  @Test
  public void onShabbat() {

    final HebrewDate jan_6_2017 = HebrewCalendar.getForDate( 2017, Calendar.JANUARY, 6 );
    Assertions.assertTrue( new DoWeSay( jan_6_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_6_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_6_2017, TefillaType.Arvit ).tachanun() );

    final HebrewDate jan_7_2017 = HebrewCalendar.getForDate( 2017, Calendar.JANUARY, 7 );
    Assertions.assertFalse( new DoWeSay( jan_7_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_7_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_7_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void onRoshChodesh() {

    // This is also Shabbat...
    HebrewDate jan_28_2017 = HebrewCalendar.getForDate( 2017, Calendar.JANUARY, 28 );
    Assertions.assertFalse( new DoWeSay( jan_28_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_28_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jan_28_2017, TefillaType.Arvit ).tachanun() );

    // Erev Rosh Chodesh
    HebrewDate aug_22_2017 = HebrewCalendar.getForDate( 2017, Calendar.AUGUST, 21 );
    Assertions.assertTrue( new DoWeSay( aug_22_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_22_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_22_2017, TefillaType.Arvit ).tachanun() );

    // 30 Av
    HebrewDate aug_23_2017 = aug_22_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( aug_23_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_23_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_23_2017, TefillaType.Arvit ).tachanun() );

    // 1 Elul
    HebrewDate aug_24_2017 = aug_23_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( aug_24_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_24_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_24_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void onPurim() {

    HebrewDate march_12_2017 = HebrewCalendar.getForDate( 2017, Calendar.MARCH, 12 );
    Assertions.assertFalse( new DoWeSay( march_12_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( march_12_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( march_12_2017, TefillaType.Arvit ).tachanun() );

    // Shushan Purim
    HebrewDate march_13_2017 = march_12_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( march_13_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( march_13_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( march_13_2017, TefillaType.Arvit ).tachanun() );


    // Feb 27, 2018 is erev Ta'anit Esther, with Purim on March 1, and Shushan Purim on the 2nd.
    HebrewDate feb_27_2018 = HebrewCalendar.getForDate( 2018, Calendar.FEBRUARY, 27 );
    Assertions.assertTrue( new DoWeSay( feb_27_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertTrue( new DoWeSay( feb_27_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_27_2018, TefillaType.Arvit ).tachanun() );

    HebrewDate feb_28_2018 = feb_27_2018.tomorrow();
    Assertions.assertTrue( new DoWeSay( feb_28_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_28_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_28_2018, TefillaType.Arvit ).tachanun() );

    HebrewDate mar_1_2018 = feb_28_2018.tomorrow();
    Assertions.assertFalse( new DoWeSay( mar_1_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( mar_1_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( mar_1_2018, TefillaType.Arvit ).tachanun() );

    HebrewDate mar_2_2018 = mar_1_2018.tomorrow();
    Assertions.assertFalse( new DoWeSay( mar_2_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( mar_2_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( mar_2_2018, TefillaType.Arvit ).tachanun() );


    // Check the 14th & 15th of Adar I in a leap year.
    //  Feb 18 is Erev Purim Katan.
    HebrewDate feb_18_2019 = HebrewCalendar.getForDate( 2019, Calendar.FEBRUARY, 18 );
    Assertions.assertTrue( new DoWeSay( feb_18_2019, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_18_2019, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_18_2019, TefillaType.Arvit ).tachanun() );

    HebrewDate feb_19_2019 = feb_18_2019.tomorrow();
    Assertions.assertFalse( new DoWeSay( feb_19_2019, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_19_2019, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_19_2019, TefillaType.Arvit ).tachanun() );

    HebrewDate feb_20_2019 = feb_18_2019.tomorrow();
    Assertions.assertFalse( new DoWeSay( feb_20_2019, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_20_2019, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( feb_20_2019, TefillaType.Arvit ).tachanun() );

  }


  @Test
  public void allOfNissan() {

    HebrewDate date = HebrewCalendar.getForJewishDate( 5777, HebrewMonth.ADAR, 29 );
    Assertions.assertTrue( new DoWeSay( date, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( date, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun() );

    while ( date.getHebrewDayOfMonth() < 30 ) {
      date = date.tomorrow();
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Shachrit ).tachanun(), date.toString() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Mincha ).tachanun(), date.toString() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun(), date.toString() );
    }
  }


  @Test
  public void yomHaZikaron_yomHaAtzmaut() {

    HebrewDate apr_30_2017 = HebrewCalendar.getForDate( 2017, Calendar.APRIL, 30 );
    Assertions.assertTrue( new DoWeSay( apr_30_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( apr_30_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( apr_30_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate may_1_2017 = apr_30_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( may_1_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_1_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_1_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate may_2_2017 = may_1_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( may_2_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_2_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_2_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void pesachSheni() {

    HebrewDate may_9_2017 = HebrewCalendar.getForDate( 2017, Calendar.MAY, 9 );
    Assertions.assertTrue( new DoWeSay( may_9_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_9_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_9_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate may_10_2017 = may_9_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( may_10_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_10_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_10_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void lagLaOmer() {

    HebrewDate may_14_2017 = HebrewCalendar.getForDate( 2017, Calendar.MAY, 14 );
    Assertions.assertFalse( new DoWeSay( may_14_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_14_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_14_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void yomYerushalyim() {

    HebrewDate may_23_2017 = HebrewCalendar.getForDate( 2017, Calendar.MAY, 23 );
    Assertions.assertTrue( new DoWeSay( may_23_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_23_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_23_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate may_24_2017 = may_23_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( may_24_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_24_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_24_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void first12DaysOfSivan() {

    HebrewDate date = HebrewCalendar.getForJewishDate( 5777, HebrewMonth.IYAR, 29 );
    Assertions.assertTrue( new DoWeSay( date, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( date, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun() );

    do {
      date = date.tomorrow();
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Shachrit ).tachanun(), date.toString() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Mincha ).tachanun(), date.toString() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun(), date.toString() );
    }
    while ( date.getHebrewDayOfMonth() < 12 );

    date = date.tomorrow();
    Assertions.assertTrue( new DoWeSay( date, TefillaType.Shachrit ).tachanun() );
    Assertions.assertTrue( new DoWeSay( date, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void onShavuot() {

    HebrewDate may_30_2017 = HebrewCalendar.getForDate( 2017, Calendar.MAY, 30);
    Assertions.assertFalse( new DoWeSay( may_30_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_30_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_30_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate may_31_2017 = may_30_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( may_31_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_31_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( may_31_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate june_1_2017 = may_31_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( june_1_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( june_1_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( june_1_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void tishaBAv() {

    HebrewDate jul_31_2017 = HebrewCalendar.getForDate( 2017, Calendar.JULY, 31 );
    Assertions.assertTrue( new DoWeSay( jul_31_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jul_31_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( jul_31_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate aug_1_2017 = jul_31_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( aug_1_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_1_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_1_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void tuBAv() {

    HebrewDate aug_6_2017 = HebrewCalendar.getForDate( 2017, Calendar.AUGUST, 6 );
    Assertions.assertTrue( new DoWeSay( aug_6_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_6_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_6_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate aug_7_2017 = aug_6_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( aug_7_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_7_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( aug_7_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void onRoshHashana() {

    // The 28th of Elul...still have tachanun
    HebrewDate sept_19_2017 = HebrewCalendar.getForDate( 2017, Calendar.SEPTEMBER, 19 );
    Assertions.assertTrue( new DoWeSay( sept_19_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertTrue( new DoWeSay( sept_19_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_19_2017, TefillaType.Arvit ).tachanun() );

    // The 29th, however, we don't...
    HebrewDate sept_20_2017 = sept_19_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( sept_20_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_20_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_20_2017, TefillaType.Arvit ).tachanun() );

    // ..of course not on Rosh Hashana...
    HebrewDate sept_21_2017 = sept_20_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( sept_21_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_21_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_21_2017, TefillaType.Arvit ).tachanun() );

    HebrewDate sept_22_2017 = sept_21_2017.tomorrow();
    Assertions.assertFalse( new DoWeSay( sept_22_2017, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_22_2017, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_22_2017, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void onYomKippur() {

    HebrewDate sept_17_2018 = HebrewCalendar.getForDate( 2018, Calendar.SEPTEMBER, 17 );
    Assertions.assertTrue( new DoWeSay( sept_17_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertTrue( new DoWeSay( sept_17_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_17_2018, TefillaType.Arvit ).tachanun() );

    // Erev Yom Kippur -- no tachanun
    HebrewDate sept_18_2018 = sept_17_2018.tomorrow();
    Assertions.assertFalse( new DoWeSay( sept_18_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_18_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_18_2018, TefillaType.Arvit ).tachanun() );

    // And not on Kippur!
    HebrewDate sept_19_2018 = sept_18_2018.tomorrow();
    Assertions.assertFalse( new DoWeSay( sept_19_2018, TefillaType.Shachrit ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_19_2018, TefillaType.Mincha ).tachanun() );
    Assertions.assertFalse( new DoWeSay( sept_19_2018, TefillaType.Arvit ).tachanun() );
  }


  @Test
  public void theRestOfTishri() {

    HebrewDate date = HebrewCalendar.getForDate( 2018, Calendar.SEPTEMBER, 19 );
    while ( date.getHebrewMonth() == HebrewMonth.TISHREI ) {

      Assertions.assertFalse( new DoWeSay( date, TefillaType.Shachrit ).tachanun() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Mincha ).tachanun() );
      Assertions.assertFalse( new DoWeSay( date, TefillaType.Arvit ).tachanun() );

      date = date.tomorrow();
    }
  }


} // End of UT_DoWeSayTachanun class
