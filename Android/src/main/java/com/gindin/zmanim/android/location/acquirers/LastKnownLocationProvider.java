/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.acquirers;


import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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

  private SaveLocationTask                                  currentlyRunningSaveTask = null;


  LastKnownLocationProvider( Context context ) {
    this.context = context;

    try ( FileInputStream input = context.openFileInput( NAME ) ) {
      ZmanimLocation location = ZmanimLocation.load( input );
      lastKnownLocation = toLocation( location );
    }
    catch ( Exception e ) {
      // Oh well, we can't read the last location, so...we just will have to deal ...
//      LogUtils.logError( "LKLP::ctor", "Couldn't read last location", e );
    }
  }


  Location getLastKnownLocation() {
    return lastKnownLocation;
  }


  void save( ZmanimLocation newLocation ) {

    if ( null == newLocation ) {
      return;
    }

    lastKnownLocation = toLocation( newLocation );

    currentlyRunningSaveTask = new SaveLocationTask( currentlyRunningSaveTask );
    currentlyRunningSaveTask.execute( newLocation );
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


  private class SaveLocationTask
    extends AsyncTask<ZmanimLocation, Void, Void> {

    private final SaveLocationTask                          lastSaveTask;


    SaveLocationTask( SaveLocationTask lastSaveTask ) {
      this.lastSaveTask = lastSaveTask;
    }


    @Override
    protected Void doInBackground( ZmanimLocation... params ) {

      if ( lastSaveTask != null ) {
        lastSaveTask.cancel( false );
      }

      final ZmanimLocation zmanimLocation = params[ 0 ];
      try ( FileOutputStream output = context.openFileOutput( NAME, Context.MODE_PRIVATE ) ) {

        zmanimLocation.save( output );
        output.flush();
        output.getFD().sync();
      }
      catch ( Exception e ) {
        // Just means we can't actually save the last location, and next time the user starts we'll have to go
        //  query from scratch.
      }

      return null;
    }

  }

} // End of LocationProvider class
