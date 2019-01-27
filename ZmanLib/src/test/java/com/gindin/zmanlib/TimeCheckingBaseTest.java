package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.Assert;

import java.text.ParseException;
import java.util.Date;

/**
 *
 */
abstract class TimeCheckingBaseTest
  extends ZmanimOutputHelper {


  protected abstract Zmanim.Type getTypeForTest();


  protected abstract boolean shouldRoundDown();


  protected final void runTest(
    HebrewDate      startDate,
    HebrewDate      endDate,
    ZmanimLocation  location,
    String          expected
  )
    throws ParseException {

    Validator validator = new Validator() {
      @Override
      public void validateZmanTimes(
        String time,
        String timeWithSeconds
      )
        throws ParseException {

        Date zmanTimeWithSeconds;
        if ( "N/A".equals( timeWithSeconds ) ) {
          zmanTimeWithSeconds = null;
        }
        else {
          zmanTimeWithSeconds = mediumFormat.parse( timeWithSeconds );
        }

        Date zmanTime;
        if ( "N/A".equals( time ) ) {
          zmanTime = null;
        }
        else {
          zmanTime = shortFormat.parse( time );
        }

        // Make sure that we rounded up. Assuming we did, then the rounded time will possibly be equal to
        //  the time with seconds (e.g., if the time with seconds is exactly zero seconds) or after.
        if ( ( zmanTime == null ) && ( zmanTimeWithSeconds == null ) ) {
          // This is OK. Both times are "N/A".
        }
        else if ( ( zmanTime == null ) || ( zmanTimeWithSeconds == null ) ) {
          Assert.fail( "zmanTime & zmanTimeWithSeconds must both be null if either is null" );
        }
        else if ( shouldRoundDown() ) {
          Assert.assertTrue( time + " is supposed to be <= " + timeWithSeconds,
            zmanTime.before( zmanTimeWithSeconds ) || zmanTime.equals( zmanTimeWithSeconds ) );
        }
        else {
          Assert.assertTrue( time + " is supposed to be >= " + timeWithSeconds,
            zmanTime.after( zmanTimeWithSeconds ) || zmanTime.equals( zmanTimeWithSeconds ) );
        }
      }


      @Override
      public void validateResult(
        String expected,
        String actual
      ) {

        Assert.assertEquals( expected, actual );
      }
    };

    String actual = buildOutput( startDate, endDate, location, getTypeForTest(), true, validator );
    validator.validateResult( expected, actual );
  }


} // End of TimeCheckingBaseTest class
