package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_CachedLocationResolver {

  private CachedLocationResolver                            cachedLocationResolver;

  private TestIOFactory                                     ioFactory;


  @BeforeEach
  public void setup() {

    ioFactory = new TestIOFactory();

    cachedLocationResolver = new CachedLocationResolver( ioFactory );
  }


  @Test
  public void useCachedLocation() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );

    ZmanimLocation location2 = new ZmanimLocation.Builder()
      .atLatitude( 47.55 )
      .atLongitude( -122.27 )
      .locationName( "PCC" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    ZmanimLocation resolvedLocation = cachedLocationResolver.resolveFromCoordinates( location2 );
    Assertions.assertEquals( location, resolvedLocation );
  }


  @Test
  public void cachedLocationIsTooFarAway() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );

    ZmanimLocation location2 = new ZmanimLocation.Builder()
      .atLatitude( 47.56 )
      .atLongitude( -122.278 )
      .locationName( "Genesse Park" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    // The caching resolver won't know about this other location, so it'll return null.
    ZmanimLocation resolvedLocation = cachedLocationResolver.resolveFromCoordinates( location2 );
    Assertions.assertNull( resolvedLocation );
  }


  @Test
  public void dontUseUnresolvedLocation() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );
    Assertions.assertEquals( 0, cachedLocationResolver.locationCache.size() );

  }


  @Test
  public void loadFromCache() throws IOException {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );

    cachedLocationResolver.locationCache.clear();

    ioFactory.prepInput( ioFactory.outputFile );

    cachedLocationResolver.loadCache();
    Assertions.assertEquals( 1, cachedLocationResolver.locationCache.size() );

    ZmanimLocation cachedLocation = cachedLocationResolver.locationCache.iterator().next();
    Assertions.assertEquals( location, cachedLocation );
  }


  @Test
  public void dontCacheSignificantlyTheSameLocation() {

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );

    ZmanimLocation location2 = new ZmanimLocation.Builder()
      .atLatitude( 47.55 )
      .atLongitude( -122.27 )
      .locationName( "PCC" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location2 );

    Assertions.assertEquals( 1, cachedLocationResolver.locationCache.size() );
  }


  @Test
  public void lruTest() {

    ZmanimLocation firstLocation = new ZmanimLocation.Builder()
      .atLatitude( 47.552827 )
      .atLongitude( -122.267065 )
      .locationName( "Ezra Bessaroth" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( firstLocation );

    for ( int i = 0; i < 25; i++ ) {
      ZmanimLocation location = new ZmanimLocation.Builder()
        .atLatitude( firstLocation.getLatitude() + i )
        .atLongitude( firstLocation.getLongitude() + i )
        .locationName( firstLocation.getLocationName() + "_" + i )
        .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
        .build();

      cachedLocationResolver.cache( location );
    }

    Assertions.assertEquals( 25, cachedLocationResolver.locationCache.size() );

    ZmanimLocation location = new ZmanimLocation.Builder()
      .atLatitude( firstLocation.getLatitude() + 25 )
      .atLongitude( firstLocation.getLongitude() + 25 )
      .locationName( firstLocation.getLocationName() + "_25" )
      .inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
      .build();

    cachedLocationResolver.cache( location );

    Assertions.assertEquals( 25, cachedLocationResolver.locationCache.size() );
    Assertions.assertFalse( cachedLocationResolver.locationCache.contains( firstLocation ) );
  }




  private static class TestIOFactory
    implements CachedLocationResolver.IOFactory {

    private File                                            inputFile;
    FileInputStream                                         input;

    private File                                            outputFile;
    FileOutputStream                                        output;


    void prepInput( File prepFrom ) throws IOException {

      input = new FileInputStream( prepFrom );
      openOutput();
    }


    @Override
    public InputStream openInput() throws IOException {

      // Normally, when this is called `input` will be null; unit tests, however, may instead
      //  invoke `prepInput`...
      if ( input == null ) {
        inputFile = File.createTempFile( "ut_cached_location_", "input" );
        inputFile.deleteOnExit();
        input = new FileInputStream( inputFile );
      }

      return input;
    }


    @Override
    public FileOutputStream openOutput() throws IOException {

      outputFile = File.createTempFile( "ut_cached_location_", "output" );
      outputFile.deleteOnExit();
      output = new FileOutputStream( outputFile );

      return output;
    }


    @Override
    public void run( Runnable runnable ) {
      runnable.run();
    }

  }
}
