/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.providers;

import com.gindin.util.eventBus.EventBus;

import android.content.SharedPreferences;
import android.location.LocationManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Knows how to create instances of the various location providers.
 */
public class LocationProviderFactory {

	public static enum Type {
		FROM_DEVICE,
		LAT_LONG_FROM_USER,
		NAME_FROM_USER
	}


	// @Inject
	private final EventBus                                    eventBus;

	private final SharedPreferences                           locationManagementPrefs;

	private final LocationManager                             locationManager;

	private final Map<Type,ZmanimLocationProvider>            typeToProviderMap = new HashMap<Type, ZmanimLocationProvider>();


	public LocationProviderFactory(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
	  LocationManager   locationManager
	) {
		this.eventBus = eventBus;
		this.locationManagementPrefs = locationManagementPrefs;
		this.locationManager = locationManager;
	}


	public ZmanimLocationProvider get( Type type ) {

		ZmanimLocationProvider provider = typeToProviderMap.get( type );
		if ( provider == null ) {
			switch ( type ) {
				case FROM_DEVICE:
					provider = new DynamicZmanimLocationProvider( eventBus, locationManagementPrefs, locationManager );
					break;

				case LAT_LONG_FROM_USER:
					provider = new LatitudeLongitudeZmanimLocationProvider( eventBus, locationManagementPrefs );
					break;

				case NAME_FROM_USER:
					provider = new StaticZmanimLocationProvider( eventBus, locationManagementPrefs );
					break;

				default:
					assert false : "Unknown provider type: " + type;
			}

			typeToProviderMap.put( type, provider );
		}

		return provider;
	}


} // End of LocationProviderFactory class
