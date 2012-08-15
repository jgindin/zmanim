/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.util;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
 * Encapsulates the common behavior found in creating and managing a set of even listeners.
 *
 * NOTE: I explicitly chose not to use reflection and Java proxies at this time, fearing for performance, though
 *  I admit that this is not based on actual tests, just some educated guesses.
 */
public abstract class EventManager<EL extends EventListener, E extends EventObject> {

	private List<EL>                                          listeners;


	public void addListener( EL listener ) {
		if ( null == listeners ) {
			listeners = new ArrayList<EL>();
		}

		listeners.add( listener );
	}

	public void removeListener( EL listener ) {
		listeners.remove( listener );
	}

	public void fire( E event ) {

		if ( ( null == listeners ) || listeners.isEmpty() ) {
			return;
		}

		List<EL> copy = new ArrayList<EL>( listeners );
		try {
			for ( EL listener : copy ) {
				dispatch( listener, event );
			}
		}
		catch ( Exception e ) {
			handleException( e );
		}
	}


	/**
	 * Override to invoke the onFoo method of the listener.
	 */
	protected abstract void dispatch(
		EL  listener,
		E   event
	);


	/**
	 * Override to handle any exception thrown during firing of the event.
	 */
	protected abstract void handleException( Exception e );

}  // End of EventManager class
