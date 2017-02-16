/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;
import com.gindin.zmanlib.location.ZmanimLocation;

/**
 * An event fired when the name (and possibly some other information) have been acquired, regardless of how they
 *  may have been acquired. Specifically, this event does not provide enough information to actually provide a
 *  location.
 */
public class PlaceAcquiredEvent
  extends Event<PlaceAcquiredEvent.Handler> {


  public static abstract class Handler implements EventHandler<PlaceAcquiredEvent> {

    /**
     * Invoked when the place has been acquired. By place, we mean some rough description of a location.
     *
     * @param place a ZmanimLocation, where only the location name and time zone have been accurately populated.
     *              Specifically, the latitude & longitude will NOT be usable.
     */
    public abstract void placeAcquired( ZmanimLocation place );

    @Override
    public Class<PlaceAcquiredEvent> getAssociatedType() {
      return PlaceAcquiredEvent.class;
    }
  }


  private final ZmanimLocation                              place;


  public PlaceAcquiredEvent( ZmanimLocation place ) {
    this.place = place;
  }


  @Override
  public void dispatch( Handler handler ) {
      handler.placeAcquired( place );
  }


  @Override
  public String toString() {
    return "PlaceAcquiredEvent{" +
      "place=" + place +
      '}';
  }

} // End of RawLocationAcquiredEvent class
