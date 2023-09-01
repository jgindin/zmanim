/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
class MinchaKetana
  extends Zmanim {

  private static final String                               DEFAULT_ZMAN_NAME = "GR'A";


  MinchaKetana(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.MinchaKetana, zmanimCalculator );
  }


  @Override
  public Zman getDefault() {
    return getByName( DEFAULT_ZMAN_NAME );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    List<Zman>  zmanim = new ArrayList<>();

    zmanim.add(
      new Zman( DEFAULT_ZMAN_NAME, "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.", Zmanim.Type.MinchaKetana, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetana() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 16.1 deg", "Based on the day starting and ending 16.1 degrees below the horizon.", Zmanim.Type.MinchaKetana, false ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetana16Point1Degrees() );
        }
      }
    );

    zmanim.add(
      new Zman( "MG'A 72 Minutes", "Based on the day starting 72 minutes before sunrise and ending 72 minutes after sunset.", Zmanim.Type.MinchaKetana ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetana72Minutes() );
        }
      }
    );

    zmanim.add(
      new Zman( "Chacham Yosef Harari-Raful", "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.", Zmanim.Type.MinchaKetana ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetanaAteretTorah() );
        }
      }
    );

    zmanim.add(
      new Zman( "Baal HaTanya", "Based on Baal HaTanya's sunrise and sunset calculations.", Zmanim.Type.MinchaKetana ) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetanaBaalHatanya() );
        }
      }
    );

    zmanim.add(
      new Zman("Rabbi Yaakov Moshe Hillel", "Calculated as 2.5 shaot zmaniyot before tzait 3.8 deg with shaot zmaniyot "
          + "calculated based on a day starting at alot 16.1 deg and ending at tzait 3.8 deg.", Type.MinchaKetana) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetanaAhavatShalom());
        }
      }
    );

    zmanim.add(
      new Zman(" Rav Moshe Feinstein - GR'A half day", "Calculated according to the GR'A as 3.5 shaot zmaniyot (solar hours) after fixed local chatzot.", Type.SofZmanShema) {
        @Override protected Calendar calculate() {
          return toCalendar( zmanimCalculator, zmanimCalculator.getMinchaKetanaGRAFixedLocalChatzosToSunset());
        }
      }
    );


    return zmanim;
  }
}  // End of MinchaKetana class
