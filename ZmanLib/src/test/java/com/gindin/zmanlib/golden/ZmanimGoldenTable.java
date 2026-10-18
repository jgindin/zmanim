/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanlib.golden;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import com.gindin.zmanlib.zman.ZmanimFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * The matrix of places and dates behind the zmanim golden table, and the computation of every zman for each
 *  cell of that matrix through the same entry points the app uses ({@link ZmanimFactory}).
 * <p>
 * Each table row is one place on one date with elevation either ignored or applied. After the three key columns
 *  come one column per zman option of every {@link Zmanim.Type}, in the order the app lists them, and a final
 *  column naming the types the app lists for that date. A zman value is the number of milliseconds between
 *  00:00 UTC of the row's date and the zman, or {@value #NO_TIME} where the zman cannot be calculated.
 * <p>
 * The same computation produced the committed table from the released implementation, so the code here must
 *  keep producing the layout the table has.
 */
public final class ZmanimGoldenTable {

  /** Marks a zman that has no time on a date, e.g., sunrise during the midnight sun. */
  public static final String                                NO_TIME = "-";

  public static final String                                LISTED_COLUMN = "listed";

  private static final long                                 MILLIS_PER_DAY = 24L * 60 * 60 * 1000;

  private static final String                               SEPARATOR = "\t";


  /** A place, and the dates used with it. */
  public static final class Place {

    public final String                                     name;

    public final ZmanimLocation                             location;

    /** True when {@link #location} has an elevation, so that applying elevation changes the results. */
    public final boolean                                    hasElevation;

    public final int[][]                                    dates;


    private Place(
      String    name,
      double    latitude,
      double    longitude,
      double    elevation,
      String    timeZone,
      int[][]   specificDates
    ) {

      this.name = name;
      this.hasElevation = elevation > 0;
      this.location = new ZmanimLocation.Builder()
        .locationName( name )
        .atLatitude( latitude )
        .atLongitude( longitude )
        .atElevation( elevation )
        .inTimeZone( TimeZone.getTimeZone( timeZone ) )
        .build();

      this.dates = new int[ COMMON_DATES.length + specificDates.length ][];
      System.arraycopy( COMMON_DATES, 0, this.dates, 0, COMMON_DATES.length );
      System.arraycopy( specificDates, 0, this.dates, COMMON_DATES.length, specificDates.length );
    }
  }


  /**
   * Dates used with every place: both solstices and equinoxes, Erev Pesach (a Wednesday, and one that falls on
   *  Shabbat), the first day of Pesach, Tisha B'Av, a Shabbat, and a Gregorian leap day. Each is {year, month, day}
   *  with a one-based month.
   */
  private static final int[][]                              COMMON_DATES = {
    { 2025, 4, 12 },
    { 2026, 3, 20 },
    { 2026, 4, 1 },
    { 2026, 4, 2 },
    { 2026, 6, 21 },
    { 2026, 7, 23 },
    { 2026, 9, 23 },
    { 2026, 11, 7 },
    { 2026, 12, 21 },
    { 2028, 2, 29 }
  };

  /** The days daylight saving time starts and ends in the United States. */
  private static final int[][]                              US_DST_DATES = { { 2026, 3, 8 }, { 2026, 11, 1 } };

  private static final List<Place>                          PLACES = new ArrayList<>();

  static {
    PLACES.add( new Place( "New York", 40.7128, -74.0060, 0, "America/New_York", US_DST_DATES ) );
    PLACES.add( new Place( "Jerusalem", 31.7683, 35.2137, 0, "Asia/Jerusalem",
      new int[][] { { 2026, 3, 27 }, { 2026, 10, 25 } } ) );
    PLACES.add( new Place( "Seattle", 47.6062, -122.3321, 0, "America/Los_Angeles", US_DST_DATES ) );
    PLACES.add( new Place( "Sydney", -33.8688, 151.2093, 0, "Australia/Sydney",
      new int[][] { { 2026, 4, 5 }, { 2026, 10, 4 } } ) );
    PLACES.add( new Place( "Tromso", 69.6492, 18.9553, 0, "Europe/Oslo",
      new int[][] { { 2026, 3, 29 }, { 2026, 10, 25 } } ) );
    PLACES.add( new Place( "Denver", 39.7392, -104.9903, 1609, "America/Denver", US_DST_DATES ) );
  }


