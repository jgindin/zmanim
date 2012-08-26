/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.util.events.EventManager;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.location.ZmanimLocation;

public class LocationChangedEventManager
	extends EventManager<LocationChangedEvent.Listener, ZmanimLocation, LocationChangedEvent> {

	@Override
	protected void dispatch(
		LocationChangedEvent.Listener listener,
		LocationChangedEvent event
	) {
		listener.onLocationChanged( event );
	}


	@Override
	protected void handleException( Exception e ) {
		LogUtils.logError( "location change dispatch", e.getMessage(), e );
	}
}
