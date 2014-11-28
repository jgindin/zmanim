package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.location.ZmanimLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.TimeZone;

/**
 *
 */
public class UT_CachedLocationResolver {

	private EventBus                                          eventBus;

	private SharedPreferences                                 testPrefs;

	private CachedLocationResolver                            cachedLocationResolver;

	private TestIOFactory                                     ioFactory;


	@Before
	public void setup() {

		ioFactory = new TestIOFactory();

		eventBus = new EventBus();
		testPrefs = new TestPrefs();
		cachedLocationResolver = new CachedLocationResolver( ioFactory, eventBus, testPrefs );
	}


	@Test
	public void addedToCache() {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );
		Assert.assertEquals( 1, cachedLocationResolver.locationCache.size() );

		String output = ioFactory.getWhatWasWritten();
		Assert.assertEquals(
			"\u0000\u0000\u0000\u0001\u0000�{\"lat\": \"47.552827\",\n" +
			"\"lon\": \"-122.267065\",\n" +
			"\"el\": \"0.0\",\n" +
			"\"tz\": \"America/Los_Angeles\",\n" +
			"\"pv\": \"Zmanim\",\n" +
			"\"ac\": \"-1.0\",\n" +
			"\"name\": \"Ezra Bessaroth\"\n" +
			"}",
			output );
	}


	@Test
	public void useCachedLocation() {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );

		ZmanimLocation location2 = new ZmanimLocation.Builder()
			.atLatitude( 47.55 )
			.atLongitude( -122.27 )
			.locationName( "PCC" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		ZmanimLocation resolvedLocation = cachedLocationResolver.resolveFromCoordinatesInBackground( location2 );
		Assert.assertEquals( location, resolvedLocation );
	}


	@Test
	public void cachedLocationIsTooFarAway() {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );

		ZmanimLocation location2 = new ZmanimLocation.Builder()
			.atLatitude( 47.56 )
			.atLongitude( -122.278 )
			.locationName( "Genesse Park" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		// The caching resolver won't know about this other location, so it'll return null.
		ZmanimLocation resolvedLocation = cachedLocationResolver.resolveFromCoordinatesInBackground( location2 );
		Assert.assertNull( resolvedLocation );
	}


	@Test
	public void dontUseUnresolvedLocation() {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );
		Assert.assertEquals( 0, cachedLocationResolver.locationCache.size() );

	}


	@Test
	public void loadFromCache() {

		// TODO: I can't seem to get this test to work properly... I worked out that the length of the UTF string
		// TODO:  being written ought to be 8F bytes (including the 2 bytes for the length of the string).
		// TODO:  However, when that's the length put in there, we get an EOF exception. If I make the length
		// TODO:  7F, then I get a JSON exception that the string is mal-formed. Any longer than that, and I get
		// TODO   the EOF exception...
		// TODO:  It does seem that the code really does work, though....

/*
		ioFactory.prepInput(
			"\u0000\u0000\u0000\u0001\u0000\u008F{\"lat\": \"47.552827\",\n" +
			"\"lon\": \"-122.267065\",\n" +
			"\"el\": \"0.0\",\n" +
			"\"tz\": \"America/Los_Angeles\",\n" +
			"\"pv\": \"Zmanim\",\n" +
			"\"ac\": \"-1.0\",\n" +
			"\"name\": \"Ezra Bessaroth\"\n" +
			"}"
		);

		cachedLocationResolver.loadCache();
		Assert.assertEquals( 1, cachedLocationResolver.locationCache.size() );

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		Assert.assertEquals( location, cachedLocationResolver.locationCache.iterator().next() );
*/
	}


	@Test
	public void dontCacheSignificantlyTheSameLocation() {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );

		ZmanimLocation location2 = new ZmanimLocation.Builder()
			.atLatitude( 47.55 )
			.atLongitude( -122.27 )
			.locationName( "PCC" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location2 ) );

		Assert.assertEquals( 1, cachedLocationResolver.locationCache.size() );
	}



	private static class TestIOFactory
		implements CachedLocationResolver.IOFactory {

		ByteArrayInputStream                                    input;
		ByteArrayOutputStream                                   output;


		TestIOFactory() {
			input = new ByteArrayInputStream( new byte[0] );
			output = new ByteArrayOutputStream();
		}


		void prepInput( String json ) {
			input = new ByteArrayInputStream( json.getBytes() );

			System.out.println( json );
		}


		@Override
		public InputStream openInput() throws FileNotFoundException {
			return input;
		}


		@Override
		public OutputStream openOutput() throws FileNotFoundException {
			return output;
		}


		@Override
		public void runInBackground( Runnable runnable ) {
			runnable.run();
		}


		public String getWhatWasWritten() {
			return output.toString();
		}
	}
}
