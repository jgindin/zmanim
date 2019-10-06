package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
@SuppressWarnings( "MagicNumber" )
public class UT_NominatimResolver {

  private NominatimResolver                                 resolver;


  @Before
  public void setup() {

    final EventBus eventBus = new EventBus();
    resolver = new NominatimResolver();
  }

  @Test
  public void reverseGeocoding() {

    testReverse( 57.15, -2.15, "Aberdeen, Scotland" );
    testReverse( -34.917, 138.6, "Adelaide, South Australia" );
    testReverse( 32.067, 34.767, "تل أبيب, מחוז תל אביב" );
    testReverse( 47.62, -122.33, "Seattle, Washington" );
    testReverse( 40.78, -73.966, "NYC, New York" );
    testReverse( 37.78333, -122.433, "SF, California" );
    testReverse( 33.48, -112.07, "Phoenix, Arizona" );
    testReverse( 45.4, -73.717, "Kahnawake, Québec" );
  }


  @Test
  public void forwardGeocoding() {

    testForward( "98118", "Seattle, Washington", 47.5414933691592, -122.276021044533 );
    testForward( "Brooklyn, NY", "NYC, New York", 40.6501038, -73.9495823 );
    testForward( "Broklyn, NY", null, 0, 0 );
    testForward( "Jerusalem, IL", "Jerusalem, מחוז ירושלים", 31.78911765, 35.2229730135452 );
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

    ZmanimLocation resolved = resolver.resolveFromCoordinates( unresolved );
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

    ZmanimLocation resolved = resolver.resolveFromName( unresolved );
    if ( expectedLocationName == null ) {
      Assert.assertNull( resolved );
    }
    else {
      Assert.assertNotNull( resolved );
      Assert.assertEquals( expectedLocationName, resolved.getLocationName() );
      Assert.assertEquals( expectedLatitude, resolved.getLatitude(), 0 );
      Assert.assertEquals( expectedLongitude, resolved.getLongitude(), 0 );
    }
  }

}
