/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.location.LocationResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * What the receiver of a fresh device fix hands to the rest of the app. How Play Services packs the fix into the
 *  intent is not public, so {@link LocationResult#extractResult} is replaced for the duration of each test.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_ZmanimLocationAcquiredService {

  private Context                                           context;

  private MockedStatic<LocationResult>                      locationResult;


  @Before
  public void setup() {

    context = ApplicationProvider.getApplicationContext();
    context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE ).edit()
      .clear()
      .putBoolean( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name(), true )
      .putString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), "America" )
      .putString( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name(), "Chicago" )
      .commit();
    context.deleteFile( "LastKnownLocationProvider" );

    ZmanimServiceRequests.drain();
  }


  @After
  public void closeStaticMock() {
    if ( locationResult != null ) {
      locationResult.close();
    }
  }


  private static Location fix( double latitude ) {

    Location fix = new Location( "fused" );
    fix.setLatitude( latitude );
    fix.setLongitude( -122.33 );
    fix.setTime( System.currentTimeMillis() );
    return fix;
  }


  /** Makes the receiver find the given result in any intent (created before the static mock replaces the class). */
  private void deliver( LocationResult result ) {
    locationResult = Mockito.mockStatic( LocationResult.class );
    locationResult.when( () -> LocationResult.extractResult( Mockito.any( Intent.class ) ) ).thenReturn( result );
  }


  private static ZmanimServiceRequests.Request onlyRequest() {

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    return requests.get( 0 );
  }


  @Test
  public void theLatestFixIsSentAndSavedInTheChosenTimeZone() {

    deliver( LocationResult.create( Arrays.asList( fix( 47.60 ), fix( 47.62 ) ) ) );

    new ZmanimLocationAcquiredService().onReceive( context, ZmanimLocationAcquiredService.createIntent( context ) );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 47.62, request.location.getLatitude(), 1e-9 );
    Assert.assertEquals( TimeZone.getTimeZone( "America/Chicago" ), request.location.getTimeZone() );

    Assert.assertEquals( 47.62, ZmanLocationUtil.load( context ).getLatitude(), 1e-9 );
  }


  @Test
  public void aFixThatCannotBeReadIsReportedWithoutALastKnownLocation() {

    deliver( null );
    locationResult.when( () -> LocationResult.extractResult( Mockito.any( Intent.class ) ) )
      .thenThrow( new IllegalStateException( "bad parcel" ) );

    new ZmanimLocationAcquiredService().onReceive( context, ZmanimLocationAcquiredService.createIntent( context ) );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, request.error.errorCode );
    Assert.assertNull( request.location );
  }


  @Test
  public void anIntentWithoutAFixIsIgnored() {

    deliver( null );

    new ZmanimLocationAcquiredService().onReceive( context, ZmanimLocationAcquiredService.createIntent( context ) );

    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
  }


  @Test
  public void anIntentForAnotherActionIsIgnored() {

    deliver( LocationResult.create( Arrays.asList( fix( 47.62 ) ) ) );

    new ZmanimLocationAcquiredService().onReceive( context, new Intent( "some.other.ACTION" ) );

    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
    locationResult.verifyNoInteractions();
  }

}  // End of UT_ZmanimLocationAcquiredService class
