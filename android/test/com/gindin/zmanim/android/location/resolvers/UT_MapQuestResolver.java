package com.gindin.zmanim.android.location.resolvers;

import android.content.SharedPreferences;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class UT_MapQuestResolver {

	private EventBus                                          eventBus;

	private MapQuestResolver                                  resolver;

	private SharedPreferences                                 prefs;

	@Before
	public void setup() {

		eventBus = new EventBus();
		prefs = new TestPrefs();
		resolver = new MapQuestResolver( eventBus, prefs );
	}

	@Test
	public void testResolution() {

		test( 57.15, -2.15, "Aberdeen, Scotland" );
		test( -34.917, 138.6, "North Adelaide, South Australia" );
		test( 32.067, 34.767, "תל אביב-יפו, מחוז תל אביב" );
		test( 47.62, -122.33, "Seattle, WA" );
		test( 40.78, -73.966, "New York, NY" );
		test( 37.78333, -122.433, "San Francisco, CA" );
		test( 33.48, -112.07, "Phoenix, AZ" );
		test( 45.4, -73.717, "Kahnawake, QC" );
	}


	private void test(
		double latitude,
		double longitude,
		String expectedLocationName
	) {

		ZmanimLocation unresolved = new ZmanimLocation.Builder()
			.atLatitude( latitude )
			.atLongitude( longitude )
			.build();

		ZmanimLocation resolved = resolver.resolveFromCoordinatesInBackground( unresolved );
		Assert.assertNotNull( resolved );
		Assert.assertEquals( expectedLocationName, resolved.getLocationName() );
	}


}
