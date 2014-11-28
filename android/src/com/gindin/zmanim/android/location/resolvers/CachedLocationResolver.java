package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.util.LogUtils;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This resolver listens for for other resolvers to do their work and caches the results. It is intended that this will
 *  be the first of the resolvers in the chain so that in case we've already gotten a request for the location then
 *  nothing has to be done...we just pull it out of the cache.
 */
class CachedLocationResolver
	extends LocationResolver {


	/**
	 * Abstracts the input / output file opening so that this class does not have to depend on any Android specifics. The
	 *  requester of these files is responsible for ensuring the streams are properly closed.
	 */
	interface IOFactory {

		/** Obtain an input stream for reading the cached entries. */
		InputStream openInput() throws FileNotFoundException;

		/** Obtain an output stream for writing a new set of cached entries. */
		OutputStream openOutput() throws FileNotFoundException;

		/** The actual loading and saving must be done on a background thread. */
		void runInBackground( Runnable runnable );
	}



	private final IOFactory                                   ioFactory;

	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

	final Set<ZmanimLocation>                                 locationCache = new HashSet<ZmanimLocation>();


	CachedLocationResolver(
		IOFactory         ioFactory,
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );
		this.ioFactory = ioFactory;

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired( ZmanimLocation newLocation ) {
				// This event is fired when (a) a location is acquired and (b) when a location is resolved.
				//  We don't want to cache unresolved locations...there's no point.
				if ( !newLocation.isResolved() ) {
					return;
				}

				saveCache( newLocation );
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


	void loadCache() {

		Runnable loadCache = new Runnable() {
			@Override
			public void run() {

				DataInputStream input = null;
				try {

					final Set<ZmanimLocation> copy = new HashSet<ZmanimLocation>();
					input = new DataInputStream( new BufferedInputStream( ioFactory.openInput() ) );

					int numLocations = input.readInt();
					for ( int i = 0; i < numLocations; i++ ) {
						copy.add( ZmanimLocation.load( input ) );
					}

					synchronized ( locationCache ) {
						locationCache.addAll( copy );
					}
				}
				catch ( Exception e ) {
					LogUtils.logError( "CLR::saveCache", e.getMessage(), e );
				}
				finally {
					if ( input != null ) {
						try {
							input.close();
						}
						catch ( IOException e ) {
							// to bad...
						}
					}
				}
			}
		};

		ioFactory.runInBackground( loadCache );
	}


	private void saveCache( ZmanimLocation newLocation ) {

		synchronized ( locationCache ) {

			// Make sure this new location is a significant change from all the other cache entries.
			for ( ZmanimLocation location : locationCache ) {
				if ( !newLocation.isSignificantChangeFrom( location ) ) {
					return;
				}
			}

			locationCache.add( newLocation );
		}

		Runnable saveCache = new Runnable() {
			@Override
			public void run() {

				final Set<ZmanimLocation> copy = new HashSet<ZmanimLocation>();
				synchronized ( locationCache ) {
					copy.addAll( locationCache );
				}

				DataOutputStream output = null;
				try {
					output = new DataOutputStream( new BufferedOutputStream( ioFactory.openOutput() ) );

					output.writeInt( copy.size() );

					for ( ZmanimLocation location : copy ) {
						location.save( output );
					}
				}
				catch ( Exception e ) {
					LogUtils.logError( "CLR::saveCache", e.getMessage(), e );
				}
				finally {
					if ( output != null ) {
						try {
							output.close();
						}
						catch ( IOException e ) {
							// Oh well.
						}
					}
				}
			}
		};

		ioFactory.runInBackground( saveCache );
	}

}
