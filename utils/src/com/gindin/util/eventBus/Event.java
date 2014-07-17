/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util.eventBus;

/**
 * An event which can be fired on the event bus. The simplest and most common case is for each event to represent
 *  one single, concrete 'event', and to define a handler with just one method. That handler will be invoked when
 *  the dispatch method is invoked.
 *<p/>
 *
 * Roughly based on the event bus from GWT.
 */
public abstract class Event<H extends EventHandler> {

	protected Event() {
	}


	/**
	 * Invoked prior to any dispatching of the event, allowing an opportunity to interact with the cache control.
	 */
	public void preDispatch( EventCacheControl cacheControl ) {
	}


	/**
	 * Called by the event bus to send this method to the specified handler. Generally, if the handler has a method
	 *  onFoo, the implementation of this method will be of the form, "handler.onFoo( this );".
	 *
	 * @param handler the handler to invoke.
	 */
	public abstract void dispatch( H handler );


	/**
	 * @return true if this event can be cached.
	 */
	public boolean isCachable() {
		return true;
	}

} // End of Event class
