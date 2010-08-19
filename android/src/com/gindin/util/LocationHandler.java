/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.util.*;

/**
 * Abstracts dealing with the Android LocationManager.
 */
public class LocationHandler {

	private static final int                                  MIN_UPDATE_INTERVAL_MINUTES = 20 * 60 * 1000;


	private final Activity                                    activity;

	/** Who's listening for our events? */
	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private final Geocoder                                    geocoder;

	private final LocationManager                             locationManager;

	/** We have one listener for every location provider. */
	private final List<AndroidLocationListener>               locationListeners = new ArrayList<AndroidLocationListener>();

	private final Formatter                                   rawLatitudeLongitudeFormatter = new Formatter();

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



	public LocationHandler(
		Activity activity
	) {
		this.activity = activity;

		locationManager = (LocationManager)activity.getSystemService( Context.LOCATION_SERVICE );
		geocoder = new Geocoder( activity );

		lastFixedLocationFactory = new LastFixedLocationFactory( activity.getSharedPreferences( "ZmanimLocation", Context.MODE_PRIVATE ) );
		ourLastFix = lastFixedLocationFactory.get();
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

		final boolean gpsProviderEnabled = isProviderEnabled( LocationManager.GPS_PROVIDER );
		cancelFirstFixesTimer = new CancelFirstFixesTimer( gpsProviderEnabled );

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


	private Location getLastKnownLocation() {

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
			lastLocation = ourLastFix;
		}

		return lastLocation;
	}


	private synchronized void fireNewLocation(
		final Location location
	) {

		String geoLocation = lookupLocationName( location );

		for ( Listener listener : listeners ) {
			listener.onLocationChanged( location, geoLocation );
		}

		lastFixedLocationFactory.save( location );
	}


	private synchronized void fireError( Error error ) {

		Location oldLocation = getLastKnownLocation();
		String oldLocationName = lookupLocationName( oldLocation );

		for ( Listener listener : listeners ) {
			listener.onError( error, oldLocation, oldLocationName );
		}

		lastFixedLocationFactory.save( oldLocation );
	}


	private String lookupLocationName(
		final Location location
	) {

		if ( null == location ) {
			return "";
		}

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
		return geoLocation;
	}



	/**
	 * Listens to location changes for a given provider.
	 */
	private class AndroidLocationListener implements LocationListener {

		public final String                                     providerName;

		private boolean                                         firstFix = true;


		protected AndroidLocationListener(
			String  providerName
		) {

			this.providerName = providerName;

			// This first update we want RIGHT NOW!!!!
			locationManager.requestLocationUpdates( providerName, 0, 0, this );
		}


		public void onLocationChanged( Location location ) {

			onReceivedFix( location );
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

				// Doesn't seem to be much use in checking too often. And, we're interested in time-based
				//  updates, not distance based. Though, I admit, distance makes more sense, this is easier
				//  to test...
				locationManager.requestLocationUpdates( providerName, MIN_UPDATE_INTERVAL_MINUTES, 0.0f, this );
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
		private static final int                                WAIT_FOR_FIRST_NETWORK_FIX_DELAY = 3000;
		private static final int                                WAIT_FOR_FIRST_GPS_FIX_DELAY = 20000;


		CancelFirstFixesTimer(
			boolean waitingForGpsProvider
		) {

			super( true );

			// If we're waiting on GPS, then we need a longer delay...
			int maxDelay;
			if ( waitingForGpsProvider ) {
				maxDelay = WAIT_FOR_FIRST_GPS_FIX_DELAY;
			}
			else {
				maxDelay = WAIT_FOR_FIRST_NETWORK_FIX_DELAY;
			}

			start( maxDelay );
		}


		private void start(
			int maxDelay
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

			schedule( task, maxDelay );
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


	public static enum Error {
		NO_LOCATION_MANAGER_SERVICE,
		NO_PROVIDERS,
		CANNOT_DETERMINE_LOCATION
	}

	public interface Listener {

		public void onLocationChanged(
			Location  newLocation,
			String    locationName
		);

		public void onError(
			Error     error,
			Location  oldLocation,
			String    locationName
		);
	}


}  // End of LocationHandler class
