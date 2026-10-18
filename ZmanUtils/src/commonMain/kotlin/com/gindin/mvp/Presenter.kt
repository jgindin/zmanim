/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp

/**
 * Controls the View, telling it what to do and when.
 *
 * The view type is deliberately unbounded: the Java presenters and views built on these contracts use raw
 *  types, and Java does not accept a raw type as a type argument within a wildcard-parameterized bound.
 */
public interface Presenter<V> {

  /**
   * Called when the presenter is no longer needed.
   */
  public fun destroy()
}
