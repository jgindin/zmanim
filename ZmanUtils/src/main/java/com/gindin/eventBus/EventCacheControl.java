/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus;

/**
 * Used to allow Events that need to clear specific cached events.
 *<p/>
 *
 * For example, if an event representing the acquisition of some resource is cached, and then later an event
 *  representing the loss of that resource is to be fired, the first event should be removed from the cache.
 */
public interface EventCacheControl {

	void clearCacheForType( Class<? extends Event> type );

} // End of EventCacheControl interface
