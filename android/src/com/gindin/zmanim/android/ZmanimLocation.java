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


	private final Activity                                    activity;

	private final LocationHandler                             locationHandler;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private final CurrentLocation                             lastKnownLocation = new CurrentLocation();

	private final LocationHandler.Listener                    locationListener;



	public ZmanimLocation(
		Activity activity
	) {

		this.activity = activity;

		locationHandler = new LocationHandler( activity );

		locationListener = new LocationHandler.Listener() {
			public void onLocationChanged(
				Location  newLocation,
				String    locationName,
				String    providerName
			) {

				CurrentLocation location = updateLastKnownLocation( newLocation, locationName, providerName );
				fireLocationChanged( location );
			}


			public void onError(
				LocationHandler.Error error,
				Location              oldLocation,
				String                locationName,
				String                providerName
			) {

				CurrentLocation location = updateLastKnownLocation( oldLocation, locationName, providerName );
				fireError( error, location );
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
	private CurrentLocation updateLastKnownLocation(
		Location  newLocation,
		String    locationName,
		String    providerName
	) {


		// Can be null if we don't actually have a new location...
		if ( null == newLocation ) {
			return null;
		}
		else {
			lastKnownLocation.setProviderName( providerName );
			lastKnownLocation.setLocationName( locationName );
			lastKnownLocation.setLatitude( newLocation.getLatitude() );
			lastKnownLocation.setLongitude( newLocation.getLongitude() );

			double altitude = newLocation.getAltitude();
			if ( !newLocation.hasAltitude() || ( altitude < 0 ) ) {
				altitude = 0.0;
			}

			lastKnownLocation.setElevation( altitude );
		}

		return lastKnownLocation;
	}



	@SuppressWarnings( { "FeatureEnvy" } )
	private void fireLocationChanged(
		CurrentLocation location
	) {

		LogUtils.logEvent( "newLocation", "latitude: " + location.getLatitude(),
			"longitude: " + location.getLongitude(), "location: " + location.getLocationName() );

		for ( Listener listener : listeners ) {
			listener.locationChanged( location );
		}
	}


	private void fireError(
		LocationHandler.Error error,
		CurrentLocation       location
	) {

		for ( Listener listener : listeners ) {
			listener.error( error, location );
		}
	}


	public interface Listener {

		void locationChanged( GeoLocation newLocation );

		void error(
			LocationHandler.Error error,
			GeoLocation lastKnownLocation
		);
	}

	
	private static class CurrentLocation
		extends GeoLocation {

		@SuppressWarnings( { "MagicNumber" } )
		private static int                                      MILLIS_IN_HOUR = 60 * 60 * 1000;

		private String                                          providerName;


		private CurrentLocation() {
			setTimeZone( TimeZone.getDefault() );
		}


		public void setProviderName( String providerName ) {
			this.providerName = providerName;
		}


		@Override
		public String toString() {

			StringBuffer sb = new StringBuffer();
			sb.append("Location Name:\t").append(getLocationName());
			sb.append("\nLatitude:\t").append(getLatitude()).append(" deg");
			sb.append("\nLongitude:\t").append(getLongitude()).append(" deg");
			sb.append("\nElevation:\t").append(getElevation()).append(" Meters");
			sb.append("\nTZ Name:\t").append(getTimeZone().getID());
			sb.append("\nTZ GMT Offset:\t").append( getTimeZone().getRawOffset() / MILLIS_IN_HOUR );
			sb.append("\nTZ DST Offset:\t").append( getTimeZone().getDSTSavings() / MILLIS_IN_HOUR );
			sb.append("\nProvider:\t").append( providerName );

			return sb.toString();
		}
	}
	

}  // End of ZmanimLocation class
