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
abstract class WakeupSchedulerBase_MutliDay {

	interface TestResultsCallback {

		String getExpectedOutput();

		Map<HebrewDate,List<Zmanim.Type>> getExpectedNotifiedZmanim( HebrewDate date );
	}



	@SuppressWarnings( "MagicNumber" )
	void december_7_2014(
		final ZmanCallback          perTestCallback,
		final TestResultsCallback   testResultsCallback
	) {

		// I'm going to try and be somewhat realistic here. We'll say someone woke up and turned on their
		//  phone at 6:34am...
		//
		//  Zman                Time        Alert (-15 min)
		//  ------------------  --------    ---------------
		//  Alot Hashachar       6:02:50     5:47
		//  Talit/Tefilin        6:39:53     6:24
		//  HaNetz               7:42:51     7:27
		//  Sof Zman Shema       9:01:39     8:46
		//  Sof Zman Tefila     10:34:34    10:19
		//  Chatzot             12:00:26    11:45
		//  Mincha Gedola       12:21:54    12:06
		//  Mincha Ketana       14:30:42    14:15
		//  Plag HaMincha       15:24:21    15:09
		//  Shkia               16:18:01    16:03
		//  Latest Mincha       16:27:41    16:12
		//  Bein Hashmashot     16:52:05    16:37
		//  Tzait Hacochavim    17:03:01    16:48
		//  Midnight            00:00:57    23:45

		Calendar testTime = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
		HebrewDate hebrewDate = HebrewCalendar.getForDate( testTime );
		Calendar endTest = UT_ZmanUtils.configureTestTime( 2014, Calendar.DECEMBER, 8, 0, 0 );

		runDays( testTime, endTest, perTestCallback, testResultsCallback.getExpectedOutput(),
			testResultsCallback.getExpectedNotifiedZmanim( hebrewDate ) );
	}


	@SuppressWarnings( "MagicNumber" )
	void december_21_2015_1_week(
		final ZmanCallback          perTestCallback,
		final TestResultsCallback   testResultsCallback
	) {

		// In this case, the user never turns their phone off...
		Calendar testTime = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 21, 0, 1 );
		HebrewDate startTest = HebrewCalendar.getForDate( testTime );
		Calendar endTest = UT_ZmanUtils.configureTestTime( 2015, Calendar.DECEMBER, 28, 0, 0 );

		runDays( testTime, endTest, perTestCallback, testResultsCallback.getExpectedOutput(),
			testResultsCallback.getExpectedNotifiedZmanim( startTest ) );
	}


	private void runDays(
		final Calendar                          testTime,
		final Calendar                          endDate,
		final ZmanCallback                      perTestCallback,
		final String                            expected,
	  final Map<HebrewDate,List<Zmanim.Type>> expectedZmanim
	) {

		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time              | Action                                        | Next (M:S:ms) |\n" );
		output.append( "------------------|-----------------------------------------------|---------------|\n" );


		RunDaysZmanCallback zmanCallback = new RunDaysZmanCallback( perTestCallback, testTime, output, endDate );

		while ( !zmanCallback.testComplete ) {

			output.append( String.format( "%1$tD %1$tH:%1$tM:%1$tS |", testTime ) );

			final HebrewDate forDate = HebrewCalendar.getForDate( testTime );
			NotifiedZmanim notifiedZmanim = zmanCallback.notifiedZmanim.get( forDate );
			if ( notifiedZmanim == null ) {
				notifiedZmanim = new NotifiedZmanim( testTime );
				zmanCallback.notifiedZmanim.put( forDate, notifiedZmanim );
			}

			ZmanNotificationScheduler.run( seattle, testTime, zmanCallback );

			output.append( "\n" );
		}


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
		public boolean hasBeenNotified(
			Calendar  now,
			Zman      zman
		) {

			final NotifiedZmanim notifiedForDay = notifiedZmanim.get( HebrewCalendar.getForDate( now ) );
			return notifiedForDay != null && notifiedForDay.contains( zman.type );
		}


		@Override
		public void notifyUser(
			Calendar  now,
			Zman      nextZman
		) {

			NotifiedZmanim notifiedForDay = getNotifiedZmanim( now );
			notifiedForDay.notified( nextZman );

			notified = true;

			long delta = nextZman.getTime().getTimeInMillis() - now.getTimeInMillis();
			long minUntilZman = ( delta / ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) );
			long remainingMs = ( delta - ( minUntilZman * ( ZmanNotificationScheduler.MIN_TO_MS_CONVERSION ) ) );
			long secs = ( remainingMs / 1000 );
			remainingMs = ( remainingMs - secs * 1000 );

			output.append( String.format( " %-45s |", "Notify: " + nextZman.type + "@" +
				zmanFormatter.formatZmanTime( nextZman ) + " (" + minUntilZman + ":" + secs + ":" + remainingMs + ")" ) );

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

} // End of WakeupSchedulerBase_MutliDay class
