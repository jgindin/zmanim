/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "2 Stars"

internal class BeinHashmashot(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.BeinHashmashot, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "13 Degrees",
          "Based on Rabainu Tam's calculation as the time the sun is 13° below geometric zenith (90°).",
          Zmanim.Type.BeinHashmashot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getBainHasmashosRT13Point24Degrees())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "58.5 Minutes",
          "Based on Rabainu Tam's calculation as a 58.5 minute offset after sunset.",
          Zmanim.Type.BeinHashmashot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getBainHasmashosRT58Point5Minutes())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "13.5 Minutes",
          "Based on 13.5 minutes (3/4 of an 18 minute mil before shkiah calculated as 7.083°.",
          Zmanim.Type.BeinHashmashot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(
            zmanimCalculator,
            zmanimCalculator.getBainHasmashosRT13Point5MinutesBefore7Point083Degrees(),
          )
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "2 Stars",
          "Based on Rabainu Tam's calculation in the opinion of the Divray Yosef (see Yisrael Vehazmanim) of 5/18th (27.77%) of the time between alot (calculated as 19.8° before sunrise) and sunrise.",
          Zmanim.Type.BeinHashmashot,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getBainHasmashosRT2Stars())
        }
      }
    )

    return zmanim
  }
}
