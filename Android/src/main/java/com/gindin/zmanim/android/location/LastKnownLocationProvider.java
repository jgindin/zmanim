/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;


import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

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

  private SaveLocationTask                                  currentlyRunningSaveTask = null;


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
//      LogUtils.logError( "LKLP::ctor", "Couldn't read last location", e );
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


  public void save( ZmanimLocation newLocation ) {

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


  private void saveInBackground( ZmanimLocation zmanimLocation ) {

    FileOutputStream output = null;
    try {
      output = context.openFileOutput( NAME, Context.MODE_PRIVATE );

      zmanimLocation.save( output );
    }
    catch ( Exception e ) {
      // Just means we can't actually save the last location, and next time the user starts we'll have to go
      //  query from scratch.
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

      saveInBackground( params[0] );

      return null;
    }
  }
} // End of LocationProvider class
