package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import android.content.SharedPreferences;

/**
 *
 */
public class UT_MapQuestResolver {

  private MapQuestResolver                                  resolver;


  @Before
  public void setup() {

    EventBus eventBus = new EventBus();
    SharedPreferences prefs = new TestPrefs();
    resolver = new MapQuestResolver( eventBus, prefs );
  }

  @Test
  public void reverseGeocoding() {

    testReverse( 57.15, -2.15, "Aberdeen, Scotland" );
    testReverse( -34.917, 138.6, "North Adelaide, South Australia" );
    testReverse( 32.067, 34.767, "תל אביב-יפו, Israel" );
    testReverse( 47.62, -122.33, "Seattle, WA" );
    testReverse( 40.78, -73.966, "New York, NY" );
    testReverse( 37.78333, -122.433, "San Francisco, CA" );
    testReverse( 33.48, -112.07, "Phoenix, AZ" );
    testReverse( 45.4, -73.717, "Kahnawake, QC" );
    testReverse( 55.0, -1.6, "Newcastle upon Tyne, England" );
  }


  @Test
  public void forwardGeocoding() {

    testForward( "98118", "Seattle, WA", 47.54055, -122.275593 );
    testForward( "Brooklyn, NY", "Brooklyn, NY", 40.692529, -73.990996 );
    testForward( "Broklyn, NY", "Brooklyn, NY", 40.692529, -73.990996 );
    testForward( "Jerusalem, IL", "Jerusalem, Israel", 31.778408, 35.206626 );
  }


  private void testReverse(
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


  private void testForward(
    String name,
    String expectedLocationName,
    double expectedLatitude,
    double expectedLongitude
  ) {

    ZmanimLocation unresolved = new ZmanimLocation.Builder()
      .locationName( name )
      .build();

    ZmanimLocation resolved = resolver.resolveFromNameInBackground( unresolved );
    Assert.assertNotNull(resolved );
    Assert.assertEquals( expectedLocationName, resolved.getLocationName() );
    Assert.assertEquals( expectedLatitude, resolved.getLatitude(), 0 );
    Assert.assertEquals( expectedLongitude, resolved.getLongitude(), 0 );
  }
}
