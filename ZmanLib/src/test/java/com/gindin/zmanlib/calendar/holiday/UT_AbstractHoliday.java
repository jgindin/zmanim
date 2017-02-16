/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.holiday;

import com.gindin.zmanlib.calendar.formatters.SecularDateFormatter;

/**
 *
 */
public class UT_AbstractHoliday {

  private static final SecularDateFormatter                 formatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.MEDIUM );


  protected UT_AbstractHoliday() {
    // Not instantiatable
  }


  protected final StringBuilder buildDetailsHeader() {

    StringBuilder output = new StringBuilder();
    output
      .append( "Date         | Description                                   | Any   | Erev  | Erev Shabbat | Light | Shabbat | Yom Tov | Havdalah | Pesach | Omer  | Taanit |\n" )
      .append( "-------------|-----------------------------------------------|-------|-------|--------------|-------|---------|---------|----------|--------|-------|--------|\n" );

    return output;
  }


  @SuppressWarnings( "AutoBoxing" )
  protected final void outputDetails( StringBuilder output, Details details ) {

    output
      .append( String.format( "%-10s", formatter.format( details.getTodaysDate() ) ) )
      .append( " | " )
      .append( String.format( "%-45s", details.getDescription() ) )
      .append( " | " )
      .append( String.format( "%-5s", details.isAnything() ) )
      .append( " | " )
      .append( String.format( "%-5s", details.isErev() ) )
      .append( " | " )
      .append( String.format( "%-12s", details.isErevShabbat() ) )
      .append( " | " )
      .append( String.format( "%-5s", details.lightCandles() ) )
      .append( " | " )
      .append( String.format( "%-7s", details.isShabbat() ) )
      .append( " | " )
      .append( String.format( "%-7s", details.isYomTov() ) )
      .append( " | " )
      .append( String.format( "%-8s", details.sayHavdalah() ) )
      .append( " | " )
      .append( String.format( "%-6s", details.isPesach() ) )
      .append( " | " )
      .append( String.format( "%-5s", details.isOmer() ) )
      .append( " | " )
      .append( String.format( "%-6s", details.isTaanit() ) )
      .append( " |\n" );
  }


} // End of UT_AbstractHoliday class
