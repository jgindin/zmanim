/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.util.eventBus.Event;
import com.gindin.util.eventBus.EventHandler;
import com.gindin.zmanim.location.ZmanimLocation;

/**
 * An event fired after the location has been resolved to an address. This is similar to the RawLocationAcquiredEvent,
 *  except that now the ZmanimLocation is expected to have as rich of a name as possible.
 */
public class FullLocationAcquiredEvent
	extends Event<FullLocationAcquiredEvent.Handler> {

	public static abstract class Handler implements EventHandler<FullLocationAcquiredEvent> {

		/**
		 * Invoked when then address has been acquired.
		 *
		 * @param location The location corresponding to the address.
		 */
		public abstract void addressAcquired(
			ZmanimLocation  location
		);

		@Override
		public Class<FullLocationAcquiredEvent> getAssociatedType() {
			return FullLocationAcquiredEvent.class;
		}
	}


	/** The location corresponding to the address. */
	private final ZmanimLocation                              location;


	public FullLocationAcquiredEvent(
		ZmanimLocation location
	) {
		this.location = location;
	}


	@Override
	public void dispatch( Handler handler ) {
		handler.addressAcquired( location );
	}


} // End of FullLocationAcquiredEvent class
