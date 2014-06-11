/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.util.LogUtils;
import com.gindin.util.eventBus.EventBus;
import com.gindin.zmanim.location.ZmanimLocation;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Uses the built-in Android Geocoder to resolve the location.
 */
class AndroidResolver
	extends LocationResolver {

	private final Geocoder                                    geocoder;


	AndroidResolver(
		EventBus eventBus,
		Geocoder geocoder
	) {
		super( eventBus );
		this.geocoder = geocoder;
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected ZmanimLocation resolveFromCoordinatesInBackground( ZmanimLocation location ) {

		if ( !Geocoder.isPresent() ) {
			return null;
		}

		final double latitude = location.getLatitude();
		final double longitude = location.getLongitude();

		// TODO: Check our cache for a hit so we don't have to go out to the geocoder...

		Address address;
		try {
			final List<Address> addresses = geocoder.getFromLocation( latitude, longitude, 5 );

			if ( ( null != addresses ) && !addresses.isEmpty() ) {
				address = addresses.get( 0 );
			}
			else {
				address = null;
			}
		}
		catch ( IOException ioe ) {
			// Docs for Geocoder.getFromLocation say this happens when there's no network connectivity, or
			//  there's some other I/O error. In any case, it can happen...
			address = null;
			LogUtils.logError( "LP::updateLocationWithName", "", ioe );
		}
		catch ( Exception e ) {
			address = null;
			LogUtils.logError( "LP::updateLocationWithName", "", e );
		}

		// TODO: Cache the location somewhere...
		return new ZmanimLocation.Builder()
			.from( location )
			.resolvedTo( address )
			.build();
	}


	@SuppressWarnings( "FeatureEnvy" )
	protected ZmanimLocation resolveFromNameInBackground( ZmanimLocation location ) {

		if ( !Geocoder.isPresent() ) {
			return null;
		}


		// In case there's an issue, we'll just return the original place. It won't have a real name, but it'll
		//  still be Good Enough.
		ZmanimLocation updatedLocation = location;

		try {
			List<Address> addresses = geocoder.getFromLocationName( location.getLocationName(), 5 );

			if ( ( null != addresses ) && !addresses.isEmpty() ) {
				final Address address = addresses.get( 0 );
				updatedLocation = new ZmanimLocation.Builder()
					.resolvedTo( address )
					.inTimeZone( location.getTimeZone() )
					.build();
			}
		}
		catch ( IOException ioe ) {
			// Docs for Geocoder.getFromLocationName say this happens when there's no network connectivity, or
			//  there's some other I/O error. In any case, it can happen...
		}
		catch ( Exception e ) {
			LogUtils.logError( "SLP::updateLocationFromPlace", "", e );
		}

		return updatedLocation;
	}


} // End of AndroidResolver class
