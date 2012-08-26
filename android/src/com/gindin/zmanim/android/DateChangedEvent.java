/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.util.events.AbstractEvent;
import com.gindin.zmanim.calendar.HebrewDate;

import java.util.EventListener;

public class DateChangedEvent
	extends AbstractEvent<HebrewDate> {


	public DateChangedEvent( HebrewDate newDate ) {
		super( newDate );
	}


	public static interface HasListeners {
		void addDateChangeListener( Listener listener );
		void removeDateChangeListener( Listener listener );
	}


	public static interface Listener
		extends EventListener {

		void onDateChanged( DateChangedEvent event );
	}

}  // End of DateChangedEvent class
