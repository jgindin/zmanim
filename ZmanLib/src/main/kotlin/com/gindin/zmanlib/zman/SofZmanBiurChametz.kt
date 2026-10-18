/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "GR'A"

public class SofZmanBiurChametz internal constructor(
  zmanimCalculator: ZmanimCalculator
) : Zmanim(Zmanim.Type.SofZmanBiurChametz, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }


  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object : Zman(
        DEFAULT_ZMAN_NAME,
        "Based on the day as calculated from sunrise to sunset.",
        Zmanim.Type.SofZmanBiurChametz
      ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanBiurChametzGRA())
        }
      }
    )

    zmanim.add(
      object : Zman(
        "MG'A 72 Minutes",
        "Based on alot being 72 minutes before sunrise.",
        Zmanim.Type.SofZmanBiurChametz
      ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanBiurChametzMGA72Minutes())
        }
      }
    )

    zmanim.add(
      object : Zman(
        "MG'A 16.1 deg",
        "Based on alot being 16.1 degrees before sunrise.",
        Zmanim.Type.SofZmanBiurChametz
      ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanBiurChametzMGA16Point1Degrees())
        }
      }
    )

    zmanim.add(
      object : Zman(
        "Baal HaTanya",
        "Based on Baal HaTanya's sunrise and sunset calculations.",
        Zmanim.Type.SofZmanBiurChametz
      ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSofZmanBiurChametzBaalHatanya())
        }
      }
    )

    return zmanim
  }

}
