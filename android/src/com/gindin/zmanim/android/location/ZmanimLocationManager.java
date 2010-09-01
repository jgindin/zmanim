/*
 * Copyright (c) 2009. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.content.Intent;
import android.view.View;
import com.gindin.util.LogUtils;
import com.gindin.util.Version;
import com.gindin.zmanim.android.prefs.EditLocationPreferences;
import net.sourceforge.zmanim.util.GeoLocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;

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
public class ZmanimLocationManager {


	private final Activity                                    activity;

	private final DynamicLocationHandler                      dynamicLocationHandler;

	private final DynamicLocationHandler.Listener             dynamicLocationListener;

	private final List<Listener>                              listeners = new ArrayList<Listener>();

	private ZmanimLocation                                    lastKnownLocation = null;



	public ZmanimLocationManager(
		Activity activity
	) {

		this.activity = activity;

		dynamicLocationHandler = new DynamicLocationHandler( activity );

		dynamicLocationListener = new DynamicLocationHandler.Listener() {
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

			final List<Listener> copy = listeners;
			for ( Listener listener : copy ) {
				removeListener( listener );
			}

			if ( !listeners.isEmpty() ) {

				// If we get here, things are in bad shape, but we can still remove our listener from
				//  the location handler...
				dynamicLocationHandler.removeListener( dynamicLocationListener );

				if ( Version.isDevelopment() ) {
					throw new IllegalStateException( "Should have removed all listeners. Still have " + listeners.size() );
				}
			}
		}
	}


	public void setLocationField(
		View locationField
	) {

		// TODO: Do we need to hold onto the field so that if it gets changed, we can remove listeners?

		locationField.setOnClickListener(
			new View.OnClickListener() {
				public void onClick( View view ) {
					changeLocation();
				}
			}
		);

		locationField.setOnLongClickListener(
			new View.OnLongClickListener() {
				public boolean onLongClick( View view ) {
					showLocationDetails();
					return true;
				}
			}
		);

	}



	public void addListener(
		Listener  listener
	) {

		if ( listeners.contains( listener ) ) {
			if ( Version.isDevelopment() ) {
				throw new IllegalArgumentException( "Listener already listening!" );
			}
		}
		else {
			listeners.add( listener );

			if ( 1 == listeners.size() ) {
				dynamicLocationHandler.addListener( dynamicLocationListener );
			}
		}
	}


	public void removeListener(
		Listener listener
	) {

		if ( listeners.contains( listener ) ) {
			listeners.remove( listener );

			if ( listeners.isEmpty() ) {
				dynamicLocationHandler.removeListener( dynamicLocationListener );
			}
		}
		else if ( Version.isDevelopment() ) {
			throw new IllegalArgumentException( "Listener isn't listening!" );
		}
	}


	/**
	 * Invoked when the user has gestured that they want to change their location or how we determine their location.
	 */
	private void changeLocation() {

		activity.startActivity( new Intent( activity, EditLocationPreferences.class ) );
	}


	/**
	 * Invoked when the user gestures that they want some really detailed info about their current location.
	 */
	private void showLocationDetails() {

		AlertDialog.Builder builder = new AlertDialog.Builder( activity );
		builder.setTitle( "Current Location" );
		builder.setMessage( lastKnownLocation.toString() );

		final AlertDialog dialog = builder.create();
		dialog.show();
	}



	public GeoLocation getLastKnownLocation() {
		return lastKnownLocation;
	}


	/**
	 * Intended to be called when someone has reason to believe that the set of available location providers
	 *  has changed significantly enough that we ought to requery them all.
	 */
	public void onLocationProvidersChanged() {

		// We don't need to muck with our listener nor do we change anything about anyone who's listening to
		//  us...just pass this through.
		// TODO: When we support "static" locations, we'll not want to pass this on...
		dynamicLocationHandler.onLocationProvidersChanged();
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
