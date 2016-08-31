package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

/**
 *
 */
public class ZmanimOutputHelper {


  protected final DateFormat                                shortFormat = DateFormat.getTimeInstance( DateFormat.SHORT );

  protected final DateFormat                                mediumFormat = DateFormat.getTimeInstance( DateFormat.MEDIUM );

  protected final ZmanFormatter                             formatter = new ZmanFormatter( false );

  protected final ZmanFormatter                             formatterWithSeconds = new ZmanFormatter( true );

  protected final SecularDateFormatter                      dateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.SHORT );




  protected int outputHeader(
    StringBuilder   buffer,
    HebrewDate      startDate,
    ZmanimLocation  location,
    Zmanim.Type     type
  ) {

    Zmanim zmanim = startDate.getZmanim( location, type );
    List<Zman> zmans = zmanim.asList();
    int maxLength = computeColumnWidth( zmans );

    buffer.append( "Date           |" );
    for ( Zman zman : zmans ) {
      String name = zman.name;
      int padding = ( maxLength - ( name.length() ) ) / 2;
      pad( buffer, padding );
      buffer.append( name );

      pad( buffer, maxLength - padding - name.length() );

      buffer.append( "|" );
    }

    buffer.append( "\n" );

    buffer.append( "---------------|" );
    for ( Zman zman : zmans ) {
      for ( int i = 0; i < maxLength; i++ ) {
        buffer.append( "-" );
      }
      buffer.append( "|" );
    }

    buffer.append( "\n" );

    return maxLength;
  }


  protected void pad(
    final StringBuilder buffer,
    final int padding
  ) {
    for ( int i = 0; i < padding; i++ ) {
      buffer.append( " " );
    }
  }


  private int computeColumnWidth( final List<Zman> zmans ) {

    int maxLength = -1;
    for ( Zman zman : zmans ) {
      maxLength = Math.max( maxLength, zman.name.length() );
    }

    // Make sure there's some padding.
    maxLength = Math.max( 14, maxLength + 2 );
    return maxLength;
  }


  protected String buildOutput(
    HebrewDate      startDate,
    HebrewDate      endDate,
    ZmanimLocation  location,
    Zmanim.Type     type,
    boolean         includeShort,
    Validator       validator
  )
    throws ParseException {

    StringBuilder output = new StringBuilder();
    int maxLength = outputHeader( output, startDate, location, type );

    while ( endDate.compareTo( startDate ) >= 0 ) {

      Zmanim zmanim = startDate.getZmanim( location, type );
      List<Zman> zmans = zmanim.asList();

      StringBuilder line1 = new StringBuilder()
        .append( dateFormatter.format( startDate ) )
        .append( "     | " );
      StringBuilder line2 = new StringBuilder()
        .append( "               | " );

      for ( Zman zman : zmans ) {

        String timeWithSeconds = formatterWithSeconds.formatZmanTime( zman );
        line1.append( timeWithSeconds );
        pad( line1, maxLength - timeWithSeconds.length() - 1 );
        line1.append( "| " );

        String time = formatter.formatZmanTime( zman );
        if ( includeShort ) {
          line2.append( time );
          pad( line2, maxLength - time.length() - 1 );
          line2.append( "| " );
        }

        validator.validateZmanTimes( time, timeWithSeconds );
      }

      output
        .append( line1 )
        .append( "\n" );

      if ( includeShort ) {
        output
          .append( line2 )
          .append( "\n" );
      }

      //noinspection AssignmentToMethodParameter
      startDate = startDate.tomorrow();
    }

    return output.toString();
  }


  protected interface Validator {

    void validateZmanTimes(
      String time,
      String timeWithSeconds
    ) throws ParseException;

    void validateResult(
      String expected,
      String actual
    );
  }
} // End of ZmanimOutputHelper class
