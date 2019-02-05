/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;
import com.gindin.zmanlib.location.ZmanimLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An event fired when the current location has been acquired. If a handler needs to know if the location
 *  is fully resolved, it will have to ask the location.
 */
public class LocationAcquiredEvent
  extends Event<LocationAcquiredEvent.Handler> {


  public static abstract class Handler implements EventHandler<LocationAcquiredEvent> {

    /**
     * Invoked when the location has been acquired.
     *  @param location The location which has been acquired.
     */
    public abstract void onLocationAcquired(
      ZmanimLocation location
    );

    /**
     * Invoked when an error occurred while trying to acquire the location. The specified location is, by definition,
     *  cached and not necessarily correct.
     *
     * @param error Describes the error.
     * @param location The last known location
     */
    public abstract void onError(
      @NotNull  LocationError   error,
      @Nullable ZmanimLocation  location
    );


    @Override
    public Class<LocationAcquiredEvent> getAssociatedType() {
      return LocationAcquiredEvent.class;
    }
  }


  /** The location which has been acquired. */
  private final ZmanimLocation                              location;


  /**
   * If there was any error determining the location, then this describes the error. In this case, the location
   *  is actually the last known location
   */
  private final LocationError                               error;


  public LocationAcquiredEvent(
    @NotNull ZmanimLocation newLocation
  ) {
    this.location = newLocation;
    this.error = null;
  }


  public LocationAcquiredEvent(
    @NotNull  LocationError   error,
    @Nullable ZmanimLocation  lastKnownLocation
  ) {
    this.location = lastKnownLocation;
    this.error = error;
  }


  @Override
  public void dispatch( Handler handler ) {
    if ( error == null ) {
      handler.onLocationAcquired( location );
    }
    else {
      handler.onError( error, location );
    }
  }


  @Override
  public String toString() {
    return "LocationAcquiredEvent{" +
      "location=" + location +
      ", error=" + error +
      '}';
  }

} // End of RawLocationAcquiredEvent class
