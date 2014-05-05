/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util.eventBus;

/**
 * The normal event handler has just one single method, something like onFoo(), which will be invoked when the
 *  corresponding event is fired on the event bus.
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
