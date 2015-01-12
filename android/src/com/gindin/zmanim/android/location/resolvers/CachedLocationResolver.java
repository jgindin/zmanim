package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.SharedPreferences;

import java.io.*;
import java.util.*;

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
		InputStream openInput() throws IOException;

		/** Obtain an output stream for writing a new set of cached entries. */
		FileOutputStream openOutput() throws IOException;

		/** The actual loading and saving must be done on a background thread. */
		void runInBackground( Runnable runnable );
	}



	private final IOFactory                                   ioFactory;

	private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

	final LRULocationCache                                    locationCache = new LRULocationCache();


	CachedLocationResolver(
		IOFactory         ioFactory,
		EventBus          eventBus,
		SharedPreferences personalPrefs
	) {
		super( eventBus, personalPrefs );
		this.ioFactory = ioFactory;

		locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
			@Override
			public void onLocationAcquired(
				ZmanimLocation  newLocation,
				final boolean   isCachedLocation
			) {
				// This event is fired when (a) a location is acquired and (b) when a location is resolved.
				//  We don't want to cache unresolved locations...there's no point.
				if ( !newLocation.isResolved() ) {
					return;
				}

				// No sense saving the location if it was a cached location to begin with...
				if ( !isCachedLocation ) {
					saveCache( newLocation );
				}
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

		// Take a copy to avoid any possible ConcurrentModificationException.
		Set<ZmanimLocation> copy = locationCache.newCopy();

		for ( ZmanimLocation cachedLocation : copy ) {
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

				final Set<ZmanimLocation> copy = locationCache.newCopy();

				FileOutputStream fos = null;
				DataOutputStream output = null;
				try {
					fos = ioFactory.openOutput();
					output = new DataOutputStream( new BufferedOutputStream( fos ) );

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

							// According to The Busy Coder's Guide to Android, v6.2, we want to make sure that
							//  we're calling sync on the FOS.
							output.flush();
							fos.getFD().sync();
							output.close();
						}
						catch ( IOException e ) {
							// Oh well.
							LogUtils.logError( "CLR::saveCache (closing)", e.getMessage(), e );
						}
					}
				}
			}
		};

		ioFactory.runInBackground( saveCache );
	}


	/**
	 * NOTE: This cache does NOT fully implement the proper Set semantics. For example, add() will always return true.
	 */
	static class LRULocationCache
		implements Iterable<ZmanimLocation> {

		private static final int                                MAX_SIZE = 25;

		private final Map<ZmanimLocation,ZmanimLocation>        cache = new LinkedHashMap<ZmanimLocation, ZmanimLocation>( 16, 0.75f, true ) {
			@Override
			protected boolean removeEldestEntry( Entry<ZmanimLocation,ZmanimLocation> eldest ) {
				return size() > MAX_SIZE;
			}
		};


		public boolean add( ZmanimLocation zmanimLocation ) {

			cache.put( zmanimLocation, zmanimLocation );
			return true;
		}


		public boolean addAll( Collection<? extends ZmanimLocation> collection ) {
			for ( ZmanimLocation location : collection ) {
				add( location );
			}

			return true;
		}


		public void clear() {
			cache.clear();
		}


		public boolean contains( Object o ) {
			return cache.containsKey( o );
		}


		public boolean isEmpty() {
			return cache.isEmpty();
		}


		public Iterator<ZmanimLocation> iterator() {
			return cache.keySet().iterator();
		}


		public boolean remove( Object o ) {
			return null != cache.remove( o );
		}


		public int size() {
			return cache.size();
		}


		public Set<ZmanimLocation> newCopy() {
			Set<ZmanimLocation> copy = new HashSet<ZmanimLocation>();

			synchronized ( this ) {
				for ( ZmanimLocation location : cache.keySet() ) {
					copy.add( location );
				}
			}

			return copy;
		}
	}

}
