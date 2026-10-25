/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val DEFAULT_ZMAN_NAME = "Hacham Ovadia Yosef"

internal class LatestMincha(zmanimCalculator: ZmanimCalculator) :
  Zmanim(Zmanim.Type.LatestMincha, zmanimCalculator) {

  override fun getDefault(): Zman? {
    return getByName(DEFAULT_ZMAN_NAME)
  }

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = ArrayList<Zman>()

    zmanim.add(
      object :
        Zman(
          "Mishna Berura",
          "Daily Halacha's representation of the Mishna Berura's ruling, that mincha must be FINISHED by sunset.",
          Zmanim.Type.LatestMincha,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getLatestMinchaMishnaBerura())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          DEFAULT_ZMAN_NAME,
          "Daily Halacha's representation of Chacham Ovadia Yosef's ruling that mincha can be prayed so long as you are beginning NO LATER THAN 13.5 shaot zmaniot minutes after sunset.",
          Zmanim.Type.LatestMincha,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getLatestMinchaOvadiaYosef())
        }
      }
    )

    zmanim.add(
      object :
        Zman(
          "Hacham Ben Sion",
          "Daily Halacha's representation of Chacham Ben Sion Abba Shaul's ruling, that mincha can be prayed so long as you FINISH NO LATER THAN 26 minutes after sunset.",
          Zmanim.Type.LatestMincha,
          false,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getLatestMinchaBenSion())
        }
      }
    )

    return zmanim
  }
}
