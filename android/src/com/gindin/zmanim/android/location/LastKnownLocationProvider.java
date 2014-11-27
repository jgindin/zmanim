/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import com.gindin.util.LogUtils;
import com.gindin.zmanim.location.ZmanimLocation;

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

	private final Activity                                    activity;


	private Location                                          lastKnownLocation;


	LastKnownLocationProvider( Activity activity ) {
		this.activity = activity;

		FileInputStream input = null;
		try {
			input = activity.openFileInput( NAME );
			ZmanimLocation location = ZmanimLocation.load( input );

			lastKnownLocation = location.toLocation();
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
			output = activity.openFileOutput( NAME, Context.MODE_PRIVATE );
			ZmanimLocation zmanimLocation = new ZmanimLocation.Builder().from( newLocation ).build();
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
					output.close();
				}
				catch ( IOException e ) {
					// Oh well.
				}
			}
		}
	}

} // End of LocationProvider class
