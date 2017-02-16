/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.location;

/**
 * Used to parse a string into a latitude or a longitude.
 *<p>
 *
 * Supported formats are:
 *  <ul>
 *    <li> DDD MM['] [SS["]] -- Assumed North
 *    <li> [N|S] DDD MM['] [SS["]]
 *    <li> DDD.MM[N|S]
 *  </ul>
 */
public abstract class LatitudeLongitudeParser {

  public static enum Direction {
    NORTH( "N" ),
    SOUTH( "S" ),
    EAST( "E" ),
    WEST( "W" );


    private final String                                    abbreviation;

    Direction( String abbreviation ) {
      this.abbreviation = abbreviation;
    }
  }

  public static class Holder {

    private Direction                                       direction;
    private int                                             minutes;
    private int                                             degrees;
    private int                                             seconds = 0;


    public Holder() {
      this( Direction.NORTH, 0, 0 );
    }


    public Holder(
      Direction direction,
      int       degrees,
      int       minutes
    ) {
      setDirection( direction );
      setDegrees( degrees );
      setMinutes( minutes );
    }


    public Direction getDirection() {
      return direction;
    }

    void setDirection( Direction direction ) {
      this.direction = direction;
    }


    public int getDegrees() {
      return degrees;
    }

    void setDegrees( int degrees ) {
      this.degrees = degrees;
    }


    public int getMinutes() {
      return minutes;
    }

    void setMinutes( int minutes ) {
      this.minutes = minutes;
    }


    public int getSeconds() {
      return seconds;
    }

    void setSeconds( int seconds ) {
      this.seconds = seconds;
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
  }

  public static final LatitudeLongitudeParser               LATITUDE_PARSER = new LatitudeParser();
  public static final LatitudeLongitudeParser               LONGITUDE_PARSER = new LongitudeParser();

  protected abstract double getMax();


  @SuppressWarnings( "AssignmentToMethodParameter" )
  public Holder parse( String value ) {

    Holder holder = new Holder();

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
    value = extractDirection( value, holder );

    // Clean up the input....
    value = value.replace( '.', ' ' );
    value = value.replace( '\'', ' ' );
    value = value.replace( '\"', ' ' );
    value = value.trim();


    String[] latitudeComponents = value.split( " " );
    if ( 0 == latitudeComponents.length ) {
      throw new IllegalArgumentException( "Can't parse value: " + value );
    }

    holder.setDegrees( Integer.parseInt( latitudeComponents[ 0 ].trim() ) );
    if ( holder.getDegrees() < 1 ) {
      if ( Direction.NORTH == holder.getDirection() ) {
        holder.setDirection( Direction.SOUTH );
      }
      else if ( Direction.EAST == holder.getDirection() ) {
        holder.setDirection( Direction.WEST );
      }

      holder.setDegrees( holder.getDegrees() * -1 );
    }

    if ( latitudeComponents.length > 1 ) {
      holder.setMinutes( Integer.parseInt( latitudeComponents[ 1 ].trim() ) );
    }

    if ( latitudeComponents.length > 2 ) {
      holder.setSeconds( Integer.parseInt( latitudeComponents[ 2 ].trim() ) );
    }

    // Ahhh, but wait. IFF the value is too large, then perhaps what the user was entering was, after all, in
    //  decimal format?
    // NOTE: Removed this for the same reason as why I removed the blanket parsing into a double...
//    if ( holder.getDegrees() > getMax() && ( holder.getMinutes() > 0 ) ) {
//      holder.setDegrees( Double.parseDouble( latitudeComponents[0] + "." + latitudeComponents[1] ) );
//    }

    // If the value is still too large, then just choose zero.
    if ( holder.getDegrees() > getMax() ) {
      holder.setDegrees( 0 );
    }

    return holder;
  }


  /**
   * Attempts to parse the specified string representing a latitude or longitude into a double containing the
   *  decimal representation.
   */
  public double parseAsDouble( String value )
    throws IllegalArgumentException {

    Holder holder = parse( value );

    // See http://stackoverflow.com/questions/6945008/converting-value-longitude-valuesdmscompass-direction-format-to-correspondi
    //noinspection MagicNumber
    double convertedDegrees = holder.getDegrees() + ( ( ( (double)holder.getSeconds() / 60 ) ) + holder.getMinutes() ) / (double)60;

    if ( ( Direction.SOUTH == holder.getDirection() ) || ( Direction.WEST == holder.getDirection() ) ) {
      convertedDegrees = -1 * convertedDegrees;
    }

    return convertedDegrees;
  }


  @SuppressWarnings( "AssignmentToMethodParameter" )
  private String extractDirection(
    String value,
    Holder holder
  ) {

    char test = value.charAt( 0 );
    if ( ( 'S' == test ) || ( 's' == test ) ) {
      value = value.substring( 1 );
      holder.setDirection( Direction.SOUTH );
    }
    else if ( ( 'W' == test ) || ( 'w' == test ) ) {
      value = value.substring( 1 );
      holder.setDirection( Direction.WEST );
    }
    else if ( ( 'N' == test ) || ( 'n' == test ) ) {
      value = value.substring( 1 );
      holder.setDirection( Direction.NORTH );
    }
    else if ( ( 'E' == test ) || ( 'e' == test ) ) {
      value = value.substring( 1 );
      holder.setDirection( Direction.EAST );
    }

    test = value.charAt( value.length() - 1 );
    if ( ( 'S' == test ) || ( 's' == test ) ) {
      value = value.substring( 0, value.length() - 1 );
      holder.setDirection( Direction.SOUTH );
    }
    else if ( ( 'W' == test ) || ( 'w' == test ) ) {
      value = value.substring( 0, value.length() - 1 );
      holder.setDirection( Direction.WEST );
    }
    else if ( ( 'N' == test ) || ( 'n' == test ) ) {
      value = value.substring( 0, value.length() - 1 );
      holder.setDirection( Direction.NORTH );
    }
    else if ( ( 'E' == test ) || ( 'e' == test ) ) {
      value = value.substring( 0, value.length() - 1 );
      holder.setDirection( Direction.EAST );
    }

    return value.trim();
  }


  static class LatitudeParser
    extends LatitudeLongitudeParser {

    @Override
    protected double getMax() {
      //noinspection MagicNumber
      return 90;
    }
  }

  static class LongitudeParser
    extends LatitudeLongitudeParser {

    @Override
    protected double getMax() {
      //noinspection MagicNumber
      return 180;
    }
  }
}
