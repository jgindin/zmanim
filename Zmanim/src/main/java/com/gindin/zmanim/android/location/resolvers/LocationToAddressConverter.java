/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanim.android.location.resolvers;

import com.gindin.zmanim.android.location.LocationError;
import com.gindin.zmanim.android.location.ZmanLocationUtil;
import com.gindin.zmanim.android.service.ZmanimService;
import com.gindin.zmanlib.location.ZmanimLocation;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SafeJobIntentService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Resolves a location to its name via various resolvers.
 */
public class LocationToAddressConverter
  extends SafeJobIntentService {

  private CachedLocationResolver cachedLocationResolver;

  /** Actions which this service can handle. */
  public enum Actions {
    RESOLVE_PLACE,

    RESOLVE_LOCATION,

    /** Dummy in case somehow something unexpected is handed into the value() method. */
    NONE;


    /** Better than valueOf() since it won't throw any exception. */
    static Actions value( @Nullable String actionName ) {

      // Shouldn't ever be null. But...allowing the param to be nullable makes the code that calls this
      //  method cleaner...no warnings...
      if ( actionName == null ) {
        return NONE;
      }

      try {
        return Actions.valueOf( actionName );
      }
      catch ( Exception e ) {
        //Ignore this request...
        return NONE;
      }
    }
  }


  private enum ActionParams {
    LOCATION
  }



  /** Sometimes, after an upgrade, we need to wipe the cache. */
  public static boolean                                     wipeCacheOnStart = false;

  private static final int                                  JOB_ID = 606;

  private List<LocationResolver>                            orderedResolvers;



  public static void enqueueWork(
    Context         context,
    ZmanimLocation  location,
    Actions         action
  ) {

    Intent resolveLocation = new Intent( context, LocationToAddressConverter.class );
    resolveLocation.setAction( action.name() );
    resolveLocation.putExtra( ActionParams.LOCATION.name(), ZmanLocationUtil.toBundle( location ) );

    enqueueWork( context, LocationToAddressConverter.class, JOB_ID, resolveLocation );
  }


  @Override
  public void onCreate() {
    super.onCreate();

    CachedLocationResolver.IOFactory ioFactory = new CachedLocationResolver.IOFactory() {
      @Override
      public InputStream openInput() throws FileNotFoundException {
        return openFileInput( "CachedLocations" );
      }


      @Override
      public FileOutputStream openOutput() throws FileNotFoundException {
        return openFileOutput( "CachedLocations", Context.MODE_PRIVATE );
      }


      @Override
      public void run( Runnable runnable ) {
        // We could run this in the background. However I think that will cause issues as we're a service that will
        //  go away once our work is done. Having some more work happen in the background seems like a recipe for
        //  the work to not actually be able to happen...
        runnable.run();
      }
    };

    cachedLocationResolver = new CachedLocationResolver( ioFactory );
    if ( wipeCacheOnStart ) {
      wipeCacheOnStart = false;
      cachedLocationResolver.wipeCache();
    }
    else {
      cachedLocationResolver.loadCache();
    }

    orderedResolvers = new ArrayList<>();
    orderedResolvers.add( cachedLocationResolver );

    if ( Geocoder.isPresent() ) {
      orderedResolvers.add( new AndroidResolver( new Geocoder( this ) ) );
    }

    orderedResolvers.add( new PassthroughResolver() );
  }


  @Override
  protected void onHandleWork( @NonNull Intent intent ) {

    ZmanimLocation location = ZmanLocationUtil.fromBundle( intent.getBundleExtra( ActionParams.LOCATION.name() ) );
    ZmanimLocation resolved = null;

    switch ( Actions.value( intent.getAction() ) ) {
      case RESOLVE_LOCATION:
        if ( location.isResolved() ) {
          // Nothing what-so-ever to do!
          return;
        }
        else if ( !location.isInvalid ) {
          resolved = resolve( location );
        }
        break;

      case RESOLVE_PLACE:
        resolved = resolve( location );
        break;

      default:
        resolved = null;
    }

    if ( resolved == null ) {
      resolved = new ZmanimLocation.Builder().invalidLocation();
    }

    if ( resolved.isInvalid || !resolved.isResolved() ) {
      ZmanimService.enqueueWork( this, new LocationError( LocationError.Code.CANNOT_DETERMINE_LOCATION ), location );
    }
    else {

      ZmanimService.enqueueWork( this, resolved );
      cachedLocationResolver.cache( resolved );
    }

  }


  @Nullable
  private ZmanimLocation resolve( @NonNull ZmanimLocation location ) {

    return orderedResolvers.get( 0 ).resolve( location, orderedResolvers.subList( 1, orderedResolvers.size() ) );
  }


} // End of LocationToAddressConverter class
