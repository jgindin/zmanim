/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

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

import java.util.Arrays;
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



	@Before
	public void setup() {

		// I'm picking this date because on my way home I counted at least 46 reminders of shkia.
		//  That's a bug I really don't see any reason to release ;-)
		// On that day in Seattle:
		//  Plag HaMincha was 4:13pm
		//  Shkia was 5:13pm

		shkia = findDefaultZman( Zmanim.Type.Shkia, seattle, 2015, Calendar.FEBRUARY, 3 );
		Assert.assertEquals( 1423012414064L, shkia.getTime().getTime().getTime() );
	}


	@Test
	public void testNextZmanIsAfterWakeupInterval() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 20 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );

		// We set the clock to 4:20pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is 38 minutes. Since we're testing with a 30 minute check interval, the next check ought
		//  to be in 30 minutes...
		Assert.assertEquals( thirtyMinutesInterval, timeUntilNextCheck );
	}


	@Test
	public void testNextZmanIsBeforeWakeupInterval() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 40 );

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

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 50 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,
			fifteenMinutesNotification );

		// We set the clock to 4:50pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is roughly 8 minutes.
		final int expectedTimeUntilNextCheck = 514064;

		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );
	}


	@Test
	public void testAsTimeGoesOn() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 14 );

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

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 14 );

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

		Calendar testTime = configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
		HebrewDate hebrewDate = HebrewCalendar.getForDate( testTime );

		runDays( testTime, hebrewDate, hebrewDate,
			"Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			"12/07/14 06:34:00 | Notify: Talit@6:39:55 AM (5:55:244)           | Hanetz          |     7:42:53 AM |  30:00:000    |\n" +
			"12/07/14 07:04:00 | Sleep                                         | Hanetz          |     7:42:53 AM |  23:53:481    |\n" +
			"12/07/14 07:27:53 | Notify: Hanetz@7:42:53 AM (15:0:0)            | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 07:57:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  30:00:000    |\n" +
			"12/07/14 08:27:53 | Sleep                                         | SofZmanShema    |     9:01:40 AM |  18:46:642    |\n" +
			"12/07/14 08:46:40 | Notify: SofZmanShema@9:01:40 AM (15:0:0)      | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 09:46:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  30:00:000    |\n" +
			"12/07/14 10:16:40 | Sleep                                         | SofZmanTefila   |    10:34:35 AM |  02:55:558    |\n" +
			"12/07/14 10:19:35 | Notify: SofZmanTefila@10:34:35 AM (15:0:0)    | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 10:49:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  30:00:000    |\n" +
			"12/07/14 11:19:35 | Sleep                                         | Chatzot         |    12:00:26 PM |  25:51:100    |\n" +
			"12/07/14 11:45:26 | Notify: Chatzot@12:00:26 PM (15:0:0)          | MinchaGedola    |    12:21:54 PM |  21:27:775    |\n" +
			"12/07/14 12:06:54 | Notify: MinchaGedola@12:21:54 PM (15:0:0)     | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 12:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 13:36:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  30:00:000    |\n" +
			"12/07/14 14:06:54 | Sleep                                         | MinchaKetana    |     2:30:41 PM |  08:46:650    |\n" +
			"12/07/14 14:15:41 | Notify: MinchaKetana@2:30:41 PM (15:0:0)      | PlagHaMincha    |     3:24:20 PM |  30:00:000    |\n" +
			"12/07/14 14:45:41 | Sleep                                         | PlagHaMincha    |     3:24:20 PM |  23:39:437    |\n" +
			"12/07/14 15:09:20 | Notify: PlagHaMincha@3:24:20 PM (15:0:0)      | Shkia           |     4:18:00 PM |  30:00:000    |\n" +
			"12/07/14 15:39:20 | Sleep                                         | Shkia           |     4:18:00 PM |  23:39:443    |\n" +
			"12/07/14 16:03:00 | Notify: Shkia@4:18:00 PM (15:0:0)             | LatestMincha    |     4:27:39 PM |  09:39:487    |\n" +
			"12/07/14 16:12:39 | Notify: LatestMincha@4:27:39 PM (15:0:0)      | BeinHashmashot  |     4:52:04 PM |  24:24:788    |\n" +
			"12/07/14 16:37:04 | Notify: BeinHashmashot@4:52:04 PM (15:0:0)    | TzaitHacochavim |     5:10:02 PM |  17:57:775    |\n" +
			"12/07/14 16:55:02 | Notify: TzaitHacochavim@5:10:02 PM (15:0:0)   | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 17:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 17:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 18:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 19:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 20:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 21:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 22:55:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  30:00:000    |\n" +
			"12/07/14 23:25:02 | Sleep                                         | NightChatzot    |    12:00:58 AM |  20:56:081    |\n" +
			"12/07/14 23:45:58 | Notify: NightChatzot@12:00:58 AM (15:0:0)     | Alot            |     6:03:46 AM |  30:00:000    |\n",

			Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot,
				Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
				Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);
	}


	@Test
	public void december_21_2015_1_week() {

		// In this case, the user never turns their phone off...
		Calendar testTime = configureTestTime( 2015, Calendar.DECEMBER, 21, 0, 1 );
		HebrewDate startTest = HebrewCalendar.getForDate( testTime );
		HebrewDate endTest = HebrewCalendar.getForDate( 2015, Calendar.DECEMBER, 28 );

		runDays( testTime, startTest, endTest,
			"Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" +
			"------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" +
			"12/21/15 00:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 00:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 01:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 01:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 02:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 02:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 03:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 03:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 04:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 04:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 05:01:00 | Sleep                                         | Alot            |     6:13:06 AM |  30:00:000    |\n" +
			"12/21/15 05:31:00 | Sleep                                         | Alot            |     6:13:06 AM |  27:06:204    |\n" +
			"12/21/15 05:58:06 | Notify: Alot@6:13:06 AM (15:0:0)              | Talit           |     6:50:27 AM |  30:00:000    |\n" +
			"12/21/15 06:28:06 | Sleep                                         | Talit           |     6:50:27 AM |  07:21:279    |\n" +
			"12/21/15 06:35:27 | Notify: Talit@6:50:27 AM (15:0:0)             | Hanetz          |     7:54:13 AM |  30:00:000    |\n" +
			"12/21/15 07:05:27 | Sleep                                         | Hanetz          |     7:54:13 AM |  30:00:000    |\n" +
			"12/21/15 07:35:27 | Sleep                                         | Hanetz          |     7:54:13 AM |  03:45:549    |\n" +
			"12/21/15 07:39:13 | Notify: Hanetz@7:54:13 AM (15:0:0)            | SofZmanShema    |     9:10:09 AM |  30:00:000    |\n" +
			"12/21/15 08:09:13 | Sleep                                         | SofZmanShema    |     9:10:09 AM |  30:00:000    |\n" +
			"12/21/15 08:39:13 | Sleep                                         | SofZmanShema    |     9:10:09 AM |  15:56:685    |\n" +
			"12/21/15 08:55:09 | Notify: SofZmanShema@9:10:09 AM (15:0:0)      | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 09:25:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 09:55:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  30:00:000    |\n" +
			"12/21/15 10:25:09 | Sleep                                         | SofZmanTefila   |    10:42:53 AM |  02:43:411    |\n" +
			"12/21/15 10:27:53 | Notify: SofZmanTefila@10:42:53 AM (15:0:0)    | Chatzot         |    12:07:13 PM |  30:00:000    |\n" +
			"12/21/15 10:57:53 | Sleep                                         | Chatzot         |    12:07:13 PM |  30:00:000    |\n" +
			"12/21/15 11:27:53 | Sleep                                         | Chatzot         |    12:07:13 PM |  24:20:048    |\n" +
			"12/21/15 11:52:13 | Notify: Chatzot@12:07:13 PM (15:0:0)          | MinchaGedola    |    12:28:18 PM |  21:05:012    |\n" +
			"12/21/15 12:13:18 | Notify: MinchaGedola@12:28:18 PM (15:0:0)     | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 12:43:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 13:13:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 13:43:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  30:00:000    |\n" +
			"12/21/15 14:13:18 | Sleep                                         | MinchaKetana    |     2:34:48 PM |  06:30:072    |\n" +
			"12/21/15 14:19:48 | Notify: MinchaKetana@2:34:48 PM (15:0:0)      | PlagHaMincha    |     3:27:30 PM |  30:00:000    |\n" +
			"12/21/15 14:49:48 | Sleep                                         | PlagHaMincha    |     3:27:30 PM |  22:42:530    |\n" +
			"12/21/15 15:12:30 | Notify: PlagHaMincha@3:27:30 PM (15:0:0)      | Shkia           |     4:20:13 PM |  30:00:000    |\n" +
			"12/21/15 15:42:30 | Sleep                                         | Shkia           |     4:20:13 PM |  22:42:532    |\n" +
			"12/21/15 16:05:13 | Notify: Shkia@4:20:13 PM (15:0:0)             | LatestMincha    |     4:29:42 PM |  09:29:254    |\n" +
			"12/21/15 16:14:42 | Notify: LatestMincha@4:29:42 PM (15:0:0)      | BeinHashmashot  |     4:54:37 PM |  24:55:135    |\n" +
			"12/21/15 16:39:37 | Notify: BeinHashmashot@4:54:37 PM (15:0:0)    | TzaitHacochavim |     5:12:53 PM |  18:15:957    |\n" +
			"12/21/15 16:57:53 | Notify: TzaitHacochavim@5:12:53 PM (15:0:0)   | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 17:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 17:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 18:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 18:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 19:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 19:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 20:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 20:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 21:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 21:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 22:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 22:57:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  30:00:000    |\n" +
			"12/21/15 23:27:53 | Sleep                                         | NightChatzot    |    12:07:28 AM |  24:34:557    |\n" +
			"12/21/15 23:52:28 | Notify: NightChatzot@12:07:28 AM (15:0:0)     | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 00:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 00:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 01:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 01:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 02:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 02:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 03:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 03:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 04:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 04:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 05:22:28 | Sleep                                         | Alot            |     6:13:36 AM |  30:00:000    |\n" +
			"12/22/15 05:52:28 | Sleep                                         | Alot            |     6:13:36 AM |  06:08:007    |\n" +
			"12/22/15 05:58:36 | Notify: Alot@6:13:36 AM (15:0:0)              | Talit           |     6:50:57 AM |  30:00:000    |\n" +
			"12/22/15 06:28:36 | Sleep                                         | Talit           |     6:50:57 AM |  07:21:297    |\n" +
			"12/22/15 06:35:57 | Notify: Talit@6:50:57 AM (15:0:0)             | Hanetz          |     7:54:43 AM |  30:00:000    |\n" +
			"12/22/15 07:05:57 | Sleep                                         | Hanetz          |     7:54:43 AM |  30:00:000    |\n" +
			"12/22/15 07:35:57 | Sleep                                         | Hanetz          |     7:54:43 AM |  03:45:599    |\n" +
			"12/22/15 07:39:43 | Notify: Hanetz@7:54:43 AM (15:0:0)            | SofZmanShema    |     9:10:39 AM |  30:00:000    |\n" +
			"12/22/15 08:09:43 | Sleep                                         | SofZmanShema    |     9:10:39 AM |  30:00:000    |\n" +
			"12/22/15 08:39:43 | Sleep                                         | SofZmanShema    |     9:10:39 AM |  15:56:767    |\n" +
			"12/22/15 08:55:39 | Notify: SofZmanShema@9:10:39 AM (15:0:0)      | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 09:25:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 09:55:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  30:00:000    |\n" +
			"12/22/15 10:25:39 | Sleep                                         | SofZmanTefila   |    10:43:23 AM |  02:43:589    |\n" +
			"12/22/15 10:28:23 | Notify: SofZmanTefila@10:43:23 AM (15:0:0)    | Chatzot         |    12:07:43 PM |  30:00:000    |\n" +
			"12/22/15 10:58:23 | Sleep                                         | Chatzot         |    12:07:43 PM |  30:00:000    |\n" +
			"12/22/15 11:28:23 | Sleep                                         | Chatzot         |    12:07:43 PM |  24:20:178    |\n" +
			"12/22/15 11:52:43 | Notify: Chatzot@12:07:43 PM (15:0:0)          | MinchaGedola    |    12:28:48 PM |  21:05:044    |\n" +
			"12/22/15 12:13:48 | Notify: MinchaGedola@12:28:48 PM (15:0:0)     | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 12:43:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 13:13:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 13:43:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  30:00:000    |\n" +
			"12/22/15 14:13:48 | Sleep                                         | MinchaKetana    |     2:35:18 PM |  06:30:267    |\n" +
			"12/22/15 14:20:18 | Notify: MinchaKetana@2:35:18 PM (15:0:0)      | PlagHaMincha    |     3:28:01 PM |  30:00:000    |\n" +
			"12/22/15 14:50:18 | Sleep                                         | PlagHaMincha    |     3:28:01 PM |  22:42:611    |\n" +
			"12/22/15 15:13:01 | Notify: PlagHaMincha@3:28:01 PM (15:0:0)      | Shkia           |     4:20:44 PM |  30:00:000    |\n" +
			"12/22/15 15:43:01 | Sleep                                         | Shkia           |     4:20:44 PM |  22:42:619    |\n" +
			"12/22/15 16:05:44 | Notify: Shkia@4:20:44 PM (15:0:0)             | LatestMincha    |     4:30:13 PM |  09:29:268    |\n" +
			"12/22/15 16:15:13 | Notify: LatestMincha@4:30:13 PM (15:0:0)      | FastEnd         |     4:50:44 PM |  20:30:732    |\n" +
			"12/22/15 16:35:44 | Notify: FastEnd@4:50:44 PM (15:0:0)           | BeinHashmashot  |     4:55:08 PM |  04:24:411    |\n" +
			"12/22/15 16:40:08 | Notify: BeinHashmashot@4:55:08 PM (15:0:0)    | TzaitHacochavim |     5:13:24 PM |  18:15:778    |\n" +
			"12/22/15 16:58:24 | Notify: TzaitHacochavim@5:13:24 PM (15:0:0)   | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 17:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 17:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 18:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 18:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 19:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 19:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 20:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 20:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 21:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 21:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 22:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 22:58:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  30:00:000    |\n" +
			"12/22/15 23:28:24 | Sleep                                         | NightChatzot    |    12:07:57 AM |  24:32:972    |\n" +
			"12/22/15 23:52:57 | Notify: NightChatzot@12:07:57 AM (15:0:0)     | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 00:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 00:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 01:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 01:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 02:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 02:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 03:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 03:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 04:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 04:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 05:22:57 | Sleep                                         | Alot            |     6:14:04 AM |  30:00:000    |\n" +
			"12/23/15 05:52:57 | Sleep                                         | Alot            |     6:14:04 AM |  06:06:834    |\n" +
			"12/23/15 05:59:04 | Notify: Alot@6:14:04 AM (15:0:0)              | Talit           |     6:51:25 AM |  30:00:000    |\n" +
			"12/23/15 06:29:04 | Sleep                                         | Talit           |     6:51:25 AM |  07:21:144    |\n" +
			"12/23/15 06:36:25 | Notify: Talit@6:51:25 AM (15:0:0)             | Hanetz          |     7:55:10 AM |  30:00:000    |\n" +
			"12/23/15 07:06:25 | Sleep                                         | Hanetz          |     7:55:10 AM |  30:00:000    |\n" +
			"12/23/15 07:36:25 | Sleep                                         | Hanetz          |     7:55:10 AM |  03:45:184    |\n" +
			"12/23/15 07:40:10 | Notify: Hanetz@7:55:10 AM (15:0:0)            | SofZmanShema    |     9:11:09 AM |  30:00:000    |\n" +
			"12/23/15 08:10:10 | Sleep                                         | SofZmanShema    |     9:11:09 AM |  30:00:000    |\n" +
			"12/23/15 08:40:10 | Sleep                                         | SofZmanShema    |     9:11:09 AM |  15:58:523    |\n" +
			"12/23/15 08:56:09 | Notify: SofZmanShema@9:11:09 AM (15:0:0)      | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 09:26:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 09:56:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  30:00:000    |\n" +
			"12/23/15 10:26:09 | Sleep                                         | SofZmanTefila   |    10:43:52 AM |  02:43:901    |\n" +
			"12/23/15 10:28:52 | Notify: SofZmanTefila@10:43:52 AM (15:0:0)    | Chatzot         |    12:08:14 PM |  30:00:000    |\n" +
			"12/23/15 10:58:52 | Sleep                                         | Chatzot         |    12:08:14 PM |  30:00:000    |\n" +
			"12/23/15 11:28:52 | Sleep                                         | Chatzot         |    12:08:14 PM |  24:21:212    |\n" +
			"12/23/15 11:53:14 | Notify: Chatzot@12:08:14 PM (15:0:0)          | MinchaGedola    |    12:29:19 PM |  21:05:303    |\n" +
			"12/23/15 12:14:19 | Notify: MinchaGedola@12:29:19 PM (15:0:0)     | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 12:44:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 13:14:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 13:44:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  30:00:000    |\n" +
			"12/23/15 14:14:19 | Sleep                                         | MinchaKetana    |     2:35:51 PM |  06:31:818    |\n" +
			"12/23/15 14:20:51 | Notify: MinchaKetana@2:35:51 PM (15:0:0)      | PlagHaMincha    |     3:28:34 PM |  30:00:000    |\n" +
			"12/23/15 14:50:51 | Sleep                                         | PlagHaMincha    |     3:28:34 PM |  22:43:257    |\n" +
			"12/23/15 15:13:34 | Notify: PlagHaMincha@3:28:34 PM (15:0:0)      | Shkia           |     4:21:17 PM |  30:00:000    |\n" +
			"12/23/15 15:43:34 | Sleep                                         | Shkia           |     4:21:17 PM |  22:43:266    |\n" +
			"12/23/15 16:06:17 | Notify: Shkia@4:21:17 PM (15:0:0)             | LatestMincha    |     4:30:47 PM |  09:29:376    |\n" +
			"12/23/15 16:15:47 | Notify: LatestMincha@4:30:47 PM (15:0:0)      | BeinHashmashot  |     4:55:42 PM |  24:54:858    |\n" +
			"12/23/15 16:40:42 | Notify: BeinHashmashot@4:55:42 PM (15:0:0)    | TzaitHacochavim |     5:13:57 PM |  18:15:395    |\n" +
			"12/23/15 16:58:57 | Notify: TzaitHacochavim@5:13:57 PM (15:0:0)   | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 17:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 17:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 18:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 18:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 19:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 19:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 20:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 20:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 21:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 21:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 22:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 22:58:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  30:00:000    |\n" +
			"12/23/15 23:28:57 | Sleep                                         | NightChatzot    |    12:08:26 AM |  24:29:064    |\n" +
			"12/23/15 23:53:26 | Notify: NightChatzot@12:08:26 AM (15:0:0)     | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 00:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 00:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 01:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 01:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 02:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 02:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 03:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 03:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 04:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 04:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 05:23:26 | Sleep                                         | Alot            |     6:14:30 AM |  30:00:000    |\n" +
			"12/24/15 05:53:26 | Sleep                                         | Alot            |     6:14:30 AM |  06:03:569    |\n" +
			"12/24/15 05:59:30 | Notify: Alot@6:14:30 AM (15:0:0)              | Talit           |     6:51:50 AM |  30:00:000    |\n" +
			"12/24/15 06:29:30 | Sleep                                         | Talit           |     6:51:50 AM |  07:20:820    |\n" +
			"12/24/15 06:36:50 | Notify: Talit@6:51:50 AM (15:0:0)             | Hanetz          |     7:55:35 AM |  30:00:000    |\n" +
			"12/24/15 07:06:50 | Sleep                                         | Hanetz          |     7:55:35 AM |  30:00:000    |\n" +
			"12/24/15 07:36:50 | Sleep                                         | Hanetz          |     7:55:35 AM |  03:44:304    |\n" +
			"12/24/15 07:40:35 | Notify: Hanetz@7:55:35 AM (15:0:0)            | SofZmanShema    |     9:11:37 AM |  30:00:000    |\n" +
			"12/24/15 08:10:35 | Sleep                                         | SofZmanShema    |     9:11:37 AM |  30:00:000    |\n" +
			"12/24/15 08:40:35 | Sleep                                         | SofZmanShema    |     9:11:37 AM |  16:01:953    |\n" +
			"12/24/15 08:56:37 | Notify: SofZmanShema@9:11:37 AM (15:0:0)      | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 09:26:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 09:56:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  30:00:000    |\n" +
			"12/24/15 10:26:37 | Sleep                                         | SofZmanTefila   |    10:44:21 AM |  02:44:347    |\n" +
			"12/24/15 10:29:21 | Notify: SofZmanTefila@10:44:21 AM (15:0:0)    | Chatzot         |    12:08:44 PM |  30:00:000    |\n" +
			"12/24/15 10:59:21 | Sleep                                         | Chatzot         |    12:08:44 PM |  30:00:000    |\n" +
			"12/24/15 11:29:21 | Sleep                                         | Chatzot         |    12:08:44 PM |  24:23:150    |\n" +
			"12/24/15 11:53:44 | Notify: Chatzot@12:08:44 PM (15:0:0)          | MinchaGedola    |    12:29:50 PM |  21:05:787    |\n" +
			"12/24/15 12:14:50 | Notify: MinchaGedola@12:29:50 PM (15:0:0)     | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 12:44:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 13:14:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 13:44:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  30:00:000    |\n" +
			"12/24/15 14:14:50 | Sleep                                         | MinchaKetana    |     2:36:25 PM |  06:34:725    |\n" +
			"12/24/15 14:21:25 | Notify: MinchaKetana@2:36:25 PM (15:0:0)      | PlagHaMincha    |     3:29:09 PM |  30:00:000    |\n" +
			"12/24/15 14:51:25 | Sleep                                         | PlagHaMincha    |     3:29:09 PM |  22:44:469    |\n" +
			"12/24/15 15:14:09 | Notify: PlagHaMincha@3:29:09 PM (15:0:0)      | Shkia           |     4:21:54 PM |  30:00:000    |\n" +
			"12/24/15 15:44:09 | Sleep                                         | Shkia           |     4:21:54 PM |  22:44:471    |\n" +
			"12/24/15 16:06:54 | Notify: Shkia@4:21:54 PM (15:0:0)             | LatestMincha    |     4:31:23 PM |  09:29:592    |\n" +
			"12/24/15 16:16:23 | Notify: LatestMincha@4:31:23 PM (15:0:0)      | BeinHashmashot  |     4:56:17 PM |  24:54:267    |\n" +
			"12/24/15 16:41:17 | Notify: BeinHashmashot@4:56:17 PM (15:0:0)    | TzaitHacochavim |     5:14:32 PM |  18:14:810    |\n" +
			"12/24/15 16:59:32 | Notify: TzaitHacochavim@5:14:32 PM (15:0:0)   | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 17:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 17:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 18:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 18:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 19:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 19:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 20:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 20:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 21:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 21:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 22:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 22:59:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  30:00:000    |\n" +
			"12/24/15 23:29:32 | Sleep                                         | NightChatzot    |    12:08:55 AM |  24:22:833    |\n" +
			"12/24/15 23:53:55 | Notify: NightChatzot@12:08:55 AM (15:0:0)     | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 00:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 00:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 01:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 01:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 02:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 02:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 03:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 03:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 04:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 04:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 05:23:55 | Sleep                                         | Alot            |     6:14:53 AM |  30:00:000    |\n" +
			"12/25/15 05:53:55 | Sleep                                         | Alot            |     6:14:53 AM |  05:58:216    |\n" +
			"12/25/15 05:59:53 | Notify: Alot@6:14:53 AM (15:0:0)              | Talit           |     6:52:14 AM |  30:00:000    |\n" +
			"12/25/15 06:29:53 | Sleep                                         | Talit           |     6:52:14 AM |  07:20:325    |\n" +
			"12/25/15 06:37:14 | Notify: Talit@6:52:14 AM (15:0:0)             | Hanetz          |     7:55:57 AM |  30:00:000    |\n" +
			"12/25/15 07:07:14 | Sleep                                         | Hanetz          |     7:55:57 AM |  30:00:000    |\n" +
			"12/25/15 07:37:14 | Sleep                                         | Hanetz          |     7:55:57 AM |  03:42:961    |\n" +
			"12/25/15 07:40:57 | Notify: Hanetz@7:55:57 AM (15:0:0)            | SofZmanShema    |     9:12:04 AM |  30:00:000    |\n" +
			"12/25/15 08:10:57 | Sleep                                         | SofZmanShema    |     9:12:04 AM |  30:00:000    |\n" +
			"12/25/15 08:40:57 | Sleep                                         | SofZmanShema    |     9:12:04 AM |  16:07:049    |\n" +
			"12/25/15 08:57:04 | Notify: SofZmanShema@9:12:04 AM (15:0:0)      | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 09:27:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 09:57:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  30:00:000    |\n" +
			"12/25/15 10:27:04 | Sleep                                         | SofZmanTefila   |    10:44:49 AM |  02:44:927    |\n" +
			"12/25/15 10:29:49 | Notify: SofZmanTefila@10:44:49 AM (15:0:0)    | Chatzot         |    12:09:15 PM |  30:00:000    |\n" +
			"12/25/15 10:59:49 | Sleep                                         | Chatzot         |    12:09:15 PM |  30:00:000    |\n" +
			"12/25/15 11:29:49 | Sleep                                         | Chatzot         |    12:09:15 PM |  24:25:988    |\n" +
			"12/25/15 11:54:15 | Notify: Chatzot@12:09:15 PM (15:0:0)          | MinchaGedola    |    12:30:21 PM |  21:06:497    |\n" +
			"12/25/15 12:15:21 | Notify: MinchaGedola@12:30:21 PM (15:0:0)     | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 12:45:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 13:15:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 13:45:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  30:00:000    |\n" +
			"12/25/15 14:15:21 | Sleep                                         | MinchaKetana    |     2:37:00 PM |  06:38:982    |\n" +
			"12/25/15 14:22:00 | Notify: MinchaKetana@2:37:00 PM (15:0:0)      | PlagHaMincha    |     3:29:46 PM |  30:00:000    |\n" +
			"12/25/15 14:52:00 | Sleep                                         | PlagHaMincha    |     3:29:46 PM |  22:46:242    |\n" +
			"12/25/15 15:14:46 | Notify: PlagHaMincha@3:29:46 PM (15:0:0)      | CandleLighting  |     4:04:33 PM |  30:00:000    |\n" +
			"12/25/15 15:44:46 | Sleep                                         | CandleLighting  |     4:04:33 PM |  04:46:243    |\n" +
			"12/25/15 15:49:33 | Notify: CandleLighting@4:04:33 PM (15:0:0)    | Shkia           |     4:22:33 PM |  18:00:000    |\n" +
			"12/25/15 16:07:33 | Notify: Shkia@4:22:33 PM (15:0:0)             | LatestMincha    |     4:32:02 PM |  09:29:916    |\n" +
			"12/25/15 16:17:02 | Notify: LatestMincha@4:32:02 PM (15:0:0)      | BeinHashmashot  |     4:56:56 PM |  24:53:372    |\n" +
			"12/25/15 16:41:56 | Notify: BeinHashmashot@4:56:56 PM (15:0:0)    | TzaitHacochavim |     5:15:10 PM |  18:14:022    |\n" +
			"12/25/15 17:00:10 | Notify: TzaitHacochavim@5:15:10 PM (15:0:0)   | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 17:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 18:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 18:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 19:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 19:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 20:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 20:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 21:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 21:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 22:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 22:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 23:00:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  30:00:000    |\n" +
			"12/25/15 23:30:10 | Sleep                                         | NightChatzot    |    12:09:24 AM |  24:14:285    |\n" +
			"12/25/15 23:54:24 | Notify: NightChatzot@12:09:24 AM (15:0:0)     | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 00:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 00:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 01:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 01:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 02:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 02:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 03:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 03:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 04:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 04:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 05:24:24 | Sleep                                         | Alot            |     6:15:15 AM |  30:00:000    |\n" +
			"12/26/15 05:54:24 | Sleep                                         | Alot            |     6:15:15 AM |  05:50:777    |\n" +
			"12/26/15 06:00:15 | Notify: Alot@6:15:15 AM (15:0:0)              | Talit           |     6:52:35 AM |  30:00:000    |\n" +
			"12/26/15 06:30:15 | Sleep                                         | Talit           |     6:52:35 AM |  07:19:661    |\n" +
			"12/26/15 06:37:35 | Notify: Talit@6:52:35 AM (15:0:0)             | Hanetz          |     7:56:16 AM |  30:00:000    |\n" +
			"12/26/15 07:07:35 | Sleep                                         | Hanetz          |     7:56:16 AM |  30:00:000    |\n" +
			"12/26/15 07:37:35 | Sleep                                         | Hanetz          |     7:56:16 AM |  03:41:158    |\n" +
			"12/26/15 07:41:16 | Notify: Hanetz@7:56:16 AM (15:0:0)            | SofZmanShema    |     9:12:30 AM |  30:00:000    |\n" +
			"12/26/15 08:11:16 | Sleep                                         | SofZmanShema    |     9:12:30 AM |  30:00:000    |\n" +
			"12/26/15 08:41:16 | Sleep                                         | SofZmanShema    |     9:12:30 AM |  16:13:809    |\n" +
			"12/26/15 08:57:30 | Notify: SofZmanShema@9:12:30 AM (15:0:0)      | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 09:27:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 09:57:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  30:00:000    |\n" +
			"12/26/15 10:27:30 | Sleep                                         | SofZmanTefila   |    10:45:15 AM |  02:45:635    |\n" +
			"12/26/15 10:30:15 | Notify: SofZmanTefila@10:45:15 AM (15:0:0)    | Chatzot         |    12:09:45 PM |  30:00:000    |\n" +
			"12/26/15 11:00:15 | Sleep                                         | Chatzot         |    12:09:45 PM |  30:00:000    |\n" +
			"12/26/15 11:30:15 | Sleep                                         | Chatzot         |    12:09:45 PM |  24:29:722    |\n" +
			"12/26/15 11:54:45 | Notify: Chatzot@12:09:45 PM (15:0:0)          | MinchaGedola    |    12:30:52 PM |  21:07:430    |\n" +
			"12/26/15 12:15:52 | Notify: MinchaGedola@12:30:52 PM (15:0:0)     | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 12:45:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 13:15:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 13:45:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  30:00:000    |\n" +
			"12/26/15 14:15:52 | Sleep                                         | MinchaKetana    |     2:37:37 PM |  06:44:583    |\n" +
			"12/26/15 14:22:37 | Notify: MinchaKetana@2:37:37 PM (15:0:0)      | PlagHaMincha    |     3:30:25 PM |  30:00:000    |\n" +
			"12/26/15 14:52:37 | Sleep                                         | PlagHaMincha    |     3:30:25 PM |  22:48:576    |\n" +
			"12/26/15 15:15:25 | Notify: PlagHaMincha@3:30:25 PM (15:0:0)      | Shkia           |     4:23:14 PM |  30:00:000    |\n" +
			"12/26/15 15:45:25 | Sleep                                         | Shkia           |     4:23:14 PM |  22:48:586    |\n" +
			"12/26/15 16:08:14 | Notify: Shkia@4:23:14 PM (15:0:0)             | LatestMincha    |     4:32:44 PM |  09:30:334    |\n" +
			"12/26/15 16:17:44 | Notify: LatestMincha@4:32:44 PM (15:0:0)      | BeinHashmashot  |     4:57:37 PM |  24:52:186    |\n" +
			"12/26/15 16:42:37 | Notify: BeinHashmashot@4:57:37 PM (15:0:0)    | ShabbatEnd      |     5:08:14 PM |  10:37:480    |\n" +
			"12/26/15 16:53:14 | Notify: ShabbatEnd@5:08:14 PM (15:0:0)        | TzaitHacochavim |     5:15:50 PM |  07:35:556    |\n" +
			"12/26/15 17:00:50 | Notify: TzaitHacochavim@5:15:50 PM (15:0:0)   | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 17:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 18:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 18:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 19:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 19:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 20:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 20:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 21:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 21:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 22:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 22:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 23:00:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  30:00:000    |\n" +
			"12/26/15 23:30:50 | Sleep                                         | NightChatzot    |    12:09:53 AM |  24:03:429    |\n" +
			"12/26/15 23:54:53 | Notify: NightChatzot@12:09:53 AM (15:0:0)     | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 00:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 00:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 01:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 01:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 02:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 02:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 03:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 03:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 04:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 04:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 05:24:53 | Sleep                                         | Alot            |     6:15:34 AM |  30:00:000    |\n" +
			"12/27/15 05:54:53 | Sleep                                         | Alot            |     6:15:34 AM |  05:41:255    |\n" +
			"12/27/15 06:00:34 | Notify: Alot@6:15:34 AM (15:0:0)              | Talit           |     6:52:53 AM |  30:00:000    |\n" +
			"12/27/15 06:30:34 | Sleep                                         | Talit           |     6:52:53 AM |  07:18:829    |\n" +
			"12/27/15 06:37:53 | Notify: Talit@6:52:53 AM (15:0:0)             | Hanetz          |     7:56:32 AM |  30:00:000    |\n" +
			"12/27/15 07:07:53 | Sleep                                         | Hanetz          |     7:56:32 AM |  30:00:000    |\n" +
			"12/27/15 07:37:53 | Sleep                                         | Hanetz          |     7:56:32 AM |  03:38:901    |\n" +
			"12/27/15 07:41:32 | Notify: Hanetz@7:56:32 AM (15:0:0)            | SofZmanShema    |     9:12:54 AM |  30:00:000    |\n" +
			"12/27/15 08:11:32 | Sleep                                         | SofZmanShema    |     9:12:54 AM |  30:00:000    |\n" +
			"12/27/15 08:41:32 | Sleep                                         | SofZmanShema    |     9:12:54 AM |  16:22:217    |\n" +
			"12/27/15 08:57:54 | Notify: SofZmanShema@9:12:54 AM (15:0:0)      | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 09:27:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 09:57:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  30:00:000    |\n" +
			"12/27/15 10:27:54 | Sleep                                         | SofZmanTefila   |    10:45:41 AM |  02:46:487    |\n" +
			"12/27/15 10:30:41 | Notify: SofZmanTefila@10:45:41 AM (15:0:0)    | Chatzot         |    12:10:15 PM |  30:00:000    |\n" +
			"12/27/15 11:00:41 | Sleep                                         | Chatzot         |    12:10:15 PM |  30:00:000    |\n" +
			"12/27/15 11:30:41 | Sleep                                         | Chatzot         |    12:10:15 PM |  24:34:352    |\n" +
			"12/27/15 11:55:15 | Notify: Chatzot@12:10:15 PM (15:0:0)          | MinchaGedola    |    12:31:24 PM |  21:08:588    |\n" +
			"12/27/15 12:16:24 | Notify: MinchaGedola@12:31:24 PM (15:0:0)     | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 12:46:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 13:16:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 13:46:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  30:00:000    |\n" +
			"12/27/15 14:16:24 | Sleep                                         | MinchaKetana    |     2:38:15 PM |  06:51:528    |\n" +
			"12/27/15 14:23:15 | Notify: MinchaKetana@2:38:15 PM (15:0:0)      | PlagHaMincha    |     3:31:07 PM |  30:00:000    |\n" +
			"12/27/15 14:53:15 | Sleep                                         | PlagHaMincha    |     3:31:07 PM |  22:51:470    |\n" +
			"12/27/15 15:16:07 | Notify: PlagHaMincha@3:31:07 PM (15:0:0)      | Shkia           |     4:23:58 PM |  30:00:000    |\n" +
			"12/27/15 15:46:07 | Sleep                                         | Shkia           |     4:23:58 PM |  22:51:473    |\n" +
			"12/27/15 16:08:58 | Notify: Shkia@4:23:58 PM (15:0:0)             | LatestMincha    |     4:33:29 PM |  09:30:861    |\n" +
			"12/27/15 16:18:29 | Notify: LatestMincha@4:33:29 PM (15:0:0)      | BeinHashmashot  |     4:58:20 PM |  24:50:699    |\n" +
			"12/27/15 16:43:20 | Notify: BeinHashmashot@4:58:20 PM (15:0:0)    | TzaitHacochavim |     5:16:32 PM |  18:11:852    |\n" +
			"12/27/15 17:01:32 | Notify: TzaitHacochavim@5:16:32 PM (15:0:0)   | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 17:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 18:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 18:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 19:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 19:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 20:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 20:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 21:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 21:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 22:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 22:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 23:01:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  30:00:000    |\n" +
			"12/27/15 23:31:32 | Sleep                                         | NightChatzot    |    12:10:22 AM |  23:50:272    |\n" +
			"12/27/15 23:55:22 | Notify: NightChatzot@12:10:22 AM (15:0:0)     | Alot            |     6:15:51 AM |  30:00:000    |\n",

			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.CandleLighting, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);
	}


	private void runDays(
		final Calendar          testTime,
		final HebrewDate        startDate,
		final HebrewDate        endDate,
		final String            expected,
	  final List<Zmanim.Type> expectedZmanim
	) {

		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" );
		output.append( "------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" );


		final MutableBoolean testComplete = new MutableBoolean();
		final MutableBoolean notified = new MutableBoolean();

		final ZmanFormatter zmanFormatter = new ZmanFormatter( true );
		ZmanUtils.PreferredZmanTypeLookup lookup = new ZmanUtils.PreferredZmanTypeLookup() {
			@Override
			public String getPreferredZman( Zmanim.Type type ) {
				switch ( type ) {
					case ShabbatEnd:
						return "Seattle Sephardic";

					case FastEnd:
						return "Seattle Sephardic";

					default:
						return null;
				}
			}
		};

		ZmanUtils.ZmanNotificationCallback zmanNotificationCallback = new ZmanUtils.ZmanNotificationCallback() {
			@Override
			public void notifyUser(
				final Zman nextZman
			) {
				notified.value = true;

				long delta = nextZman.getTime().getTimeInMillis() - testTime.getTimeInMillis();
				long minUntilZman = ( delta / ( 1000 * 60 ) );
				long remainingMs = ( delta - ( minUntilZman * ( 1000 * 60 ) ) );
				long secs = ( remainingMs / 1000 );
				remainingMs = ( remainingMs - secs * 1000 );

				output.append( String.format( " %-45s |", "Notify: " + nextZman.type + "@"
					+ zmanFormatter.formatZmanTime( nextZman ) + " (" + minUntilZman + ":" + secs + ":" + remainingMs + ")" ) );

				testComplete.value = isTestComplete( nextZman, endDate );
			}

			@Override
			public void scheduleNextCheck(
				final Calendar  now,
				final int       timeUntilNextCheck,
				final Zman      nextZman
			) {

				if ( !notified.value ) {
					output.append( String.format( " %-45s |", "Sleep" ) );
				}

				output.append( String.format( " %-15s |", nextZman.type ) );
				output.append( String.format( " %14s | ",  zmanFormatter.formatZmanTime( nextZman ) ) );

				int minUntilNextCheck = ( timeUntilNextCheck / ( 1000 * 60 ) );
				int remainingMs = ( timeUntilNextCheck - ( minUntilNextCheck * ( 1000 * 60 ) ) );
				int secs = ( remainingMs / 1000 );
				remainingMs = ( remainingMs - secs * 1000 );

				//noinspection AutoBoxing
				output.append( String.format( " %02d:%02d:%03d    |", minUntilNextCheck, secs, remainingMs ) );
				testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );
			}
		};


		ZmanUtils.NotifiedZmanim notifiedZmanim = new ZmanUtils.NotifiedZmanim( testTime );
		while ( !testComplete.value ) {

			if ( !notifiedZmanim.isForDate( testTime ) ) {
				notifiedZmanim = new ZmanUtils.NotifiedZmanim( testTime );
			}

			notified.value = false;
			output.append( String.format( "%1$tD %1$tH:%1$tM:%1$tS |", testTime ) );
			ZmanUtils.nextZmanAction( seattle, testTime, notifiedZmanim, thirtyMinutesInterval, fifteenMinutesNotification,
				lookup, zmanNotificationCallback );
			output.append( "\n" );
		}


		Assert.assertEquals( expected, output.toString() );

		// Make sure we notified for all the zmanim of the day...
		List<Zmanim> todaysZmanim = startDate.getZmanim( seattle );
		for ( Zmanim zmanim : todaysZmanim ) {
			if ( expectedZmanim.contains( zmanim.type ) ) {
				Assert.assertTrue( notifiedZmanim.contains( zmanim.type ) );
			}
			else {
				Assert.assertFalse( notifiedZmanim.contains( zmanim.type ) );
			}
		}
	}


	@SuppressWarnings( "MagicConstant" )
	private boolean isTestComplete(
		final Zman        nextZman,
		final HebrewDate  endDate
	) {

		if ( nextZman.type != Zmanim.Type.NightChatzot ) {
			return false;
		}

		Calendar time = nextZman.getTime();

		if ( time.get( Calendar.YEAR ) >= endDate.getGregorianYear() ) {
			if ( time.get( Calendar.MONTH ) >= endDate.getGregorianMonth() ) {
				if ( time.get( Calendar.DAY_OF_MONTH ) >= endDate.getGregorianDayOfMonth() ) {
					return true;
				}
			}
		}

		return false;
	}


	private Calendar configureTestTime(
		int     year,
	  int     month,
	  int     day,
	  int     hour,
	  int     minute
	) {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		Calendar testTime = Calendar.getInstance();
		testTime.set( year, month, day, hour, minute, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		return testTime;
	}


	private static final class MutableBoolean {
		boolean value = false;
	}

}
