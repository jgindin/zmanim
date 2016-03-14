/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

/**
 * Various tests to ensure that we're calculating the correct time to schedule the next wakeup when we'll check
 *  to see if we ought to alert the user.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ServiceWakeupScheduler
	extends WakeupSchedulerBaseTest {

	private static final FifteenMinuteBeforeZmanCallback      zmanCallback = new FifteenMinuteBeforeZmanCallback();


	private static Zman findDefaultZman(
		Zmanim.Type           type,
		ZmanimLocation        location,
	  int                   year,
		@CalendarMonth	int   month,
	  int                   day
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

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );

		// We set the clock to 4:20pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is 38 minutes. Since we're testing with a 30 minute check interval, the next check ought
		//  to be in 30 minutes...
		Assert.assertEquals( thirtyMinutesInterval, timeUntilNextCheck );
	}


	@Test
	public void testNextZmanIsBeforeWakeupInterval() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 40 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );

		// We set the clock to 4:40pm; shkia is at 5:13 so we would expect to get notified at 4:58 (fifteen minutes).
		//  That delta is 18 minutes. Since we're testing with a 30 minute check interval, the next check ought
		//  to be in 18 about minutes...
		final int expectedTimeUntilNextCheck = 1114064; // 18*60*1000==1080000 ... the actual result is just over 18 minutes...

		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );
	}


	@Test
	public void testNextZmanIsVerySoon() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 50 );

		final int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );

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

		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );
		int expectedTimeUntilNextCheck = thirtyMinutesInterval;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

		// It should now be 4:44pm, leaving 14 minutes until the next wake-up, which is when we would expect the alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );
		expectedTimeUntilNextCheck = 874064; // About 14 minutes...
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

		// It should now be 4:58pm, right when we want to alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval,zmanCallback );
		Assert.assertEquals( 0, timeUntilNextCheck );
	}


	@Test
	public void testAsTimeGoesOnInexact() {

		Calendar testTime = configureTestTime( 2015, Calendar.FEBRUARY, 3, 16, 14 );

		// We're going to start at 4:14pm, which should result in wakeups at:
		//  4:44pm
		//  4:58pm

		int timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );
		int expectedTimeUntilNextCheck = thirtyMinutesInterval;
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time, but assume that we don't awake exactly when we wanted to...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 2000 );

		// It should now be 4:44pm, leaving 14 minutes until the next wake-up, which is when we would expect the alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );
		expectedTimeUntilNextCheck = 872064; // About 14 minutes...
		Assert.assertEquals( expectedTimeUntilNextCheck, timeUntilNextCheck );

		// Go ahead and fast forward time...
		testTime.add( Calendar.MILLISECOND, timeUntilNextCheck + 3333 );

		// It should now be 4:58pm, right when we want to alert.
		timeUntilNextCheck = ZmanUtils.determineNextWakeup( shkia, testTime, thirtyMinutesInterval, zmanCallback );
		Assert.assertTrue( timeUntilNextCheck < 0 );
	}


	private static class FifteenMinuteBeforeZmanCallback
		extends ZmanCallback.Adapter {

		static final int                                        fifteenMinutesNotification = ( 15 * 60 * 1000 );

		@Override
		public int getZmanAlertTime( Zmanim.Type type ) {
			return fifteenMinutesNotification;
		}
	}
}
