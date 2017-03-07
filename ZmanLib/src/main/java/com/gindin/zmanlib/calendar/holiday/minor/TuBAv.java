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
public class TuBAv
  extends Observance {

  private static final String                               TU_BAV = "Tu B'Av";

  @Override
  public String getName() {
    return TU_BAV;
  }


  @SuppressWarnings( "MagicNumber" )
  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( date.getHebrewMonth() == HebrewMonth.AV ) {
      int hebrewDayOfMonth = date.getHebrewDayOfMonth();
      if ( 14 == hebrewDayOfMonth ) {
        return new Details.Erev( date, TU_BAV, false );
      }

      if ( 15 == hebrewDayOfMonth ) {
        return new TuBAvDetails( date );
      }
    }

    return null;
  }


  private static class TuBAvDetails
    extends Details.Yom {

    public TuBAvDetails(
      final HebrewDate date
    ) {
      super( date, TU_BAV );
    }


    @Override
    public boolean isTuBAv() {
      return true;
    }
  }


}  // End of TuBAv class
