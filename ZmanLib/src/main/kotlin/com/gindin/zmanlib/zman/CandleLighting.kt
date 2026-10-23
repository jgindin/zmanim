/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "18 Minutes"

private const val LONDON_MINUTES_BEFORE_SHKIA = 15
private const val RAANANA_MINUTES_BEFORE_SHKIA = 20
private const val TEL_AVIV_MINUTES_BEFORE_SHKIA = 21
private const val ASHDOD_MINUTES_BEFORE_SHKIA = 22
private const val NORTHERN_ISRAEL_MINUTES_BEFORE_SHKIA = 30
private const val JERUSALEM_MINUTES_BEFORE_SHKIA = 40

internal class CandleLighting(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.CandleLighting, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  // A flat table of independent zman definitions, one per opinion, with no control flow: splitting it would only
  //  scatter the table, so the length rule does not apply.
  @Suppress("LongMethod")
  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "Standard Candle lighting",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "15 Minutes",
          "London",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(LONDON_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "20 Minutes",
          "Raanana",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(RAANANA_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "21 Minutes",
          "Tel Aviv",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(TEL_AVIV_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "22 Minutes",
          "Ashdod",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(ASHDOD_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "30 Minutes",
          "Tzefat, Teveria, Haifa",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(NORTHERN_ISRAEL_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "40 Minutes",
          "Jerusalem, Petach Tikvah",
          Zmanim.Type.CandleLighting,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(JERUSALEM_MINUTES_BEFORE_SHKIA))
        }
      }
    )

    return zmanim
  }
}
