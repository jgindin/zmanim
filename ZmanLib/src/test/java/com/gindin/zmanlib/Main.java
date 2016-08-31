package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class Main
  extends ZmanimOutputHelper {


  private static final Validator validator = new Validator() {
    @Override
    public void validateZmanTimes(
      String time,
      String timeWithSeconds
    ) throws ParseException {
      // don't care
    }


    @Override
    public void validateResult(
      String expected,
      String actual
    ) {
      // don't care
    }
  };


  @Test
  public void justRun() throws ParseException {

    HebrewDate start = HebrewCalendar.getForDate( 2016, Calendar.OCTOBER, 1 );
    HebrewDate end = HebrewCalendar.getForDate( 2017, Calendar.SEPTEMBER, 30 );

    String output = buildOutput( start, end, UT_ZmanUtils.seattle, Zmanim.Type.Talit, false, validator );
    System.out.println( output );
  }

  public static void main(
    String[] args
  ) {

    System.out.println( "Usage:" );
    System.out.println( "  start_date end_date zmanType" );
    System.out.println( "Where: " );
    System.out.println( "  start/end date are expected to be in the form MM/dd/yyyy" );
    System.out.println( "  zmanType is one of the Zmanim.Type enum values" );

    if ( args.length != 3 ) {
      System.out.println( "Bad usage." );
      System.exit( 1 );
    }

    Date startDate = null;
    Date endDate = null;

    DateFormat format = new SimpleDateFormat( "MM/dd/yyy", Locale.US );
    try {
      startDate = format.parse( args[0] );
    }
    catch ( ParseException e ) {
      System.out.println( "Cannot parse: " + args[0] + " for the start date." );
      System.exit( 1 );
    }

    try {
      endDate = format.parse( args[1] );
    }
    catch ( ParseException e ) {
      System.out.println( "Cannot parse: " + args[1] + " for the end date." );
      System.exit( 1 );
    }

    final Zmanim.Type type = Zmanim.Type.valueOf( args[ 2 ] );


    Main m = new Main();

    final Calendar startCal = Calendar.getInstance();
    startCal.setTime( startDate );
    HebrewDate start = HebrewCalendar.getForDate( startCal );

    final Calendar endCal = Calendar.getInstance();
    endCal.setTime( endDate );
    HebrewDate end = HebrewCalendar.getForDate( endCal );

    String output = null;
    try {
      output = m.buildOutput( start, end, UT_ZmanUtils.seattle, type, false, validator );
      System.out.println( output );
    }
    catch ( ParseException e ) {
      e.printStackTrace();
    }
  }



} // End of Main class
