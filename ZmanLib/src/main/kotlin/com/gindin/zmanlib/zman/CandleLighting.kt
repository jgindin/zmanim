/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class CandleLighting
  extends Zmanim {

  private static final String                               DEFAULT_ZMAN_NAME = "18 Minutes";


  CandleLighting(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.CandleLighting, zmanimCalculator );
  }


  @Override
  public Zman getDefault() {
    return getByName( DEFAULT_ZMAN_NAME );
  }


  @SuppressWarnings( { "FeatureEnvy", "MagicNumber" } )
  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    List<Zman>  zmanim = new ArrayList<Zman>();

    zmanim.add(
      new Zman( DEFAULT_ZMAN_NAME, "Standard Candle lighting", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting() );
        }
      }
    );

    zmanim.add(
      new Zman( "15 Minutes", "London", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 15 ) );
        }
      }
    );

    zmanim.add(
      new Zman( "20 Minutes", "Raanana", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 20 ) );
        }
      }
    );

    zmanim.add(
      new Zman( "21 Minutes", "Tel Aviv", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 21 ) );
        }
      }
    );

    zmanim.add(
      new Zman( "22 Minutes", "Ashdod", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 22 ) );
        }
      }
    );

    zmanim.add(
      new Zman( "30 Minutes", "Tzefat, Teveria, Haifa", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 30 ) );
        }
      }
    );

    zmanim.add(
      new Zman( "40 Minutes", "Jerusalem, Petach Tikvah", Zmanim.Type.CandleLighting, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getCandleLighting( 40 ) );
        }
      }
    );


    return zmanim;
  }

}  // End of CandleLighting class
