/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.formatters;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;

/**
 * Knows how to provide various formats of a HebrewDate.
 *
 * TODO: Refactor to allow for locale-based formatters?
 */
public abstract class HebrewDateFormatter {

  private static final String[]                           HEBREW_MONTH_NAMES = {
    "Nissan",     "Iyar",     "Sivan",    "Tammuz",   "Av",       "Elul",
    "Tishrei",    "Cheshvan", "Kislev",   "Tevet",    "Shevat",   "Adar", "Adar II"
  };


  private static HebrewDateFormatter                        SHORT_FORMATTER;
  private static HebrewDateFormatter                        LONG_FORMATTER;


  public enum Style {
    DEFAULT,
    SHORT,
    MEDIUM,
    LONG
  }


  public static HebrewDateFormatter getInstance( Style formatterStyle ) {

    switch ( formatterStyle ) {
      case SHORT:
        if ( null == SHORT_FORMATTER ) {
          SHORT_FORMATTER = new ShortFormatter();
        }
        return SHORT_FORMATTER;

      case DEFAULT:
      case MEDIUM:
      case LONG:
      default:
        if ( null == LONG_FORMATTER ) {
          LONG_FORMATTER = new LongFormatter();
        }
        return LONG_FORMATTER;
    }

  }

  public abstract String format(
    HebrewDate      date,
    ZmanimLocation  location
  );


  /**
   * Formats a Hebrew date in a short fashion, which includes only the date. E.g., "1 Nissan 5772".
   */
  static class ShortFormatter
    extends HebrewDateFormatter {

    @SuppressWarnings( "FeatureEnvy" )
    @Override
    public String format(
      HebrewDate      date,
      ZmanimLocation  location
    ) {

      StringBuilder buf = new StringBuilder();

      if ( ( location != null ) && date.isToday() && date.isAfterSunset( location ) ) {
        //noinspection AssignmentToMethodParameter
        date = date.tomorrow();
        buf.append( "Or le " );
      }

      buf
        .append( date.getHebrewDayOfMonth() )
        .append( " " )

        // subtract one because the hebrew month is stored as a one-based value...
        .append( HEBREW_MONTH_NAMES[ date.getHebrewMonth() - 1 ] )
        .append( " " )
        .append( date.getHebrewYear() );

      return buf.toString();
    }


  }  // End of ShortFormatter class


  /**
   * Builds on the format presented by the short format and adds information about the day. E.g., if it is a
   *  holiday, etc...
   */
  static class LongFormatter
    extends ShortFormatter {

    @Override
    public String format(
      HebrewDate date,
      ZmanimLocation location
    ) {

      String buf = super.format( date, location );
      buf += date.getDescription();

      return buf;
    }
  }  // End of LongFormatter class

}  // End of HebrewDateFormatter class
