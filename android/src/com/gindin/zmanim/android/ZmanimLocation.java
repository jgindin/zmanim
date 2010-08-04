/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;
import com.gindin.util.LocationHandler;
import com.gindin.util.LogUtils;
import com.gindin.util.Version;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 *
 * TODO: Allow user to choose how to select location:
 *        1. Auto
 *        2. Manually enter city, state (with optional street, etc...)
 *        3. Google Map (see http://code.google.com/android/add-ons/google-apis/mapkey.html, http://code.google.com/android/add-ons/google-apis/reference/index.html)
 */
public class ZmanimLocation {

	private static final Location                             SEATTLE;

	static {
		SEATTLE = new Location( "hard-coded" );
		//noinspection MagicNumber
		SEATTLE.setLatitude( 47.554163 );
		//noinspection MagicNumber
		SEATTLE.setLongitude( -122.257919 );
		SEATTLE.setAltitude( 0.0 );
		SEATTLE.setTime( System.currentTimeMillis() );
	}

	/** Minimum amount of time we want to elapse before we check for a new location. 30 minutes. */
	public static final long                                  MIN_LOCATION_UPDATE_INTERVAL = 30 * 60 * 1000;

	/** Minimum distance we want to elapse before we check for a new location. 2000 meters. */
	public static final float                                 MIN_LOCATION_UPDATE_DISTANCE = 2000;

	private final Activity                                    activity;

	private final LocationHandler                             locationHandler;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private CurrentLocation                                   lastKnownLocation;

	private final LocationHandler.Listener                    locationListener;



	@SuppressWarnings( { "FeatureEnvy" } )
	public ZmanimLocation(
		Activity activity
	) {

		this.activity = activity;

		lastKnownLocation  = new CurrentLocation( "Seattle", SEATTLE.getLatitude(), SEATTLE.getLongitude(),
			SEATTLE.getAltitude(), TimeZone.getDefault() );

		locationHandler = new LocationHandler( activity, MIN_LOCATION_UPDATE_INTERVAL, MIN_LOCATION_UPDATE_DISTANCE );

		locationListener = new LocationHandler.Listener() {
			public void onLocationChanged(
				Location  newLocation,
				String    locationName,
				String    providerName
			) {

				lastKnownLocation.setProviderName( providerName );
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


	public void onClickLocation() {

		// So...the activity notifies us, we then ask it to show a given dialog, which it will wind up
		//  asking us to display....
		activity.showDialog( R.id.location );
	}


	public Dialog createLocationSelectionDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( "Current Location" );
		builder.setMessage( lastKnownLocation.toString() );

		return builder.create();
	}



	public GeoLocation getLastKnownLocation() {
		return lastKnownLocation;
	}


	public void reset() {
		locationHandler.reset();
	}


	@SuppressWarnings( { "FeatureEnvy" } )
	private void fireLocationChanged() {

		LogUtils.logEvent( "newLocation", "latitude: " + lastKnownLocation.getLatitude(),
			"longitude: " + lastKnownLocation.getLongitude(), "location: " + lastKnownLocation.getLocationName() );

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

	
	private static class CurrentLocation
		extends GeoLocation {
		
		private String                                          providerName;


		CurrentLocation(
			String    locationName,
			double    latitude,
			double    longitude,
			double    altitude,
			TimeZone  timeZone
		) {

			super( locationName, latitude, longitude, altitude, timeZone );
		}


		public void setProviderName( String providerName ) {
			this.providerName = providerName;
		}


		@Override
		public String toString() {

			@SuppressWarnings( { "MagicNumber" } )
			final int millisInHour = 1000 * 60 * 60;

			StringBuffer sb = new StringBuffer();
			sb.append("Location Name:\t").append(getLocationName());
			sb.append("\nLatitude:\t").append(getLatitude()).append(" deg");
			sb.append("\nLongitude:\t").append(getLongitude()).append(" deg");
			sb.append("\nElevation:\t").append(getElevation()).append(" Meters");
			sb.append("\nTZ Name:\t").append(getTimeZone().getID());
			sb.append("\nTZ GMT Offset:\t").append( getTimeZone().getRawOffset() / millisInHour );
			sb.append("\nTZ DST Offset:\t").append( getTimeZone().getDSTSavings() / millisInHour );
			sb.append("\nProvider:\t").append( providerName );

			return sb.toString();
		}
	}
	

}  // End of ZmanimLocation class
