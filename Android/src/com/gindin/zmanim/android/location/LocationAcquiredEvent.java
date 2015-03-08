/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An event fired when the current location has been acquired. If a handler needs to know if the location
 *  is fully resolved, it will have to ask the location.
 */
public class LocationAcquiredEvent
	extends Event<LocationAcquiredEvent.Handler> {


	public static abstract class Handler implements EventHandler<LocationAcquiredEvent> {

		/**
		 * Invoked when the location has been acquired.
		 *
		 * @param location The location which has been acquired.
		 * @param isCachedLocation Indicate if the location is cached and, therefore, may not be up-to-date.
		 */
		public abstract void onLocationAcquired(
			ZmanimLocation  location,
			boolean         isCachedLocation
		);

		/**
		 * Invoked when an error occurred while trying to acquire the location. The specified location is, by definition,
		 *  cached and not necessarily correct.
		 *
		 * @param error Describes the error.
		 * @param lastKnownLocation The last known location.
		 */
		public abstract void onError(
			@NotNull LocationError   error,
			@NotNull ZmanimLocation  lastKnownLocation
		);


		@Override
		public Class<LocationAcquiredEvent> getAssociatedType() {
			return LocationAcquiredEvent.class;
		}
	}


	/** The location which has been acquired. */
	private final ZmanimLocation                              location;


	private final boolean                                     isCachedLocation;

	/**
	 * If there was any error determining the location, then this describes the error. In this case, the location
	 *  is actually the last known location
	 */
	private final LocationError                               error;


	public LocationAcquiredEvent(
		ZmanimLocation  newLocation,
		boolean         isCachedLocation
	) {
		this( newLocation, isCachedLocation, null );
	}


	public LocationAcquiredEvent(
		@NotNull ZmanimLocation location,
		boolean                 isCachedLocation,
		@Nullable LocationError error
	) {
		this.location = location;
		this.isCachedLocation = isCachedLocation;
		this.error = error;
	}


	@Override
	public void dispatch( Handler handler ) {
		if ( error == null ) {
			handler.onLocationAcquired( location, isCachedLocation );
		}
		else {
			handler.onError( error, location );
		}
	}


	@Override
	public String toString() {
		return "LocationAcquiredEvent{" +
			"location=" + location +
			"cached=" + isCachedLocation +
			", error=" + error +
			'}';
	}

} // End of RawLocationAcquiredEvent class
