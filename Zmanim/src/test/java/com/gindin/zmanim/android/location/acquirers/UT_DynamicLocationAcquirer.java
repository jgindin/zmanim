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
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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

import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage.NEVER;
import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage.PREFER;

/**
 * What {@link DynamicLocationAcquirer} hands to the rest of the app for each outcome of asking the device for its
 *  location, through the real {@link FusedLocationSource}. The Play Services entry points are static, so they are
 *  replaced for the duration of each test.
 */
@RunWith( RobolectricTestRunner.class )
@Config( sdk = 24 )
@SuppressWarnings( "MagicNumber" )
public class UT_DynamicLocationAcquirer {

  private static final long                                 ONE_MINUTE = 60 * 1000;

  private static final double                               SEATTLE_SEA_LEVEL_ELEVATION = 76.7;

  private Application                                       context;

  private SharedPreferences                                 prefs;

  private MockedStatic<GoogleApiAvailability>               googleApiAvailability;

  private MockedStatic<LocationServices>                    locationServices;

  private GoogleApiAvailability                             availability;

  private FusedLocationProviderClient                       fusedClient;

  private SettingsClient                                    settingsClient;

  /** The location saved by an earlier run, which comes back with every error. */
  private ZmanimLocation                                    lastKnown;

  /** What a fresh-fix request delivers as soon as it starts; null delivers nothing. */
  @Nullable
  private Location                                          freshFix;


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

    freshFix = freshFixAt( 40.71, -74.01 );
    Mockito.when( fusedClient.requestLocationUpdates( Mockito.any( LocationRequest.class ),
        Mockito.any( LocationCallback.class ), Mockito.any( Looper.class ) ) )
      .thenAnswer( invocation -> {
        if ( freshFix != null ) {
          invocation.getArgument( 1, LocationCallback.class )
            .onLocationResult( LocationResult.create( Collections.singletonList( freshFix ) ) );
        }
        return Tasks.forResult( null );
      } );

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


  private static Location freshFixAt( double latitude, double longitude ) {

    Location fix = new Location( "fused" );
    fix.setLatitude( latitude );
    fix.setLongitude( longitude );
    fix.setTime( System.currentTimeMillis() );
    return fix;
  }


  /**
   * An acquirer over the app's fused location source that starts each request on the calling thread. Mockito
   *  replaces the static Play Services entry points on the test's thread only, and the source asks whether Play
   *  Services are available when the request starts.
   */
  public static DynamicLocationAcquirer startingOnThisThread( SharedPreferences prefs, Context context ) {
    return new DynamicLocationAcquirer( prefs, context, FusedLocationSource.create( context ),
      CoroutineScopeKt.CoroutineScope( Dispatchers.getUnconfined() ) );
  }


  /** Runs the acquirer until it has handed over its answer, as the service does. */
  private DynamicLocationAcquirer acquire( CachedLocationUsage usage ) {

    DynamicLocationAcquirer acquirer = startingOnThisThread( prefs, context );
    acquirer.acquire( usage );
    acquirer.awaitCompletion();
    return acquirer;
  }


  private static ZmanimServiceRequests.Request onlyRequest() {

    List<ZmanimServiceRequests.Request> requests = ZmanimServiceRequests.drain();
    Assert.assertEquals( 1, requests.size() );
    return requests.get( 0 );
  }


  private void assertNoFreshFixRequested() {
    Mockito.verify( fusedClient, Mockito.never() ).requestLocationUpdates( Mockito.any( LocationRequest.class ),
      Mockito.any( LocationCallback.class ), Mockito.any( Looper.class ) );
  }


  private LocationRequest freshFixRequest() {

    ArgumentCaptor<LocationRequest> request = ArgumentCaptor.forClass( LocationRequest.class );
    Mockito.verify( fusedClient ).requestLocationUpdates( request.capture(), Mockito.any( LocationCallback.class ),
      Mockito.any( Looper.class ) );
    return request.getValue();
  }


  /** The single piece of work sent, which must be the fresh fix, unnamed. */
  private static void assertTheFreshFixIsSent() {

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 40.71, request.location.getLatitude(), 1e-9 );
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


  /** An approximate-only grant (Android 12 and later let the user choose it) is accepted. */
  @Test
  public void anApproximateOnlyGrantGetsTheLocation() {

    Shadows.shadowOf( context ).denyPermissions( Manifest.permission.ACCESS_FINE_LOCATION );
    lastLocation( Tasks.forResult( fixMinutesAgo( 29 ) ) );

    acquire( PREFER );

    ZmanimServiceRequests.Request request = onlyRequest();
    Assert.assertEquals( "PLACE_ACQUIRED", request.action );
    Assert.assertEquals( 47.62, request.location.getLatitude(), 1e-9 );

    acquire( NEVER );

    assertTheFreshFixIsSent();
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
    Assert.assertEquals( "fused", request.location.providerName );
    // The fix's altitude is above the ellipsoid, and the geoid is about 21 m below it in Seattle.
    Assert.assertEquals( SEATTLE_SEA_LEVEL_ELEVATION, request.location.getElevation(), 0.5 );

    Assert.assertEquals( 47.62, ZmanLocationUtil.load( context ).getLatitude(), 1e-9 );
    assertNoFreshFixRequested();
  }


  @Test
  public void aLastKnownFixThirtyMinutesOldOrMoreIsNotUsed() {

    lastLocation( Tasks.forResult( fixMinutesAgo( 31 ) ) );

    acquire( PREFER );

    assertTheFreshFixIsSent();
  }


  @Test
  public void noLastKnownFixAsksForAFreshOne() {

    lastLocation( Tasks.forResult( null ) );

    acquire( PREFER );

    assertTheFreshFixIsSent();
  }


  @Test
  public void whenTheCachedLocationIsNotWantedTheLastKnownFixIsNotAskedFor() {

    acquire( NEVER );

    Mockito.verify( fusedClient, Mockito.never() ).getLastLocation();
    assertTheFreshFixIsSent();
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
  public void theFreshFixIsSavedForTheNextRun() {

    acquire( NEVER );

    Assert.assertEquals( 40.71, ZmanLocationUtil.load( context ).getLatitude(), 1e-9 );
  }


  @Test
  public void cancellingStopsTheOutstandingRequestWithoutAnAnswer() {

    freshFix = null;
    DynamicLocationAcquirer acquirer = startingOnThisThread( prefs, context );
    acquirer.acquire( NEVER );

    ArgumentCaptor<LocationCallback> callback = ArgumentCaptor.forClass( LocationCallback.class );
    Mockito.verify( fusedClient, Mockito.timeout( TimeUnit.SECONDS.toMillis( 10 ) ) )
      .requestLocationUpdates( Mockito.any( LocationRequest.class ), callback.capture(), Mockito.any( Looper.class ) );

    acquirer.cancel();
    acquirer.awaitCompletion();

    Mockito.verify( fusedClient ).removeLocationUpdates( callback.getValue() );
    Assert.assertTrue( ZmanimServiceRequests.drain().isEmpty() );
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
  public void aFailureToReadTheLastKnownFixAsksForAFreshOne() {

    lastLocation( Tasks.forException( new IllegalStateException( "no last location" ) ) );

    acquire( PREFER );

    assertTheFreshFixIsSent();
  }

}  // End of UT_DynamicLocationAcquirer class