  private ZmanimGoldenTable() {
  }


  public static List<Place> places() {
    return PLACES;
  }


  /**
   * @return one entry per column holding zman times, in table order. Each is
   *  {@code Type|zman name|advanced}; the type is the enum constant name, because display names are shared by
   *  more than one type.
   */
  public static List<String> zmanColumns() {

    // The zmanim list does not depend on the place or date, so any combination yields the same columns.
    ZmanimLocation location = PLACES.get( 0 ).location;
    HebrewDate date = HebrewCalendar.getForDate( 2026, Calendar.JANUARY, 1 );

    List<String> columns = new ArrayList<>();
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      for ( Zman zman : ZmanimFactory.get( location, date, type ).asList() ) {
        columns.add( type.name() + "|" + zman.name + "|" + zman.advanced );
      }
    }

    return columns;
  }


  public static String headerLine() {

    List<String> header = new ArrayList<>();
    header.add( "place" );
    header.add( "elevation" );
    header.add( "date" );
    header.addAll( zmanColumns() );
    header.add( LISTED_COLUMN );

    return String.join( SEPARATOR, header );
  }


  /**
   * @return the same-named columns as {@link #headerLine()}, mapped to the computed cell for the specified
   *  row, in column order.
   */
  public static Map<String, String> computeRow(
    Place   place,
    int[]   date,
    boolean useElevation
  ) {

    ZmanimFactory.useElevation( useElevation );

    HebrewDate hebrewDate = HebrewCalendar.getForDate( date[ 0 ], date[ 1 ] - 1, date[ 2 ] );
    long dayStart = utcDayStart( date );

    Map<String, String> row = new LinkedHashMap<>();
    for ( Zmanim.Type type : Zmanim.Type.values() ) {
      for ( Zman zman : ZmanimFactory.get( place.location, hebrewDate, type ).asList() ) {
        Calendar time = zman.getTime();
        String value = ( time == null ) ? NO_TIME : Long.toString( time.getTimeInMillis() - dayStart );
        String column = type.name() + "|" + zman.name + "|" + zman.advanced;
        if ( row.put( column, value ) != null ) {
          throw new IllegalStateException( "Duplicate column " + column );
        }
      }
    }

    StringBuilder listed = new StringBuilder();
    for ( Zmanim zmanim : hebrewDate.getZmanim( place.location ) ) {
      if ( listed.length() > 0 ) {
        listed.append( ',' );
      }
      listed.append( zmanim.type.name() );
    }
    row.put( LISTED_COLUMN, listed.toString() );

    return row;
  }


  /**
   * @return all rows of the table, each as the first three key cells followed by the computed cells.
   */
  public static String render() {

    StringBuilder table = new StringBuilder();
    table.append( headerLine() ).append( '\n' );

    for ( Place place : PLACES ) {
      for ( boolean useElevation : elevationSettings( place ) ) {
        for ( int[] date : place.dates ) {
          table.append( place.name )
            .append( SEPARATOR ).append( useElevation ? "on" : "off" )
            .append( SEPARATOR ).append( formatDate( date ) );
          for ( String cell : computeRow( place, date, useElevation ).values() ) {
            table.append( SEPARATOR ).append( cell );
          }
          table.append( '\n' );
        }
      }
    }

    ZmanimFactory.useElevation( false );
    return table.toString();
  }


  /** Applying elevation only makes a difference where the place has one. */
  public static boolean[] elevationSettings( Place place ) {
    return place.hasElevation ? new boolean[] { false, true } : new boolean[] { false };
  }


  public static String formatDate( int[] date ) {
    return String.format( "%04d-%02d-%02d", date[ 0 ], date[ 1 ], date[ 2 ] );
  }


  public static int[] parseDate( String date ) {

    String[] parts = date.split( "-" );
    return new int[] { Integer.parseInt( parts[ 0 ] ), Integer.parseInt( parts[ 1 ] ), Integer.parseInt( parts[ 2 ] ) };
  }


  private static long utcDayStart( int[] date ) {

    Calendar utc = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
    utc.clear();
    utc.set( date[ 0 ], date[ 1 ] - 1, date[ 2 ] );
    return utc.getTimeInMillis();
  }
}
