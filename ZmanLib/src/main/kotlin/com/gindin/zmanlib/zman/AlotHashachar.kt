/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "16.1 deg"

internal class AlotHashachar(zmanimCalculator: ZmanimCalculator) : Zmanim(Zmanim.Type.Alot, zmanimCalculator) {

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
          "120 Minutes",
          "Before sea level sunrise, based on the time to walk the distance of 5 Mil at 24 minutes a Mil.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos120())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "120 Minutes Zmaniyot",
          "GR'A, based on 1/6th of the day before sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos120Zmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "When the sun is 16.9 degrees below the eastern geometric horizon before sunrise.",
          Zmanim.Type.Alot,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlosBaalHatanya())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "When the sun is 16.1 degrees below the eastern geometric horizon before sunrise.",
          Zmanim.Type.Alot,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "18 deg",
          "When the sun is 18 degrees below the eastern geometric horizon before sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos18Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "19.8 deg",
          "When the sun is 19.8 degrees below the eastern geometric horizon before sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos19Point8Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "26 deg",
          "When the sun is 26 degrees below the eastern geometric horizon before sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos26Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "60 Minutes",
          "Based on the time to walk 4 Mil at 15 minutes/Mil.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos60())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "72 Minutes",
          "Based on the time to walk 4 Mil at 18 minutes/Mil.",
          Zmanim.Type.Alot,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos72())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "GR'A 72 Minutes Zmaniyot",
          "Based on 1/10th of the day before sea level sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos72Zmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "90 Minutes",
          "Based on the time to walk 4 Mil at 22.5 minutes a Mil.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos90())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "GR'A 90 Minutes Zmaniyot",
          "Based on 1/8th of the day before sea level sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos90Zmanis())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "96 Minutes",
          "Based on the time to walk the distance of 4 Mil at 24 minutes/Mil.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos96())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "GR'A 96 Minutes Zmaniyot",
          "Based on 1/8th of the day before sea level sunrise.",
          Zmanim.Type.Alot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getAlos96Zmanis())
        }
      }
    )

    return zmanim
  }
}
