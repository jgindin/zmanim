/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.View;

import java.util.*;

import com.gindin.util.LogUtils;
import com.gindin.util.Version;
import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.prefs.EditLocationProviderPreferences;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.location.ZmanimLocation;


/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 *
 * TODO: Allow user to choose how to select location:
 *        3. Google Map (see http://code.google.com/android/add-ons/google-apis/mapkey.html, http://code.google.com/android/add-ons/google-apis/reference/index.html)
 */
public class ZmanimLocationManager {


	/** Need to know the activity so we can do things like build dialogs and spawn other activities. */
	private final Activity                                    activity;

	/** Who's really providing the location for us? */
	private LocationProvider                                  locationProvider;

	/** These are the folks who need to know when the location changes. */
	private final List<Listener>                              listeners = new ArrayList<Listener>();

	/** Everyone wants to see what preferences have been set. */
	private final SharedPreferences                           locationManagementPrefs;

	/** We need to listen to the provider for changes, which we then forward on to our listeners. */
	private final LocationProvider.Listener                   locationListener;

	/** When the user clicks on the location, they're indicating they want to change it. */
	private final View.OnClickListener                        locationClickListener = new View.OnClickListener() {
		public void onClick( View view ) {
			editLocationPrefs();
		}
	};

	/** When the user long-clicks on the location, they're indicating they want to see some details about it. */
	private final View.OnLongClickListener                    locationLongClickListener = new View.OnLongClickListener() {
			public boolean onLongClick( View view ) {
				showLocationDetails();
				return true;
			}
		};

	/** Uh, where's the last place we knew we were? */
	private ZmanimLocation                                    lastKnownLocation = null;

	/** This is where we display the user's location. */
	private View                                              locationField;

	/**
	 * Prior to invoking the location configuration activity, tuck away the current prefs so that if
	 *  the user doesn't make any changes, then we don't do anything upon return.
 	 */
	private Map<String,?>                                     prefsBeforeEditing;


	public ZmanimLocationManager(
		Activity activity
	) {

		this.activity = activity;
		locationManagementPrefs = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );

		locationListener = new DynamicLocationProvider.Listener() {

			public void onProgress(
				String message
			) {
				fireProgress( message );
			}


			public void onLocationChanged(
				Location  newLocation,
				String    locationName
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
	}


	/**
	 * Called when the activity is paused, and so we must temporarily stop anything to do with
	 *  acquiring locations or handling location updates.
	 */
	public void onStop() {
		locationProvider.onStop();
	}


	/**
	 * Called after onPause() once the activity has restarted.
	 */
	public void onStart() {
		locationProvider.onStart();
	}


	public void onDestroy() {

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

		clearLocationFieldListeners();
	}


	public void setLocationField(
		View locationField
	) {

		if ( null != this.locationField ) {
			clearLocationFieldListeners();
		}

		this.locationField = locationField;

		locationField.setOnClickListener( locationClickListener );
		locationField.setOnLongClickListener( locationLongClickListener );
	}



	public void addListener(
		Listener  listener
	) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				LogUtils.logError( "ZmanimLocationManager::addListener", "Listener already listening!", new Throwable() );
			}
		}
		else {
			listeners.add( listener );

			if ( 1 == listeners.size() ) {
				// Force a provider to be created...
				startInitialProvider();
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
			LogUtils.logError( "ZmanimLocationManager::removeListener", "Listener wasn't listening!", new Throwable() );
		}
	}


	public ZmanimLocation getLastKnownLocation() {
		return lastKnownLocation;
	}
	
	
	public String getLastKnownLocationString() {
		if ( null == lastKnownLocation ) {
			return "Unknown";
		}
		
		return lastKnownLocation.toShortString();
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


	/**
	 * Invoked when the user has gestured that they want to change their location or how we determine their location.
	 */
	public void editLocationPrefs() {

		// Capture all the prefs so that if the user doesn't make any changes, we don't do a bunch of recalculations.
		prefsBeforeEditing = locationManagementPrefs.getAll();

		activity.startActivityForResult( new Intent( activity, EditLocationProviderPreferences.class ), Activities.EDIT_LOCATION_PREFERENCES.ordinal() );
	}


	/**
	 * Called after the user has changed some preferences regarding location. Always comes after we request the activity
	 *   to be displayed from within editLocationPrefs().
	 */
	public void locationPrefsChanged() {

		final Map<String, ?> prefsAfterEditing = locationManagementPrefs.getAll();
		if ( !prefsAfterEditing.equals( prefsBeforeEditing ) ) {

			startInitialProvider();
		}

		prefsBeforeEditing = null;
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void startInitialProvider() {

		if ( null != locationProvider ) {
			locationProvider.removeListener( locationListener );
			locationProvider.onDestroy();
		}


		boolean useThis = locationManagementPrefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
		if ( useThis ) {
			locationProvider = new DynamicLocationProvider( activity, locationManagementPrefs );
		}
		else {

			useThis = locationManagementPrefs.getBoolean( LocationProviders.STATIC_POSITION.name(), false );
			if ( useThis ) {
				locationProvider = new StaticLocationProvider( activity, locationManagementPrefs );
			}
			else {

				useThis = locationManagementPrefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false );
				if ( useThis ) {
					locationProvider = new LatitudeLongitudeLocationProvider( activity, locationManagementPrefs );
				}
			}
		}

		// Didn't find anything? Use dynamic positioning, and make sure the prefs are updated.
		if ( !useThis ) {
			locationProvider = new DynamicLocationProvider( activity, locationManagementPrefs );
			locationManagementPrefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
		}

		locationProvider.addListener( locationListener );
	}


	private void clearLocationFieldListeners() {

		// Can happen that we get shutdown before we get a fix and really have an opportunity to get going...
		if ( null != locationField ) {
			locationField.setOnClickListener( null );
			locationField.setOnLongClickListener( null );
		}
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



	private ZmanimLocation updateLastKnownLocation(
		Location  newLocation,
		String    locationName
	) {

		// Can be null if we don't actually have a new location...
		if ( null == newLocation ) {
			return null;
		}
		else {

			TimeZone timeZone = getTimeZoneFromPrefs();
			lastKnownLocation = new ZmanimLocation( newLocation.getProvider(), locationName, newLocation.getTime(), timeZone,
				newLocation.getLatitude(), newLocation.getLongitude(),
				newLocation.hasAltitude() ? newLocation.getAltitude() : -1, newLocation.hasAccuracy() ? newLocation.getAccuracy() : -1 );
		}

		return lastKnownLocation;
	}


	/**
	 * @return The timezone specified by the user in the preferences, or null if we're to use the system time zone.
	 */
	private TimeZone getTimeZoneFromPrefs() {

		if ( !locationManagementPrefs.getBoolean( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name(), false ) ) {
			return null;
		}

		final String timeZoneArea = locationManagementPrefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
		if ( ( null == timeZoneArea ) || ( 0 == timeZoneArea.trim().length() ) ) {
			return null;
		}

		final String timeZoneLocale = locationManagementPrefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name(), null );
		if ( ( null == timeZoneLocale ) || ( 0 == timeZoneLocale.trim().length() ) ) {
			return null;
		}


		return TimeZone.getTimeZone( timeZoneArea + "/" + timeZoneLocale );
	}


	private void fireProgress(
		String message
	) {

		for ( Listener listener : listeners ) {
			listener.onProgress( message );
		}
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void fireLocationChanged(
		ZmanimLocation location
	) {

		/*
		//noinspection unchecked
		LogUtils.logEvent( "newLocation",
			new Pair<String,String>( "latitude", location.getLatitudeString() ),
			new Pair<String,String>( "longitude", location.getLongitudeString() ),
			new Pair<String, String>( "location", location.getLocationName() ) );
		*/

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
			ZmanimLocation lastKnownLocation
		);

		void onProgress(
			String message
		);
	}


}  // End of ZmanimLocation class
