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
import android.os.Bundle;
import com.gindin.util.LogUtils;
import com.gindin.util.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for our various location handlers.
 *<p>
 * Derived classes should wait until the first listener is added before trying to do anything with any location
 *  acquisition. And, once the final listener is removed, any necessary clean up needs to be performed.
 *<p>
 * Once a location has been determined, fireNewLocation() is to be called, which will take care of notifying
 *  all the registered listeners.
 */
abstract class LocationProvider {

	/**
	 * If we've already looked up the location information, then we'll store the Address that we got back in the
	 *  extras associated with the location.
	 */
	protected static final String                             ADDRESS = "address";

	protected final Activity                                  activity;

	/** Who's listening for our events? */
	private final List<Listener>                              listeners = new ArrayList<Listener>();

	/** Translates from latitude & longitude to a location name, and vice-versa. */
	protected final Geocoder                                  geocoder;

	/** Everyone wants to see what preferences have been set. */
	protected final SharedPreferences                         locationManagementPrefs;



	protected LocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		this.activity = activity;
		this.locationManagementPrefs = locationManagementPrefs;

		geocoder = new Geocoder( activity );
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
	 * Called when we're to pause in providing location information.
	 */
	public void onStop() {
		// Nothing by default
	}


	/**
	 * Called to resume providing location information.
	 */
	public void onStart() {
		// Nothing by default
	}


	/**
	 * Called when this location provider is going away.
	 */
	public void onDestroy() {

		listeners.clear();
	}


	/**
	 * Adds the specified listener to receive notifications of location changes.
	 *
	 * @param listener the new listener
	 */
	public final synchronized void addListener( Listener listener ) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}
		}
		else {
			listeners.add( listener );
			listenerAdded( listener, ( 1 == listeners.size() ) );
		}
	}


	/**
	 * Removes the specified listener so that it will no longer recieve location change notifications.
	 *
	 * @param listener the listener
	 */
	public final synchronized void removeListener( Listener listener ) {

		if ( listeners.contains( listener ) ) {
			listeners.remove( listener );
			listenerRemoved( listener, listeners.isEmpty() );
		}
		else if ( Version.isDevelopment() ) {
			throw new IllegalArgumentException( "Listener isn't listening!" );
		}
	}


	/**
	 * Called after a new listener has been added to our collection of listeners.
	 *
	 * @param listener the listener just added
	 * @param isFirst indicates if this is the first listener added
	 */
	protected abstract void listenerAdded(
		Listener  listener,
		boolean   isFirst
	);

	/**
	 * Called after a listener has been removed from our collection of listeners.
	 *
	 * @param listener the listener just removed
	 * @param wasLast indicates if this was the last listener, and no one is listening any more
	 */
	protected abstract void listenerRemoved(
		Listener  listener,
		boolean   wasLast
	);


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

		final Bundle extras = location.getExtras();
		if ( null != extras ) {
			final Address address = extras.getParcelable( ADDRESS );

			// Even if there are extras, that doesn't mean there's our address in there...
			if ( null != address ) {
				return addressToLocationName( location, address );
			}
		}


		AsyncTask<Location, Void, String> getGeoLocationTask = new AsyncTask<Location, Void, String>() {
			@Override
			protected String doInBackground( Location... locations ) {

				final double latitude = location.getLatitude();
				final double longitude = location.getLongitude();

				Address address;
				try {
					final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 1 );

					if ( ( null != addresses ) && !addresses.isEmpty() ) {
						address = addresses.get( 0 );
					}
					else {
						address = null;
					}
				}
				catch ( Exception e ) {
					address = null;
					LogUtils.logError( "LP::lookupLocationName", "", e );
				}

				return addressToLocationName( location, address );
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


	private String addressToLocationName( Location location, Address address ) {

		String locationString = null;

		if ( null != address ) {
			final String adminArea = address.getAdminArea();
			final String locality = address.getLocality();
			final String postalCode = address.getPostalCode();
			final int maxAddressLineIndex = address.getMaxAddressLineIndex();
			final String country = address.getCountryName();

			// There are cases where locality is null. No kiddin'...if you use the StaticLocationProvider, and choose
			//  "Brooklyn, New York", the locality comes back null. Go figure...
			//  (Well, that is the case, except that when the StaticLocationProvider goes to look up what the location is
			//    it now stores the returned Address as the Extras in the Location. And, for some reason, *that* Address
			//    comes back properly. Oh well...)
			if ( ( null != locality ) && ( null != adminArea ) ) {
				locationString = locality + ", " + adminArea;
			}
			else if ( ( null == locality ) && ( -1 != maxAddressLineIndex ) && ( null != postalCode ) ) {
				// Find the address line that has the location. In the case mentioned above, address line two is: Brooklyn, NY 11226
				for ( int index = 0; index < maxAddressLineIndex; index++ ) {
					String addrLine = address.getAddressLine( index );
					if ( null == addrLine ) continue;

					if ( addrLine.contains( postalCode ) ) {
						locationString = addrLine.substring( 0, addrLine.indexOf( postalCode ) );
						break;
					}
				}
			}
			else if ( ( null != locality ) && ( null != country ) ) {
				locationString = locality + ", " + country;
			}
			else if ( null != locality ) {
				locationString = locality;
			}
		}

		// Final fallback...if we got nothing then just use the lat & long...
		if ( null == locationString ) {
			final double latitude = location.getLatitude();
			final double longitude = location.getLongitude();

			//noinspection AutoBoxing
			locationString = String.format( "%.1f lat %.1f long", latitude, longitude );
		}

		return locationString;
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
