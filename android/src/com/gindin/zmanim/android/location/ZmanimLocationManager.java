/*
 * Copyright (c) 2011. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.zmanim.android.location.providers.ZmanimLocationProvider;
import com.gindin.zmanim.android.location.providers.LocationProviderFactory;
import com.gindin.zmanim.android.prefs.LocationProviders;

import android.content.SharedPreferences;


/**
 * Handles all things related to finding and knowing the location necessary to compute the Zmanim.
 *<p/>
 *
 * Events Fired:
 *  <ul>
 *    <li>RawLocationAcquiredEvent
 *    <li>FullLocationAcquiredEvent
 *  </ul>
 */
public class ZmanimLocationManager {

	/** Everyone wants to see what preferences have been set. */
	// @Inject
	private final SharedPreferences                           locationManagementPrefs;


	// @Inject
	private final LocationProviderFactory                     locationProviderFactory;

	/** Which provider are we currently using? */
	private ZmanimLocationProvider                            currentProvider;


	public ZmanimLocationManager(
		SharedPreferences       locationManagementPrefs,
		LocationProviderFactory locationProviderFactory
	) {
		this.locationManagementPrefs = locationManagementPrefs;
		this.locationProviderFactory = locationProviderFactory;
	}


	@SuppressWarnings( "FeatureEnvy" )
	public void determineLocation() {

		cancel();

		boolean useThis = locationManagementPrefs.getBoolean( LocationProviders.DYNAMIC_POSITION.name(), false );
		if ( useThis ) {
			currentProvider = locationProviderFactory.get( LocationProviderFactory.Type.FROM_DEVICE );
		}
		else {

			useThis = locationManagementPrefs.getBoolean( LocationProviders.STATIC_POSITION.name(), false );
			if ( useThis ) {
				currentProvider = locationProviderFactory.get( LocationProviderFactory.Type.NAME_FROM_USER );
			}
			else {

				useThis = locationManagementPrefs.getBoolean( LocationProviders.LATITUDE_LONGITUDE_POSITION.name(), false );
				if ( useThis ) {
					currentProvider = locationProviderFactory.get( LocationProviderFactory.Type.LAT_LONG_FROM_USER );
				}
			}
		}

		// Didn't find anything? Use dynamic positioning, and make sure the prefs are updated.
		if ( !useThis ) {
			currentProvider = locationProviderFactory.get( LocationProviderFactory.Type.FROM_DEVICE );
			locationManagementPrefs.edit().putBoolean( LocationProviders.DYNAMIC_POSITION.name(), true ).commit();
		}

		currentProvider.acquire();
	}


	public void cancel() {

		if ( currentProvider != null ) {
			currentProvider.cancel();
		}
	}

}  // End of ZmanimLocation class
