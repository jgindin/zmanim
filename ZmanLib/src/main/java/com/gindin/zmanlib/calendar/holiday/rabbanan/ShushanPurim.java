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
public class ShushanPurim
  extends Observance {


  private static final String                               SHUSHAN_PURIM = "Shushan Purim";


  @Override
  public String getName() {
    return SHUSHAN_PURIM;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR_II == date.getHebrewMonth() ) ) ||
         ( !date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) ) {

      if ( 15 == date.getHebrewDayOfMonth() ) {
        return new Details.Yom( date, SHUSHAN_PURIM ) {
          @Override
          public boolean isShushanPurim() {
            return true;
          }
        };
      }
    }

    return null;
  }

}  // End of ShushanPurim class
