package com.gindin.zehutyosef.tefilla;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_DoWeSay {


  @Test
  public void may_30_2015() {

    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2015, Calendar.MAY, 30 ), TefillaType.Arvit );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void sept_16_2015() {

    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 16 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertTrue( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertTrue( doWeSay.taanitDayInsertions() );
    Assert.assertTrue( doWeSay.tachanun() );
  }


  @Test
  public void sept_11_2015() {

    // Friday in late Elul
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2015, Calendar.SEPTEMBER, 11 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertTrue( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void sept_9_2018() {

    // Erev Rosh Hashana
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2018, Calendar.SEPTEMBER, 9 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void sept_18_2018() {

    // Erev Yom Kippur
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2018, Calendar.SEPTEMBER, 18 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertTrue( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void dec_3_2018() {

    // 1st day of Chanukah
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2018, Calendar.DECEMBER, 3 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertTrue( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void dec_9_2018() {

    // 8th day of Chanukah and Rosh Chodesh
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2018, Calendar.DECEMBER, 9 ), TefillaType.Mincha );
    Assert.assertFalse( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertTrue( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertTrue( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void feb_18_2019() {

    // Erev Purim katan
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2019, Calendar.FEBRUARY, 18 ), TefillaType.Mincha );
    Assert.assertFalse( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void feb_19_2019() {

    // Purim Katan
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2019, Calendar.FEBRUARY, 19 ), TefillaType.Mincha );
    Assert.assertFalse( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void mar_20_2019() {

    // Erev Purim
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2019, Calendar.MARCH, 20 ), TefillaType.Mincha );
    Assert.assertFalse( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertTrue( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void oct_25_2016() {

    // Simchat Torah
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2016, Calendar.OCTOBER, 25 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void nov_30_2016() {

    // Just some date that's making sure we're in the new Hebrew year before the new secular year.
    //  It happens to be erev Rosh Chodesh, though, so no Tachanun!
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2016, Calendar.NOVEMBER, 30 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );
  }


  @Test
  public void dec_14_2016() {

    // Just some date that's making sure we're in the new Hebrew year before the new secular year.
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2016, Calendar.DECEMBER, 14 ), TefillaType.Mincha );
    Assert.assertFalse( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertFalse( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertTrue( doWeSay.tachanun() );
  }

  @Test
  public void aug_19_2016() {

    // Erev Tu B'Av
    DoWeSay doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2016, Calendar.AUGUST, 18 ), TefillaType.Mincha );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertFalse( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );

    // Tu B'Av
    doWeSay = new DoWeSay( HebrewCalendar.getForDate( 2016, Calendar.AUGUST, 19 ), TefillaType.Shachrit );
    Assert.assertTrue( doWeSay.barechenu() );
    Assert.assertFalse( doWeSay.aseretYameiTeshuvaInsertions() );
    Assert.assertFalse( doWeSay.chanukahAdditions() );
    Assert.assertTrue( doWeSay.erevShabbatModifications() );
    Assert.assertFalse( doWeSay.purimAdditions() );
    Assert.assertTrue( doWeSay.moridHaTal() );
    Assert.assertFalse( doWeSay.roshChodeshAdditions() );
    Assert.assertFalse( doWeSay.taanitDayInsertions() );
    Assert.assertFalse( doWeSay.tachanun() );

  }

} // End of UT_DoWeSay class
