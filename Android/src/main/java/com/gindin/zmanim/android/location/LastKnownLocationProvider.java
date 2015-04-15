/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;


import com.gindin.util.LogUtils;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.location.Location;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This is a pseudo-location provider that is used to keep track of the last known location. It cannot be a real location
 *  provider because that would require various permissions that we just don't need. And, there's some discussion
 *  that such permissions actually can only be granted if its installed in a package signed with the system key.
 *  See http://stackoverflow.com/questions/3394626/how-to-provide-your-own-locationprovider-on-android.
 */
public class LastKnownLocationProvider {

	public static final String                                NAME = "LastKnownLocationProvider";

	private final Context                                     context;


	private Location                                          lastKnownLocation;


	LastKnownLocationProvider( Context context ) {
		this.context = context;

		FileInputStream input = null;
		try {
			input = context.openFileInput( NAME );
			ZmanimLocation location = ZmanimLocation.load( input );
			lastKnownLocation = toLocation( location );
		}
		catch ( Exception e ) {
			// Oh well, we can't read the last location, so...we just will have to deal ...
			LogUtils.logError( "LKLP::ctor", "Couldn't read last location", e );
		}
		finally {
			if ( input != null ) {
				try {
					input.close();
				}
				catch ( IOException e ) {
					// Urgh. Oh well.
				}
			}
		}
	}


	public Location getLastKnownLocation() {
		return lastKnownLocation;
	}


	public void update( Location newLocation ) {

		if ( null == newLocation ) {
			return;
		}

		FileOutputStream output = null;
		try {
			output = context.openFileOutput( NAME, Context.MODE_PRIVATE );

			ZmanimLocation.Builder builder = new ZmanimLocation.Builder()
				.fromProvider( newLocation.getProvider() )
				.at( newLocation.getTime() )
				.atLatitude( newLocation.getLatitude() )
				.atLongitude( newLocation.getLongitude() );

			if ( newLocation.hasAccuracy() ) builder.withAccuracy( newLocation.getAccuracy() );
			if ( newLocation.hasAltitude() ) builder.atElevation( newLocation.getAltitude() );

			ZmanimLocation zmanimLocation = builder.build();
			zmanimLocation.save( output );
		}
		catch ( Exception e ) {
			// Just means we can't actually save the last location, and next time the user starts we'll have to go
			//  query from scratch.
			LogUtils.logError( "LKLP::update", "Can't open file.", e );
		}
		finally {
			if ( output != null ) {
				try {
					output.flush();
					output.getFD().sync();
					output.close();
				}
				catch ( IOException e ) {
					// Oh well.
				}
			}
		}
	}


	private Location toLocation( ZmanimLocation zmanimLocation ) {

		Location location = new Location( zmanimLocation.providerName );
		location.setLatitude( zmanimLocation.getLatitude() );
		location.setLongitude( zmanimLocation.getLongitude() );
		location.setTime( zmanimLocation.locationTime );
		location.setAccuracy( zmanimLocation.accuracy );
		location.setAltitude( zmanimLocation.getElevation() );

		return location;
	}


} // End of LocationProvider class
