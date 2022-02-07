/*
 * Copyright (c) 2022. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

import com.gindin.util.Pair;

/**
 * Used to parse a string into a latitude or a longitude.
 * <p>
 * <p>
 * Supported formats are:
 * <ul>
 *   <li> DDD MM['] [SS["]] -- Assumed North
 *   <li> [N|S] DDD MM['] [SS["]]
 *   <li> DDD.MM[N|S]
 * </ul>
 */
public class LatitudeLongitudeParser {

  private static final int                                  MAX_LATITUDE = 90;

  private static final int                                  MAX_LONGITUDE = 180;

  public enum Direction {
    NORTH( "N" ),
    SOUTH( "S" ),
    EAST( "E" ),
    WEST( "W" );


    private final String abbreviation;


    Direction( String abbreviation ) {
      this.abbreviation = abbreviation;
    }
  }

  private static final int                                  MINUTES_PER_DEGREE = 60;
  private static final int                                  SECONDS_PER_MINUTE = 60;

  public final Direction                                    direction;
  public final int                                          minutes;
  public final int                                          degrees;
  public final double                                       seconds;


  @SuppressWarnings( "AssignmentToMethodParameter" )
  protected LatitudeLongitudeParser(
    Direction direction,
    int       degrees,
    int       minutes,
    double    seconds,
    int       maxDegrees
  ) {

    if ( degrees < 0 ) {
      if ( Direction.NORTH == direction ) {
        direction = Direction.SOUTH;
      }
      else if ( Direction.EAST == direction ) {
        direction = Direction.WEST;
      }

      degrees = degrees * -1;
    }

    this.direction = direction;
    this.minutes = minutes;
    this.seconds = seconds;

    // If the value is still too large, then just choose zero.
    if ( degrees > maxDegrees ) {
      this.degrees = 0;
    }
    else {
      this.degrees = degrees;
    }
  }


  public String toFullString( boolean withMarkers ) {
    return direction.abbreviation + " " + formattedDMS( withMarkers );
  }


  public String toSimpleString( boolean withMarkers ) {
    String str;
    if ( ( Direction.SOUTH == direction ) || ( Direction.WEST == direction ) ) {
      str = direction.abbreviation + " ";
    }
    else {
      str = "";
    }

    return str + formattedDMS( withMarkers );
  }


  private String formattedDMS( boolean withMarkers ) {

    StringBuilder str = new StringBuilder().append( degrees );
    if ( ( 0 != minutes ) || ( 0 != seconds ) ) {
      str.append( " " ).append( minutes );
      if ( withMarkers ) {
        str.append( "'" );
      }
    }

    if ( 0 != seconds ) {
      str.append( " " ).append( seconds );
      if ( withMarkers ) {
        str.append( "\"" );
      }
    }

    return str.toString();
  }

  static public LatitudeLongitudeParser latitude(
    Direction direction,
    int       degrees,
    int       minutes,
    int       seconds
  ) {
    return new LatitudeLongitudeParser( direction, degrees, minutes, seconds, MAX_LATITUDE );
  }


  static public LatitudeLongitudeParser latitude(
    Direction direction,
    String    degreesText,
    String    minutesText,
    String    secondsText
  ) {
    return parse( direction, degreesText, minutesText, secondsText, MAX_LATITUDE );
  }

  static public LatitudeLongitudeParser latitude( String value ) {
    return parse( value, Direction.NORTH, MAX_LATITUDE );
  }


  static public double latitudeValue( String value ) {
    
    double latitude = parseAsDouble( value, Direction.NORTH, MAX_LATITUDE );

    // Latitude must be -90 <= lat <= 90
    if ( ( latitude < -MAX_LATITUDE ) || ( latitude > MAX_LATITUDE ) ) {
      latitude = 0;
    }

    return latitude;
  }


  static public LatitudeLongitudeParser longitude(
    Direction direction,
    int       degrees,
    int       minutes,
    int       seconds
  ) {
    return new LatitudeLongitudeParser( direction, degrees, minutes, seconds, MAX_LONGITUDE );
  }

  
  static public LatitudeLongitudeParser longitude(
    Direction direction,
    String    degreesText,
    String    minutesText,
    String    secondsText
  ) {
    return parse( direction, degreesText, minutesText, secondsText, MAX_LONGITUDE );
  }


  static public LatitudeLongitudeParser longitude( String value ) {
    return parse( value, Direction.EAST, MAX_LONGITUDE );
  }


    static public double longitudeValue( String value ) {

    double longitude = parseAsDouble( value, Direction.EAST, MAX_LONGITUDE );

    // Longitude must be -180 <= long <= 180
    if ( ( longitude < -MAX_LONGITUDE ) || ( longitude > MAX_LONGITUDE ) ) {
      longitude = MAX_LONGITUDE;
    }

    return longitude;
  }


