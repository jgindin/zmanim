/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.Calendar;
import java.util.List;


/**
 * Used for allowing the user to see when a full-day fast ends.
 */
class FastEnds
  extends TzaitHacochavim {

  FastEnds(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.FastEnd, zmanimCalculator );
  }


  protected List<Zman> buildZmanimList(
    final ZmanimCalculator zmanimCalculator
  ) {

    List<Zman>  zmanim = buildStandardTzaitZmanim( Type.FastEnd, zmanimCalculator, true );

    zmanim.add(
      new Zman( "Seattle Sephardic", "Based on the Seattle Sephardic community's tradition.", Type.FastEnd ) {
        @Override protected Calendar calculate() {
          //noinspection MagicNumber
          return toCalendar( zmanimCalculator, zmanimCalculator.getEndOfFast( 30 ) );
        }
      }
    );

    return zmanim;
  }


}  // End of TzaitHacochavim class
