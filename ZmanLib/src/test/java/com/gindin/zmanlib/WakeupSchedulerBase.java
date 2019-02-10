package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.notification.NotifiedZmanim;
import com.gindin.zmanlib.notification.ZmanNotificationScheduler;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.util.*;

import static com.gindin.zmanlib.UT_ZmanUtils.seattle;

/**
 *
 */
abstract class WakeupSchedulerBase {


  @SuppressWarnings( "MagicNumber" )
  void december_21_2015_1_week(
    final ZmanCallback  perTestCallback,
    final String        expectedOutput
  ) {

    // In this case, the user never turns their phone off...
    Calendar testTime = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 21, 0, 1 );

    runDays( testTime, 7, perTestCallback, expectedOutput );
  }


  void runDays(
    final Calendar      testTime,
    final ZmanCallback  perTestCallback,
    final String        expectedOutput
  ) {
    runDays( testTime, 1, perTestCallback, expectedOutput );
  }


  void runDays(
    final Calendar      testTime,
    final int           numDays,
    final ZmanCallback  perTestCallback,
    final String        expectedOutput
  ) {

    final StringBuilder output = new StringBuilder();
    //                       1         2         3         4         5         6         7         8         0
    //              123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
    output.append( "Zman                | Time           | Action Time                   | Actually Alert?   | \n" );
    output.append( "--------------------|----------------|-------------------------------|-------------------|\n" );


    RunDaysZmanCallback zmanCallback = new RunDaysZmanCallback( perTestCallback, testTime, output );
    ZmanNotificationScheduler scheduler = new ZmanNotificationScheduler( zmanCallback );

    for ( int i = 0; i < numDays; i++ ) {

      scheduler.run( seattle, testTime );
      testTime.add( Calendar.DAY_OF_MONTH, 1 );
      testTime.set( Calendar.HOUR, 0 );
      testTime.set( Calendar.MINUTE, 0 );
    }

    Assert.assertEquals( expectedOutput, output.toString() );
  }



  private class RunDaysZmanCallback
    extends ZmanCallback {

    /** For a specified day, which zmanim have we notified for? */
    final Map<HebrewDate, NotifiedZmanim>                   notifiedZmanim;

    /** The real callback. */
    private final ZmanCallback                              perTestCallback;

    /** Format the output. */
    private final ZmanFormatter                             zmanFormatter = new ZmanFormatter( () -> true );

    /** Where to generate the output into. */
    private final StringBuilder                             output;



    RunDaysZmanCallback(
      ZmanCallback        perTestCallback,
      Calendar            testTime,
      StringBuilder       output
    ) {
      this.perTestCallback = perTestCallback;
      this.output = output;
      notifiedZmanim = new HashMap<>();

      notifiedZmanim.put( HebrewCalendar.getForDate( testTime ), new NotifiedZmanim( testTime ) );
    }


    @Override
    public String getPreferredZman( Zmanim.Type type ) {
      return perTestCallback.getPreferredZman( type );
    }


    @Override
    public int howManyMinutesToAlertBefore( final Zmanim.Type type ) {
      return perTestCallback.howManyMinutesToAlertBefore( type );
    }


    @Override
    public boolean shouldShowAnyNotifications() {
      return true;
    }


    @Override
    public boolean shouldNotifyOnShabbat() {
      return perTestCallback.shouldNotifyOnShabbat();
    }


    @Nullable
    @Override
    public String getNotificationTone() {
      return null;
    }


    @Override
    public boolean hasBeenNotified(
      Calendar    zmanTime,
      Zmanim.Type type
    ) {

      final NotifiedZmanim notifiedForDay = notifiedZmanim.get( HebrewCalendar.getForDate( zmanTime ) );
      return notifiedForDay != null && notifiedForDay.contains( type );
    }


    @Override
    public void completed(
      Calendar    now,
      Zmanim.Type type
    ) {

      NotifiedZmanim notifiedForDay = getNotifiedZmanim( now );
      notifiedForDay.notified( type );
    }


    private NotifiedZmanim getNotifiedZmanim( Calendar now ) {

      NotifiedZmanim notifiedForDay = notifiedZmanim.get( HebrewCalendar.getForDate( now ) );
      if ( notifiedForDay == null ) {

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis( now.getTimeInMillis() );
        today.setTimeZone( now.getTimeZone() );

        notifiedForDay = new NotifiedZmanim( today );

        notifiedZmanim.put( HebrewCalendar.getForDate( today ), notifiedForDay );
      }
      return notifiedForDay;
    }


    @Override
    public void scheduleAlert(
      Zman              zman,
      ZmanimLocation    location,
      Calendar          alertAt
    ) {

      output.append(
        String.format( "%-20s| %-15s| %-30s| %-18s|\n", zman.type, zmanFormatter.formatZmanTime( zman ),
          new Date( alertAt.getTimeInMillis() ), shouldActuallyNotify( location, alertAt ) )
      );
    }


    @Override
    public boolean formatTimeWithSeconds() {
      return false;
    }


  }

} // End of WakeupSchedulerBase class
