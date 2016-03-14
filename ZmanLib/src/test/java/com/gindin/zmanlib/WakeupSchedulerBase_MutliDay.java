package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewCalendar;
import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.util.*;

/**
 *
 */
abstract class WakeupSchedulerBase_MutliDay
	extends WakeupSchedulerBaseTest {

	static final int                                          ZERO_MINUTES = 0;
	static final int                                          FIVE_MINUTES = 5;
	static final int                                          FIFTEEN_MINUTES = 15;
	static final int                                          TWENTY_MINUTES = 20;
	static final int                                          THIRTY_MINUTES = 30;
	static final int                                          ONE_HOUR = 60;


	@SuppressWarnings( "MagicNumber" )
	void december_7_2014(
		final ZmanCallback  perTestCallback,
		final String        expected
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

		Calendar testTime = configureTestTime( 2014, Calendar.DECEMBER, 7, 6, 34 );
		HebrewDate hebrewDate = HebrewCalendar.getForDate( testTime );
		Calendar endTest = configureTestTime( 2014, Calendar.DECEMBER, 8, 0, 0 );

		Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
		expectedZmanim.put( hebrewDate,
			Arrays.asList( Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema, Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot,
				Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana, Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia,
				Zmanim.Type.LatestMincha, Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		runDays( testTime, hebrewDate, endTest, perTestCallback, expected, expectedZmanim );
	}


	@SuppressWarnings( "MagicNumber" )
	void december_21_2015_1_week(
		final ZmanCallback  perTestCallback,
		final String        expected
	) {

		// In this case, the user never turns their phone off...
		Calendar testTime = configureTestTime( 2015, Calendar.DECEMBER, 21, 0, 1 );
		HebrewDate startTest = HebrewCalendar.getForDate( testTime );
		Calendar endTest = configureTestTime( 2015, Calendar.DECEMBER, 28, 0, 0 );

		Map<HebrewDate,List<Zmanim.Type>> expectedZmanim = new HashMap<>();
		expectedZmanim.put( startTest,
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 1 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha, Zmanim.Type.FastEnd,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 2 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 3 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 4 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.CandleLighting, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 5 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.ShabbatEnd, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);

		expectedZmanim.put( startTest.forward( 6 ),
			Arrays.asList( Zmanim.Type.Alot, Zmanim.Type.Talit, Zmanim.Type.Hanetz, Zmanim.Type.SofZmanShema,
				Zmanim.Type.SofZmanTefila, Zmanim.Type.Chatzot, Zmanim.Type.MinchaGedola, Zmanim.Type.MinchaKetana,
				Zmanim.Type.PlagHaMincha, Zmanim.Type.Shkia, Zmanim.Type.LatestMincha,
				Zmanim.Type.BeinHashmashot, Zmanim.Type.TzaitHacochavim, Zmanim.Type.NightChatzot )
		);


		runDays( testTime, startTest, endTest, perTestCallback, expected, expectedZmanim );
	}


	private void runDays(
		final Calendar                          testTime,
		final HebrewDate                        startDate,
		final Calendar                          endDate,
		final ZmanCallback                      perTestCallback,
		final String                            expected,
	  final Map<HebrewDate,List<Zmanim.Type>> expectedZmanim
	) {

		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" );
		output.append( "------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" );

		final MutableBoolean testComplete = new MutableBoolean();
		final MutableBoolean notified = new MutableBoolean();

		final ZmanFormatter zmanFormatter = new ZmanFormatter( true );

		ZmanCallback zmanCallback = new ZmanCallback() {
			@Override
			public String getPreferredZman( Zmanim.Type type ) {
				return perTestCallback.getPreferredZman( type );
			}


			@Override
			public int getZmanAlertTime( final Zmanim.Type type ) {
				return perTestCallback.getZmanAlertTime( type );
			}


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

				output.append( String.format( " %-45s |", "Notify: " + nextZman.type + "@" +
					zmanFormatter.formatZmanTime( nextZman ) + " (" + minUntilZman + ":" + secs + ":" + remainingMs + ")" ) );

				testComplete.value = isTestComplete( testTime, endDate );
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

				int minUntilNextCheck = ( timeUntilNextCheck / ( ZmanUtils.MIN_TO_MS_CONVERSION ) );
				int remainingMs = ( timeUntilNextCheck - ( minUntilNextCheck * ( ZmanUtils.MIN_TO_MS_CONVERSION ) ) );
				int secs = ( remainingMs / 1000 );
				remainingMs = ( remainingMs - secs * 1000 );

				//noinspection AutoBoxing
				output.append( String.format( " %02d:%02d:%03d    |", minUntilNextCheck, secs, remainingMs ) );
				testTime.add( Calendar.MILLISECOND, timeUntilNextCheck );

				testComplete.value = isTestComplete( testTime, endDate );
			}
		};


		Map<HebrewDate, NotifiedZmanim> notifiedZmanim = new HashMap<>();
		NotifiedZmanim currentDayNotifiedZmanim = new NotifiedZmanim( testTime );
		HebrewDate currentDate = startDate;

		notifiedZmanim.put( currentDate, currentDayNotifiedZmanim );

		while ( !testComplete.value ) {

			if ( !currentDayNotifiedZmanim.isForDate( testTime ) ) {
				currentDayNotifiedZmanim = new NotifiedZmanim( testTime );
				currentDate = currentDate.tomorrow();
				notifiedZmanim.put( currentDate, currentDayNotifiedZmanim );
			}

			notified.value = false;
			output.append( String.format( "%1$tD %1$tH:%1$tM:%1$tS |", testTime ) );

			ZmanUtils.nextZmanAction( seattle, testTime, currentDayNotifiedZmanim, thirtyMinutesInterval, zmanCallback );

			output.append( "\n" );
		}


		Assert.assertEquals( expected, output.toString() );

		final SecularDateFormatter dateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.SHORT );

		// Make sure we notified for all the zmanim of the day...
		for ( Map.Entry<HebrewDate, NotifiedZmanim> entry : notifiedZmanim.entrySet() ) {
			currentDate = entry.getKey();
			currentDayNotifiedZmanim = entry.getValue();

			List<Zmanim> todaysZmanim = currentDate.getZmanim( seattle );
			final List<Zmanim.Type> expectedTypes = new ArrayList<>( expectedZmanim.get( currentDate ) );

			// First, we'll check that all of today's zmanim are expected...
			for ( Zmanim zmanim : todaysZmanim ) {
				final boolean removed = expectedTypes.remove( zmanim.type );
				if ( removed && ( perTestCallback.getZmanAlertTime( zmanim.type ) > 0 ) ) {
					Assert.assertTrue( dateFormatter.format( currentDate ) + " should have had " + zmanim.type, currentDayNotifiedZmanim.contains( zmanim.type ) );
				}
				else {
					Assert.assertFalse( dateFormatter.format( currentDate ) + " should NOT have had " + zmanim.type, currentDayNotifiedZmanim.contains( zmanim.type ) );
				}
			}

			// Make sure we found all the expected zmanim.
			Assert.assertTrue( "Didn't find: " + expectedTypes, expectedTypes.isEmpty() );
		}

	}


	private boolean isTestComplete(
		final Calendar  currentTestTime,
		final Calendar  endDate
	) {

		return currentTestTime.after( endDate );
	}


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


} // End of WakeupSchedulerBase_MutliDay class
