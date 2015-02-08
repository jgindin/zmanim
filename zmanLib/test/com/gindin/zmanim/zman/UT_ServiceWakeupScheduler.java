/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanim.zman;

import com.gindin.zmanim.calendar.Constants;
import com.gindin.zmanim.calendar.HebrewCalendar;
import com.gindin.zmanim.calendar.HebrewDate;
import com.gindin.zmanim.location.ZmanimLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Various tests to ensure that we're calculating the correct time to schedule the next wakeup when we'll check
 *  to see if we ought to alert the user.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ServiceWakeupScheduler {

	private static final ZmanimLocation                       seattle;

	private static final int                                  thirtyMinutesInterval = ( 30 * 60 * 1000 );
	private static final int                                  fifteenMinutesNotification = ( 15 * 60 * 1000 );

	static {

		seattle = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();
	}


	private static Zman findDefaultZman(
		Zmanim.Type     type,
		ZmanimLocation  location,
	  int             year,
		@Constants.CalendarMonth
		int             month,
	  int             day
	) {

		HebrewDate hebrewDate = HebrewCalendar.getForDate( year, month, day );
		List<Zmanim> zmanimForDate = hebrewDate.getZmanim( location );
		Zman zman = null;
		for ( Zmanim zmanim : zmanimForDate ) {
			if ( zmanim.type == type ) {
				zman = zmanim.getDefault();
				break;
			}
		}

		Assert.assertNotNull( zman );
		return zman;
	}


	private Zman                                              shkia;

	private Calendar                                          testTime;



	@Before
	public void setup() {

		// I'm picking this date because on my way home I counted at least 46 reminders of shkia.
		//  That's a bug I really don't see any reason to release ;-)
		// On that day in Seattle:
		//  Plag HaMincha was 4:13pm
		//  Shkia was 5:13pm

		shkia = findDefaultZman( Zmanim.Type.Shkia, seattle, 2015, Calendar.FEBRUARY, 3 );
		Assert.assertEquals( 1423012414064L, shkia.getTime().getTime().getTime() );

		testTime = Calendar.getInstance();
	}


	@Test
	public void testNextZmanIsAfterWakeupInterval() {

		testTime.set( 2015, Calendar.FEBRUARY, 3, 16, 20 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );

		// We set the clock to 4:20pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is 38 minutes. Since we're testing with a 30 minute check interval, the next check ought
		//  to be in 30 minutes...
		Assert.assertEquals( thirtyMinutesInterval, timeUntilNextCheck );
	}


	@Test
	public void testNextZmanIsBeforeWakeupInterval() {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		testTime.set( 2015, Calendar.FEBRUARY, 3, 16, 40, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );

		// We set the clock to 4:40pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is 18 minutes. Since we're testing with a 30 minute check interval, the next check ought
		//  to be in 18 about minutes...
		final int expectedTimeUntilNextCheck = 1114064; // 18*60*1000==1080000 ... the actual result is just over 18 minutes...

		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );
	}


	@Test
	public void testNextZmanIsVerySoon() {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		testTime.set( 2015, Calendar.FEBRUARY, 3, 16, 50, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );

		// We set the clock to 4:50pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is roughly 8 minutes.
		final int expectedTimeUntilNextCheck = 514064;

		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );
	}


	@Test
	public void testAsTimeGoesOn() {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		testTime.set( 2015, Calendar.FEBRUARY, 3, 16, 14, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		// We're going to start at 4:14pm, which should result in wakeups at:
		//  4:44pm
		//  4:58pm

		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		int expectedTimeUntilNextCheck = thirtyMinutesInterval;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

		// It should now be 4:44pm, leaving 14 minutes until the next wake-up, which is when we would expect the alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		expectedTimeUntilNextCheck = 874064; // About 14 minutes...
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

		// It should now be 4:58pm, right when we want to alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		Assert.assertEquals( 0, timeUntilNextCheck );
	}

	@Test
	public void testAsTimeGoesOnInexact() {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		testTime.set( 2015, Calendar.FEBRUARY, 3, 16, 14, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		// We're going to start at 4:14pm, which should result in wakeups at:
		//  4:44pm
		//  4:58pm

		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		int expectedTimeUntilNextCheck = thirtyMinutesInterval;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time, but assume that we don't awake exactly when we wanted to...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 2000 );

		// It should now be 4:44pm, leaving 14 minutes until the next wake-up, which is when we would expect the alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		expectedTimeUntilNextCheck = 872064; // About 14 minutes...
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 3333 );

		// It should now be 4:58pm, right when we want to alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );
		Assert.assertTrue( timeUntilNextCheck < 0 );
	}


	@Test
	public void testAWholeDay() {

		ZmanUtils.PreferredZmanTypeLookup lookup = new ZmanUtils.PreferredZmanTypeLookup() {
			@Override
			public String getPreferredZman( Zmanim.Type type ) {
				return null;
			}
		};


		// I'm going to try and be somewhat realistic here. We'll say someone woke up and turned on their
		//  phone at 6:14am...
		testTime.set( 2015, Calendar.OCTOBER, 5, 6, 14, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		// What zman is next? It's Tallit, and it happens at 6:17:49. That means we can't alert the user 15 minutes
		//  ahead of time, so we ought to alert ASAP.
		Zman nextZman = ZmanUtils.findNextZman( seattle, testTime, lookup );
		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		Assert.assertTrue( timeUntilNextCheck < 0 );

		// Because there wasn't enough time and we alerted the user, we find the next zman based on the time of
		//  the one which we just alerted on. Now, this next zman is HaNetz which is at 7:13:25, which means we
		//  want to alert at 6:58. Since it's now 6:14, we'll wait the full interval before the next check.
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		int expectedTimeUntilNextCheck = thirtyMinutesInterval;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Move ahead so that it's now about 6:44. Since that's about 14 minutes until when we want to alert,
		//  that's how long we should go until the next check.
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 14231 );
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		expectedTimeUntilNextCheck = 850818;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// The time is now about 6:58...
		// Now we should be up to Sof Zman Kriat Shema, which is at 9:19:47, meaning an alert is around 9:04.
		//  That gives us a few full intervals...
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		runFullIntervals( nextZman, 5, 1129262 );

		// The time should now be around 9:04.
		// Next up is Sof Zman Tefila, which is at 11:02:30, meaning an alert is around 10:47.
		Assert.assertEquals( 1444061087403L, testTime.getTime().getTime() );
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		runFullIntervals( nextZman, 4, 710013 );

		// Now, it's about 10:47. Next up is mid-day, around 12:57, so alert is around 12:42.
		Assert.assertEquals( 1444067250525L, testTime.getTime().getTime() );
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		runFullIntervals( nextZman, 4, 1419629 );

		// Now it's about 12:42. Next up is Mincha Gedola at 13:25:41, for which we alert around 13:10.
		Assert.assertEquals( 1444074123263L, testTime.getTime().getTime() );
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		expectedTimeUntilNextCheck = 1718184;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Now it's about 13:10. Next up is Mincha Ketana at 16:17:30, for which we alert around 16:02.
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 1231 );
		Assert.assertEquals( 1444075842678L, testTime.getTime().getTime() );
		nextZman = ZmanUtils.findNextZman( seattle, nextZman.getTime(), lookup );
		runFullIntervals( nextZman, 6, 1213932 );
	}


	private void runFullIntervals(
		Zman  nextZman,
	  int   expectedIterations,
	  int   expectedTimeUntilNextCheckForLastIteration
	) {

		final int [] deltaToSimulateNonPreciseWakeups = new int[] {
			0,
			2093,
			42982,
			39812,
			8034,
			19023,
			1023,
			22,
			56819
		};

		// Run the first iteration here...
		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		Assert.assertEquals( thirtyMinutesInterval, timeUntilNextCheck );

		for ( int i = 1; i < ( expectedIterations - 1 ); i++ ) {

			testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + deltaToSimulateNonPreciseWakeups[i] );
			timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
			Assert.assertEquals( "Interval #" + i + " of " + expectedIterations, thirtyMinutesInterval, timeUntilNextCheck );
		}

		// Now run the last iteration...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + deltaToSimulateNonPreciseWakeups[ expectedIterations ] );
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( nextZman, testTime, thirtyMinutesInterval, fifteenMinutesNotification );
		Assert.assertEquals( "Interval #" + expectedIterations + " of " + expectedIterations, expectedTimeUntilNextCheckForLastIteration, timeUntilNextCheck );

		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );
	}

}
