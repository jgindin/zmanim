/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus;

/**
 * The normal event handler has just one single method, something like onFoo(), which will be invoked when the
 *  corresponding event is fired on the event bus.
 *<p/>
 *
 * NOTE: Handlers MUST not assume that they are being invoked on any given thread. If a handler needs to execute
 *  on a particular thread, it must take care to do so. E.g., if it must execute on the UI thread, then code
 *  similar to the following must be used:
 *  <pre>
 *  {@code
 *  activity.runOnUiThread(
 *    new Runnable() {
 *      public void run() {
 *        doWhatYouNeedToDo();
 *      }
 *    } );
 *  }
 *  </pre>
 *
 *<p/>
 *
 * Roughly based on the event bus from GWT.
 */
public interface EventHandler<E extends Event<?>>  {

  /**
   * Indicate the type of event this handler is associated with.
   */
  Class<E> getAssociatedType();


} // End of EventHandler interface
