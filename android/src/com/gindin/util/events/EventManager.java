/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.util.events;

import com.gindin.util.Version;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Encapsulates the common behavior found in creating and managing a set of even listeners.
 *
 * NOTE: I explicitly chose not to use reflection and Java proxies at this time, fearing for performance, though
 *  I admit that this is not based on actual tests, just some educated guesses.
 */
public abstract class EventManager<EL extends EventListener, T, E extends AbstractEvent<T>> {

	private List<EL>                                          listeners;


	/**
	 * Adds the specified listener for these events.
	 *
	 * @return true if this was the first listener added. (Note...may not be the very first, as it's possible
	 *  that some other listener(s) were added and then all removed.)
	 */
	public boolean addListener( EL listener ) {
		boolean isFirstListener;

		if ( null == listeners ) {
			listeners = new ArrayList<EL>();
			isFirstListener = true;
		}
		else {
			isFirstListener = false;
		}

		if ( Version.isDevelopment() ) {
			if ( listeners.contains( listener ) ) {
				throw new IllegalStateException( "listener already listening to " + getClass() );
			}
		}

		listeners.add( listener );

		return isFirstListener;
	}


	/**
	 * Removes the specified listener.
	 *
	 * @return true if there are no listeners remaining; false otherwise.
	 */
	public boolean removeListener( EL listener ) {

		if ( Version.isDevelopment() ) {
			if ( !listeners.contains( listener ) ) {
				throw new IllegalStateException( "listener NOT listening to " + getClass() );
			}
		}

		listeners.remove( listener );

		return listeners.isEmpty();
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


	public void progress( String message ) {

		if ( ( null == listeners ) || listeners.isEmpty() ) {
			return;
		}

		List<EL> copy = new ArrayList<EL>( listeners );
		try {
			for ( EL listener : copy ) {
				if ( listener instanceof ProgressAwareEventListener ) {
					((ProgressAwareEventListener)listener).onProgress( message );
				}
			}
		}
		catch ( Exception e ) {
			handleException( e );
		}

	}


	/**
	 * Call when it is known that this manager is done and can be completely cleaned up.
	 *
	 * @return true if all the listeners had already been removed; false if some listeners remained.
	 */
	public boolean destroy() {

		if ( !listeners.isEmpty() ) {

			if ( Version.isDevelopment() ) {
				throw new IllegalStateException( "Still have: " + listeners.size() + " listeners from " + getClass() );
			}

			// For production, go ahead and do the necessary cleanup...
			List<EL> copy = new ArrayList<EL>( listeners );
			for ( EL listener : copy ) {
				removeListener( listener );
			}

			return false;
		}

		return true;
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
