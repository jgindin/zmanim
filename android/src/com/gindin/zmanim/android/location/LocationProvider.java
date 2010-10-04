/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import com.gindin.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Base class for our various location handlers.
 */
abstract class LocationProvider {

	protected final Activity                                  activity;

	/** Who's listening for our events? */
	protected final List<Listener>                            listeners = new ArrayList<Listener>();

	/** Translates from latitude & longitude to a location name, and vice-versa. */
	protected final Geocoder                                  geocoder;

	/** Nicely formats raw latitude & longitude when we can't resolve it to a location name. */
	protected final Formatter                                 rawLatitudeLongitudeFormatter = new Formatter();


	protected LocationProvider(
		Activity activity
	) {
		geocoder = new Geocoder( activity );
		this.activity = activity;
	}


	/**
	 * Called by the ZmanimLocationManager when it has reason to believe that the set of available location providers
	 *  has changed significantly enough that we ought to requery them all.
	 */
	public abstract void onLocationProvidersChanged();


	/**
	 * Derived class must implement to return the last location it knows about.
	 *
	 * @return The last location this handler knew about, or null if it never knew about anywhere.
	 */
	protected abstract Location getLastKnownLocation();


	/**
	 * Called when this location provider is going away.
	 */
	protected void onDestroy() {

		listeners.clear();
	}


	public synchronized boolean addListener( Listener listener ) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}

			return false;
		}
		else {
			listeners.add( listener );
			return true;
		}
	}


	public synchronized boolean removeListener( Listener listener ) {

		if ( listeners.contains( listener ) ) {
			listeners.remove( listener );
			return true;
		}
		else if ( Version.isDevelopment() ) {
			throw new IllegalArgumentException( "Listener isn't listening!" );
		}

		return false;
	}


	protected synchronized void fireNewLocation(
		final Location location
	) {

		String geoLocation = lookupLocationName( location );

		for ( Listener listener : listeners ) {
			listener.onLocationChanged( location, geoLocation );
		}
	}


	protected synchronized void fireError(
		LocationError error
	) {

		Location oldLocation = getLastKnownLocation();
		String oldLocationName = lookupLocationName( oldLocation );

		for ( Listener listener : listeners ) {
			listener.onError( error, oldLocation, oldLocationName );
		}
	}



	protected String lookupLocationName(
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


	public interface Listener {

		public void onLocationChanged(
			Location  newLocation,
			String    locationName
		);

		public void onError(
			LocationError error,
			Location      oldLocation,
			String        locationName
		);
	}
}  // End of LocationProvider class
