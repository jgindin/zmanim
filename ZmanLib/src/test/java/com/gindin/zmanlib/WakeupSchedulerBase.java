package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;

import org.junit.Assert;

import java.util.*;

import static com.gindin.zmanlib.UT_ZmanUtils.seattle;

/**
 *
 */
abstract class WakeupSchedulerBase {

	interface TestResultsCallback {

		String getExpectedOutput();

		Map<HebrewDate,List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date );
	}


  @SuppressWarnings( "MagicNumber" )
	void december_21_2015_1_week(
		final ZmanCallback          perTestCallback,
		final TestResultsCallback   testResultsCallback
	) {

		// In this case, the user never turns their phone off...
		Calendar testTime = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 21, 0, 1 );
		Calendar endTest = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 28, 0, 0 );

		runDays( testTime, endTest, perTestCallback, testResultsCallback );
	}


	void runDays(
		final Calendar              testTime,
		final Calendar              endDate,
		final ZmanCallback          perTestCallback,
    final TestResultsCallback   testResultsCallback
	) {

    HebrewDate hebrewDate = HebrewCalendar.getForDate( testTime );
    final Map<HebrewDate, List<Zmanim.Type>> expectedZmanim =
      testResultsCallback.getExpectedNotifiedZmanim( hebrewDate );

		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time              | Action                                        | Next (M:S:ms) |\n" );
		output.append( "------------------|-----------------------------------------------|---------------|\n" );


		RunDaysZmanCallback zmanCallback = new RunDaysZmanCallback( perTestCallback, testTime, output, endDate );
    ZmanNotificationScheduler scheduler = new ZmanNotificationScheduler( zmanCallback );

		while ( !zmanCallback.testComplete ) {

			output.append( String.format( "%1$tD %1$tH:%1$tM:%1$tS |", testTime ) );

			final HebrewDate forDate = HebrewCalendar.getForDate( testTime );
			NotifiedZmanim notifiedZmanim = zmanCallback.notifiedZmanim.get( forDate );
			if ( notifiedZmanim == null ) {
				notifiedZmanim = new NotifiedZmanim( testTime );
				zmanCallback.notifiedZmanim.put( forDate, notifiedZmanim );
			}

			scheduler.run( seattle, testTime );

			output.append( "\n" );
		}


    final String expected = testResultsCallback.getExpectedOutput();
    Assert.assertEquals( expected, output.toString() );

    final SecularDateFormatter dateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.SHORT );

		// Make sure we notified for all the zmanim of the day...
		for ( Map.Entry<HebrewDate, NotifiedZmanim> entry : zmanCallback.notifiedZmanim.entrySet() ) {
			HebrewDate currentDate = entry.getKey();
			NotifiedZmanim actuallyNotifiedZmanim = entry.getValue();

      final List<Zmanim.Type> types = expectedZmanim.get( currentDate );
      if ( types == null ) {
        // This can happen and is OK, as long as it is the same as the expected end date.
        Assert.assertTrue( currentDate.compareTo( HebrewCalendar.getForDate( endDate ) ) == 0 );

        // ...but make sure that nothing was actually notified...
        Assert.assertTrue( actuallyNotifiedZmanim.isEmpty() );
      }
      else {

        final List<Zmanim.Type> expectedNotificationTypes = new ArrayList<>( types );

  			Iterator<Zmanim.Type> iter = expectedNotificationTypes.iterator();
  			while ( iter.hasNext() ) {
  				final Zmanim.Type next = iter.next();
  				Assert.assertTrue( "Didn't notify for " + next + " on " + dateFormatter.format( currentDate ), actuallyNotifiedZmanim.contains( next ) );
  				iter.remove();
  			}

  			Assert.assertTrue( expectedNotificationTypes.toString(), expectedNotificationTypes.isEmpty() );
      }
		}

	}



	private class RunDaysZmanCallback
		implements ZmanCallback {

		/** For a specified day, which zmanim have we notified for? */
		final Map<HebrewDate, NotifiedZmanim>                   notifiedZmanim;

		/** Is the test complete? */
		boolean                                                 testComplete = false;

		/** Used to know if we notified for a particular zman or not. */
		private boolean                                         notified = false;

		/** The real callback. */
		private final ZmanCallback                              perTestCallback;

		/** What time is it? As the test runs, this calendar is moved forward in time. */
		private final Calendar                                  testTime;

		/** When is the test supposed to end? */
		private final Calendar                                  endDate;

		/** Format the output. */
		private final ZmanFormatter                             zmanFormatter = new ZmanFormatter( true );

		/** Where to generate the output into. */
		private final StringBuilder                             output;



		RunDaysZmanCallback(
			ZmanCallback  perTestCallback,
			Calendar      testTime,
			StringBuilder output,
			Calendar      endDate
		) {
			this.perTestCallback = perTestCallback;
			this.testTime = testTime;
			this.output = output;
			this.endDate = endDate;
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
    public boolean shouldNotifyOnShabbat() {
      return perTestCallback.shouldNotifyOnShabbat();
    }


    @Override
		public boolean hasBeenNotified(
			Calendar  now,
			Zman      zman
		) {

			final NotifiedZmanim notifiedForDay = notifiedZmanim.get( HebrewCalendar.getForDate( now ) );
			return notifiedForDay != null && notifiedForDay.contains( zman.type );
		}


    @Override
    public void completed(
      Calendar  now,
      Zman      zman
    ) {

      NotifiedZmanim notifiedForDay = getNotifiedZmanim( now );
      notifiedForDay.notified( zman );
    }


    @Override
		public void notifyUser(
      Calendar        now,
      Zman            nextZman
    ) {

			long delta = nextZman.getTime().getTimeInMillis() - now.getTimeInMillis();
			long minUntilZman = ( delta / ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) );
			long remainingMs = ( delta - ( minUntilZman * ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) ) );
			long secs = ( remainingMs / 1000 );
			remainingMs = ( remainingMs - secs * 1000 );

			output.append( String.format( " %-45s |", "Notify: " + nextZman.type + "@" +
				zmanFormatter.formatZmanTime( nextZman ) + " (" + minUntilZman + ":" + secs + ":" + remainingMs + ")" ) );

      notified = true;
			testComplete = isTestComplete( now, endDate );
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
		public void scheduleNextCheck(
			final Calendar  now,
			final int       timeUntilNextCheck
		) {

			if ( !notified ) {
				output.append( String.format( " %-45s |", "Sleep" ) );
			}

			notified = false;

			int minUntilNextCheck = ( timeUntilNextCheck / ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) );
			int remainingMs = ( timeUntilNextCheck - ( minUntilNextCheck * ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) ) );
			int secs = ( remainingMs / 1000 );
			remainingMs = ( remainingMs - secs * 1000 );

			//noinspection AutoBoxing
			output.append( String.format( " %02d:%02d:%03d     |", minUntilNextCheck, secs, remainingMs ) );
			testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

			testComplete = isTestComplete( testTime, endDate );
		}


    private boolean isTestComplete(
			final Calendar  currentTestTime,
			final Calendar  endDate
		) {

			return currentTestTime.after( endDate );
		}


	}

} // End of WakeupSchedulerBase class
