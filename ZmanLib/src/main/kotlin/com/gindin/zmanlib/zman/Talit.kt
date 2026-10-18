/*
 * Copyright (c) 2020. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class Talit
  extends Zmanim {

  private static final String                               DEFAULT_ZMAN_NAME = "10.2 deg";


  Talit(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.Talit, zmanimCalculator );
  }


  @Override
  public Zman getDefault() {
    return getByName( DEFAULT_ZMAN_NAME );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    List<Zman>  zmanim = new ArrayList<Zman>();

    zmanim.add(
      new Zman( "7.65 deg", "Based on the position of the sun when it is 7.65 degrees below geometric zenith.", Zmanim.Type.Talit ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMisheyakir7Point65Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "9.5 deg", "Based on the position of the sun when it is 9.5 degrees below geometric zenith.", Zmanim.Type.Talit ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMisheyakir9Point5Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( DEFAULT_ZMAN_NAME, "Based on the position of the sun when it is 10.2 degrees below geometric zenith.", Zmanim.Type.Talit, false )  {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMisheyakir10Point2Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "11 deg", "Based on the position of the sun when it is 11 degrees below geometric zenith.", Zmanim.Type.Talit, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMisheyakir11Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "11.5 deg", "Based on the position of the sun when it is 11.5 degrees below geometric zenith.", Zmanim.Type.Talit ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMisheyakir11Point5Degrees() );
        }
      }
    );

    return zmanim;
  }

}  // End of Talit class
