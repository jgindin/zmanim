/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus;

import com.gindin.util.Pair;

import java.util.*;

/**
 * Acts as a communication 'bus', allowing events to be fired to registered handlers.
 *<p/>
 *
 * Guarantees that if event 'a' is fired, all handlers of 'a' will be invoked before handlers for any subsequent
 *  events are invoked. In other words, if A fires 'a' and B and C are registered handlers, and B fires 'b' in
 *  the course of handling 'a', B and C will both receive and have a chance to handle 'a' before anyone is notified
 *  about 'b'.
 *<p/>
 *
 * If a handler of event 'a' is added during the firing of an 'a', that handler will not be notified of this event.
 *<p/>
 *
 * Also adds caching of the most recent event. This allows 'late' handlers to receive "catchup" events if they so desire.
 *
 * Roughly based on the event bus from GWT.
 */
public final class EventBus {

  /** The registered handlers, keyed by the event they're interested in. */
  private final HandlerCollection                           handlerCollection = new HandlerCollection();

  /**
   * If we're currently firing an event, then we'll queue newly added handlers here, and they will be added
   *  after the event is done being dispatched.
   */
  private final List<Pair<EventHandler, Boolean>>           postponedHandlersToAdd = new ArrayList<>();

  /**
   * If we're currently firing an event, then we'll queue handlers which have requested to be removed here, and they
   *  will be added after the event is done being dispatched.
   */
  private final HandlerCollection                           postponedHandlersToRemove = new HandlerCollection();

  /**
   * If we're currently firing an event, then we'll queue newly fired events here, and they will be fired, in the order
   *  they were received, after the current event is done being dispatched.
   */
  private final List<Event<?>>                              postponedEvents = new ArrayList<>();

  /**
   * A cache of the most recently fired event for a given event type.
   */
  private final EventCache                                  eventCache = new EventCache();

  /**
   * Used to allow interaction with our cache prior to dispatching an event.
   */
  private final EventCacheControl                           eventCacheControl = new EventCacheControlImpl();

  /**
   * When we're actively firing an event, this will be set. Used to ensure that (a) handler add/removes only happen
   *  after the event is fully dispatched; (b) events are dispatched in the proper order.
   */
  private Event<?>                                          inProgressEvent = null;



  /**
   * Adds the specified handler. If an event of this handler's type is currently being fired, the newly added handler
   *  will NOT get this event.
   */
  public <E extends Event<H>, H extends EventHandler<E>> void addHandler( H handler ) {

    addHandler( handler, false );
  }


  public <E extends Event<H>, H extends EventHandler<E>> void addHandler( H handler, boolean withCatchup ) {

    assert null != handler;

    if ( null == inProgressEvent ) {
      actuallyAddHandler( handler, withCatchup );
    }
    else {
      synchronized ( this ) {
        //noinspection AutoBoxing
        postponedHandlersToAdd.add( new Pair<>( handler, withCatchup ) );
      }
    }
  }


  /**
   * Removes the specified handler. If an event of this handler's type is currently being fired, this handler may
   *  still receive the event.
   */
  public synchronized <E extends Event<H>, H extends EventHandler<E>> void removeHandler( H handler ) {

    assert handler != null;

    if ( null == inProgressEvent ) {
      List<H> handlers = handlerCollection.get( handler.getAssociatedType() );
      handlers.remove( handler );
    }
    else {
      postponedHandlersToRemove.put( handler, handler.getAssociatedType() );
    }
  }


  public <H extends EventHandler, E extends Event<H>> void fire( E event ) {

    assert null != event;

    boolean inProgress;
    synchronized ( this ) {
      inProgress = ( inProgressEvent != null );
    }

    if ( inProgress ) {
      synchronized ( this ) {
        postponedEvents.add( event );
      }
    }
    else {

      fireEvent( event );
      handlePostponed();
    }
  }


  private <H extends EventHandler, E extends Event<H>> void fireEvent( E event ) {

    synchronized ( this ) {
      inProgressEvent = event;
    }

    // It's not clear to me, and I'm hoping its a synchronization thing, but I've seen stack traces where the
    //  event here is null after we've fired an event and we're handling the postponed stuff...
    if ( event == null ) {
      return;
    }

    event.preDispatch( eventCacheControl );

    List<H> handlers;
    synchronized ( this ) {
      handlers = handlerCollection.get( event.getClass() );
      if ( null != handlers ) {
        handlers = new ArrayList<>( handlers );
      }
      else {
        handlers = Collections.emptyList();
      }
    }

    for ( H handler : handlers ) {
      dispatchEvent( event, handler );
    }

    synchronized ( this ) {
      if ( event.isCachable() ) {
        eventCache.put( (Class<Event>)event.getClass(), event );
      }

      inProgressEvent = null;
    }
  }


