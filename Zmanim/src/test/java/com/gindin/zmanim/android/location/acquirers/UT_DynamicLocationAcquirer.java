/*
 * Copyright (c) 2026. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.location.ZmanimServiceRequests;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage.NEVER;
import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage.PREFER;

/**
 * What {@link DynamicLocationAcquirer} hands to the rest of the app for each outcome of asking the device for its
 *  location. The Play Services entry points are static, so they are replaced for the duration of each test. Tests
 *  named {@code ...CurrentBehavior} pin behavior that looks wrong; it is kept as it is until a later change fixes it
 *  on purpose.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_DynamicLocationAcquirer {

  private static final long                                 ONE_MINUTE = 60 * 1000;

  private Application                                       context;

  private SharedPreferences                                 prefs;

  private MockedStatic<GoogleApiAvailability>               googleApiAvailability;

  private MockedStatic<LocationServices>                    locationServices;

  private GoogleApiAvailability                             availability;

  private FusedLocationProviderClient                       fusedClient;

  private SettingsClient                                    settingsClient;

  /** The location saved by an earlier run, which comes back with every error. */
  private ZmanimLocation                                    lastKnown;


  @Before
  public void setup() {

    context = ApplicationProvider.getApplicationContext();
    prefs = context.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
    prefs.edit().clear().commit();

    lastKnown = new ZmanimLocation.Builder()
      .locationName( "Albany, New York" )
      .atLatitude( 42.65 )
      .atLongitude( -73.76 )
      .build();
    ZmanLocationUtil.save( context, lastKnown );

    availability = Mockito.mock( GoogleApiAvailability.class );
    Mockito.when( availability.isGooglePlayServicesAvailable( Mockito.any( Context.class ) ) )
      .thenReturn( ConnectionResult.SUCCESS );
    googleApiAvailability = Mockito.mockStatic( GoogleApiAvailability.class );
    googleApiAvailability.when( GoogleApiAvailability::getInstance ).thenReturn( availability );

    fusedClient = Mockito.mock( FusedLocationProviderClient.class );
    settingsClient = Mockito.mock( SettingsClient.class );
    locationServices = Mockito.mockStatic( LocationServices.class );
    locationServices.when( () -> LocationServices.getFusedLocationProviderClient( Mockito.any( Context.class ) ) )
      .thenReturn( fusedClient );
    locationServices.when( () -> LocationServices.getSettingsClient( Mockito.any( Context.class ) ) )
      .thenReturn( settingsClient );

    Shadows.shadowOf( context ).grantPermissions( Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION );

    settingsAnswer( Tasks.forResult( (LocationSettingsResponse)null ) );

    ZmanimServiceRequests.drain();
  }


  @After
  public void closeStaticMocks() {
    locationServices.close();
    googleApiAvailability.close();
  }


  private void lastLocation( Task<Location> task ) {
    Mockito.when( fusedClient.getLastLocation() ).thenReturn( task );
  }


  private void settingsAnswer( Task<LocationSettingsResponse> task ) {
    Mockito.when( settingsClient.checkLocationSettings( Mockito.any( LocationSettingsRequest.class ) ) )
      .thenReturn( task );
  }


  private static Location fixMinutesAgo( long minutes ) {

    Location fix = new Location( "fused" );
    fix.setLatitude( 47.62 );
    fix.setLongitude( -122.33 );
    fix.setAltitude( 56 );
    fix.setAccuracy( 12 );
    fix.setTime( System.currentTimeMillis() - ( minutes * ONE_MINUTE ) );
    return fix;
  }


  /** Runs the acquirer, and the Play Services listeners it registers (they are called on the main thread). */
  private DynamicLocationAcquirer acquire( CachedLocationUsage usage ) {

    DynamicLocationAcquirer acquirer = new DynamicLocationAcquirer( prefs, context );
    acquirer.acquire( usage );
    Shadows.shadowOf( Looper.getMainLooper() ).idle();
    return acquirer;
  }


  private static ZmanimServiceRequests.Request onlyRequest() {

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    return requests.get( 0 );
  }


  private void assertNoFreshFixRequested() {
    Mockito.verify( fusedClient, Mockito.never() )
      .requestLocationUpdates( Mockito.any( LocationRequest.class ), Mockito.any( PendingIntent.class ) );
  }


  private LocationRequest freshFixRequest() {

    ArgumentCaptor<LocationRequest> request = ArgumentCaptor.forClass( LocationRequest.class );
    Mockito.verify( fusedClient ).requestLocationUpdates( request.capture(), Mockito.any( PendingIntent.class ) );
    return request.getValue();
  }


  @Test
  public void withoutLocationPermissionTheLastKnownLocationIsSentWithAnError() {

    Shadows.shadowOf( context ).denyPermissions( Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION );

    acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "LOCATION_NOT_ACQUIRED", request.action );
    Assert.assertEquals( LocationError.Code.NO_PERMISSIONS, request.error.errorCode );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( fusedClient );
  }


  /** An approximate-only grant (Android 12 and later let the user choose it) is treated as no permission at all. */
  @Test
  public void anApproximateOnlyGrantIsTreatedAsNoPermissionCurrentBehavior() {

    Shadows.shadowOf( context ).denyPermissions( Manifest.permission.ACCESS_FINE_LOCATION );

    acquire( PREFER );

    Assert.assertEquals( LocationError.Code.NO_PERMISSIONS, onlyRequest().error.errorCode );
    Mockito.verifyNoInteractions( fusedClient );
  }


  @Test
  public void withoutPlayServicesTheLastKnownLocationIsSentWithAnError() {

    Mockito.when( availability.isGooglePlayServicesAvailable( Mockito.any( Context.class ) ) )
      .thenReturn( ConnectionResult.SERVICE_MISSING );

    acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.NO_PLAY_SERVICES, request.error.errorCode );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    Mockito.verifyNoInteractions( fusedClient );
  }


  @Test
  public void aLastKnownFixUnderThirtyMinutesOldIsUsedAndSaved() {

    lastLocation( Tasks.forResult( fixMinutesAgo( 29 ) ) );

    acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    // It has no name yet, so the service sends it on to be named.
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 47.62, request.location.getLatitude(), 1e-9 );
    Assert.assertEquals( 56.0, request.location.getElevation(), 0.0 );
    Assert.assertEquals( "fused", request.location.providerName );

    Assert.assertEquals( 47.62, ZmanLocationUtil.load( context ).getLatitude(), 1e-9 );
    assertNoFreshFixRequested();
  }


  @Test
  public void aLastKnownFixThirtyMinutesOldOrMoreIsNotUsed() {

    lastLocation( Tasks.forResult( fixMinutesAgo( 31 ) ) );

    acquire( PREFER );

    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
    Assert.assertEquals( "Albany, New York", ZmanLocationUtil.load( context ).getLocationName() );
    freshFixRequest();
  }


  @Test
  public void noLastKnownFixAsksForAFreshOne() {

    lastLocation( Tasks.forResult( null ) );

    acquire( PREFER );

    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
    freshFixRequest();
  }


  @Test
  public void whenTheCachedLocationIsNotWantedTheLastKnownFixIsNotAskedFor() {

    acquire( NEVER );

    Mockito.verify( fusedClient, Mockito.never() ).getLastLocation();
    freshFixRequest();
  }


  @Test
  public void theFreshFixIsOneHighAccuracyUpdateAtMostAMinuteOld() {

    acquire( NEVER );

    LocationRequest request = freshFixRequest();
    Assert.assertEquals( Priority.PRIORITY_HIGH_ACCURACY, request.getPriority() );
    Assert.assertEquals( 1, request.getMaxUpdates() );
    Assert.assertEquals( ONE_MINUTE, request.getMaxUpdateAgeMillis() );
    Assert.assertEquals( 5 * 1000, request.getIntervalMillis() );
  }


  @Test
  public void theFreshFixIsDeliveredToTheLocationReceiver() {

    acquire( NEVER );

    ArgumentCaptor<PendingIntent> pendingIntent = ArgumentCaptor.forClass( PendingIntent.class );
    Mockito.verify( fusedClient ).requestLocationUpdates( Mockito.any( LocationRequest.class ), pendingIntent.capture() );

    Assert.assertEquals( ZmanimLocationAcquiredService.createIntent( context ).getAction(),
      Shadows.shadowOf( pendingIntent.getValue() ).getSavedIntent().getAction() );
    Assert.assertTrue( Shadows.shadowOf( pendingIntent.getValue() ).isBroadcastIntent() );
  }


  @Test
  public void cancellingStopsTheOutstandingRequest() {

    DynamicLocationAcquirer acquirer = acquire( NEVER );

    ArgumentCaptor<PendingIntent> pendingIntent = ArgumentCaptor.forClass( PendingIntent.class );
    Mockito.verify( fusedClient ).requestLocationUpdates( Mockito.any( LocationRequest.class ), pendingIntent.capture() );

    acquirer.cancel();

    Mockito.verify( fusedClient ).removeLocationUpdates( pendingIntent.getValue() );
  }


  @Test
  public void locationServicesTurnedOffAreReportedWithTheWayToTurnThemOn() {

    settingsAnswer( Tasks.forException(
      new ResolvableApiException( new Status( CommonStatusCodes.RESOLUTION_REQUIRED, "Location is off" ) ) ) );

    acquire( NEVER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.NO_LOCATION_MANAGER_SERVICE, request.error.errorCode );
    Assert.assertNotNull( request.error.apiExceptionStatus );
    Assert.assertEquals( CommonStatusCodes.RESOLUTION_REQUIRED, request.error.apiExceptionStatus.getStatusCode() );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
    assertNoFreshFixRequested();
  }


  @Test
  public void anyOtherSettingsFailureIsReportedAsCannotDetermineLocation() {

    settingsAnswer( Tasks.forException( new IllegalStateException( "no settings" ) ) );

    acquire( NEVER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, request.error.errorCode );
    Assert.assertEquals( "Albany, New York", request.location.getLocationName() );
  }


  @Test
  public void aFailureToReadTheLastKnownFixIsReportedAsCannotDetermineLocation() {

    lastLocation( Tasks.forException( new IllegalStateException( "no last location" ) ) );

    acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( LocationError.Code.CANNOT_DETERMINE_LOCATION, request.error.errorCode );
    assertNoFreshFixRequested();
  }

}  // End of UT_DynamicLocationAcquirer class
