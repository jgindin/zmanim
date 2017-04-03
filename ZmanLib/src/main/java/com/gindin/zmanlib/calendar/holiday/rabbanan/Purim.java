/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.rabbanan;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( { "MagicNumber", "FeatureEnvy" } )
public class Purim
  extends Observance {


  private static final String                               PURIM = "Purim";


  @Override
  public String getName() {
    return PURIM;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR_II == date.getHebrewMonth() ) ) ||
         ( !date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) ) {

      if ( 13 == date.getHebrewDayOfMonth() ) {
        return new Details.Erev( date, PURIM, false );
      }
      if ( 14 == date.getHebrewDayOfMonth() ) {
        return new PurimDetails( date );
      }
    }

    return null;
  }


  private static class PurimDetails
    extends Details.Yom {

    public PurimDetails(
      final HebrewDate date
    ) {
      super( date, PURIM );
    }


    @Override
    public boolean isPurim() {
      return true;
    }


    @Override
    public boolean sayTachanun() {
      return false;
    }
  }

}  // End of Purim class
