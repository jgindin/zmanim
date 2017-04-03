package com.gindin.zmanlib.calendar.holiday.other;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;
import com.gindin.zmanlib.calendar.holiday.Observance;

/**
 * We don't say tachanun for the entire month of Nissan.
 */
public class Nissan
  extends Observance {

  @Override
  public String getName() {
    return "Nissan";
  }


  @Override
  public Details getDetailsFor( HebrewDate date ) {

    int hebrewMonth = date.getHebrewMonth();
    if ( HebrewMonth.NISSAN == hebrewMonth ) {

        return new NissanDetails( date );
    }

    return null;
  }



  private static final class NissanDetails
    extends Details.Yom {

    NissanDetails(
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
