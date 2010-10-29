/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.LocationProviders;

import java.io.IOException;
import java.util.ArrayList;
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

	/** Everyone wants to see what preferences have been set. */
	protected final SharedPreferences                         locationManagementPrefs;

	/** Listen for the user changing where they want to be. */
	private final SharedPreferences.OnSharedPreferenceChangeListener locationManagementPrefsListener;


	protected LocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		this.activity = activity;
		this.locationManagementPrefs = locationManagementPrefs;

		geocoder = new Geocoder( activity );
		final SharedPreferences.OnSharedPreferenceChangeListener changeListener = createLocationManagementPrefsListener();
		locationManagementPrefsListener = createWrappingLocationProviderChangeListener( changeListener );
	}


	/**
	 * Common mechanism to get setup for listening to location management preference changes.
	 *
	 * @return the change listener.
	 */
	protected abstract SharedPreferences.OnSharedPreferenceChangeListener createLocationManagementPrefsListener();


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
		if ( null != locationManagementPrefsListener ) {
			locationManagementPrefs.unregisterOnSharedPreferenceChangeListener( locationManagementPrefsListener );
		}
	}


	/**
	 * Adds the specified listener to receive notifications of location changes.
	 *
	 * @param listener the new listener
	 *
	 * @return true if the listener was actually added, false if not (e.g., it's already a listener)
	 */
	public synchronized boolean addListener( Listener listener ) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}

			return false;
		}
		else {
			listeners.add( listener );
			locationManagementPrefs.registerOnSharedPreferenceChangeListener( locationManagementPrefsListener );
			return true;
		}
	}


	/**
	 * Removes the specified listener so that it will no longer recieve location change notifications.
	 *
	 * @param listener the listener
	 *
	 * @return true if the listener was actually removed, false if not (e.g., it wasn't a listener)
	 */
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


	protected synchronized void fireLocationProgress(
		String  message
	) {

		for ( Listener listener : listeners ) {
			listener.onProgress( message );
		}
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
						locationString = String.format( "%.1f lat %.1f long", latitude, longitude );
					}
				}
				catch ( IOException e ) {
					//noinspection AutoBoxing
					locationString = String.format( "%.1f lat %.1f long", latitude, longitude );
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


	private SharedPreferences.OnSharedPreferenceChangeListener createWrappingLocationProviderChangeListener(
		final SharedPreferences.OnSharedPreferenceChangeListener changeListener
	) {

		return new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences,
				String            prefKey
			) {

				changeListener.onSharedPreferenceChanged( sharedPreferences, prefKey );

				// Maybe it was the time zone that changed? If so, that's like a location change...
				if ( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name().equals( prefKey ) ||
						 LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name().equals( prefKey ) ||
						 LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name().equals( prefKey ) ) {

					fireNewLocation( getLastKnownLocation() );
				}
			}
		};
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

		void onProgress(
			String message
		);
	}
}  // End of LocationProvider class
