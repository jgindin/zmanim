/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.yomtov;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class RoshHashana
  extends Observance {

  private static final String ROSH_HASHANA = "Rosh Hashana";



  @Override
  public String getName() {
    return ROSH_HASHANA;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    final int hebrewMonth = date.getHebrewMonth();
    final int hebrewDayOfMonth = date.getHebrewDayOfMonth();

    if ( ( HebrewMonth.ELUL == hebrewMonth ) && ( 29 == hebrewDayOfMonth ) ) {
      return new Details.Erev( date, ROSH_HASHANA );
    }
    else if ( HebrewMonth.TISHREI == hebrewMonth ) {
      if ( 1 == hebrewDayOfMonth ) {
        return new RoshHashanaYomDetails( date, true );
      }
      else if ( 2 == hebrewDayOfMonth ) {
        return new RoshHashanaYomDetails( date, false );
      }
    }

    return null;
  }


  private static final class RoshHashanaYomDetails
    extends Details.YomTovDetails {

    private RoshHashanaYomDetails(
      HebrewDate date,
      boolean lightCandles
    ) {
      super( date, ROSH_HASHANA, lightCandles );
    }


    @Override
    public boolean isRoshHashana() {
      return true;
    }
  }

}  // End of RoshHashana class
