/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.golden;

import com.gindin.zmanlib.zman.ZmanimFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Compares the time of every zman option of every zman type, for a matrix of places and dates, against a table
 *  produced by the released implementation. See the README next to the table.
 */
public class UT_ZmanimGoldenTable {

  private static final String                               TABLE_RESOURCE = "/golden/zmanim-golden.tsv";

  /** How many individual differences a failure message spells out. */
  private static final int                                  MAX_REPORTED_DIFFERENCES = 25;

  private static final int                                  KEY_COLUMNS = 3;


  @AfterEach
  public void restoreDefaultElevationSetting() {
    ZmanimFactory.useElevation( false );
  }


  @Test
  public void everyZmanOptionIsPresentInTheSameOrderAsInTheTable() throws IOException {

    List<String> expected = Arrays.asList( loadTable().get( 0 ) );
    List<String> actual = new ArrayList<>( Arrays.asList( ZmanimGoldenTable.headerLine().split( "\t", -1 ) ) );

    Set<String> missing = new LinkedHashSet<>( expected );
    missing.removeAll( actual );
    Set<String> unexpected = new LinkedHashSet<>( actual );
    unexpected.removeAll( expected );

    Assertions.assertEquals( "[]", missing.toString(), "Zman options in the table but not offered any more" );
    Assertions.assertEquals( "[]", unexpected.toString(), "Zman options offered but not in the table" );
    Assertions.assertEquals( expected, actual, "Same zman options, but listed in a different order" );
  }


  @Test
  public void theTableCoversEveryPlaceAndDateOfTheMatrix() throws IOException {

    List<String[]> rows = loadTable();

    List<String> expectedKeys = new ArrayList<>();
    for ( ZmanimGoldenTable.Place place : ZmanimGoldenTable.places() ) {
      for ( boolean useElevation : ZmanimGoldenTable.elevationSettings( place ) ) {
        for ( int[] date : place.dates ) {
          expectedKeys.add( place.name + "/" + ( useElevation ? "on" : "off" ) + "/" + ZmanimGoldenTable.formatDate( date ) );
        }
      }
    }

    List<String> actualKeys = new ArrayList<>();
    for ( String[] row : rows.subList( 1, rows.size() ) ) {
      actualKeys.add( row[ 0 ] + "/" + row[ 1 ] + "/" + row[ 2 ] );
    }

    Assertions.assertEquals( expectedKeys, actualKeys );
  }


  @Test
  public void everyZmanTimeMatchesTheTable() throws IOException {

    List<String[]> rows = loadTable();
    String[] header = rows.get( 0 );

    List<String> differences = new ArrayList<>();
    int checked = 0;

    for ( String[] row : rows.subList( 1, rows.size() ) ) {

      ZmanimGoldenTable.Place place = findPlace( row[ 0 ] );
      Map<String, String> actualRow = ZmanimGoldenTable.computeRow(
        place, ZmanimGoldenTable.parseDate( row[ 2 ] ), "on".equals( row[ 1 ] ) );

      for ( int i = KEY_COLUMNS; i < header.length; i++ ) {

        String expectedCell = row[ i ];
        String actualCell = actualRow.get( header[ i ] );
        checked++;

        if ( !expectedCell.equals( actualCell ) ) {
          differences.add( describe( row, header[ i ], expectedCell, actualCell ) );
        }
      }
    }

    Assertions.assertTrue( checked > 0 );
    Assertions.assertTrue( differences.isEmpty(),
      differences.size() + " of " + checked + " values differ from the table; first "
        + Math.min( differences.size(), MAX_REPORTED_DIFFERENCES ) + ":\n"
        + String.join( "\n", differences.subList( 0, Math.min( differences.size(), MAX_REPORTED_DIFFERENCES ) ) ) );
  }


