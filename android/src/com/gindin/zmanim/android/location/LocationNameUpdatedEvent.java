/*
 * Copyright (c) 2013. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.zmanim.location.ZmanimLocation;

/**
 * Fired when we determine that the name of a location has been updated.
 */
public class LocationNameUpdatedEvent
	extends LocationChangedEvent {

	public LocationNameUpdatedEvent( ZmanimLocation newLocation ) {
		super( newLocation );
	}


} // End of LocationNameUpdatedEvent class
