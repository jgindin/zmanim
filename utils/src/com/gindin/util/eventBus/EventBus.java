/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.util.eventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Roughly based on the event bus from GWT.
 */
public class EventBus {

	/**
	 * The registered handlers, keyed by the event they're interested in.
	 */
	private final HandlerCollection                           handlerCollection = new HandlerCollection();

	private final HandlerCollection                           postponedHandlersToAdd = new HandlerCollection();

	private final HandlerCollection                           postponedHandlersToRemove = new HandlerCollection();

	private final List<Event<?>>                              postponedEvents = new ArrayList<Event<?>>();

	/**
	 * When we're actively firing an event, this will be set. Used to ensure that (a) handler add/removes only happen
	 *  after the event is fully dispatched; (b) events are dispatched in the proper order.
	 */
	private Event<?>                                          inProgressEvent = null;


	public <E extends Event<H>, H extends EventHandler<E>> void addHandler( H handler ) {

		if ( null == inProgressEvent ) {
			handlerCollection.put( handler, handler.getAssociatedType() );
		}
		else {
			postponedHandlersToAdd.put( handler, handler.getAssociatedType() );
		}
	}


	public <E extends Event<H>, H extends EventHandler<E>> void removeHandler( H handler ) {

		if ( null == inProgressEvent ) {
			List<H> handlers = handlerCollection.get( handler.getAssociatedType() );
			handlers.remove( handler );
		}
		else {
			postponedHandlersToRemove.put( handler, handler.getAssociatedType() );
		}
	}


	public <H extends EventHandler, E extends Event<H>> void fire( E event ) {

		if ( null == inProgressEvent ) {

			fireEvent( event );
			handlePostponed();
		}
		else {
			postponedEvents.add( event );
		}
	}


	private <H extends EventHandler, E extends Event<H>> void fireEvent( E event ) {

		inProgressEvent = event;

		List<H> handlers = handlerCollection.get( event.getClass() );
		if ( null != handlers ) {
			for ( H handler : handlers ) {
				dispatchEvent( event, handler );
			}
		}

		inProgressEvent = null;
	}


	private void handlePostponed() {

		// First, add/remove handlers...
		handlerCollection.putAll( postponedHandlersToAdd );
		postponedHandlersToAdd.clear();

		handlerCollection.removeAll( postponedHandlersToRemove );
		postponedHandlersToRemove.clear();


		// Have to take a copy in case during the firing of one of these events we get more events to postpone...
		ArrayList<Event<?>> copyOfPostponedEvents = new ArrayList<Event<?>>( postponedEvents );
		postponedEvents.clear();
		for ( Event<?> event : copyOfPostponedEvents ) {
			fireEvent( event );
		}


		// Have to go around again...
		if ( !postponedHandlersToAdd.isEmpty() || !postponedHandlersToRemove.isEmpty() || !postponedEvents.isEmpty() ) {
			handlePostponed();
		}
	}


	private <H extends EventHandler, E extends Event<H>> void dispatchEvent( E event, H handler ) {

		try {
			event.dispatch( handler );
		}
		catch ( Exception e ) {
			// TODO: Need some way of logging this problem...
			System.out.println( e.getMessage() );
		}
	}


	/**
	 * Type-safe heterogeneous container. See Effective Java, 2nd ed.
	 */
	private static class HandlerCollection {

		/**
		 * The actual, non-type safe map. The keys are the event class, and the values are a list of handlers for that
		 *  event type.
		 */
		private final Map<Class<?>, List<? extends EventHandler>>   handlerMap = new HashMap<Class<?>, List<? extends EventHandler>>();


		public <H extends EventHandler, E extends Event<H>> void put( H handler, Class<E> eventType ) {

			// This is OK because we're the only ones with access to put things into the map...
			@SuppressWarnings( "unchecked" )
			List<H> handlers = (List<H>)handlerMap.get( eventType );
			if ( null == handlers ) {
				handlers = new ArrayList<H>();
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

		public void putAll( HandlerCollection source ) {
			handlerMap.putAll( source.handlerMap );
		}


		public void removeAll( HandlerCollection source ) {

			for ( Map.Entry<Class<?>, List<? extends EventHandler>> entry : source.handlerMap.entrySet() ) {

				List<? extends EventHandler> handlers = handlerMap.get( entry.getKey() );
				if ( null != handlers ) {
					handlers.removeAll( entry.getValue() );
				}
			}
		}
	}


} // End of EventBus class
