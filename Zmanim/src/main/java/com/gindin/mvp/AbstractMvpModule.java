/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.mvp;

import com.gindin.eventBus.EventBus;

import androidx.fragment.app.FragmentActivity;

/**
 * Base for a module which is used to tie together a presenter and view.
 */
public abstract class AbstractMvpModule<P extends Presenter>
  implements MvpModule<P> {

  protected final FragmentActivity                          activity;

  protected final EventBus                                  eventBus;


  protected AbstractMvpModule(
    FragmentActivity activity,
    EventBus eventBus
  ) {
    this.activity = activity;
    this.eventBus = eventBus;
  }


} // End of AbstractMvpModule class
