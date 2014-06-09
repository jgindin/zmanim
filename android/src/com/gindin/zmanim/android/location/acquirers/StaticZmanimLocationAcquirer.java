/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.PlaceAcquiredEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.SharedPreferences;

/**
 * Location provider which uses a place name like "5217 South Brandon St., Seattle, WA" to determine the Zmanim.
 */
public class StaticZmanimLocationAcquirer
	extends ZmanimLocationAcquirer {


	public StaticZmanimLocationAcquirer(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs
	) {

		super( eventBus, locationManagementPrefs );
	}


	@Override
	public void acquire() {
		String place = locationManagementPrefs.getString( LocationProviders.StaticProviderOptions.PLACE.name(), "" );
		ZmanimLocation location = new ZmanimLocation.Builder()
			.locationName( place )
			.inTimeZone( getTimeZoneFromPrefs() )
			.build();
		eventBus.fire( new PlaceAcquiredEvent( location ) );
	}


}  // End of StaticLocationProvider class
