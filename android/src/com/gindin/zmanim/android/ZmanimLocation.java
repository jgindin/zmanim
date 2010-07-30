/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.content.Context;
import android.location.Location;
import com.gindin.util.LocationHandler;
import com.gindin.util.Version;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 */
public class ZmanimLocation {

	private static final Location                             SEATTLE;

	static {
		SEATTLE = new Location( "hard-coded" );
		SEATTLE.setLatitude( 47.554163 );
		SEATTLE.setLongitude( -122.257919 );
		SEATTLE.setAltitude( 0.0 );
		SEATTLE.setTime( System.currentTimeMillis() );
	}

	/** Minimum amount of time we want to elapse before we check for a new location. 30 minutes. */
	public static final long                                  MIN_LOCATION_UPDATE_INTERVAL = 30 * 60 * 1000;

	/** Minimum distance we want to elapse before we check for a new location. 2000 meters. */
	public static final float                                 MIN_LOCATION_UPDATE_DISTANCE = 2000;

	private final LocationHandler                             locationHandler;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private GeoLocation                                       lastKnownLocation;

	private final LocationHandler.Listener                    locationListener;


	public ZmanimLocation(
		Context context
	) {

		lastKnownLocation  = new GeoLocation( "Seattle", SEATTLE.getLatitude(), SEATTLE.getLongitude(),
			SEATTLE.getAltitude(), TimeZone.getDefault() );

		locationHandler = new LocationHandler( context, MIN_LOCATION_UPDATE_INTERVAL, MIN_LOCATION_UPDATE_DISTANCE );

		locationListener = new LocationHandler.Listener() {
			public void onLocationChanged(
				Location newLocation,
				String    locationName
			) {
				lastKnownLocation.setLocationName( locationName );
				lastKnownLocation.setLatitude( newLocation.getLatitude() );
				lastKnownLocation.setLongitude( newLocation.getLongitude() );

				double altitude = newLocation.getAltitude();
				if ( !newLocation.hasAltitude() || ( altitude < 0 ) ) {
					altitude = 0.0;
				}

				lastKnownLocation.setElevation( altitude );

				fireLocationChanged();
			}


			public void onError( LocationHandler.Error error ) {
				fireError( error );
			}
		};
	}


	public void onDestroy() {

		if ( !listeners.isEmpty() ) {
			listeners.clear();
			locationHandler.removeListener( locationListener );
		}
	}


	public void addListener(
		Listener  listener
	) {

		if ( Version.isDevelopment() ) {
			assert !listeners.contains( listener );
		}

		listeners.add( listener );

		if ( 1 == listeners.size() ) {
			locationHandler.addListener( locationListener );
		}
	}


	public void removeListener(
		Listener listener
	) {

		if ( Version.isDevelopment() ) {
			assert listeners.contains( listener );
		}

		listeners.remove( listener );

		if ( listeners.isEmpty() ) {
			locationHandler.removeListener( locationListener );
		}
	}


	public GeoLocation getLastKnownLocation() {
		return lastKnownLocation;
	}


	public void reset() {
		locationHandler.reset();
	}


	private void fireLocationChanged() {

		for ( Listener listener : listeners ) {
			listener.locationChanged( lastKnownLocation );
		}
	}


	private void fireError( LocationHandler.Error error ) {

		for ( Listener listener : listeners ) {
			listener.error( error );
		}
	}


	public interface Listener {

		void locationChanged( GeoLocation newLocation );

		void error( LocationHandler.Error error );
	}


}  // End of ZmanimLocation class