  @Test
  public void theTableIsNotTrivial() throws IOException {

    List<String[]> rows = loadTable();
    String[] header = rows.get( 0 );
    List<String[]> data = rows.subList( 1, rows.size() );

    Assertions.assertTrue( data.size() >= 80, "rows: " + data.size() );
    Assertions.assertTrue( header.length - KEY_COLUMNS - 1 >= 200, "zman columns: " + ( header.length - KEY_COLUMNS - 1 ) );

    Set<String> places = new HashSet<>();
    Set<String> dates = new HashSet<>();
    int nullCells = 0;
    int timeCells = 0;
    int tromsoNullCells = 0;
    for ( String[] row : data ) {
      places.add( row[ 0 ] );
      dates.add( row[ 2 ] );
      for ( int i = KEY_COLUMNS; i < header.length - 1; i++ ) {
        if ( ZmanimGoldenTable.NO_TIME.equals( row[ i ] ) ) {
          nullCells++;
          if ( "Tromso".equals( row[ 0 ] ) ) {
            tromsoNullCells++;
          }
        }
        else {
          timeCells++;
          Long.parseLong( row[ i ] );
        }
      }
    }

    Assertions.assertTrue( places.size() >= 6, "places: " + places );
    Assertions.assertTrue( dates.size() >= 10, "dates: " + dates );
    Assertions.assertTrue( nullCells >= 100, "cells without a time: " + nullCells );
    Assertions.assertTrue( tromsoNullCells >= 100, "Tromso cells without a time: " + tromsoNullCells );
    Assertions.assertTrue( timeCells >= 10000, "cells with a time: " + timeCells );
  }


  @Test
  public void applyingElevationChangesTheShemaTimeOfAHighPlaceInTheTable() throws IOException {

    List<String[]> rows = loadTable();
    int shema = Arrays.asList( rows.get( 0 ) ).indexOf( "SofZmanShema|GR'A|false" );
    Assertions.assertTrue( shema >= 0, "GR'A Sof Zman Shema column" );

    int compared = 0;
    for ( String[] off : rows.subList( 1, rows.size() ) ) {
      if ( !"off".equals( off[ 1 ] ) ) {
        continue;
      }
      for ( String[] on : rows.subList( 1, rows.size() ) ) {
        if ( on[ 0 ].equals( off[ 0 ] ) && on[ 2 ].equals( off[ 2 ] ) && "on".equals( on[ 1 ] ) ) {
          compared++;
          Assertions.assertNotEquals( off[ shema ], on[ shema ],
            off[ 0 ] + " " + off[ 2 ] + ": the Shema time should move when elevation is applied" );
        }
      }
    }

    Assertions.assertTrue( compared >= 10, "elevation on/off pairs: " + compared );
  }


  private static ZmanimGoldenTable.Place findPlace( String name ) {

    for ( ZmanimGoldenTable.Place place : ZmanimGoldenTable.places() ) {
      if ( place.name.equals( name ) ) {
        return place;
      }
    }

    throw new AssertionError( "Unknown place in the table: " + name );
  }


  private static String describe(
    String[]  row,
    String    column,
    String    expected,
    String    actual
  ) {

    String[] parts = column.split( "\\|", -1 );
    String what = ZmanimGoldenTable.LISTED_COLUMN.equals( column )
      ? "zman types listed for the day"
      : parts[ 0 ] + " / " + parts[ 1 ];

    return row[ 0 ] + ", " + row[ 2 ] + ", elevation " + row[ 1 ] + ", " + what
      + ": expected " + expected + " but was " + actual;
  }


  private static List<String[]> loadTable() throws IOException {

    List<String[]> rows = new ArrayList<>();
    try ( InputStream in = UT_ZmanimGoldenTable.class.getResourceAsStream( TABLE_RESOURCE ) ) {

      Assertions.assertNotNull( in, "Missing resource " + TABLE_RESOURCE );

      BufferedReader reader = new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ) );
      String line;
      while ( ( line = reader.readLine() ) != null ) {
        if ( !line.isEmpty() ) {
          rows.add( line.split( "\t", -1 ) );
        }
      }
    }

    return rows;
  }
}
