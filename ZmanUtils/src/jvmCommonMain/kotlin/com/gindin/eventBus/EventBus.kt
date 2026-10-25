/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.eventBus

/**
 * Acts as a communication 'bus', allowing events to be fired to registered handlers.
 *
 * Guarantees that if event 'a' is fired, all handlers of 'a' will be invoked before handlers for
 * any subsequent events are invoked. In other words, if A fires 'a' and B and C are registered
 * handlers, and B fires 'b' in the course of handling 'a', B and C will both receive and have a
 * chance to handle 'a' before anyone is notified about 'b'.
 *
 * If a handler of event 'a' is added during the firing of an 'a', that handler will not be notified
 * of this event.
 *
 * Also adds caching of the most recent event. This allows 'late' handlers to receive "catchup"
 * events if they so desire.
 *
 * Roughly based on the event bus from GWT.
 */
public class EventBus {

  /** The registered handlers, keyed by the event they're interested in. */
  private val handlerCollection = HandlerCollection()

  /**
   * If we're currently firing an event, then we'll queue newly added handlers here, and they will
   * be added after the event is done being dispatched.
   */
  private val postponedHandlersToAdd = ArrayList<Pair<EventHandler<*>, Boolean>>()

  /**
   * If we're currently firing an event, then we'll queue handlers which have requested to be
   * removed here, and they will be removed after the event is done being dispatched.
   */
  private val postponedHandlersToRemove = HandlerCollection()

  /**
   * If we're currently firing an event, then we'll queue newly fired events here, and they will be
   * fired, in the order they were received, after the current event is done being dispatched.
   */
  private val postponedEvents = ArrayList<Event<*>>()

  /** A cache of the most recently fired event for a given event type. */
  private val eventCache = EventCache()

  /** Used to allow interaction with our cache prior to dispatching an event. */
  private val eventCacheControl: EventCacheControl = EventCacheControlImpl()

  /**
   * When we're actively firing an event, this will be set. Used to ensure that (a) handler
   * add/removes only happen after the event is fully dispatched; (b) events are dispatched in the
   * proper order.
   */
  private var inProgressEvent: Event<*>? = null

  /**
   * Adds the specified handler. If an event of this handler's type is currently being fired, the
   * newly added handler will NOT get this event.
   */
  @JvmOverloads
  public fun <E : Event<H>, H : EventHandler<E>> addHandler(
    handler: H,
    withCatchup: Boolean = false,
  ) {
    if (inProgressEvent == null) {
      actuallyAddHandler(handler, withCatchup)
    } else {
      synchronized(this) { postponedHandlersToAdd.add(Pair(handler, withCatchup)) }
    }
  }

  /**
   * Removes the specified handler. If an event of this handler's type is currently being fired,
   * this handler may still receive the event.
   */
  @Synchronized
  public fun <E : Event<H>, H : EventHandler<E>> removeHandler(handler: H) {
    if (inProgressEvent == null) {
      handlerCollection.get(handler.associatedType)?.remove(handler)
    } else {
      postponedHandlersToRemove.put(handler, handler.associatedType)
    }
  }

  public fun <H : EventHandler<*>, E : Event<H>> fire(event: E) {
    val inProgress = synchronized(this) { inProgressEvent != null }

    if (inProgress) {
      synchronized(this) { postponedEvents.add(event) }
    } else {
      fireEvent(event)
      handlePostponed()
    }
  }

  private fun <H : EventHandler<*>, E : Event<H>> fireEvent(event: E) {
    synchronized(this) { inProgressEvent = event }

    event.preDispatch(eventCacheControl)

    val handlers: List<H> =
      synchronized(this) {
        handlerCollection.get(event.javaClass)?.let { ArrayList(it) } ?: emptyList()
      }

    for (handler in handlers) {
      event.dispatch(handler)
    }

    synchronized(this) {
      if (event.isCachable()) {
        eventCache.put(event.javaClass, event)
      }

      inProgressEvent = null
    }
  }

