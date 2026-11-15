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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.SafeJobIntentService;

/**
 * Names a location, or finds the place the user typed, through {@link PlaceResolution}'s chain: the cache, the
 *  system geocoder, then the place data bundled with the app.
 */
public class LocationToAddressConverter
  extends SafeJobIntentService {

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

  private PlaceNamer                                        namer;



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

    namer = new PlaceNamer( PlaceResolution.create( this ) );
    if ( wipeCacheOnStart ) {
      wipeCacheOnStart = false;
      namer.forgetCachedPlaces();
    }
  }


  /** Names places through {@code resolution} instead of the app's own chain. */
  @VisibleForTesting
  void namePlacesWith( @NonNull PlaceResolution resolution ) {
    namer = new PlaceNamer( resolution );
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
    }

  }


  @Nullable
  private ZmanimLocation resolve( @NonNull ZmanimLocation location ) {

    return location.hasCoordinates() ? namer.placeForCoordinates( location ) : namer.placeForName( location );
  }


} // End of LocationToAddressConverter class
