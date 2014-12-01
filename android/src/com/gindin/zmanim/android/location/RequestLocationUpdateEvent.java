/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventCacheControl;
import com.gindin.eventBus.EventHandler;

/**
 * Fired to request that the location be re-acquired, using whatever acquirer has already been configured.
 */
public class RequestLocationUpdateEvent
	extends Event<RequestLocationUpdateEvent.Handler> {

	public static abstract class Handler implements EventHandler<RequestLocationUpdateEvent> {

		public abstract void onLocationUpdateRequested();


		@Override
		public Class<RequestLocationUpdateEvent> getAssociatedType() {
			return RequestLocationUpdateEvent.class;
		}
	}


	@Override
	public boolean isCachable() {
		return false;
	}


	@Override
	public void preDispatch( EventCacheControl cacheControl ) {
		super.preDispatch( cacheControl );

		// Need to ensure that any prior location acquisition events are removed from the cache because we need
		//  to get a fresh start.
		cacheControl.clearCacheForType( LocationAcquiredEvent.class );
	}


	@Override
	public void dispatch( Handler handler ) {
		handler.onLocationUpdateRequested();
	}

} // End of RequestLocationUpdateEvent class
