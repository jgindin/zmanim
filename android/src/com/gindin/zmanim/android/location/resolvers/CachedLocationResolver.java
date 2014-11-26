package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * This resolver listens for for other resolvers to do their work and caches the results. It is intended that this will
 *  be the first of the resolvers in the chain so that in case we've already gotten a request for the location then
 *  nothing has to be done...we just pull it out of the cache.
 */
class CachedLocationResolver
	extends LocationResolver {


	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

	final Set<ZmanimLocation>                                 locationCache = new HashSet<ZmanimLocation>();


	CachedLocationResolver(
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation location ) {
				// This event is fired when (a) a location is acquired and (b) when a location is resolved.
				//  We don't want to cache unresolved locations...there's no point.
				if ( !location.isResolved() ) {
					return;
				}

				locationCache.add( location );
			}


			@Override
			public void onError(
				@NotNull LocationError  error,
				@NotNull ZmanimLocation lastKnownLocation
			) {
				// ignore.
			}
		};

		eventBus.addHandler( locationAcquiredHandler );
	}


	@Override
	public void onDestroy() {

		eventBus.removeHandler( locationAcquiredHandler );
	}


	@Nullable
	@Override
	protected ZmanimLocation resolveFromCoordinatesInBackground( @NotNull ZmanimLocation zmanimLocation ) {

		return findCachedMatch( zmanimLocation );
	}


	@Nullable
	@Override
	protected ZmanimLocation resolveFromNameInBackground( @NotNull ZmanimLocation zmanimLocation ) {

		return findCachedMatch( zmanimLocation );
	}


	private ZmanimLocation findCachedMatch( ZmanimLocation zmanimLocation ) {

		for ( ZmanimLocation cachedLocation : locationCache ) {
			if ( !zmanimLocation.isSignificantChangeFrom( cachedLocation ) && cachedLocation.hasName() ) {
				return cachedLocation;
			}
		}

		return null;
	}


}