  private fun handlePostponed() {
    handlePostponedAdds()
    handlePostponedRemoves()
    handlePostponedFiring()

    // Have to go around again...
    if (
      postponedHandlersToAdd.isNotEmpty() ||
        !postponedHandlersToRemove.isEmpty() ||
        postponedEvents.isNotEmpty()
    ) {
      handlePostponed()
    }
  }

  private fun handlePostponedAdds() {
    val copy =
      synchronized(this) {
        ArrayList(postponedHandlersToAdd).also { postponedHandlersToAdd.clear() }
      }

    for ((handler, withCatchup) in copy) {
      actuallyAddHandler(handler, withCatchup)
    }
  }

  @Synchronized
  private fun handlePostponedRemoves() {
    handlerCollection.removeAll(postponedHandlersToRemove)
    postponedHandlersToRemove.clear()
  }

  private fun handlePostponedFiring() {
    // Have to take a copy in case during the firing of one of these events we get more events to
    // postpone...
    val copyOfPostponedEvents =
      synchronized(this) { ArrayList(postponedEvents).also { postponedEvents.clear() } }

    for (event in copyOfPostponedEvents) {
      fireEvent(event)
    }
  }

  // The postponed collections hold handlers whose event types were erased when they were queued;
  // the
  //  bus only ever pairs a handler with the event type it reports, so re-establishing the type is
  // safe.
  @Suppress("UNCHECKED_CAST")
  private fun actuallyAddHandler(handler: EventHandler<*>, withCatchup: Boolean) {
    val eventType = handler.associatedType as Class<Event<EventHandler<*>>>

    synchronized(this) { handlerCollection.put(handler, eventType) }

    if (withCatchup) {
      eventCache.get(eventType)?.dispatch(handler)
    }
  }

  private inner class EventCacheControlImpl : EventCacheControl {
    override fun clearCacheForType(type: Class<out Event<*>>) {
      synchronized(this@EventBus) { eventCache.remove(type) }
    }
  }

  /**
   * Type-safe heterogeneous container, associating an event type with the handlers for that type.
   *
   * See Effective Java, 2nd ed.
   */
  private class HandlerCollection {

    /**
     * The actual, non-type safe map. The keys are the event class, and the values are a list of
     * handlers for that event type.
     */
    private val handlerMap = HashMap<Class<out Event<*>>, MutableList<EventHandler<*>>>()

    fun <H : EventHandler<*>, E : Event<H>> put(handler: H, eventType: Class<E>) {
      handlerMap.getOrPut(eventType) { ArrayList() }.add(handler)
    }

    // This is OK because we're the only ones with access to put things into the map...
    @Suppress("UNCHECKED_CAST")
    fun <H : EventHandler<*>, E : Event<H>> get(eventType: Class<E>): MutableList<H>? =
      handlerMap[eventType] as MutableList<H>?

    fun clear() {
      handlerMap.clear()
    }

    fun isEmpty(): Boolean = handlerMap.isEmpty()

    fun removeAll(source: HandlerCollection) {
      for ((eventType, handlersToRemove) in source.handlerMap) {
        handlerMap[eventType]?.removeAll(handlersToRemove)
      }
    }
  }

  /**
   * Type-safe heterogeneous container, associating an event type with the most recent event for
   * that type.
   *
   * See Effective Java, 2nd ed.
   */
  private class EventCache {

    /**
     * The actual, non-type safe map. The keys are the event class, and the value is the most recent
     * event for that event type.
     */
    private val eventCache = HashMap<Class<out Event<*>>, Event<*>>()

    fun <H : EventHandler<*>, E : Event<H>> put(eventType: Class<E>, cache: E) {
      eventCache[eventType] = cache
    }

    // This is OK because we're the only ones with access to put things into the map...
    @Suppress("UNCHECKED_CAST")
    fun <H : EventHandler<*>, E : Event<H>> get(eventType: Class<E>): E? =
      eventCache[eventType] as E?

    fun remove(eventType: Class<out Event<*>>) {
      eventCache.remove(eventType)
    }
  }
}
