/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.util.LogUtils;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.*;
import com.gindin.zmanim.location.ZmanimLocation;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

/**
 * Listens for RawLocationAcquiredEvent and PlaceAcquiredEvent and starts the process of translating to an address.
 */
public class LocationToAddressConverter {

	private final Geocoder                                    geocoder;

	private final EventBus                                    eventBus;

	private final RawLocationAcquiredEvent.Handler            locationAcquiredHandler;
	private final PlaceAcquiredEvent.Handler                  placeAcquiredHandler;


	public LocationToAddressConverter(
		Geocoder geocoder,
		EventBus eventBus
	) {
		this.geocoder = geocoder;
		this.eventBus = eventBus;

		locationAcquiredHandler = new RawLocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				updateLocationWithName( location );
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
			public void placeAcquired( ZmanimLocation place ) {
				lookupLocationFromName( place );
			}
		};
		eventBus.addHandler( placeAcquiredHandler );
	}


	public void destroy() {

		eventBus.removeHandler( locationAcquiredHandler );
		eventBus.removeHandler( placeAcquiredHandler );
	}


	/**
	 * Given a location which MUST have the latitude and longitude set, use reverse geocoding to find the
	 *  name of that location. Upon completion, the location is updated and the FullLocationAcquiredEvent
	 *  will be fired.
	 */
	private void updateLocationWithName(
		final ZmanimLocation location
	) {

		if ( null == location ) {
			return;
		}

		AsyncTask<ZmanimLocation, Void, ZmanimLocation> getGeoLocationTask = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground( ZmanimLocation... locations ) {

				final double latitude = location.getLatitude();
				final double longitude = location.getLongitude();

				// TODO: Check our cache for a hit so we don't have to go out to the geocoder...

				Address address;
				try {
					final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 5 );

					if ( ( null != addresses ) && !addresses.isEmpty() ) {
						address = addresses.get( 0 );
					}
					else {
						address = null;
					}
				}
				catch ( IOException ioe ) {
					// Docs for Geocoder.getFromLocation say this happens when there's no network connectivity, or
					//  there's some other I/O error. In any case, it can happen...
					address = null;
				}
				catch ( Exception e ) {
					address = null;
					LogUtils.logError( "LP::updateLocationWithName", "", e );
				}

				// TODO: Cache the location somewhere...
				return new ZmanimLocation.Builder()
					.from( location )
					.resolvedTo( address )
					.build();
			}


			@Override
			protected void onPostExecute( ZmanimLocation location ) {
				super.onPostExecute( location );
				eventBus.fire( new FullLocationAcquiredEvent( location ) );
			}
		};

		getGeoLocationTask.execute( location );
	}


	/**
	 * Given a descriptive place name, use geocoding to find the actual location.
	 */
	private void lookupLocationFromName( final ZmanimLocation place ) {

		if ( place.getLocationName().trim().length() == 0 ) {
			eventBus.fire( new RawLocationAcquiredEvent( place, new LocationError( LocationError.Code.BAD_INPUT, "Empty place!" ) ) );
		}

		AsyncTask<ZmanimLocation, Void, ZmanimLocation> lookupPlaceTask = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground( ZmanimLocation... places ) {

				// In case there's an issue, we'll just return the original place. It won't have a real name, but it'll
				//  still be Good Enough.
				ZmanimLocation updatedLocation = place;

				try {
					List<Address> addresses = geocoder.getFromLocationName( places[ 0 ].getLocationName(), 5 );

					if ( ( null != addresses ) && !addresses.isEmpty() ) {
						final Address address = addresses.get( 0 );
						updatedLocation = new ZmanimLocation.Builder()
							.resolvedTo( address )
							.inTimeZone( places[0].getTimeZone() )
							.build();
					}
				}
				catch ( IOException ioe ) {
					// Docs for Geocoder.getFromLocationName say this happens when there's no network connectivity, or
					//  there's some other I/O error. In any case, it can happen...
				}
				catch ( Exception e ) {
					LogUtils.logError( "SLP::updateLocationFromPlace", "", e );
				}

				return updatedLocation;
			}


			@Override
			protected void onPostExecute( ZmanimLocation location ) {
				super.onPostExecute( location );
				eventBus.fire( new FullLocationAcquiredEvent( location ) );
			}
		};

		lookupPlaceTask.execute( place );
	}

} // End of LocationToAddressConverter class
