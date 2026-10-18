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
@SuppressWarnings( "MagicNumber" )
public class TaanitBechorot
  extends Taanit {

  private static final String                               BECHOROT = TAANIT + "Bechorot";



  public TaanitBechorot() {
    super( HebrewMonth.NISSAN, 14 );
  }


  @Override
  public String getName() {
    return BECHOROT;
  }


  @Override
  protected boolean canFallOnFriday( HebrewDate date ) {

    // This one is complicated. In a year when Pesach starts Friday night, then this fast WILL
    //  be observed on Friday (the 14th of Nisan). However, if Pesach starts Saturday night, then
    //  it gets pulled all the way back to Thursday (the 12th of Nisan).
    //
    // We return true here ONLY if today IS the 14th, and handle the exceptions in the
    //  checkWednesday & checkThursday methods.
    //
    // NOTE: This means that if this method is ever called to check a date which is NOT ITSELF
    //  the normal date of the fast, we'll return false. What this really means is that if the
    //  base class' checkWednesday or checkThursday were ever to be called, they would NOT work
    //  correctly.
    return date.getHebrewDayOfMonth() == 14;
  }


  @Override
  protected Details checkWednesday( HebrewDate date ) {

    if ( date.getHebrewDayOfMonth() == 11 ) {
      return createErevDetails( date );
    }

    // DO NOT CALL SUPER. See note in canFallOnFriday.
    return null;
  }


  @Override
  protected Details checkThursday( HebrewDate date ) {

    if ( date.getHebrewDayOfMonth() == 12 ) {
      return createYomDetails( date );
    }

    if ( date.getHebrewDayOfMonth() == 13 ) {
      return createErevDetails( date );
    }

    // DO NOT CALL SUPER. See note in canFallOnFriday.
    return null;
  }


  @Override
  protected Details createErevDetails( HebrewDate date ) {
    return new ErevTaanitDetails( date, BECHOROT );
  }


  @Override
  protected Details createYomDetails( HebrewDate date ) {
    return new TaanitDetails( date, BECHOROT );
  }


  @Override
  protected boolean canPushToSunday() {
    return false;
  }


}  // End of TaanitBechorot class
