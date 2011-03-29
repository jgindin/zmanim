/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.*;
import android.os.Bundle;
import com.gindin.zmanim.android.R;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;

import java.util.*;

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

	private static final int                                  MIN_UPDATE_INTERVAL_MINUTES = 20 * 60 * 1000;


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

	/** Used so that we can decide if a new location is enough of a change to warrant firing an event. */
	private Location                                          ourLastFix = null;

	/** Do we prefer to receive GPS fixes over other providers? */
	private boolean                                           preferGPS;


	DynamicLocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		super( activity, locationManagementPrefs );

		locationManager = (LocationManager)activity.getSystemService( Context.LOCATION_SERVICE );

		lastFixedLocationFactory = new LastFixedLocationFactory( activity.getSharedPreferences( ZmanimPrefs.LAST_LOCATION.name(), Context.MODE_PRIVATE ) );
		preferGPS = locationManagementPrefs.getBoolean( LocationProviders.DynamicProviderOptions.PREFER_GPS.name(), false );
	}


	/**
	 * Intended to be called when someone has reason to believe that the set of available location providers
	 *  has changed significantly enough that we ought to requery them all.
	 */
	@Override
	public void onLocationProvidersChanged() {

		stopListeningToLocations();
		startListeningToLocations();
	}


	@Override
	public void onStop() {
		super.onStop();
		stopListeningToLocations();
	}


	@Override
	public void onStart() {
		super.onStart();

		// TODO: If we ever want to get periodic updates, then this won't work. Instead, we'll need to do something
		//        where we *always* call startListeningToLocations(), but without it firing any update just yet.
		//        As it is, for now, once we get a fix we never bother to update again during the lifetime of the
		//        app, so we're OK.
		if ( null == ourLastFix ) {
			startListeningToLocations();
		}
	}


	@Override
	public void onDestroy() {

		stopListeningToLocations();
		super.onDestroy();
	}


	@Override
	protected void listenerAdded(
		Listener  listener,
		boolean   isFirst
	) {

		if ( isFirst ) {
			startListeningToLocations();
		}
	}


	@Override
	protected void listenerRemoved(
		Listener  listener,
		boolean   wasLast
	) {

		if ( wasLast ) {
			stopListeningToLocations();
		}
	}


	@Override
	protected Location getLastKnownLocation() {

		Location gpsLocation = null;
		if ( isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			gpsLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
		}

		Location networkLocation = null;
		if ( isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
			networkLocation = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
		}

		// Which one to use?
		Location lastLocation;
		if ( ( null != gpsLocation ) && ( null != networkLocation ) ) {
			if ( gpsLocation.getTime() > networkLocation.getTime() ) {
				lastLocation = gpsLocation;
			}
			else {
				lastLocation = networkLocation;
			}
		}
		else if ( null != gpsLocation ) {
			lastLocation = gpsLocation;
		}
		else if ( null != networkLocation ) {
			lastLocation = networkLocation;
		}
		else {
			// The location of last resort...
			ourLastFix = lastFixedLocationFactory.get();
			lastLocation = ourLastFix;
		}

		return lastLocation;
	}


	@Override
	protected synchronized void fireNewLocation(
		final Location location
	) {

		ourLastFix = location;

		super.fireNewLocation( location );
		lastFixedLocationFactory.save( location );
	}


	@Override
	protected synchronized void fireError(
		LocationError error
	) {

		ourLastFix = getLastKnownLocation();

		super.fireError( error );
	}

	
	void onReceivedFix(
		final Location                location,
		final AndroidLocationListener source
	) {

		// We've got a fix we can use, even if it turns out to not be from our preferred method. Make sure
		//  we don't time out and claim to have failed...
		waitingForLocationTimer.cancel();


		// Now, there's a few cases here:
		//  1. We don't care how we get a fix: Tell all the location listeners to stop their first fix
		//  2. We prefer GPS, but this isn't: Let the location listeners continue trying to get their first fix
		//  3. We prefer GPS and this is it:  Tell all the location listeners to stop their first fix
		//  4. We prefer GPS, but it isn't turned on: Tell all the location listeners to stop their first fix
		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );

		if ( !preferGPS || LocationManager.GPS_PROVIDER.equals( source.providerName ) || !gpsProviderEnabled ) {

			for ( AndroidLocationListener listener : locationListeners ) {
				listener.cancel();
			}
		}

		// Did we ever get a fix before? If so, is this new fix a significant enough change to warrant firing an
		//  event?
		boolean needToFire = false;
		if ( null == ourLastFix ) {
			needToFire = true;
		}
		else {

			// First, check the time. If the time is more than our min update, then sure...go ahead and notify everyone.
			final long lastFixTime = ourLastFix.getTime();
			final long thisFixTime = location.getTime();

			if ( ( thisFixTime - lastFixTime ) > MIN_UPDATE_INTERVAL_MINUTES ) {
				needToFire = true;
			}
			else {
				// Maybe our new fix is GPS, which is more accurate?
				boolean lastFixWasGps =  LocationManager.GPS_PROVIDER.equals( ourLastFix.getProvider() );
				boolean thisFixIsGps = LocationManager.GPS_PROVIDER.equals( location.getProvider() );

				if ( thisFixIsGps && !lastFixWasGps ) {
					needToFire = true;
				}
			}
		}

		if ( needToFire ) {
			fireNewLocation( location );
		}

	}


	void onFailedToGetAnyFixes() {

		cancelWaitingForLocationTimer();

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancel();
		}

		// Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
		fireError( new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION ) );
	}


	private void startListeningToLocations() {

		if ( !isLocationEnabled() ) {
			return;
		}

		// Cancel any outstanding timer *first* so that if one were to be running right now, it couldn't
		//  decide to fire...
		cancelWaitingForLocationTimer();

		fireLocationProgress( activity.getString( R.string.waiting_for_location_update ) );

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
			fireError( new LocationError( LocationError.Code.NO_LOCATION_MANAGER_SERVICE ) );
			return false;
		}

		final boolean networkProviderEnabled = isProviderEnabled( LocationManager.NETWORK_PROVIDER );
		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );

		if ( !networkProviderEnabled && !gpsProviderEnabled ) {
			fireError( new LocationError( LocationError.Code.NO_PROVIDERS ) );
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

			// This first update we want RIGHT NOW!!!!
			locationManager.requestLocationUpdates( providerName, 0, 0, this );
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

					// Looks like if we don't invoke the callback on the UI thread, then things don't work
					//  so well...
					activity.runOnUiThread(
						new Runnable() {
							public void run() {
								onFailedToGetAnyFixes();
							}
						}
					);
				}
			};

			schedule( task, timeToWaitForFix );
		}

	} // End of WaitingForLocationTimer


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


		Location get() {

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


		void save(
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
