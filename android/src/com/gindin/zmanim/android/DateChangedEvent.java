/*
 * Copyright (c) 2012. Jay R. Gindin
 */

package com.gindin.zmanim.android;

import com.gindin.zmanim.calendar.HebrewDate;

import java.util.EventListener;
import java.util.EventObject;

public class DateChangedEvent
	extends EventObject {


	public DateChangedEvent( HebrewDate newDate ) {
		super( newDate );
	}


	@Override
	public HebrewDate getSource() {
		return (HebrewDate)super.getSource();
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
