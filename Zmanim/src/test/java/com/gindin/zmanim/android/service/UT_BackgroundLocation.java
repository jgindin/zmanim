/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.service;

import com.gindin.zmanim.android.location.ZmanimLocationManager;
import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanim.android.location.acquirers.LocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.UT_DynamicLocationAcquirer;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Tasks;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The work that needs the location while the app is in the background: the periodic alarm check from
 *  {@link ZmanimReceiver}, which accepts a recent fix, and booting or a change of the clock, which ask for a fresh one.
 *  Each must get the location under an approximate-only grant, and the service's work must not end before the fix has
 *  been handed over, or the system may stop the process first.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_BackgroundLocation {

  /** How long the fresh fix takes to arrive; longer than the service would take if it did not wait. */
  private static final long                                 FRESH_FIX_DELAY_MS = 300;

  private Application                                       context;

  private MockedStatic<GoogleApiAvailability>               googleApiAvailability;

  private MockedStatic<LocationServices>                    locationServices;

  private FusedLocationProviderClient                       fusedClient;

  private ServiceController<ZmanimService>                  service;


  @Before
  public void setup() throws Exception {

    context = ApplicationProvider.getApplicationContext();

    GoogleApiAvailability availability = Mockito.mock( GoogleApiAvailability.class );
    Mockito.when( availability.isGooglePlayServicesAvailable( Mockito.any( Context.class ) ) )
      .thenReturn( ConnectionResult.SUCCESS );
    googleApiAvailability = Mockito.mockStatic( GoogleApiAvailability.class );
    googleApiAvailability.when( GoogleApiAvailability::getInstance ).thenReturn( availability );

    fusedClient = Mockito.mock( FusedLocationProviderClient.class );
    SettingsClient settingsClient = Mockito.mock( SettingsClient.class );
    Mockito.when( settingsClient.checkLocationSettings( Mockito.any( LocationSettingsRequest.class ) ) )
      .thenReturn( Tasks.forResult( (LocationSettingsResponse)null ) );
    locationServices = Mockito.mockStatic( LocationServices.class );
    locationServices.when( () -> LocationServices.getFusedLocationProviderClient( Mockito.any( Context.class ) ) )
      .thenReturn( fusedClient );
    locationServices.when( () -> LocationServices.getSettingsClient( Mockito.any( Context.class ) ) )
      .thenReturn( settingsClient );

    // Approximate location only.
    Shadows.shadowOf( context ).grantPermissions( Manifest.permission.ACCESS_COARSE_LOCATION );
    Shadows.shadowOf( context ).denyPermissions( Manifest.permission.ACCESS_FINE_LOCATION );

    service = Robolectric.buildService( ZmanimService.class ).create();
    useAnAcquirerThatStartsOnThisThread();
    ZmanimServiceRequests.drain();
  }


  /**
   * Mockito replaces the static Play Services entry points on the test's thread only, so the service's location
   *  manager is given a device acquirer that starts each request on the thread that asks.
   */
  private void useAnAcquirerThatStartsOnThisThread() throws Exception {

    Field managerField = ZmanimService.class.getDeclaredField( "locationManager" );
    managerField.setAccessible( true );
    ZmanimLocationManager manager = (ZmanimLocationManager)managerField.get( service.get() );

    Field acquirersField = ZmanimLocationManager.class.getDeclaredField( "typeToAcquirerMap" );
    acquirersField.setAccessible( true );
    @SuppressWarnings( "unchecked" )
    Map<ZmanimLocationManager.Type, LocationAcquirer> acquirers =
      (Map<ZmanimLocationManager.Type, LocationAcquirer>)acquirersField.get( manager );

    SharedPreferences prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    acquirers.put( ZmanimLocationManager.Type.FROM_DEVICE, UT_DynamicLocationAcquirer.startingOnThisThread( prefs, context ) );
  }


  @After
  public void tearDown() {
    service.destroy();
    locationServices.close();
    googleApiAvailability.close();
  }


  private static Location fixAt( double latitude, long ageMillis ) {

    Location fix = new Location( "fused" );
    fix.setLatitude( latitude );
    fix.setLongitude( -73.76 );
    fix.setTime( System.currentTimeMillis() - ageMillis );
    return fix;
  }


  /** Runs the service's work for an alarm check, as {@link ZmanimReceiver} asks for it. */
  private List<ZmanimServiceRequests.Request> checkAlarms( boolean force ) {

    Intent work = new Intent( context, ZmanimService.class );
    work.setAction( ZmanimService.Actions.SCHEDULE_ALARM.name() );
    work.putExtra( "FORCE_SCHEDULE", force );

    service.get().onHandleWork( work );
    return ZmanimServiceRequests.drain();
  }


  @Test
  public void thePeriodicCheckGetsARecentFix() {

    Mockito.when( fusedClient.getLastLocation() ).thenReturn( Tasks.forResult( fixAt( 42.65, 60_000 ) ) );

    ZmanimServiceRequests.Request request = checkAlarms( false ).get( 0 );

    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 42.65, request.location.getLatitude(), 1e-9 );
    Mockito.verify( fusedClient, Mockito.never() ).requestLocationUpdates( Mockito.any( LocationRequest.class ),
      Mockito.any( LocationCallback.class ), Mockito.any( Looper.class ) );
  }


  @Test
  public void bootOrAClockChangeWaitsForAFreshFix() {

    Mockito.when( fusedClient.requestLocationUpdates( Mockito.any( LocationRequest.class ),
        Mockito.any( LocationCallback.class ), Mockito.any( Looper.class ) ) )
      .thenAnswer( invocation -> {
        LocationCallback callback = invocation.getArgument( 1, LocationCallback.class );
        new Thread( () -> {
          try {
            Thread.sleep( FRESH_FIX_DELAY_MS );
          }
          catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
          }
          callback.onLocationResult( LocationResult.create( Collections.singletonList( fixAt( 42.66, 0 ) ) ) );
        } ).start();
        return Tasks.forResult( null );
      } );

    List<ZmanimServiceRequests.Request> requests = checkAlarms( true );

    Assert.assertEquals( 1, requests.size() );
    Assert.assertEquals( "PLACE_ACQUIRED", requests.get( 0 ).action );
    Assert.assertEquals( 42.66, requests.get( 0 ).location.getLatitude(), 1e-9 );
    Mockito.verify( fusedClient, Mockito.never() ).getLastLocation();
  }

}  // End of UT_BackgroundLocation class
