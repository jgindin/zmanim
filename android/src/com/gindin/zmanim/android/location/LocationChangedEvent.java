/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.util.events.ProgressAwareEventListener;
import com.gindin.util.events.AbstractEvent;
import com.gindin.zmanim.location.ZmanimLocation;


public class LocationChangedEvent
	extends AbstractEvent<ZmanimLocation> {

	private final LocationError                               error;

	public LocationChangedEvent( ZmanimLocation newLocation ) {
		this( newLocation, null );
	}


	public LocationChangedEvent(
		ZmanimLocation  location,
		LocationError   error
	) {
		super( location );
		this.error = error;
	}


	public LocationError getError() {
		return error;
	}


	public static interface HasListeners {
		void addLocationChangeListener( Listener listener );
		void removeLocationChangeListener( Listener listener );
	}


	public static interface Listener
		extends ProgressAwareEventListener {

		void onLocationChanged(
			LocationChangedEvent event
		);

		void onLocationNameUpdated(
			LocationNameUpdatedEvent event
		);
	}

}  // End of LocationChangedEvent class
