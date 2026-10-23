/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "Sunrise"

public class Hanetz internal constructor(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.Hanetz, zmanimCalculator) {

  public companion object {

    public const val ZMAN_WITH_ELEVATION: String = "Sunrise (with elevation)"
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "The exact sunrise time.",
          Zmanim.Type.Hanetz,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSeaLevelSunrise())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          ZMAN_WITH_ELEVATION,
          "The exact sunrise time, taking elevation into account.",
          Zmanim.Type.Hanetz,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSunrise())
        }
      }
    )

    return zmanim
  }

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }
}
