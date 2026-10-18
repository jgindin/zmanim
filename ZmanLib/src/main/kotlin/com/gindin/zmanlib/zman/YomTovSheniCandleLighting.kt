/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

internal class YomTovSheniCandleLighting(
  zmanimCalculator: ZmanimCalculator
) : TzaitHacochavim(Zmanim.Type.YomTovSheniCandleLighting, zmanimCalculator) {

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {
    return buildStandardTzaitZmanim(Zmanim.Type.YomTovSheniCandleLighting, zmanimCalculator, false)
  }

}
