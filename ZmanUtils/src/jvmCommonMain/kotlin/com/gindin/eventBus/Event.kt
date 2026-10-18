/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus

/**
 * An event which can be fired on the event bus. The simplest and most common case is for each event to represent
 *  one single, concrete 'event', and to define a handler with just one method. That handler will be invoked when
 *  the dispatch method is invoked.
 *
 * Roughly based on the event bus from GWT.
 */
public abstract class Event<H : EventHandler<*>> protected constructor() {

  /**
   * Invoked prior to any dispatching of the event, allowing an opportunity to interact with the cache control.
   */
  public open fun preDispatch(cacheControl: EventCacheControl) {
  }

  /**
   * Called by the event bus to send this method to the specified handler. Generally, if the handler has a method
   *  onFoo, the implementation of this method will be of the form, "handler.onFoo( this );".
   *
   * @param handler the handler to invoke.
   */
  public abstract fun dispatch(handler: H)

  /**
   * @return true if this event can be cached.
   */
  public open fun isCachable(): Boolean = true
}
