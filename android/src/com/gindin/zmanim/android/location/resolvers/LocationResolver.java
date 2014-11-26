/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.SharedPreferences;
import android.os.AsyncTask;

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


	protected final EventBus                                  eventBus;

	private final SharedPreferences                           personalPrefs;


	protected LocationResolver(
		EventBus          eventBus,
	  SharedPreferences personalPrefs
	) {
		this.eventBus = eventBus;
		this.personalPrefs = personalPrefs;
	}


	/**
	 * Called when the resolver is being destroyed.
	 */
	public abstract void onDestroy();


	public final void resolve(
		@NotNull ZmanimLocation          location,
		@NotNull List<LocationResolver>  nextResolvers
	) {

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
	@Nullable
	protected abstract ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation );

	/**
	 * This method, guaranteed to be run on a background thread, must perform whatever actions necessary to resolve the
	 *  specified location using the name, and return a new location with all possible details fillled out.
	 *
	 * @return An actual ZmanimLocation that has been properly resolved, or null if resolution failed.
	 */
	@Nullable
	protected abstract ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation );


	/**
	 * Given a location which MUST have the latitude and longitude set, use reverse geocoding to find the
	 *  name of that location. Upon completion, the location is updated and the FullLocationAcquiredEvent
	 *  will be fired.
	 */
	private void resolveFromCoordinates(
		@NotNull final ZmanimLocation          location,
		@NotNull final List<LocationResolver>  nextResolvers
	) {

		AsyncTask<ZmanimLocation, Void, ZmanimLocation> task = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground( final ZmanimLocation... zmanimLocations ) {
				return resolveFromCoordinatesInBackground( zmanimLocations[0] );
			}


			@Override
			protected void onPostExecute( final ZmanimLocation resolvedLocation ) {
				super.onPostExecute( resolvedLocation );
				handlePostExecute( location, resolvedLocation, nextResolvers );
			}
		};

		task.execute( location );
	}


	/**
	 * Given a descriptive place name, use geocoding to find the actual location.
	 */
	private void resolveFromName(
		@NotNull final ZmanimLocation          location,
		@NotNull final List<LocationResolver>  nextResolvers
	) {

		if ( location.getLocationName().trim().length() == 0 ) {
			eventBus.fire( new LocationAcquiredEvent( location, new LocationError( LocationError.Code.BAD_INPUT, "Empty place!" ) ) );
		}

		AsyncTask<ZmanimLocation,Void,ZmanimLocation> task = new AsyncTask<ZmanimLocation, Void, ZmanimLocation>() {
			@Override
			protected ZmanimLocation doInBackground( final ZmanimLocation... zmanimLocations ) {
				return resolveFromNameInBackground( zmanimLocations[0] );
			}


			@Override
			protected void onPostExecute( final ZmanimLocation resolvedLocation ) {
				super.onPostExecute( resolvedLocation );
				handlePostExecute( location, resolvedLocation, nextResolvers );
			}
		};

		task.execute( location );
	}


	private void handlePostExecute(
		@NotNull ZmanimLocation                originalLocation,
		@Nullable ZmanimLocation               resolvedLocation,
		@NotNull final List<LocationResolver>  nextResolvers
	) {

		if ( ( resolvedLocation == null ) && !nextResolvers.isEmpty() ) {
			nextResolvers.get( 0 ).resolve( originalLocation, nextResolvers.subList( 1, nextResolvers.size() ) );
		}
		else {

			if ( ( resolvedLocation == null ) || resolvedLocation.isInvalid || !resolvedLocation.isResolved() ) {
				// If we never got this location resolved, don't fire an event.
				LogUtils.logError( "LocationResolver", "No one resolved location!", null,
					new Pair<String, String>( "Location", ( resolvedLocation == null ? "<null>" : resolvedLocation.toShortString() ) ) );
			}
			else {

				boolean useElevation = personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
				if ( !useElevation ) {
					resolvedLocation.setElevation( 0.0 );
				}

				eventBus.fire( new LocationAcquiredEvent( resolvedLocation ) );

				// Let's keep track of how often we use each resolver.
				LogUtils.logEvent( "LocationResolved", Arrays.asList(
					new Pair<String, String>( "Resolver", getClass().getSimpleName() ) ) );
			}
		}
	}


} // End of LocationResolver class
