/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.eventBus.EventBus;
import com.gindin.util.Pair;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.*;

import static com.gindin.zmanim.android.location.RequestLocationUpdateEvent.CachedLocationUsage;

/**
 * Abstracts dealing with the Android LocationManager.
 *<p>
 * The way this works is as follows:
 *  - We know about two different Android location providers: Network and GPS
 *  - It is possible, though not likely, for network to not be enabled
 *  - It is likely that GPS won't be enabled.
 *<p>
 * The user may have indicated that they prefer to use GPS location, in which case if we're able to get a GPS fix,
 *  that location must take precedence. However, just because they *prefer* GPS, doesn't mean that we won't use
 *  some other mechanism if we're not able to get a GPS fix.
 *<p>
 * When the first listener is added, we will make sure that we have either the network OR GPS provider enabled.
 *  If neither are enabled, we need to error out...
 *  Otherwise, ask all available providers to get us a fix. If the first provider we get a response from is:
 *    <ul>
 *      <li>the network, and the user doesn't care about GPS, tell other providers to stop, and let our listeners
 *          know we've got a location.
 *      <li>the network, but the user prefers GPS:
 *        <ul>
 *          <li>If the GPS provider isn't enabled, then tell other providers to stop, and let our listeners know we've got a location.
 *          <li>Otherwise, continue waiting for the GPS provider
 *        </ul>
 *      <li>GPS, then tell all the other providers to stop, and let our listeners know we've got a location.
 *
 *<p>
 * Once we have a location, we really don't need to bother getting updates. Really. I doubt that anyone is going to run
 *  this app long enough to move far enough that it would make a difference. Flurry shows the median session length
 *  as about 30 seconds...
 *
 *<p>
 *  The last bit of info to know relates to the "continue waiting for the GPS provider" comment above. When we start
 *    asking the location providers for a fix, we kick off a timer. If that time actually expires, then if we did receive
 *    a fix from some other location provider, we'll use that. Otherwise, we'll fire an error.
 */
