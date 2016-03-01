package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Assert;

import java.util.Calendar;
import java.util.List;

/**
 *
 */
class WakeupSchedulerBase_MutliDay
	extends WakeupSchedulerBaseTest {

	protected void runDays(
		final Calendar testTime,
		final HebrewDate startDate,
		final HebrewDate              endDate,
		final String                  expected,
	  final List<Zmanim.Type> expectedZmanim,
	  final ZmanUtils.ZmanCallback  perTestCallback
	) {

		final StringBuilder output = new StringBuilder();
		//              123456789012345678901234567890123456789012345678901234567890
		output.append( "Time              | Action                                        | Next Zman       | Next Zman Time | Next (M:S:ms) |\n" );
		output.append( "------------------|-----------------------------------------------|-----------------|----------------|---------------|\n" );


		final MutableBoolean testComplete = new MutableBoolean();
		final MutableBoolean notified = new MutableBoolean();

		final ZmanFormatter zmanFormatter = new ZmanFormatter( true );

		ZmanUtils.ZmanCallback zmanCallback = new ZmanUtils.ZmanCallback() {
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
				zmanCallback );
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




} // End of WakeupSchedulerBase_MutliDay class
