package com.gindin.zmanlib.calendar.holiday.other;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 * We don't say tachanun on the 1st thru 12th of Sivan.
 */
public class Sivan
  extends Observance {

  @Override
  public String getName() {
    return "First 12 days of Sivan";
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    int hebrewMonth = date.getHebrewMonth();
    if ( HebrewMonth.SIVAN == hebrewMonth ) {

      int hebrewDayOfMonth = date.getHebrewDayOfMonth();
      if ( hebrewDayOfMonth <= 12 ) {
        return new SivanDetails( date );
      }
    }

    return null;
  }



  private static final class SivanDetails
    extends Details.Yom {

    SivanDetails(
      HebrewDate  date
    ) {
      // There is no real description for this day.
      super( date, "" );
    }


    @Override
    public boolean sayTachanun() {
      return false;
    }
  }


} // End of Sivan class
