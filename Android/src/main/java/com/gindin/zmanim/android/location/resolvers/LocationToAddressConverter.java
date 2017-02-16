/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.eventBus.EventBus;
import com.gindin.zmanim.android.location.LocationAcquiredEvent;
import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.PlaceAcquiredEvent;
import com.gindin.zmanlib.location.ZmanimLocation;
import org.jetbrains.annotations.NotNull;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens for RawLocationAcquiredEvent and PlaceAcquiredEvent and starts the process of translating to an address.
 */
public class LocationToAddressConverter {

  private final EventBus                                    eventBus;

  private final List<LocationResolver>                      orderedResolvers;

  private final LocationAcquiredEvent.Handler               locationAcquiredHandler;
  private final PlaceAcquiredEvent.Handler                  placeAcquiredHandler;


  public LocationToAddressConverter(
    final Activity          activity,
    EventBus          eventBus,
    Geocoder          geocoder,
    SharedPreferences personalPrefs
  ) {

    this.eventBus = eventBus;

    CachedLocationResolver.IOFactory ioFactory = new CachedLocationResolver.IOFactory() {
      @Override
      public InputStream openInput() throws FileNotFoundException {
        return activity.openFileInput( "CachedLocations" );
      }


      @Override
      public FileOutputStream openOutput() throws FileNotFoundException {
        return activity.openFileOutput( "CachedLocations", Context.MODE_PRIVATE );
      }


      @Override
      public void runInBackground( Runnable runnable ) {
        // Urgh. API 11 adds "execute( Runnable )". Since my min API is 10, that method doesn't exist,
        //  and we get a NoSuchMethodError... Adding a 'null' argument is enough to get it to do the right thing...
        new BackgroundTask().execute( runnable, null );
      }
    };

    CachedLocationResolver cachedLocationResolver = new CachedLocationResolver( ioFactory, eventBus, personalPrefs );
    cachedLocationResolver.loadCache();

    orderedResolvers = new ArrayList<>();
    orderedResolvers.add( cachedLocationResolver );
    orderedResolvers.add( new AndroidResolver( eventBus, geocoder, personalPrefs ) );

    orderedResolvers.add( new NominatimResolver( eventBus, personalPrefs ) );
    orderedResolvers.add( new MapQuestResolver( eventBus, personalPrefs ) );

    orderedResolvers.add( new PassthroughResolver( eventBus, personalPrefs ) );

    locationAcquiredHandler = new LocationAcquiredEvent.Handler() {
      @Override
      public void onLocationAcquired(
        ZmanimLocation  location,
        boolean         isCachedLocation
      ) {
        handleLocationAcquired( location );
      }


      @Override
      public void onError(
        @NotNull LocationError  error,
        @NotNull ZmanimLocation lastKnownLocation
      ) {
        handleLocationAcquired( lastKnownLocation );
      }
    };
    eventBus.addHandler( locationAcquiredHandler );

    placeAcquiredHandler = new PlaceAcquiredEvent.Handler() {
      @Override
      public void placeAcquired( ZmanimLocation location ) {
        orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
      }
    };
    eventBus.addHandler( placeAcquiredHandler );
  }


  public void onDestroy() {

    eventBus.removeHandler( locationAcquiredHandler );
    eventBus.removeHandler( placeAcquiredHandler );

    for ( LocationResolver resolver : orderedResolvers ) {
      resolver.onDestroy();
    }
  }


  private void handleLocationAcquired( @NotNull ZmanimLocation location ) {
    if ( !location.isResolved() && !location.isInvalid ) {
      orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
    }
  }


  private static class BackgroundTask
    extends AsyncTask<Runnable, Void, Void> {

    @Override
    protected Void doInBackground( Runnable... runnables ) {
      runnables[0].run();

      return null;
    }
  }

} // End of LocationToAddressConverter class
