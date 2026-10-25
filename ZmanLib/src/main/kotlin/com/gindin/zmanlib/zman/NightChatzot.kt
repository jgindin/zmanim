/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

internal class NightChatzot(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.NightChatzot, zmanimCalculator) {

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "Mid-night",
          "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.",
          Zmanim.Type.NightChatzot,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getSolarMidnight())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Fixed Local Mid-Night",
          "R. Moshe Feinstein and Aruch Ha-Shulchan, based on chatzot being fixed at solar noon.",
          Zmanim.Type.NightChatzot,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getFixedLocalNightChatzos())
        }
      }
    )

    return zmanim
  }
}
