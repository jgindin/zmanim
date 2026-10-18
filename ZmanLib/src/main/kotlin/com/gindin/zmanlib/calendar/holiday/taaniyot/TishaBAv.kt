/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday.taaniyot;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.HebrewMonth;
import com.gindin.zmanlib.calendar.holiday.Details;

/**
 *
 */
public class TishaBAv
  extends Taanit {

  private static final String                               TISHA_B_AV = "Tisha B'Av";


  public TishaBAv() {
    super( HebrewMonth.AV, 9 );
  }


  @Override
  public String getName() {
    return TISHA_B_AV;
  }


  @Override
  protected boolean canFallOnFriday( HebrewDate date ) {
    return false;
  }


  @Override
  protected Details createErevDetails( HebrewDate date ) {
    return new ErevTaanitDetails( date, TISHA_B_AV );
  }


  @Override
  protected Details createYomDetails( HebrewDate date ) {
    return new TaanitDetails( date, TISHA_B_AV ) {
      @Override
      public boolean sayTachanun() {
        return false;
      }
    };
  }


}  // End of TishaBAv class
