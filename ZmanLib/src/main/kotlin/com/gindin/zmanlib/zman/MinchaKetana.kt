/*
 * Copyright (c) 2023. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "GR'A"

internal class MinchaKetana(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.MinchaKetana, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  // A flat table of independent zman definitions, one per opinion, with no control flow: splitting
  //  it would only scatter the table, so the length rule does not apply.
  @Suppress("LongMethod")
  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "GR'A and the Baal Hatanya, based on the day as calculated from sunrise to sunset.",
          Zmanim.Type.MinchaKetana,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetana())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 16.1 deg",
          "Based on the day starting and ending 16.1 degrees below the horizon.",
          Zmanim.Type.MinchaKetana,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetana16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "MG'A 72 Minutes",
          "Based on the day starting 72 minutes before sunrise and ending 72 minutes after sunset.",
          Zmanim.Type.MinchaKetana,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetana72Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Chacham Yosef Harari-Raful",
          "Based on the day starting 1/10th of the day before sunrise and and is usually calculated as ending 40 minutes after sunset.",
          Zmanim.Type.MinchaKetana,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetanaAteretTorah())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Baal HaTanya",
          "Based on Baal HaTanya's sunrise and sunset calculations.",
          Zmanim.Type.MinchaKetana,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetanaBaalHatanya())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Rabbi Yaakov Moshe Hillel",
          "Calculated as 2.5 shaot zmaniyot before tzait 3.8 deg with shaot zmaniyot " +
            "calculated based on a day starting at alot 16.1 deg and ending at tzait 3.8 deg.",
          Zmanim.Type.MinchaKetana,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getMinchaKetanaAhavatShalom())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          " Rav Moshe Feinstein - GR'A half day",
          "Calculated according to the GR'A as 3.5 shaot zmaniyot (solar hours) after fixed local chatzot.",
          Zmanim.Type.SofZmanShema,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getMinchaKetanaGRAFixedLocalChatzosToSunset(),
          )
        }
      }
    )

    return zmanim
  }
}
