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
public class TzomGedaliah
  extends Taanit {

  private static final String                               TZOM_GEDALIAH = "Tzom Gedaliah";



  public TzomGedaliah() {
    super( HebrewMonth.TISHREI, 3 );
  }


  @Override
  public String getName() {
    return TZOM_GEDALIAH;
  }


  @Override
  protected boolean canFallOnFriday( HebrewDate date ) {
    return false;
  }


  @Override
  protected Details createErevDetails( HebrewDate date ) {
    return new ErevTaanitDetails( date, TZOM_GEDALIAH );
  }


  @Override
  protected Details createYomDetails( HebrewDate date ) {
    return new TaanitDetails( date, TZOM_GEDALIAH );
  }


}  // End of TzomGedaliah class
