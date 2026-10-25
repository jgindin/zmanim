/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

/** See http://dinonline.org/2015/01/17/parshas-bo-the-halachic-time-of-chatzos/ */
internal class Chatzot(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.Chatzot, zmanimCalculator) {

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "Mid-Day",
          "GR'A, based on the day for Jewish halachic times starts at sunrise and ends at sunset.",
          Zmanim.Type.Chatzot,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getChatzos())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Fixed Local Mid-Day",
          "R. Moshe Feinstein and Aruch Ha-Shulchan, based on chatzot being fixed at solar noon.",
          Zmanim.Type.Chatzot,
          true,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getFixedLocalChatzos())
        }
      }
    )

    return zmanim
  }
}
