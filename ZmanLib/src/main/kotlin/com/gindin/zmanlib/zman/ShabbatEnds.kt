/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

internal class ShabbatEnds(
  zmanimCalculator: ZmanimCalculator
) : TzaitHacochavim(Zmanim.Type.ShabbatEnd, zmanimCalculator) {

  override fun buildZmanimList(zmanimCalculator: ZmanimCalculator): List<Zman> {

    return mergeZmanimLists(
      buildStandardTzaitZmanim(Zmanim.Type.ShabbatEnd, zmanimCalculator, false),
      buildExtraTzaitZmanim(Zmanim.Type.ShabbatEnd, zmanimCalculator)
    )
  }

}
