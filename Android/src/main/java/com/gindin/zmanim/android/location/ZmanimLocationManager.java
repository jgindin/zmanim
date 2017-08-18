/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.location.acquirers.DynamicLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LatitudeLongitudeLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.StaticLocationAcquirer;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.jetbrains.annotations.NotNull;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Handles all things related to finding and knowing the location necessary to compute the Zmanim.
 *<p/>
 *
 * Listens for:
 *  <ul>
 *    <li>RequestLocationUpdateEvent
 *  </ul>
 *<p/>
 *
 * Events Fired:
 *  <ul>
 *    <li>RawLocationAcquiredEvent
 *    <li>FullLocationAcquiredEvent
 *  </ul>
 */
public class ZmanimLocationManager {


  public enum Type {
    FROM_DEVICE,
    LAT_LONG_FROM_USER,
    NAME_FROM_USER
  }


  private final Context                                     context;

  private final EventBus                                    eventBus;

  /** Everyone wants to see what preferences have been set. */
  private final SharedPreferences                           locationManagementPrefs;

  private final SharedPreferences                           personalPrefs;

  /** System-wide, actual location manager. */
  private final LocationManager                             locationManager;

  private final Map<Type, LocationAcquirer>                 typeToAcquirerMap = new HashMap<Type, LocationAcquirer>();

  /**
   * Used to store & retrieve the last fix we received, but in a way that will survive restarts of the app.
   */
  private final LastKnownLocationProvider                   lastKnownLocationProvider;

  /** Which provider are we currently using? */
  private LocationAcquirer                                  currentAcquirer;

  /** Listen for requests to get a new location. */
  private RequestLocationUpdateEvent.Handler                locationRequestHandler;

  /** Listen for the location to be acquired in order to cache it. */
  private LocationAcquiredEvent.Handler                     locationAcquiredHandler;



