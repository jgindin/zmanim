/*
 * Copyright (c) 2014. Jay R. Gindin
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
public class StaticLocationAcquirer
	extends LocationAcquirer {


	public StaticLocationAcquirer(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
		SharedPreferences personalPrefs
	) {

		super( eventBus, locationManagementPrefs, personalPrefs );
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
