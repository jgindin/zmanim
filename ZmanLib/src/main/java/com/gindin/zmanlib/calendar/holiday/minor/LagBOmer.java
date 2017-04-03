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
public class LagBOmer
  extends Observance {

  private static final String                               NAME = "Lag B'Omer";


  @Override
  public String getName() {
    return NAME;
  }


  @SuppressWarnings( "MagicNumber" )
  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( HebrewMonth.IYAR == date.getHebrewMonth() ) {
      int hebrewDayOfMonth = date.getHebrewDayOfMonth();
      if ( 17 == hebrewDayOfMonth ) {
        return new Details.Erev( date, NAME, false );
      }
      else if ( 18 == hebrewDayOfMonth ) {
        return new Details.Yom( date, NAME ) {
          @Override
          public boolean sayTachanun() {
            return false;
          }
        };
      }
    }

    return null;
  }

}  // End of LagBOmer class
