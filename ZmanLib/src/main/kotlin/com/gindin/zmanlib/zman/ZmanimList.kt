/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman

/** Encapsulate a list of Zmanim. */
public class ZmanimList private constructor(private val list: MutableList<Zmanim>) : MutableList<Zmanim> by list {

  public constructor() : this(ArrayList())

  public fun findType(type: Zmanim.Type): Zmanim? {
    for (z in list) {
      if (z.type == type) {
        return z
      }
    }

    return null
  }

  override fun equals(other: Any?): Boolean {
    return list == other
  }

  override fun hashCode(): Int {
    return list.hashCode()
  }
}
