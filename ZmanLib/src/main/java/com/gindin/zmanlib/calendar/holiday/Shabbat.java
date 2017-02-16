/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.Calendar;

/**
 * Represents Shabbat.
 */
public class Shabbat
  extends Observance {


  private static final String                               SHABBAT = "Shabbat";


  @Override
  public String getName() {
    return SHABBAT;
  }


  @Override
  public Details getDetailsFor( final HebrewDate date ) {

    if ( Calendar.FRIDAY == date.getDayOfWeek() ) {
      return new ErevShabbatDetails( date );
    }

    if ( Calendar.SATURDAY == date.getDayOfWeek() ) {
      return new ShabbatDetails( date );
    }

    return null;
  }


  private static class ShabbatDetails
    extends Details.Yom {

    private ShabbatDetails( HebrewDate date ) {
      super( date, SHABBAT );
    }


    @Override public boolean lightCandles() {
      return false;
    }

    @Override public boolean sayHavdalah() {
      return true;
    }
  }


  private static class ErevShabbatDetails
    extends Details.Erev {

    private ErevShabbatDetails(
      HebrewDate date
    ) {
      super( date, SHABBAT, true );
    }


    @Override
    public String getDescription() {
      // No one wants to see "Erev Shabbat" for every Friday...
      return "";
    }
  }

}  // End of Shabbat class
