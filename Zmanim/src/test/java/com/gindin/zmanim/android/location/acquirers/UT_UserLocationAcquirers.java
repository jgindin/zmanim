/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.TimeZone;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage.PREFER;

/**
 * What the acquirers for a place the user typed and for coordinates the user entered hand to the rest of the app.
 *  Tests named {@code ...CurrentBehavior} pin behavior that looks wrong; it is kept as it is until a later change
 *  fixes it on purpose.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_UserLocationAcquirers {

  private Context                                           context;

  private SharedPreferences                                 prefs;


  @Before
  public void setup() {

    context = ApplicationProvider.getApplicationContext();
    prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    prefs.edit().clear().commit();
    ZmanimServiceRequests.drain();
  }


  private void chooseTimeZone( String area, String locale ) {
    prefs.edit()
      .putBoolean( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name(), true )
      .putString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), area )
      .putString( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name(), locale )
      .commit();
  }


  private void enterCoordinates( String latitude, String longitude, String elevation ) {
    prefs.edit()
      .putString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), latitude )
      .putString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), longitude )
      .putString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE_LONGITUDE_ELEVATION.name(), elevation )
      .commit();
  }


  private static ZmanimServiceRequests.Request onlyRequest() {

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    return requests.get( 0 );
  }


  @Test
  public void aTypedPlaceIsSentToBeLookedUpByName() {

    prefs.edit().putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Saratoga Springs, NY" ).commit();
    chooseTimeZone( "America", "Chicago" );

    new StaticLocationAcquirer( prefs, context ).acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( "Saratoga Springs, NY", request.location.getLocationName() );
    Assert.assertEquals( "Saratoga Springs, NY", request.location.originalUserName );
    Assert.assertFalse( request.location.hasCoordinates() );
    Assert.assertEquals( TimeZone.getTimeZone( "America/Chicago" ), request.location.getTimeZone() );
  }


  /** The preferences screen stores an elevation for the typed place, but it is never read. */
  @Test
  public void theElevationEnteredForATypedPlaceIsNotUsedCurrentBehavior() {

    prefs.edit()
      .putString( LocationProviders.StaticProviderOptions.PLACE.name(), "Saratoga Springs, NY" )
      .putString( LocationProviders.StaticProviderOptions.PLACE_ELEVATION.name(), "94" )
      .commit();

    new StaticLocationAcquirer( prefs, context ).acquire( PREFER );

    Assert.assertEquals( 0.0, onlyRequest().location.getElevation(), 0.0 );
  }


  @Test
  public void enteredCoordinatesAreSentAsANewLocation() {

    // The preferences screen stores degrees, minutes and seconds.
    enterCoordinates( "N 47 37 12", "W 122 19 48", "56" );
    chooseTimeZone( "America", "Los_Angeles" );

    new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    // Not resolved yet, as it has no name: the service sends it on to be named.
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 47.62, request.location.getLatitude(), 1e-9 );
    Assert.assertEquals( -122.33, request.location.getLongitude(), 1e-9 );
    Assert.assertEquals( 56.0, request.location.getElevation(), 0.0 );
    Assert.assertEquals( "Zmanim Latitude & Longitude Provider", request.location.providerName );
    Assert.assertEquals( TimeZone.getTimeZone( "America/Los_Angeles" ), request.location.getTimeZone() );
  }


  @Test
  public void noElevationEnteredIsSeaLevel() {

    enterCoordinates( "N 47 37 12", "W 122 19 48", "" );

    new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER );

    Assert.assertEquals( 0.0, onlyRequest().location.getElevation(), 0.0 );
  }


  /** The message for a bad latitude names the longitude, and the other way around. */
  @Test
  public void aBadLatitudeIsReportedAsABadLongitudeCurrentBehavior() {

    enterCoordinates( "north-ish", "W 122 19 48", "0" );

    new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "LOCATION_NOT_ACQUIRED", request.action );
    Assert.assertEquals( LocationError.Code.BAD_INPUT, request.error.errorCode );
    Assert.assertEquals( "Invalid longitude: north-ish", request.error.message );
    Assert.assertNull( request.location );
  }


  @Test
  public void aBadLongitudeIsReportedAsABadLatitudeCurrentBehavior() {

    enterCoordinates( "N 47 37 12", "west-ish", "0" );

    new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.BAD_INPUT, request.error.errorCode );
    Assert.assertEquals( "Invalid latitude: west-ish", request.error.message );
  }


  /** An elevation that is not a number is not caught, so acquiring the location throws. */
  @Test
  public void anElevationThatIsNotANumberThrowsCurrentBehavior() {

    enterCoordinates( "N 47 37 12", "W 122 19 48", "high" );

    Assert.assertThrows( NumberFormatException.class,
      () -> new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER ) );
    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
  }


  /** With nothing entered the coordinates are 0, 0, which the app treats as having no coordinates. */
  @Test
  public void noCoordinatesEnteredIsZeroZeroCurrentBehavior() {

    new LatitudeLongitudeLocationAcquirer( prefs, context ).acquire( PREFER );

    ZmanimLocation location = onlyRequest().location;
    Assert.assertEquals( 0.0, location.getLatitude(), 0.0 );
    Assert.assertEquals( 0.0, location.getLongitude(), 0.0 );
    Assert.assertFalse( location.hasCoordinates() );
  }

}  // End of UT_UserLocationAcquirers class
