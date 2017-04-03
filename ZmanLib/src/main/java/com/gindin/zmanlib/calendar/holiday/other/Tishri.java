package com.gindin.zmanlib.calendar.holiday.other;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 * We don't say tachanun from Erev Yom Kippur until the end of the month.
 */
public class Tishri
  extends Observance {

  @Override
  public String getName() {
    return "Tishri";
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    int hebrewMonth = date.getHebrewMonth();
    if ( HebrewMonth.TISHREI == hebrewMonth ) {

      int hebrewDayOfMonth = date.getHebrewDayOfMonth();
      if ( hebrewDayOfMonth >= 11 ) {
        return new TishriDetails( date );
      }
    }

    return null;
  }



  private static final class TishriDetails
    extends Details.Yom {

    TishriDetails(
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
