/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util;

import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.util.*;

/**
 * Abstracts dealing with the Android LocationManager.
 */
public class LocationHandler {

	private final Activity                                    activity;

	private final long                                        minUpdateInterval;

	private final float                                       minUpdateDistance;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private final Geocoder                                    geocoder;

	private final LocationManager                             locationManager;

	private final List<AndroidLocationListener>               locationListeners = new ArrayList<AndroidLocationListener>();

	private final Formatter                                   rawLatitudeLongitudeFormatter = new Formatter();

	private CancelFirstFixesTimer                             cancelFirstFixesTimer = null;



	public LocationHandler(
		Activity  activity,
	  long      minUpdateInterval,
	  float     minUpdateDistance
	) {
		this.activity = activity;

		this.minUpdateInterval = minUpdateInterval;
		this.minUpdateDistance = minUpdateDistance;

		locationManager = (LocationManager)activity.getSystemService( Context.LOCATION_SERVICE );
		geocoder = new Geocoder( activity );
	}




	public synchronized void addListener( Listener l ) {

		if ( listeners.contains( l ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}
		}
		else {
			listeners.add( l );
		}

		if ( 1 == listeners.size() ) {
			startListeningToLocations();
		}
	}


	public synchronized void removeListener( Listener l ) {

		if ( !listeners.contains( l ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener isn't listening!" );
			}
		}
		else {
			listeners.remove( l );
		}

		if ( listeners.isEmpty() ) {
			stopListeningToLocation();
		}
	}


	public void reset() {
		stopListeningToLocation();
		startListeningToLocations();
	}


	void onReceivedFix() {

		cancelFirstFixesTimer.cancel();
		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancelFirstFix();
		}
		
	}

	void onFailedToGetAnyFixes() {

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancelFirstFix();
		}

		// Well, we're being run, so let someone know that they'll have to use a fall-back mechanism.
		fireOnError( Error.CANNOT_DETERMINE_LOCATION );
	}


	private void startListeningToLocations() {

		if ( !isLocationEnabled() ) {
			return;
		}

		if ( null != cancelFirstFixesTimer ) {
			cancelFirstFixesTimer.cancel();
		}
		cancelFirstFixesTimer = new CancelFirstFixesTimer();

		// I'm going to listen to whatever providers might be available.
		// Loop over the array backwards, presumably because the more accurate providers are last?
		// This code was basically taken from: http://www.androidsnippets.org/snippets/21/
		final List<String> providers = locationManager.getProviders( true );
		for ( int i = providers.size() - 1; i >= 0; i-- ) {

			final String providerName = providers.get( i );
			AndroidLocationListener listener = new AndroidLocationListener( providerName );
			locationListeners.add( listener );
		}
	}


	private void stopListeningToLocation() {

		cancelFirstFixesTimer.cancel();

		for ( AndroidLocationListener listener : locationListeners ) {
			listener.cancel();
		}

		locationListeners.clear();
	}


	private boolean isLocationEnabled() {

		if ( null == locationManager ) {
			fireOnError( Error.NO_LOCATION_MANAGER_SERVICE );
			return false;
		}

		final boolean networkProviderEnabled = locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
		final boolean gpsProviderEnabled = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );

		if ( !networkProviderEnabled && !gpsProviderEnabled ) {
			fireOnError( Error.NO_PROVIDERS );
			return false;
		}

		return true;
	}


	private synchronized void fireNewLocation(
		final Location  location,
		final String    providerName
	) {


		AsyncTask<Location, Void, String> getGeoLocationTask = new AsyncTask<Location, Void, String>() {
			@Override
			protected String doInBackground( Location... locations ) {

				String locationString;
				final double latitude = location.getLatitude();
				final double longitude = location.getLongitude();

				try {
					final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 1 );

					if ( ( null != addresses ) && !addresses.isEmpty() ) {
						final Address address = addresses.get( 0 );
						locationString = address.getLocality() + ", " + address.getAdminArea();
					}
					else {
						//noinspection AutoBoxing
						locationString = rawLatitudeLongitudeFormatter.format( "%.1f lat %.1f long", latitude, longitude ).toString();
					}
				}
				catch ( IOException e ) {
					//noinspection AutoBoxing
					locationString = rawLatitudeLongitudeFormatter.format( "%.1f lat %.1f long", latitude, longitude ).toString();
				}

				return locationString;
			}
		};

		getGeoLocationTask.execute( location );
		String geoLocation;
		try {
			geoLocation = getGeoLocationTask.get();
		}
		catch ( Exception e ) {
			geoLocation = null;
		}


		for ( Listener listener : listeners ) {
			listener.onLocationChanged( location, geoLocation, providerName );
		}
	}


	private synchronized void fireOnError( Error error ) {

		for ( Listener listener : listeners ) {
			listener.onError( error );
		}
	}


	/**
	 * Listens to location changes for a given provider.
	 */
	private class AndroidLocationListener implements LocationListener {

		public final String                                     providerName;

		public boolean                                          firstFix = true;


		public AndroidLocationListener(
			String providerName
		) {

			this.providerName = providerName;
			locationManager.requestLocationUpdates( providerName, 0, 0, this );
		}


		public void onLocationChanged( Location location ) {

			onReceivedFix();
			fireNewLocation( location, providerName );
		}


		public void onStatusChanged( String provider, int status, Bundle extras ) {
			// Not used
		}


		public void onProviderEnabled( String provider ) {
			// Not used
		}


		public void onProviderDisabled( String provider ) {
			// Not used
		}


		public void cancel() {
			locationManager.removeUpdates( this );
		}

		public void cancelFirstFix() {

			if ( firstFix ) {
				firstFix = false;
				locationManager.requestLocationUpdates( providerName, minUpdateInterval, minUpdateDistance, this );
			}
		}

	} // End of AndroidLocationListener



	/**
	 * If this timer's task actually gets to run, then that means it took too long to get a fix on our location. So, we'll
	 *  fallback to whatever we can get.
	 */
	private class CancelFirstFixesTimer
		extends Timer {

		/** Max time to wait for SOMEONE to cancel us. */
		private static final int                                  WAIT_FOR_FIRST_FIX_DELAY = 20000;

		CancelFirstFixesTimer() {
			super( true );
			start();
		}


		private void start() {

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

			schedule( task, WAIT_FOR_FIRST_FIX_DELAY );
		}

	} // End of CancelFirstFixesTimer




	public static enum Error {
		NO_LOCATION_MANAGER_SERVICE,
		NO_PROVIDERS,
		CANNOT_DETERMINE_LOCATION
	}

	public interface Listener {

		public void onLocationChanged(
			Location  newLocation,
			String    locationName,
			String    providerName
		);

		public void onError( Error error );
	}


}  // End of LocationHandler class
