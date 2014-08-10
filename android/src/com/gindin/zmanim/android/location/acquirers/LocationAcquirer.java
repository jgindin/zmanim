/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.prefs.PersonalPreferences;
import com.gindin.zmanim.location.ZmanimLocation;

import android.content.SharedPreferences;

import java.util.TimeZone;

/**
 * Base class for our various location acquirers.
 *<p/>
 *
 * The acquirer will do what it needs to in order to provide a location to the application when acquire() is invoked.
 *  When the location is acquired, either a RawLocationAcquiredEvent or PlaceAcquiredEvent will be fired, depending
 *  on what information was acquired.
 *
 *<p/>
 *  Due to the fact that some acquirers may have a cached location (in order to provide faster startup for the
 *    application), multiple of these events could be fired.
 *
 *<p/>
 *  If, for any reason, the acquisition is to be canceled, cancel() will handle that.
 */
public abstract class LocationAcquirer {

	protected final EventBus                                  eventBus;

	protected final SharedPreferences                         locationManagementPrefs;

	private final SharedPreferences                           personalPrefs;

	/**
	 * Keep track of the last location which was acquired so that we can avoid firing spurious events.
	 */
	private ZmanimLocation                                    lastKnownLocation;


	protected LocationAcquirer(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
	  SharedPreferences personalPrefs
	) {
		this.eventBus = eventBus;
		this.locationManagementPrefs = locationManagementPrefs;
		this.personalPrefs = personalPrefs;
	}


	/**
	 * Called to acquire the current location information.
	 */
	public abstract void acquire();


	/**
	 * Called when we're to stop trying to acquire a location. If called when the provider is not actively trying to
	 *  acquire the location, then it will silently do nothing.
	 */
	public void cancel() {
		// Nothing by default
	}


	/**
	 * Called when this location provider is going away.
	 */
	public void onDestroy() {
		// Nothing by default
	}


	/**
	 * @return The timezone specified by the user in the preferences, or null if we're to use the system time zone.
	 */
	protected final TimeZone getTimeZoneFromPrefs() {

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


	protected final void fireLocationAcquiredEvent( ZmanimLocation newLocation ) {

		fireLocationAcquiredEvent( newLocation, null );
	}


	protected final void fireLocationAcquiredEvent( ZmanimLocation newLocation, LocationError error ) {

		// Before we do anything, make sure that there's a significant change worthy of our time...
		if ( ( error == null ) && ( newLocation != null ) && ( lastKnownLocation != null ) ) {

			// Less than 1km? Don't bother...
			// I suppose you could run into an issue if that 1km puts you in a different time zone
			//  or something like that...
			if ( newLocation.getGeodesicDistance( lastKnownLocation ) < 1000 ) {
				return;
			}
		}

		lastKnownLocation = newLocation;

		boolean useElevation = personalPrefs.getBoolean( PersonalPreferences.Prefs.USE_ELEVATION.name(), false );
		if ( !useElevation && ( lastKnownLocation != null ) ) {
			lastKnownLocation.setElevation( 0.0 );
		}

		eventBus.fire( new LocationAcquiredEvent( lastKnownLocation, error ) );
	}


}  // End of LocationProvider class
