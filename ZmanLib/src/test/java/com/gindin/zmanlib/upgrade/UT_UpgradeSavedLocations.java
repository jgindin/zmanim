/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.upgrade;

import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Reads the location files written by the released app: {@code LastKnownLocationProvider} is a single
 *  {@code writeUTF} JSON record, and {@code CachedLocations} is a count followed by that many records.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_UpgradeSavedLocations {

  private static final double                               PRECISION = 1e-9;

  private Locale                                            savedLocale;


  @BeforeEach
  public void pinLocale() {

    // The name of a location without one is formatted with the default locale's number formatting.
    savedLocale = Locale.getDefault();
    Locale.setDefault( Locale.US );
  }


  @AfterEach
  public void restoreLocale() {
    Locale.setDefault( savedLocale );
  }


  @Test
  public void lastKnownLocationIsReadFromTheFramedRecord() throws IOException {

    try ( FileInputStream input = new FileInputStream( UpgradeFixtures.file( "files/LastKnownLocationProvider" ) ) ) {

      ZmanimLocation location = ZmanimLocation.load( input );

      Assertions.assertFalse( location.isInvalid );
      Assertions.assertEquals( 42.6526, location.getLatitude(), PRECISION );
      Assertions.assertEquals( -73.7562, location.getLongitude(), PRECISION );
      Assertions.assertEquals( 47.0, location.getElevation(), PRECISION );
      Assertions.assertEquals( TimeZone.getTimeZone( "America/New_York" ), location.getTimeZone() );
      Assertions.assertEquals( "fused", location.providerName );
      Assertions.assertEquals( 23.456f, location.accuracy, 1e-4f );

      // A raw fix has no name, and this record has no user name, which is read as empty.
      Assertions.assertFalse( location.hasName() );
      Assertions.assertEquals( "42.7 lat; -73.8 long", location.getLocationName() );
      Assertions.assertEquals( "", location.originalUserName );

      // The record is the whole file.
      Assertions.assertEquals( -1, input.read() );
    }
  }


  @Test
  public void cachedLocationsAreReadFromTheCountedRecords() throws IOException {

    Map<String, ZmanimLocation> byName = new HashMap<>();

    try ( DataInputStream input = new DataInputStream( new FileInputStream( UpgradeFixtures.file( "files/CachedLocations" ) ) ) ) {

      int count = input.readInt();
      Assertions.assertEquals( 4, count );

      for ( int i = 0; i < count; i++ ) {
        ZmanimLocation location = ZmanimLocation.load( input );
        byName.put( location.getLocationName(), location );
      }

      Assertions.assertThrows( EOFException.class, input::readByte, "the counted records are the whole file" );
    }

    Assertions.assertEquals( 4, byName.size() );

    // The oldest records have no user name at all.
    ZmanimLocation albany = byName.get( "Albany, New York" );
    Assertions.assertEquals( 42.6526, albany.getLatitude(), PRECISION );
    Assertions.assertEquals( -73.7562, albany.getLongitude(), PRECISION );
    Assertions.assertEquals( 47.0, albany.getElevation(), PRECISION );
    Assertions.assertEquals( TimeZone.getTimeZone( "America/New_York" ), albany.getTimeZone() );
    Assertions.assertEquals( "Zmanim", albany.providerName );
    Assertions.assertEquals( -1f, albany.accuracy );
    Assertions.assertEquals( "", albany.originalUserName );
    Assertions.assertTrue( albany.isResolved() );

    ZmanimLocation saratogaSprings = byName.get( "Saratoga Springs, New York" );
    Assertions.assertEquals( 43.0831, saratogaSprings.getLatitude(), PRECISION );
    Assertions.assertEquals( -73.7846, saratogaSprings.getLongitude(), PRECISION );
    Assertions.assertEquals( 94.0, saratogaSprings.getElevation(), PRECISION );
    Assertions.assertEquals( "Saratoga Springs, NY", saratogaSprings.originalUserName );

    ZmanimLocation jerusalem = byName.get( "Jerusalem, Israel" );
    Assertions.assertEquals( 31.7683, jerusalem.getLatitude(), PRECISION );
    Assertions.assertEquals( 35.2137, jerusalem.getLongitude(), PRECISION );
    Assertions.assertEquals( 754.0, jerusalem.getElevation(), PRECISION );
    Assertions.assertEquals( TimeZone.getTimeZone( "Asia/Jerusalem" ), jerusalem.getTimeZone() );
    Assertions.assertEquals( "network", jerusalem.providerName );
    Assertions.assertEquals( 18.25f, jerusalem.accuracy );
    Assertions.assertEquals( "", jerusalem.originalUserName );

    ZmanimLocation newYork = byName.get( "New York, New York" );
    Assertions.assertEquals( 40.7128, newYork.getLatitude(), PRECISION );
    Assertions.assertEquals( -74.006, newYork.getLongitude(), PRECISION );
    Assertions.assertEquals( 10.0, newYork.getElevation(), PRECISION );
    Assertions.assertEquals( 65.5f, newYork.accuracy );
    Assertions.assertEquals( "", newYork.originalUserName );
  }

}
