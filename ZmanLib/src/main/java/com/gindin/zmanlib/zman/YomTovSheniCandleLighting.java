/*
 * Copyright (c) 2019. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import java.util.List;

public class YomTovSheniCandleLighting
  extends TzaitHacochavim {


  YomTovSheniCandleLighting(
    ZmanimCalculator zmanimCalculator
  ) {
    super( Type.YomTovSheniCandleLighting, zmanimCalculator );
  }


  @Override
  protected List<Zman> buildZmanimList( ZmanimCalculator zmanimCalculator ) {
    return buildStandardTzaitZmanim( Type.YomTovSheniCandleLighting, zmanimCalculator );
  }
}
