package com.gindin.zmanlib;

import com.gindin.zmanlib.location.ZmanimLocation;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 */
class WakeupSchedulerBaseTest {

	static final ZmanimLocation                               seattle;

	static final int                                          thirtyMinutesInterval = ( 30 * 60 * 1000 );


	static {

		seattle = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();
	}


	Calendar configureTestTime(
		int year,
		int month,
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


	static class MutableBoolean {
		boolean value = false;
	}


} // End of WakeupSchedulerBaseTest class
