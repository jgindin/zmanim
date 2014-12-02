package com.gindin.zmanim.android;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;

/**
 * Fired by the Zmanim service after it has been started by the main Activity.
 */
public class ServiceStartedEvent
	extends Event<ServiceStartedEvent.Handler> {

	public static abstract class Handler implements EventHandler<ServiceStartedEvent> {

		public abstract void onServiceStarted();


		@Override
		public Class getAssociatedType() {
			return ServiceStartedEvent.class;
		}
	}


	@Override
	public void dispatch( Handler handler ) {
		handler.onServiceStarted();
	}
}
