/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
class Shkia
  extends Zmanim {

  Shkia(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.Shkia, zmanimCalculator );
  }


  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    List<Zman>  zmanim = new ArrayList<Zman>();

    zmanim.add(
      new Zman( "Sunset", "The exact sunset time.", Zmanim.Type.Shkia, false ) {
        @Override protected Calendar calculate() {
          final Date sunset = zmanimCalculator.getSunset();
          return toCalendar( zmanimCalculator, sunset );
        }
      }
    );

    return zmanim;
  }


}  // End of Shkia class
