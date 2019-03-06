/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.RequestLocationUpdateEvent;
import com.gindin.zmanim.android.prefs.LocationProviders;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.TimeZone;

/**
 * Base class for our various location acquirers.
 *<p/>
 *
 * The acquirer will do what it needs to in order to provide a location to the application when acquire() is invoked.
 *  When the location is acquired, work will be enqueued to the ZmanimService to handle the result.
 *
 *<p/>
 *  Due to the fact that some acquirers may have a cached location (in order to provide faster startup for the
 *    application), multiple of these events could be fired.
 *
 *<p/>
 *  If, for any reason, the acquisition is to be canceled, cancel() will handle that.
 */
public abstract class LocationAcquirer {

  protected final SharedPreferences                         locationManagementPrefs;

  protected final Context                                   context;


  LocationAcquirer(
    SharedPreferences locationManagementPrefs,
    Context           context
  ) {
    this.locationManagementPrefs = locationManagementPrefs;
    this.context = context;
  }


  /**
   * Called to acquire the current location information.
   */
  public abstract void acquire( final RequestLocationUpdateEvent.CachedLocationUsage cachedLocationUsage );


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
  @Nullable
  final TimeZone getTimeZoneFromPrefs() {

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


  final void fireLocationAcquired(
    ZmanimLocation newLocation
  ) {

    ZmanimService.enqueueWork( context, newLocation );
  }


  final void fireLocationNotAcquired(
    LocationError             error,
    @Nullable ZmanimLocation  lastKnownLocation
  ) {

    ZmanimService.enqueueWork( context, error, lastKnownLocation );
  }


  final void firePlaceAcquired(
    ZmanimLocation newPlace
  ) {

    ZmanimService.enqueueWork( context, newPlace );
  }

}  // End of LocationProvider class
