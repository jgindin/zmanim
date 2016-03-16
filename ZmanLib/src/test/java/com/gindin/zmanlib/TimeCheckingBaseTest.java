package com.gindin.zmanlib;

import com.gindin.zmanlib.calendar.HebrewDate;
import com.gindin.zmanlib.calendar.formatters.SecularDateFormatter;
import com.gindin.zmanlib.location.ZmanimLocation;
import com.gindin.zmanlib.zman.Zman;
import com.gindin.zmanlib.zman.ZmanFormatter;
import com.gindin.zmanlib.zman.Zmanim;
import org.junit.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 */
abstract class TimeCheckingBaseTest {


	protected abstract Zmanim.Type getTypeForTest();


	private Zmanim getZmanimForDate(
		final HebrewDate      date,
		final ZmanimLocation  location
	) {

		return date.getZmanim( location, getTypeForTest() );
	}


	protected abstract boolean shouldRoundDown();


	protected final void runTest(
		HebrewDate      startDate,
	  HebrewDate      endDate,
	  ZmanimLocation  location,
	  String          expected
	)
		throws ParseException {

		Zmanim zmanim = getZmanimForDate( startDate, location );
		List<Zman> zmans = zmanim.asList();
		int maxLength = computeColumnWidth( zmans );


		DateFormat shortFormat = DateFormat.getTimeInstance( DateFormat.SHORT );
		DateFormat mediumFormat = DateFormat.getTimeInstance( DateFormat.MEDIUM );

		ZmanFormatter formatter = new ZmanFormatter( false );
		ZmanFormatter formatterWithSeconds = new ZmanFormatter( true );
		SecularDateFormatter dateFormatter = SecularDateFormatter.getInstance( SecularDateFormatter.Style.SHORT );

		StringBuilder output = new StringBuilder();
		outputHeader( output, startDate, location );

		while ( endDate.compareTo( startDate ) >= 0 ) {

			zmanim = getZmanimForDate( startDate, location );
			zmans = zmanim.asList();

			StringBuilder line1 = new StringBuilder()
				.append( dateFormatter.format( startDate ) )
				.append( "     | " );
			StringBuilder line2 = new StringBuilder()
				.append( "               | " );

			for ( Zman zman : zmans ) {

				String timeWithSeconds = formatterWithSeconds.formatZmanTime( zman );
				line1.append( timeWithSeconds );
				pad( line1, maxLength - timeWithSeconds.length() - 1 );
				line1.append( "| " );

				Date zmanTimeWithSeconds;
				if ( "N/A".equals( timeWithSeconds ) ) {
					zmanTimeWithSeconds = null;
				}
				else {
					zmanTimeWithSeconds = mediumFormat.parse( timeWithSeconds );
				}

				String time = formatter.formatZmanTime( zman );
				line2.append( time );
				pad( line2, maxLength - time.length() - 1 );
				line2.append( "| " );


				Date zmanTime;
				if ( "N/A".equals( time ) ) {
					zmanTime = null;
				}
				else {
					zmanTime = shortFormat.parse( time );
				}

				// Make sure that we rounded up. Assuming we did, then the rounded time will possibly be equal to
				//  the time with seconds (e.g., if the time with seconds is exactly zero seconds) or after.
				if ( ( zmanTime == null ) && ( zmanTimeWithSeconds == null ) ) {
					// This is OK. Both times are "N/A".
				}
				else if ( ( zmanTime == null ) || ( zmanTimeWithSeconds == null ) ) {
					Assert.fail( "zmanTime & zmanTimeWithSeconds must both be null if either is null" );
				}
				else if ( shouldRoundDown() ) {
					Assert.assertTrue( time + " is supposed to be <= " + timeWithSeconds,
						zmanTime.before( zmanTimeWithSeconds ) || zmanTime.equals( zmanTimeWithSeconds ) );
				}
				else {
					Assert.assertTrue( time + " is supposed to be >= " + timeWithSeconds,
						zmanTime.after( zmanTimeWithSeconds ) || zmanTime.equals( zmanTimeWithSeconds ) );
				}
			}

			output
				.append( line1 )
				.append( "\n" )
				.append( line2 )
				.append( "\n" );

			//noinspection AssignmentToMethodParameter
			startDate = startDate.tomorrow();
		}

		Assert.assertEquals( expected, output.toString() );
	}


	private void outputHeader(
		StringBuilder   buffer,
		HebrewDate      startDate,
		ZmanimLocation  location
	) {

		Zmanim zmanim = getZmanimForDate( startDate, location );
		List<Zman> zmans = zmanim.asList();
		int maxLength = computeColumnWidth( zmans );

		buffer.append( "Date           |" );
		for ( Zman zman : zmans ) {
			String name = zman.name;
			int padding = ( maxLength - ( name.length() ) ) / 2;
			pad( buffer, padding );
			buffer.append( name );

			pad( buffer, maxLength - padding - name.length() );

			buffer.append( "|" );
		}

		buffer.append( "\n" );

		buffer.append( "---------------|" );
		for ( Zman zman : zmans ) {
			for ( int i = 0; i < maxLength; i++ ) {
				buffer.append( "-" );
			}
			buffer.append( "|" );
		}

		buffer.append( "\n" );
	}


	private int computeColumnWidth( final List<Zman> zmans ) {

		int maxLength = -1;
		for ( Zman zman : zmans ) {
			maxLength = Math.max( maxLength, zman.name.length() );
		}

		// Make sure there's some padding.
		maxLength = Math.max( 14, maxLength + 2 );
		return maxLength;
	}


	private void pad(
		final StringBuilder buffer,
		final int           padding
	) {
		for ( int i = 0; i < padding; i++ ) {
			buffer.append( " " );
		}
	}


} // End of TimeCheckingBaseTest class
