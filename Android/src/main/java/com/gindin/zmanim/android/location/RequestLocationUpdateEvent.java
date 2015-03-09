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

		public abstract void onLocationUpdateRequested( final RequestLocationUpdateEvent event );


		@Override
		public Class<RequestLocationUpdateEvent> getAssociatedType() {
			return RequestLocationUpdateEvent.class;
		}
	}


	public enum LocationDeterminationOverride {

		/**
		 * Use the normal procedure for determining the user's location.
		 */
		NORMAL,

		/**
		 * Force the determination of the user's location to finding their actual location.
		 */
		FORCE_TO_ACTUAL_LOCATION
	}


	public enum CachedLocationUsage {

		/**
		 * We prefer to use the cached location ASAP, and then continue to try and determine the actual location.
		 */
		PREFER,

		/**
		 * If a cached location is available, use it and don't bother with anything else. If no cached location is
		 *  available, consider that an error.
		 */
		EXCLUSIVE,

		/**
		 * Try to get the actual location and only the case where it cannot be determined should we fall back to using
		 *  a cached location.
		 */
		FALLBACK,

		/**
		 * Do not use a cached location.
		 */
		NEVER
	}


	/**
	 * Indicate that despite user preferences, the location being requested is the user's actual location.
	 */
	public final LocationDeterminationOverride                locationDeterminationOverride;

	/**
	 * Indicate that if a cached location is available, we should use it immediately, but then continue to obtain
	 *  the user's location, based on their preferences. (Unless, of course the needsActualLocation flag is set, in
	 *  which case that takes precedence.)
	 */
	public final CachedLocationUsage                          cachedLocationUsage;



	public RequestLocationUpdateEvent( final CachedLocationUsage cachedLocationUsage ) {

		this.locationDeterminationOverride = LocationDeterminationOverride.NORMAL;
		this.cachedLocationUsage = cachedLocationUsage;
	}


	public RequestLocationUpdateEvent(
		final LocationDeterminationOverride locationDeterminationOverride,
		final CachedLocationUsage           cachedLocationUsage
	) {
		this.locationDeterminationOverride = locationDeterminationOverride;
		this.cachedLocationUsage = cachedLocationUsage;
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
		handler.onLocationUpdateRequested( this );
	}

} // End of RequestLocationUpdateEvent class
