/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.PlaceAcquiredEvent;
import com.gindin.zmanim.location.ZmanimLocation;

import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens for RawLocationAcquiredEvent and PlaceAcquiredEvent and starts the process of translating to an address.
 */
public class LocationToAddressConverter {

	private final EventBus                                    eventBus;

	private final List<LocationResolver>                      orderedResolvers;

	private final LocationAcquiredEvent.Handler            locationAcquiredHandler;
	private final PlaceAcquiredEvent.Handler                  placeAcquiredHandler;


	public LocationToAddressConverter(
		Geocoder geocoder,
		EventBus eventBus
	) {

		this.eventBus = eventBus;

		orderedResolvers = new ArrayList<LocationResolver>();
		orderedResolvers.add( new AndroidResolver( eventBus, geocoder ) );
		orderedResolvers.add( new NominatimResolver( eventBus ) );
		orderedResolvers.add( new PassthroughResolver( eventBus ) );

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				if ( !location.isResolved() ) {
					orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
				}
			}


			@Override
			public void onError(
				LocationError   error,
				ZmanimLocation  lastKnownLocation
			) {
				// Don't need to do anything with this.
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


} // End of LocationToAddressConverter class
