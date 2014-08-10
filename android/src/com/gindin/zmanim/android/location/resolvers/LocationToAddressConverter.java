/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.PlaceAcquiredEvent;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.SharedPreferences;
import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens for RawLocationAcquiredEvent and PlaceAcquiredEvent and starts the process of translating to an address.
 */
public class LocationToAddressConverter {

	private final EventBus                                    eventBus;

	private final List<LocationResolver>                      orderedResolvers;

	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;
	private final PlaceAcquiredEvent.Handler                  placeAcquiredHandler;


	public LocationToAddressConverter(
		Geocoder          geocoder,
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {

		this.eventBus = eventBus;

		orderedResolvers = new ArrayList<LocationResolver>();
		orderedResolvers.add( new AndroidResolver( eventBus, geocoder, personalPrefs ) );
		orderedResolvers.add( new NominatimResolver( eventBus, personalPrefs ) );
		orderedResolvers.add( new PassthroughResolver( eventBus, personalPrefs ) );

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				handleLocationAcquired( location );
			}


			@Override
			public void onError(
				LocationError   error,
				ZmanimLocation  lastKnownLocation
			) {
				handleLocationAcquired( lastKnownLocation );
			}
		};
		eventBus.addHandler( locationAcquiredHandler );

		placeAcquiredHandler = new PlaceAcquiredEvent.Handler() {
			@Override
			public void placeAcquired( ZmanimLocation location ) {
				orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
			}
		};
		eventBus.addHandler( placeAcquiredHandler );
	}


	public void destroy() {

		eventBus.removeHandler( locationAcquiredHandler );
		eventBus.removeHandler( placeAcquiredHandler );
	}


	private void handleLocationAcquired( ZmanimLocation location ) {
		if ( !location.isResolved() ) {
			orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
		}
	}


} // End of LocationToAddressConverter class