  public ZmanimLocationManager(
    Context           context,
    EventBus          eventBus,
    SharedPreferences locationManagementPrefs,
    SharedPreferences personalPrefs,
    LocationManager   locationManager
  ) {

    this.context = context;
    this.eventBus = eventBus;
    this.locationManagementPrefs = locationManagementPrefs;
    this.personalPrefs = personalPrefs;
    this.locationManager = locationManager;

    lastKnownLocationProvider = new LastKnownLocationProvider( context );

    locationRequestHandler = new RequestLocationUpdateEvent.Handler() {
      @Override
      public void onLocationUpdateRequested( final RequestLocationUpdateEvent event ) {
        determineLocation( event );
      }
    };
    eventBus.addHandler( locationRequestHandler );

    locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
      @Override
      public void onLocationAcquired(
        ZmanimLocation location,
        boolean isCachedLocation
      ) {
        lastKnownLocationProvider.save( location );
      }


      @Override
      public void onError(
        @NotNull LocationError error,
        @NotNull ZmanimLocation lastKnownLocation
      ) {
        // Ignore on error...
      }
    };
    eventBus.addHandler( locationAcquiredHandler );
  }


  public void onStop() {

    if ( locationRequestHandler != null ) {
      eventBus.removeHandler( locationRequestHandler );
      locationRequestHandler = null;
    }

    if ( locationAcquiredHandler != null ) {
      eventBus.removeHandler( locationAcquiredHandler );
      locationAcquiredHandler = null;
    }

    cancel();
  }


  private void determineLocation( final RequestLocationUpdateEvent event ) {

    cancel();
    currentAcquirer = null;

    if ( event.locationDeterminationOverride == RequestLocationUpdateEvent.LocationDeterminationOverride.FORCE_TO_ACTUAL_LOCATION ) {
      currentAcquirer = getAcquirer( Type.FROM_DEVICE );
    }
    else {

      boolean useThis = locationManagementPrefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
      if ( useThis ) {
        currentAcquirer = getAcquirer( Type.FROM_DEVICE );
      }
      else {

        useThis = locationManagementPrefs.getBoolean( LocationProviders.STATIC_POSITION.name(), false );
        if ( useThis ) {
          currentAcquirer = getAcquirer( Type.NAME_FROM_USER );
        }
        else {

          useThis = locationManagementPrefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false );
          if ( useThis ) {
            currentAcquirer = getAcquirer( Type.LAT_LONG_FROM_USER );
          }
        }
      }
    }

    // Didn't find anything? Use dynamic positioning, and make sure the prefs are updated.
    if ( currentAcquirer == null ) {
      currentAcquirer = getAcquirer( Type.FROM_DEVICE );
      locationManagementPrefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).apply();
    }

    currentAcquirer.acquire( event.cachedLocationUsage );
  }


  private void cancel() {

    if ( currentAcquirer != null ) {
      currentAcquirer.cancel();
      currentAcquirer = null;
    }
  }


  public List<String> getAllProviders( boolean evenDisabledProviders ) {

    ArrayList<String> providerNames;

    if ( evenDisabledProviders ) {
      providerNames = new ArrayList<String>( locationManager.getAllProviders() );

      // We treat the last known provider as disabled because we don't want someone thinking that they can (or should)
      //  ask this provider for an update. (We also prevent that in the requestSingleUpdate method, but this is a good
      //  safe guard.)
      providerNames.add( LastKnownLocationProvider.NAME );
    }
    else {
      providerNames = new ArrayList<String>( locationManager.getProviders( true ) );
    }

    return providerNames;
  }


  /**
   * @return The location provider's last known location, null if it doesn't know it.
   *
   * @throws SecurityException in case we don't have the location permission.
   */
  @Nullable
  public Location getLastKnownLocation( String providerName ) throws SecurityException {

    if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
      return lastKnownLocationProvider.getLastKnownLocation();
    }
    else if ( hasLocationPermissions() ) {
      //noinspection MissingPermission
      return locationManager.getLastKnownLocation( providerName );
    }
    else {
      throw new SecurityException( "No location permissions available" );
    }
  }


  public boolean isProviderEnabled( String providerName ) {

    return !LastKnownLocationProvider.NAME.equals( providerName ) && locationManager.isProviderEnabled( providerName );
  }


  /**
   * Tries to use the specified provider to get a one-time location update.
   *
   * @throws SecurityException in case we don't have the location permission.
   */
  public void requestSingleUpdate( String providerName, LocationListener listener ) throws SecurityException {

    //noinspection StatementWithEmptyBody
    if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
      // Don't allow this. It'll prevent the real providers from having a chance....
    }
    else if ( hasLocationPermissions() ) {
      //noinspection MissingPermission
      locationManager.requestSingleUpdate( providerName, listener, null );
    }
    else {
      throw new SecurityException( "No location permissions available" );
    }
  }


  public void removeUpdates( LocationListener listener ) {

    if ( hasLocationPermissions() ) {
      //noinspection MissingPermission
      locationManager.removeUpdates( listener );
    }
  }


  private LocationAcquirer getAcquirer( Type type ) {

    LocationAcquirer provider = typeToAcquirerMap.get( type );
    if ( provider == null ) {
      switch ( type ) {
        case FROM_DEVICE:
          provider = new DynamicLocationAcquirer( eventBus, locationManagementPrefs, personalPrefs, this );
          break;

        case LAT_LONG_FROM_USER:
          provider = new LatitudeLongitudeLocationAcquirer( eventBus, locationManagementPrefs, personalPrefs );
          break;

        case NAME_FROM_USER:
          provider = new StaticLocationAcquirer( eventBus, locationManagementPrefs, personalPrefs );
          break;

        default:
          if ( BuildConfig.DEBUG ) {
            throw new AssertionError( "Unknown provider type: " + type );
          }
      }

      typeToAcquirerMap.put( type, provider );
    }

    return provider;
  }


  private boolean hasLocationPermissions() {

    boolean hasFineAccess = ( ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED );
    boolean hasCoarseAccess = ( ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED );

    return hasFineAccess || hasCoarseAccess;
  }

}  // End of ZmanimLocation class
