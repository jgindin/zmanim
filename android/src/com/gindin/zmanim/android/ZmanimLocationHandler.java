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

/**
 * Encapsulates the necessary work of finding our current location from the phone in a way that is usable by the Zmanim
 * API.
 *
 * TODO: Allow user to choose how to select location:
 *        1. Auto
 *        2. Manually enter city, state (with optional street, etc...)
 *        3. Google Map (see http://code.google.com/android/add-ons/google-apis/mapkey.html, http://code.google.com/android/add-ons/google-apis/reference/index.html)
 */
public class ZmanimLocationHandler {


	private final Activity                                    activity;

	private final LocationHandler                             locationHandler;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private final LocationHandler.Listener                    locationListener;

	private ZmanimLocation                                    lastKnownLocation = null;



	public ZmanimLocationHandler(
		Activity activity
	) {

		this.activity = activity;

		locationHandler = new LocationHandler( activity );

		locationListener = new LocationHandler.Listener() {
			public void onLocationChanged(
				Location newLocation,
				String locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( newLocation, locationName );
				fireLocationChanged( location );
			}


			public void onError(
				LocationHandler.Error error,
				Location              oldLocation,
				String                locationName
			) {

				ZmanimLocation location = updateLastKnownLocation( oldLocation, locationName );
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
	private ZmanimLocation updateLastKnownLocation(
		Location  newLocation,
		String    locationName
	) {

		// Can be null if we don't actually have a new location...
		if ( null == newLocation ) {
			return null;
		}
		else {
			lastKnownLocation = new ZmanimLocation( newLocation, locationName );
		}

		return lastKnownLocation;
	}



	@SuppressWarnings( { "FeatureEnvy" } )
	private void fireLocationChanged(
		ZmanimLocation location
	) {

		LogUtils.logEvent( "newLocation", "latitude: " + location.getLatitude(),
			"longitude: " + location.getLongitude(), "location: " + location.getLocationName() );

		for ( Listener listener : listeners ) {
			listener.locationChanged( location );
		}
	}


	private void fireError(
		LocationHandler.Error error,
		ZmanimLocation        location
	) {

		for ( Listener listener : listeners ) {
			listener.error( error, location );
		}
	}


	public interface Listener {

		void locationChanged( ZmanimLocation newLocation );

		void error(
			LocationHandler.Error error,
			ZmanimLocation        lastKnownLocation
		);
	}


}  // End of ZmanimLocation class