  private static LatitudeLongitudeParser parse(
    Direction direction,
    String    degreesText,
    String    minutesText,
    String    secondsText,
    int       maxDegrees
  ) {

    boolean parseMinutes = true;

    int degrees;
    int minutes = 0;
    double seconds = 0;
    
    try {
      degrees = Integer.parseInt( degreesText );
    }
    catch ( NumberFormatException e ) {
      // Maybe the user doesn't have separate minutes & degrees, so they just entered something like "47.5461"?
      try {
        double degreesDouble = Double.parseDouble( degreesText );

        // Casting the double to an int will give us the degrees, by just lopping off the part after the decimal.
        degrees = (int)degreesDouble;

        // Now, convert the decimal portion to the minutes. http://geography.about.com/library/howto/htdegrees.htm
        double doubleMinutes = Math.abs( ( ( degreesDouble - degrees ) * MINUTES_PER_DEGREE ) );
        minutes = (int)doubleMinutes;

        seconds = ( ( doubleMinutes - minutes ) * SECONDS_PER_MINUTE );

        parseMinutes = false;
      }
      catch ( NumberFormatException e1 ) {
        // Nope... just give up.
        degrees = 0;
        minutes = 0;
        seconds = 0;
      }
    }

    if ( parseMinutes ) {

      try {
        minutes = Integer.parseInt( minutesText );
      }
      catch ( NumberFormatException e ) {
        minutes = 0;
      }

      try {
        seconds = Double.parseDouble( secondsText );
      }
      catch ( NumberFormatException e ) {
        seconds = 0;
      }
    }
    
    return new LatitudeLongitudeParser( direction, degrees, minutes, seconds, maxDegrees );
  }


  /**
   * Attempts to parse the specified string representing a latitude or longitude into a double containing the
   * decimal representation.
   */
   private static double parseAsDouble(
     String     value,
     Direction  defaultDirection,
     int        maxDegress
   ) {

     final LatitudeLongitudeParser parsed = parse( value, defaultDirection, maxDegress );

     // See http://stackoverflow.com/questions/6945008/converting-value-longitude-valuesdmscompass-direction-format-to-correspondi
    double convertedDegrees = parsed.degrees + ( ( ( parsed.seconds / SECONDS_PER_MINUTE ) ) + parsed.minutes ) / (double)MINUTES_PER_DEGREE;

    if ( ( Direction.SOUTH == parsed.direction ) || ( Direction.WEST == parsed.direction ) ) {
      convertedDegrees = -1 * convertedDegrees;
    }

    return convertedDegrees;
  }


  @SuppressWarnings( "AssignmentToMethodParameter" )
  private static LatitudeLongitudeParser parse(
    String    value,
    Direction defaultDirection,
    int       maxDegrees
  ) {

    // Make sure there's no extra whitespace...
    value = value.trim();

    // NOTE: I used to try to parse the value directly into a double. I don't think this really makes sense, though,
    //  and quite possibly explains why so many users got confused and couldn't make this functionality work the
    //  way they thought it should.
//    try {
//      return Double.parseDouble( value );
//    }
//    catch ( NumberFormatException e ) {
//      // Well, keep trying to parse.
//    }
//
    final Pair<Direction, String> extracted = extractDirection( value, defaultDirection );
    value = extracted.second;

    // Clean up the input....
    value = value.replace( '.', ' ' );
    value = value.replace( '\'', ' ' );
    value = value.replace( '\"', ' ' );
    value = value.trim();


    String[] latitudeComponents = value.split( " " );
    if ( 0 == latitudeComponents.length ) {
      throw new IllegalArgumentException( "Can't parse value: " + value );
    }

    int degrees = Integer.parseInt( latitudeComponents[ 0 ].trim() );

    int minutes;
    if ( latitudeComponents.length > 1 ) {
      minutes = Integer.parseInt( latitudeComponents[ 1 ].trim() );
    }
    else {
      minutes = 0;
    }

    int seconds;
    if ( latitudeComponents.length > 2 ) {
      seconds = Integer.parseInt( latitudeComponents[ 2 ].trim() );
    }
    else {
      seconds = 0;
    }

    // Ahhh, but wait. IFF the value is too large, then perhaps what the user was entering was, after all, in
    //  decimal format?
    // NOTE: Removed this for the same reason as why I removed the blanket parsing into a double...
//    if ( holder.getDegrees() > getMax() && ( holder.getMinutes() > 0 ) ) {
//      holder.setDegrees( Double.parseDouble( latitudeComponents[0] + "." + latitudeComponents[1] ) );
//    }

    return new LatitudeLongitudeParser( extracted.first, degrees, minutes, seconds, maxDegrees );
  }


  private static Pair<Direction, String> extractDirection(
    String    value,
    Direction defaultDirection
  ) {

    char test = value.charAt( 0 );
    if ( ( 'S' == test ) || ( 's' == test ) ) {
      return new Pair<>( Direction.SOUTH, value.substring( 1 ) );
    }
    else if ( ( 'W' == test ) || ( 'w' == test ) ) {
      return new Pair<>( Direction.WEST, value.substring( 1 ) );
    }
    else if ( ( 'N' == test ) || ( 'n' == test ) ) {
      return new Pair<>( Direction.NORTH, value.substring( 1 ) );
    }
    else if ( ( 'E' == test ) || ( 'e' == test ) ) {
      return new Pair<>( Direction.EAST, value.substring( 1 ) );
    }

    test = value.charAt( value.length() - 1 );
    if ( ( 'S' == test ) || ( 's' == test ) ) {
      return new Pair<>( Direction.SOUTH, value.substring( 0, value.length() - 1 ) );
    }
    else if ( ( 'W' == test ) || ( 'w' == test ) ) {
      return new Pair<>( Direction.WEST, value.substring( 0, value.length() - 1 ) );
    }
    else if ( ( 'N' == test ) || ( 'n' == test ) ) {
      return new Pair<>( Direction.NORTH, value.substring( 0, value.length() - 1 ) );
    }
    else if ( ( 'E' == test ) || ( 'e' == test ) ) {
      return new Pair<>( Direction.EAST, value.substring( 0, value.length() - 1 ) );
    }

    return new Pair<>( defaultDirection, value );
  }

}
