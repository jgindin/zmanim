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
public class Shkia
  extends Zmanim {

  public static final String                                ZMAN_WITH_ELEVATION = "Sunset (with elevation)";

  private static final String                               DEFAULT_ZMAN_NAME = "Sunset";



  Shkia(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.Shkia, zmanimCalculator );
  }


  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    List<Zman>  zmanim = new ArrayList<Zman>();

    zmanim.add(
      new Zman( DEFAULT_ZMAN_NAME, "The exact sunset time.", Zmanim.Type.Shkia, false ) {
        @Override protected Calendar calculate() {
          final Date sunset = zmanimCalculator.getSeaLevelSunset();
          return toCalendar( zmanimCalculator, sunset );
        }
      }
    );

    zmanim.add(
      new Zman( ZMAN_WITH_ELEVATION, "The exact sunset time, taking elevation into account.", Zmanim.Type.Shkia, false ) {
        @Override protected Calendar calculate() {
          final Date sunset = zmanimCalculator.getSunset();
          return toCalendar( zmanimCalculator, sunset );
        }
      }
    );

    return zmanim;
  }


  @Override
  public Zman getDefault() {
    return getByName( DEFAULT_ZMAN_NAME );
  }


}  // End of Shkia class
