/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.BuildConfig;
import com.gindin.zmanim.android.location.acquirers.DynamicLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LatitudeLongitudeLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.StaticLocationAcquirer;
import com.gindin.zmanim.android.prefs.LocationProviders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;


/**
 * Handles all things related to finding and knowing the location necessary to compute the Zmanim.
 *<p/>
 *
 * Listens for:
 *  <ul>
 *    <li>RequestLocationUpdateEvent
 *  </ul>
 *<p/>
 *
 * Events Fired:
 *  <ul>
 *    <li>RawLocationAcquiredEvent
 *    <li>FullLocationAcquiredEvent
 *  </ul>
 */
public class ZmanimLocationManager {


  public enum Type {
    FROM_DEVICE,
    LAT_LONG_FROM_USER,
    NAME_FROM_USER
  }


  private final Context                                     context;

  private final EventBus                                    eventBus;

  /** Everyone wants to see what preferences have been set. */
  private final SharedPreferences                           locationManagementPrefs;

  private final SharedPreferences                           personalPrefs;

  private final Map<Type, LocationAcquirer>                 typeToAcquirerMap = new HashMap<>();

  /** Which provider are we currently using? */
  private LocationAcquirer                                  currentAcquirer;

  /** Listen for requests to get a new location. */
  private RequestLocationUpdateEvent.Handler                locationRequestHandler;



  public ZmanimLocationManager(
    Context           context,
    EventBus          eventBus,
    SharedPreferences locationManagementPrefs,
    SharedPreferences personalPrefs
  ) {

    this.context = context;
    this.eventBus = eventBus;
    this.locationManagementPrefs = locationManagementPrefs;
    this.personalPrefs = personalPrefs;

    locationRequestHandler = new RequestLocationUpdateEvent.Handler() {
      @Override
      public void onLocationUpdateRequested( final RequestLocationUpdateEvent event ) {
        determineLocation( event );
      }
    };
    eventBus.addHandler( locationRequestHandler );
  }


  public void onStop() {

    if ( locationRequestHandler != null ) {
      eventBus.removeHandler( locationRequestHandler );
      locationRequestHandler = null;
    }

    cancel();
  }


  private void determineLocation( final RequestLocationUpdateEvent event ) {

    cancel();

    boolean useThis = locationManagementPrefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
    if ( useThis ) {
      currentAcquirer = getAcquirer( Type.FROM_DEVICE );
    }
    else {

      useThis = locationManagementPrefs.getBoolean( LocationProviders.STATIC_POSITION.name(), false );
      if ( useThis ) {
        currentAcquirer = getAcquirer( Type.NAME_FROM_USER );
      }
      else {

        useThis = locationManagementPrefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false );
        if ( useThis ) {
          currentAcquirer = getAcquirer( Type.LAT_LONG_FROM_USER );
        }
      }
    }

    // Didn't find anything? Use dynamic positioning, and make sure the prefs are updated.
    if ( currentAcquirer == null ) {
      currentAcquirer = getAcquirer( Type.FROM_DEVICE );
      locationManagementPrefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).apply();
    }

    currentAcquirer.acquire( event.cachedLocationUsage );
  }


  private void cancel() {

    if ( currentAcquirer != null ) {
      currentAcquirer.cancel();
      currentAcquirer = null;
    }
  }


  private LocationAcquirer getAcquirer( Type type ) {

    LocationAcquirer provider = typeToAcquirerMap.get( type );
    if ( provider == null ) {
      switch ( type ) {
        case FROM_DEVICE:
          provider = new DynamicLocationAcquirer( locationManagementPrefs, context );
          break;

        case LAT_LONG_FROM_USER:
          provider = new LatitudeLongitudeLocationAcquirer( locationManagementPrefs, context );
          break;

        case NAME_FROM_USER:
          provider = new StaticLocationAcquirer( locationManagementPrefs, context );
          break;

        default:
          if ( BuildConfig.DEBUG ) {
            throw new AssertionError( "Unknown provider type: " + type );
          }
      }

      typeToAcquirerMap.put( type, provider );
    }

    return provider;
  }


}  // End of ZmanimLocation class
