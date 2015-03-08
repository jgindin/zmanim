/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanim;

import com.gindin.zmanlib.ZmanUtils;
import com.gindin.zmanlib.calendar.Constants;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
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
	public void december_7_2014() {

		// I'm going to try and be somewhat realistic here. We'll say someone woke up and turned on their
		//  phone at 6:34am...
		testTime.set( 2014, Calendar.DECEMBER, 7, 6, 34, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		HebrewDate hebrewDate = HebrewCalendar.getForDate( testTime );


		//////////////////////////////////////////////////////
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


		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time      | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" );
		output.append( "----------|-----------------------------------------------|-----------------|----------------|---------------|\n" );

		final int testCompleteIndex = 0;
		final int notifiedIndex = 1;
		final boolean[] testFlags = new boolean[2];

		final ZmanFormatter zmanFormatter = new ZmanFormatter( true );
		ZmanUtils.PreferredZmanTypeLookup lookup = new ZmanUtils.PreferredZmanTypeLookup() {
			@Override
			public String getPreferredZman( Zmanim.Type type ) {
				return null;
			}
		};

		final ZmanUtils.NotifiedZmanim notifiedZmanim = new ZmanUtils.NotifiedZmanim( testTime );

		ZmanUtils.ZmanNotificationCallback zmanNotificationCallback = new ZmanUtils.ZmanNotificationCallback() {
			@Override
			public void notifyUser(
				final Zman nextZman
			) {
				testFlags[ notifiedIndex ] = true;

				long delta = nextZman.getTime().getTimeInMillis() - testTime.getTimeInMillis();
				long minUntilZman = ( delta / ( 1000 * 60 ) );
				long remainingMs = ( delta - ( minUntilZman * ( 1000 * 60 ) ) );
				long secs = ( remainingMs / 1000 );
				remainingMs = ( remainingMs - secs * 1000 );

				output.append( String.format( " %-45s |", "Notify: " + nextZman.type + "@"
					+ zmanFormatter.formatZmanTime( nextZman ) + " (" + minUntilZman + ":" + secs + ":" + remainingMs + ")" ) );

				if ( nextZman.type == Zmanim.Type.NightChatzot ) {
					testFlags[ testCompleteIndex ] = true;
				}
			}

			@Override
			public void scheduleNextCheck(
				final Calendar  now,
				final int       timeUntilNextCheck,
				final Zman      nextZman
			) {

				if ( !testFlags[ notifiedIndex ] ) {
					output.append( String.format( " %-45s |", "Sleep" ) );
				}

				output.append( String.format( " %-15s |", nextZman.type ) );
				output.append( String.format( " %14s | ",  zmanFormatter.formatZmanTime( nextZman ) ) );

				int minUntilNextCheck = ( timeUntilNextCheck / ( 1000 * 60 ) );
				int remainingMs = ( timeUntilNextCheck - ( minUntilNextCheck * ( 1000 * 60 ) ) );
				int secs = ( remainingMs / 1000 );
				remainingMs = ( remainingMs - secs * 1000 );

				output.append( String.format( " %02d:%02d:%03d    |", minUntilNextCheck, secs, remainingMs ) );
				testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );
			}
		};



		while ( !testFlags[ testCompleteIndex ] ) {

			testFlags[ notifiedIndex ] = false;
			output.append( String.format( " %1$tH:%1$tM:%1$tS |", testTime ) );
			ZmanUtils.nextZmanAction( seattle, testTime, notifiedZmanim, thirtyMinutesInterval, fifteenMinutesNotification,
				lookup, zmanNotificationCallback );
			output.append( "\n" );
		}


		String expected =
			"Time      | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"----------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			" 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | Hanetz          |     7:42:53 AM |  30:00:000    |\n" +
			" 07:04:00 | Sleep                                         | Hanetz          |     7:42:53 AM |  23:53:481    |\n" +
			" 07:27:53 | Notify: Hanetz@7:42:53 AM (15:0:0)            | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			" 07:57:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			" 08:27:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  18:46:642    |\n" +
			" 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			" 09:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			" 09:46:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			" 10:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  02:55:558    |\n" +
			" 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			" 10:49:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			" 11:19:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  25:51:100    |\n" +
			" 11:45:26 | Notify: Chatzot@12:00:26 PM (15:0:0)          | MinchaGedola    |    12:21:54 PM |  21:27:775    |\n" +
			" 12:06:54 | Notify: MinchaGedola@12:21:54 PM (15:0:0)     | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			" 12:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			" 13:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			" 13:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			" 14:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  08:46:650    |\n" +
			" 14:15:41 | Notify: MinchaKetana@2:30:41 PM (15:0:0)      | PlagHaMincha    |     3:24:20 PM |  30:00:000    |\n" +
			" 14:45:41 | Sleep                                         | PlagHaMincha    |     3:24:20 PM |  23:39:437    |\n" +
			" 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | Shkia           |     4:18:00 PM |  30:00:000    |\n" +
			" 15:39:20 | Sleep                                         | Shkia           |     4:18:00 PM |  23:39:443    |\n" +
			" 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | LatestMincha    |     4:27:39 PM |  09:39:487    |\n" +
			" 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | BeinHashmashot  |     4:52:04 PM |  24:24:788    |\n" +
			" 16:37:04 | Notify: BeinHashmashot@4:52:04 PM (15:0:0)    | TzaitHacochavim |     5:10:02 PM |  17:57:775    |\n" +
			" 16:55:02 | Notify: TzaitHacochavim@5:10:02 PM (15:0:0)   | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 17:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 17:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 18:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 18:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 19:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 19:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 20:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 20:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 21:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 21:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 22:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 22:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			" 23:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  20:56:081    |\n" +
			" 23:45:58 | Notify: NightChatzot@12:00:58 AM (15:0:0)     | Alot            |     6:03:46 AM |  30:00:000    |\n";

		Assert.assertEquals( expected, output.toString() );

		// Make sure we notified for all the zmanim of the day...
		List<Zmanim> todaysZmanim = hebrewDate.getZmanim( seattle );
		for ( Zmanim zmanim : todaysZmanim ) {

			// We know that this test doesn't include Alot Hashachar...
			if ( zmanim.type == Zmanim.Type.Alot ) {
				Assert.assertFalse( notifiedZmanim.contains( zmanim.type ) );
			}
			else {
				Assert.assertTrue( notifiedZmanim.contains( zmanim.type ) );
			}
		}
	}

}
