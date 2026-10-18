/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanlib.calendar.formatters;

import com.gindin.zmanlib.calendar.HebrewDate;

import java.util.Calendar;

/**
 * Knows how to format a HebrewDate as a secular date.
 */
public abstract class SecularDateFormatter {

  public enum Style {
    DEFAULT,
    SHORT,
    MEDIUM,
    LONG
  }

  private static SecularDateFormatter                       FORMATTER;

  private static SecularDateFormatter                       MEDIUM_FORMATTER;

  private static SecularDateFormatter                       SHORT_FORMATTER;


  public static SecularDateFormatter getInstance( Style formatterStyle ) {

    switch ( formatterStyle ) {
      case SHORT:
        if ( SHORT_FORMATTER == null ) {
          SHORT_FORMATTER = new ShortFormatter();
        }
        return SHORT_FORMATTER;

      case MEDIUM:
        if ( MEDIUM_FORMATTER == null ) {
          MEDIUM_FORMATTER = new MediumFormatter();
        }
        return MEDIUM_FORMATTER;

      case DEFAULT:
      case LONG:
      default:
        if ( null == FORMATTER ) {
          FORMATTER = new DefaultFormatter();
        }
        return FORMATTER;
    }

  }

  public abstract String format( HebrewDate date );


  private static class DefaultFormatter
    extends SecularDateFormatter {


    @SuppressWarnings( "FeatureEnvy" )
    @Override
    public String format( HebrewDate date ) {

      final StringBuilder formattedDate = new StringBuilder();
      switch ( date.getDayOfWeek() ) {
        case Calendar.SUNDAY:
          formattedDate.append( "Sun" );
          break;

        case Calendar.MONDAY:
          formattedDate.append( "Mon" );
          break;

        case Calendar.TUESDAY:
          formattedDate.append( "Tue" );
          break;

        case Calendar.WEDNESDAY:
          formattedDate.append( "Wed" );
          break;

        case Calendar.THURSDAY:
          formattedDate.append( "Thur" );
          break;

        case Calendar.FRIDAY:
          formattedDate.append( "Fri" );
          break;

        case Calendar.SATURDAY:
          formattedDate.append( "Shabbat" );
          break;

      }

      formattedDate.append( ", " );

      switch ( date.getGregorianMonth() ) {
        case Calendar.JANUARY:
          formattedDate.append( "Jan " );
          break;

        case Calendar.FEBRUARY:
          formattedDate.append( "Feb " );
          break;

        case Calendar.MARCH:
          formattedDate.append( "Mar " );
          break;

        case Calendar.APRIL:
          formattedDate.append( "Apr " );
          break;

        case Calendar.MAY:
          formattedDate.append( "May " );
          break;

        case Calendar.JUNE:
          formattedDate.append( "Jun " );
          break;

        case Calendar.JULY:
          formattedDate.append( "Jul " );
          break;

        case Calendar.AUGUST:
          formattedDate.append( "Aug " );
          break;

        case Calendar.SEPTEMBER:
          formattedDate.append( "Sep " );
          break;

        case Calendar.OCTOBER:
          formattedDate.append( "Oct " );
          break;

        case Calendar.NOVEMBER:
          formattedDate.append( "Nov " );
          break;

        case Calendar.DECEMBER:
          formattedDate.append( "Dec " );
          break;
      }

      formattedDate.append( date.getGregorianDayOfMonth() ).append( ", " ).append( date.getGregorianYear() );

      return formattedDate.toString();
    }
  }


  private static class ShortFormatter
    extends SecularDateFormatter {

    @SuppressWarnings( { "FeatureEnvy", "AutoBoxing" } )
    @Override
    public String format( HebrewDate date ) {

      return String.format( "%2s/%2s/%2s", date.getGregorianMonth() + 1, date.getGregorianDayOfMonth(),
        date.getGregorianYear() );
    }
  }


  private static class MediumFormatter
    extends ShortFormatter {

    @SuppressWarnings( { "FeatureEnvy", "AutoBoxing" } )
    @Override
    public String format( HebrewDate date ) {

      final StringBuilder formattedDate = new StringBuilder( super.format( date ) );

      switch ( date.getDayOfWeek() ) {
        case Calendar.SUNDAY:
          formattedDate.append( " S" );
          break;

        case Calendar.MONDAY:
          formattedDate.append( " M" );
          break;

        case Calendar.TUESDAY:
          formattedDate.append( " T" );
          break;

        case Calendar.WEDNESDAY:
          formattedDate.append( " W" );
          break;

        case Calendar.THURSDAY:
          formattedDate.append( " R" );
          break;

        case Calendar.FRIDAY:
          formattedDate.append( " F" );
          break;

        case Calendar.SATURDAY:
          formattedDate.append( " B" );
          break;

      }

      return formattedDate.toString();
    }

  }


}  // End of SecularDateFormatter class
