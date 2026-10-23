/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "Sunset"

public class Shkia internal constructor(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.Shkia, zmanimCalculator) {

  public companion object {

    public const val ZMAN_WITH_ELEVATION: String = "Sunset (with elevation)"
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "The exact sunset time.",
          Zmanim.Type.Shkia,
          false,
        ) {
        override fun calculate(): Calendar? {
          val sunset = zmanimCalculator.getSeaLevelSunset()
          return toCalendar(zmanimCalculator, sunset)
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          ZMAN_WITH_ELEVATION,
          "The exact sunset time, taking elevation into account.",
          Zmanim.Type.Shkia,
          false,
        ) {
        override fun calculate(): Calendar? {
          val sunset = zmanimCalculator.getSunset()
          return toCalendar(zmanimCalculator, sunset)
        }
      }
    )

    return zmanim
  }

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }
}
