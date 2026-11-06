/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.TimeZone;

/**
 * Which cached entry answers a lookup: an entry matches the name the user typed for it, or a request within 1 km and
 *  5 m of elevation in the same time zone. Tests named {@code ...CurrentBehavior} pin behavior that looks wrong; it is
 *  kept as it is until a later change fixes it on purpose.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_CachedLocationMatching {

  private static final TimeZone                             NEW_YORK = TimeZone.getTimeZone( "America/New_York" );

  private static final TimeZone                             CHICAGO = TimeZone.getTimeZone( "America/Chicago" );

  @TempDir
  File                                                      cacheDir;

  private CachedLocationResolver                            resolver;


  @BeforeEach
  public void setup() {

    resolver = new CachedLocationResolver( new CachedLocationResolver.IOFactory() {
      @Override
      public InputStream openInput() throws FileNotFoundException {
        throw new FileNotFoundException( "no cache yet" );
      }

      @Override
      public FileOutputStream openOutput() throws FileNotFoundException {
        return new FileOutputStream( new File( cacheDir, "CachedLocations" ) );
      }

      @Override
      public void run( Runnable runnable ) {
        runnable.run();
      }
    } );
  }


  /** What the app caches after looking up a place the user typed. */
  private static ZmanimLocation.Builder saratogaSprings() {
    return new ZmanimLocation.Builder()
      .locationName( "Saratoga Springs, New York" )
      .userName( "Saratoga Springs, NY" )
      .atLatitude( 43.08 )
      .atLongitude( -73.78 )
      .atElevation( 93 )
      .inTimeZone( NEW_YORK );
  }


  /** What the static acquirer asks for when the user typed a place. */
  private static ZmanimLocation typed( String name, TimeZone timeZone ) {
    return new ZmanimLocation.Builder()
      .locationName( name )
      .userName( name )
      .inTimeZone( timeZone )
      .build();
  }


  @Test
  public void theNameTheUserTypedFindsTheEntry() {

    resolver.cache( saratogaSprings().build() );

    ZmanimLocation found = resolver.resolveFromName( typed( "Saratoga Springs, NY", NEW_YORK ) );

    Assertions.assertNotNull( found );
    Assertions.assertEquals( "Saratoga Springs, New York", found.getLocationName() );
  }


  @Test
  public void theTypedNameMustMatchExactly() {

    resolver.cache( saratogaSprings().build() );

    Assertions.assertNull( resolver.resolveFromName( typed( "saratoga springs, ny", NEW_YORK ) ) );
    Assertions.assertNull( resolver.resolveFromName( typed( "Saratoga Springs, New York", NEW_YORK ) ) );
  }


  @Test
  public void aHitByTypedNameTakesTheTimeZoneOfTheRequest() {

    resolver.cache( saratogaSprings().build() );

    ZmanimLocation found = resolver.resolveFromName( typed( "Saratoga Springs, NY", CHICAGO ) );

    Assertions.assertNotNull( found );
    Assertions.assertEquals( CHICAGO, found.getTimeZone() );
  }


  /** The time zone is set on the cached entry itself, so later lookups see it too. */
  @Test
  public void aHitByTypedNameChangesTheTimeZoneOfTheCachedEntryCurrentBehavior() {

    resolver.cache( saratogaSprings().build() );
    resolver.resolveFromName( typed( "Saratoga Springs, NY", CHICAGO ) );

    ZmanimLocation cached = resolver.locationCache.iterator().next();
    Assertions.assertEquals( CHICAGO, cached.getTimeZone() );
  }


  @Test
  public void nearbyCoordinatesFindTheEntry() {

    resolver.cache( saratogaSprings().build() );

    ZmanimLocation nearby = new ZmanimLocation.Builder()
      .atLatitude( 43.085 )
      .atLongitude( -73.78 )
      .atElevation( 95 )
      .inTimeZone( NEW_YORK )
      .build();

    ZmanimLocation found = resolver.resolveFromCoordinates( nearby );

    Assertions.assertNotNull( found );
    Assertions.assertEquals( "Saratoga Springs, New York", found.getLocationName() );
    Assertions.assertEquals( 43.08, found.getLatitude() );
  }


  /**
   * A custom time zone is a significant change, so nearby coordinates in another time zone miss the cache, and the
   *  time zone override is only ever applied to a hit by typed name.
   */
  @Test
  public void nearbyCoordinatesInAnotherTimeZoneMissTheEntry() {

    resolver.cache( saratogaSprings().build() );

    ZmanimLocation nearby = new ZmanimLocation.Builder()
      .atLatitude( 43.08 )
      .atLongitude( -73.78 )
      .atElevation( 93 )
      .inTimeZone( CHICAGO )
      .build();

    Assertions.assertNull( resolver.resolveFromCoordinates( nearby ) );
  }


  @Test
  public void nearbyCoordinatesMoreThanFiveMetersHigherMissTheEntry() {

    resolver.cache( saratogaSprings().build() );

    ZmanimLocation higher = new ZmanimLocation.Builder()
      .atLatitude( 43.08 )
      .atLongitude( -73.78 )
      .atElevation( 99 )
      .inTimeZone( NEW_YORK )
      .build();

    Assertions.assertNull( resolver.resolveFromCoordinates( higher ) );
  }


  @Test
  public void aLocationThatIsNotResolvedIsNotCached() {

    resolver.cache( typed( "Saratoga Springs, NY", NEW_YORK ) );
    resolver.cache( null );

    Assertions.assertEquals( 0, resolver.locationCache.size() );
  }

}  // End of UT_CachedLocationMatching class
