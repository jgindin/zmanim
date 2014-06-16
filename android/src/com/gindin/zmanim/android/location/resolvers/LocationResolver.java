/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.BaseAsyncClass;
import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.FullLocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RawLocationAcquiredEvent;
import com.gindin.zmanim.location.ZmanimLocation;

import java.util.Arrays;
import java.util.List;

/**
 * Uses the information in either a RawLocationAcquiredEvent or PlaceAcquiredEvent to fill out the full set of
 *  data about a given location.
 *<p/>
 *
 * After resolving the location, a FullLocationAcquiredEvent will be fired.
 */
abstract class LocationResolver {


	private final EventBus                                  eventBus;


	protected LocationResolver( EventBus eventBus ) {
		this.eventBus = eventBus;
	}


	public final void resolve(
		ZmanimLocation          location,
		List<LocationResolver>  nextResolvers
	) {

		if ( location == null ) {
			return;
		}

		if ( location.hasCoordinates() ) {
			resolveFromCoordinates( location, nextResolvers );
		}
		else {
			resolveFromName( location, nextResolvers );
		}
	}


	/**
	 * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
	 *  specified location using the latitude and longitude, and return a new location with all possible details filled out.
	 *
	 * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
	 */
	protected abstract ZmanimLocation resolveFromCoordinatesInBackground( ZmanimLocation zmanimLocation );

	/**
	 * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
	 *  specified location using the name, and return a new location with all possible details fillled out.
	 *
	 * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
	 */
	protected abstract ZmanimLocation resolveFromNameInBackground( ZmanimLocation zmanimLocation );


	/**
	 * Given a location which MUST have the latitude and longitude set, use reverse geocoding to find the
	 *  name of that location. Upon completion, the location is updated and the FullLocationAcquiredEvent
	 *  will be fired.
	 */
	private void resolveFromCoordinates(
		final ZmanimLocation          location,
		final List<LocationResolver>  nextResolvers
	) {

		BaseAsyncClass.Callbacks<ZmanimLocation, Void, ZmanimLocation> callbacks = new BaseAsyncClass.Callbacks<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground(
				BaseAsyncClass.ProgressPublisher<Void> publisher,
				ZmanimLocation...                   zmanimLocations
			) {
				return resolveFromCoordinatesInBackground( zmanimLocations[0] );
			}


			@Override
			protected void onPostExecute( ZmanimLocation resolvedLocation ) {
				handlePostExecute( location, resolvedLocation, nextResolvers );
			}
		};

		BaseAsyncClass<ZmanimLocation,Void,ZmanimLocation> async = new BaseAsyncClass<ZmanimLocation, Void, ZmanimLocation>( callbacks );
		async.execute( location );
	}


	/**
	 * Given a descriptive place name, use geocoding to find the actual location.
	 */
	private void resolveFromName(
		final ZmanimLocation          location,
		final List<LocationResolver>  nextResolvers
	) {

		if ( location.getLocationName().trim().length() == 0 ) {
			eventBus.fire( new RawLocationAcquiredEvent( location, new LocationError( LocationError.Code.BAD_INPUT, "Empty place!" ) ) );
		}

		BaseAsyncClass.Callbacks<ZmanimLocation,Void,ZmanimLocation> callbacks = new BaseAsyncClass.Callbacks<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground(
				BaseAsyncClass.ProgressPublisher<Void> publisher,
				ZmanimLocation...                   zmanimLocations
			) {
				return resolveFromNameInBackground( zmanimLocations[0] );
			}

			@Override
			protected void onPostExecute( ZmanimLocation resolvedLocation ) {
				handlePostExecute( location, resolvedLocation, nextResolvers );
			}
		};

		BaseAsyncClass<ZmanimLocation,Void,ZmanimLocation> async = new BaseAsyncClass<ZmanimLocation, Void, ZmanimLocation>( callbacks );
		async.execute( location );
	}


	private void handlePostExecute(
		ZmanimLocation                originalLocation,
		ZmanimLocation                resolvedLocation,
		final List<LocationResolver>  nextResolvers
	) {

		if ( ( resolvedLocation == null ) && !nextResolvers.isEmpty() ) {
			nextResolvers.get( 0 ).resolve( originalLocation, nextResolvers.subList( 1, nextResolvers.size() ) );
		}
		else {
			eventBus.fire( new FullLocationAcquiredEvent( resolvedLocation ) );

			// Let's keep track of how often we use each resolver.
			LogUtils.logEvent( "LocationResolved", Arrays.asList( new Pair<String, String>( "Resolver", getClass().getSimpleName() ) ) );
		}
	}


} // End of LocationResolver class
