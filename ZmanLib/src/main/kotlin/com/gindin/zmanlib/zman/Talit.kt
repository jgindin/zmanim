/*
 * Copyright (c) 2020. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "10.2 deg"

internal class Talit(zmanimCalculator: ZmanimCalculator) : Zmanim(Zmanim.Type.Talit, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "7.65 deg",
          "Based on the position of the sun when it is 7.65 degrees below geometric zenith.",
          Zmanim.Type.Talit,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMisheyakir7Point65Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "9.5 deg",
          "Based on the position of the sun when it is 9.5 degrees below geometric zenith.",
          Zmanim.Type.Talit,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMisheyakir9Point5Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "Based on the position of the sun when it is 10.2 degrees below geometric zenith.",
          Zmanim.Type.Talit,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMisheyakir10Point2Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "11 deg",
          "Based on the position of the sun when it is 11 degrees below geometric zenith.",
          Zmanim.Type.Talit,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMisheyakir11Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "11.5 deg",
          "Based on the position of the sun when it is 11.5 degrees below geometric zenith.",
          Zmanim.Type.Talit,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMisheyakir11Point5Degrees())
        }
      }
    )

    return zmanim
  }
}
