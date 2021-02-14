/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.zmanlib.location.ZmanLocationJSON;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;

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
    void run( Runnable runnable );
  }



  private final IOFactory                                   ioFactory;

  final LRULocationCache                                    locationCache = new LRULocationCache();


  CachedLocationResolver(
    IOFactory ioFactory
  ) {
    this.ioFactory = ioFactory;
  }


  @Nullable
  @Override
  protected ZmanimLocation resolveFromCoordinates( @NotNull ZmanimLocation zmanimLocation ) {

    return findCachedMatch( zmanimLocation );
  }


  @Nullable
  @Override
  protected ZmanimLocation resolveFromName( @NotNull ZmanimLocation zmanimLocation ) {

    return findCachedMatch( zmanimLocation );
  }


  @SuppressWarnings( "FeatureEnvy" )
  @Nullable
  private ZmanimLocation findCachedMatch( ZmanimLocation zmanimLocation ) {

    // Take a copy to avoid any possible ConcurrentModificationException.
    Set<ZmanimLocation> copy;
    synchronized ( locationCache ) {
      copy = locationCache.newCopy();
    }

    ZmanimLocation found = null;
    for ( ZmanimLocation cachedLocation : copy ) {

      if ( zmanimLocation.getLocationName().equals( cachedLocation.originalUserName ) ) {
        // This is the case where the user has told us the name of their location and we've gone
        //  and looked it up. The name the user gave us will be the location name in what's passed in, but stored
        //  as the original user name in the resolved location.
        found = cachedLocation;
        break;
      }

      if ( !zmanimLocation.isSignificantChangeFrom( cachedLocation ) && cachedLocation.hasName() ) {

        found = cachedLocation;
      }
    }

    // It's possible that the user has requested a custom time zone. So...we have to honor that.
    if ( ( found != null ) && !zmanimLocation.getTimeZone().equals( found.getTimeZone() ) ) {
      found.setTimeZone( zmanimLocation.getTimeZone() );
    }

    return found;
  }


  void loadCache() {

    DataInputStream input = null;
    try {

      // Sync on the entire load process just in case we wind up getting a save() operation
      //  while we're trying to load!
      synchronized ( locationCache ) {

        final Set<ZmanimLocation> copy = new HashSet<>();
        input = new DataInputStream( new BufferedInputStream( ioFactory.openInput() ) );

        StringBuilder cacheData = new StringBuilder();

        int numLocations = input.readInt();
        for ( int i = 0; i < numLocations; i++ ) {
          final ZmanimLocation location = ZmanimLocation.load( input );
          copy.add( location );
          cacheData.append( ZmanLocationJSON.toJSON( location ) );
        }

        locationCache.addAll( copy );
        LogUtils.setContext( "location-cache", cacheData.toString() );
      }
    }
    catch( FileNotFoundException fnfe ) {
      // This seems to happen when the cache file doesn't exist.
    }
    catch ( Exception e ) {
      if ( e.getMessage() != null ) {
        LogUtils.logError( "CLR::loadCache", e.getMessage(), e );
      }

      wipeCache();
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


  void wipeCache() {

    synchronized ( locationCache ) {
      locationCache.clear();
    }

    saveCache();
  }


  void cache( @Nullable ZmanimLocation newLocation ) {

    if ( ( newLocation == null ) || !newLocation.isResolved() ) {
      return;
    }

    synchronized ( locationCache ) {

      // Make sure this new location is a significant change from all the other cache entries.
      final ZmanimLocation cachedMatch = findCachedMatch( newLocation );
      if ( null != cachedMatch ) {
        return;
      }

      locationCache.add( newLocation );
    }

    saveCache();
  }


  private void saveCache() {

    Runnable saveCache = () -> {

      // Synchronize the whole writing process so we can't conflict with a load...
      final Set<ZmanimLocation> copy;
      synchronized ( locationCache ) {
        copy = locationCache.newCopy();
      }

      try ( DataOutputStream output = new DataOutputStream( new BufferedOutputStream( ioFactory.openOutput() ) ) ) {

        output.writeInt( copy.size() );

        StringBuilder cacheData = new StringBuilder();

        for ( ZmanimLocation location : copy ) {
          location.save( output );
          cacheData.append( ZmanLocationJSON.toJSON( location ) );
        }

        LogUtils.setContext( "location-cache", cacheData.toString() );
      }
      catch ( Exception e ) {
        LogUtils.logError( "CLR::saveCache", e.getMessage(), e );
      }
    };

    ioFactory.run( saveCache );
  }


  /**
   * NOTE: This cache does NOT fully implement the proper Set semantics. For example, add() will always return true.
   */
  static class LRULocationCache
    implements Iterable<ZmanimLocation> {

    private static final int                                MAX_SIZE = 25;

    private final Map<ZmanimLocation,ZmanimLocation>        cache = new LinkedHashMap<ZmanimLocation, ZmanimLocation>( 16, 0.75f, true ) {
      @Override
      protected boolean removeEldestEntry( Map.Entry<ZmanimLocation,ZmanimLocation> eldest ) {
        return size() > MAX_SIZE;
      }
    };


    public boolean add( ZmanimLocation zmanimLocation ) {

      cache.put( zmanimLocation, zmanimLocation );
      return true;
    }


    void addAll( Collection<? extends ZmanimLocation> collection ) {
      for ( ZmanimLocation location : collection ) {
        add( location );
      }
    }


    void clear() {
      cache.clear();
    }


    boolean contains( Object o ) {
      return cache.containsKey( o );
    }


    public boolean isEmpty() {
      return cache.isEmpty();
    }


    @NonNull
    public Iterator<ZmanimLocation> iterator() {
      return cache.keySet().iterator();
    }


    public boolean remove( Object o ) {
      return null != cache.remove( o );
    }


    public int size() {
      return cache.size();
    }


    Set<ZmanimLocation> newCopy() {
      return new HashSet<>( cache.keySet() );
    }
  }

}
