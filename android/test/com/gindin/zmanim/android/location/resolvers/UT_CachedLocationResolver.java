package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.eventBus.EventBus;
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
	public void loadFromCache() throws IOException {

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );

		cachedLocationResolver.locationCache.clear();

		ioFactory.prepInput( ioFactory.outputFile );

		cachedLocationResolver.loadCache();
		Assert.assertEquals( 1, cachedLocationResolver.locationCache.size() );

		ZmanimLocation cachedLocation = cachedLocationResolver.locationCache.iterator().next();
		Assert.assertEquals( location, cachedLocation );
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


	@Test
	public void lruTest() {

		ZmanimLocation firstLocation = new ZmanimLocation.Builder()
			.atLatitude( 47.552827 )
			.atLongitude( -122.267065 )
			.locationName( "Ezra Bessaroth" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( firstLocation ) );

		for ( int i = 0; i < 25; i++ ) {
			ZmanimLocation location = new ZmanimLocation.Builder()
				.atLatitude( firstLocation.getLatitude() + i )
				.atLongitude( firstLocation.getLongitude() + i )
				.locationName( firstLocation.getLocationName() + "_" + i )
				.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
				.build();

			eventBus.fire( new LocationAcquiredEvent( location ) );
		}

		Assert.assertEquals( 25, cachedLocationResolver.locationCache.size() );

		ZmanimLocation location = new ZmanimLocation.Builder()
			.atLatitude( firstLocation.getLatitude() + 25 )
			.atLongitude( firstLocation.getLongitude() + 25 )
			.locationName( firstLocation.getLocationName() + "_25" )
			.inTimeZone( TimeZone.getTimeZone( "America/Los_Angeles" ) )
			.build();

		eventBus.fire( new LocationAcquiredEvent( location ) );

		Assert.assertEquals( 25, cachedLocationResolver.locationCache.size() );
		Assert.assertFalse( cachedLocationResolver.locationCache.contains( firstLocation ) );
	}




	private static class TestIOFactory
		implements CachedLocationResolver.IOFactory {

		private File                                            inputFile;
		FileInputStream                                         input;

		private File                                            outputFile;
		FileOutputStream                                        output;


		void prepInput( File prepFrom ) throws IOException {

			input = new FileInputStream( prepFrom );

			outputFile = File.createTempFile( "ut_cached_location_", "output" );
			outputFile.deleteOnExit();
			output = new FileOutputStream( outputFile );
		}


		@Override
		public InputStream openInput() throws IOException {

			inputFile = File.createTempFile( "ut_cached_location_", "input" );
			inputFile.deleteOnExit();
			input = new FileInputStream( inputFile );

			return input;
		}


		@Override
		public OutputStream openOutput() throws IOException {

			outputFile = File.createTempFile( "ut_cached_location_", "output" );
			outputFile.deleteOnExit();
			output = new FileOutputStream( outputFile );

			return output;
		}


		@Override
		public void runInBackground( Runnable runnable ) {
			runnable.run();
		}

	}
}
