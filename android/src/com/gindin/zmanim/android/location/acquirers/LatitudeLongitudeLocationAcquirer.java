/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.eventBus.EventBus;
import com.gindin.location.LatitudeLongitudeParser;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.SharedPreferences;
import android.location.Location;

import java.util.TimeZone;

/**
 * Location provider that deals with a static latitude & longitude location.
 */
public class LatitudeLongitudeLocationAcquirer
	extends LocationAcquirer {


	public LatitudeLongitudeLocationAcquirer(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
		SharedPreferences personalPrefs
	) {

		super( eventBus, locationManagementPrefs, personalPrefs );
	}


	@Override
	public void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage ) {

		Location ourLocation = new Location( "Zmanim Latitude & Longitude Provider" );

		updateLatitudeFromPrefs( locationManagementPrefs, ourLocation );
		updateLongitudeFromPrefs( locationManagementPrefs, ourLocation );

		TimeZone timeZone = getTimeZoneFromPrefs();

		ZmanimLocation newLocation = new ZmanimLocation.Builder()
			.atLatitude( ourLocation.getLatitude() )
			.atLongitude( ourLocation.getLongitude() )
			.inTimeZone( timeZone )
			.build();

		fireLocationAcquiredEvent( newLocation, false );
	}


	private void updateLatitudeFromPrefs(
		SharedPreferences locationManagementPrefs,
		Location          ourLocation
	) {

		String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
		try {
			ourLocation.setLatitude( LatitudeLongitudeParser.LATITUDE_PARSER.parseAsDouble( latitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			fireLocationAcquiredEvent( null, true, new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + latitudePref ) );
		}
	}


	private void updateLongitudeFromPrefs(
		SharedPreferences locationManagementPrefs,
		Location          ourLocation
	) {

		String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
		try {
			ourLocation.setLongitude( LatitudeLongitudeParser.LONGITUDE_PARSER.parseAsDouble( longitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			fireLocationAcquiredEvent( null, true, new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + longitudePref ) );
		}
	}

}  // End of LatitudeLongitudeLocationProvider class
