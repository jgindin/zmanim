/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.mvp

import com.gindin.eventBus.EventBus

/**
 * Abstract, base implementation of a Presenter.
 */
public abstract class AbstractPresenter<V> protected constructor(
  protected val view: V,
  protected val eventBus: EventBus,
) : Presenter<V> {

  init {
    // The view's presenter type is only known to be some Presenter, so the registration has to be
    //  made through a cast to the presenter type this view was written for.
    @Suppress("UNCHECKED_CAST")
    (view as View<Presenter<V>>).setPresenter(this)
  }
}
