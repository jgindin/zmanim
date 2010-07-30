/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.util;

import android.content.Context;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Abstracts dealing with the Android LocationManager.
 */
public class LocationHandler {


	private final long                                        minUpdateInterval;

	private final float                                       minUpdateDistance;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private final Geocoder                                    geocoder;

	private final LocationManager                             locationManager;

	private final AndroidLocationListener                     locationListener = new AndroidLocationListener();

	private final Formatter                                   rawLatitudeLongitudeFormatter = new Formatter();



	public LocationHandler(
		Context   context,
	  long      minUpdateInterval,
	  float     minUpdateDistance
	) {

		this.minUpdateInterval = minUpdateInterval;
		this.minUpdateDistance = minUpdateDistance;

		locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
		geocoder = new Geocoder( context );
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


	private void startListeningToLocations() {

		if ( !isLocationEnabled() ) {
			return;
		}

		// I'm going to listen to whatever providers might be available.
		// Loop over the array backwards, presumably because the more accurate providers are last?
		// This code was basically taken from: http://www.androidsnippets.org/snippets/21/
		boolean foundLocation = false;
		final List<String> providers = locationManager.getProviders( true );
		for ( int i = providers.size() - 1; i >= 0; i-- ) {

			final String providerName = providers.get( i );
			locationManager.requestLocationUpdates( providerName, minUpdateInterval, minUpdateDistance, locationListener );

			if ( !foundLocation ) {
				Location location = locationManager.getLastKnownLocation( providerName );
				if ( null != location ) {
					foundLocation = true;
					fireNewLocation( location );
				}
			}
		}

		if ( !foundLocation ) {
			fireOnError( Error.CANNOT_DETERMINE_LOCATION );
		}

	}


	private void stopListeningToLocation() {

		locationManager.removeUpdates( locationListener );
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
		final Location location
	) {


		AsyncTask<Location, Void, String> getGeoLocationTask = new AsyncTask<Location, Void, String>() {
			@Override
			protected String doInBackground( Location... locations ) {

				String locationString;

				try {
					final double latitude = location.getLatitude();
					final double longitude = location.getLongitude();
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
					locationString = "";
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
			listener.onLocationChanged( location, geoLocation );
		}
	}


	private synchronized void fireOnError( Error error ) {

		for ( Listener listener : listeners ) {
			listener.onError( error );
		}
	}


	private class AndroidLocationListener implements LocationListener {

		public void onLocationChanged( Location location ) {
			fireNewLocation( location );
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
	}


	public static enum Error {
		NO_LOCATION_MANAGER_SERVICE,
		NO_PROVIDERS,
		CANNOT_DETERMINE_LOCATION
	}

	public interface Listener {

		public void onLocationChanged( Location newLocation, String locationName );

		public void onError( Error error );
	}


}  // End of LocationHandler class
