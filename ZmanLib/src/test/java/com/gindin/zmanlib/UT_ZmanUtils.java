/*
 * Copyright (c) 2015. Jay R. Gindin
 */

package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Tests for the ZmanUtils functionality.
 */
@SuppressWarnings( "MagicNumber" )
public class UT_ZmanUtils {

	private static final ZmanimLocation                       seattle;

	private static final ZmanimLocation                       yeshivaUniviersity;


	static {

		seattle = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		yeshivaUniviersity = new ZmanimLocation.Builder()
			.atLatitude( 40.851546 )
			.atLongitude( -73.928937 )
			.locationName( "Yeshiva University" )
			.inTimeZone( TimeZone.getTimeZone( "America/New_York" ) )
			.build();
	}


	private ZmanUtils.ZmanCallback                            zmanCallback;


	@Before
	public void setup() {

		zmanCallback = new ZmanUtils.ZmanCallback() {
			@Nullable
			@Override
			public String getPreferredZman( final Zmanim.Type type ) {
				return null;
			}


			@Override
			public void notifyUser( final Zman nextZman ) {
			}


			@Override
			public void scheduleNextCheck(
				final Calendar  now,
				final int       timeUntilNextCheck,
				final Zman      nextZman
			) {

			}
		};
	}


	@Test
	public void testFindingNextZmanAfterDate() {

		HebrewDate hebrewDate = HebrewCalendar.getForDate( 2014, Calendar.OCTOBER, 15 );
		List<Zmanim> zmanimForDate = hebrewDate.getZmanim( seattle );

		for ( Zmanim zmanim : zmanimForDate ) {
			Zman zman = zmanim.getDefault();
			Calendar zmanTime = zman.getTime();

			Zman nextZman = ZmanUtils.findNextZman( seattle, zmanTime, new ZmanUtils.NotifiedZmanim( zmanTime ),
				zmanCallback );
			Assert.assertNotNull( "Didn't find nextZman after " + zman.type, nextZman );

			Calendar nextZmanTime = nextZman.getTime();
			Assert.assertTrue( nextZmanTime.after( zmanTime ) );
		}
	}


	@Test
	public void testFindNextZmanFromEndOfDay() {

		// Make sure that when we're at the end of a day we'll find the next zman, even if its on the next day...
		Calendar findAfter = Calendar.getInstance();
		findAfter.setTimeZone( seattle.getTimeZone() );
		findAfter.set( 2014, Calendar.DECEMBER, 21, 23, 0 );

		Zman nextZman = ZmanUtils.findNextZman( seattle,  findAfter, new ZmanUtils.NotifiedZmanim( findAfter ),
			zmanCallback );
		Assert.assertNotNull( nextZman );
	}

} // End of UT_ZmanUtils class
