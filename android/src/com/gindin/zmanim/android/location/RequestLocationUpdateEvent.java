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


	public static enum NeedActualLocation {

	}

	public static enum CachedLocation {
		/** We prefer to use the cached location ASAP, and then continue to try and determine the actual location. */
		PREFER,



		FALLBACK
	}


	/**
	 * Indicate that despite user preferences, the location being requested is the user's actual location.
	 */
	public final boolean                                      needsActualLocation;

	/**
	 * Indicate that if a cached location is available, we should use it immediately, but then continue to obtain
	 *  the user's location, based on their preferences. (Unless, of course the needsActualLocation flag is set, in
	 *  which case that takes precedence.)
	 */
	public final boolean                                      preferCachedLocation;



	public RequestLocationUpdateEvent() {
		needsActualLocation = false;
		preferCachedLocation = true;
	}


	public RequestLocationUpdateEvent(
		final boolean needsActualLocation,
		final boolean preferCachedLocation
	) {
		this.needsActualLocation = needsActualLocation;
		this.preferCachedLocation = preferCachedLocation;
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
