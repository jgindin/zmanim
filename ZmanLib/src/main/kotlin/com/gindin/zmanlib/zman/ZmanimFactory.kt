/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

import com.gindin.zmanlib.calendar.HebrewDate
import com.kosherjava.zmanim.util.GeoLocation

/** Handle creation of zmanim. */
public class ZmanimFactory
private constructor(
  location: GeoLocation,
  date: HebrewDate,
) {

  /** Cache so that we don't create the zmanim instances more than once. */
  private val typeToZmanimMap: MutableMap<Zmanim.Type, Zmanim> = HashMap()

  // TODO: If we were using Dagger to create as needed, we could just have each instance get
  //  injected with something that could be called when the user wants (doesn't want) to use
  //  elevation. Instead...well, we've got something more complicated to deal with...
  private val zmanimCalculator: ZmanimCalculator = ZmanimCalculator(location, date)

  private var usingElevation: Boolean = false

  private fun usingElevation(useElevation: Boolean) {

    if (usingElevation != useElevation) {
      usingElevation = useElevation
      zmanimCalculator.setUseElevation(useElevation)

      typeToZmanimMap.clear()
    }
  }

  private fun get(type: Zmanim.Type): Zmanim {

    synchronized(typeToZmanimMap) {
      var zmanim = typeToZmanimMap[type]
      if (null != zmanim) {
        return zmanim
      }

      zmanim = type.get(zmanimCalculator)
      typeToZmanimMap[type] = zmanim
      return zmanim
    }
  }

  public companion object {

    private val locationToFactory: MutableMap<Pair<GeoLocation, HebrewDate>, ZmanimFactory> =
      HashMap()

    private var useElevation: Boolean = false

    @JvmStatic
    public fun get(
      location: GeoLocation,
      date: HebrewDate,
      type: Zmanim.Type,
    ): Zmanim {

      val key = Pair(location, date)
      var factory = locationToFactory[key]
      if (null == factory) {
        factory = ZmanimFactory(location, date)
        locationToFactory[key] = factory
      }

      factory.usingElevation(useElevation)

      return factory.get(type)
    }

    @JvmStatic
    public fun useElevation(use: Boolean) {
      useElevation = use
      for (factory in locationToFactory.values) {
        factory.usingElevation(use)
      }
    }
  }
}
