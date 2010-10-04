/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import com.gindin.util.LogUtils;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.EditLocationProviderPreferences;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import net.sourceforge.zmanim.util.GeoLocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 *
 * TODO: Allow user to choose how to select location:
 *        1. Auto
 *        2. Manually enter city, state (with optional street, etc...)
 *        3. Google Map (see http://code.google.com/android/add-ons/google-apis/mapkey.html, http://code.google.com/android/add-ons/google-apis/reference/index.html)
 */
public class ZmanimLocationManager {


	/** Need to know the activity so we can do things like build dialogs and spawn other activities. */
	private final Activity                                    activity;

	/** Who's really providing the location for us? */
	private LocationProvider                                  locationProvider;

	/** These are the folks who need to know when the location changes. */
	private final List<Listener>                              listeners = new ArrayList<Listener>();

	/** We have to listen to prefs changes to know when the user has decided to change how we're supposed to determine the location. */
	private final SharedPreferences.OnSharedPreferenceChangeListener locationProviderChangeListener;

	/** We need to listen to the provider for changes, which we then forward on to our listeners. */
	private final LocationProvider.Listener                   locationListener;

	/** Uh, where's the last place we knew we were? */
	private ZmanimLocation                                    lastKnownLocation = null;



	public ZmanimLocationManager(
		Activity activity
	) {

		this.activity = activity;

		locationListener = new DynamicLocationProvider.Listener() {
			public void onLocationChanged(
				Location newLocation,
				String locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( newLocation, locationName );
				fireLocationChanged( location );
			}


			public void onError(
				LocationError error,
				Location      oldLocation,
				String        locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( oldLocation, locationName );
				fireError( error, location );
			}
		};

		SharedPreferences prefs = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name, Context.MODE_PRIVATE );
		locationProviderChangeListener = createLocationProviderChangeListener();
		prefs.registerOnSharedPreferenceChangeListener( locationProviderChangeListener );

		// Force a provider to be created...
		startInitialProvider( prefs );
	}


	public void onDestroy() {

		SharedPreferences prefs = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name, Context.MODE_PRIVATE );
		prefs.unregisterOnSharedPreferenceChangeListener( locationProviderChangeListener );

		if ( !listeners.isEmpty() ) {

			final List<Listener> copy = listeners;
			for ( Listener listener : copy ) {
				removeListener( listener );
			}

			if ( !listeners.isEmpty() ) {

				// If we get here, things are in bad shape, but we can still remove our listener from
				//  the location handler...
				locationProvider.removeListener( locationListener );

				if ( Version.isDevelopment() ) {
					throw new IllegalStateException( "Should have removed all listeners. Still have " + listeners.size() );
				}
			}
		}

		locationProvider.onDestroy();
	}


	public void setLocationField(
		View locationField
	) {

		// TODO: Do we need to hold onto the field so that if it gets changed, we can remove listeners?

		locationField.setOnClickListener(
			new View.OnClickListener() {
				public void onClick( View view ) {
					changeLocation();
				}
			}
		);

		locationField.setOnLongClickListener(
			new View.OnLongClickListener() {
				public boolean onLongClick( View view ) {
					showLocationDetails();
					return true;
				}
			}
		);

	}



	public void addListener(
		Listener  listener
	) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}
		}
		else {
			listeners.add( listener );

			if ( 1 == listeners.size() ) {
				locationProvider.addListener( locationListener );
			}
		}
	}


	public void removeListener(
		Listener listener
	) {

		if ( listeners.contains( listener ) ) {
			listeners.remove( listener );

			if ( listeners.isEmpty() ) {
				locationProvider.removeListener( locationListener );
			}
		}
		else if ( Version.isDevelopment() ) {
			throw new IllegalArgumentException( "Listener isn't listening!" );
		}
	}


	private void startInitialProvider(
		SharedPreferences prefs
	) {

		boolean useThis = prefs.getBoolean( LocationProviders.DYNAMIC_POSITION.providerType, false );
		if ( useThis ) {
			locationProviderChangeListener.onSharedPreferenceChanged( prefs, LocationProviders.DYNAMIC_POSITION.providerType );
		}
		else {

			useThis = prefs.getBoolean( LocationProviders.STATIC_POSITION.providerType, false );
			if ( useThis ) {
				locationProviderChangeListener.onSharedPreferenceChanged( prefs, LocationProviders.STATIC_POSITION.providerType );
			}
			else {

				useThis = prefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.providerType, false );
				if ( useThis ) {
					locationProviderChangeListener.onSharedPreferenceChanged( prefs, LocationProviders.LATITUDE_LONGITUDE_POSITION.providerType );
				}
			}
		}

		// Didn't find anything? Use dynamic positioning...
		if ( !useThis ) {
			locationProviderChangeListener.onSharedPreferenceChanged( prefs, LocationProviders.DYNAMIC_POSITION.providerType );
		}
	}


	private SharedPreferences.OnSharedPreferenceChangeListener createLocationProviderChangeListener() {

		return new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences,
				String            prefKey
			) {

				LocationProviders newProvider = LocationProviders.fromProviderType( prefKey );
				if ( null == newProvider ) {
					// Nope...not a provider type...
					return;
				}

				if ( null != locationProvider ) {
					locationProvider.onDestroy();
				}

				switch ( newProvider ) {
					case DYNAMIC_POSITION:
						locationProvider = new DynamicLocationProvider( activity );
						break;

					case STATIC_POSITION:
						break;

					case LATITUDE_LONGITUDE_POSITION:
						locationProvider = new LatitudeLongitudeLocationProvider( activity );
						break;
				}
			}
		};
	}


	/**
	 * Invoked when the user has gestured that they want to change their location or how we determine their location.
	 */
	private void changeLocation() {

		activity.startActivity( new Intent( activity, EditLocationProviderPreferences.class ) );
	}


	/**
	 * Invoked when the user gestures that they want some really detailed info about their current location.
	 */
	private void showLocationDetails() {

		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( "Current Location" );
		builder.setMessage( lastKnownLocation.toString() );

		final AlertDialog dialog = builder.create();
		dialog.show();
	}



	public GeoLocation getLastKnownLocation() {
		return lastKnownLocation;
	}


	/**
	 * Intended to be called when someone has reason to believe that the set of available location providers
	 *  has changed significantly enough that we ought to requery them all.
	 */
	public void onLocationProvidersChanged() {

		// We don't need to muck with our listener nor do we change anything about anyone who's listening to
		//  us...just pass this through.
		locationProvider.onLocationProvidersChanged();
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private ZmanimLocation updateLastKnownLocation(
		Location  newLocation,
		String    locationName
	) {

		// Can be null if we don't actually have a new location...
		if ( null == newLocation ) {
			return null;
		}
		else {
			lastKnownLocation = new ZmanimLocation( newLocation, locationName );
		}

		return lastKnownLocation;
	}



	@SuppressWarnings( { "FeatureEnvy" } )
	private void fireLocationChanged(
		ZmanimLocation location
	) {

		LogUtils.logEvent( "newLocation", "latitude: " + location.getLatitude(),
			"longitude: " + location.getLongitude(), "location: " + location.getLocationName() );

		for ( Listener listener : listeners ) {
			listener.locationChanged( location );
		}
	}


	private void fireError(
		LocationError   error,
		ZmanimLocation  location
	) {

		for ( Listener listener : listeners ) {
			listener.error( error, location );
		}
	}



	public interface Listener {

		void locationChanged( ZmanimLocation newLocation );

		void error(
			LocationError   error,
			ZmanimLocation  lastKnownLocation
		);
	}


}  // End of ZmanimLocation class
