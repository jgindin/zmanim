package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanlib.location.ZmanLocationJSON;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.Context;
import android.support.annotation.NonNull;

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

  private final EventBus                                    eventBus;

  private final LocationAcquiredEvent.Handler               locationAcquiredHandler;

  final LRULocationCache                                    locationCache = new LRULocationCache();


  CachedLocationResolver(
    Context   context,
    EventBus  eventBus,
    IOFactory ioFactory
  ) {
    super( context );
    this.eventBus = eventBus;
    this.ioFactory = ioFactory;

    locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
      @Override
      public void onLocationAcquired(
        ZmanimLocation newLocation
      ) {
        // This event is fired when (a) a location is acquired and (b) when a location is resolved.
        //  We don't want to cache unresolved locations...there's no point.
        if ( !newLocation.isResolved() ) {
          return;
        }

        cacheLocation( newLocation );
      }


      @Override
      public void onError(
        @NotNull  LocationError   error,
        @Nullable ZmanimLocation  location
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
  protected ZmanimLocation resolveFromCoordinates( @NotNull ZmanimLocation zmanimLocation ) {

    return findCachedMatch( zmanimLocation );
  }


  @Nullable
  @Override
  protected ZmanimLocation resolveFromName( @NotNull ZmanimLocation zmanimLocation ) {

    return findCachedMatch( zmanimLocation );
  }


  @Nullable
  private ZmanimLocation findCachedMatch( ZmanimLocation zmanimLocation ) {

    // Take a copy to avoid any possible ConcurrentModificationException.
    Set<ZmanimLocation> copy;
    synchronized ( locationCache ) {
      copy = locationCache.newCopy();
    }

    for ( ZmanimLocation cachedLocation : copy ) {

      if ( zmanimLocation.getLocationName().equals( cachedLocation.originalUserName ) ) {
        // This is the case where the user has told us the name of their location and we've gone
        //  and looked it up. The name the user gave us will be the location name in what's passed in, but stored
        //  as the original user name in the resolved location.
        return cachedLocation;
      }

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

          // Sync on the entire load process just in case we wind up getting a save() operation
          //  while we're trying to load!
          synchronized ( locationCache ) {

            final Set<ZmanimLocation> copy = new HashSet<ZmanimLocation>();
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
        catch ( EOFException eof ) {
          // Something happened to cause the cache to be malformed.
          wipeCache();
        }
        catch ( Exception e ) {
          if ( e.getMessage() != null ) {
            LogUtils.logError( "CLR::loadCache", e.getMessage(), e );
          }
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


  void wipeCache() {

    synchronized ( locationCache ) {
      locationCache.clear();
    }

    saveCache();
  }


  private void cacheLocation( ZmanimLocation newLocation ) {

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

    Runnable saveCache = new Runnable() {
      @Override
      public void run() {

        FileOutputStream fos = null;
        DataOutputStream output = null;
        try {

          // Synchronize the whole writing process so we can't conflict with a load...
          final Set<ZmanimLocation> copy;
          synchronized ( locationCache ) {
            copy = locationCache.newCopy();
          }

          fos = ioFactory.openOutput();
          output = new DataOutputStream( new BufferedOutputStream( fos ) );
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
      protected boolean removeEldestEntry( Map.Entry<ZmanimLocation,ZmanimLocation> eldest ) {
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
      Set<ZmanimLocation> copy = new HashSet<ZmanimLocation>();

      for ( ZmanimLocation location : cache.keySet() ) {
        copy.add( location );
      }

      return copy;
    }
  }

}
