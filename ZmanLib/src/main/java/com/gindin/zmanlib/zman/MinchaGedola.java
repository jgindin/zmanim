/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class MinchaGedola
  extends Zmanim {

  private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


  MinchaGedola(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.MinchaGedola, zmanimCalculator );
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
      new Zman( DEFAULT_ZMAN_NAME, "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.MinchaGedola, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedola() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 16.1 deg", "Magen Avraham, based on the day starting and ending 16.1 degrees below the horizon.", Zmanim.Type.MinchaGedola, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedola16Point1Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "30 Minutes", "Calculated as 30 minutes after chatzot (and not 1/2 of a shaah zmanit after chatzot).", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedola30Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "Lechumra 30 Minutes", "Calculated as 30 minutes after chatzot (and not 1/2 of a shaah zmanit after chatzot), except in the winter when 1/2 shaah zmanit is less than 30 minutes.", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedolaGreaterThan30() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 72 Minutes", "Magen Avraham, based onthe day starting 72 minutes before sunrise and ending 72 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedola72Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedolaAteretTorah() );
        }
      }
    );

    zmanim.add(
      new Zman( "Baal HaTanya", "Based on Baal HaTanya's sunrise and sunset calculations.", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedolaBaalHatanya() );
        }
      }
    );

    zmanim.add(
      new Zman( "Baal HaTanya Lechumra 30 Minutes", "Based on Baal HaTanya's sunrise and sunset calculations, except in the winter when 1/2 shaah zmanit is less than 30 minutes.", Zmanim.Type.MinchaGedola ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaGedolaBaalHatanyaGreaterThan30() );
        }
      }
    );

    return zmanim;
  }

}  // End of MinchaGedola class
