/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;


import java.util.List;

/**
 *
 */
class ShabbatEnds
  extends TzaitHacochavim {


  ShabbatEnds(
    final ZmanimCalculator zmanimCalculator
  ) {

    super( Type.ShabbatEnd, zmanimCalculator );
  }


  @Override
  protected List<Zman> buildZmanimList( final ZmanimCalculator zmanimCalculator ) {

    return mergeZmanimLists(
      buildStandardTzaitZmanim( Type.ShabbatEnd, zmanimCalculator, false ),
      buildExtraTzaitZmanim( Type.ShabbatEnd, zmanimCalculator ) );
  }

}  // End of TzaitHacochavim class
