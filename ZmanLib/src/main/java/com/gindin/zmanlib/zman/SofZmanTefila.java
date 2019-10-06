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
class SofZmanTefila
  extends Zmanim {

  private static final String                               DEFAULT_ZMAN_NAME = "GR'A";
  

  SofZmanTefila(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.SofZmanTefila, zmanimCalculator );
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
      new Zman( "2 Hours before Chatzot", "2 hours before chatzot.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfila2HoursBeforeChatzos() );
        }
      }
    );

//    sofZmanTefila.addZman( zmanimCalculator.getSofZmanTfilaFixedLocal(), "", "" );

    zmanim.add(
      new Zman( DEFAULT_ZMAN_NAME, "Based on the day as calculated from sunrise to sunset.", Zmanim.Type.SofZmanTefila, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaGRA() );
        }
      }
    );

    zmanim.add(
      new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting at 1/10th of the day before sunrise and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilahAteretTorah() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A", "Based on alot being 72 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 120 Minutes", "Based on alot being 120 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA120Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 16.1 deg", "Based on alot being 16.1 degrees before sunrise.", Zmanim.Type.SofZmanTefila, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA16Point1Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 19.8 deg", "Based on alot being 19.8 degrees before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA19Point8Degrees() );
        }
      }
    );

//    sofZmanTefila.addZman( zmanimCalculator.getSofZmanTfilaMGA72Minutes(), "", "" );

    zmanim.add(
      new Zman( "MG'A 72 Minutes Zmaniyot", "Based on alot being 72 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA72MinutesZmanis() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 90 Minutes", "Based on alot being 90 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA90Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 90 Minutes Zmaniyot", "Based on alot being 90 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA90MinutesZmanis() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 96 Minutes", "Based on alot being 96 minutes before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA96Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 96 Minutes Zmaniyot", "Based on alot being 96 minutes zmaniyot before sunrise.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaMGA96MinutesZmanis() );
        }
      }
    );

    zmanim.add(
      new Zman( "Baal HaTanya", "Based on Baal HaTanya's sunrise and sunset calculations.", Zmanim.Type.SofZmanTefila ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getSofZmanTfilaBaalHatanya() );
        }
      }
    );

    return zmanim;
  }

}  // End of SofZmanTefila class
