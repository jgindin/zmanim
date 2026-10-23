/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import java.util.Calendar

private const val SEATTLE_SEPHARDIC_MINUTES_AFTER_SHKIA = 30

/** Used for allowing the user to see when a full-day fast ends. */
internal class FastEnds(zmanimCalculator: ZmanimCalculator) : TzaitHacochavim(Zmanim.Type.FastEnd, zmanimCalculator) {

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    val zmanim = buildStandardTzaitZmanim(Zmanim.Type.FastEnd, zmanimCalculator, true)

    zmanim.add(
      object :
        Zman(
          "Seattle Sephardic",
          "Based on the Seattle Sephardic community's tradition.",
          Zmanim.Type.FastEnd,
        ) {
        override fun calculate(): Calendar? {
          return toCalendar(zmanimCalculator, zmanimCalculator.getEndOfFast(SEATTLE_SEPHARDIC_MINUTES_AFTER_SHKIA))
        }
      }
    )

    return zmanim
  }
}
