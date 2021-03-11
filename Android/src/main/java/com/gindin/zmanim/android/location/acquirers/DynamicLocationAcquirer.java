/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanlib.location.ZmanimLocation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage;

/**
 * Abstracts dealing with the Android Fused Location Provider.
 */
public class DynamicLocationAcquirer
  extends LocationAcquirer {

  private static final long                                 ONE_MINUTE = 1 * 60 * 1000;
  private static final long                                 MAX_LAST_LOCATION_AGE = 30 * ONE_MINUTE;


  /** System-wide, location client. */
  private final FusedLocationProviderClient                 locationProvider;

  /**
   * If non-null it means we have an outstanding location request.
   */
  @Nullable
  private PendingIntent                                     pendingIntent;


  public DynamicLocationAcquirer(
    SharedPreferences     locationManagementPrefs,
    @NonNull Context      context
  ) {

    super( locationManagementPrefs, context );

    this.locationProvider = LocationServices.getFusedLocationProviderClient( context );
  }


  @Override
  public void acquire( final CachedLocationUsage cachedLocationUsage ) {

    if ( checkRequirements() ) {
      return;
    }

    if ( cachedLocationUsage == CachedLocationUsage.PREFER ) {
      useLastKnownLocation();
    }
    else {
      acquireLocation();
    }
  }


  private boolean checkRequirements() {

    boolean hasFineAccess = ( ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED );
    if ( !hasFineAccess ) {

      fireLocationNotAcquired( new LocationError( LocationError.Code.NO_PERMISSIONS ), ZmanLocationUtil.load( context ) );
      return true;
    }

    final int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable( context );
    if ( result != ConnectionResult.SUCCESS ) {

      fireLocationNotAcquired( new LocationError( LocationError.Code.NO_PLAY_SERVICES ), ZmanLocationUtil.load( context ) );
      return true;
    }

    return false;
  }


  private void useLastKnownLocation() {

    @SuppressLint( "MissingPermission" )
    final Task<Location> lastLocationTask = locationProvider.getLastLocation();
    lastLocationTask.addOnSuccessListener( location -> {

      // Docs say location can be null in these cases:
      //  1. Location is turned off in the device settings. The result could be null even if the last location was
      //      previously retrieved because disabling location also clears the cache.
      //  2. The device never recorded its location, which could be the case of a new device or a device that has been
      //      restored to factory settings.
      //  3. Google Play services on the device has restarted, and there is no active Fused Location Provider client
      //      that has requested location after the services restarted. To avoid this situation you can create a new
      //      client and request location updates yourself.
      if ( location == null ) {
        acquireLocation();
      }
      else {

        final long fixTime = location.getTime();
        if ( System.currentTimeMillis() - fixTime < MAX_LAST_LOCATION_AGE ) {

          final ZmanimLocation newLocation = ZmanLocationUtil.fromLocation( location, getTimeZoneFromPrefs() );

          // See the save() in ZmanimLocationAcquiredService after we have to acquire the location...
          ZmanLocationUtil.save( context, newLocation );
          fireLocationAcquired( newLocation );
        }
        else {
          acquireLocation();
        }
      }

    } );

    lastLocationTask.addOnFailureListener( this::onFailedToGetAnyFixes );

  }


  @SuppressLint( "MissingPermission" )
  private void acquireLocation() {

    if ( pendingIntent != null ) {
      locationProvider.removeLocationUpdates( pendingIntent );
    }

    // Have to actually go get the location.
    // These settings are largely taken from
    //  https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest#setFastestInterval(long)
    LocationRequest request = new LocationRequest()
      .setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY )
      .setInterval( 5 * 1000 )
      .setNumUpdates( 1 )
      .setFastestInterval( ONE_MINUTE );

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
      .addLocationRequest( request );
    SettingsClient client = LocationServices.getSettingsClient( context );
    Task<LocationSettingsResponse> task = client.checkLocationSettings( builder.build() );

    task
      .addOnSuccessListener( response -> {

        Intent locationReceivedIntent = ZmanimLocationAcquiredService.createIntent( context );

        // Per the Android sample code (https://github.com/googlesamples/android-play-location/blob/master/LocationUpdatesPendingIntent/app/src/main/java/com/google/android/gms/location/sample/locationupdatespendingintent/MainActivity.java)
        //    For apps targeting API level O, only PendingIntent.getBroadcast() should be used. This is due to the
        //      limits placed on services started in the background in "O".
        pendingIntent = PendingIntent.getBroadcast( context, 0, locationReceivedIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        locationProvider.requestLocationUpdates( request, pendingIntent );
      } )

      .addOnFailureListener( e -> {

        pendingIntent = null;
        onFailedToGetAnyFixes( e );
      } );

  }


  @Override
  public void cancel() {

    if ( pendingIntent != null ) {
      locationProvider.removeLocationUpdates( pendingIntent );
    }

    super.cancel();
  }


  @Override
  public void onDestroy() {

    super.onDestroy();
  }


  private void onFailedToGetAnyFixes( @Nullable Exception ex ) {

    ZmanimLocation lastKnownLocation = ZmanLocationUtil.load( context );

    if ( ex instanceof ResolvableApiException ) {
      // Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
      fireLocationNotAcquired( new LocationError( LocationError.Code.NO_LOCATION_MANAGER_SERVICE, (ResolvableApiException)ex ),
        lastKnownLocation );
    }
    else {
      fireLocationNotAcquired( new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION ), lastKnownLocation );
    }
  }


}  // End of DynamicLocationProvider class
