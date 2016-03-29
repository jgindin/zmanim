/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.CalendarMonth;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Tests for the ZmanNotificationScheduler functionality.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ZmanUtils {

	static final int                                          thirtyMinutesInterval = ( 30 * 60 * 1000 );

	static final ZmanimLocation                               seattle;

	static final ZmanimLocation                               yeshivaUniversity;

	static final int                                          ZERO_MINUTES = 0;

	static final int                                          FIVE_MINUTES = 5;

	static final int                                          FIFTEEN_MINUTES = 15;

	static final int                                          TWENTY_MINUTES = 20;

	static final int                                          THIRTY_MINUTES = 30;

	static final int                                          ONE_HOUR = 60;


	static {

		seattle = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		yeshivaUniversity = new ZmanimLocation.Builder()
			.atLatitude( 40.851546 )
			.atLongitude( -73.928937 )
			.locationName( "Yeshiva University" )
			.inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
			.build();
	}



	@Test
	public void testFindNextZmanFromEndOfDay() {

		// Make sure that when we're at the end of a day we'll find the next zman, even if its on the next day...
		Calendar findAfter = Calendar.getInstance();
		findAfter.setTimeZone( seattle.getTimeZone() );
		findAfter.set( 2015, Calendar.DECEMBER, 21, 23, 54 );

/*
		final ZmanNotification zmanNotification = ZmanNotificationScheduler.determineNextNotification( seattle, findAfter,
			new NotifiedZmanim( findAfter ), thirtyMinutesInterval, new AlwaysFifteenMinuteAlertsZmanCallback() );
		Assert.assertNotNull( zmanNotification );
		Assert.assertNotNull( zmanNotification.zmanToNotify );
		Assert.assertEquals( Zmanim.Type.NightChatzot, zmanNotification.zmanToNotify.type );
*/
	}



	/**
	 * A callback which specifies that we want to use the Seattle Sephardic values for the end of Shabbat and Fasts.
	 */
	abstract static class ZmanCallbackForTests
		extends ZmanCallback.Adapter {

		@Nullable
		@Override
		public String getPreferredZman( final Zmanim.Type type ) {
			switch ( type ) {
				case ShabbatEnd:
					return "Seattle Sephardic";

				case FastEnd:
					return "Seattle Sephardic";

				default:
					return null;
			}
		}
	}


	/**
	 * A callback which specifies that we alert 15 minutes before EVERY zman.
	 */
	static class AlwaysFifteenMinuteAlertsZmanCallback
		extends ZmanCallbackForTests {

		@Override
		public int getZmanAlertTime( final Zmanim.Type type ) {
			return FIFTEEN_MINUTES;
		}
	}



	/**
	 * Configure a calendar that allows us to ensure the seconds & milliseconds are zeroed out so that we know
	 *  every test will be run the same. (If these fields aren't set explicitly, they'll be initialized to the
	 *  values at the time the calendar is instantiated.)
	 */
	static Calendar configureTestTime(
		int year,
		@CalendarMonth int month,
		int day,
		int hour,
		int minute
	) {

		// In order to get exactly the same answer every time, we MUST set the seconds and even milliseconds here.
		//  Otherwise they get set randomly...
		Calendar testTime = Calendar.getInstance();
		testTime.set( year, month, day, hour, minute, 0 );
		testTime.set( Calendar.MILLISECOND, 0 );

		return testTime;
	}


} // End of UT_ZmanUtils class
