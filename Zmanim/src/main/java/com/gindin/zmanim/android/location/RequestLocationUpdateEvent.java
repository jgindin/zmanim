/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.location;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventCacheControl;
import com.gindin.eventBus.EventHandler;

/**
 * Fired to request that the location be re-acquired, using whatever acquirer has already been configured.
 */
public class RequestLocationUpdateEvent
  extends Event<RequestLocationUpdateEvent.Handler> {

  public static abstract class Handler implements EventHandler<RequestLocationUpdateEvent> {

    public abstract void onLocationUpdateRequested( final RequestLocationUpdateEvent event );


    @Override
    public Class<RequestLocationUpdateEvent> getAssociatedType() {
      return RequestLocationUpdateEvent.class;
    }
  }


  public enum CachedLocationUsage {

    /**
     * We prefer to use the cached location ASAP, and then continue to try and determine the actual location.
     */
    PREFER,

    /**
     * Do not use a cached location.
     */
    NEVER
  }


  /**
   * Indicate that if a cached location is available, we should use it immediately, but then continue to obtain
   *  the user's location, based on their preferences. (Unless, of course the needsActualLocation flag is set, in
   *  which case that takes precedence.)
   */
  public final CachedLocationUsage                          cachedLocationUsage;



  public RequestLocationUpdateEvent( final CachedLocationUsage cachedLocationUsage ) {

    this.cachedLocationUsage = cachedLocationUsage;
  }


  @Override
  public boolean isCachable() {
    return false;
  }


  @Override
  public void preDispatch( EventCacheControl cacheControl ) {
    super.preDispatch( cacheControl );

    // Need to ensure that any prior location acquisition events are removed from the cache because we need
    //  to get a fresh start.
    cacheControl.clearCacheForType( LocationAcquiredEvent.class );
  }


  @Override
  public void dispatch( Handler handler ) {
    handler.onLocationUpdateRequested( this );
  }

} // End of RequestLocationUpdateEvent class
