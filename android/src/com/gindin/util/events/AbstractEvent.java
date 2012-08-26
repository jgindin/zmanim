/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.util.events;

/**
 *
 */
public abstract class AbstractEvent<T> {

	public final T                                            eventInfo;


	protected AbstractEvent( T eventInfo ) {
		this.eventInfo = eventInfo;
	}


	public T getEventInfo() {
		return eventInfo;
	}
}  // End of AbstractEvent class