  private void handlePostponed() {

    handlePostponedAdds();
    handlePostponedRemoves();
    handlePostponedFiring();

    // Have to go around again...
    if ( !postponedHandlersToAdd.isEmpty() || !postponedHandlersToRemove.isEmpty() || !postponedEvents.isEmpty() ) {
      handlePostponed();
    }
  }


  private void handlePostponedAdds() {

    for ( Pair<EventHandler, Boolean> pair : postponedHandlersToAdd ) {
      //noinspection AutoUnboxing
      actuallyAddHandler( pair.first, pair.second );
    }
    postponedHandlersToAdd.clear();
  }


  private synchronized void handlePostponedRemoves() {
    handlerCollection.removeAll( postponedHandlersToRemove );
    postponedHandlersToRemove.clear();
  }


  private void handlePostponedFiring() {
    // Have to take a copy in case during the firing of one of these events we get more events to postpone...
    ArrayList<Event<?>> copyOfPostponedEvents = new ArrayList<Event<?>>( postponedEvents );
    postponedEvents.clear();
    for ( Event<?> event : copyOfPostponedEvents ) {
      fireEvent( event );
    }
  }


  private <H extends EventHandler, E extends Event<H>> void dispatchEvent( E event, H handler ) {
    event.dispatch( handler );
  }


  private <E extends Event<H>, H extends EventHandler<E>> void actuallyAddHandler( H handler, boolean withCatchup ) {

    synchronized ( this ) {
      handlerCollection.put( handler, handler.getAssociatedType() );
    }

    if ( withCatchup ) {
      E cachedEvent = eventCache.get( handler.getAssociatedType() );
      if ( cachedEvent != null ) {
        cachedEvent.dispatch( handler );
      }
    }
  }


  private class EventCacheControlImpl
    implements EventCacheControl {

    @Override
    public void clearCacheForType( Class<? extends Event> type ) {
      synchronized ( EventBus.this ) {
        eventCache.remove( type );
      }
    }
  }


  /**
   * Type-safe heterogeneous container, associating an event type with the handlers for that type..
   *
   * See Effective Java, 2nd ed.
   */
  private static class HandlerCollection {

    /**
     * The actual, non-type safe map. The keys are the event class, and the values are a list of handlers for that
     *  event type.
     */
    private final Map<Class<? extends Event>, List<? extends EventHandler>>   handlerMap = new HashMap<>();


    public <H extends EventHandler, E extends Event<H>> void put( H handler, Class<E> eventType ) {

      // This is OK because we're the only ones with access to put things into the map...
      @SuppressWarnings( "unchecked" )
      List<H> handlers = (List<H>)handlerMap.get( eventType );
      if ( null == handlers ) {
        handlers = new ArrayList<>();
        handlerMap.put( eventType, handlers );
      }

      handlers.add( handler );
    }


    public <H extends EventHandler, E extends Event<H>> List<H> get( Class<E> eventType ) {

      // This is OK because we're the only ones with access to put things into the map...
      @SuppressWarnings( "unchecked" )
      List<H> handlers = (List<H>)handlerMap.get( eventType );
      return handlers;
    }


    public void clear() {
      handlerMap.clear();
    }


    public boolean isEmpty() {
      return handlerMap.isEmpty();
    }


    void removeAll( HandlerCollection source ) {

      for ( Map.Entry<Class<? extends Event>, List<? extends EventHandler>> entry : source.handlerMap.entrySet() ) {

        List<? extends EventHandler> handlers = handlerMap.get( entry.getKey() );
        if ( null != handlers ) {
          handlers.removeAll( entry.getValue() );
        }
      }
    }
  }



  /**
   * Type-safe heterogeneous container, associating an event type with the most recent event for that type..
   *
   * See Effective Java, 2nd ed.
   */
  private static class EventCache {

    /**
     * The actual, non-type safe map. The keys are the event class, and the value is the most recent event for that
     *  event type.
     */
    private final Map<Class<? extends Event>, Event>        eventCache = new HashMap<>();


    public <H extends EventHandler, E extends Event<H>> void put( Class<E> eventType, E cache ) {
      eventCache.put( eventType, cache );
    }


    public <H extends EventHandler, E extends Event<H>> E get( Class<E> eventType ) {
      //noinspection unchecked
      return (E)eventCache.get( eventType );
    }


    public <H extends EventHandler, E extends Event<H>> void remove( Class<E> eventType ) {
      eventCache.remove( eventType );
    }
  }

} // End of EventBus class
