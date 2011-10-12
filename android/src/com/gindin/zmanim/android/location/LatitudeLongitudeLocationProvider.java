/*
 * Copyright (c) 2010. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import com.gindin.zmanim.android.prefs.LocationProviders;

/**
 * Location provider that deals with a static latitude & longitude location.
 */
class LatitudeLongitudeLocationProvider
	extends LocationProvider {


	/** The location represented by this provider. */
	private final Location                                    ourLocation = new Location( "Zmanim Latitude & Longitude Provider" );


	LatitudeLongitudeLocationProvider(
		Activity          activity,
		SharedPreferences locationManagementPrefs
	) {

		super( activity, locationManagementPrefs );

		updateLatitudeFromPrefs();
		updateLongitudeFromPrefs();
	}


	@Override
	public void onLocationProvidersChanged() {
		// Doesn't matter to us.
	}


	@Override
	protected Location getLastKnownLocation() {
		return ourLocation;
	}


	@Override
	protected void listenerAdded(
		Listener  listener,
		boolean   isFirst
	) {

		// Don't want to fireNewLocation because that would notify *everyone*...
		listener.onLocationChanged( ourLocation, lookupLocationName( ourLocation ) );
	}


	@Override
	protected void listenerRemoved(
		Listener  listener,
		boolean   wasLast
	) {
		// nothing to do
	}


	private void updateLatitudeFromPrefs() {

		String latitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LATITUDE.name(), "0" );
		try {
			LatitudeParser latitudeParser = new LatitudeParser();

			ourLocation.setLatitude( latitudeParser.parse( latitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid latitude: " + latitudePref ) );
		}
	}


	private void updateLongitudeFromPrefs() {

		String longitudePref = locationManagementPrefs.getString( LocationProviders.LatitudeLongitudeProviderOptions.LONGITUDE.name(), "0" );
		try {
			LongitudeParser longitudeParser = new LongitudeParser();

			ourLocation.setLongitude( longitudeParser.parse( longitudePref ) );
			ourLocation.setTime( System.currentTimeMillis() );
		}
		catch ( Exception e ) {
			// Oh well.
			fireError( new LocationError( LocationError.Code.BAD_INPUT, "Invalid longitude: " + longitudePref ) );
		}
	}


	private abstract static class LatitudeLongitudeParser {

		protected abstract double getMax();

		public double parse( String latitude )
			throws IllegalArgumentException {

			// Make sure there's no extra whitespace...
			latitude = latitude.trim();

			try {
				return Double.parseDouble( latitude );
			}
			catch ( NumberFormatException e ) {
				// Well, keep trying to parse.
			}

			// Supported formats are:
			//  DDD MM['] [SS["]] -- Assumed North
			//  [N|S] DDD MM['] [SS["]]
			//  DDD.MM[N|S]

			int multiplier = 1;
			char test = latitude.charAt( 0 );

			if ( ( 'S' == test ) || ( 's' == test ) || ( 'W' == test ) || ( 'w' == test ) ) {
				multiplier = -1;
				latitude = latitude.substring( 1 );
			}
			else if ( ( 'N' == test ) || ( 'n' == test ) || ( 'E' == test ) || ( 'e' == test ) ) {
				latitude = latitude.substring( 1 );
			}

			test = latitude.charAt( latitude.length() - 1 );
			if ( ( 'S' == test ) || ( 's' == test ) || ( 'W' == test ) || ( 'w' == test ) ) {
				multiplier = -1;
				latitude = latitude.substring( 0, latitude.length() - 1 );
			}
			else if ( ( 'N' == test ) || ( 'n' == test ) || ( 'E' == test ) || ( 'e' == test ) ) {
				latitude = latitude.substring( 0, latitude.length() - 1 );
			}


			// Clean up the input....
			latitude = latitude.replace( '.', ' ' );
			latitude = latitude.replace( '\'', ' ' );
			latitude = latitude.replace( '\"', ' ' );

			latitude = latitude.trim();


			String[] latitudeComponents = latitude.split( " " );
			if ( 0 == latitudeComponents.length ) {
				throw new IllegalArgumentException( "Can't parse latitude: " + latitude );
			}

			double degrees = Double.parseDouble( latitudeComponents[0].trim() );

			double minutes = 0;
			if ( latitudeComponents.length > 1 ) {
				minutes = Double.parseDouble( latitudeComponents[1].trim() );
			}

			double seconds = 0;
			if ( latitudeComponents.length > 2 ) {
				seconds = Double.parseDouble( latitudeComponents[2].trim() );
			}

			// See http://stackoverflow.com/questions/6945008/converting-latitude-longitude-valuesdmscompass-direction-format-to-correspondi
			//noinspection MagicNumber
			degrees = degrees + ( ( ( seconds / 60 ) + minutes ) / 60 );

			// Ahhh, but wait. IFF the value is too large, then perhaps what the user was entering was, after all, in
			//  decimal format?
			if ( degrees > getMax() && ( minutes > 0 ) ) {
				degrees = Double.parseDouble( latitudeComponents[0] + "." + latitudeComponents[1] );
			}

			// If the value is still too large, then just choose zero.
			if ( degrees > getMax() ) {
				degrees = 0;
			}

			degrees = multiplier * degrees;

			return degrees;
		}
	}


	private static class LatitudeParser
		extends LatitudeLongitudeParser {

		@Override
		protected double getMax() {
			//noinspection MagicNumber
			return 90;
		}
	}


	private static class LongitudeParser
		extends LatitudeLongitudeParser {

		@Override
		protected double getMax() {
			//noinspection MagicNumber
			return 180;
		}
	}

}  // End of LatitudeLongitudeLocationProvider class
