/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.*;
import android.os.Bundle;

import java.io.IOException;
import java.util.*;

/**
 * Abstracts dealing with the Android LocationManager.
 */
class DynamicLocationHandler 
	extends LocationHandler {

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
	 *  providers give us a fix, we cancel this timer. If it expires, then we'll fire an error.
	 */
	private CancelFirstFixesTimer                             cancelFirstFixesTimer = null;

	/** Used so that we can decide if a new location is enough of a change to warrant firing an event. */
	private Location                                          ourLastFix = null;



	public DynamicLocationHandler(
		Activity activity
	) {

		super( activity );

		locationManager = (LocationManager)activity.getSystemService( Context.LOCATION_SERVICE );

		lastFixedLocationFactory = new LastFixedLocationFactory( activity.getSharedPreferences( "ZmanimLocation", Context.MODE_PRIVATE ) );
	}


	@Override
	public synchronized boolean addListener( Listener listener ) {

		if ( super.addListener( listener ) ) {
			if ( 1 == listeners.size() ) {
				startListeningToLocations();
			}

			return true;
		}

		return false;
	}


	@Override
	public synchronized boolean removeListener( Listener listener ) {
		
		if ( super.removeListener( listener ) ) {
			if ( listeners.isEmpty() ) {
				stopListeningToLocations();
			}

			return true;
		}

		return false;
	}


	/**
	 * Intended to be called when someone has reason to believe that the set of available location providers
	 *  has changed significantly enough that we ought to requery them all.
	 */
	public void onLocationProvidersChanged() {

		stopListeningToLocations();
		startListeningToLocations();
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

		super.fireNewLocation( location );
		lastFixedLocationFactory.save( location );
	}


	@Override
	protected synchronized void fireError( Error error ) {

		Location oldLocation = getLastKnownLocation();

		super.fireError( error );
		lastFixedLocationFactory.save( oldLocation );
	}

	
	void onReceivedFix(
		final Location location
	) {

		cancelFirstFixesTimer.cancel();
		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancelFirstFix();
		}

		// Did we ever get a fix before? If so, is this new fix a significant enough change to warrant firing an
		//  event?
		boolean needToFire = false;
		if ( null == ourLastFix ) {
			needToFire = true;
		}
		else {

			// First, check the time. If the time is more than 5 minutes,
			final long lastFixTime = ourLastFix.getTime();
			final long thisFixTime = location.getTime();

			if ( thisFixTime - lastFixTime > MIN_UPDATE_INTERVAL_MINUTES ) {
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

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancelFirstFix();
		}

		// Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
		fireError( Error.CANNOT_DETERMINE_LOCATION );
	}


	private void startListeningToLocations() {

		if ( !isLocationEnabled() ) {
			return;
		}

		if ( null != cancelFirstFixesTimer ) {
			cancelFirstFixesTimer.cancel();
		}

		// Now, kick off a timer such that if the timer expires, we're going to assume we can't get any
		//  location fix... Start this BEFORE any listeners get added in case one of the listeners returns
		//  a fix right away...
		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );
		cancelFirstFixesTimer = new CancelFirstFixesTimer( gpsProviderEnabled );

		// I'm going to listen to whatever providers might be available.
		final List<String> providers = locationManager.getProviders( true );
		for ( String provider : providers ) {
			AndroidLocationListener listener = new AndroidLocationListener( provider );
			locationListeners.add( listener );
		}
	}


	private void stopListeningToLocations() {

		if ( null != cancelFirstFixesTimer ) {
			cancelFirstFixesTimer.cancel();
		}

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancel();
		}

		locationListeners.clear();
	}


	private boolean isLocationEnabled() {

		if ( null == locationManager ) {
			fireError( Error.NO_LOCATION_MANAGER_SERVICE );
			return false;
		}

		final boolean networkProviderEnabled = isProviderEnabled( LocationManager.NETWORK_PROVIDER );
		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );

		if ( !networkProviderEnabled && !gpsProviderEnabled ) {
			fireError( Error.NO_PROVIDERS );
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

		/** Have we recieved our first location fix yet? */
		private boolean                                         firstFix = true;


		AndroidLocationListener(
			String  providerName
		) {

			this.providerName = providerName;

			// This first update we want RIGHT NOW!!!!
			locationManager.requestLocationUpdates( providerName, 0, 0, this );
		}


		public void onLocationChanged( Location location ) {
			onReceivedFix( location );
		}


		public void cancel() {
			locationManager.removeUpdates( this );
		}


		void cancelFirstFix() {

			if ( firstFix ) {
				firstFix = false;

				// Doesn't seem to be much use in checking too often. And, we're interested in time-based
				//  updates, not distance based. Though, I admit, distance makes more sense, this is easier
				//  to test...
				cancel();
				locationManager.requestLocationUpdates( providerName, MIN_UPDATE_INTERVAL_MINUTES, 0.0f, this );
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

	} // End of AndroidLocationListener


	/**
	 * If this timer's task actually gets to run, then that means it took too long to get a fix on our location. So, we'll
	 *  fallback to whatever we can get.
	 */
	private class CancelFirstFixesTimer
		extends Timer {

		/** Max time to wait for SOMEONE to cancel us. */
		private static final int                                WAIT_FOR_FIRST_NETWORK_FIX_DELAY = 3000;
		private static final int                                WAIT_FOR_FIRST_GPS_FIX_DELAY = 20000;


		CancelFirstFixesTimer(
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

	} // End of CancelFirstFixesTimer


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


}  // End of DynamicLocationHandler class
