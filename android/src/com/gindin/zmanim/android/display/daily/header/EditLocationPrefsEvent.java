/*
 * Copyright (c) 2014. Jay R. Gindin
 */

package com.gindin.zmanim.android.display.daily.header;

import com.gindin.eventBus.Event;
import com.gindin.eventBus.EventHandler;

/**
 * Fired when someone detects that the user wants to edit their location preferences.
 */
public class EditLocationPrefsEvent
	extends Event<EditLocationPrefsEvent.Handler> {

	public static abstract class Handler implements EventHandler<EditLocationPrefsEvent> {

		public abstract void onEditLocationPrefs();


		@Override
		public Class<EditLocationPrefsEvent> getAssociatedType() {
			return EditLocationPrefsEvent.class;
		}
	}


	@Override
	public boolean isCachable() {
		return false;
	}


	@Override
	public void dispatch( Handler handler ) {
		handler.onEditLocationPrefs();
	}

} // End of EditLocationPrefsEvent class