public class DynamicLocationAcquirer
  extends LocationAcquirer {


  /** We have one listener for every location provider. */
  private final List<AndroidLocationListener>               locationListeners = new ArrayList<>();

  /**
   * We need to wait for "some time" before giving up on all the location providers. So, when any of the location
   *  providers give us a fix, we cancel this timer. If it expires, then it will cause us to fire an error.
   */
  private WaitingForLocationTimer                           waitingForLocationTimer = null;


  /** Do we prefer to receive GPS fixes over other providers? */
  private boolean                                           preferGPS;

  @org.jetbrains.annotations.NotNull
  private final Context                                     context;

  /** System-wide, actual location manager. */
  private final LocationManager                             locationManager;

  /**
   * Used to store & retrieve the last fix we received, but in a way that will survive restarts of the app.
   */
  private final LastKnownLocationProvider                   lastKnownLocationProvider;



  public DynamicLocationAcquirer(
    EventBus              eventBus,
    SharedPreferences     locationManagementPrefs,
    SharedPreferences     personalPrefs,
    Context               context
  ) {

    super( eventBus, locationManagementPrefs, personalPrefs );

    this.context = context;
    this.locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
    this.lastKnownLocationProvider = new LastKnownLocationProvider( context );
    this.preferGPS = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_GPS.name(), false );
  }


  @Override
  public void acquire( final CachedLocationUsage cachedLocationUsage ) {

    if ( cachedLocationUsage == CachedLocationUsage.PREFER ) {
      try {
        ZmanimLocation lastKnownLocation = getLastKnownLocation();
        fireLocationAcquiredEvent( lastKnownLocation, true );
      }
      catch ( SecurityException e ) {
        fireLocationAcquiredEvent( new ZmanimLocation.Builder().invalidLocation(), true, new LocationError( LocationError.Code.NO_PERMISSIONS ) );
      }
    }

    startListeningToLocations();
  }


  @Override
  public void cancel() {
    super.cancel();
    stopListeningToLocations();
  }


  @Override
  public void onDestroy() {

    stopListeningToLocations();
    super.onDestroy();
  }


  private boolean hasLocationPermissions() {

    boolean hasFineAccess = ( ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED );
    boolean hasCoarseAccess = ( ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED );

    return hasFineAccess || hasCoarseAccess;
  }


  /**
   * Check the various location providers for their last known locations, and try to use the most recent.
   *
   * @throws SecurityException In case we were unable to get any last known location due to a lack of permissions.
   */
  @SuppressWarnings( { "AutoBoxing", "AutoUnboxing", "FeatureEnvy" } )
  @NonNull
  private ZmanimLocation getLastKnownLocation() throws SecurityException {

    List<Pair<Location,Long>> lastKnownLocationsWithTime = new ArrayList<>();

    boolean noLocationPermissions = false;

    for ( String provider : getAllProviders( true ) ) {
      Location lastKnownLocation;

      try {
        lastKnownLocation = getLastKnownLocation( provider );
      }
      catch ( SecurityException e ) {
        noLocationPermissions = true;
        lastKnownLocation = null;
      }

      if ( null != lastKnownLocation ) {
        lastKnownLocationsWithTime.add( new Pair<>( lastKnownLocation, lastKnownLocation.getTime() ) );
      }
    }

    Pair<Location,Long> bestLocation = new Pair<>( null, Long.MIN_VALUE );
    for ( Pair<Location, Long> pair : lastKnownLocationsWithTime ) {
      if ( pair.second > bestLocation.second ) {
        bestLocation = pair;
      }
    }

    ZmanimLocation location;
    if ( bestLocation.first != null ) {

      final TimeZone timeZone = getTimeZoneFromPrefs();
      location = ZmanLocationUtil.fromLocation( bestLocation.first, timeZone );
    }
    else {

      if ( noLocationPermissions ) {
        // We got nothing and no way of getting anything...
        throw new SecurityException( "No location permissions." );
      }
      else {
        location = new ZmanimLocation.Builder()
          .invalidLocation();
      }
    }

    return location;
  }


  @SuppressWarnings( "FeatureEnvy" )
  private void onReceivedFix(
    final Location                newLocation,
    final AndroidLocationListener source
  ) {

    // We've got a fix we can use, even if it turns out to not be from our preferred method. Make sure
    //  we don't time out and claim to have failed...
    cancelWaitingForLocationTimer();


    // Now, there's a few cases here:
    //  1. We don't care how we get a fix: Tell all the location listeners to cancel their first fix
    //  2. We prefer GPS, but this isn't: Let the location listeners continue trying to get their first fix
    //  3. We prefer GPS and this is it:  Tell all the location listeners to cancel their first fix
    //  4. We prefer GPS, but it isn't turned on: Tell all the location listeners to cancel their first fix
    final boolean gpsProviderEnabled = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );

    if ( !preferGPS || LocationManager.GPS_PROVIDER.equals( source.providerName ) || !gpsProviderEnabled ) {
      stopListeningToLocations();
    }

    final TimeZone timeZone = getTimeZoneFromPrefs();
    ZmanimLocation location = ZmanLocationUtil.fromLocation( newLocation, timeZone );

    lastKnownLocationProvider.save( location );

    fireLocationAcquiredEvent( location, false );
  }


  private void onFailedToGetAnyFixes() {

    cancelWaitingForLocationTimer();

    for ( AndroidLocationListener listener : locationListeners ) {
      listener.cancel();
    }

    // Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
    try {
      fireLocationAcquiredEvent( getLastKnownLocation(), true, new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION )  );
    }
    catch ( SecurityException e ) {
      fireLocationAcquiredEvent( new ZmanimLocation.Builder().invalidLocation(), true, new LocationError( LocationError.Code.NO_PERMISSIONS ) );
    }
  }


  private void startListeningToLocations() {

    if ( !isLocationEnabled() ) {
      return;
    }

    // Cancel any outstanding timer *first* so that if one were to be running right now, it couldn't
    //  decide to fire...
    cancelWaitingForLocationTimer();

    // Now, kick off a timer such that if the timer expires, we're going to assume we can't get any
    //  location fix... Start this BEFORE any listeners get added in case one of the listeners returns
    //  a fix right away...
    final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );
    waitingForLocationTimer = new WaitingForLocationTimer( gpsProviderEnabled );

    // I'm going to listen to whatever providers might be available.
    final List<String> providers = getAllProviders( false );
    for ( String provider : providers ) {
      AndroidLocationListener listener = new AndroidLocationListener( provider );
      locationListeners.add( listener );
    }
  }


  private void stopListeningToLocations() {

    // Cancel any outstanding timer *first* so that if one were to be running right now, it couldn't
    //  decide to fire...
    cancelWaitingForLocationTimer();

    for ( AndroidLocationListener listener : locationListeners ) {
      listener.cancel();
    }

    locationListeners.clear();
  }


  private void cancelWaitingForLocationTimer() {

    if ( null != waitingForLocationTimer ) {
      waitingForLocationTimer.cancel();
      waitingForLocationTimer = null;
    }
  }


  private boolean isProviderEnabled( String providerName ) {

    return !LastKnownLocationProvider.NAME.equals( providerName ) && locationManager.isProviderEnabled( providerName );
  }


  private boolean isLocationEnabled() {

    if ( null == locationManager ) {
      try {
        fireLocationAcquiredEvent( getLastKnownLocation(), true, new LocationError( LocationError.Code.NO_LOCATION_MANAGER_SERVICE ) );
      }
      catch ( SecurityException e ) {
        fireLocationAcquiredEvent( new ZmanimLocation.Builder().invalidLocation(), true, new LocationError( LocationError.Code.NO_PERMISSIONS ) );
      }
      return false;
    }

    List<String> providers = getAllProviders( true );
    boolean foundAProvider = false;
    for ( String provider : providers ) {
      if ( locationManager.isProviderEnabled( provider ) ) {
        foundAProvider = true;
        break;
      }
    }

    if ( !foundAProvider ) {
      try {
        fireLocationAcquiredEvent( getLastKnownLocation(), true, new LocationError( LocationError.Code.NO_PROVIDERS ) );
      }
      catch ( SecurityException e ) {
        fireLocationAcquiredEvent( new ZmanimLocation.Builder().invalidLocation(), true, new LocationError( LocationError.Code.NO_PERMISSIONS ) );
      }
      return false;
    }

    return true;
  }


  /**
   * @return The location provider's last known location, null if it doesn't know it.
   *
   * @throws SecurityException in case we don't have the location permission.
   */
  @Nullable
  private Location getLastKnownLocation( String providerName ) throws SecurityException {

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


  private List<String> getAllProviders( boolean evenDisabledProviders ) {

    ArrayList<String> providerNames;

    if ( evenDisabledProviders ) {
      providerNames = new ArrayList<>( locationManager.getAllProviders() );

      // We treat the last known provider as disabled because we don't want someone thinking that they can (or should)
      //  ask this provider for an update. (We also prevent that in the requestSingleUpdate method, but this is a good
      //  safe guard.)
      providerNames.add( LastKnownLocationProvider.NAME );
    }
    else {
      providerNames = new ArrayList<>( locationManager.getProviders( true ) );
    }

    return providerNames;
  }





  /**
   * Listens to location changes for a given provider.
   */
  private class AndroidLocationListener
    implements LocationListener {

    /**
     * Who provides the location updates for this listener? Common identifiers are taken from the
     *  LocationManager class, e.g., LocationManager.NETWORK_PROVIDER or LocationManager.GPS_PROVIDER.
     */
    private final String                                    providerName;


    AndroidLocationListener(
      String  providerName
    ) {

      this.providerName = providerName;

      try {
        // TODO: I think with the move to JobIntent blah...we preobably can't rely on the service hanging around
        // TODO:  until we get the location update. I think we need to pass in a PendingIntent instead...
        // TODO:  Methinks this may require a good amount of refactoring....
        requestSingleUpdate( providerName, this );
      }
      catch ( SecurityException e ) {

        cancel();
        cancelWaitingForLocationTimer();

        for ( AndroidLocationListener listener : locationListeners ) {
          listener.cancel();
        }

        fireLocationAcquiredEvent( new ZmanimLocation.Builder().invalidLocation(), false,
          new LocationError( LocationError.Code.NO_PERMISSIONS ) );
      }
    }


    public void onLocationChanged(
      Location location
    ) {

      cancel();
      onReceivedFix( location, this );
    }


    void cancel() {

      if ( hasLocationPermissions() ) {
        //noinspection MissingPermission
        locationManager.removeUpdates( this );
      }
    }


    public void onStatusChanged( String provider, int status, Bundle extras ) {

      // Inherited from LocationListener...Not used
    }


    public void onProviderEnabled( String provider ) {
      // Inherited from LocationListener...Not used
    }


    public void onProviderDisabled( String provider ) {
      // Inherited from LocationListener...Not used
    }


    /**
     * Tries to use the specified provider to get a one-time location update.
     *
     * @throws SecurityException in case we don't have the location permission.
     */
    private void requestSingleUpdate(
      String            providerName,
      LocationListener  listener
    )
      throws SecurityException {

      //noinspection StatementWithEmptyBody
      if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
        // Don't allow this. It'll prevent the real providers from having a chance....
      }
      else if ( hasLocationPermissions() ) {
        locationManager.requestSingleUpdate( providerName, listener, null );
      }
      else {
        throw new SecurityException( "No location permissions available" );
      }
    }

  } // End of AndroidLocationListener


  /**
   * If this timer's task actually gets to run, then that means it took too long to get a fix on our location. So, we'll
   *  fallback to whatever we can get.
   */
  private class WaitingForLocationTimer
    extends Timer {

    /** Max time to wait for SOMEONE to cancel us when we're using only the network. */
    private static final int                                WAIT_FOR_FIRST_NETWORK_FIX_DELAY = 10000;

    /** Max time to wait for SOMEONE to cancel us when we have access to GPS. */
    private static final int                                WAIT_FOR_FIRST_GPS_FIX_DELAY = 30000;


    WaitingForLocationTimer(
      boolean waitingForGpsProvider
    ) {

      super( true );

      // If we're waiting on GPS, then we need a longer delay...
      int timeToWaitForFix;
      if ( waitingForGpsProvider ) {
        timeToWaitForFix = WAIT_FOR_FIRST_GPS_FIX_DELAY;
      }
      else {
        timeToWaitForFix = WAIT_FOR_FIRST_NETWORK_FIX_DELAY;
      }

      if ( BuildConfig.DEBUG ) {
        timeToWaitForFix *= 10;
      }

      start( timeToWaitForFix );
    }


    private void start(
      int timeToWaitForFix
    ) {

      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          fireOnUIThread();
        }
      };

      schedule( task, timeToWaitForFix );
    }


    private void fireOnUIThread() {

      new Handler( Looper.getMainLooper() ).post( DynamicLocationAcquirer.this::onFailedToGetAnyFixes );
    }

  } // End of WaitingForLocationTimer


}  // End of DynamicLocationProvider class
