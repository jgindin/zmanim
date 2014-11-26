package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.location.ZmanimLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TimeZone;

/**
 *
 */
public class UT_CachedLocationResolver {

	private EventBus                                          eventBus;

	private SharedPreferences                                 testPrefs;

	private CachedLocationResolver                            cachedLocationResolver;


	@Before
	public void setup() {

		eventBus = new EventBus();
		testPrefs = new TestPrefs();
		cachedLocationResolver = new CachedLocationResolver( eventBus, testPrefs );
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



	private static class TestPrefs
		implements SharedPreferences {

		@Override
		public boolean contains( String key ) {
			return false;
		}


		@Override
		public Map<String, ?> getAll() {
			return null;
		}


		@Override
		public String getString(
			String key,
			String defValue
		) {
			return null;
		}


		@Override
		public int getInt(
			String  key,
			int     defValue
		) {
			return 0;
		}


		@Override
		public long getLong(
			String  key,
			long    defValue
		) {
			return 0;
		}


		@Override
		public float getFloat(
			String  key,
			float   defValue
		) {
			return 0;
		}


		@Override
		public boolean getBoolean(
			String  key,
			boolean defValue
		) {
			return false;
		}


		@Override
		public Editor edit() {
			return null;
		}


		@Override
		public void registerOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener onSharedPreferenceChangeListener ) {

		}


		@Override
		public void unregisterOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener onSharedPreferenceChangeListener ) {

		}
	}
}
