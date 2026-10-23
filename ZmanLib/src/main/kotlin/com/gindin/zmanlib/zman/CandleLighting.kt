/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "18 Minutes"

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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(15))
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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(20))
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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(21))
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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(22))
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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(30))
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
          return toCalendar(zmanimCalculator, zmanimCalculator.getCandleLighting(40))
        }
      }
    )

    return zmanim
  }
}
