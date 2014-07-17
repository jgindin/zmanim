/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.android.location.acquirers.*;
import com.gindin.zmanim.android.location.acquirers.LatitudeLongitudeLocationAcquirer;
import com.gindin.zmanim.android.location.acquirers.LocationAcquirer;
import com.gindin.zmanim.android.prefs.LocationProviders;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


	public static enum Type {
		FROM_DEVICE,
		LAT_LONG_FROM_USER,
		NAME_FROM_USER
	}


	private final EventBus                                    eventBus;

	/** Everyone wants to see what preferences have been set. */
	private final SharedPreferences                           locationManagementPrefs;

	/** System-wide, actual location manager. */
	private final LocationManager                             locationManager;

	private final Map<Type,LocationAcquirer>                  typeToAcquirerMap = new HashMap<Type, LocationAcquirer>();

	/**
	 * Used to store & retrieve the last fix we received, but in a way that will survive restarts of the app.
	 */
	private final LastKnownLocationProvider                   lastKnownLocationProvider;

	/** Which provider are we currently using? */
	private LocationAcquirer                                  currentAcquirer;

	/** Listen for requests to get a new location. */
	private final RequestLocationUpdateEvent.Handler          locationRequestHandler;


	public ZmanimLocationManager(
		EventBus          eventBus,
		SharedPreferences locationManagementPrefs,
	  LocationManager   locationManager
	) {

		this.eventBus = eventBus;
		this.locationManagementPrefs = locationManagementPrefs;
		this.locationManager = locationManager;

		lastKnownLocationProvider = new LastKnownLocationProvider( locationManagementPrefs );

		locationRequestHandler = new RequestLocationUpdateEvent.Handler() {
			@Override
			public void onLocationUpdateRequested() {
				determineLocation();
			}
		};
		eventBus.addHandler( locationRequestHandler );
	}


	@SuppressWarnings( "FeatureEnvy" )
	private void determineLocation() {

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
		if ( !useThis ) {
			currentAcquirer = getAcquirer( Type.FROM_DEVICE );
			locationManagementPrefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).apply();
		}

		currentAcquirer.acquire();
	}


	public void cancel() {

		if ( currentAcquirer != null ) {
			currentAcquirer.cancel();
		}
	}


	public List<String> getAllProviders( boolean evenDisabledProviders ) {

		ArrayList<String> providerNames;

		if ( evenDisabledProviders ) {
			providerNames = new ArrayList<String>( locationManager.getAllProviders() );

			// We treat the last known provider as disabled because we don't want someone thinking that they can (or should)
			//  ask this provider for an update. (We also prevent that in the requestSingleUpdate method, but this is a good
			//  safe guard.)
			providerNames.add( LastKnownLocationProvider.NAME );
		}
		else {
			providerNames = new ArrayList<String>( locationManager.getProviders( true ) );
		}

		return providerNames;
	}


	public Location getLastKnownLocation( String providerName ) {

		if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
			return lastKnownLocationProvider.getLastKnownLocation();
		}
		else {
			return locationManager.getLastKnownLocation( providerName );
		}
	}


	public boolean isProviderEnabled( String providerName ) {

		if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
			return false;
		}
		else {
			return locationManager.isProviderEnabled( providerName );
		}
	}


	public void requestSingleUpdate( String providerName, LocationListener listener ) {

		if ( LastKnownLocationProvider.NAME.equals( providerName ) ) {
			// Don't allow this. It'll prevent the real providers from having a chance....
		}
		else {
			locationManager.requestSingleUpdate( providerName, listener, null );
		}
	}


	public void removeUpdates( LocationListener listener ) {

		locationManager.removeUpdates( listener );
	}


	public void saveLastLocation( Location lastLocation ) {

		lastKnownLocationProvider.update( lastLocation );
	}


	private LocationAcquirer getAcquirer( Type type ) {

		LocationAcquirer provider = typeToAcquirerMap.get( type );
		if ( provider == null ) {
			switch ( type ) {
				case FROM_DEVICE:
					provider = new DynamicLocationAcquirer( eventBus, locationManagementPrefs, this );
					break;

				case LAT_LONG_FROM_USER:
					provider = new LatitudeLongitudeLocationAcquirer( eventBus, locationManagementPrefs );
					break;

				case NAME_FROM_USER:
					provider = new StaticLocationAcquirer( eventBus, locationManagementPrefs );
					break;

				default:
					assert false : "Unknown provider type: " + type;
			}

			typeToAcquirerMap.put( type, provider );
		}

		return provider;
	}

}  // End of ZmanimLocation class
