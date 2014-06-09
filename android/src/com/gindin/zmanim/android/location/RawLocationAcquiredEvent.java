/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.util.eventBus.Event;
import com.gindin.util.eventBus.EventHandler;
import com.gindin.zmanim.location.ZmanimLocation;

/**
 * An event fired when the latitude and longitude (the current location) have been acquired, regardless of how they
 *  may have been acquired.
 */
public class RawLocationAcquiredEvent
	extends Event<RawLocationAcquiredEvent.Handler> {


	public static abstract class Handler implements EventHandler<RawLocationAcquiredEvent> {

		/**
		 * Invoked when the location has been acquired. By location, we mean only the latitude and
		 *  longitude.
		 *
		 * @param location Only the latitude and longitude fields will be valid.
		 */
		public abstract void onLocationAcquired( ZmanimLocation location );

		/**
		 * Invoked when an error occurred while trying to acquire the location.
		 *
		 * @param error Describes the error.
		 * @param lastKnownLocation The last known locaiton.
		 */
		public abstract void onError(
			LocationError   error,
			ZmanimLocation  lastKnownLocation
		);


		@Override
		public Class<RawLocationAcquiredEvent> getAssociatedType() {
			return RawLocationAcquiredEvent.class;
		}
	}


	/** The location which has been acquired. */
	private final ZmanimLocation                              location;

	/**
	 * If there was any error determining the location, then this describes the error. In this case, the location
	 *  is actually the last known location
	 */
	private final LocationError                               error;


	public RawLocationAcquiredEvent(
		ZmanimLocation newLocation
	) {
		this( newLocation, null );
	}


	public RawLocationAcquiredEvent(
		ZmanimLocation  location,
		LocationError   error
	) {
		this.location = location;
		this.error = error;
	}


	@Override
	public void dispatch( Handler handler ) {
		handler.onLocationAcquired( location );
	}

} // End of RawLocationAcquiredEvent class
