/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.providers;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.prefs.LocationProviders;

import android.content.SharedPreferences;

import java.util.TimeZone;

/**
 * Base class for our various location handlers.
 *<p/>
 *
 * The provider will do what it needs to in order to provide a location to the application when acquire() is invoked.
 *  When the location is acquired, either a RawLocationAcquiredEvent or PlaceAcquiredEvent will be fired, depending
 *  on what information was acquired.
 *
 *<p/>
 *  Due to the fact that some providers may have a cached location (in order to provide faster startup for the
 *    application), multiple of these events could be fired.
 *
 *<p/>
 *  If, for any reason, the acquisition is to be canceled, cancel() will handle that.
 */
public abstract class ZmanimLocationProvider {

	protected final EventBus                                  eventBus;

	protected final SharedPreferences                         locationManagementPrefs;


	protected ZmanimLocationProvider(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs
	) {
		this.eventBus = eventBus;
		this.locationManagementPrefs = locationManagementPrefs;
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


}  // End of LocationProvider class
