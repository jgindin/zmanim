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
public class PurimKatan
  extends Observance {

  private static final String                               PURIM_KATAN = "Purim Katan";


  @Override
  public String getName() {
    return PURIM_KATAN;
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    if ( date.isHebrewLeapYear() && ( HebrewMonth.ADAR == date.getHebrewMonth() ) ) {

      final int hebrewDayOfMonth = date.getHebrewDayOfMonth();
      if ( 13 == hebrewDayOfMonth ) {
        return new Details.Erev( date, PURIM_KATAN, false );
      }
      if ( ( 14 == hebrewDayOfMonth ) || ( 15 == hebrewDayOfMonth ) ) {
        return new PurimKatanDetails( date );
      }
    }

    return null;
  }


  private static class PurimKatanDetails
    extends Details.Yom {

    public PurimKatanDetails(
      final HebrewDate date
    ) {
      super( date, PURIM_KATAN );
    }


    @Override
    public boolean sayTachanun() {
      return false;
    }
  }

}  // End of Purim class
