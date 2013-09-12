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
import com.gindin.zmanim.android.Activities;
import com.gindin.zmanim.android.prefs.EditLocationProviderPreferences;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.ZmanimPrefs;
import com.gindin.zmanim.location.ZmanimLocation;

import java.util.Map;
import java.util.TimeZone;


/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 *
 * TODO: Allow user to choose how to select location:
 *        3. Google Map (see http://code.google.com/android/add-ons/google-apis/mapkey.html, http://code.google.com/android/add-ons/google-apis/reference/index.html)
 */
public class ZmanimLocationManager
	implements LocationChangedEvent.HasListeners {


	/** Need to know the activity so we can do things like build dialogs and spawn other activities. */
	private final Activity                                    activity;

	/** Who's really providing the location for us? */
	private LocationProvider                                  locationProvider;

	/** Everyone wants to see what preferences have been set. */
	private SharedPreferences                                 locationManagementPrefs = null;

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

	/** Handles event firing, etc.. */
	private final LocationChangedEventManager                 locationChangedEventManager = new LocationChangedEventManager();


	@SuppressWarnings( "FeatureEnvy" )
	public ZmanimLocationManager(
		Activity activity
	) {

		this.activity = activity;

		locationListener = new LocationProvider.Listener() {

			public void onProgress(
				String message
			) {
				locationChangedEventManager.progress( message );
			}

			public void onLocationChanged(
				Location  newLocation,
				String    locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( newLocation, locationName );
				locationChangedEventManager.fire( new LocationChangedEvent( location ) );
			}

			public void onError(
				LocationError error,
				Location      oldLocation,
				String        locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( oldLocation, locationName );
				locationChangedEventManager.fire( new LocationChangedEvent( location, error ) );
			}
		};
	}


	/**
	 * Called when the activity is paused, and so we must temporarily stop anything to do with
	 *  acquiring locations or handling location updates.
	 */
	public void onStop() {

		// Flurry event log showed an NPE accessing the locationProvider. It seems that this could
		//  only happen if Zmanim was paused or stopped before we were able to create the provider.
		//  Better safe than sorry...
		if ( null != locationProvider ) {
			locationProvider.onStop();
		}
	}


	/**
	 * Called after onPause() once the activity has restarted.
	 */
	public void onStart() {

		if ( null == locationProvider ) {
			startInitialProvider();
		}

		locationProvider.onStart();
	}


	public void onDestroy() {

		if ( !locationChangedEventManager.destroy() ) {
			// If we get here, things are in bad shape, but we can still remove our listener from
			//  the location handler...
			locationProvider.removeListener( locationListener );
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


	@Override
	public void addLocationChangeListener( LocationChangedEvent.Listener listener ) {
		locationChangedEventManager.addListener( listener );
	}


	@Override
	public void removeLocationChangeListener( LocationChangedEvent.Listener listener ) {
		if ( locationChangedEventManager.removeListener( listener ) ) {
			locationProvider.removeListener( locationListener );
		}
	}


	/**
	 * @return the current location or null if we don't know our location.
	 */
	public ZmanimLocation getCurrentLocation() {
		return lastKnownLocation;
	}



	/**
	 * @return the last known location, or a default location if we don't know our location.
	 */
	public ZmanimLocation getLastKnownLocation() {

		if ( null == lastKnownLocation ) {
			// Do the best we can with a default location.
			return new ZmanimLocation();
		}

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
		prefsBeforeEditing = getLocationManagementPrefs().getAll();

		activity.startActivityForResult( new Intent( activity, EditLocationProviderPreferences.class ),
			Activities.EDIT_LOCATION_PREFERENCES.ordinal() );
	}


	/**
	 * Called after the user has changed some preferences regarding location. Always comes after we request the activity
	 *   to be displayed from within editLocationPrefs().
	 */
	public void locationPrefsChanged() {

		final Map<String, ?> prefsAfterEditing = getLocationManagementPrefs().getAll();
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

		SharedPreferences prefs = getLocationManagementPrefs();
		boolean useThis = prefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
		if ( useThis ) {
			locationProvider = new DynamicLocationProvider( activity, prefs );
		}
		else {

			useThis = prefs.getBoolean( LocationProviders.STATIC_POSITION.name(), false );
			if ( useThis ) {
				locationProvider = new StaticLocationProvider( activity, prefs );
			}
			else {

				useThis = prefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false );
				if ( useThis ) {
					locationProvider = new LatitudeLongitudeLocationProvider( activity, prefs );
				}
			}
		}

		// Didn't find anything? Use dynamic positioning, and make sure the prefs are updated.
		if ( !useThis ) {
			locationProvider = new DynamicLocationProvider( activity, prefs );
			prefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
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

		SharedPreferences prefs = getLocationManagementPrefs();
		if ( !prefs.getBoolean( LocationProviders.TimeZoneOptions.OVERRIDE_SYSTEM_TIME_ZONE.name(), false ) ) {
			return null;
		}

		final String timeZoneArea = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_AREA.name(), null );
		if ( ( null == timeZoneArea ) || ( 0 == timeZoneArea.trim().length() ) ) {
			return null;
		}

		final String timeZoneLocale = prefs.getString( LocationProviders.TimeZoneOptions.TIME_ZONE_LOCALE.name(), null );
		if ( ( null == timeZoneLocale ) || ( 0 == timeZoneLocale.trim().length() ) ) {
			return null;
		}


		return TimeZone.getTimeZone( timeZoneArea + "/" + timeZoneLocale );
	}


	private SharedPreferences getLocationManagementPrefs() {

		if ( null == locationManagementPrefs ) {
			locationManagementPrefs = activity.getSharedPreferences( ZmanimPrefs.LOCATION_MANAGEMENT.name(), Context.MODE_PRIVATE );
		}

		return locationManagementPrefs;
	}


}  // End of ZmanimLocation class
