/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.minor;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
public class PesachSheni
  extends Observance {

  private static final String                               PESACH_SHENI = "Pesach Sheni";


  @Override
  public String getName() {
    return PESACH_SHENI;
  }


  @SuppressWarnings( "MagicNumber" )
  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
      int hebrewDayOfMonth = date.getHebrewDayOfMonth();

      if ( 13 == hebrewDayOfMonth ) {
        return new Details.Erev( date, PESACH_SHENI, false );
      }

      if ( 14 == hebrewDayOfMonth ) {
        return new Details.Yom( date, PESACH_SHENI ) {
          @Override
          public boolean isPesachSheni() {
            return true;
          }
        };
      }
    }

    return null;
  }

}  // End of PesachSheni class
