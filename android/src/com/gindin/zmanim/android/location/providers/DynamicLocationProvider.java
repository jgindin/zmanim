/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.providers;

import com.gindin.util.Pair;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RawLocationAcquiredEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
class DynamicLocationProvider
	extends LocationProvider {


	private final LocationManager                             locationManager;

	/** We have one listener for every location provider. */
	private final List<AndroidLocationListener>               locationListeners = new ArrayList<AndroidLocationListener>();

	/**
	 * Used to store & retrieve the last fix we received, but in a way that will survive restarts of the app.
	 *  Ideally we'd store the Location object in a Bundle, but it doesn't seem possible to get a Bundle
	 *  except when the app is being shutdown or restarted.
	 */
	private final LastFixedLocationFactory                    lastFixedLocationFactory;

	/**
	 * We need to wait for "some time" before giving up on all the location providers. So, when any of the location
	 *  providers give us a fix, we cancel this timer. If it expires, then it will cause us to fire an error.
	 */
	private WaitingForLocationTimer                           waitingForLocationTimer = null;


	/** Do we prefer to receive GPS fixes over other providers? */
	private boolean                                           preferGPS;


	DynamicLocationProvider(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
		LocationManager   locationManager
	) {

		super( eventBus, locationManagementPrefs );
		this.locationManager = locationManager;


		// TODO: Upgrade note:
		// TODO:  Move prefs from ZmanimPrefs.LAST_LOCATION.name() to locationManagementPrefs
		// TODO:  Even better, move to an alternate storage mechanism?
		lastFixedLocationFactory = new LastFixedLocationFactory( locationManagementPrefs );
		preferGPS = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_GPS.name(), false );
	}


	@SuppressWarnings( "FeatureEnvy" )
	@Override
	public void acquire() {

		boolean preferCachedLocation = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_CACHED_LOCATION.name(), true );
		if ( preferCachedLocation ) {
			ZmanimLocation lastKnownLocation = getLastKnownLocation();
			if ( lastKnownLocation != null ) {
				eventBus.fire( new RawLocationAcquiredEvent( lastKnownLocation ) );
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


	/**
	 * Check the various location providers for their last known locations, and try to use the most recent. May
	 *  return null if none of the providers are able to give us anything.
	 */
	@SuppressWarnings( { "AutoBoxing", "AutoUnboxing" } )
	private ZmanimLocation getLastKnownLocation() {

		List<Pair<Location,Long>> lastKnownLocationsWithTime = new ArrayList<Pair<Location,Long>>();

		for ( String provider : locationManager.getAllProviders() ) {
			Location lastKnownLocation = locationManager.getLastKnownLocation( provider );
			if ( null != lastKnownLocation ) {
				lastKnownLocationsWithTime.add( new Pair<Location,Long>( lastKnownLocation, lastKnownLocation.getTime() ) );
			}
		}

		Pair<Location,Long> bestLocation = new Pair<Location, Long>( null, Long.MIN_VALUE );
		for ( Pair<Location, Long> pair : lastKnownLocationsWithTime ) {
			if ( pair.second > bestLocation.second ) {
				bestLocation = pair;
			}
		}

		ZmanimLocation location;
		if ( bestLocation.first != null ) {

			location = new ZmanimLocation.Builder()
				.from( bestLocation.first )
				.inTimeZone( getTimeZoneFromPrefs() )
				.build();
		}
		else {

			location = new ZmanimLocation.Builder()
				.inTimeZone( getTimeZoneFromPrefs() )
				.build();
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
		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );

		if ( !preferGPS || LocationManager.GPS_PROVIDER.equals( source.providerName ) || !gpsProviderEnabled ) {
			stopListeningToLocations();
		}

		lastFixedLocationFactory.save( newLocation );

		ZmanimLocation location = new ZmanimLocation.Builder()
			.from( newLocation )
			.inTimeZone( getTimeZoneFromPrefs() )
			.build();
		eventBus.fire( new RawLocationAcquiredEvent( location ) );
	}


	private void onFailedToGetAnyFixes() {

		cancelWaitingForLocationTimer();

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancel();
		}

		// Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
		eventBus.fire( new RawLocationAcquiredEvent( getLastKnownLocation(),
			new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION )  ) );
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
		final List<String> providers = locationManager.getProviders( true );
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


	private boolean isLocationEnabled() {

		if ( null == locationManager ) {
			eventBus.fire( new RawLocationAcquiredEvent( getLastKnownLocation(),
				new LocationError( LocationError.Code.NO_LOCATION_MANAGER_SERVICE ) ) );
			return false;
		}

		List<String> providers = locationManager.getAllProviders();
		boolean foundAProvider = false;
		for ( String provider : providers ) {
			if ( isProviderEnabled( provider ) ) {
				foundAProvider = true;
				break;
			}
		}

		if ( !foundAProvider ) {
			eventBus.fire( new RawLocationAcquiredEvent( getLastKnownLocation(),
				new LocationError( LocationError.Code.NO_PROVIDERS ) ) );
			return false;
		}

		return true;
	}


	private boolean isProviderEnabled(
		String provider
	) {
		return locationManager.isProviderEnabled( provider );
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
		public final String                                     providerName;


		AndroidLocationListener(
			String  providerName
		) {

			this.providerName = providerName;

			locationManager.requestSingleUpdate( providerName, this, null );

			// This first update we want RIGHT NOW!!!!
//			locationManager.requestLocationUpdates( providerName, 0, 0, this );
		}


		public void onLocationChanged(
			Location location
		) {

			cancel();
			onReceivedFix( location, this );
		}


		public void cancel() {
			locationManager.removeUpdates( this );
		}


		public void onStatusChanged( String provider, int status, Bundle extras ) {

			// Inherited from LocationListener...Not used
//			StringBuffer message = new StringBuffer( "StatusChanged for " + provider );
//			for ( String s : extras.keySet() ) {
//				message.append( s ).append( " = " ).append( extras.get( s ) );
//			}
//
//			LogUtils.logEvent( "onStatusChanged", message.toString() );
		}


		public void onProviderEnabled( String provider ) {
			// Inherited from LocationListener...Not used
		}


		public void onProviderDisabled( String provider ) {
			// Inherited from LocationListener...Not used
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

			start( timeToWaitForFix );
		}


		private void start(
			int timeToWaitForFix
		) {

			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					onFailedToGetAnyFixes();
				}
			};

			schedule( task, timeToWaitForFix );
		}

	} // End of WaitingForLocationTimer


	// TODO: Make this an android.location.LocationProvider
	private static class LastFixedLocationFactory {

		private static final String                               HAS_ACCURACY = "lastFixHasAccuracy";
		private static final String                               ACCURACY = "lastFixAccuracy";
		private static final String                               HAS_ALTITUDE = "lastfixHasAltitude";
		private static final String                               ALTITUDE = "lastfixAltitude";
		private static final String                               HAS_BEARING = "lastfixHasBearing";
		private static final String                               BEARING = "lastfixBearing";
		private static final String                               LATITUDE = "lastfixLatitude";
		private static final String                               LONGITUDE = "lastfixLongitude";
		private static final String                               PROVIDER_NAME = "lastFixProviderName";
		private static final String                               HAS_SPEED = "lastfixHasSpeed";
		private static final String                               SPEED = "lastfixSpeed";
		private static final String                               TIME = "lastfixTime";


		/** Stores data that we need between invocations. */
		private final SharedPreferences                           preferences;


		LastFixedLocationFactory( SharedPreferences preferences ) {
			this.preferences = preferences;
		}


		Location load() {

			String lastFixProviderName = preferences.getString( PROVIDER_NAME, null );
			if ( null == lastFixProviderName ) {
				return null;
			}

			Location lastLocation = new Location( lastFixProviderName );
			if ( preferences.getBoolean( HAS_ACCURACY, false ) ) {
				lastLocation.setAccuracy( preferences.getFloat( ACCURACY, 0.0f ) );
			}

			if ( preferences.getBoolean( HAS_ALTITUDE, false ) ) {
				lastLocation.setAltitude( Double.parseDouble( preferences.getString( ALTITUDE, "0" ) ) );
			}

			if ( preferences.getBoolean( HAS_BEARING, false ) ) {
				lastLocation.setBearing( preferences.getFloat( BEARING, 0.0f ) );
			}

			lastLocation.setLatitude( Double.parseDouble( preferences.getString( LATITUDE, "0" ) ) );
			lastLocation.setLongitude( Double.parseDouble( preferences.getString( LONGITUDE, "0" ) ) );

			if ( preferences.getBoolean( HAS_SPEED, false ) ) {
				lastLocation.setSpeed( preferences.getFloat( SPEED, 0.0f ) );
			}

			lastLocation.setTime( preferences.getLong( TIME, 0 ) );

			return lastLocation;
		}


		private void save(
			Location location
		) {

			if ( null == location ) {
				return;
			}

			final SharedPreferences.Editor editor = preferences.edit();

			editor.putBoolean( HAS_ACCURACY, location.hasAccuracy() );
			editor.putFloat( ACCURACY, location.getAccuracy() );

			editor.putBoolean( HAS_ALTITUDE, location.hasAltitude() );
			editor.putString( ALTITUDE, Double.toString( location.getAltitude() ) );

			editor.putBoolean( HAS_BEARING, location.hasBearing() );
			editor.putFloat( BEARING, location.getBearing() );

			editor.putString( LATITUDE, Double.toString( location.getLatitude() ) );
			editor.putString( LONGITUDE, Double.toString( location.getLongitude() ) );

			editor.putString( PROVIDER_NAME, location.getProvider() );

			editor.putBoolean( HAS_SPEED, location.hasSpeed() );
			editor.putFloat( SPEED, location.getSpeed() );

			editor.putLong( TIME, location.getTime() );

			editor.commit();
		}

	}


}  // End of DynamicLocationProvider class
